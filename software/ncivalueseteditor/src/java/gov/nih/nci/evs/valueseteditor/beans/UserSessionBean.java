/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-value-set-editor/LICENSE.txt for details.
 */

package gov.nih.nci.evs.valueseteditor.beans;
import java.util.*;
import javax.faces.context.FacesContext;

import gov.nih.nci.evs.valueseteditor.properties.ApplicationProperties;

/**
 * 
 */

/**
 * @author kim.ong@ngc.com, garciawa2
 * UserSessionBean
 */
public class UserSessionBean {

    private Throwable _exception = null;

	public String getBuilddate() throws Exception {
        return ApplicationProperties.getBuilddate();
    }

    public String getAppversion() throws Exception {
        return ApplicationProperties.getAppversion();
    }

    public String getBuildtag() throws Exception {
        return ApplicationProperties.getAppbuildtag();
    }

    public String getEvsserviceurl() throws Exception {
        return ApplicationProperties.getServiceurl();
    }

    public boolean getDebug() throws Exception {
        return ApplicationProperties.getDebug();
    }

	private static final String SERVLET_EXCEPTION_KEY = "javax.servlet.error.exception";

	public Throwable getException() {
		initializeException();
		if (_exception == null) return new Throwable("Unknown error.");
		return _exception;
	}

    // -----------------------------------------------------
    // Internal utility methods
    // -----------------------------------------------------

	/**
	 * Find the exception method
	 */
	private void initializeException() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		if (ctx.getExternalContext().getRequestMap()
				.containsKey(SERVLET_EXCEPTION_KEY)) {
			this._exception = (Throwable) ctx.getExternalContext()
					.getRequestMap().remove(SERVLET_EXCEPTION_KEY);
		} else if (ctx.getExternalContext().getSessionMap()
				.containsKey(SERVLET_EXCEPTION_KEY)) {
			this._exception = (Throwable) ctx.getExternalContext()
					.getSessionMap().remove(SERVLET_EXCEPTION_KEY);
		}
	}

    public static List getResultsPerPageValues() {
        List resultsPerPageList = new ArrayList();
        resultsPerPageList.add("10");
        resultsPerPageList.add("25");
        resultsPerPageList.add("50");
        resultsPerPageList.add("75");
        resultsPerPageList.add("100");
        resultsPerPageList.add("250");
        resultsPerPageList.add("500");
        return resultsPerPageList;
    }

} // End of UserSessionBean
