/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-value-set-editor/LICENSE.txt for details.
 */

package gov.nih.nci.evs.valueseteditor.beans;

import gov.nih.nci.evs.valueseteditor.beans.ValueSetBean.ValueSetObject;
import gov.nih.nci.evs.valueseteditor.utilities.FacesUtil;
import gov.nih.nci.evs.valueseteditor.utilities.ValueSetSearchUtil;
import gov.nih.nci.evs.valueseteditor.utilities.*;

import java.util.Vector;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.ArrayList;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;


import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.util.PrintUtility;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.PropertyMatchValue;
import org.LexGrid.valueSets.PropertyReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitionReference;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.valueSets.CodingSchemeReference;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;

import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.Exceptions.LBException;

import org.lexgrid.valuesets.dto.ResolvedValueSetCodedNodeSet;


import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;


import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;

import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Properties;

import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedConceptDomain;
import org.LexGrid.naming.SupportedSource;

import org.LexGrid.valueSets.CodingSchemeReference;

import org.LexGrid.valueSets.ValueSetDefinitionReference;


import javax.servlet.http.*;

import javax.faces.context.*;
import javax.faces.event.*;
import javax.faces.model.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

/**
 * 
 */

/**
 * Action bean for component operations
 *
 * @author kim.ong@ngc.com, garciawa2
 */
public class ComponentBean {

   	private HashMap componentObjectHashMap = null;


	// Local class variables
    private static Logger _logger = Logger.getLogger(ComponentBean.class);
    private ResourceBundle resource = ResourceBundle.getBundle("gov.nih.nci.evs.valueseteditor.resources.Resources");
    private String _message = null;

    // Internal maps
    //private String _selectedOntology = null;
    //private Map<String,String> _selectedOntologyList = null;
    private List _selectedOntologyList = null;

    // Component variables
    private String _label = null;
    private String _description = null;
    private String _type = "code";
    private String _matchText = null;
    private String _algo = null;

    private String _vocabulary = null;

    private String _focusConceptCode = null;
    private String _propertyName = null;

    private String _rel_search_association = null;
    private String _include_focus_node = null;
    private String _transitivity = null;
	private String _selectedDirection = "Backward";

    private List _directionList = null;

    private String _codes = null;

    private String _selectValueSetReference = null;

    private String _expression = null;


    // Value Set bean
    ValueSetBean vsb = null;

    /**
     * Class constructor
     * @throws Exception
     */
    public ComponentBean() throws Exception {

    	// Read in pull down lists

    	ValueSetSearchUtil util = new ValueSetSearchUtil();
    	if (_selectedOntologyList == null)
    		_selectedOntologyList = util.getOntologyList();

    	vsb = (ValueSetBean)FacesContext.getCurrentInstance()
			.getExternalContext().getSessionMap().get("ValueSetBean");

    	// Check if this is an edit session

		String labelParam = FacesUtil.getRequestParameter("labelParam");
		if (labelParam != null) {
			_logger.debug("Reading component: " + labelParam);
	        ValueSetObject vs = vsb.getCurrentValueSet();
	        ValueSetBean.ComponentObject co = vs.getCompList().get(labelParam);
	        _label = co.getLabel();
	        _description = co.getDescription();
	        _type = co.getType();
	        _matchText = co.getMatchText();
	        _algo = co.getAlgorithm();
		}

	}



	public List getDirectionList() {
		if (_directionList != null) return _directionList;
		_directionList = new ArrayList();

		_directionList.add(new SelectItem("Forward"));
		_directionList.add(new SelectItem("Backward"));

		return _directionList;
	}


    // ========================================================
    // ====               Getters & Setters                 ===
    // ========================================================

        public String getValueSetReference() {
        	return _selectValueSetReference;
        }

        public void setValueSetReference(String selectValueSetReference) {
        	this._selectValueSetReference = selectValueSetReference;
        }


        public String getPropertyName() {
        	return _propertyName;
        }

        public void setPropertyName(String propertyName) {
        	this._propertyName = propertyName;
        }


