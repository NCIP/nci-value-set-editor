package gov.nih.nci.evs.valueseteditor.properties;

import java.util.*;

import org.apache.log4j.*;

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
 * @author EVS Team
 * @version 1.0
 *
 *          Modification history Initial implementation kim.ong@ngc.com
 *
 */

public class ApplicationProperties {
    private static Logger _logger =
        Logger.getLogger(ApplicationProperties.class);

    // Public Constants
    public static final String LG_CONFIG_FILE = "LG_CONFIG_FILE";

    // Private Constants
    private static final String PROP_FILE = "NCIvseProperties";
    private static final String DEBUG = "DEBUG";
    private static final String BUILD_DATE = "BUILD_DATE";
    private static final String APP_VERSION = "APPLICATION_VERSION";
    private static final String EVS_SERVICE_URL = "EVS_SERVICE_URL";
    private static final String APP_BUILD_TAG = "APP_BUILD_TAG";

    private static final String EHCACHE_XML_PATHNAME = "EHCACHE_XML_PATHNAME";

    private static final String NCIT_URL = "NCIT_URL";

    // Private Variables
    private static ApplicationProperties appProperties = null;
    private static boolean _debug = false;
    private static String _service_url = null;
    private static String _build_date = null;
    private static String _app_version = null;
    private static String _lg_config_file = null;
    private static String _app_build_tag = null;
    private static HashMap<String,String> _configurableItemMap;
    private static HashMap<String,String> _securityTokenMap;

    private static String _ncit_url = null;

    /**
     * Private constructor for singleton pattern.
     */
    private ApplicationProperties() {
    	// Prevent class from being explicitly instantiated
    }

    /**
     * Gets the single instance of Properties.
     * @return single instance of ApplicationProperties
     * @throws Exception the exception
     */
    public static ApplicationProperties getInstance() throws Exception {
        if (appProperties == null) {
            synchronized (ApplicationProperties.class) {
                //if (appProperties == null) {
                	appProperties = new ApplicationProperties();
                    loadProperties();

                    _debug = Boolean.parseBoolean(getProperty(DEBUG));
                    _build_date =
                    	ApplicationProperties
                            .getProperty(ApplicationProperties.BUILD_DATE);
                    _app_version =
                    	ApplicationProperties
                            .getProperty(ApplicationProperties.APP_VERSION);
                    _service_url =
                    	ApplicationProperties
                            .getProperty(ApplicationProperties.EVS_SERVICE_URL);
                    _lg_config_file =
                    	ApplicationProperties
                            .getProperty(ApplicationProperties.LG_CONFIG_FILE);

                    _app_build_tag =
                    	ApplicationProperties
                            .getProperty(ApplicationProperties.APP_BUILD_TAG);

                    _ncit_url =
                    	ApplicationProperties
                            .getProperty(ApplicationProperties.NCIT_URL);
               // }
            }
        }

        return appProperties;
    }

    // ***************** Getters ****************************

    public static boolean getDebug() throws Exception {
    	if (appProperties == null) getInstance();
        return _debug;
    }

    public static String getBuilddate() throws Exception {
    	if (appProperties == null) getInstance();
        return _build_date;
    }

    public static String getAppversion() throws Exception {
    	if (appProperties == null) getInstance();
        return _app_version;
    }

    public static String getServiceurl() throws Exception {
    	if (appProperties == null) getInstance();
        return _service_url;
    }

    public static String getLgconfigfile() throws Exception {
    	if (appProperties == null) getInstance();
        return _lg_config_file;
    }

    public static String getAppbuildtag() throws Exception {
    	if (appProperties == null) getInstance();
        return _app_build_tag;
    }

    public static HashMap<String,String> getSecurityTokenMap() throws Exception {
    	if (appProperties == null) getInstance();
        return _securityTokenMap;
    }

    public static String getNCITurl() throws Exception {
    	if (appProperties == null) getInstance();
        return _ncit_url;
    }

    // ***************** Internal methods *******************

    /**
     * @param key
     * @return
     * @throws Exception
     */
    private static String getProperty(String key) throws Exception {
        String ret_str = (String) _configurableItemMap.get(key);
        if (ret_str == null)
            return null;
        if (ret_str.compareToIgnoreCase("null") == 0)
            return null;
        return ret_str;
    }

    /**
     * @throws Exception
     */
    private static void loadProperties() throws Exception {
        String propertyFile =
            System.getProperty(PROP_FILE);

        _logger.info("Properties file Location = " + propertyFile);

        PropertyFileParser parser = new PropertyFileParser(propertyFile);
        parser.run();

        _configurableItemMap = parser.getConfigurableItemMap();
        _securityTokenMap = parser.getSecurityTokenMap();
    }

} // End of ApplicationProperties
