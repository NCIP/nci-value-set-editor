/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-value-set-editor/LICENSE.txt for details.
 */

package gov.nih.nci.evs.valueseteditor.beans;

import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

/**
 * 
 */

/**
 * Action bean for value set expression operations
 *
 * @author kim.ong@ngc.com, garciawa2
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