        public String getFocusConceptCode() {
        	return _focusConceptCode;
        }

        public void setFocusConceptCode(String focusConceptCode) {
        	this._focusConceptCode = focusConceptCode;
        }

        public String getRel_search_association() {
         	return _rel_search_association;
        }

        public void setRel_search_association(String rel_search_association) {
        	this._rel_search_association = rel_search_association;
        }


        public String getInclude_focus_node() {
          	return _include_focus_node;
        }

        public void setInclude_focus_node(String include_focus_node) {
          	this._include_focus_node = include_focus_node;
        }


        public String getTransitivity() {
         	return _transitivity;
        }

        public void setTransitivity(String transitivity) {
         	this._transitivity = transitivity;
        }

        public String getSelectedDirection() {
        	return _selectedDirection;
        }

        public void setSelectedDirection(String selectedDirection) {
        	this._selectedDirection = selectedDirection;
        }


    public String getMessage() {
        return _message;
    }

    // -----------

    public String getLabel() {
        return _label;
    }

    public void setLabel(String label) {
        _label = label;
    }

    // -----------

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    // -----------

	public String getVocabulary() {
		if (_vocabulary == null) {
			_vocabulary = "NCI Thesaurus";

			setVocabulary(_vocabulary);
		}
		return _vocabulary;
	}

	public void setVocabulary(String selectedOntology) {
		this._vocabulary = selectedOntology;

        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

		request.getSession().setAttribute("vocabulary", _vocabulary);
	}

	//public Map<String,String> getOntologyList() {
	public List getOntologyList() {
		return _selectedOntologyList;
	}

    // -----------

    public String getType() {
        return _type;
    }

    public void setType(String type) {
        _type = type;
    }

    // -----------

    public String getMatchText() {
        return _matchText;
    }

    public void setMatchText(String matchText) {
        _matchText = matchText;
    }

    // -----------

    public String getAlgo() {
        return _algo;
    }

    public void setAlgo(String algo) {
        _algo = algo;
    }



	public void directionSelectionChanged(ValueChangeEvent event) {
		if (event.getNewValue() == null) return;

	}




    // ========================================================
    // ====                 Action Methods                  ===
    // ========================================================


    public ValueSetBean.ComponentObject getComponentObject() {
    	_message = null;

        if (vsb.getUri() == null || vsb.getUri().length() < 1) {
           return null;
        }
    	_label = FacesUtil.getRequestParameter("Label");
    	_description = FacesUtil.getRequestParameter("Description");
    	_vocabulary = FacesUtil.getRequestParameter("Vocabulary");
     	_type = FacesUtil.getRequestParameter("selectSearchOption");
     	//_logger.debug("Type: " + _type);

     	_selectedDirection = FacesUtil.getRequestParameter("direction");

     	if (_type.compareTo("EnumerationOfCodes") == 0) {
			_codes = FacesUtil.getRequestParameter("codes");
			_codes = _codes.trim();

		} else if (_type.compareTo("ValueSetReference") == 0) {
			_selectValueSetReference = FacesUtil.getRequestParameter("selectValueSetReference");

		}
     	_include_focus_node = FacesUtil.getRequestParameter("include_focus_node_checkbox");
     	_transitivity = FacesUtil.getRequestParameter("transitivity_checkbox");
     	_matchText = FacesUtil.getRequestParameter("matchText");
      	_algo = FacesUtil.getRequestParameter("search_algorithm");
      	_focusConceptCode = FacesUtil.getRequestParameter("focusConceptCode");
      	_propertyName = FacesUtil.getRequestParameter("selectProperty");
      	_rel_search_association = FacesUtil.getRequestParameter("rel_search_association");

        ValueSetBean.ComponentObject co = null;
        try{
        	co = new ValueSetBean().instantiateComponentObject();

		} catch (Exception ex) {
			return null;
		}


        co.setLabel(_label);
        co.setDescription(_description);
        co.setType(_type);
        co.setMatchText(_matchText);
        co.setAlgorithm(_algo);

        if (_vocabulary != null) {
			String cs_uri = DataUtils.getCodingSchemeURI(_vocabulary, null);
			co.setVocabulary(cs_uri); // change to URI
	    }
        co.setPropertyName(_propertyName);
        co.setFocusConceptCode(_focusConceptCode);
        co.setRel_search_association(_rel_search_association);
        co.setInclude_focus_node(_include_focus_node);
        co.setTransitivity(_transitivity);

        co.setSelectedDirection(_selectedDirection);
        co.setValueSetReference(_selectValueSetReference);
        if (_codes == null) _codes = "";
        co.setCodes(_codes);

        return co;
    }


