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
		      <%@ include file="/pages/include/navBar.jsp" %>


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
          String contactUsUrl = request.getContextPath() + "/pages/contact_us.jsf";
          String arrowImage = request.getContextPath() + "/images/up_arrow.jpg";
          %>
          
          <a href="#value_set">Value Set Tab</a><br/>
          
          <%=indent%> <a href="#create_value_set">Create Value Set Definition</a><br/>
          <%=indent%><%=indent%> <a href="#enter_metadata">Enter Metadata</a><br/>
          <%=indent%><%=indent%> <a href="#modify_metadata">Modify Metadata</a><br/>
          
          <%=indent%><%=indent%> <a href="#create_component_subset">Create Component Subset</a><br/>
          <%=indent%><%=indent%> <a href="#edit_component_subset">Edit Component Subset</a><br/>
          
          <%=indent%><%=indent%> <a href="#resolve_component_subset">Resolve Component Subset</a><br/>
          <%=indent%><%=indent%> <a href="#create_value_set_expression">Create Value Set Expression</a><br/>
          
          <%=indent%> <a href="#copy_value_set">Make a Copy of a Value Set Definition</a><br/>
          <%=indent%> <a href="#edit_value_set_definition">Edit Value Set Definition</a><br/>
          
          <%=indent%> <a href="#remove_value_set">Remove a Value Set Definition</a><br/>
          
          <%=indent%> <a href="#resolve_value_set_definition">Resolve Value Set Definition</a><br/>
          <%=indent%> <a href="#export_value_set_definition">Export Value Set Definition</a><br/>

          <a href="#additionalInformation">Additional Information</a><br/>
          
        </p>

        <%-- -------------------------------------------------------------- --%> 
 
         <div class="textbody">
           <br/>
           <table width="720px" cellpadding="0" cellspacing="0" border="0">
             <tr>
               <td><a name="introduction"><b>Introduction</b></a></td>
               <td align="right">
                 <a href="#"><img src="<%=arrowImage%>" 
                   width="16" height="16" border="0" alt="top" /></a>
               </td>
             </tr>
           </table>
<p>
The objective of the value set editor application is to provide users with capabilities to extract an arbitrary subset of   
concepts, referred to here as a value set, from the EVS terminology sever. 
A value set can be fully characterized by a value set definition.
A value set definition can be represented in an LexGrid XML format and exported to the server 
for later retrieval by client applications such as the NCI Term Browser.
A value set definition can also be resolved into a list of concepts provided that 
a version of each terminology participating in the value set is specified by the user. 
A terminology is said to participate in a value set if and only if a concept belonging to the terminology 
is also a member of this value set.
If no version is specified, then the production version of each participating terminology would be used
in resolving the value set definition. 
</p>
<p>
The remainder sessions would be focused on how to create, edit, remove, resolve, and export  
value set definitions. 
</p>

        </div>  


        <%-- -------------------------------------------------------------- --%> 
 
         <div class="textbody">
           <br/>
           <table width="720px" cellpadding="0" cellspacing="0" border="0">
             <tr>
               <td><a name="homePage"><b>NCI EVS Value Set Editor Home Page</b></a></td>
               <td align="right">
                 <a href="#"><img src="<%=arrowImage%>" 
                   width="16" height="16" border="0" alt="top" /></a>
               </td>
             </tr>
           </table>
<p>
The Home page of the value set editor is composed of an application banner, 
a menu bar containing a online help hyperlink, 
a horizontal bar containing a quicklink combobox, 
a navigation bar, and a welcome paragraph.
The navigation bar contains two tabs, the <b>Home</b> tab and the <b>Value Set</b> tab.

</p>

        </div>  
        
        <%-- -------------------------------------------------------------- --%> 
 
         <div class="textbody">
           <br/>
           <table width="720px" cellpadding="0" cellspacing="0" border="0">
             <tr>
               <td><a name="home"><b>Home Tab</b></a></td>
               <td align="right">
                 <a href="#"><img src="<%=arrowImage%>" 
                   width="16" height="16" border="0" alt="top" /></a>
               </td>
             </tr>
           </table>
