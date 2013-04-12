/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-value-set-editor/LICENSE.txt for details.
 */

package gov.nih.nci.evs.valueseteditor.utilities;

import java.util.*;

import org.LexGrid.LexBIG.caCore.interfaces.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Impl.*;

import gov.nih.nci.system.client.*;

import gov.nih.nci.evs.security.SecurityToken;
import gov.nih.nci.evs.valueseteditor.properties.ApplicationProperties;
import org.apache.log4j.*;

import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;


/**
 * 
 */

/**
 * @author EVS Team
 * @version 1.0
 *
 *     Modification history Initial implementation kim.ong@ngc.com
 *
 */
public class RemoteServerUtil {
	private static Logger _logger = Logger.getLogger(RemoteServerUtil.class);
	private static boolean _debug = false;
	public static final String SEPARATOR =
		"----------------------------------------" +
		"----------------------------------------";

	/**
	 * Constructor
	 */
	public RemoteServerUtil() {
		// Do nothing
	}

	/**
	 * @return
	 * @throws Exception
	 */


	public static LexBIGService createLexBIGService() { //throws Exception {
		String url = null;
		try {
			url = ApplicationProperties.getServiceurl();
			return createLexBIGService(url);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


	/**
	 * @param serviceUrl
	 * @return
	 */
	private static LexBIGService createLexBIGService(String serviceUrl) {
		try {
			if (serviceUrl == null || serviceUrl.compareTo("") == 0 || serviceUrl.compareTo("null") == 0) {
				String lg_config_file = ApplicationProperties.getLgconfigfile();

				System.setProperty(ApplicationProperties.LG_CONFIG_FILE, lg_config_file);
                LexBIGService lbSvc = null;
                try {
					//lbSvc = new LexBIGServiceImpl();
					lbSvc = LexBIGServiceImpl.defaultInstance();
				} catch (Exception ex) {
					System.out.println("(*) createLexBIGService exception ???");
				}

				return lbSvc;
			}
			if (_debug) {
				_logger.debug(SEPARATOR);
				_logger.debug("LexBIGService(remote): " + serviceUrl);
			}

			LexEVSApplicationService lexevsService = (LexEVSApplicationService) ApplicationServiceProvider
					.getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");
			lexevsService = registerAllSecurityTokens(lexevsService);
			return (LexBIGService) lexevsService;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param lexevsService
	 * @return
	 * @throws Exception
	 */
	public static LexEVSApplicationService registerAllSecurityTokens(
			LexEVSApplicationService lexevsService) throws Exception {
		HashMap<String,String> map = ApplicationProperties.getSecurityTokenMap();
		map.entrySet().iterator();

		for (Map.Entry<String, String> entry: map.entrySet()) {
			lexevsService = registerSecurityToken(lexevsService,
					entry.getKey(), entry.getValue());

		}
		return lexevsService;
	}

	/**
	 * @param lexevsService
	 * @param codingScheme
	 * @param token
	 * @return
	 */
	public static LexEVSApplicationService registerSecurityToken(
			LexEVSApplicationService lexevsService, String codingScheme,
			String token) {
		SecurityToken securityToken = new SecurityToken();
		securityToken.setAccessToken(token);
		Boolean retval = null;
		try {
			retval = lexevsService.registerSecurityToken(codingScheme,
					securityToken);

			if (retval == null) {
				_logger.error("WARNING: Registration of SecurityToken failed.");
			}

			/*
			if (retval != null && retval.equals(Boolean.TRUE)) { //
				_logger.debug("Registration of SecurityToken was successful.");
			} else {
				_logger.error("WARNING: Registration of SecurityToken failed.");
			}
			*/
		} catch (Exception e) {
			_logger.error("WARNING: Registration of SecurityToken failed.");
		}
		return lexevsService;
	}



    public static LexEVSDistributed getLexEVSDistributed() {
		String url = "http://ncias-d488-v.nci.nih.gov:29080/lexevsapi60";
		ApplicationProperties properties = null;
		try {
            properties = ApplicationProperties.getInstance();
            url = properties.getServiceurl();
            return getLexEVSDistributed(url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
	}


    public static LexEVSDistributed getLexEVSDistributed(String serviceUrl) {
		try {
			LexEVSDistributed distributed =
				(LexEVSDistributed)
				ApplicationServiceProvider.getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");

			return distributed;
		} catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}


    public static LexEVSValueSetDefinitionServices getLexEVSValueSetDefinitionServices() {

		ApplicationProperties properties = null;
		try {
            properties = ApplicationProperties.getInstance();
            String serviceUrl = properties.getServiceurl();
            if (serviceUrl == null || serviceUrl.compareTo("") == 0 || serviceUrl.compareToIgnoreCase("null") == 0) {
				return LexEVSValueSetDefinitionServicesImpl.defaultInstance();
			}
			LexEVSDistributed distributed = getLexEVSDistributed();
			LexEVSValueSetDefinitionServices vds = distributed.getLexEVSValueSetDefinitionServices();
			return vds;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
	}


    public static LexEVSValueSetDefinitionServices getLexEVSValueSetDefinitionServices(String serviceUrl) {
		try {
			LexEVSDistributed distributed =
				(LexEVSDistributed)
				ApplicationServiceProvider.getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");

			LexEVSValueSetDefinitionServices vds = distributed.getLexEVSValueSetDefinitionServices();
			return vds;
		} catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}




} // End of RemoteServerUtil

