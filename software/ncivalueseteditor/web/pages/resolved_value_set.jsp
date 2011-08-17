<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.beans.*" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.beans.ValueSetBean.ComponentObject" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.beans.ValueSetBean.ValueSetObject" %>

<%@ page import="gov.nih.nci.evs.valueseteditor.properties.*" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.utilities.*" %>
<%@ page import="org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator" %>

<%@ page import="java.io.*" %>
<%@ page import="java.util.*"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Entity" %>


<%@ page import="org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="org.apache.log4j.*" %>

<%@ page import="gov.nih.nci.evs.valueseteditor.utilities.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns:c="http://java.sun.com/jsp/jstl/core">

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>NCI ValueSet Editor</title>
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
 
 <script type="text/javascript">
 function openQuickLinkSite(url) {
     if (url != "#")
     {
         window.open (url, "", "alwaysRaised,dependent,status,scrollbars,resizable,width=800,height=600"); 
     }
 }
 </script>
 
</head>
<body>

<%!
  private static Logger _logger = Utils.getJspLogger("resovled_value_set.jsp");
%>
   
<f:view>
    <%@ include file="/pages/include/header.jsp" %>
    <div class="center-page">
        <%@ include file="/pages/include/subHeader.jsp" %>

        <div class="mainbox-top"><img src="<%= request.getContextPath() %>/images/mainbox-top.gif" width="745" height="5" alt="Mainbox Top" /></div>
	<div id="main-area">
	          <%@ include file="/pages/include/applicationBanner.jsp" %>
	          <%@ include file="/pages/include/quickLinks.jsp" %>
		  <div class="pagecontent">
		      <%@ include file="/pages/include/navBar.jsp" %>
		      
            <%
            
String cs_ref_key = (String) request.getAttribute("cs_ref_key"); 
if (cs_ref_key == null) cs_ref_key = "";