    public String validateComponentForm(HttpServletRequest request) {

        String state = (String) request.getParameter("state");
        request.getSession().setAttribute("state", state);

        String label = (String) request.getParameter("Label");
        _label = label;

        String description = (String) request.getParameter("Description");
        _description = description;

        String adv_search_vocabulary = (String) request.getParameter("Vocabulary");
        _vocabulary = adv_search_vocabulary;

        String selectSearchOption = (String) request.getParameter("selectSearchOption");
        _type = selectSearchOption;



        String codes = (String) request.getParameter("codes");
        _codes = codes;

        String selectValueSetReference = (String) request.getParameter("selectValueSetReference");
        _selectValueSetReference = selectValueSetReference;

        String focusConceptCode = (String) request.getParameter("focusConceptCode");
        _focusConceptCode = focusConceptCode;

        String search_string = (String) request.getParameter("matchText");
        _matchText = search_string;


        String search_algorithm = (String) request.getParameter("search_algorithm");
        _algo = search_algorithm;

        //String code_enumeration = (String) request.getParameter("code_enumeration");
        String selectProperty = (String) request.getParameter("selectProperty");
        _propertyName = selectProperty;

        String rel_search_association = (String) request.getParameter("rel_search_association");
        _rel_search_association = rel_search_association;

        String direction = (String) request.getParameter("direction");
        _selectedDirection = direction;

        String matchText = (String) request.getParameter("matchText");
        _matchText = matchText;

        String include_focus_node_checkbox = (String) request.getParameter("include_focus_node_checkbox");
        _include_focus_node = include_focus_node_checkbox;

        String transitivity_checkbox = (String) request.getParameter("transitivity_checkbox");
        _transitivity = transitivity_checkbox;

        String _message = null;

        if (_focusConceptCode != null && _focusConceptCode.compareTo("") != 0 && _vocabulary != null) {
			_vocabulary = DataUtils.getCodingSchemeName(_vocabulary, null);
			if (DataUtils.getConceptByCode(_vocabulary, null, null, _focusConceptCode) == null) {
				_message = "WARNING: Invalid code " + _focusConceptCode + ".";
				request.setAttribute("message", _message);
				request.getSession().setAttribute("selectSearchOption", _type);
				request.setAttribute("message", _message);
			}
		}

        else if (_type.compareToIgnoreCase("Code") == 0) {
			if (_matchText != null && _matchText.compareTo("") != 0 && _vocabulary != null) {
				_vocabulary = DataUtils.getCodingSchemeName(_vocabulary, null);
				if (DataUtils.getConceptByCode(_vocabulary, null, null, _matchText) == null) {
					_message = "WARNING: Invalid code " + _matchText + ".";
					request.setAttribute("message", _message);
					request.getSession().setAttribute("selectSearchOption", _type);
					request.setAttribute("message", _message);
				}
			}
		}

		String incompleteDataEntry = "WARNING: Incomplete data entry.";


		if (_type.compareTo("EntireVocabulary") == 0) {
			if (isNull(_vocabulary)) {
				request.setAttribute("message", incompleteDataEntry);
				_message = incompleteDataEntry;
			}
		} else if (_type.compareTo("Code") == 0) {
			if (isNull(_vocabulary) || isNull(_matchText)) {
				request.setAttribute("message", incompleteDataEntry);
				_message = incompleteDataEntry;
			}
		} else if (_type.compareTo("Name") == 0) {
			if (isNull(_vocabulary) || isNull(_matchText)) {
				request.setAttribute("message", incompleteDataEntry);
				_message = incompleteDataEntry;
			}
		} else if (_type.compareTo("Property") == 0) {
			if (isNull(_vocabulary) || isNull(_matchText) || isNull(_algo) || isNull(_propertyName)) {
				request.setAttribute("message", incompleteDataEntry);
				_message = incompleteDataEntry;
			}

		} else if (_type.compareTo("Relationship") == 0) {

            if (isNull(_transitivity)) {
				_transitivity = "false";
			}
            if (isNull(_include_focus_node)) {
				_include_focus_node = "false";
			}


			if (isNull(_vocabulary)) {
				//|| isNull(_focusConceptCode) || isNull(_rel_search_association) || isNull(_selectedDirection)) {
				request.setAttribute("message", incompleteDataEntry + " - vocabulary.");
				_message = incompleteDataEntry + " - vocabulary.";
			}

			if (isNull(_rel_search_association)) {
				//|| isNull(_focusConceptCode) || isNull(_rel_search_association) || isNull(_selectedDirection)) {
				request.setAttribute("message", incompleteDataEntry + " - association name.");
				_message = incompleteDataEntry + " - association name.";
			}

			if (isNull(_selectedDirection)) {
				//|| isNull(_focusConceptCode) || isNull(_rel_search_association) || isNull(_selectedDirection)) {
				request.setAttribute("message", incompleteDataEntry + " - association direction.");
				_message = incompleteDataEntry + " - association direction.";
			}


			/*
			if (isNull(_transitivity) || isNull(_include_focus_node)) {
				request.setAttribute("message", incompleteDataEntry);
				_message = incompleteDataEntry;
			}
			*/


		} else if (_type.compareTo("EnumerationOfCodes") == 0) {

			if (isNull(_vocabulary) || isNull(_codes)) {
				request.setAttribute("message", incompleteDataEntry);
				_message = incompleteDataEntry;
			}
		}


        String action = (String) request.getParameter("action");
        if (action != null && action.compareTo("edit") == 0) {
			String vs_uri = (String) request.getParameter("vs_uri");
			request.getSession().setAttribute("preview_vs_uri", vs_uri);

		}


		return _message;
	}



