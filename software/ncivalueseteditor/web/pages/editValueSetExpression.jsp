<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.beans.*" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.utilities.*" %>

<%@ page import="gov.nih.nci.evs.valueseteditor.beans.ValueSetBean.ComponentObject" %>


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
    


    function resetExpression() {
        //document.valueSetEditForm.expression.value = '';
        
        var expression = document.getElementById('expression');
        expression.value = ''; 

    }   



    function updateExpression(frm, btnName) {
	var btn = frm[btnName];
	var valid;

	for (var x=0;x<btn.length; x++)
	{
		valid = btn[x].checked;
		if (valid) {
		    document.valueSetEditForm.expression.value+=btn[x].value;
		    break;
		    
	        }
	}
	if(!valid)
	{
		alert("Please select a component subset.");
	}
    }   
    
  </script>
 
  
</head>

<body onload="noBack();"    onpageshow="if (event.persisted) noBack();" onunload=""> 

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
	String curr_uri = null;
	String form_requestContextPath = request.getContextPath();
   
        boolean isNewVSD = true;
        int componentCount = 0;
        String vsd_description = "";
	String isActive = "true";
	String organizations = "";
	
	String check_true = "";
	String check_false = "";
	
	String expression = "";
	String selectedOntology = "";
		
   
       	ValueSetBean vsb = (ValueSetBean)FacesContext.getCurrentInstance()
			     .getExternalContext().getSessionMap().get("ValueSetBean");
			     
	if (vsb == null) {
	    System.out.println("ValueSetBean == null");
	    
	    
	} else {
	    System.out.println("ValueSetBean != null");
	    
	    
	    isNewVSD = vsb.getIsNewVSD();
	    
	    isActive = vsb.getIsActive();
	    if (isActive.compareTo("true") == 0) check_true = "checked";
	    if (isActive.compareTo("false") == 0) check_false = "checked";
	    
	    vsd_description = vsb.getDescription();
	    
	    if (vsd_description == null) vsd_description = "";
	    
	    organizations = vsb.getOrganizations();
	    if (organizations == null) organizations = "";
	    
	    selectedOntology = DataUtils.getFormalName(vsb.getSelectedOntology(), null);
	    
	    if (selectedOntology == null) {
selectedOntology = "NCI Thesaurus";

	    } 
	    

curr_uri = vsb.getUri();
if (curr_uri == null) {
    curr_uri = (String) request.getSession().getAttribute("vs_uri");
}


expression = vsb.getExpression();
if (expression == null) expression = "";

	    componentCount = vsb.getComponentCount();
	    
		Collection<ComponentObject> objs = vsb.getComponentObjectList();
		
		
		if (objs != null) {
			Iterator it = objs.iterator();
			while (it.hasNext()) {
			    ComponentObject co = (ComponentObject) it.next();
			    System.out.println("\tlabel: " +  co.getLabel());
			    System.out.println("\tdescription: " +  co.getDescription());
			    System.out.println("\tcheck box checked: " +  co.getCheckbox());

			}
		} else {


System.out.println("(*) objs == null??? " + curr_uri);


		}
	    
	}

	%>
		

             <%
             String warning_msg= (String) request.getAttribute("message");
             if (warning_msg != null && warning_msg.compareTo("null") != 0) {
                 request.removeAttribute("message");
             %>
                <p class="textbodyred"><%=warning_msg%></p>
             <%
             }
             
             %>
	
		      
     <h:form id="valueSetEditForm">
     
      <table border="0" width="80%">
      <tr>
       <td align="left"><font size="4"><b>Edit Value Set</b></font></td>

      </tr>
      </table>
      <hr></hr>
    
      
     <table border="0" width="80%">
      <tr>
        <td align="right"><font size="3"><b><u>Metadata</u></b></font></td>
         <td></td>
      </tr>
      <tr>
        <td align="right"><h:outputLabel for="uri" value="URI" styleClass="inputLabel"  /></td>
        <!--
        <td><h:inputText id="uri" value="#{ValueSetBean.uri}" size="75"  readonly="true"/></td>
        -->
        <td class="textbody"><%=curr_uri%></td>
      </tr>
      
      
       <tr>
        <td align="right"><h:outputLabel for="name" value="Name" styleClass="inputLabel"  /></td>
        <td><h:inputText id="name" value="#{ValueSetBean.name}" size="75" /></td>
      </tr>   
 
       <tr>
	  <td align="right" class="inputLabel">
	       Description:
	  </td>         
         
         <td ><textarea id="vsd_description"  name="vsd_description" cols="60" rows="6" wrap="soft" ><%=vsd_description%></textarea></td>
      </tr>       
      
      
      <tr>
        <td align="right"><h:outputLabel for="conceptDomain" value="Concept Domain" styleClass="inputLabel"  /></td>
         <td>
         
	   <h:selectOneMenu id="conceptDomain" value="#{ValueSetBean.selectedConceptDomain}" >
	        <f:selectItems value="#{ValueSetBean.conceptDomainList}" />
	  </h:selectOneMenu> 

         </td>
      </tr>
      <tr>
        <td align="right"><h:outputLabel for="codingScheme" value="Default Coding Scheme" styleClass="inputLabel"  /></td>
        <td>


                           <select id="codingScheme" name="codingScheme" size="1">
