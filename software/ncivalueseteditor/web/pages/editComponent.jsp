<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.beans.*" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.utilities.*" %>

<%@ page import="gov.nih.nci.evs.valueseteditor.beans.ValueSetBean.ValueSetObject" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.beans.ValueSetBean.ComponentObject" %>


<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.apache.log4j.*" %>

<%@ page import="javax.faces.model.*" %>



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
    
    
     function refresh() {

      var selectSearchOption = "Property";
      var selectSearchOptionObj = document.forms["addComponentForm"].selectSearchOption;
      for (var i=0; i<selectSearchOptionObj.length; i++) {
        if (selectSearchOptionObj[i].checked) {
          selectSearchOption = selectSearchOptionObj[i].value;
        }
      }

     
      var dictionary = "";
      if (document.forms["addComponentForm"].Vocabulary != null) {
          dictionary = document.forms["addComponentForm"].Vocabulary.value;
      }        
      
      var text = "";
      var algorithm = "exactMatch";
      if (selectSearchOption != 'EntireVocabulary' && selectSearchOption != 'EnumerationOfCodes') {
              var textObj = document.forms["addComponentForm"].matchText;
              if (textObj != null) {
		      text = document.forms["addComponentForm"].matchText.value;
              }
	      
	      var algorithmObj = document.forms["addComponentForm"].search_algorithm;
	      if (algorithmObj != null) {
		      for (var i=0; i<algorithmObj.length; i++) {
			if (algorithmObj[i].checked) {
			  algorithm = algorithmObj[i].value;
			}
		      }
	      }
      }
      
      var rel_search_association = "";
      if (selectSearchOption == "Relationship" && document.forms["addComponentForm"].rel_search_association != null) {
          rel_search_association = document.forms["addComponentForm"].rel_search_association.value;
      }
      
      var selectProperty = "";
      if (selectSearchOption == "Property" && document.forms["addComponentForm"].selectProperty != null) {
           selectProperty = document.forms["addComponentForm"].selectProperty.value;
      }

      var selectValueSetReference = "";
      if (selectSearchOption == "ValueSetReference" && document.forms["addComponentForm"].selectValueSetReference != null) {
           selectValueSetReference = document.forms["addComponentForm"].selectValueSetReference.value;
      }       

      var label = "";
      if (document.forms["addComponentForm"].Label != null) {
           label = document.forms["addComponentForm"].Label.value;
      }

      var description = "";
      if (document.forms["addComponentForm"].Description != null) {
           description = document.forms["addComponentForm"].Description.value;
      }    
      
      var text = "";
      if (document.forms["addComponentForm"].matchText != null) {
            text = document.forms["addComponentForm"].matchText.value;
      }     
      
      window.location.href="/ncivalueseteditor/pages/editComponent.jsf?refresh=1"
          + "&opt="+ selectSearchOption
          + "&label="+ label
          + "&description="+ description
          + "&text="+ text
          + "&algorithm="+ algorithm
          + "&ref_uri="+ selectValueSetReference
          + "&prop="+ selectProperty
          + "&rel="+ rel_search_association
          + "&dictionary="+ dictionary;

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

<%


System.out.println("editComponent.jsp Step 1");



    String edit_action = (String) request.getParameter("action");
    System.out.println("editComponent.jsp edit_action: " + edit_action);

    String vs_uri = (String) request.getParameter("uri");
    if (vs_uri == null) {
    	vs_uri = (String) request.getAttribute("vs_uri");
    }
    
    System.out.println("editComponent.jsp vs_uri: " + vs_uri);

    String component_label = (String) request.getParameter("label");
    System.out.println("editComponent.jsp component_label: " + component_label);

    String form_requestContextPath = request.getContextPath();

	String _label = null;
	String _description = null;

	String _vocabulary = null;

	String _type = null;
	String _matchText = null;
	String _algorithm = null;

	String _propertyName = null;
	boolean _checkbox = false;

	String _focusConceptCode = null;
	String _rel_search_association = null;
	String _include_focus_node = null;
	String _transitivity = null;
	String _selectedDirection = null;
	String selectValueSetReference = null;
	
	String _codes = null;
        String search_algorithm = null;
        String search_string = "";
        String selectProperty = null;
        String rel_search_association = null;
        
        String adv_search_source = "ALL";
        String adv_search_type = null;

        String t = null;
        String selectSearchOption = null;
        String label = null;
        String description = null;
        
        String adv_search_vocabulary = null;
        String adv_search_version = null;//request.getParameter("version");
         
        
    // find component object
	ValueSetBean vsb = (ValueSetBean)FacesContext.getCurrentInstance()
			     .getExternalContext().getSessionMap().get("ValueSetBean");

	ValueSetObject vs_obj = vsb.getCurrentValueSet();
	ComponentObject component_obj = null;


System.out.println("editComponent.jsp -- Step 2");


	if (vs_obj == null) {
	   System.out.println("(*) vs_obj == null???");
	} else {
	   component_obj = vs_obj.getComponent(component_label);

	   if (component_obj != null) {
	       ValueSetBean.dumpComponentObject(component_obj);
        
		_label = component_obj.getLabel();
		_description = component_obj.getDescription();
		_vocabulary = component_obj.getVocabulary();
		
		
System.out.println("debugging editComponent.jsp _vocabulary: " + _vocabulary);
	

		_type = component_obj.getType();
		_matchText = component_obj.getMatchText();
		_algorithm = component_obj.getAlgorithm();

		_propertyName = component_obj.getPropertyName();
		_checkbox = false;

		_focusConceptCode = component_obj.getFocusConceptCode();
		_rel_search_association = component_obj.getRel_search_association();
		_include_focus_node = component_obj.getInclude_focus_node();
		_transitivity = component_obj.getTransitivity();
		_selectedDirection = component_obj.getSelectedDirection();
		_codes = component_obj.getCodes();
		
		selectValueSetReference = component_obj.getValueSetReference();
		
		search_algorithm = _algorithm;
		search_string = _matchText;
		selectProperty = _propertyName;
		rel_search_association = _rel_search_association;
		
		System.out.println("rel_search_association (1): " + rel_search_association);	
		
		
		//adv_search_source = null;
		adv_search_type = null;

		t = null;
		selectSearchOption = _type;
		label = _label;
		description = _description;
		
		adv_search_vocabulary = _vocabulary;
		
System.out.println("debugging editComponent.jsp adv_search_vocabulary: " + _vocabulary);
		
		
	   }
	}


        
    
        String refresh = (String) request.getParameter("refresh");

    boolean refresh_page = false;
    if (refresh != null) {
        refresh_page = true;
    }
    
  
  //if (adv_search_vocabulary == null) adv_search_vocabulary = "NCI_Thesaurus"; // to be modified

    if (refresh_page) {
        adv_search_vocabulary = (String) request.getParameter("dictionary");
        selectSearchOption = (String) request.getParameter("opt");
        _type = selectSearchOption;
        
        _vocabulary = adv_search_vocabulary;
        
        label = (String) request.getParameter("label");
        description = (String) request.getParameter("description");
        
        search_string = (String) request.getParameter("text");
        search_algorithm = (String) request.getParameter("algorithm");
        adv_search_source = (String) request.getParameter("sab");
        rel_search_association = (String) request.getParameter("rel");
        selectProperty = (String) request.getParameter("prop");
        
        selectValueSetReference = (String) request.getParameter("ref_uri");
        

    } 
    

    if (selectSearchOption == null || selectSearchOption.compareTo("null") == 0) {
        selectSearchOption = "Property";
    }

   

     String warning_msg= (String) request.getAttribute("message");
     if (warning_msg != null && warning_msg.compareTo("null") != 0) {
	 request.removeAttribute("message");
     %>
	<p class="textbodyred"><%=warning_msg%></p>
     <%
     }
    
    
    // to be modified:
    String code_enumeration = "";

    adv_search_type = selectSearchOption;

    if (selectProperty == null) selectProperty = "ALL";
    if (search_string == null) search_string = "";
    if (search_algorithm == null) search_algorithm = "exactMatch";


    String check__e = "", check__b = "", check__s = "" , check__c ="";
    if (search_algorithm == null || search_algorithm.compareTo("exactMatch") == 0)
        check__e = "checked";
    else if (search_algorithm.compareTo("startsWith") == 0)
        check__s= "checked";
    else if (search_algorithm.compareTo("DoubleMetaphoneLuceneQuery") == 0)
        check__b= "checked";
    else
        check__c = "checked";

    String check_n2 = "", check_c2 = "", check_p2 = "" , check_c3 = "" , check_cs = "" , check_r2 ="" ,  check_vs ="";

    if (selectSearchOption == null || selectSearchOption.compareTo("Name") == 0)
      check_n2 = "checked";
    else if (selectSearchOption.compareTo("Code") == 0)
        check_c2 = "checked";
    else if (selectSearchOption.compareTo("Property") == 0)
      check_p2 = "checked";
    else if (selectSearchOption.compareTo("Relationship") == 0)
      check_r2 = "checked";
    else if (selectSearchOption.compareTo("EnumerationOfCodes") == 0)
      check_c3 = "checked";   
    else if (selectSearchOption.compareTo("EntireVocabulary") == 0)
      check_cs = "checked";     
    else if (selectSearchOption.compareTo("ValueSetReference") == 0)
      check_vs = "checked";             
    else check_n2 = "checked";

    String editAction = (String) request.getSession().getAttribute("editAction");
 
%>


 <font size="4"><b>Component Set</b></font><br/>
 <h:form id="addComponentForm" styleClass="pagecontent">            
               
      <table border="0" width="80%">
                
                <tr valign="top" align="left">
                   
                         <td align="right" class="inputLabel">
                             Label:
                         </td>
                         <td>
                     <input CLASS="searchbox-input" name="Label" value="<%=_label%>" size="75" tabindex="1">
                         </td>
                </tr>
 
                <tr valign="top" >
                    
                          <td align="right" class="inputLabel">
                              Description:
                          </td>
                          <td>
                      <input CLASS="searchbox-input" name="Description" value="<%=_description%>" size="75" tabindex="2">
                          </td>
               </tr>


 <% if (selectSearchOption == null || selectSearchOption.compareTo("ValueSetReference") != 0) { %>

                
                <tr valign="top" >
                        <td align="right" class="inputLabel">
                            Vocabulary:
                        </td>
              
 			<td class="inputItem">
                         
  <%                      
  Vector vocabulary_vec = DataUtils.getOntologyNames(); 
  
  if (adv_search_vocabulary == null) {
  	 if (vocabulary_vec.contains("NCI_Thesaurus")) {
  	     adv_search_vocabulary = "NCI_Thesaurus";
  	 } else if (vocabulary_vec.contains("NCI Thesaurus")) {
  	     adv_search_vocabulary = "NCI Thesaurus";
  	 }
  }
  
  if (selectSearchOption != null && selectSearchOption.compareTo("EntireVocabulary") == 0) {
        adv_search_vocabulary = DataUtils.getCodingSchemeName(_vocabulary, null); ;
  }
  
  String adv_search_vocabulary_cs = adv_search_vocabulary;
  if (adv_search_vocabulary_cs != null) {
  	adv_search_vocabulary_cs = DataUtils.getCodingSchemeName(adv_search_vocabulary_cs, null);
  }
   
  %>                       
                         
                         
                           <select id="Vocabulary" name="Vocabulary" size="1" onChange="javascript:refresh()" tabindex="6">
  <%                         
                            
                           for (int k=0; k<vocabulary_vec.size(); k++) {
                           
                                 String t2 = (String) vocabulary_vec.elementAt(k);
                                 String t2_temp = DataUtils.getCodingSchemeName(t2, null);
                                 
                                 if (t2_temp != null && adv_search_vocabulary_cs != null && t2_temp.compareTo(adv_search_vocabulary_cs) == 0) {
  %>                               
                                     <option value="<%=t2%>" selected><%=t2%></option>
  <%                                   
                                 } else {
  %>                            
  
                                     <option value="<%=t2%>" ><%=t2%></option>
 <%
                                 }
                                 
                           }
 %>
 
 			 </select>
 				
			</td>  
		
			
                </tr>
<%}%>                
                
                
                
                <tr valign="top" >
                    <td align="right" class="inputLabel">
                        Type:
                    </td>

                    <td align="left" class="inputItem">
                    
			  <input type="radio" id="selectSearchOption" name="selectSearchOption" value="Code" alt="Code" <%=check_c2%> onclick="javascript:refresh()" tabindex="5">Code&nbsp;
			  <input type="radio" id="selectSearchOption" name="selectSearchOption" value="Property" alt="Property" <%=check_p2%> onclick="javascript:refresh()" tabindex="5">Property&nbsp;
			  <input type="radio" id="selectSearchOption" name="selectSearchOption" value="Relationship" alt="Relationship" <%=check_r2%> onclick="javascript:refresh()" tabindex="5">Relationship&nbsp;
			  <input type="radio" id="selectSearchOption" name="selectSearchOption" value="EnumerationOfCodes" alt="Enumeration of Codes" <%=check_c3%> onclick="javascript:refresh()" tabindex="5">Enumeration of Codes&nbsp;


                    </td>
                </tr> 
                
                
                 <tr valign="top" >
                    <td align="right" class="inputLabel">
                        &nbsp;
                    </td>

                    <td align="left" class="inputItem">
                    
			  <input type="radio" id="selectSearchOption" name="selectSearchOption" value="EntireVocabulary" alt="Entire Vocabulary" <%=check_cs%> onclick="javascript:refresh()" tabindex="5">Entire Vocabulary
			  <input type="radio" id="selectSearchOption" name="selectSearchOption" value="ValueSetReference" alt="Value Set Reference" <%=check_vs%> onclick="javascript:refresh()" tabindex="5">Value Set Reference

                    </td>
                </tr>    


 <%
 if (selectSearchOption != null && selectSearchOption.compareTo("EntireVocabulary") == 0) {
 
 
 
 
 } else if (selectSearchOption != null && selectSearchOption.compareTo("Relationship") == 0) {
 %>
     <tr>               
                          <td align="right" class="inputLabel">
                              Focus concept code:
                          </td>
     <td>                   
     <input CLASS="inputLabel" name="focusConceptCode" value="<%=_focusConceptCode%>" tabindex="3">
     </td>
     </tr>
 <% 
  } else if (selectSearchOption != null && selectSearchOption.compareTo("EnumerationOfCodes") == 0) {
      if (_codes == null) _codes = "";
      _codes = _codes.trim();
 %>
     <tr>               
                          <td align="right" class="inputLabel">
                              Codes:
                          </td>
     <td>  
         <textarea name="codes" cols="50" rows=10 tabindex="3" class="inputText" ><%=_codes%></textarea>
     </td>
     </tr>
 
 
<% 
} else if (selectSearchOption != null && selectSearchOption.compareTo("ValueSetReference") == 0) { 
%>
 
 
     <tr>
 
 	 <td align="right" class="inputLabel">
 	     Value Set Reference:
 	 </td>                   
 
 	 <td class="inputItem">
 	 <select id="selectValueSetReference" name="selectValueSetReference" size="1" tabindex="6">
 
 	   <%
 	     Vector item_vec = DataUtils.getValueSetDefinitions();
 	     
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

<%

 } else {
      String match_text_label = "Match Text";
      if (selectSearchOption != null && selectSearchOption.compareTo("Code") == 0) {
          match_text_label = "Code";
     }
 %>
     <tr>
                          <td align="right" class="inputLabel">
                              <%=match_text_label%>:
                          </td>
			  <td> 
			     <input name="matchText" value="<%=_matchText%>" tabindex="3">
			  </td>
     </tr>


<%
  if (selectSearchOption != null && selectSearchOption.compareTo("Code") != 0) {
%>
                  <tr>
                  
                        <td align="right" class="inputLabel">
                            Match Algorithm:
                        </td>
                  
                 
			<td>
			     <table border="0" cellspacing="0" cellpadding="0">
			       <tr valign="top" align="left">
			       <td align="left" class="inputItem">
				      <input type="radio" name="search_algorithm" value="exactMatch" alt="Exact Match" <%=check__e%> tabindex="4">Exact Match&nbsp;
				      <input type="radio" name="search_algorithm" value="startsWith" alt="Begins With" <%=check__s%> tabindex="4">Begins With&nbsp;
				      <input type="radio" name="search_algorithm" value="contains" alt="Contains" <%=check__c%> tabindex="4">Contains
			       </td>
                                </tr>     

                            </table>
                  
                        </td>
                  </tr>

<%
        }
 }
 %>


                  <% if (selectSearchOption.equals("EnumerationOfCodes")) { %>
                       <input type="hidden" name="code_enumeration" id="code_enumeration" value="<%=code_enumeration%>">
                 
                  <% } else if (selectSearchOption.equals("EntireVocabulary")) { %>
                  
                  
                  <% } else if (selectSearchOption.equals("Property")) { %>
                    <input type="hidden" name="rel_search_association" id="rel_search_association" value="<%=rel_search_association%>">
                    
                    <tr>
                    
                        <td align="right" class="inputLabel">
                            Property:
                        </td>                   
                    
                     
                        <td class="inputItem">
                        
                          <select id="selectProperty" name="selectProperty" size="1" tabindex="6">
                          <%
                            t = "ALL";
                            if (_propertyName != null && t.compareTo(_propertyName) == 0) {
                          %>
                              <option value="<%=t%>" selected><%=t%></option>
                          <%} else {%>
                              <option value="<%=t%>"><%=t%></option>
                          <%}%>

                          <%
                            Vector property_vec = OntologyBean.getSupportedPropertyNames(adv_search_vocabulary, adv_search_version);
                            if (property_vec != null) {
				    for (int i=0; i<property_vec.size(); i++) {
				      t = (String) property_vec.elementAt(i);
				      if (_propertyName != null && t.compareTo(_propertyName) == 0) {
				  %>
					<option value="<%=t%>" selected><%=t%></option>
				  <%  } else { %>
					<option value="<%=t%>"><%=t%></option>
				  <%
				      }
				    }
                            }
                          %>
                          </select>
                        
                        </td>
                    </tr>

                  <% } else if (selectSearchOption.equals("Relationship")) { %>
                    <input type="hidden" name="selectProperty" id="selectProperty" value="<%=selectProperty%>">
                    <tr>

                        <td align="right" class="inputLabel">
                            Association:
                        </td> 
                        
                        <td class="inputItem">
                           <select id="rel_search_association" name="rel_search_association" size="1">

<%
String blank_str = "";
%>

<option value="<%=blank_str%>"><%=blank_str%></option>

                          <%
                          

if (rel_search_association == null || rel_search_association.compareTo("") == 0) {
    rel_search_association = "Concept_In_Subset";
}



                            Vector association_vec = OntologyBean.getSupportedAssociationNames(adv_search_vocabulary, null);
                            if (association_vec != null) { 
				    for (int i=0; i<association_vec.size(); i++) {
				      t = (String) association_vec.elementAt(i);
				      
				      int compareTo = t.compareTo(rel_search_association);
				      if (t.compareToIgnoreCase(rel_search_association) == 0) {
				      
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
                   
                        <td align="right" class="inputLabel">
                            Direction:
                        </td> 
                                     
 
                         <td>
                         
 <h:selectOneRadio id="direction"
 	value="#{ComponentBean.selectedDirection}" styleClass="inputItem" >
 	<f:selectItems value="#{ComponentBean.directionList}"/>
 </h:selectOneRadio>
 
                         </td> 
                     </tr>         
 
 
                     <tr>
                    
                         <td align="right" class="inputLabel">
                             Include focus concept?
                         </td> 
 
                          <td>
                          
                          
                             <% 
                             if (_include_focus_node != null && _include_focus_node.compareTo("true") == 0) {
                             %>
                                 <input type="checkbox" name="include_focus_node_checkbox" value="true" checked />
                             <%    
                             } else {
                             %>
                                 <input type="checkbox" name="include_focus_node_checkbox" value="true" />
                             <%    
                             }
                             %>
                          

                          </td> 
                     </tr>    

                     <tr>
                    
                         <td align="right" class="inputLabel">
                             Transitive closure?
                         </td> 
 
                          <td>
                             <% 
                             if (_transitivity != null && _transitivity.compareTo("true") == 0) {
                             %>
                                 <input type="checkbox" name="transitivity_checkbox" value="true" checked/>
                             <%    
                             } else {
                             %>
                                 <input type="checkbox" name="transitivity_checkbox" value="true" />
                             <%    
                             }
                             %>
                          
                             
                         
                          </td> 
                     </tr>  
                     
           
                     
                  <% } else { %>
                    <input type="hidden" name="selectProperty" id="selectProperty" value="<%=_propertyName%>">
                    <input type="hidden" name="rel_search_association" id="rel_search_association" value="<%=rel_search_association%>">
                    
 
                  <% }%>

<tr>

<td align="right" >&nbsp;</td>

<td>

<%
if (!selectSearchOption.equals("EntireVocabulary")) {
%>

<h:commandButton
	id="Preview"
	value="Preview"
	action="#{ComponentBean.resolveComponentSubsetAction}" 
	image="#{form_requestContextPath}/images/preview.gif" alt="Preview component subset concepts" >
</h:commandButton>
&nbsp;

<%
}
%>

<h:commandButton
	id="Save"
	value="Save"
	action="#{ComponentBean.saveComponentSubsetAction}" 
	image="#{form_requestContextPath}/images/save.gif" alt="Save component subset data">
</h:commandButton>
&nbsp;

<h:commandButton
	id="Cancel"
	value="Cancel"
	action="#{ComponentBean.cancelComponentSubsetAction}" 
	image="#{form_requestContextPath}/images/cancel.gif" alt="Cancel editing component subset">
</h:commandButton>

</td>

</tr>

              </table>
              
              <input type="hidden" name="referer" id="referer" value="HTTPUtils.getRefererParmEncode(request)">
              <input type="hidden" name="adv_search_type" id="adv_search_type" value="<%=adv_search_type%>" />

              <input type="hidden" name="vs_uri" id="vs_uri" value="<%=vs_uri%>" />
              <input type="hidden" name="component_label" id="component_label" value="<%=component_label%>" />
              <input type="hidden" name="action" id="action" value="edit" />
              
    
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