    public String findBestContainsAlgorithm(String matchText) {
        if (matchText == null)
            return "nonLeadingWildcardLiteralSubString";
        matchText = matchText.trim();
        if (matchText.length() == 0)
            return "nonLeadingWildcardLiteralSubString"; // or null
        if (matchText.length() > 1)
            return "nonLeadingWildcardLiteralSubString";
        char ch = matchText.charAt(0);
        if (Character.isDigit(ch))
            return "literal";
        else if (Character.isLetter(ch))
            return "LuceneQuery";
        else
            return "literalContains";
    }



public String previewComponentSubsetAction() throws Exception {

        ResolvedConceptReferencesIterator iterator = null;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        String message = validateComponentForm(request);

        String vs_uri = (String) request.getParameter("vs_uri");

        ValueSetBean.ComponentObject co = new ValueSetBean().instantiateComponentObject();


        String state = (String) request.getParameter("state");
        request.getSession().setAttribute("state", state);

        String label = (String) request.getParameter("Label");
        _label = label;

        String description = (String) request.getParameter("Description");
        _description = description;

        String adv_search_vocabulary = (String) request.getParameter("Vocabulary");
        _vocabulary = adv_search_vocabulary;

        String selectSearchOption = (String) request.getParameter("selectSearchOption");
        _type = selectSearchOption;

        String codes = (String) request.getParameter("codes");
        _codes = codes;

        String selectValueSetReference = (String) request.getParameter("selectValueSetReference");
        _selectValueSetReference = selectValueSetReference;

        String focusConceptCode = (String) request.getParameter("focusConceptCode");
        _focusConceptCode = focusConceptCode;

        String search_string = (String) request.getParameter("matchText");

        String search_algorithm = (String) request.getParameter("search_algorithm");


        if (search_algorithm != null && search_algorithm.compareToIgnoreCase("contains") == 0) {
			search_algorithm = findBestContainsAlgorithm(search_string);
		}

        _algo = search_algorithm;

        //String code_enumeration = (String) request.getParameter("code_enumeration");
        String selectProperty = (String) request.getParameter("selectProperty");
        _propertyName = selectProperty;

        String rel_search_association = (String) request.getParameter("rel_search_association");
        _rel_search_association = rel_search_association;

        String direction = (String) request.getParameter("direction");
        _selectedDirection = direction;


        String matchText = (String) request.getParameter("matchText");
        _matchText = matchText;

        String include_focus_node_checkbox = (String) request.getParameter("include_focus_node_checkbox");
        _include_focus_node = include_focus_node_checkbox;

        String transitivity_checkbox = (String) request.getParameter("transitivity_checkbox");
        _transitivity = transitivity_checkbox;


        co.setLabel(_label);
        co.setDescription(_description);
        co.setType(_type);

        if (_matchText != null) _matchText = _matchText.trim();

        co.setMatchText(_matchText);
        co.setAlgorithm(_algo);
        co.setVocabulary(_vocabulary);
        co.setPropertyName(_propertyName);

        if (_focusConceptCode != null) _focusConceptCode = _focusConceptCode.trim();
        co.setFocusConceptCode(_focusConceptCode);

        co.setRel_search_association(_rel_search_association);
        co.setInclude_focus_node(_include_focus_node);
        co.setTransitivity(_transitivity);
        co.setSelectedDirection(_selectedDirection);

        co.setValueSetReference(_selectValueSetReference);

		if (_type.compareTo("EntireVocabulary") == 0) {
			String cs_name = DataUtils.getCodingSchemeName(_vocabulary, null);
            co.setCodingSchemeReference(cs_name);
		}

        if (_codes == null) _codes = "";
        co.setCodes(_codes);


		request.getSession().setAttribute("preview_component", co);
		request.getSession().setAttribute("vs_uri", vs_uri);
		request.getSession().setAttribute("ComponentObjectLabel", co.getLabel());
		request.getSession().setAttribute("ComponentDescription", co.getDescription());


		request.getSession().setAttribute("component_obj", co);

        if (message != null) {
			request.setAttribute("message", message);
			return "message";
		}

		return "comp_obj_coding_scheme_references";

    }



