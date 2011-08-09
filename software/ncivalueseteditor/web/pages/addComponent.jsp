<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.beans.*" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.utilities.*" %>


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
      
      window.location.href="/ncivalueseteditor/pages/addComponent.jsf?refresh=1"
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
    String adv_search_vocabulary = (String) request.getSession().getAttribute("dictionary");

                      
 Vector vocabulary_vec = DataUtils.getOntologyNames(); 
 if (adv_search_vocabulary == null || adv_search_vocabulary.compareTo("null") == 0) {
	 if (vocabulary_vec.contains("NCI_Thesaurus")) {
	     adv_search_vocabulary = "NCI_Thesaurus";
	 } else if (vocabulary_vec.contains("NCI Thesaurus")) {
	     adv_search_vocabulary = "NCI Thesaurus";
	 }
 }
 
 System.out.println("(*) adv_search_vocabulary: " + adv_search_vocabulary);
     
    
    String vs_uri = (String) request.getSession().getAttribute("vs_uri");

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

    String t = null;
    
    String label = null;
    String description = null;
    String selectSearchOption = null;

    if (refresh_page) {
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

    } else {
        selectSearchOption = (String) request.getAttribute("selectSearchOption");
        search_string = (String) request.getSession().getAttribute("matchText");
    }

    if (selectSearchOption == null || selectSearchOption.compareTo("null") == 0) {
        selectSearchOption = "Property";
    }

    String message = (String) request.getAttribute("message");
    if (message != null) {
        request.removeAttribute("message");
    }
    
    // to be modified:
    String code_enumeration = "";

 
    if (!refresh_page || message != null) {
  
    }

    adv_search_type = selectSearchOption;

    //if (rel_search_association == null) rel_search_association = "ALL";
    //if (rel_search_rela == null) rel_search_rela = " ";
    if (selectProperty == null) selectProperty = "ALL";
    //if (adv_search_source == null) adv_search_source = "ALL";
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
 

%>

 <font size="4"><b>Component Set</b></font><br/>
 <h:form id="addComponentForm" styleClass="pagecontent">            
               
      <table border="0" width="80%">
                
                <tr valign="top" align="left">
                   
                         <td align="right" class="inputLabel">
                             Label:
                         </td>
                         <td>
                     <input CLASS="searchbox-input" name="Label" value="<%=label%>" size="75" tabindex="1">
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
                
                
                
 <% if (selectSearchOption == null || selectSearchOption.compareTo("ValueSetReference") != 0) { %>
                
                <tr valign="top" >
                        <td align="right" class="inputLabel">
                            Vocabulary:
                        </td>
			<td class="inputItem">
                        
                    
                        
                        
                          <select id="Vocabulary" name="Vocabulary" size="1" onChange="javascript:refresh()" tabindex="6">
 <%                         
                          
                          for (int k=0; k<vocabulary_vec.size(); k++) {
                          
                                String t2 = (String) vocabulary_vec.elementAt(k);
                                if (adv_search_vocabulary != null && t2.compareTo(adv_search_vocabulary) == 0) {
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
     <input CLASS="inputLabel" name="focusConceptCode" value="<%=search_string%>" tabindex="3">
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
         <textarea name="codes" cols="50" rows=10 tabindex="3" class="inputText" >
         </textarea>
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
                            t = "ALL";
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
                          
                            <input type="checkbox" name="include_focus_node_checkbox" value="true" />

                          </td> 
                     </tr>    

                     <tr>
                    
                         <td align="right" class="inputLabel">
                             Transitive closure?
                         </td> 
 
                          <td>
                             <input type="checkbox" name="transitivity_checkbox" value="true" />

                          
                          </td> 
                     </tr>  
                     
           
                     
                  <% } else { %>
                    <input type="hidden" name="selectProperty" id="selectProperty" value="<%=selectProperty%>">
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
	
	action="#{ComponentBean.saveComponentSubsetAction}" 
	
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
