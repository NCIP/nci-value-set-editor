package gov.nih.nci.evs.valueseteditor.beans;

import gov.nih.nci.evs.valueseteditor.utilities.FacesUtil;
import gov.nih.nci.evs.valueseteditor.utilities.ValueSetSearchUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.component.html.HtmlSelectBooleanCheckbox;

import org.apache.log4j.Logger;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction
 * with the National Cancer Institute, and so to the extent government
 * employees are co-authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the disclaimer of Article 3,
 *      below. Redistributions in binary form must reproduce the above
 *      copyright notice, this list of conditions and the following
 *      disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution,
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIT and the National
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not
 *      authorize the recipient to use any trademarks owned by either NCI
 *      or NGIT
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT,
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * Action bean for cart operations
 *
 * @author garciawa2
 */
public class ValueSetBean {

	// Local class variables
    private static Logger _logger = Logger.getLogger(ValueSetBean.class);
    private ResourceBundle resource = ResourceBundle.getBundle("gov.nih.nci.evs.valueseteditor.resources.Resources");
    private HashMap<String, ValueSetObject> _cart = null;
    private String _message = null;
    
    // Internal maps
    private String _selectedConceptDomain = null;
    private Map<String,String> _selectedConceptDomainList = null;    
    private String _selectedOntology = null;
    private Map<String,String> _selectedOntologyList = null;   
    
    // Metadata variables
    private String _uri = null;
    private String _sources = null;
     
    // Error messages
    static public final String NO_VALUE_SETS = "No value sets in cart.";
    static public final String NOTHING_SELECTED = "No value sets selected.";
    static public final String DUPLICATE_VS = "Duplicate URI.";

    /**
     * Class constructor
     * @throws Exception
     */
    public ValueSetBean() throws Exception {
    	ValueSetSearchUtil util = new ValueSetSearchUtil();
    	if (_selectedConceptDomainList == null)
    		_selectedConceptDomainList = util.getConceptDomainNames();
    	if (_selectedOntologyList == null)
    		_selectedOntologyList = util.getOntologyList();
    	if (_cart == null) _init();
	}    
    
    // ========================================================
    // ====               Getters & Setters                 ===
    // ========================================================

    public int getCount() {
        if (_cart == null) return 0;
        return _cart.size();
    }

    public Collection<ValueSetObject> getValueSetList() {
        if (_cart == null) _init();
        return _cart.values();
    }

    public ValueSetObject getCurrentValueSet() {
        if (_cart == null) {
        	_init();
        	return null;
        }
        if (_uri == null || _uri.length() < 1)
        	return null;       
        return _cart.get(_uri);
    }    
    
    public String getMessage() {
        return _message;
    }

     public String getUri() {
        return _uri;
    }

    public void setUri(String uri) {
        _uri = uri;
    }
    
    public int getComponentCount() {
    	if (_uri == null || _uri.length() < 1) return 0;
    	return _cart.get(_uri).getCompListSize();
    }
 
    public Collection<ComponentObject> getComponentObjectList() {
    	if (_uri == null || _uri.length() < 1) return null;
    	return _cart.get(_uri)._compList.values();
    }    
    
    // =======================
   
	public String getSelectedConceptDomain() {		
		return _selectedConceptDomain;
	}
	
	public void setSelectedConceptDomain(String selectedConceptDomain) {		
		this._selectedConceptDomain = selectedConceptDomain;
	}	

	public Map<String,String> getConceptDomainList() {
		return _selectedConceptDomainList;
	}	
	
	// =======================
	
	public String getSelectedOntology() {		
		return _selectedOntology;
	}	
	
	public void setSelectedOntology(String selectedOntology) {		
		this._selectedOntology = selectedOntology;
	}	
	
	public Map<String,String> getOntologyList() {
		return _selectedOntologyList;
	}		
	
    // =======================
    
    /**
     * Get metadata sources
     * @return
     */
    public String getSources() {
        return _sources;
    }

    /**
     * Set metadata sources
     * @param sources
     */
    public void setSources(String sources) {
        _sources = sources;
    }

    // ========================================================
    // ====                 Action Methods                  ===
    // ========================================================   
    
    public String saveMetadataAction() {

    	_message = null;	
        _logger.debug("Saving metadata to cart.");
        
        // Validate input
        if (_uri == null || _uri.length() < 1) {
            _message = resource.getString("error_missing_uri");
            _logger.debug("URI is null.");
            return "error";
        }
  
        ValueSetObject item = null;
        if (_cart.containsKey(_uri)) {
        	item = _cart.get(_uri);
        	_logger.debug("Updating value set.");
        } else {
        	item = new ValueSetObject();
        	_logger.debug("Adding value set to cart.");
        }
    	    	
    	item.setUri(_uri);
    	item.setConceptDomain(_selectedConceptDomain);
    	item.setCodingScheme(_selectedOntology);
    	item.setSources(_sources);

        _cart.put(_uri,item);            
        _message = resource.getString("action_saved");
        
        return "sucess";
    }

    // *** Value Set List Buttons
    
    public String newValueSetAction() {
    	_message = null;
    	_logger.debug("Creating new value set.");
    	
    	clear();
    	return "newvalueset";
    } 
    
