/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-value-set-editor/LICENSE.txt for details.
 */

package gov.nih.nci.evs.valueseteditor.properties;

import java.util.*;
import javax.xml.parsers.*;
import org.apache.log4j.*;
import org.w3c.dom.*;

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
public class PropertyFileParser {
    private static Logger _logger = Logger.getLogger(PropertyFileParser.class);
    private HashMap<String,String> _configurableItemMap;
    private HashMap<String,String> _securityTokenHashMap;
    private String _xmlfile;
    private Document _dom;

    /**
     * Initialize lists
     */
    private void init() {
        _configurableItemMap = new HashMap<String,String>();
        _securityTokenHashMap = new HashMap<String,String>();
    }

    /**
     * @param xmlfile
     */
    public PropertyFileParser(String xmlfile) {
    	init();
        _xmlfile = xmlfile;
    }

    /**
     * Constructor
     */
    public PropertyFileParser() {
        init();
    }

    /**
     * Parse XML file
     * @throws Exception
     */
    public void run() throws Exception {
        parseXmlFile(_xmlfile);
        parseDocument();
    }

    /**
     * @return
     */
    public HashMap<String,String> getConfigurableItemMap() {
        return _configurableItemMap;
    }

    /**
     * @return
     */
    public HashMap<String,String> getSecurityTokenMap() {
        return _securityTokenHashMap;
    }

    /**
     * @param xmlfile
     */
    private void parseXmlFile(String xmlfile) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        _dom = db.parse(xmlfile);
    }

    /**
     * Parse document
     */
    private void parseDocument() {
        Element docEle = _dom.getDocumentElement();

        NodeList items = docEle.getElementsByTagName("ConfigurableItem");
        if (items != null && items.getLength() > 0) {
            for (int i = 0; i < items.getLength(); i++) {
                Element el = (Element) items.item(i);
                setNameValueItem(_configurableItemMap,el);
            }
        } else {
        	_logger.debug("Warning! 'ConfigurableItem' tag not found in property file!");
        }

        NodeList tokens = docEle.getElementsByTagName("SecurityTokenHolder");
        if (tokens != null && tokens.getLength() > 0) {
            for (int i = 0; i < tokens.getLength(); i++) {
                Element el = (Element) tokens.item(i);
                setNameValueItem(_securityTokenHashMap,el);
            }
        } else {
        	_logger.debug("Warning! 'SecurityTokenHolder' tag not found in property file!");
        }

    }

    /**
     * @param map
     * @param displayItemElement
     */
    private void setNameValueItem(HashMap<String,String> map, Element displayItemElement) {
        String key = getTextValue(displayItemElement, "name");
        String value = getTextValue(displayItemElement, "value");
        map.put(key, value);
    }

    /**
     * @param ele
     * @param tagName
     * @return
     */
    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            if (el != null && el.getFirstChild() != null) {
            	textVal = el.getFirstChild().getNodeValue();
			}
        }
        if (textVal == null) return null;
        return textVal.trim();
    }

}
