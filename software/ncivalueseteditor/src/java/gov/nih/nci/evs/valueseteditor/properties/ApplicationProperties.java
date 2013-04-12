/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-value-set-editor/LICENSE.txt for details.
 */

package gov.nih.nci.evs.valueseteditor.properties;

import java.util.*;

import org.apache.log4j.*;

/**
 * 
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