    public String previewNewComponentSubsetAction() throws Exception {

        ResolvedConceptReferencesIterator iterator = null;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        String message = validateComponentForm(request);
        if (message != null) {
			request.setAttribute("message", message);
			return "message";
		}

        String state = (String) request.getParameter("state");
        if (state == null) {
			state = (String) request.getSession().getAttribute("state");

		}


		String component_label = (String) request.getParameter("component_label");


		ValueSetObject vs_obj = null;//vsb.getCurrentValueSet();
		ValueSetBean.ComponentObject ob = null;
		boolean retval;


		String vs_uri = null;

		if (state.compareTo("add_component") == 0) {
			vs_obj = vsb.getCurrentValueSet();
			vs_obj = new ValueSetBean().copyValueSetObject(vs_obj);
			ob = getComponentObject();
			//retval = vs_obj.addComponent(ob);

			vs_uri = vs_obj.getUri();

		} else if (state.compareTo("edit_component") == 0) {

            vs_uri = (String) request.getParameter("vs_uri");

            vs_obj = vsb.getCurrentValueSet();
            ob = vs_obj.getComponent(component_label);
            retval = vs_obj.addComponent(ob);
		}

		request.getSession().setAttribute("vs_obj", vs_obj);
		request.getSession().setAttribute("vs_uri", vs_uri);


		if (component_label != null) {
			request.getSession().setAttribute("ComponentObjectLabel", component_label);

		} else{
			request.getSession().setAttribute("ComponentObjectLabel", ob.getLabel());

		}

		request.getSession().setAttribute("ComponentDescription", _description);

		return "comp_obj_coding_scheme_references";

    }



