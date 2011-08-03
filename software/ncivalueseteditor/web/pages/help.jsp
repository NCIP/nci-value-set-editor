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
	          <%@ include file="/pages/include/applicationBanner.jsp" %>
	          <%@ include file="/pages/include/quickLinks.jsp" %>
		  <div class="pagecontent">
		      <%@ include file="/pages/include/menuBar.jsp" %>


        <!-- ======================================= -->
        <!--                 HELP CONTENT            -->
        <!-- ======================================= -->
        <div class="texttitle-blue">Help</div>
       
        <p class="textbody">
          <a href="#introduction">Introduction</a><br/>
          <a href="#homePage">NCI EVS Value Set Editor Home Page</a><br/>
          <a href="#home">Home Tab</a><br/>
          
          <%
          String indent = "&nbsp;&nbsp;&nbsp;&nbsp;";
          %>
          
          <a href="#value_set">Value Set Tab</a><br/>
          <%=indent%> <a href="#create_value_set">Create a New Value Set Definition</a><br/>
          <%=indent%><%=indent%> <a href="#enter_metadata">Enter Metadata</a><br/>
          <%=indent%><%=indent%> <a href="#modify_metadata">Modify Metadata</a><br/>
          <%=indent%><%=indent%> <a href="#create_component_subset">Create Component Subset</a><br/>
          <%=indent%><%=indent%> <a href="#edit_component_subset">Edit Component Subset</a><br/>
          <%=indent%><%=indent%> <a href="#edit_component_subset">Resolve Component Subset</a><br/>
          
          <%=indent%><%=indent%> <a href="#edit_component_subset">Create Value Set Expression</a><br/>
          <%=indent%><%=indent%> <a href="#edit_component_subset">Resolve Value Set Definition</a><br/>
          <%=indent%><%=indent%> <a href="#edit_component_subset">Export Value Set Definition</a><br/>
          
          <%=indent%> <a href="#copy_value_set">Copy a Value Set Definition from Server</a><br/>
          <%=indent%> <a href="#remove_value_set">Remove a Value Set Definition</a><br/>

          <a href="#additionalInformation">Additional Information</a><br/>
        </p>

        <%-- -------------------------------------------------------------- --%> 
        
        
 		           
		      <%@ include file="/pages/include/footer.jsp" %>
		  </div>
	</div>
        <div class="mainbox-bottom"><img src="<%= request.getContextPath() %>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    </div>
   <br/> 
</f:view>    
</body>
</html>

