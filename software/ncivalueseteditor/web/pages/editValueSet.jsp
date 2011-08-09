<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.beans.*" %>

<%@ page import="gov.nih.nci.evs.valueseteditor.beans.ValueSetBean.ComponentObject" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.beans.ValueSetBean.ValueSetObject" %>

<%@ page import="java.util.*" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.utilities.*" %>



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
		      <%@ include file="/pages/include/menuBar.jsp" %>


	<%
        //List vocabulary_list = DataUtils.getOntologyList();
        
        String vs_uri = null;
        
        
	String form_requestContextPath = request.getContextPath();
   
        boolean isNewVSD = true;
        int componentCount = 0;
   
       	ValueSetBean vsb = (ValueSetBean)FacesContext.getCurrentInstance()
			     .getExternalContext().getSessionMap().get("ValueSetBean");
	
	String vsd_description = "";
	String isActive = "true";
	String organizations = "";
	
	String check_true = "";
	String check_false = "";
	
	if (vsb == null) {
	    System.out.println("ValueSetBean == null???");
	} else {
	    System.out.println("ValueSetBean != null");
	
		vs_uri = vsb.getUri();
		String action = (String) request.getParameter("action");
		if (action != null && action.compareTo("remove") == 0) {
			String component_label = request.getParameter("label");
			ValueSetObject vs_obj = vsb.getValueSet(vs_uri);
			vs_obj.removeComponent(component_label);
		}
	    
	    isNewVSD = vsb.getIsNewVSD();
	    
	    isActive = vsb.getIsActive();
	    if (isActive.compareTo("true") == 0) check_true = "checked";
	    if (isActive.compareTo("false") == 0) check_false = "checked";
	    
	    vsd_description = vsb.getDescription();
	    if (vsd_description == null) vsd_description = "";

	    organizations = vsb.getOrganizations();
	    if (organizations == null) organizations = "";
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
		}		    
	    
	}

        System.out.println("componentCount: " + componentCount);

	%>
	
	
	
	
		      
     <h:form id="valueSetFormId">
     
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
        <td><h:inputText id="uri" value="#{ValueSetBean.uri}" size="75" /></td>
      </tr>
      
      <tr>
        <td align="right"><h:outputLabel for="name" value="Name" styleClass="inputLabel"  /></td>
        <td><h:inputText id="name" value="#{ValueSetBean.name}" size="75" /></td>
      </tr>   
 
       <tr>
	  <td align="right" class="inputLabel">
	       Description:
	  </td>
                          
         </td>
         <td><textarea id="vsd_description" name="vsd_description" cols="60" rows="6" wrap="soft" ><%=vsd_description%></textarea></td>
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
            
	   <h:selectOneMenu id="codingScheme" value="#{ValueSetBean.selectedOntology}" >
	        <f:selectItems value="#{ValueSetBean.ontologyList}" />
	  </h:selectOneMenu>            
            
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
        <td><h:inputText id="organizations" value="#{ValueSetBean.organizations}" />
        </td>
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

 if (!isNewVSD) { 
 
 
 %>

	<table class="dataTable" summary="" cellpadding="3" cellspacing="0"
		border="0" width="96%">
		
	        <tr>
	           <td align="right"><font size="3"><b><u>Component Subsets</u></b></font></td>
	        </tr>		
		
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
			<td colspan="4">Component subsets are operands shown in a value set expression. 
			Press the Add Component button to create component subsets.</td>
		</tr>		

		<tr>
			<td>&nbsp;</td>
		</tr>
		
<%	
	} else if (componentCount > 0){
		Collection<ComponentObject> objs = vsb.getComponentObjectList();
		Iterator it = objs.iterator();
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
		    //String checkedStr = "";
%>
                    <td>   
		    
		    <input type=radio id="component_subset" name="component_subset" value="<%=label%>" "<%=checked%>" >&nbsp;</input>
		    
                    </td> 
		   <td><%=label%></td>
		   <td><%=description%></td>
		   <td width="110px">
		     <a href="<%= request.getContextPath() %>/pages/editComponent.jsf?action=edit&uri=<%=vs_uri%>&label=<%=label%>" tabindex="15">Edit</a>
		      &#xA0;
		     <a href="<%= request.getContextPath() %>/pages/editValueSet.jsf?action=remove&uri=<%=vs_uri%>&label=<%=label%>" tabindex="15"
		           onclick="return confirm('Are you sure you want to delete?')">Delete</a>
		      &#xA0;
		   </td>

                   </tr>
<%                   
		}

      } 
%>

		
<%    if (!isNewVSD) { %>

	      <tr>
	          <td align="right" >&nbsp;</td>

                  <td valign="right">
		     <h:commandButton
			value="Add" action="#{ValueSetBean.addComponentAction}"
			onclick="javascript:cursor_wait();"
			rendered="#{!ValueSetBean.isNewVSD}"
			image="#{form_requestContextPath}/images/addComponent.gif" alt="Add component subset" />
                    
	             &nbsp;    
	 
		  
		     <h:commandButton
			value="Expression" action="#{ValueSetBean.editValueSetExpressionAction}"
			onclick="javascript:cursor_wait();"
			rendered="#{ValueSetBean.hasComponent}"
			image="#{form_requestContextPath}/images/setExpression.gif" alt="Edit value set expression" />
		  </td>

	      </tr>	


		
	</table>
	
<%    }%>	

          <input type="hidden" id="vs_uri" name="vs_uri" value="<%=vs_uri%>" />  
		
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

     