    public String continueResolveComponentSubsetAction() throws Exception {

        ResolvedConceptReferencesIterator iterator = null;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();


		ValueSetBean.ComponentObject component = null;


        _message = "";
        request.getSession().removeAttribute("message");



String vsd_uri = (String) request.getParameter("uri");


ValueSetObject vs_obj = vsb.getValueSet(vsd_uri);

if (vs_obj == null) {
	String message = "ERROR: Value set object is not found in session.";
	request.setAttribute("message", message);
	return "message";
} else {
	System.out.println("continueResolveComponentSubsetAction vs_obj found in session.: " + vs_obj.getUri());
}

        // find vs_uri and component_label
//		String vs_uri = vs_obj.getUri();
//String vs_uri = vsd_uri;


		String component_label = (String) request.getParameter("ComponentObjectLabel");


boolean preview = true;
request.getSession().setAttribute("vsd_uri", vsd_uri);
request.getSession().setAttribute("component_label", component_label);


component = (ValueSetBean.ComponentObject) request.getSession().getAttribute("preview_component");


        String infixExpression = null;
        infixExpression = component_label;


		ValueSetDefinition vsd = null;


		if (!preview) {
			vsd = new ValueSetBean().convertToValueSetDefinition(vs_obj, infixExpression);
	    } else {
			ValueSetObject clone = new ValueSetBean().cloneValueSetObject(vs_obj);
			clone.addComponent(component);
			vsd = new ValueSetBean().convertToValueSetDefinition(clone, infixExpression);
		}

        //vsd = new ValueSetBean().convertToValueSetDefinition(vs_obj, infixExpression);

		HashMap<String, ValueSetDefinition> referencedVSDs = null;

        iterator = ValueSetUtils.resolveValueSetDefinition(vsd, referencedVSDs);

        if (iterator != null) {
			try {
				int numRemaining = iterator.numberRemaining();
				System.out.println("number of matches: " + numRemaining);
			} catch (Exception ex) {
				System.out.println("numRemaining exception???");
			}
		}


        if (iterator == null) {
			String message = "WARNING: Unable to resolve component set.";
			request.setAttribute("message", message);
			return "message";
		} else {
			System.out.println("continueResolveComponentSubsetAction iterator != null");
		}

		IteratorBean iteratorBean = null;

		String key = vsd.getValueSetDefinitionURI();

		iteratorBean = new IteratorBean(iterator);
		iteratorBean.setKey(key);
		//iteratorBeanManager.addIteratorBean(iteratorBean);

		int size = iteratorBean.getSize();
		if (size > 0) {
			String match_size = Integer.toString(size);
			request.getSession().setAttribute("match_size", match_size);
			request.getSession().setAttribute("page_string", "1");
			request.getSession().setAttribute("key", key);

			request.getSession().setAttribute("iteratorBean", iteratorBean);

            request.getSession().setAttribute("label", _label);
			return "search_results";
		}

        String message = "No match found.";
        request.setAttribute("message", message);

        if (component == null) {
			return "add_component";
		}
		return "edit_component";
   }


