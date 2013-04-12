/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-value-set-editor/LICENSE.txt for details.
 */

package gov.nih.nci.evs.valueseteditor.utilities;

import java.util.*;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.codingSchemes.CodingScheme;

import org.apache.log4j.Logger;
import javax.faces.model.*;


/**
 * 
 */

/**
 * Search utility for value sets
 *
 * @author kim.ong@ngc.com, garciawa2
 */
public class ValueSetSearchUtil {

    // Local variables
    private static Logger _logger = Logger.getLogger(ValueSetSearchUtil.class);
    private static LexBIGService lbSvc = null;

    List ontologyList = null;
    //HashMap csnv2codingSchemeNameMap = null;
    //HashMap csnv2VersionMap = null;

    /**
     * Constructor
     * @throws Exception
     */
    public ValueSetSearchUtil() throws Exception {
        // Setup lexevs service
        if (lbSvc == null) {
            lbSvc = RemoteServerUtil.createLexBIGService();
        }
    }

    /**
     * Get list all of all concept domains loaded
     * @return
     */
    public Map<String,String> getConceptDomainNames() {
    	/* TODO: Add lexevs data retrieval code here. */
    	Map<String,String> hmap = new HashMap<String,String>();
    	hmap.put("1","AcknowledgementCondition");
    	hmap.put("2","AcknowledgementDetailCode");
    	hmap.put("3","AcknowledgementDetailType");
    	hmap.put("4","AcknowledgementMessageCode");
    	hmap.put("5","AcknowledgementType");
        return hmap;
    }



    public List getOntologyList() throws Exception {

		if (ontologyList != null) return ontologyList;

        ontologyList = new DataUtils().getOntologyList();
        return ontologyList;

    }




    // -----------------------------------------------------
    // Internal utility methods
    // -----------------------------------------------------

    /**
     * @param codingScheme
     * @param version
     * @return
     */
    @SuppressWarnings("unused")
    private static CodingScheme getCodingScheme(String codingScheme,
            String version) {
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        if (version != null)
            versionOrTag.setVersion(version);
        CodingScheme cs = null;
        LexBIGService lbSvc =null;
        try {
            lbSvc = RemoteServerUtil.createLexBIGService();
            cs = lbSvc.resolveCodingScheme(codingScheme, versionOrTag);
        } catch (Exception e) {
            _logger.error(e.getStackTrace());
            return null;
        }
        return cs;
    }

} // End of ValueSetSearchUtil
