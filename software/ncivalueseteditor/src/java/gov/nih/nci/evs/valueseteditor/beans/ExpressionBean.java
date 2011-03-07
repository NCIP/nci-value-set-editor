package gov.nih.nci.evs.valueseteditor.beans;

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
 * Action bean for value set expression operations
 *
 * @author garciawa2
 */
public class ExpressionBean {

	// Local class variables
    private static Logger _logger = Logger.getLogger(ExpressionBean.class);
    private ResourceBundle resource = ResourceBundle.getBundle("gov.nih.nci.evs.valueseteditor.resources.Resources");
    private String _message = null;
    
    // Expression variables
    private String _expText = null;
    
    // Value Set bean
    ValueSetBean vsb = null;

    /**
     * Class constructor
     * @throws Exception
     */
    public ExpressionBean() throws Exception {
    	
    	vsb = (ValueSetBean)FacesContext.getCurrentInstance()
			.getExternalContext().getSessionMap().get("ValueSetBean");
    	
	}     
    
    // ========================================================
    // ====               Getters & Setters                 ===
    // ========================================================

    public String getMessage() {
        return _message;
    }
    
    // -----------
    
    public String getExpText() {
        return _expText;
    }

    public void setExpText(String expText) {
        _expText = expText;
    }
    
    // ========================================================
    // ====                 Action Methods                  ===
    // ========================================================   
    
    public String saveExpressionAction() throws Exception {

    	_logger.debug("Save expression action.");
    	_message = null;	
        
        // Validate input
        if (vsb.getUri() == null || vsb.getUri().length() < 1) {
            _message = resource.getString("error_missing_uri");
            return "error";
        }        
   
        _message = resource.getString("action_saved");
        
        return "sucess";
    }

    public String componentSetAction() throws Exception {

    	_logger.debug("Component set expression action.");
    	_message = null;	
        
        // Validate input
        if (vsb.getUri() == null || vsb.getUri().length() < 1) {
            _message = resource.getString("error_missing_uri");
            return "error";
        }        
        
        return "sucess";
    }    

    public String unionAction() throws Exception {

    	_logger.debug("Union action.");
    	_message = null;	
        
        // Validate input
        if (vsb.getUri() == null || vsb.getUri().length() < 1) {
            _message = resource.getString("error_missing_uri");
            return "error";
        }        
        
        return "sucess";
    }      

    public String intersectionAction() throws Exception {

    	_logger.debug("Intersection action.");
    	_message = null;	
        
        // Validate input
        if (vsb.getUri() == null || vsb.getUri().length() < 1) {
            _message = resource.getString("error_missing_uri");
            return "error";
        }        
        
        return "sucess";
    }     

    public String differenceAction() throws Exception {

    	_logger.debug("Difference action.");
    	_message = null;	
        
        // Validate input
        if (vsb.getUri() == null || vsb.getUri().length() < 1) {
            _message = resource.getString("error_missing_uri");
            return "error";
        }        
        
        return "sucess";
    }     
    
} // End of ExpressionBean
