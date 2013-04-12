<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-value-set-editor/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*"%>




<%@ page import="gov.nih.nci.evs.valueseteditor.beans.*" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.utilities.*" %>
<%@ page import="javax.faces.context.FacesContext" %>

<!--
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.properties.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
-->
<%@ page import="javax.faces.context.FacesContext" %>
<!--
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference" %>
-->
<%@ page import="org.apache.log4j.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
  <title>NCI Thesaurus</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  
  <!--
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
  -->
  
</head>
<body onLoad="document.forms.advancedSearchForm.matchText.focus();">

    
     
     
<!--
  <script type="text/javascript"
    src="<%=request.getContextPath()%>/js/wz_tooltip.js"></script>
  <script type="text/javascript"
    src="<%=request.getContextPath()%>/js/tip_centerwindow.js"></script>
  <script type="text/javascript"
    src="<%=request.getContextPath()%>/js/tip_followscroll.js"></script>
    
-->

  <script type="text/javascript">
  
    function refresh() {

      var selectSearchOption = "Property";
      var selectSearchOptionObj = document.forms["advancedSearchForm"].selectSearchOption;
      for (var i=0; i<selectSearchOptionObj.length; i++) {
        if (selectSearchOptionObj[i].checked) {
          selectSearchOption = selectSearchOptionObj[i].value;
        }
      }

      var dictionary = document.forms["advancedSearchForm"].dictionary.value;
      var text = "";
      var algorithm = "exactMatch";
      if (selectSearchOption != 'EntireVocabulary' && selectSearchOption != 'EnumerationOfCodes') {
              var textObj = document.forms["advancedSearchForm"].matchText;
              if (textObj != null) {
		      text = document.forms["advancedSearchForm"].matchText.value;
              }
	      
	      var algorithmObj = document.forms["advancedSearchForm"].adv_search_algorithm;
	      if (algorithmObj != null) {
		      for (var i=0; i<algorithmObj.length; i++) {
			if (algorithmObj[i].checked) {
			  algorithm = algorithmObj[i].value;
			}
		      }
	      }
      }
      
      var rel_search_association = "";
      if (selectSearchOption == "Relationship" && document.forms["advancedSearchForm"].rel_search_association != null) {
          rel_search_association = document.forms["advancedSearchForm"].rel_search_association.value;
      }
      
      var selectProperty = "";
      if (selectSearchOption == "Property" && document.forms["advancedSearchForm"].selectProperty != null) {
           selectProperty = document.forms["advancedSearchForm"].selectProperty.value;
      }
       
            
      var _version = "";
      if (document.forms["advancedSearchForm"].version != null) {
	      document.forms["advancedSearchForm"].version.value;
      }

      window.location.href="/ncitbrowser/pages/component_subset.jsf?refresh=1"
          + "&opt="+ selectSearchOption
          + "&text="+ text
          + "&algorithm="+ algorithm
          + "&prop="+ selectProperty
          + "&rel="+ rel_search_association
          + "&dictionary="+ dictionary
          + "&version="+ _version;
    }
  </script>

  <f:view>
    

        
