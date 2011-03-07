package gov.nih.nci.evs.valueseteditor.beans;

import gov.nih.nci.evs.valueseteditor.beans.ValueSetBean.ValueSetObject;
import gov.nih.nci.evs.valueseteditor.utilities.FacesUtil;
import gov.nih.nci.evs.valueseteditor.utilities.ValueSetSearchUtil;

import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

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
 * Action bean for component operations
 *
 * @author garciawa2
 */
public class ComponentBean {

	// Local class variables
    private static Logger _logger = Logger.getLogger(ComponentBean.class);
    private ResourceBundle resource = ResourceBundle.getBundle("gov.nih.nci.evs.valueseteditor.resources.Resources");
    private String _message = null;
    
    // Internal maps   
    private String _selectedOntology = null;
    private Map<String,String> _selectedOntologyList = null;   
    
    // Component variables
    private String _label = null;
    private String _description = null;
    private String _type = null;
    private String _matchText = null;
    private String _algo = null;
    
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
	        _algo = co.getAlgo();
		}
    	
	}     
    
    // ========================================================
    // ====               Getters & Setters                 ===
    // ========================================================

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
	
	public String getSelectedOntology() {		
		return _selectedOntology;
	}	
	
	public void setSelectedOntology(String selectedOntology) {		
		this._selectedOntology = selectedOntology;
	}	
	
	public Map<String,String> getOntologyList() {
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
    
    // ========================================================
    // ====                 Action Methods                  ===
    // ========================================================   
    
    public String saveComponentAction() throws Exception {

    	_message = null;	
        
        // Validate input
        if (vsb.getUri() == null || vsb.getUri().length() < 1) {
            _message = resource.getString("error_missing_uri");
            return "error";
        }        
        if (_label == null || _label.length() < 1) {
            _message = resource.getString("error_missing_label");
            return "error";
        }
        
        _logger.debug("Saving component.");
        
        ValueSetObject vs = vsb.getCurrentValueSet();
        ValueSetBean.ComponentObject co = vsb.new ComponentObject();
        co.setLabel(_label);
        co.setDescription(_description);
        co.setType(_type);
        co.setMatchText(_matchText);
        co.setAlgo(_algo);
        vs.getCompList().put(_label, co);        
   
        _message = resource.getString("action_saved");
        
        return "sucess";
    }
    
    public String previewComponentAction() {
    	_message = null;
    	_logger.debug("Previewing component.");
    	
    	return "previewcomponent";
    } 
    
    public String openPopupAction() {
    	_message = null;
    	_logger.debug("Popup opened.");
    	return null;
    }

} // End of ComponentBean
