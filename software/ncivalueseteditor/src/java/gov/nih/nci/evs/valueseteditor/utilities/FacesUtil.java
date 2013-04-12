/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-value-set-editor/LICENSE.txt for details.
 */

package gov.nih.nci.evs.valueseteditor.utilities;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 * 
 */

/**
 * Faces Utility Class
 * @author garciawa2
 */
public class FacesUtil {

    /**
     * Get a parameter (HTTP GET)
     * @param name
     * @return
     */
    public static String getRequestParameter(String name) {
        return (String) FacesContext.getCurrentInstance().getExternalContext()
            .getRequestParameterMap().get(name);
    }

    /**
     * Get an attribute (HTTP POST)
     * @param event
     * @param name
     * @return
     */
    public static String getActionAttribute(ActionEvent event, String name) {
        return (String) event.getComponent().getAttributes().get(name);
    }
   
} // End of FacesUtil