<%

  String advSearch_requestContextPath = request.getContextPath();
  
  advSearch_requestContextPath = advSearch_requestContextPath.replace("//ncitbrowser//ncitbrowser", "//ncitbrowser");


    String adv_search_vocabulary = request.getParameter("dictionary");
    String adv_search_version = request.getParameter("version");
    
    if (adv_search_vocabulary == null) adv_search_vocabulary = "NCI_Thesaurus"; // to be modified
 
 if (adv_search_version == null) {
     adv_search_version = (String) request.getAttribute("version");
 }
    
   
    String refresh = (String) request.getParameter("refresh");

    boolean refresh_page = false;
    if (refresh != null) {
        refresh_page = true;
    }
    String adv_search_algorithm = null;
    String search_string = "";
    String selectProperty = null;
    String rel_search_association = null;
    String adv_search_source = null;
    String adv_search_type = null;

    String t = null;
    String selectSearchOption = null;

    if (refresh_page) {
        // Note: Called when the user selects "Search By" fields.
        selectSearchOption = (String) request.getParameter("opt");
        
        search_string = (String) request.getParameter("text");
        adv_search_algorithm = (String) request.getParameter("algorithm");
        adv_search_source = (String) request.getParameter("sab");
        rel_search_association = (String) request.getParameter("rel");
        selectProperty = (String) request.getParameter("prop");

    } else {
        selectSearchOption = (String) request.getAttribute("selectSearchOption");
        search_string = (String) request.getSession().getAttribute("matchText");
    }

    if (selectSearchOption == null || selectSearchOption.compareTo("null") == 0) {
        selectSearchOption = "Property";
    }

    SearchStatusBean bean = null;
    String message = (String) request.getAttribute("message");
    if (message != null) {
        request.removeAttribute("message");
    }
    
    // to be modified:
    String code_enumeration = "";

 
    if (!refresh_page || message != null) {
   
/*    
        // Note: Called when search contains no match.
        Object bean_obj = FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("searchStatusBean");
        if (bean_obj == null) {
            bean_obj = request.getAttribute("searchStatusBean");
        }

        if (bean_obj == null) {
            bean = new SearchStatusBean(adv_search_vocabulary);
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("searchStatusBean", bean);

        } else {
            bean = (SearchStatusBean) bean_obj;
            adv_search_algorithm = bean.getAlgorithm();
            adv_search_source = bean.getSelectedSource();
            selectProperty = bean.getSelectedProperty();
            search_string = bean.getMatchText();
            rel_search_association = bean.getSelectedAssociation();
            //rel_search_rela = bean.getSelectedRELA();

            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("searchStatusBean", bean);
        }
*/        
    }

    adv_search_type = selectSearchOption;

    if (rel_search_association == null) rel_search_association = "ALL";
    //if (rel_search_rela == null) rel_search_rela = " ";
    if (selectProperty == null) selectProperty = "ALL";
    //if (adv_search_source == null) adv_search_source = "ALL";
    if (search_string == null) search_string = "";
    if (adv_search_algorithm == null) adv_search_algorithm = "exactMatch";


    String check__e = "", check__b = "", check__s = "" , check__c ="";
    if (adv_search_algorithm == null || adv_search_algorithm.compareTo("exactMatch") == 0)
        check__e = "checked";
    else if (adv_search_algorithm.compareTo("startsWith") == 0)
        check__s= "checked";
    else if (adv_search_algorithm.compareTo("DoubleMetaphoneLuceneQuery") == 0)
        check__b= "checked";
    else
        check__c = "checked";

    String check_n2 = "", check_c2 = "", check_p2 = "" , check_c3 = "" , check_cs = "" , check_r2 ="";

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
    else check_n2 = "checked";

    String editAction = (String) request.getSession().getAttribute("editAction");

%>

<!--

        <div class="pagecontent">
          <a name="evs-content" id="evs-content"></a>
