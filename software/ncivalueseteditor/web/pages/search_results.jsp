
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.beans.*" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.beans.ValueSetBean.ComponentObject" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.properties.*" %>


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
  private static Logger _logger = Utils.getJspLogger("search_results.jsp");
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

//JSPUtils.JSPHeaderInfo info = new JSPUtils.JSPHeaderInfo(request);
//String search_results_dictionary = info.dictionary;

String browser_url = ApplicationProperties.getNCITurl();

System.out.println("NCIt URL: " + browser_url);


String search_results_dictionary = request.getParameter("dictionary");
if (search_results_dictionary == null) {
   request.getSession().getAttribute("dictionary");
   
   
}

boolean isMapping = DataUtils.isMapping(search_results_dictionary, null);
boolean isExtension = DataUtils.isExtension(search_results_dictionary, null);

String search_results_version = null;//info.version;

/*
HashMap hmap = DataUtils.getNamespaceId2CodingSchemeFormalNameMapping();
HashMap name_hmap = new HashMap();
*/
String vocabulary_name = null;
String short_vocabulary_name = null;
String coding_scheme_version = null;

String key = (String) request.getSession().getAttribute("key");
if (key == null) {
    key = HTTPUtils.cleanXSS((String) request.getParameter("key"));
}

_logger.debug("search_results.jsp dictionary: " + search_results_dictionary);
_logger.debug("search_results.jsp version: " + search_results_version);


key = (String) request.getSession().getAttribute("key");

System.out.println("key: " + key);


String resultsPerPage = HTTPUtils.cleanXSS((String) request.getParameter("resultsPerPage"));
if (resultsPerPage == null) {
    resultsPerPage = (String) request.getSession().getAttribute("resultsPerPage");
    if (resultsPerPage == null) {
       resultsPerPage = "50";
    }
} else {
    request.getSession().setAttribute("resultsPerPage", resultsPerPage);
}

String selectedResultsPerPage = resultsPerPage;

_logger.debug("search_result.jsp " + key);
request.setAttribute("key", key);

          IteratorBeanManager iteratorBeanManager = (IteratorBeanManager) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("iteratorBeanManager");



          IteratorBean iteratorBean = iteratorBeanManager.getIteratorBean(key);
          

    if (iteratorBean == null){
      _logger.warn("iteratorBean NOT FOUND???" + key);
      System.out.println("iteratorBean NOT FOUND???" + key);
    }
String matchText = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("matchText"));
 
int pageNum = 0; 
int pageSize = Integer.parseInt( resultsPerPage );
int size = iteratorBean.getSize();    
List list = null;
int num_pages = size / pageSize;
if (num_pages * pageSize < size) num_pages++;
System.out.println("num_pages: " + num_pages);

String page_number = HTTPUtils.cleanXSS((String) request.getParameter("page_number"));
if (page_number != null) {
    pageNum = Integer.parseInt(page_number);
}
System.out.println("pageNum: " + pageNum);
int istart = pageNum * pageSize;
int page_num = pageNum;
if (page_num == 0) {
    page_num++;
} else {
    istart = (pageNum-1) * pageSize;
}
int iend = istart + pageSize - 1;


try {
   list = iteratorBean.getData(istart, iend);
   int prev_size = size;
   size = iteratorBean.getSize();
   
System.out.println( "(*) prev_size: " + prev_size);
System.out.println( "(*) size: " + size);


   if (size != prev_size) {
	if (iend > size) {
	    iend = size;

	}
       list = iteratorBean.getData(istart, size);
       
   } else {

	if (iend > size) {
	    iend = size;

	}

   }
} catch (Exception ex) {
   System.out.println("ERROR: bean.getData throws exception??? istart: " + istart + " iend: " + iend);
}


num_pages = size / pageSize;
if (num_pages * pageSize < size) num_pages++;

int istart_plus_pageSize = istart+pageSize;


String istart_str = Integer.toString(istart+1);    
String iend_str = new Integer(iend).toString();

if (iend >= istart+pageSize-1) {
    iend = istart+pageSize-1;
    list = iteratorBean.getData(istart, iend);
    iend_str = new Integer(iend+1).toString();
}

