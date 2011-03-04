package gov.nih.nci.evs.valueseteditor.beans;

import gov.nih.nci.evs.valueseteditor.utilities.ValueSetSearchUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.component.html.HtmlSelectBooleanCheckbox;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
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
    
    // Set metadata variables
    private String _uri = null;
    private String _scheme = null;
    private String _sources = null;
    private String _selectedConceptDomain = null;
    private Map<String,String> _selectedConceptDomainList = null;    
    private String _selectedOntology = null;
    private Map<String,CodingSchemeSummary> _selectedOntologyList = null; 
    
    // Error messages
    static public final String NO_VALUE_SETS = "No value sets in cart.";
    static public final String NOTHING_SELECTED = "No value sets selected.";

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
	}    
    
    // ========================================================
    // ====               Getters & Setters                 ===
    // ========================================================

    public int getCount() {
        if (_cart == null) return 0;
        return _cart.size();
    }

    public Collection<ValueSetObject> getValuesets() {
        if (_cart == null) _init();
        return _cart.values();
    }

    public String getMessage() {
        String text = _message;
        _message = null;        // Clear last message
        return text;
    }

    // ****************  Metadata Entries  ******************

     public String getUri() {
        return _uri;
    }

    public void setUri(String uri) {
        _uri = uri;
    }
    
    // =======================
   
	public String getSelectedConceptDomain() {		
		return _selectedConceptDomain;
	}

	public void setSelectedConceptDomain(String selectedConceptDomain) {		
		this._selectedConceptDomain = selectedConceptDomain;
	}	
	
	// =======================
	
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

	// =======================
	
	public Map<String,CodingSchemeSummary> getOntologyList() {
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

    /**
     * Add value set definition to cart
     * @return
     */
    public String addToCart() {
        return null;
    }

    /**
     * Edit value set
     * @return
     */
    public String editValueSet() {
        return null;
    }

    /**
     * Remove concept(s) from the Cart
     * @return
     */
    public String removeFromCart() {

        for (Iterator<ValueSetObject> i = getValuesets().iterator(); i.hasNext();) {
            ValueSetObject item = (ValueSetObject)i.next();
            if (item.getCheckbox().isSelected()) {
                if (_cart.containsKey(item.code))
                    i.remove();
            }
        }

        return null;
    }

    public String addMetadataAction() {

        _logger.debug("Adding metadata to cart.");
        // Validate input
        if (_uri == null || _uri.length() < 1) {
            _message = resource.getString("error_missing_uri");
            return null;
        }

        _message = resource.getString("action_saved");

        return null;
    }

    // ******************** Internal Class Methods ************************

    /**
     * Initialize the cart container
     */
    private void _init() {
        if (_cart == null) _cart = new HashMap<String, ValueSetObject>();
    }

    /**
     * Subclass to hold contents of the cart
     * @author garciawa2
     */
    public class ValueSetObject {
        private String code = null;
        private String codingScheme = null;
        private String nameSpace = null;
        private String name = null;
        private String version = null;
        private String url = null;
        private String semanticType = null;
        private HtmlSelectBooleanCheckbox _checkbox = null;

        // Getters & setters

        public String getCode() {
            return this.code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCodingScheme() {
            return this.codingScheme;
        }

        public void setCodingScheme(String codingScheme) {
            this.codingScheme = codingScheme;
        }

        public String getNameSpace() {
            return this.nameSpace;
        }

        public void setNameSpace(String namespace) {
            this.nameSpace = namespace;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return this.version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSemanticType() {
            return this.semanticType;
        }

        public void setSemanticType(String semanticType) {
            this.semanticType = semanticType;
        }

        public HtmlSelectBooleanCheckbox getCheckbox() {
            return _checkbox;
        }

        public void setCheckbox(HtmlSelectBooleanCheckbox checkbox) {
            _checkbox = checkbox;
        }

        // *** Private Methods ***

        private void setSelected(boolean selected) {
            _checkbox.setSelected(selected);
        }

        private boolean getSelected() {
            return _checkbox.isSelected();
        }

    } // End of Concept

    //**
    //* Utility methods
    //**

    /**
     * Test any concept in the cart has been selected
     * @return
     */
    private boolean hasSelected() {
        if (_cart != null && _cart.size() > 0) {
            for (Iterator<ValueSetObject> i = getValuesets().iterator(); i.hasNext();) {
                ValueSetObject item = (ValueSetObject)i.next();
                if (item.getSelected()) return true;
            }
        }
        return false;
    }

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
            for (Iterator<ValueSetObject> i = getValuesets().iterator(); i.hasNext();) {
                ValueSetObject item = (ValueSetObject)i.next();
                sb.append("\t         Code = " + item.code + "\n");
                sb.append("\tCoding scheme = " + item.codingScheme + "\n");
                sb.append("\t      Version = " + item.version + "\n");
                sb.append("\t   Name space = " + item.nameSpace + "\n");
                sb.append("\t         Name = " + item.name + "\n");
                sb.append("\t     Selected = " + item.getSelected() + "\n");
                sb.append("\t          URL = " + item.url + "\n");
                sb.append("\tSemantic Type = " + item.semanticType + "\n");
            }
        } else {
            sb.append("Cart is empty.");
        }

        return sb.toString();
    }

    /**
     * Clean a string for use in file type CSV
     * @param str
     * @return
     */
    private String clean(String str) {
        String tmpStr = str.replace('"', ' ');
        return tmpStr;
    }

} // End of ValueSetBean
