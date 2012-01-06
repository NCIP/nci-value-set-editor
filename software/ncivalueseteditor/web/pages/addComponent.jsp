<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.beans.*" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.utilities.*" %>


<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*"%>

<%@ page import="java.util.ResourceBundle"%>


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
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>

  <script type="text/javascript"> 
      window.history.forward(1);
      function noBack() {
         window.history.forward(1);
      }
  </script> 

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

      var vs_uri = "";
      if (document.forms["addComponentForm"].vs_uri != null) {
          vs_uri = document.forms["addComponentForm"].vs_uri.value;
      }  
      
      var dictionary = "";
      if (document.forms["addComponentForm"].Vocabulary != null) {
          dictionary = document.forms["addComponentForm"].Vocabulary.value;
      }   
      
      
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

      var dir = "Forward";
      var radioObj = document.forms["addComponentForm"].direction;
      if (radioObj != null) {
          if (radioObj[1].checked) dir = "Backward";
      }
       
      window.location.href="/ncivalueseteditor/pages/addComponent.jsf?refresh=1"
          + "&opt="+ selectSearchOption
          + "&label="+ label
          + "&uri="+ vs_uri
          + "&description="+ description
          + "&text="+ text
          + "&algorithm="+ algorithm
          + "&ref_uri="+ selectValueSetReference
          + "&prop="+ selectProperty
          + "&rel="+ rel_search_association
          + "&dir="+ dir
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

ResourceBundle resource = ResourceBundle.getBundle("gov.nih.nci.evs.valueseteditor.resources.Resources");
boolean co_label_readonly = true;


