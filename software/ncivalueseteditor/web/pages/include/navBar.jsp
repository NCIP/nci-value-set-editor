<!--

    <table cellpadding="0" cellspacing="0" border="0" height="20px">
      <tr height="20px" class="mainMenuItem">

<%
String bgcolor = "#A4A4A4";
String tab = request.getParameter("tab");
if (tab == null || tab.compareTo("null") == 0) tab = "valueset";
if (tab.compareTo("home") == 0) {
    bgcolor = "#5C5C5C";
} 
%>



	      <td bgcolor="<%=bgcolor%>">

        <a href="<%=request.getContextPath() %>" /pages/welcome.jsf?tab=home"  class="mainMenuLink" >Home</a>

	      </td>

<%
bgcolor = "#A4A4A4";
if (tab.compareTo("valueset") == 0) bgcolor = "#5C5C5C";
%>

	      <td>&#xA0;</td>
	      <td bgcolor="<%=bgcolor%>">

<a href="<%=request.getContextPath() %>/pages/ValueSets.jsf?tab=valueset" class="mainMenuLink" >Value Set</a>

	      </td>
	      
      </tr>
      <tr height="10px"><td>&#xA0;</td></tr>
    </table>
-->