-->          
          
          
          <table>
            <tr>
            <td class="texttitle-blue"><%=editAction%>&nbsp;Component Set</td>
            </tr>

            <% if (message != null) { %>
        <tr class="textbodyred"><td>
      <p class="textbodyred">&nbsp;<%=message%></p>
        </td></tr>
            <% } %>

            <tr class="textbody"><td>

 <h:form id="advancedSearchForm" styleClass="search-form">            
               
                <table>
                  <tr valign="top" align="left">
                   
                         <td align="left" class="textbody">
                             Label:
                         </td>
                         <td>
                     <input CLASS="searchbox-input" name="Label" value="" tabindex="1">
                         </td>
                  </tr>
 
                   <tr valign="top" align="left">
                    
                          <td align="left" class="textbody">
                              Description:
                          </td>
                          <td>
                      <input CLASS="searchbox-input" name="Description" value="" tabindex="2">
                          </td>
                  </tr>
                  
                
                
                <tr valign="top" align="left">
                        <td align="left" class="textbody">
                            Vocabulary:
                        </td>
              
			<td class="dataCellText">
				<h:selectOneMenu id="selectedOntology" value="#{valueSetBean.selectedOntology}">
				     <f:selectItems value="#{valueSetBean.ontologyList}"/>
				</h:selectOneMenu>
			</td>               
                </tr>
                
                <tr valign="top" align="left">
                    <td align="left" class="textbody">
                        Type:
                    </td>

                    <td align="left" class="textbody">
                  <input type="radio" id="selectSearchOption" name="selectSearchOption" value="Code" alt="Code" <%=check_c2%> onclick="javascript:refresh()" tabindex="5">Code&nbsp;
                  <input type="radio" id="selectSearchOption" name="selectSearchOption" value="Name" alt="Name" <%=check_n2%> onclick="javascript:refresh()" tabindex="5">Name&nbsp;
                  <input type="radio" id="selectSearchOption" name="selectSearchOption" value="Property" alt="Property" <%=check_p2%> onclick="javascript:refresh()" tabindex="5">Property&nbsp;
                  <input type="radio" id="selectSearchOption" name="selectSearchOption" value="Relationship" alt="Relationship" <%=check_r2%> onclick="javascript:refresh()" tabindex="5">Relationship

                    </td>
                </tr>                
                

                <tr valign="top" align="left">
                    <td align="left" class="textbody">
                        &nbsp;
                    </td>

                    <td align="left" class="textbody">
                  <input type="radio" id="selectSearchOption" name="selectSearchOption" value="EnumerationOfCodes" alt="Enumeration of Codes" <%=check_c3%> onclick="javascript:refresh()" tabindex="5">Enumeration of Codes
                  <input type="radio" id="selectSearchOption" name="selectSearchOption" value="EntireVocabulary" alt="Entire Vocabulary" <%=check_cs%> onclick="javascript:refresh()" tabindex="5">Entire Vocabulary

                    </td>
                </tr>                   
                
 <%
 if (selectSearchOption != null && selectSearchOption.compareTo("EntireVocabulary") == 0) {
 
 
 } else if (selectSearchOption != null && selectSearchOption.compareTo("Relationship") == 0) {
 %>
     <tr>               
                          <td align="left" class="textbody">
                              Focus concept code:
                          </td>
     <td>                   
     <input CLASS="searchbox-input" name="focusConceptCode" value="<%=search_string%>" tabindex="3">
     </td>
     </tr>
 <% 
 
 
 } else if (selectSearchOption != null && selectSearchOption.compareTo("EnumerationOfCodes") == 0) {
 %>
     <tr>               
                          <td align="left" class="textbody">
                              Codes:
                          </td>
     <td>  
         <textarea name="codes" cols="50" rows=10 tabindex="3"></textarea>
     </td>
     </tr>
 <%  
  
 } else {
 %>
     <tr>
                          <td align="left" class="textbody">
                              Match Text:
                          </td>
			  <td> 
			     <input CLASS="searchbox-input" name="matchText" value="<%=search_string%>" tabindex="3">
			  </td>
     </tr>


<%
  if (selectSearchOption.compareTo("Code") != 0) {
%>
                  <tr>
                  
                        <td align="left" class="textbody">
                            Match Algorithm:
                        </td>
                  
                 
			<td>
			     <table border="0" cellspacing="0" cellpadding="0">
			    <tr valign="top" align="left">
			       <td align="left" class="textbody">
			      <input type="radio" name="adv_search_algorithm" value="exactMatch" alt="Exact Match" <%=check__e%> tabindex="4">Exact Match&nbsp;
			      <input type="radio" name="adv_search_algorithm" value="startsWith" alt="Begins With" <%=check__s%> tabindex="4">Begins With&nbsp;
			      <input type="radio" name="adv_search_algorithm" value="contains" alt="Contains" <%=check__c%> tabindex="4">Contains
			       </td>
                           </tr>     


                            </table>
                  
                        </td>
                  </tr>
                  

<%
        }
 }
 %>



                <tr><td>
                  <table>
 
                  <% if (selectSearchOption.equals("EnumerationOfCodes")) { %>
                   
                       <input type="hidden" name="code_enumeration" id="code_enumeration" value="<%=code_enumeration%>">
                       
                   
                  
                  <% } else if (selectSearchOption.equals("EntireVocabulary")) { %>
                  
                  
                  
                  <% } else if (selectSearchOption.equals("Property")) { %>
                    <input type="hidden" name="rel_search_association" id="rel_search_association" value="<%=rel_search_association%>">
                    
                    <tr>
                      <td></td>
                      <td>
                        <h:outputLabel id="selectPropertyLabel" value="Property" styleClass="textbody">
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
                        </h:outputLabel>
                      </td>
                    </tr>

                  <% } else if (selectSearchOption.equals("Relationship")) { %>
                    <input type="hidden" name="selectProperty" id="selectProperty" value="<%=selectProperty%>">
                    <tr>

                      <td>
                        <h:outputLabel id="rel_search_associationLabel" value="Relationship" styleClass="textbody">
                          <select id="rel_search_association" name="rel_search_association" size="1">
                          <%
                            t = "ALL";
                            if (t.compareTo(rel_search_association) == 0) {
                          %>
                              <option value="<%=t%>" selected><%=t%></option>
                          <%} else {%>
                              <option value="<%=t%>"><%=t%></option>
                          <%} %>

                          <%
                          
System.out.println("adv_search_vocabulary: " + adv_search_vocabulary);
System.out.println("adv_search_version: " + adv_search_version);
                          
                            Vector association_vec = OntologyBean.getSupportedAssociationNames(adv_search_vocabulary, adv_search_version);
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
                        </h:outputLabel>
                      </td>
                      <td></td>                      
                    </tr>
 
                        <tr>
                         <td><h:outputText value="Direction:" styleClass="textbody" /></td>
                         <td>
 <h:selectOneRadio id="direction"
 	value="#{valueSetBean.selectedDirection}" styleClass="textbody" >
 	<f:selectItems value="#{valueSetBean.directionList}"/>
 </h:selectOneRadio>
 
                         </td> 
                     </tr>         
                     
                     <tr>
                       <td>
                         
 			    <h:selectBooleanCheckbox id="checkbox" value="false" styleClass="textbody"
 		 title="Include focus concept"/>
 			    <h:outputText value="Include focus concept?" styleClass="textbody" />

                        </td> 
                        <td>
                          
  			    <h:selectBooleanCheckbox id="transitivity_checkbox" value="false" styleClass="textbody"
  		 title="Transitive closure"/>
  			    <h:outputText value="Transitive Closure?" styleClass="textbody" />
 
                        </td> 
                     </tr>
                     
           
                     
                  <% } else { %>
                    <input type="hidden" name="selectProperty" id="selectProperty" value="<%=selectProperty%>">
                    <input type="hidden" name="rel_search_association" id="rel_search_association" value="<%=rel_search_association%>">
                    
 
                  <% }%>

                  </table>
                </td></tr>

<tr><td>

<h:commandButton
	id="Preview"
	value="Preview"
	action="#{valueSetBean.previewComponentSubsetAction}" >
</h:commandButton>
&nbsp;

<h:commandButton
	id="Save"
	value="Save"
	action="#{valueSetBean.saveComponentSubsetAction}" >
</h:commandButton>
&nbsp;

<h:commandButton
	id="Cancel"
	value="Cancel"
	action="#{valueSetBean.cancelComponentSubsetAction}" >
</h:commandButton>

</td></tr>


              </table>
              <input type="hidden" name="referer" id="referer" value="<%=HTTPUtils.getRefererParmEncode(request)%>">
              <input type="hidden" name="dictionary" id="dictionary" value="<%=adv_search_vocabulary%>">
              <input type="hidden" name="version" id="version" value="<%=adv_search_version%>">

              <input type="hidden" name="adv_search_type" id="adv_search_type" value="<%=adv_search_type%>" />
              <input type="hidden" id="tab" name="tab" value="valueset" /> 
         
            </h:form>
            
          </td></tr>
        </table>
        


  
</f:view>

  
  
</body>
</html>