<p>
The application banner, 
menu bar, quicklink combobox, and navigation bar are 
accessible from all user interface pages.
You may return to the Home page at any time by clicking on the <b>Home</b> tab on the navigation bar.
</p>

        </div>  
        

        <%-- -------------------------------------------------------------- --%> 
 
         <div class="textbody">
           <br/>
           <table width="720px" cellpadding="0" cellspacing="0" border="0">
             <tr>
               <td><a name="value_set"><b>Value Set Tab</b></a></td>
               <td align="right">
                 <a href="#"><img src="<%=arrowImage%>" 
                   width="16" height="16" border="0" alt="top" /></a>
               </td>
             </tr>
           </table>
<p>
Clicking on the <b>Value Set</b> tab
will take you to the Value Set List page.
This page shows a list of value set definitions under construction. 
You may click on the <b>Add</b> button to create a value set definition.
The detailed procedure for creating a value set definition would be discussed in 
the <a name="create_value_set"><b>Create Value Set Definition</b></a> session next.
</p>
<p>
You may make a local copy of a value set definition available on the server using the
<b>Copy</b> command. Refer to 
<a name="copy_value_set"><b>Make a Copy of a Value Set Definition</b></a> for details.
</p>
<p>
If there is any value value definition under construction, then the
<b>Remove</b> will appear.
You may check a value set definition and press the <b>Remove</b>button to remove the value set definition
from the system.
A confirmation dialog box will appear asking you to confirm the <b>Remove</b> action.
All data would be permanently deleted once the action is confirmed.
</p>
<p>
There other value set definition actions are also available: 
<b>Edit</b>, <b>Resolve</b>, and <b>Export</b>.
Each would be discussed in 
<a name="edit_value_set_definition"><b>Edit Value Set Definition</b></a>,
<a name="resolve_value_set_definition"><b>Resolve Value Set Definition</b></a>,
<a name="export_value_set_definition"><b>Export Value Set Definition</b></a> below.
</p>


        </div>  
        
        <%-- -------------------------------------------------------------- --%> 
 
         <div class="textbody">
           <br/>
           <table width="720px" cellpadding="0" cellspacing="0" border="0">
             <tr>
               <td><a name="create_value_set"><b>Create Value Set Definition</b></a></td>
               <td align="right">
                 <a href="#"><img src="<%=arrowImage%>" 
                   width="16" height="16" border="0" alt="top" /></a>
               </td>
             </tr>
           </table>        
        