    public String saveComponentSubsetAction() throws Exception {

        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        request.getSession().removeAttribute("preview_component");
        request.getSession().removeAttribute("preview");

        _message = "";
        request.removeAttribute("message");

		//String vs_uri = vsb.getUri();

		String message = validateComponentForm(request);

		String vs_uri = (String) request.getParameter("vs_uri");//vsb.getUri();


    	_label = FacesUtil.getRequestParameter("Label");
    	if (_label == null) {
			_label = (String) request.getSession().getAttribute("label");
		}


    	_logger.debug("saveComponentSubsetAction Label: " + _label);


request.getSession().removeAttribute("component_obj");


		request.getSession().setAttribute("vs_uri", vs_uri);
		request.getSession().setAttribute("label", _label);


		String component_label = _label;
        //ValueSetObject existing_vs_obj = vsb.getValueSet(vs_uri);
        //ValueSetBean.ComponentObject component = existing_vs_obj.getComponent(component_label);

        ValueSetObject vs = vsb.getCurrentValueSet();

        ValueSetBean.ComponentObject co = null;
        String _edit_action = FacesUtil.getRequestParameter("action");
        if (_edit_action == null || _edit_action.compareTo("edit") != 0) {
			co = vsb.new ComponentObject();

		} else {


String _vs_uri = (String) request.getParameter("vs_uri");
String _component_label = (String) request.getParameter("component_label");


			if (_vs_uri != null && _component_label != null) {
				vs = vsb.getValueSet(_vs_uri);
				co = vs.getComponent(_component_label);
			}
			vsb.setUri(_vs_uri);
			//vsb.setExpression(_component_label);
	    }

      	_logger.debug("rel_search_association: " + _rel_search_association);

      	if (co == null) {
			co = new ValueSetBean().instantiateComponentObject();
		}

        co.setLabel(_label);
        co.setDescription(_description);
        co.setType(_type);

        if (_matchText != null) _matchText = _matchText.trim();

        co.setMatchText(_matchText);
        co.setAlgorithm(_algo);
        co.setVocabulary(_vocabulary);
        co.setPropertyName(_propertyName);

        if (_focusConceptCode != null) _focusConceptCode = _focusConceptCode.trim();
        co.setFocusConceptCode(_focusConceptCode);

        co.setRel_search_association(_rel_search_association);
        co.setInclude_focus_node(_include_focus_node);
        co.setTransitivity(_transitivity);
        co.setSelectedDirection(_selectedDirection);

        co.setValueSetReference(_selectValueSetReference);

		if (_type.compareTo("EntireVocabulary") == 0) {
			String cs_name = DataUtils.getCodingSchemeName(_vocabulary, null);
            co.setCodingSchemeReference(cs_name);
		}

        if (_codes == null) _codes = "";
        co.setCodes(_codes);


        vs.getCompList().put(_label, co);
        if (_expression != null && vs.getExpression() != null && vs.getCompListSize() == 1) {
			vs.setExpression(_expression);
		}

        //vs.setExpression(_expression);

        _message = resource.getString("action_saved");
        _logger.debug("saveComponentSubsetAction returns success");

/*
		request.getSession().removeAttribute("preview_adv_search_vocabulary");
		request.getSession().removeAttribute("preview_selectSearchOption");
		request.getSession().removeAttribute("preview_label");
		request.getSession().removeAttribute("label");
		request.getSession().removeAttribute("preview_focusConceptCode");
		request.getSession().removeAttribute("preview_description");
		request.getSession().removeAttribute("preview_search_string");
		request.getSession().removeAttribute("preview_search_algorithm");
		request.getSession().removeAttribute("preview_rel_search_association");
		request.getSession().removeAttribute("preview_selectProperty");
		request.getSession().removeAttribute("preview_selectValueSetReference");
		request.getSession().removeAttribute("preview_direction");
		request.getSession().removeAttribute("preview_include_focus_node_checkbox");
		request.getSession().removeAttribute("preview_transitivity_checkbox");
		request.getSession().removeAttribute("preview");
		request.getSession().removeAttribute("preview_vs_uri");
*/

        // Validate input
        if (vsb.getUri() == null || vsb.getUri().length() < 1) {
            _message = resource.getString("error_missing_uri");
            request.setAttribute("message", _message);
            request.getSession().setAttribute("label", _label);

            request.getSession().setAttribute("component_obj", co);
            return "message";
        }



        if (message != null) {

            request.getSession().setAttribute("vs_uri", vs_uri);
            request.getSession().setAttribute("label", _label);


			request.setAttribute("message", message);
			request.getSession().setAttribute("component_obj", co);
			return "message";
		}


        if (_label == null || _label.length() < 1) {
            _message = resource.getString("error_missing_label");
            request.setAttribute("message", _message);

            request.getSession().setAttribute("vs_uri", vs_uri);
            request.getSession().setAttribute("label", _label);

            request.getSession().setAttribute("component_obj", co);
            return "error";
        }

        addComponentObject(vs.getUri(), co);
        request.getSession().removeAttribute("component_obj");
        return "success";

    }

