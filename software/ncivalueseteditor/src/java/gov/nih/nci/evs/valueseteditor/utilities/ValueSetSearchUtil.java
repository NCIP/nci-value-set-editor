package gov.nih.nci.evs.valueseteditor.utilities;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;

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
 * Search utility for value sets
 *
 * @author garciawa2
 */
public class ValueSetSearchUtil {

    // Local variables
    private static Logger _logger = Logger.getLogger(ValueSetSearchUtil.class);
    private static LexBIGService lbSvc = null;
    private static LexBIGServiceConvenienceMethods lbscm = null;

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
    	hmap.put("AcknowledgementCondition","1");
        return hmap;
    }
    
    /**
     * Retrieve list of ontologies
     * @return
     * @throws LBInvocationException 
     */
    public Map<String,CodingSchemeSummary> getOntologyList() throws LBInvocationException {
    	Map<String,CodingSchemeSummary> hmap = new HashMap<String,CodingSchemeSummary>();    	
    	CodingSchemeRenderingList csrl = lbSvc.getSupportedCodingSchemes();
        CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
        
        for (int i = 0; i < csrs.length; i++) {
        	CodingSchemeRendering csr = csrs[i];
        	CodingSchemeSummary css = csr.getCodingSchemeSummary();
        	String representsVersion = css.getRepresentsVersion();
        	String name = css.getLocalName();
        	String key = name + " (" + representsVersion + ")";
        	hmap.put(key,css);	
        }
    	return hmap;
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