String match_size = new Integer(size).toString();
   

          String contains_warning_msg = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("contains_warning_msg"));
          int next_page_num = page_num + 1;
          int prev_page_num = page_num - 1;
          String prev_page_num_str = Integer.toString(prev_page_num);
          String next_page_num_str = Integer.toString(next_page_num);



    String message = null;
    if (list.size() == 0) {
        message = "No match found.";    
    }

    
    boolean timeout = iteratorBean.getTimeout();
    if (timeout) {
      %>
      <p class="textbodyred">WARNING: System times out. Please advance fewer pages at one time.</p>
      <%
    } else if (message != null) {
    %>
      <p class="textbodyred"><%=message%></p>
    <%
    } else {

        %>
<!--
        <table width="700px">
-->        
        <table border="0" width="95%">
        
          <tr>
            <table>
              <tr>
                <td class="texttitle-blue">Result for:</td>
                <td class="texttitle-gray"><%=matchText%></td>
              </tr>
            </table>
          </tr>
          <tr>
            <td><hr></td>
          </tr>
          <tr>
            <td>
               <%
               if (contains_warning_msg != null) {
               %>
                  <b>Results <%=istart_str%>-<%=iend_str%> of&nbsp;<%=match_size%></b>&nbsp;<%=contains_warning_msg%>
               <%
               } else {
               %>
                  <b>Results <%=istart_str%>-<%=iend_str%> of&nbsp;<%=match_size%></b>
               <%
               }
               %>
            </td>
          </tr>
          <tr>
            <td class="textbody">
            <!--
              <table class="dataTable" summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
             -->
             <table class="dataTable" summary="" cellpadding="3" cellspacing="0" border="0" width="95%">
                <th class="dataTableHeader" scope="col" align="left">Concept</th>
                <th class="dataTableHeader" scope="col" align="left">Vocabulary</th>

                <%
                  
HashMap concept_status_hmap = DataUtils.getPropertyValuesInBatch(list, "Concept_Status");
                  
                  
                  int i = -1;

                  for (int k=0; k<list.size(); k++) {
                      Object obj = list.get(k);
                      ResolvedConceptReference rcr = null;

if (obj == null) {
   _logger.warn("rcr == null???");
} else {
   rcr = (ResolvedConceptReference) obj;
}

                      if (rcr != null) {
                      String code = rcr.getConceptCode();
                      coding_scheme_version = rcr.getCodingSchemeVersion();


                      String name = "null";
                      if (rcr.getEntityDescription() != null) {
                          name = rcr.getEntityDescription().getContent();
                      } else {
      Entity entity = SearchUtils.getConceptByCode(rcr.getCodeNamespace(),
           null, null, rcr.getConceptCode());
      name = entity.getEntityDescription().getContent();


                      }
                      if (code == null || code.indexOf("@") != -1) {
                          i++;
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
             <%=name%>
          </td>
        </tr>
          <%
                      }

                      else if (code != null && code.indexOf("@") == -1) {
                          i++;
                          

        String con_status = (String) concept_status_hmap.get(rcr.getCodingSchemeName() + "$" + rcr.getCodingSchemeVersion()
               + "$" + code);
        
        //if (status_vec != null && status_vec.elementAt(i) != null) {
        //   con_status = (String) status_vec.elementAt(i);
        if (con_status != null) {
           con_status = con_status.replaceAll("_", " ");
        }
              


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
               String url = browser_url + "/ConceptReport.jsp?dictionary=" + search_results_dictionary 
                                        + "&version=" + search_results_version
                                        + "&code=" + code;


          if (con_status == null) {
          %>
               <a href="javascript:openQuickLinkSite('<%=url%>')"><%=name%></a>
          <%
          } else {
          %>
               <a href="javascript:openQuickLinkSite('<%=url%>')"><%=name%></a>&nbsp;(<%=con_status%>)
          <%
          }
          %>
          </td>

<%
   short_vocabulary_name = rcr.getCodingSchemeName();
%>
            <td class="dataCellText">
           <%=short_vocabulary_name%>
            </td>

        </tr>
            <%
                        }
                     }
                  }

                %>
              </table>
            </td>
          </tr>
        </table>

               <%
               }
               %>

        <%@ include file="/pages/templates/pagination.jsp" %>
        
 
 
<form> 
<table><tr><td>

<!--
 <h:commandButton
 	id="Save"
 	value="Save"
 	action="#{ComponentBean.saveComponentSubsetAction}" 
 	image="#{form_requestContextPath}/images/save.gif" alt="Save component subset data">
 </h:commandButton>
 &nbsp; 
-->

 <h:commandButton
	id="Cancel"
	value="Cancel"
	action="#{ComponentBean.cancelComponentSubsetAction}"
	image="#{form_requestContextPath}/images/close.gif" alt="Cancel creating component subset">
 </h:commandButton>
</td></tr></table> 
</form> 
        
        
		           
		      <%@ include file="/pages/include/footer.jsp" %>
		  </div>
	</div>
        <div class="mainbox-bottom"><img src="<%= request.getContextPath() %>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    </div>
   <br/> 
</f:view>    
</body>
</html>