<%

 
                            Vector formalname_vec = null;
                            try {
                            	formalname_vec = DataUtils.getOntologyNames();
                            } catch (Exception ex) {
                            
                            }
                            String t;
                            if (formalname_vec != null) { 
				    for (int i=0; i<formalname_vec.size(); i++) {
				      t = (String) formalname_vec.elementAt(i);
				      
				      if (t.compareToIgnoreCase(selectedOntology) == 0) {
				      
				  %>
					<option value="<%=t%>" selected><%=t%></option>
				  <%  } else { 
				  
				  %>
					<option value="<%=t%>"><%=t%></option>
				  <%
				      }
				    }
                            }
                            
                          %>
                          </select>
         </td>
      </tr>
      

      <tr>
        <td>&nbsp;</td><td>&nbsp;</td>
      </tr>
      
      
      <tr>
        
 	  <td align="right" class="inputLabel">
	       Is Active?
	  </td>         
        
        <td align="left" class="inputItem">
        
	  <input type="radio" id="isActive" name="isActive" value="true" alt="true"  <%=check_true%> tabindex="5">true&nbsp;
	  <input type="radio" id="isActive" name="isActive" value="false" alt="false" <%=check_false%> tabindex="5">false&nbsp;

	 
        </td>
      </tr>

      <tr>
        <td align="right"><h:outputLabel for="owner" value="Owner" styleClass="inputLabel" /></td>
        <td><h:inputText id="owner" value="#{ValueSetBean.owner}" /></td>
      </tr>
      
      <tr>
        <td align="right"><h:outputLabel for="organizations" value="Organizations" styleClass="inputLabel" /></td>
        <td><h:inputText id="organizations" value="#{ValueSetBean.organizations}" /></td>
      </tr>

      <tr>
        <td align="right"><h:outputLabel for="source" value="Source" styleClass="inputLabel"  /></td>
        <td>
            
	  <h:selectOneMenu id="source" value="#{ValueSetBean.selectedSource}" >
	        <f:selectItems value="#{ValueSetBean.sourceList}" />
	  </h:selectOneMenu>            
            
            
         </td>
      </tr>
      


      <tr>
      <td align="right" >&nbsp;</td>

	  <td valign="right">
	     <h:commandButton
		value="Save" action="#{ValueSetBean.saveMetadataAction}"
		onclick="javascript:cursor_wait();"
		image="#{form_requestContextPath}/images/saveMetadata.gif" alt="Save value set metadata" />
           
	 &nbsp; 
          
 	 
	     <h:outputText value="#{ValueSetBean.message}"
		rendered="#{not empty ValueSetBean.message}"
		styleClass="inputMessage" />
	</td>

       </tr>
    </table>

    <hr />


 <% 
 
 request.getSession().removeAttribute("vs_obj");
 if (!isNewVSD) { 

 %>

	<table class="dataTable" summary="" cellpadding="3" cellspacing="0"
		border="0" width="96%">
	        <tr>
	           <td align="left"><font size="3"><b><u>Component Sets</u></b></font></td>
	        </tr>	
	</table>
	
	
	        
	<table class="dataTable" summary="" cellpadding="3" cellspacing="0"
		border="0" width="96%">
		
		<tr height="15px">
			<th class="dataTableHeader" scope="col" align="left" width="20px">&#xA0;</th>
			<th class="dataTableHeader" scope="col" align="left">Label</th>
			<th class="dataTableHeader" scope="col" align="left">Description</th>
			<th class="dataTableHeader" scope="col" align="left">Action</th>
		</tr>
		
<% }%>		