System.out.println("=================== addComponent.jsp ======================================= ");


    String adv_search_vocabulary = (String) request.getSession().getAttribute("dictionary");

                      
 Vector vocabulary_vec = DataUtils.getOntologyNames(); 
 if (adv_search_vocabulary == null || adv_search_vocabulary.compareTo("null") == 0) {
	 if (vocabulary_vec.contains("NCI_Thesaurus")) {
	     adv_search_vocabulary = "NCI_Thesaurus";
	 } else if (vocabulary_vec.contains("NCI Thesaurus")) {
	     adv_search_vocabulary = "NCI Thesaurus";
	 }
 }
    

 ValueSetBean vsb = (ValueSetBean)FacesContext.getCurrentInstance()
	     .getExternalContext().getSessionMap().get("ValueSetBean");

 String vs_uri = vsb.getUri();
 System.out.println("addComponent.jsp vs_uri: " + vs_uri);

    String refresh = (String) request.getParameter("refresh");
    boolean refresh_page = false;
    if (refresh != null && refresh.compareTo("null") != 0) {
        refresh_page = true;
    }
    
    String form_requestContextPath = request.getContextPath();
    String advSearch_requestContextPath = request.getContextPath();
    advSearch_requestContextPath = advSearch_requestContextPath.replace("//ncitbrowser//ncitbrowser", "//ncitbrowser");
    
    String adv_search_version = null;//request.getParameter("version");

    String search_algorithm = null;
    String search_string = "";
    String selectValueSetReference = null;
    String selectProperty = null;
    String rel_search_association = null;
    String adv_search_source = null;
    String adv_search_type = null;
    String focusConceptCode = "";

    String t = null;
    
    String label = null;
    String description = null;
    String selectSearchOption = null;
    String direction = null;
        
    String preview = (String) request.getSession().getAttribute("preview");
    
   
 String include_focus_node_checkbox = null;   
 String transitivity_checkbox = null; 
 
    if (preview != null && preview.compareTo("true") == 0) {
    
        adv_search_vocabulary = (String) request.getSession().getAttribute("preview_adv_search_vocabulary");
        selectSearchOption = (String) request.getSession().getAttribute("preview_selectSearchOption");
        label = (String) request.getSession().getAttribute("preview_label");
        
        description = (String) request.getSession().getAttribute("preview_description");
        search_string = (String) request.getSession().getAttribute("preview_search_string");
        search_algorithm = (String) request.getSession().getAttribute("preview_search_algorithm");
        adv_search_source = (String) request.getSession().getAttribute("preview_adv_search_source");
        rel_search_association = (String) request.getSession().getAttribute("preview_rel_search_association");
        selectProperty = (String) request.getSession().getAttribute("preview_selectProperty");
        selectValueSetReference = (String) request.getSession().getAttribute("preview_selectValueSetReference");
        direction = (String) request.getSession().getAttribute("preview_direction");

include_focus_node_checkbox = (String) request.getSession().getAttribute("preview_include_focus_node_checkbox");
transitivity_checkbox = (String) request.getSession().getAttribute("preview_transitivity_checkbox");
      
        focusConceptCode = (String) request.getSession().getAttribute("preview_focusConceptCode");
        if (focusConceptCode == null || focusConceptCode.compareTo("null") == 0) {
            focusConceptCode = "";
        }
       
    } 
    
	    if (refresh_page) {
	    
	    
System.out.println("********************** REFRESH PAGE");	

                vs_uri = (String) request.getParameter("uri");
	    
		adv_search_vocabulary = (String) request.getParameter("dictionary");
		selectSearchOption = (String) request.getParameter("opt");
		label = (String) request.getParameter("label");
		description = (String) request.getParameter("description");
		search_string = (String) request.getParameter("text");
		search_algorithm = (String) request.getParameter("algorithm");
		adv_search_source = (String) request.getParameter("sab");
		rel_search_association = (String) request.getParameter("rel");
		selectProperty = (String) request.getParameter("prop");
		selectValueSetReference = (String) request.getParameter("ref_uri");



System.out.println("REFRESH PAGE adv_search_vocabulary " + adv_search_vocabulary);
System.out.println("REFRESH PAGE selectSearchOption " + selectSearchOption);
System.out.println("REFRESH PAGE label " + label);
System.out.println("REFRESH PAGE description " + description);
System.out.println("REFRESH PAGE search_string " + search_string);
System.out.println("REFRESH PAGE search_algorithm " + search_algorithm);
System.out.println("REFRESH PAGE adv_search_source " + adv_search_source);
System.out.println("REFRESH PAGE rel_search_association " + rel_search_association);
System.out.println("REFRESH PAGE selectProperty " + selectProperty);
System.out.println("REFRESH PAGE selectValueSetReference " + selectValueSetReference);



	    } 

	    if (selectSearchOption == null || selectSearchOption.compareTo("null") == 0) {
		selectSearchOption = "Property";
	    }



System.out.println("------------ addComponent.jsp selectSearchOption: " + selectSearchOption);



     String warning_msg= (String) request.getSession().getAttribute("message");
     if (warning_msg != null && warning_msg.compareTo("null") != 0) {
	 request.getSession().removeAttribute("message");
	 
	 if (warning_msg.compareTo(resource.getString("error_missing_label")) == 0) {
	      co_label_readonly = false;
	 }
	 
	 
     %>
	<p class="textbodyred"><%=warning_msg%></p>
     <%
     }

    
    // to be modified:
    String code_enumeration = "";


    adv_search_type = selectSearchOption;

    if (selectProperty == null) selectProperty = "";
    if (search_string == null) search_string = "";
    if (search_algorithm == null) search_algorithm = "exactMatch";

    String check__e = "", check__b = "", check__s = "" , check__c ="";
    if (search_algorithm == null || search_algorithm.compareTo("exactMatch") == 0)
        check__e = "checked";
    else if (search_algorithm.compareTo("startsWith") == 0)
        check__s= "checked";
    else
        check__c = "checked";

    String check_n2 = "", check_c2 = "", check_p2 = "" , check_c3 = "" , check_cs = "" , check_r2 ="",  check_vs ="";

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
    
    if (label == null) label = "";
    if (description == null) description = "";
 