    public String cancelComponentSubsetAction() throws Exception {

        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        request.getSession().removeAttribute("preview_component");
        request.getSession().removeAttribute("preview");

		String vs_uri = FacesUtil.getRequestParameter("vs_uri");

        if (vs_uri != null) {
			vsb.setUri(vs_uri);
			request.getSession().setAttribute("vs_uri", vs_uri);
		}

		return "cancel";
	}

    public String closeResolvedComponentSubsetAction() throws Exception {

        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        request.getSession().removeAttribute("closeResolvedComponentSubset");


        String vsd_uri = (String) request.getParameter("vsd_uri");
        String component_label = (String) request.getParameter("component_label");
	    ValueSetBean.ComponentObject component = getComponentObject(vsd_uri, component_label);

	    request.getSession().setAttribute("vs_uri", vsd_uri);
	    request.getSession().setAttribute("component_label", component_label);


        if (component != null) {
			request.getSession().setAttribute("closeResolvedComponentSubset", "true");
			request.getSession().setAttribute("action", "edit");

			request.getSession().setAttribute("label", component_label);

			request.getSession().removeAttribute("vs_obj");
			request.getSession().setAttribute("preview", "true");

            return "edit_component";
		} else {
			request.getSession().setAttribute("closeResolvedComponentSubset", "true");
            request.getSession().setAttribute("preview", "true");
            request.getSession().setAttribute("preview_label", component_label);


			return "add_component";
		}


	}


	public String _searchOption = "code";

	public void setSearchOption(String searchOption) {
		this._searchOption = searchOption;
	}

	public String getSearchOption() {
		return this._searchOption;
	}


    public void searchOptionChangedEvent(ValueChangeEvent event) {
        if (event.getNewValue() == null) {
            return;
		}
        String newValue = (String) event.getNewValue();

       //setSearchOption(newValue);
        setType(newValue);
	}

    public void ontologyChangedEvent(ValueChangeEvent event) {

        if (event.getNewValue() == null) {
            return;
		}
        String newValue = (String) event.getNewValue();

        setVocabulary(newValue);
	}

	public String cancelResolveComponentSubsetAction() {
        ResolvedConceptReferencesIterator iterator = null;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

ValueSetObject vs_obj = (ValueSetObject) request.getSession().getAttribute("vs_obj");//vsb.getValueSet(vsd_uri);

if (vs_obj == null) {
	String message = "ERROR: Value set object is not found in session.";
	request.setAttribute("message", message);
	return "message";
}

		String vs_uri = vs_obj.getUri();
		String component_label = (String) request.getSession().getAttribute("ComponentObjectLabel");

        ValueSetObject existing_vs_obj = vsb.getValueSet(vs_uri);
        ValueSetBean.ComponentObject component = existing_vs_obj.getComponent(component_label);

        if (component == null) {
			return "add_component";
		}
		return "edit_component";

	}

    public boolean isNull(String s) {
		if (s == null) return true;
		s = s.trim();
		if (s.compareTo("") == 0 || s.compareTo("null") == 0) return true;
		return false;
	}


	public ValueSetBean.ComponentObject getComponentObject(String vsd_uri, String component_label) {
	    if (componentObjectHashMap == null) {
			componentObjectHashMap = new HashMap();
		}
		String key = vsd_uri + "$" + component_label;
		if (componentObjectHashMap.containsKey(key)) {
			return (ValueSetBean.ComponentObject) componentObjectHashMap.get(key);
		}

		return null;
    }

	public void addComponentObject(String vsd_uri, ValueSetBean.ComponentObject comp_obj) {
		String component_label = comp_obj.getLabel();
	    if (componentObjectHashMap == null) {
			componentObjectHashMap = new HashMap();
		}
		String key = vsd_uri + "$" + component_label;
		componentObjectHashMap.put(key, comp_obj);

    }

    public void removeComponentObject(String vsd_uri, String component_label) {
	    if (componentObjectHashMap == null) {
			return;
		}
		String key = vsd_uri + "$" + component_label;
		if (!componentObjectHashMap.containsKey(key)) return;
		componentObjectHashMap.remove(key);
	}

} // End of ComponentBean