<%

	if (!isNewVSD && componentCount == 0) {
%>	
		<tr valign="top">
			<td colspan="4">Component sets are operands shown in a value set expression. Press the Add Component button to create component subsets.</td>
		</tr>		

		<tr>
			<td>&nbsp;</td>
		</tr>
		
<%	
	} else if (componentCount > 0) {
	
		Collection<ComponentObject> objs = vsb.getComponentObjectList();
			
		ArrayList co_list = new ArrayList();
		Iterator it = objs.iterator();
		while (it.hasNext()) {
		    ComponentObject co = (ComponentObject) it.next();
		    co_list.add(co);
		}
		
		SortUtils.quickSort(co_list);
		
		it = co_list.iterator();
		int lcv = 0;
		while (it.hasNext()) {

		    if (lcv %2 == 0) {
%>		    
			<tr class="dataRowLight">
<%			
		    } else {
%>		    
			<tr class="dataRowDark">
<%			
		    }
		    
		    
		    String checked = "";
		    if (lcv == 0) checked = "checked";
		    
		    lcv++;

		    ComponentObject co = (ComponentObject) it.next();
		    String label = co.getLabel();
		    String description = co.getDescription();
		    //String checkedStr = co.getCheckbox() ? "checked" : "";
		    
		    
		    
%>
                    <td> 
	                <input type="radio" name="component_subset"  value="<%=label%>" "<%=checked%>">&nbsp;</input>
                    </td> 
                    
		   <td><%=label%></td>
		   <td><%=description%></td>
		   <td width="110px">

		     <a href="<%= request.getContextPath() %>/pages/editComponent.jsf?action=edit&uri=<%=curr_uri%>&label=<%=label%>" tabindex="15">Edit</a>
		      &#xA0;
		     <a href="<%= request.getContextPath() %>/pages/editValueSet.jsf?action=remove&uri=<%=curr_uri%>&label=<%=label%>" tabindex="15"
		           onclick="return confirm('Are you sure you want to delete?')">Delete</a>
		      &#xA0;		      


		   </td>

                   </tr>
<%                   
		}

          } 
%>


		
<%    if (!isNewVSD) { 

%>

	      <tr>
	          <td align="right" >&nbsp;</td>

                  <td valign="right">
		     <h:commandButton
			value="Add" action="#{ValueSetBean.addComponentAction}"
			onclick="javascript:cursor_wait();"
			rendered="#{!ValueSetBean.isNewVSD}"
			image="#{form_requestContextPath}/images/addComponent.gif" alt="Add component subset" />
		  </td>

	      </tr>	


		
	</table>
	
<%    }%>	

		

      
      <hr />


	<%
	if (componentCount > 1) {
	
	%>

	      <table border="0" width="80%">
	      <tr>
	       <td align="left"><font size="3"><b>Value Set Expression</b></font></td>

	      </tr>
	      </table>

	       <table border="0" width="80%">
			<tr>
			    <td>
				<textarea name="expression" cols="80" rows="6" wrap="soft" ><%=expression%></textarea>
			    </td>
			</tr>
			<tr>
			    <td>

			<button type="button" onClick="javascript:updateExpression(this.form, 'component_subset');" >Component Set</button>&nbsp;

			<button type="button" onClick="document.valueSetEditForm.expression.value+=' &#x222a; '">Union</button>&nbsp;
			<button type="button" onClick="document.valueSetEditForm.expression.value+=' &#x2229; '">Intersection</button>&nbsp;
			<button type="button" onClick="document.valueSetEditForm.expression.value+=' / '">Difference</button>&nbsp;
			<button type="button" onClick="document.valueSetEditForm.expression.value+='('">(</button>&nbsp;
			<button type="button" onClick="document.valueSetEditForm.expression.value+=')'">)</button>
			    </td>
			</tr>

		 <tr>
		      <td>

				<h:commandButton
					id="Reset"
					value="Reset"
					image="#{subsetEditor_requestContextPath}/images/reset.gif"
					action="#{ValueSetBean.resetVSDExpression}" 
					onclick="javascript:resetExpression()"
					alt="Reset Expression" >
				</h:commandButton>

				&nbsp;

				<h:commandButton
					id="Resolve"
					value="Resolve"
					image="#{subsetEditor_requestContextPath}/images/resolve.gif"
					action="#{ValueSetBean.resolveVSDAction}" 
					onclick="javascript:cursor_wait();"
					alt="Resolve Value Set" >
				</h:commandButton>

				&nbsp;

				<h:commandButton
					id="Export"
					value="Export"
					image="#{subsetEditor_requestContextPath}/images/exportxml.gif"
					action="#{ValueSetBean.exportVSDToXMLAction}" 
					onclick="javascript:cursor_wait();"
					alt="Export to LexGridXML" >
				</h:commandButton>

		       </td>
		 </tr>
		</table>

	<%
	} 
	else if (componentCount > 0){
	
	%>


	       <table border="0" width="80%">
			<tr>
			    <td>

				<h:commandButton
					id="Resolve"
					value="Resolve"
					image="#{subsetEditor_requestContextPath}/images/resolve.gif"
					action="#{ValueSetBean.resolveVSDAction}" 
					onclick="javascript:cursor_wait();"
					alt="Resolve Value Set" >
				</h:commandButton>

				&nbsp;

				<h:commandButton
					id="Export"
					value="Export"
					image="#{subsetEditor_requestContextPath}/images/exportxml.gif"
					action="#{ValueSetBean.exportVSDToXMLAction}" 
					onclick="javascript:cursor_wait();"
					alt="Export to LexGridXML" >
				</h:commandButton>


		       </td>
		 </tr>
		</table>

	<%
	}

	%>


         <input type="hidden" id="uri" name="uri" value="<%=curr_uri%>" />   
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

     