<p>
The procedure for creating a value set definition involves first entering some metadata about the value set definition.
Next, define a collection of component subsets.  
Finally, you can create a value set expression using component subsets you just created and three standard set operators 
Union (&#8746;), Intersection (&#8745;), and Difference(/); for example, ((A &#8746; B) &#8745; C), here A, B, C are labels 
of component subsets.
</p>
<p>
When you click on the Export XML button, 
the value set editor application will derive a value set definition using metadata, component subsets, and value set expression
you provided and export it to a file in LexGrid XML format. 
</p>
<p>
You may review the list of concepts belonging to a value set
by clicking on the <b>Resolve</b> button.
See <a href="#resolve_value_set_definition"><b>Resolve Value Set Definition</b></a> for details.
<p>
</p>
        
</div>             
        
    
        <%-- -------------------------------------------------------------- --%> 
    
         <div class="textbody">
           <br/>
           <table width="720px" cellpadding="0" cellspacing="0" border="0">
             <tr>
               <td><a name="enter_metadata"><b>Enter Metadata</b></a></td>
               <td align="right">
                 <a href="#"><img src="<%=arrowImage%>" 
                   width="16" height="16" border="0" alt="top" /></a>
               </td>
             </tr>
           </table>
<p>
The metadata of a value set definition contain the following data elements:
</p>

          <ul>
            <li>
              <b>URI</b>: A unique identifier of a value set definition for computer processing.
            </li>
            <li>
              <b>Name</b>: A human readable name of a value set definition; for example: FDA SPL Color Terminology              
            </li>
            <li>
              <b>Description</b>: A narrative text describing the value set definition.               
            </li>
            <li>
              <b>Concept domain</b>: A concept domain name for categorizing value set definitions; for example, Intellectual Product. 
                Select one item from the combo-box.
            </li>               
            <li>
              <b>Default coding scheme</b>: The default terminology name. Select one item from the combo-box.               
            </li>
            <li>
              <b>Is active?</b>: An active flag. The default value is true.               
            </li> 
             <li>
              <b>Owner</b>: The owner of the value set definition; for example, FDA.               
            </li>            
            <li>
              <b>Organizations</b>Organization names; for example: FDA.             
            </li>               
            <li>
              <b>Source</b>A source name for determining the position of the value set definition in value set hierarchies; for example:, FDA_SPL_Component. 
              Select one item from the combo-box. The default value is a blank string indicating that the relative position of the value set definition in value set hierarchies has not been specified.
            </li>            
          </ul>

<p>
Press the <b>Save Metadata</b> button to save metadata you entered. 
If all data are entered properly, an <a href="#edit_component_subset">Edit Component Subset</a> page will appear.
A "Change saved." message will also show. 
</p>
         
        </div>          
      
        <%-- -------------------------------------------------------------- --%> 
    
         <div class="textbody">
           <br/>
           <table width="720px" cellpadding="0" cellspacing="0" border="0">
             <tr>
               <td><a name="modify_metadata"><b>Modify Metadata</b></a></td>
               <td align="right">
                 <a href="#"><img src="<%=arrowImage%>" 
                   width="16" height="16" border="0" alt="top" /></a>
               </td>
             </tr>
           </table>
<p>
To change the value of a metadata element, enter or select a new value of the field and press the
<b>Save</b> button. A message will appear indicating the change has been saved properly.
</p>

        </div>       
      
        <%-- -------------------------------------------------------------- --%> 
    
         <div class="textbody">
           <br/>
           <table width="720px" cellpadding="0" cellspacing="0" border="0">
             <tr>
               <td><a name="create_component_subset"><b>Create Component Subset</b></a></td>
               <td align="right">
                 <a href="#"><img src="<%=arrowImage%>" 
                   width="16" height="16" border="0" alt="top" /></a>
               </td>
             </tr>
           </table>
<p>

</p>
<p>
The
<a href="#edit_component_subset">Edit Component Subset</a> page
will show metadata you entered and a Component Subsets table.
Component subsets are operands shown in a value set expression 
(See <a href="#create_value_set_expression">Create Value Set Expression</a>).
Initially, the Component subsets table would be empty.
Click on the <b>Add Component</b> button to create a new component subset.
</p>
<p>
When the <b>Add Component</b> button is pressed, a Component Set page will appear.
</p>
<p>
Specify a value for each of the following data elements:
</p>
          <ul>
            <li>
              <b>Label</b>: A short notation for representing the component subset; for example, S1.
              It would be used in formulating a <a href="#create_value_set_expression">value set expression</a>.
            </li>
            <li>
              <b>Name</b>: A human readable name of a component subset; for example: The Blue color.              
            </li>
            <li>
              <b>Description</b>: A narrative text describing the component subset.               
            </li>
            <li>
              <b>Vocabulary</b>: The name of the vocabulary where concepts in this component subset
                 would be drawn. 
                 It is applicable to all types of component subsets except value set reference.    
                 Select one item from the combo-box.               
            </li>
            <li>
              <b>Type</b>: The type of component subset. There are six types of component subset:     
              
		  <ul>
		    <li>
		      <b>Code</b>: The component subset consists of a single concept in a particular vocabulary.
		    </li>
		    <li>
		      <b>Property</b>: The component subset consists of a collection of concepts in a particular vocabulary 
				     that matches with a user specified matching criterion
		    </li>
		    <li>
		      <b>Relationship</b>               
		    </li>
		    <li>
		      <b>Entire vocabulary</b>              
		    </li>
		    <li>
		      <b>Enumeration of codes</b>             
		    </li>   
		    <li>
		      <b>Value set reference</b>             
		    </li>   		    
		  </ul>              
            </li>            
            <li>
              <b>Match text</b>: Search string, applicable only to Property type component subsets.             
            </li>               
            <li>
              <b>Match Algorithm</b>: Matching Algorithm, applicable only to Property type component subsets.
              Click one of the three radio buttons: Eact Match, Begin With, and Contains. 
            </li>  
            <li>
              <b>Property</b>: Property name, applicable only to Property type component subsets.  
              Select one from the dropdown combo-box.
            </li>              
             <li>
              <b>Code</b>: Concept code, applicable only to Code type component subsets.  
            </li>            
             <li>
              <b>Focus concept code</b>: Focus concept code, applicable only to Relationship type component subsets.  
            </li>              
             <li>
              <b>Association</b>: Association name, applicable only to Relationship type component subsets.
              Select one from the dropdown combo-box.
            </li>               
             <li>
              <b>Direction</b>: Association direction, applicable only to Relationship type component subsets.
              Select one from two radio buttons: Forward and Backward.
            </li>               
             <li>
              <b>Include focus concept?</b>: Include focus concept indicator, applicable only to Relationship type component subsets.
              Check only if the focus concept is included.
            </li>              
             <li>
              <b>Transitive closure?</b>: Transitive closure indicator, applicable only to Relationship type component subsets.
              Check only if transitive closure is applied.
            </li>  
            <li>
              <b>Codes</b>: List of codes, applicable only to Enumeration of Codes type component subsets.
            </li>  
            <li>
              <b>Value Set Reference</b>: The name of the referenced value set definition available on the server,
              applicable only to Value Set Reference type component subsets.
            </li>              
          </ul>

<p>
Press the <b>Preview</b> button to find out the list of concepts belonging to the component subset. 
</p>
<p>
Press the <b>Save</b> button to save the component subset.
</p>
<p>
Press the <b>Cancel</b> button to return to the previous page without saving the component subset data. 
</p>

        </div>     
 


        <%-- -------------------------------------------------------------- --%> 
    
         <div class="textbody">
           <br/>
           <table width="720px" cellpadding="0" cellspacing="0" border="0">
             <tr>
               <td><a name="edit_component_subset"><b>Edit Component Subset</b></a></td>
               <td align="right">
                 <a href="#"><img src="<%=arrowImage%>" 
                   width="16" height="16" border="0" alt="top" /></a>
               </td>
             </tr>
           </table>
<p>

</p>

        </div>   
        

        <%-- -------------------------------------------------------------- --%> 
    
         <div class="textbody">
           <br/>
           <table width="720px" cellpadding="0" cellspacing="0" border="0">
             <tr>
               <td><a name="resolve_component_subset"><b>Resolve Component Subset</b></a></td>
               <td align="right">
                 <a href="#"><img src="<%=arrowImage%>" 
                   width="16" height="16" border="0" alt="top" /></a>
               </td>
             </tr>
           </table>
<p>

</p>

        </div>
        
 
 
         <%-- -------------------------------------------------------------- --%> 
     
          <div class="textbody">
            <br/>
            <table width="720px" cellpadding="0" cellspacing="0" border="0">
              <tr>
                <td><a name="create_value_set_expression"><b>Create Value Set Expression</b></a></td>
                <td align="right">
                  <a href="#"><img src="<%=arrowImage%>" 
                    width="16" height="16" border="0" alt="top" /></a>
                </td>
              </tr>
            </table>
 <p>
 A value set expression determines how a sequence of binary operations on component subsets will be executed to
 resolve the value set, or equivalently, to 
 obtain the list of concepts belonging to a value set.
 For example, the value set expression, ((A &#8746; B) &#8745; C), indicates that
 the steps for resolving the corresponding value set would be first finding the Union of A and B, and then 
 finding the intersection of A &#8746; B and C.
 </p>
 
 <p>
 You may use the following buttons to create the value set expression:
 </p>
           <ul>
             <li>
               <b>Component Set</b>: Add the lable of the selected component subset to the expression.
             </li>
             <li>
               <b>Union</b>: Add the set union operator (&#8746;) to the expression.          
             </li>
             <li>
               <b>Intersection</b>: Add the set intersection operator (&#8745;) to the expression.       
            </li>
             <li>
               <b>Difference</b>: Add the set difference operator (/) to the expression.  
            </li> 
             <li>
               <b>(</b>: Add the open bracket to the expression.       
            </li>  
             <li>
               <b>)</b>: Add the close bracket to the expression.       
            </li>               
           </ul> 
  
 <p>
 Use the <b>Reset</b> command to clear and reset the value set expression.
 </p> 
 <p>
 Use the <b>Resolve</b> command to resolve the corresponding value set definition.
 </p>  
 <p>
 Use the <b>Export XML</b> command to export the value set definition to a file in LexGrid XML format.
 </p> 
 
        </div>
        
        
          <%-- -------------------------------------------------------------- --%> 
      
           <div class="textbody">
             <br/>
             <table width="720px" cellpadding="0" cellspacing="0" border="0">
               <tr>
                 <td><a name="copy_value_set"><b>Make a Copy of a Value Set Definition</b></a></td>
                 <td align="right">
                   <a href="#"><img src="<%=arrowImage%>" 
                     width="16" height="16" border="0" alt="top" /></a>
                 </td>
               </tr>
             </table>
  <p>
  <p>
To make a local copy of a value set definition available on the server, 
click on the Value Set tab on the navigation bar.
This would take you to the Value Set List page.
Press the <b>Copy</b>button to open the Copy Value Set page.

<p>
Specify the value of the following two fields and press the Save button.
</p>

          <ul>
            <li>
              <b>URI</b>: A unique identifier of a value set definition for computer processing.
            </li>
            <li>
              <b>Value Set Reference</b>: The name of the value set definition available on the server. 
              Select one name from the combo-box.
            </li>
          </ul>  

  
<p>
The data about the referenced value set definition will appear in a
Edit Value Set page.
Refer to <a href="#edit_value_set_definition">Edit Value Set Definition</a> for information on
how to modify a value set definition.

</p>  
  
        </div>        
        
        
         <%-- -------------------------------------------------------------- --%> 
     
          <div class="textbody">
            <br/>
            <table width="720px" cellpadding="0" cellspacing="0" border="0">
              <tr>
                <td><a name="edit_value_set_definition"><b>Edit Value Set Definition</b></a></td>
                <td align="right">
                  <a href="#"><img src="<%=arrowImage%>" 
                    width="16" height="16" border="0" alt="top" /></a>
                </td>
              </tr>
            </table>
 <p>
 
 </p>
 
        </div>


        


          <%-- -------------------------------------------------------------- --%> 
      
           <div class="textbody">
             <br/>
             <table width="720px" cellpadding="0" cellspacing="0" border="0">
               <tr>
                 <td><a name="remove_value_set"><b>Remove a Value Set Definition</b></a></td>
                 <td align="right">
                   <a href="#"><img src="<%=arrowImage%>" 
                     width="16" height="16" border="0" alt="top" /></a>
                 </td>
               </tr>
             </table>
  <p>
To remove a value set definition, click on the Value Set tab on the navigation bar.
This would take you to the Value Set List page.
Check the value set definition you would like to remove and press the <b>Remove</b>button.
The system would prompt you for confirmation.
All data you entered regarding the value set definition
would be permanently deleted once the <b>Remove</b> action is confirmed.
So, you should exercise this command with caution.
  </p>
  
        </div>
        
        
        
         <%-- -------------------------------------------------------------- --%> 
     
          <div class="textbody">
            <br/>
            <table width="720px" cellpadding="0" cellspacing="0" border="0">
              <tr>
                <td><a name="resolve_value_set_definition"><b>Resolve Value Set Definition</b></a></td>
                <td align="right">
                  <a href="#"><img src="<%=arrowImage%>" 
                    width="16" height="16" border="0" alt="top" /></a>
                </td>
              </tr>
            </table>
 <p>
 
 </p>
 
        </div>
 
 
 
          <%-- -------------------------------------------------------------- --%> 
      
           <div class="textbody">
             <br/>
             <table width="720px" cellpadding="0" cellspacing="0" border="0">
               <tr>
                 <td><a name="export_value_set_definition"><b>Export Value Set Definition</b></a></td>
                 <td align="right">
                   <a href="#"><img src="<%=arrowImage%>" 
                     width="16" height="16" border="0" alt="top" /></a>
                 </td>
               </tr>
             </table>
  <p>
  
  </p>
  
        </div>
        
        



        
 
 
           <%-- -------------------------------------------------------------- --%> 
       
            <div class="textbody">
              <br/>
              <table width="720px" cellpadding="0" cellspacing="0" border="0">
                <tr>
                  <td><a name="additionalInformation"><b>Additional Information</b></a></td>
                  <td align="right">
                    <a href="#"><img src="<%=arrowImage%>" 
                      width="16" height="16" border="0" alt="top" /></a>
                  </td>
                </tr>
              </table>
   <p>
   Notes on nested value set definition, i.e., value set definition involving component subsets of value set reference type; 
   for example, a value set definition with an expression, ((A &#8746; B) &#8745; C) / ((D &#8745; E) &#8746; F).
   
   
   </p>
   
        </div>
        
        
          
          
 		           
		      <%@ include file="/pages/include/footer.jsp" %>
		  </div>
	</div>
        <div class="mainbox-bottom"><img src="<%= request.getContextPath() %>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    </div>
   <br/> 
</f:view>    
</body>
</html>