System.out.println("(*) resolved_value_set.jsp cs_ref_key: " + cs_ref_key);

            
            		String browser_url = ApplicationProperties.getNCITurl();

                        Vector u = null;
            		int numRemaining = 0;
            		String valueSetSearch_requestContextPath = request
            				.getContextPath();

            		String message = (String) request.getSession().getAttribute("message");
            		request.getSession().removeAttribute("message");
            		
            		
            		ValueSetObject vs_obj = (ValueSetObject) request.getSession().getAttribute("value_set_object");

			String valueset_uri = vs_obj.getUri();
			String name = vs_obj.getName();
			String description = vs_obj.getDescription();
			String concept_domain = vs_obj.getConceptDomain();
			String sources = vs_obj.getSources();

            		IteratorBeanManager iteratorBeanManager = (IteratorBeanManager) FacesContext
            				.getCurrentInstance().getExternalContext()
            				.getSessionMap().get("iteratorBeanManager");

            		if (iteratorBeanManager == null) {
            			iteratorBeanManager = new IteratorBeanManager();
            			request.getSession().setAttribute("iteratorBeanManager",
            					iteratorBeanManager);
            		}

            		String resolved_vs_key = (String) request.getSession()
            				.getAttribute("resolved_vs_key");
            				
            		IteratorBean iteratorBean = iteratorBeanManager
            				.getIteratorBean(resolved_vs_key);
            		if (iteratorBean == null) {
            			System.out.println("(*) iteratorBean with key "
            					+ resolved_vs_key + " NOT found.");

            			ResolvedConceptReferencesIterator itr = (ResolvedConceptReferencesIterator) request
            					.getSession().getAttribute(
            							"ResolvedConceptReferencesIterator");
            							
            			iteratorBean = new IteratorBean(itr);
            			iteratorBean.initialize();
            			iteratorBean.setKey(resolved_vs_key);
            			request.getSession().setAttribute("resolved_vs_key",
            					resolved_vs_key);
            			iteratorBeanManager.addIteratorBean(iteratorBean);

            			int itr_size = iteratorBean.getSize();
            			System.out.println("itr_size: " + itr_size);
            			Integer obj = new Integer(itr_size);
            			String itr_size_str = obj.toString();
            			System.out.println("itr_size_str: " + itr_size_str);
            			request.getSession().setAttribute("itr_size_str",
            					itr_size_str);

            		} else {
            			System.out.println("(*) iteratorBean with key "
            					+ resolved_vs_key + " found.");
            					
            			String match_size = (String) request.getSession().getAttribute("match_size");
            			System.out.println("match_size: " + match_size);
            			
            			int itr_size = iteratorBean.getSize();
            			
            			System.out.println("itr_size: " + itr_size);
            			Integer obj = new Integer(itr_size);
            			String itr_size_str = obj.toString();
            			System.out.println("itr_size_str: " + itr_size_str);

            		}

            		String resultsPerPage = (String) request
            				.getParameter("resultsPerPage");
            		if (resultsPerPage == null) {
            			resultsPerPage = (String) request.getSession()
            					.getAttribute("resultsPerPage");
            			if (resultsPerPage == null) {
            				resultsPerPage = "50";
            			}

            		} else {
            			request.getSession().setAttribute("resultsPerPage",
            					resultsPerPage);
            		}

            		String selectedResultsPerPage = resultsPerPage;

            		String page_number = HTTPUtils.cleanXSS((String) request
            				.getParameter("page_number"));
            		int pageNum = 0;

            		if (page_number != null) {
            			pageNum = Integer.parseInt(page_number);
            		} else {
            			pageNum = 1;
            		}

            		int page_num = pageNum;

            		int pageSize = Integer.parseInt(resultsPerPage);
            		iteratorBean.setPageSize(pageSize);
            		int size = iteratorBean.getSize();
            		numRemaining = size;

            		System.out.println("\npage_num: " + page_num);
            		System.out.println("size: " + size);
            		System.out.println("pageSize: " + pageSize);

            		int num_pages = size / pageSize;
            		if (num_pages * pageSize < size)
            			num_pages++;

            		System.out.println("num_pages: " + num_pages + "\n");

            		int istart = (page_num - 1) * pageSize;
            		if (istart < 0)
            			istart = 0;

            		int iend = istart + pageSize - 1;
            		if (iend > size)
            			iend = size - 1;
            %>

               
                  <div class="tabTableContentContainer">
                  
                  <h:form id="valueSetSearchResultsForm" styleClass="search-form">
                     <%
                     	if (message != null) {
                     %>
                     <p class="textbodyred">
                        &nbsp;<%=message%></p>
                     <%
                     	request.getSession().removeAttribute("message");
                     			} else {
                     %>
                     <table border="0">
                        <tr>
                           <td>
                             <table border="0" width="95%">
                             
                                 <tr>
                                    <td align="left" class="texttitle-blue"><b>Value Set</b>:&nbsp;<%=valueset_uri%></td>
                                    <td align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                    <td align="right">
                                       <h:commandLink
                                          value="Export XML"
                                          action="#{ValueSetBean.exportResolvedVSDToXMLAction}"
                                          styleClass="texttitle-blue-small"
                                          title="Export VSD in LexGrid XML format" />
                                       | <h:commandLink
                                          value="Export CSV"
                                          action="#{ValueSetBean.exportResolvedVSDToCSVAction}"
                                          styleClass="texttitle-blue-small"
                                          title="Export VSD in CSV format" />
                                    </td>
                                 </tr>
                              </table>
                           </td>
                        </tr>
                        <tr class="textbodyred">
                           <td></td>
                        </tr>
                        <tr class="textbody">
                           <td><b>Name</b>: <%=name%></td>
                        </tr>
                        <tr class="textbody">
                           <td><b>Description</b>: <%=description%></td>
                        </tr>
                        <tr class="textbody">
                           <td><b>Concept Domain</b>: <%=concept_domain%></td>
                        </tr>
                        <tr class="textbody">
                           <td><b>Sources</b>: <%=sources%></td>
                        </tr>
                        <tr class="textbody">
                           <td>&nbsp;</td>
                        </tr>
                        <tr class="textbody">
                           <td><b>Concepts</b>:</td>
                        </tr>
                        <tr class="textbody">
                           <td>
                              <table class="dataTable" summary="Data Table" cellpadding="3" cellspacing="0" border="0" style="width: 694px;">
                                 <th class="dataTableHeader" scope="col" align="left">Code</th>
                                 <th class="dataTableHeader" scope="col" align="left">Name</th>
                                 <th class="dataTableHeader" scope="col" align="left">Vocabulary</th>
                                 <th class="dataTableHeader" scope="col" align="left">Namespace</th>
                                 <%
                                 	Vector concept_vec = new Vector();
                                 				List list = iteratorBean.getData(istart, iend);
                                 				for (int k = 0; k < list.size(); k++) {
                                 					Object obj = list.get(k);
                                 					ResolvedConceptReference ref = null;
                                 					if (obj == null) {
                                 						_logger.warn("rcr == null???");
                                 					} else {
                                 						ref = (ResolvedConceptReference) obj;
                                 					}

                                 					String entityDescription = "<NOT ASSIGNED>";
                                 					if (ref.getEntityDescription() != null) {
                                 						entityDescription = ref.getEntityDescription()
                                 								.getContent();
                                 					}

                                 					concept_vec.add(ref.getConceptCode() + "|"
                                 							+ entityDescription + "|"
                                 							+ ref.getCodingSchemeName() + "|"
                                 							+ ref.getCodeNamespace() + "|"
                                 							+ ref.getCodingSchemeVersion());

                                 				}
                                 				for (int i = 0; i < concept_vec.size(); i++) {
                                 					String concept_str = (String) concept_vec
                                 							.elementAt(i);
                                 					u = DataUtils.parseData(concept_str);
                                 					String code = (String) u.elementAt(0);
                                 					String conceptname = (String) u.elementAt(1);
                                 					String coding_scheme = (String) u.elementAt(2);
                                 					String namespace = (String) u.elementAt(3);
                                 					String vsn = (String) u.elementAt(4);

                                 					if (i % 2 == 0) {
                                 %>
                                 <tr class="dataRowDark">
                                    <%
                                    	} else {
                                    %>
                                 
                                 <tr class="dataRowLight">
                                    <%
                                    	}
                                    %>
                                    <td class="dataCellText">
                                       <%
                                       	if (code.indexOf("@") == -1) {

               String url = browser_url + "/ConceptReport.jsp?dictionary=" + coding_scheme 
                                        + "&version=" + vsn
                                        + "&code=" + code;
                                       	
                                       %> 
                                       <a href="javascript:openQuickLinkSite('<%=url%>')"><%=code%></a>
                                       <%
                                        	} else {
                                       %> <%=code%> <%
                                        	}
                                       %>
                                    </td>
                                    <td class="dataCellText"><%=conceptname%></td>
                                    <td class="dataCellText"><%=coding_scheme%></td>
                                    <td class="dataCellText"><%=namespace%></td>
                                 <%
                                 	}
                                 %>
                                 </tr>
                              </table>
                           </td>
                        </tr>
                     </table>
                     <%
                     	}
                     %>
                     <input type="hidden" name="uri" id="uri" value="<%=valueset_uri%>">
                     <input type="hidden" name="cs_ref_key" id="cs_ref_key" value="<%=cs_ref_key%>">
                     <input type="hidden" name="referer" id="referer" value="<%=HTTPUtils.getRefererParmEncode(request)%>">
                     </h:form>
                                          
                     
                     
               </div> <!-- end tabTableContentContainer -->      
               <%
                  if (message == null) {
               %>
               <%@ include file="/pages/templates/pagination-resolved-valueset.jsp"%>
               <%
                  }
               %>
 
 
 <h:form>
 <table><tr><td> 
  <h:commandButton
 	id="Close"
 	value="Close"
 	action="#{ValueSetBean.closeAction}"
 	image="#{form_requestContextPath}/images/close.gif" alt="Close window">
  </h:commandButton>
 </td></tr></table> 
</h:form> 

               
               
		      <%@ include file="/pages/include/footer.jsp" %>
		  </div>
	</div>
        <div class="mainbox-bottom"><img src="<%= request.getContextPath() %>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    </div>
   <br/> 
</f:view>    
</body>
</html>