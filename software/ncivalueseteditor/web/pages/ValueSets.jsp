<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.apache.log4j.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns:c="http://java.sun.com/jsp/jstl/core">

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>NCI Value Set Editor</title>
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
  <script type="text/javascript">
    function newPopup(url) {    	  
    	  var centerWidth = (window.screen.width - 500) / 2;
        var centerHeight = (window.screen.height - 400) / 2;
        window.open(url,'_blank',
        	   'top=' + centerHeight +
        	   ',left=' + centerWidth +
        	   ', height=200, width=300, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');
    }
  </script>
</head>
<body>
<f:view>
    <%@ include file="/pages/include/header.jsp" %>
    <div class="center-page">
        <%@ include file="/pages/include/subHeader.jsp" %>

        <div class="mainbox-top"><img src="<%= request.getContextPath() %>/images/mainbox-top.gif" width="745" height="5" alt="Mainbox Top" /></div>
	<div id="main-area">
	          <%@ include file="/pages/include/applicationBannerNoHome.jsp" %>
	          <%@ include file="/pages/include/quickLinks.jsp" %>
	          
	          
		  <div class="pagecontent">
 


	<table cellpadding="0" cellspacing="0" border="0" width="715">
	  <tr>
	    <td align="left"><font size="4"><b>WELCOME TO NCI EVS Value Set Editor</b></font></td>
	  </tr>
	</table>  
	<hr />
	
		      
             <%
             String warning_msg= (String) request.getAttribute("message");
             if (warning_msg != null && warning_msg.compareTo("null") != 0) {
                 request.removeAttribute("message");
             %>
                <p class="textbodyred"><%=warning_msg%></p>
             <%
             }
             %>		      
		      
	<h:form id="valueSetFormId">
		<table summary="" border="0" width="96%">
			<tr>
				<td align="left"><font size="4"><b>Value Set List</b></font></td>
				<td align="right" valign="bottom">&#xA0;&#xA0;
				
				      <h:commandButton
					 value="Add to cart" action="#{ValueSetBean.newValueSetAction}"
					 onclick="javascript:cursor_wait();"
					 image="#{requestContextPath}/images/add.gif" alt="Create a new value set definition" />
					&#xA0;&#xA0;
				      <h:commandButton
					value="Copy" action="#{ValueSetBean.copyFromServerAction}"
					onclick="javascript:cursor_wait();"
					image="#{requestContextPath}/images/copy.gif" alt="Copy value set definition from server" />

					&#xA0;&#xA0;
				      <h:commandButton
					value="Remove from cart" action="#{ValueSetBean.removeFromCartAction}"
					onclick="return confirm('Are you sure you want to remove?')"
					rendered="#{ValueSetBean.isNotEmpty}"
					image="#{requestContextPath}/images/remove.gif" alt="Remove value set definition" />
					
				</td>
			</tr>
		</table>
		<hr />
		<table class="dataTable" summary="" cellpadding="3" cellspacing="0"
			border="0" width="96%">
			<tr height="15px">
				<th class="dataTableHeader" scope="col" align="left" width="20px">&#xA0;</th>
				<th class="dataTableHeader" scope="col" align="left">URI</th>
				<th class="dataTableHeader" scope="col" align="left">Description</th>
				<th class="dataTableHeader" scope="col" align="left">Action</th>
			</tr>
			<c:choose>
				<c:when test="${sessionScope.ValueSetBean.count>0}">
				
				<%
				int lcv = 0;
				%>
				
					<c:forEach var="item" begin="0" items="#{ValueSetBean.valueSetList}" varStatus="status">
					
				
				<%
				    if (lcv %2 == 0) {
				%>    
				        <tr class="dataRowLight">
				<%        
				    } else {
				%>
				        <tr class="dataRowDark">
				<%        
				    }
				    lcv++;
				%>

							<td>
                        <h:selectBooleanCheckbox binding="#{item.checkbox}"
								    onclick="submit();" />
                     </td>
		     <td>
                        <h:outputText value="#{item.uri}" />
                     </td>
		     <td>&#xA0;&#xA0;</td>
		     <td width="110px">
							
                        <h:commandLink action="#{ValueSetBean.editValueSetAction}" value="Edit">
                             <f:param name="uri" value="#{item.uri}" />
                        </h:commandLink>      

                        &#xA0; 

                        <h:commandLink action="#{ValueSetBean.resolveVSDAction}" value="Resolve"
                             rendered="#{item.isNotEmpty}">
                             <f:param name="uri" value="#{item.uri}" />
                             <f:param name="expression" value="#{item.expression}" />
                        </h:commandLink>      
                        &#xA0; 
                        
                        <h:commandLink action="#{ValueSetBean.exportVSDToXMLAction}" value="Export"
                              rendered="#{item.isNotEmpty}">  
                              <f:param name="uri" value="#{item.uri}" />
                              <f:param name="expression" value="#{item.expression}" />
                        </h:commandLink> 

                        
                     </td>
						</tr>
						
					</c:forEach>
					
				</c:when>
				<c:otherwise>
					<tr valign="top">
						<td colspan="4">No value set is available.</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</table>
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



