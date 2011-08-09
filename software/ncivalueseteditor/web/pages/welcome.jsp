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
		      
		      
		      
	<table cellpadding="0" cellspacing="0" border="0" width="715">
	  <tr>
	    <td align="left"><font size="4"><b>WELCOME TO NCI EVS Value Set Editor</b></font></td>
	  </tr>
	</table>  
	<hr />
	
	
	<table cellpadding="0" cellspacing="0" border="0" width="715" height="280">  
	  <tr>
	    <td class="welcomeContent" valign="top">
		  The NCI EVS Value Set Editor is a web-based tool that will allow users to generate
		  LexGrid value set definitions from hosted data source and to export them for later use.
	      <br/><br/>
	      <b>
	      No user authentication is required.
	      </b>
	    </td>
	  </tr>
	</table>
		           
		           
		      <%@ include file="/pages/include/footer.jsp" %>
		  </div>
	</div>
        <div class="mainbox-bottom"><img src="<%= request.getContextPath() %>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    </div>
   <br/> 
</f:view>    
</body>
</html>