if (adv_search_vocabulary == null) {
    adv_search_vocabulary = (String) request.getSession().getAttribute("vocabulary");
}
  String adv_search_vocabulary_cs = adv_search_vocabulary;
  if (adv_search_vocabulary_cs != null) {
  	adv_search_vocabulary_cs = DataUtils.getCodingSchemeName(adv_search_vocabulary_cs, null);
  } 



String is_new_component = (String) request.getAttribute("isNewComponent");
request.removeAttribute("isNewComponent");

System.out.println("************* is_new_component " + is_new_component);

if (is_new_component != null && is_new_component.compareTo("true") == 0) {
    co_label_readonly = false;
}
	    
System.out.println("(*) co_label_readonly: " + co_label_readonly);


%>

 <font size="4"><b>Add Component Set</b></font><br/>
 <h:form id="addComponentForm" styleClass="pagecontent">            
               
      <table border="0" width="80%">
 
 
                 <tr valign="top" align="left">
                    
                          <td align="right" class="inputLabel">
                              Value Set:
                          </td>
                          <td class="textbody">
                          
                             <%=vs_uri%>
                     
                          
                          </td>
                 </tr>
               
                
                <tr valign="top" align="left">
                   
                         <td align="right" class="inputLabel">
                             Label:
                         </td>
                         <td class="textbody">
 
                     <%    
                      if (label == null || label.compareTo("") == 0 || label.compareTo("null") == 0) {
                     %>
                          <input CLASS="searchbox-input" name="Label" value="<%=label%>" size="75" tabindex="1" />
                     <%    
                     } else if (co_label_readonly) {
                     %>
                         <%=label%>
                         <input type="hidden" name="Label" id="state" value="<%=label%>" />
                         <input type="hidden" name="component_label" id="state" value="<%=label%>" />
                         
                     <%    
                     } else {
                     %>
                         <input CLASS="searchbox-input" name="Label" value="<%=label%>" size="75" tabindex="1" />
                     <%    
                     }
                     
                     
                     %>
                     
                         </td>
                </tr>
 
                <tr valign="top" >
                    
                          <td align="right" class="inputLabel">
                              Description:
                          </td>
                          <td>
                      <input CLASS="searchbox-input" name="Description" value="<%=description%>" size="75" tabindex="2">
                          </td>
               </tr>
                
                
                
 <% 
 
 
 if (selectSearchOption == null || selectSearchOption.compareTo("ValueSetReference") != 0) { %>
                
                <tr valign="top" >
                        <td align="right" class="inputLabel">
                            Vocabulary:
                        </td>
			<td class="inputItem">
                        
                    
                        
                        
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
     <input CLASS="inputLabel" name="focusConceptCode" value="<%=focusConceptCode%>" tabindex="3">
     </td>
     </tr>
 <% 
 
 
 } else if (selectSearchOption != null && selectSearchOption.compareTo("EnumerationOfCodes") == 0) {
 %>
     <tr>               
                          <td align="right" class="inputLabel">
                              Codes:
                          </td>
     <td>  
         <textarea name="codes" cols="50" rows=10 tabindex="3" class="inputText" ></textarea>
     </td>
     </tr>
     
     
 <% } else if (selectSearchOption != null && selectSearchOption.compareTo("ValueSetReference") == 0) { %>


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
		      if (key.compareTo(selectValueSetReference) == 0) {
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
     if (selectSearchOption.compareTo("Code") == 0) {
         match_text_label = "Code";
     }
     
 %>
     <tr>
                          <td align="right" class="inputLabel">
                              <%=match_text_label%>:
                          </td>
			  <td> 
			     <input name="matchText" value="<%=search_string%>" tabindex="3">
			  </td>
     </tr>


<%
  if (selectSearchOption.compareTo("Code") != 0) {
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
                            t = "";
                            if (t.compareTo(selectProperty) == 0) {
                          %>
                              <option value="<%=t%>" selected><%=t%></option>
                          <%} else {%>
                              <option value="<%=t%>"><%=t%></option>
                          <%}%>

                          <%
                          
adv_search_version = null;                         
                          
                            Vector property_vec = OntologyBean.getSupportedPropertyNames(adv_search_vocabulary, adv_search_version);
                            if (property_vec != null) {
				    for (int i=0; i<property_vec.size(); i++) {
				      t = (String) property_vec.elementAt(i);
				      if (t.compareTo(selectProperty) == 0) {
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

                  <% } else if (selectSearchOption.equals("Relationship")) { 
                 
                  
                  %>
                  
                  
                  
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

adv_search_version = null;
System.out.println("addComponent.jsp OntologyBean.getSupportedAssociationNames  adv_search_vocabulary: " + adv_search_vocabulary);
System.out.println("addComponent.jsp OntologyBean.getSupportedAssociationNames  version: " + adv_search_version);



if (rel_search_association == null || rel_search_association.compareTo("") == 0) {
    rel_search_association = "Concept_In_Subset";
}
                          
                            Vector association_vec = OntologyBean.getSupportedAssociationNames(adv_search_vocabulary, null);
                            if (association_vec != null) { 
				    for (int i=0; i<association_vec.size(); i++) {
				      t = (String) association_vec.elementAt(i);
				      if (t.compareTo(rel_search_association) == 0) {
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
 
                   <tr>
                   
                        <td align="right" class="inputLabel">
                            Direction:
                        </td> 
                                     

                    <td align="left" class="inputItem">
                    <%
                          if (direction != null && direction.compareTo("Backward") == 0) {
		    %>
		                  <input type="radio" id="direction" name="direction" value="Forward" alt="Forward" tabindex="5">Forward&nbsp;
				  <input type="radio" id="direction" name="direction" value="Backward" alt="Backward" checked tabindex="6">Backward;
		<%
		} else {
		%>	  
				  <input type="radio" id="direction" name="direction" value="Forward" alt="Forward" checked tabindex="5">Forward&nbsp;
				  <input type="radio" id="direction" name="direction" value="Backward" alt="Backward"  tabindex="6">Backward;
		<%
		}
                %> 
                    </td>
                </tr> 
                

                         </td> 
                     </tr>         
 
 
                     <tr>
                    
                         <td align="right" class="inputLabel">
                             Include focus concept?
                         </td> 
 
                          <td>
<%
if (include_focus_node_checkbox != null && include_focus_node_checkbox.compareTo("true") == 0) {
%>
    <input type="checkbox" name="include_focus_node_checkbox" value="true" checked="yes" />
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
if (transitivity_checkbox != null && transitivity_checkbox.compareTo("true") == 0) {
%>
    <input type="checkbox" name="transitivity_checkbox" value="true" checked="yes" />
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
                    <input type="hidden" name="selectProperty" id="selectProperty" value="<%=selectProperty%>">
                    <input type="hidden" name="rel_search_association" id="rel_search_association" value="<%=rel_search_association%>">
                    
 
                  <% }%>
                  
                  <input type="hidden" id="tab" name="tab" value="valueset" /> 

<tr>

<td align="right" >&nbsp;</td>

<td>

<%
if (!selectSearchOption.equals("EntireVocabulary")) {
%>
<h:commandButton
	id="Preview"
	value="Preview"
	action="#{ComponentBean.previewComponentSubsetAction}" 
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
	image="#{form_requestContextPath}/images/cancel.gif" alt="Cancel creating component subset">
</h:commandButton>

</td>


</tr>


              </table>
              
              <input type="hidden" name="referer" id="referer" value="HTTPUtils.getRefererParmEncode(request)">
              
              <input type="hidden" name="dictionary" id="dictionary" value="<%=adv_search_vocabulary%>">
              <input type="hidden" name="version" id="version" value="<%=adv_search_version%>">

              <input type="hidden" name="adv_search_type" id="adv_search_type" value="<%=adv_search_type%>" />
              
              <input type="hidden" name="vs_uri" id="vs_uri" value="<%=vs_uri%>" />
              
              
              <input type="hidden" name="state" id="state" value="add_component" />
              <%
              request.getSession().setAttribute("state", "add_component");
              %>
              
         
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

