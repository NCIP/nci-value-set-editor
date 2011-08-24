<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.beans.*" %>

<%@ page import="gov.nih.nci.evs.valueseteditor.beans.ValueSetBean.ComponentObject" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.beans.ValueSetBean.ValueSetObject" %>

<%@ page import="java.util.*" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.utilities.*" %>

<%@ page import="javax.faces.model.SelectItem" %>

<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.apache.log4j.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns:c="http://java.sun.com/jsp/jstl/core">

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>NCI ValueSet Editor</title>
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
	          <%@ include file="/pages/include/applicationBanner.jsp" %>
	          <%@ include file="/pages/include/quickLinks.jsp" %>
		  <div class="pagecontent">
		      <%@ include file="/pages/include/navBar.jsp" %>


	
		      
     <h:form id="valueSetFormId">
     
      <table border="0" width="80%">
      <tr>
       <td align="left"><font size="4"><b>Copy Value Set</b></font></td>

      </tr>
      </table>
      <hr></hr>
  
      <%
      String message = (String) request.getAttribute("message");
      String requestContextPath = request.getContextPath();
      
      String uri_value = (String) request.getAttribute("uri_value");
      if(uri_value == null) uri_value = "";
      
      if (message != null) {
          request.removeAttribute("message");
      }    
      if (message != null && message.compareTo("null") != 0) {
       %>
	  <p class="textbodyred"><%=message%></p>
       <%
       }
      %>   
             
             
      
     <table border="0" width="80%">

      <tr>
	<td align="right" class="inputLabel">
	      URI:
	</td>      
        <td><input CLASS="searchbox-input" name="uri" value="<%=uri_value%>" size="75" tabindex="2"></td>
        
      </tr>

          
     <tr>
 
 	 <td align="right" class="inputLabel">
 	     Copy From Value Set:
 	 </td>                   
 
 	 <td class="inputItem">
 	 <select id="selectValueSetReference" name="selectValueSetReference" size="1" tabindex="6">
 
 	   <%
	   
 	     Vector item_vec = DataUtils.getValueSetDefinitions();
 	     String selectValueSetReference = (String) request.getAttribute("selectValueSetReference");
 	     
 	     
 	     if (selectValueSetReference == null) {
 		      SelectItem item = (SelectItem) item_vec.elementAt(0);
 		      selectValueSetReference = (String) item.getLabel();
 	     }
	     
 	     if (item_vec != null) {
 		    for (int i=0; i<item_vec.size(); i++) {
 		      SelectItem item = (SelectItem) item_vec.elementAt(i);
 		      String key = (String) item.getLabel();
 		      String value = (String) item.getValue();
 		      if (value != null && value.compareTo(selectValueSetReference) == 0) {
 		  %>
 			<option value="<%=value%>" selected><%=key%></option>
 		  <%  } else { %>
 			<option value="<%=value%>"><%=key%></option>
 		  <%
 		      }
 		    }
 	     }
 	   %>
 	   </select>
 
 	 </td>
      </tr>     
     

      <tr><td>&nbsp;&nbsp;</td></tr> 


    </table>

      <tr><td>&nbsp;&nbsp;</td></tr> 

      <tr>
          <td align="right" >&nbsp;</td>

	     <h:commandButton
		value="Save" action="#{ValueSetBean.saveCopyAction}"
		onclick="javascript:cursor_wait();"
		image="#{requestContextPath}/images/continue.gif" alt="Save a copy" />

	 &nbsp; 

	     <h:commandButton
		value="Save" action="#{ValueSetBean.cancelCopyAction}"
		image="#{requestContextPath}/images/cancel.gif" alt="Cancel" />
	

	  </td>

       </tr>
       
<input type="hidden" id="tab" name="tab" value="valueset" /> 
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

     
