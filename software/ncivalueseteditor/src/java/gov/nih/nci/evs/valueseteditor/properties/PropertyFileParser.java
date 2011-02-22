package gov.nih.nci.evs.valueseteditor.properties;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.apache.log4j.*;
import org.w3c.dom.*;
import org.xml.sax.*;

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
public class PropertyFileParser {
    private static Logger _logger = Logger.getLogger(PropertyFileParser.class);
    private HashMap _configurableItemMap;
    private String _xmlfile;
    private Document _dom;

    /**
     * Constructor
     */
    public PropertyFileParser() {
        _configurableItemMap = new HashMap();
    }

    /**
     * @param xmlfile
     */
    public PropertyFileParser(String xmlfile) {
        _configurableItemMap = new HashMap();
        _xmlfile = xmlfile;
    }

    /**
     * Parse XML file
     */
    public void run() {
        parseXmlFile(_xmlfile);
        parseDocument();
    }

    /**
     * @return
     */
    public HashMap getConfigurableItemMap() {
        return _configurableItemMap;
    }

    /**
     * @param xmlfile
     */
    private void parseXmlFile(String xmlfile) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            _dom = db.parse(xmlfile);
        } catch (ParserConfigurationException pce) {
        	_logger.error(pce.getMessage());        	
        } catch (SAXException se) {
        	_logger.error(se.getMessage());       
        } catch (IOException ioe) {
        	_logger.error(ioe.getMessage());       
        }
    }

    /**
     * Parse document
     */
    private void parseDocument() {
        Element docEle = _dom.getDocumentElement();

        NodeList list2 = docEle.getElementsByTagName("ConfigurableItem");
        if (list2 != null && list2.getLength() > 0) {
            for (int i = 0; i < list2.getLength(); i++) {
                Element el = (Element) list2.item(i);
                getConfigurableItem(el);
            }
        }
    }

    /**
     * @param displayItemElement
     */
    private void getConfigurableItem(Element displayItemElement) {
        String key = getTextValue(displayItemElement, "key");
        String value = getTextValue(displayItemElement, "value");
        _configurableItemMap.put(key, value);
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
            textVal = el.getFirstChild().getNodeValue();
        }
        return textVal.trim();
    }

}