    public String removeFromCartAction() {

    	_message = null;
        for (Iterator<ValueSetObject> i = getValueSetList().iterator(); i.hasNext();) {
            ValueSetObject item = (ValueSetObject)i.next();
            if (item.getCheckbox().isSelected()) {
                if (_cart.containsKey(item._uri))
                    i.remove();
            }
        }

        return "removevalueset";
    }    
 
    // Value Item Action Methods
    
    public String editValueSetAction() {
    	
    	_message = null;
    	String uriParam = FacesUtil.getRequestParameter("uriParam");
    	_logger.debug("Editng value set: " + uriParam);
    	
    	loadUI(uriParam);
    	
    	return "editvalueset";
    }     
  
    public String previewValueSetAction() {
    	_message = null;
    	_logger.debug("Previewing value set.");
    	    	
    	return "previewvalueset";
    }    
    
    public String exportValueSetAction() {
    	_message = null;
    	_logger.debug("Exporting value.");
    	
    	return "exportvalueset";
    }    
    
    // Component Action Methods
    
    public String editComponentAction() {
    	
    	_message = null;
    	String labelParam = FacesUtil.getRequestParameter("labelParam");
    	_logger.debug("Editng value set: " + labelParam);
    	
    	return "editcomponent";
    }     
 
    public String removeComponentAction() {

    	_message = null;
    	_logger.debug("Removing component.");

        return "removecomponent";
    }    
    
    // ******************** Internal Class Methods ************************

    /**
     * Initialize the cart container
     */
    private void _init() {
        if (_cart == null) _cart = new HashMap<String, ValueSetObject>();
    }

    /**
     * Clear UI data
     */
    private void clear() {
    	_uri = null;
        _sources = null;
        _selectedConceptDomain = null;
        _selectedOntology = null;
    }    
    
    /**
     * Load UI with Value set data
     * @param uriParam
     */
    private void loadUI(String uriParam) {
    	ValueSetObject vso = _cart.get(uriParam);
    	_uri = vso.getUri();
        _sources = vso.getSources();
        _selectedConceptDomain = vso.getConceptDomain();
        _selectedOntology = vso.getCodingScheme();    	
    }
    
    /**
     * Subclass to hold contents of the cart
     * @author garciawa2
     */
    public class ValueSetObject {

		private String _uri = null;
        private String _conceptDomain = null;
        private String _codingScheme = null;
        private String _sources = null;
        private HtmlSelectBooleanCheckbox _checkbox = null;
        private Map<String,ComponentObject> _compList = null; 

        /**
         * Constructor
         */
        public ValueSetObject() {
        	_compList = new HashMap<String,ComponentObject>();
		}        
        
        // Getters & setters

        public String getUri() {
            return this._uri;
        }

        public void setUri(String uri) {
            this._uri = uri;
        }

        public String getConceptDomain() {
            return this._conceptDomain;
        }

        public void setConceptDomain(String conceptDomain) {
            this._conceptDomain = conceptDomain;
        }

        public String getCodingScheme() {
            return this._codingScheme;
        }

        public void setCodingScheme(String codingScheme) {
            this._codingScheme = codingScheme;
        }

        public String getSources() {
            return this._sources;
        }

        public void setSources(String sources) {
            this._sources = sources;
        }

        public HtmlSelectBooleanCheckbox getCheckbox() {
            return _checkbox;
        }

        public void setCheckbox(HtmlSelectBooleanCheckbox checkbox) {
            _checkbox = checkbox;
        }

        public Map<String,ComponentObject> getCompList() {
            return this._compList;
        }

        public int getCompListSize() {
            if (_compList == null) return 0;
            return _compList.size();
        }     
        
        // *** Private Methods ***

        private boolean getSelected() {
            return _checkbox.isSelected();
        }

    } // End of Concept

    public class ComponentObject {
        private String _label = null;
        private String _description = null;
        private String _type = null;
        private String _matchText = null;
        private String _algo = null;
        
        // Getters & setters
        
        public String getLabel() {
        	return _label;
        }
        
        public void setLabel(String label) {
        	this._label = label;
        }
        
        public String getDescription() {
        	return _description;
        }
        
        public void setDescription(String description) {
        	this._description = description;
        }

        public String getType() {
        	return _type;
        }
        
        public void setType(String type) {
        	this._type = type;
        }        

        public String getMatchText() {
        	return _matchText;
        }
        
        public void setMatchText(String matchText) {
        	this._matchText = matchText;
        }         

        public String getAlgo() {
        	return _algo;
        }
        
        public void setAlgo(String algo) {
        	this._algo = algo;
        }        
        
    } // End of ComponentObject

    //**
    //* Utility methods
    //**    
    
    /**
     * Dump contents of cart object
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Listing cart contents...\n");

        if (_cart != null && _cart.size() > 0) {
            sb.append("\tCart:\n");
            for (Iterator<ValueSetObject> i = getValueSetList().iterator(); i.hasNext();) {
                ValueSetObject item = (ValueSetObject)i.next();
                sb.append("\t                URI  = " + item._uri + "\n");
                sb.append("\t      Concept Domain = " + item._conceptDomain + "\n");
                sb.append("\t       Coding Scheme = " + item._codingScheme + "\n");
                sb.append("\t             Sources = " + item._sources + "\n");
                sb.append("\t            Selected = " + item.getSelected() + "\n");
                sb.append("\t Component list size = " + item.getCompListSize() + "\n");
            }
        } else {
            sb.append("Cart is empty.");
        }

        return sb.toString();
    }

} // End of ValueSetBean
