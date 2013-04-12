/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-value-set-editor/LICENSE.txt for details.
 */

package gov.nih.nci.evs.valueseteditor.utilities;


import java.io.*;
import java.net.URI;

import java.text.*;
import java.util.*;
import java.sql.*;
import javax.faces.model.*;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.History.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.commonTypes.*;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.*;
import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.DataModel.Core.types.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;

import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Direction;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOptionName;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.QualifierSortOption;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;

//import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Extensions.Generic.SupplementExtension;
import org.LexGrid.relations.AssociationPredicate;


import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;

import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.commonTypes.Source;


import org.apache.log4j.*;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping.SearchContext;



import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSApplicationService;
import org.lexevs.property.PropertyExtension;


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
public class DataUtils {
    private static Logger _logger = Logger.getLogger(DataUtils.class);
    //private static LocalNameList _noopList = Constructors.createLocalNameList("_noop_");
    private static LocalNameList _noopList = new LocalNameList();
    private int _maxReturn = 5000;
    private Connection _con;
    private Statement _stmt;
    private ResultSet _rs;

    private static List _ontologies = null;

    private static org.LexGrid.LexBIG.LexBIGService.LexBIGService _lbSvc = null;
    public org.LexGrid.LexBIG.Utility.ConvenienceMethods _lbConvMethods = null;
    public CodingSchemeRenderingList _csrl = null;

    private static HashSet _codingSchemeHashSet = null;
    private static HashMap _csnv2codingSchemeNameMap = null;
    private static HashMap _csnv2VersionMap = null;

    private static boolean initializeValueSetHierarchy = true;

    // ==================================================================================
    // For customized query use

    public static int ALL = 0;
    public static int PREFERRED_ONLY = 1;
    public static int NON_PREFERRED_ONLY = 2;

    private static int RESOLVE_SOURCE = 1;
    private static int RESOLVE_TARGET = -1;
    private static int RESTRICT_SOURCE = -1;
    private static int RESTRICT_TARGET = 1;

    public static final int SEARCH_NAME_CODE = 1;
    public static final int SEARCH_DEFINITION = 2;

    public static final int SEARCH_PROPERTY_VALUE = 3;
    public static final int SEARCH_ROLE_VALUE = 6;
    public static final int SEARCH_ASSOCIATION_VALUE = 7;

    private static final List<String> STOP_WORDS =
        Arrays.asList(new String[] { "a", "an", "and", "by", "for", "of", "on",
            "in", "nos", "the", "to", "with" });

    public static String TYPE_ROLE = "type_role";
    public static String TYPE_ASSOCIATION = "type_association";
    public static String TYPE_SUPERCONCEPT = "type_superconcept";
    public static String TYPE_SUBCONCEPT = "type_subconcept";
    public static String TYPE_INVERSE_ROLE = "type_inverse_role";
    public static String TYPE_INVERSE_ASSOCIATION = "type_inverse_association";


    public String _ncicbContactURL = null;
    public String _terminologySubsetDownloadURL = null;
    public String _term_suggestion_application_url = null;
    public String _ncitBuildInfo = null;
    public String _ncitAppVersion = null;
    public String _ncitAnthillBuildTagBuilt = null;
    public String _evsServiceURL = null;
    public String _ncimURL = null;

    public static HashMap _namespace2CodingScheme = null;

    public static HashMap _formalName2LocalNameHashMap = null;
    public static HashMap _localName2FormalNameHashMap = null;
    public static HashMap _formalName2MetadataHashMap = null;
    public static HashMap _displayName2FormalNameHashMap = null;

    public static HashMap _formalNameVersion2LocalNameHashMap = null;
    public static HashMap _localNameVersion2FormalNameVersionHashMap = null;
    public static HashMap _formalNameVersion2MetadataHashMap = null;
    public static HashMap _displayNameVersion2FormalNameVersionHashMap = null;
    public static HashMap _uri2CodingSchemeNameHashMap = null;
    public static HashMap _codingSchemeName2URIHashMap = null;

    public static Vector _nonConcept2ConceptAssociations = null;
    public static String _defaultOntologiesToSearchOnStr = null;

    public static HashSet _vocabulariesWithConceptStatusHashSet = null;
    public static HashSet _vocabulariesWithoutTreeAccessHashSet = null;

    public static HashMap _formalName2NCImSABHashMap = null;

    public static HashMap _isMappingHashMap = null;
    public static HashMap _mappingDisplayNameHashMap = null;

    public static HashMap _codingSchemeTagHashMap = null;

    public static HashMap _valueSetDefinitionHierarchyHashMap = null;
    public static Vector  _availableValueSetDefinitionSources = null;
    public static Vector  _valueSetDefinitionHierarchyRoots = null;

    public static HashMap _codingScheme2MappingCodingSchemes = null;

    public static Vector _valueSetDefinitionMetadata = null;

    public static List ontologyList = null;

    public static Vector _valueset_uri_vec = null;

     public static Vector _valueset_item_vec = null;

    public static HashMap formalName2URI = null;

    public static HashMap formalName2CodingSchemeName = null;
    public static HashMap codingSchemeName2formalName = null;

    public static Vector valueSetDefinitionOnServer_uri_vec = null;

    // ==================================================================================
   public DataUtils() {
        _isMappingHashMap = new HashMap();
   }


    public static boolean validateVSDURI(String uri) {
		/*
		if (valueSetDefinitionOnServer_uri_vec == null) {
			Vector v = getValueSetDefinitions();
		}
		*/

		if (valueSetDefinitionOnServer_uri_vec.contains(uri)) {
			return false;
		}
		return true;
	}



    public static boolean isMapping(String scheme, String version) {
		/*
		if (_isMappingHashMap == null) {
			setCodingSchemeMap();
		}
		*/
		if (_isMappingHashMap == null) {
			_isMappingHashMap = new HashMap();
		}


		if (_isMappingHashMap.containsKey(scheme)) {
			Boolean is_mapping = (Boolean) _isMappingHashMap.get(scheme);
			return is_mapping.booleanValue();
		}


        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);

		//List list = new ArrayList();
		try {
			LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
			CodingScheme cs = lbSvc.resolveCodingScheme(scheme, csvt);
			Relations[] relations = cs.getRelations();
			if (relations.length == 0) {
				_isMappingHashMap.put(scheme, Boolean.FALSE);
				return false;
			}
			for (int i = 0; i < relations.length; i++) {
				Relations relation = relations[i];
				Boolean bool_obj = relation.isIsMapping();
				if (bool_obj == null || bool_obj.equals(Boolean.FALSE)) {
					_isMappingHashMap.put(scheme, Boolean.FALSE);
					return false;
				}
			}
		} catch (Exception ex) {
			_isMappingHashMap.put(scheme, Boolean.FALSE);
            return false;
        }
        _isMappingHashMap.put(scheme, Boolean.TRUE);
        return true;
    }


	public static boolean isExtension(String codingScheme, String version) {
		CodingSchemeVersionOrTag tagOrVersion = new CodingSchemeVersionOrTag();
		if (version != null) tagOrVersion.setVersion(version);
		try {
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			SupplementExtension supplementExtension =
				(SupplementExtension) lbSvc.getGenericExtension("SupplementExtension");

			return supplementExtension.isSupplement(codingScheme, tagOrVersion);
			//return ServiceUtility.isSupplement(codingScheme, tagOrVersion);
		} catch (Exception ex) {
			//ex.printStackTrace();
			_logger.warn("WARNING: DataUtils SupplementExtension.isSupplement throws exeption???");
		}
		return false;
	}


    public static Vector getOntologyNames() throws Exception {
        Vector v = new Vector();
        if (ontologyList != null) {
			ontologyList = getOntologyList();
		} else {
			return v;
		}

		for (int k=0; k<ontologyList.size(); k++) {
			SelectItem item = (SelectItem) ontologyList.get(k);
			v.add(item.getValue());
		}

		return v;
    }




    public static List getOntologyList() throws Exception {
        if (formalName2URI == null) formalName2URI = new HashMap();
        if (formalName2CodingSchemeName == null) formalName2CodingSchemeName = new HashMap();
        if (codingSchemeName2formalName == null) codingSchemeName2formalName = new HashMap();

        if (_codingSchemeName2URIHashMap == null) _codingSchemeName2URIHashMap = new HashMap();


		if (ontologyList != null) return ontologyList;

    	//HashMap csnv2codingSchemeNameMap = new HashMap();
    	//HashMap csnv2VersionMap = new HashMap();

		ontologyList = new ArrayList();

    	//Map<String,String> hmap = new HashMap<String,String>();
    	LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();

    	CodingSchemeRenderingList csrl = lbSvc.getSupportedCodingSchemes();
        CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();

        Vector v = new Vector();
        HashSet hset = new HashSet();
        for (int i = 0; i < csrs.length; i++) {
        	CodingSchemeRendering csr = csrs[i];
        	CodingSchemeSummary css = csr.getCodingSchemeSummary();
        	String representsVersion = css.getRepresentsVersion();
       	    String formalname = css.getFormalName();
       	    String uri = css.getCodingSchemeURI();

       	    _codingSchemeName2URIHashMap.put(formalname, uri);

       	    formalName2URI.put(formalname, uri);
       	    formalName2URI.put(uri, uri);

            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            if (representsVersion != null) csvt.setVersion(representsVersion);
       	    CodingScheme cs = null;
       	    try {
				cs = lbSvc.resolveCodingScheme(uri, csvt);
       	        formalName2CodingSchemeName.put(formalname, cs.getCodingSchemeName());
       	        codingSchemeName2formalName.put(cs.getCodingSchemeName(), formalname);
       	        codingSchemeName2formalName.put(formalname, formalname);

       	        formalName2CodingSchemeName.put(uri, cs.getCodingSchemeName());
       	        formalName2CodingSchemeName.put(cs.getCodingSchemeName(), cs.getCodingSchemeName());


       	        formalName2URI.put(cs.getCodingSchemeName(), uri);

			} catch (Exception ex) {
                ex.printStackTrace();
			}


       	    //formalname = css.getCodingSchemeName();

        	if (!DataUtils.isMapping(formalname, representsVersion) &&
        	    !DataUtils.isExtension(formalname, representsVersion)) {

				if (!hset.contains(formalname)) {
					hset.add(formalname);
					v.add(formalname);
				}
			}
		}

		v = SortUtils.quickSort(v);
		for (int i=0; i<v.size(); i++) {
			String formalname = (String) v.elementAt(i);
			ontologyList.add(new SelectItem(formalname));
		}
    	return ontologyList;
    }

    /**
     * Get list all of all concept domains loaded
     * @return
     */
    public static Map<String,String> getConceptDomainNames() {
    	/* TODO: Add lexevs data retrieval code here. */
    	Map<String,String> hmap = new HashMap<String,String>();
    	hmap.put("1","AcknowledgementCondition");
    	hmap.put("2","AcknowledgementDetailCode");
    	hmap.put("3","AcknowledgementDetailType");
    	hmap.put("4","AcknowledgementMessageCode");
    	hmap.put("5","AcknowledgementType");
        return hmap;
    }


    public static NameAndValueList getMappingAssociationNames(String scheme, String version) {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);

		NameAndValueList navList = new NameAndValueList();
		try {
			LexBIGService lbSvc = null;
			lbSvc = new RemoteServerUtil().createLexBIGService();
			CodingScheme cs = lbSvc.resolveCodingScheme(scheme, csvt);
			Relations[] relations = cs.getRelations();
			for (int i = 0; i < relations.length; i++) {
				Relations relation = relations[i];
				System.out.println(relation.getContainerName());
                Boolean isMapping = relation.isIsMapping();
                if (isMapping != null && isMapping.equals(Boolean.TRUE)) {
					AssociationPredicate[] associationPredicates = relation.getAssociationPredicate();
					for (int j=0; j<associationPredicates.length; j++) {
						AssociationPredicate associationPredicate = associationPredicates[j];
						String name = associationPredicate.getAssociationName();
						NameAndValue vNameAndValue = new NameAndValue();
						vNameAndValue.setName(name);
						navList.addNameAndValue(vNameAndValue);


						System.out.println("getMappingAssociationNames " + name);
					}
					return navList;
				} else {
					return null;
				}
			}
		} catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static String getVocabularyVersionByTag(String codingSchemeName,
        String ltag) {

        if (codingSchemeName == null)
            return null;
        String version = null;
        int knt = 0;
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingSchemeRenderingList lcsrl = lbSvc.getSupportedCodingSchemes();
            CodingSchemeRendering[] csra = lcsrl.getCodingSchemeRendering();
            for (int i = 0; i < csra.length; i++) {
                CodingSchemeRendering csr = csra[i];
                CodingSchemeSummary css = csr.getCodingSchemeSummary();
                if (css.getFormalName().compareTo(codingSchemeName) == 0
                    || css.getLocalName().compareTo(codingSchemeName) == 0
                    || css.getCodingSchemeURI().compareTo(codingSchemeName) == 0) {
					version = css.getRepresentsVersion();
                    knt++;

                    if (ltag == null)
                        return version;
                    RenderingDetail rd = csr.getRenderingDetail();
                    CodingSchemeTagList cstl = rd.getVersionTags();
                    java.lang.String[] tags = cstl.getTag();
                    // KLO, 102409
                    if (tags == null)
                        return version;

                    if (tags.length > 0) {
                        for (int j = 0; j < tags.length; j++) {
                            String version_tag = (String) tags[j];

                            if (version_tag != null && version_tag.compareToIgnoreCase(ltag) == 0) {
                                return version;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        _logger.warn("Version corresponding to tag " + ltag + " is not found "
            + " in " + codingSchemeName);
        if (ltag != null && ltag.compareToIgnoreCase("PRODUCTION") == 0
            & knt == 1) {
            _logger.warn("\tUse " + version + " as default.");
            return version;
        }
        return null;
    }


    public static HashMap getPropertyValuesInBatch(List list, String propertyName) {

        if (list == null) return null;
        HashMap hmap = new HashMap();
        if (list.size() == 0) return hmap;
        PropertyExtension extension = null;
        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();

            if (lbSvc == null) {
                _logger.warn("lbSvc = null");
                return null;
            }
            extension = (PropertyExtension) lbSvc.getGenericExtension("property-extension");
            if (extension == null) {
                _logger.error("Error! PropertyExtension is null!");
                return null;
            }
		} catch (Exception ex) {
			return null;
		}


        //Vector cs_name_vec = new Vector();
        //Vector cs_version_vec = new Vector();
        HashSet hset = new HashSet();

        HashMap csnv2codesMap = new HashMap();

		for (int i=0; i<list.size(); i++) {
			ResolvedConceptReference rcr = (ResolvedConceptReference) list.get(i);
			String cs_name = rcr.getCodingSchemeName();
			boolean conceptStatusSupported = false;

			//if (_vocabulariesWithConceptStatusHashSet.contains(cs_name)) {

				String version = rcr.getCodingSchemeVersion();
				String cs_name_and_version = cs_name + "$" + version;
				if (!hset.contains(cs_name_and_version)) {
					hset.add(cs_name_and_version);
					ArrayList alist = new ArrayList();
					alist.add(rcr.getConceptCode());
					csnv2codesMap.put(cs_name_and_version, alist);

				} else {
					ArrayList alist = (ArrayList) csnv2codesMap.get(cs_name_and_version);
					alist.add(rcr.getConceptCode());
					csnv2codesMap.put(cs_name_and_version, alist);
				}

			//}
		}
/*
		Iterator it = csnv2codesMap.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			ArrayList alist = (ArrayList) csnv2codesMap.get(key);
*/

		Iterator it = csnv2codesMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry map_entry = (Entry) it.next();
			String key = (String) map_entry.getKey();
			ArrayList alist = (ArrayList) map_entry.getValue();

			Vector u = parseData(key, "$");
			String scheme = (String) u.elementAt(0);
			String version = (String) u.elementAt(1);
			CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
			if (version != null) csvt.setVersion(version);
			String[] a = new String[alist.size()];
			for (int i=0; i<alist.size(); i++) {
				a[i] = (String) alist.get(i);
			}

            try {
				Map<String,String> propertyMap =
					extension.getProperty(scheme, csvt, propertyName, Arrays.asList(a));

				for (Entry<String, String> entry : propertyMap.entrySet()) {
					//System.out.println("Code: " + entry.getKey());
					//System.out.println(" - Property: " + entry.getValue());
					hmap.put(key + "$" + entry.getKey(), entry.getValue());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

        return hmap;
	}

    public static Vector<String> parseData(String line) {
		if (line == null) return null;
        String tab = "|";
        return parseData(line, tab);
    }

    public static Vector<String> parseData(String line, String tab) {
		if (line == null) return null;
        Vector data_vec = new Vector();
        StringTokenizer st = new StringTokenizer(line, tab);
        while (st.hasMoreTokens()) {
            String value = st.nextToken();
            if (value.compareTo("null") == 0)
                value = " ";
            data_vec.add(value);
        }
        return data_vec;
    }



    public static Vector getConceptEntityNamesInCodingScheme(String codingSchemeName, String version) {
		ResolvedConceptReferencesIterator rcri = null;

		//String scheme = "Terminology_Value_Set";
		//

        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;
		//LocalNameList restrictToProperties = new LocalNameList();
		SortOptionList sortCriteria = null;

		Vector v = new Vector();

        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            if (version != null)
                versionOrTag.setVersion(version);

            if (lbSvc == null) {
                _logger.warn("lbSvc = null");
                return null;
            }

            cns = lbSvc.getCodingSchemeConcepts(codingSchemeName, versionOrTag);
            if (cns == null) {
                _logger.debug("cns = null");
                return null;
            }
			boolean resolveConcepts = false;
			try {

				iterator =
					cns.resolve(sortCriteria, null, null, null, resolveConcepts);
                if (iterator == null) return null;

                while (iterator.hasNext()) {
					ResolvedConceptReference rcr = (ResolvedConceptReference) iterator.next();
					v.add(rcr.getEntityDescription().getContent());
				}
				v = SortUtils.quickSort(v);
				return v;

			} catch (Exception e) {
				_logger
					.error("Method: SearchUtil.restrictToMatchingProperty");
				_logger.error("* ERROR: cns.resolve throws exceptions.");
				_logger.error("* " + e.getClass().getSimpleName() + ": "
					+ e.getMessage());
			}

        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Unable to resolve " + codingSchemeName);
            return null;
        }
        return v;
	}


    public static Vector getConceptEntityCodesInCodingScheme(String codingSchemeName, String version) {
		ResolvedConceptReferencesIterator rcri = null;

		//String scheme = "Terminology_Value_Set";
		//

        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;
		//LocalNameList restrictToProperties = new LocalNameList();
		SortOptionList sortCriteria = null;

		Vector v = new Vector();

        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            if (version != null)
                versionOrTag.setVersion(version);

            if (lbSvc == null) {
                _logger.warn("lbSvc = null");
                return null;
            }

            cns = lbSvc.getCodingSchemeConcepts(codingSchemeName, versionOrTag);
            if (cns == null) {
                _logger.debug("cns = null");
                return null;
            }
			boolean resolveConcepts = false;
			try {

				iterator =
					cns.resolve(sortCriteria, null, null, null, resolveConcepts);
                if (iterator == null) return null;

                while (iterator.hasNext()) {
					ResolvedConceptReference rcr = (ResolvedConceptReference) iterator.next();
					v.add(rcr.getCode());
				}
				v = SortUtils.quickSort(v);
				return v;

			} catch (Exception e) {
				_logger
					.error("Method: SearchUtil.restrictToMatchingProperty");
				_logger.error("* ERROR: cns.resolve throws exceptions.");
				_logger.error("* " + e.getClass().getSimpleName() + ": "
					+ e.getMessage());
			}

        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Unable to resolve " + codingSchemeName);
            return null;
        }
        return v;
	}


    public static CodingScheme getCodingScheme(String codingScheme,
        CodingSchemeVersionOrTag versionOrTag) throws LBException {

        CodingScheme cs = null;
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            cs = lbSvc.resolveCodingScheme(codingScheme, versionOrTag);
        } catch (Exception ex) {
            //ex.printStackTrace();
            if (versionOrTag.getVersion() != null) {
            	System.out.println("Unable to resolve coding scheme: " + codingScheme + " version: " + versionOrTag.getVersion());
			} else {
            	System.out.println("Unable to resolve coding scheme: " + codingScheme);
			}
        }
        return cs;
    }

    public static Mappings getCodingSchemeMappings(String codingScheme,
        CodingSchemeVersionOrTag versionOrTag) throws LBException {

		CodingScheme cs = getCodingScheme(codingScheme, versionOrTag);
		if (cs == null) {
			return null;
		}
		return cs.getMappings();
	}



	public static Vector getValueSetURIs() {
		if (_valueset_uri_vec != null) return _valueset_uri_vec;
		_valueset_uri_vec = new Vector();
		LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
        List list = vsd_service.listValueSetDefinitionURIs();
        for (int i=0; i<list.size(); i++) {
			String t = (String) list.get(i);
			_valueset_uri_vec.add(t);
		}
		_valueset_uri_vec = SortUtils.quickSort(_valueset_uri_vec);
		return _valueset_uri_vec;
	}


    public static ValueSetDefinition findValueSetDefinitionByURI(String uri) {
		if (uri == null) return null;
	    if (uri.indexOf("|") != -1) {
			Vector u = parseData(uri);
			uri = (String) u.elementAt(1);
		}

		String valueSetDefinitionRevisionId = null;
		try {
			LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
			ValueSetDefinition vsd = vsd_service.getValueSetDefinition(new URI(uri), valueSetDefinitionRevisionId);
			return vsd;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


	public static Vector getValueSetNamesAndURIs() {
		Vector v = new Vector();
		LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
        List list = vsd_service.listValueSetDefinitionURIs();
        for (int i=0; i<list.size(); i++) {
			String t = (String) list.get(i);
			ValueSetDefinition vsd = findValueSetDefinitionByURI(t);
			String name = vsd.getValueSetDefinitionName();
			if (name == null) {
				name = "<NOT ASSIGNED>";
			}

			v.add(name + "|" + t);
		}
		return SortUtils.quickSort(v);
	}

	public static Vector getValueSetDefinitions() {
		if (_valueset_item_vec != null) return _valueset_item_vec;

		_valueset_item_vec = new Vector();
		valueSetDefinitionOnServer_uri_vec = new Vector();
		Vector key_vec = new Vector();

		HashMap hmap = new HashMap();
		LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
        List list = vsd_service.listValueSetDefinitionURIs();
        for (int i=0; i<list.size(); i++) {
			String t = (String) list.get(i);
			ValueSetDefinition vsd = findValueSetDefinitionByURI(t);
			if (vsd != null) {
				String name = vsd.getValueSetDefinitionName();
				String uri = vsd.getValueSetDefinitionURI();
				if (name == null) {
					name = "<NOT ASSIGNED>";
				}
				hmap.put(name, vsd);
			    valueSetDefinitionOnServer_uri_vec.add(uri);
			    key_vec.add(name);
		    }
		}

		key_vec = SortUtils.quickSort(key_vec);
		for (int i=0; i<key_vec.size(); i++) {
			String key = (String) key_vec.elementAt(i);
			ValueSetDefinition vsd = (ValueSetDefinition) hmap.get(key);
			_valueset_item_vec.add(new SelectItem(vsd.getValueSetDefinitionURI(), key)); // value, label
		}
		return _valueset_item_vec;
	}

    public static String getCodingSchemeURI(String formalname, String version) {
        try {
			if (formalName2URI == null) {
				ontologyList = getOntologyList();
			}
			if (!formalName2URI.containsKey(formalname)) {

System.out.println(	"WARNING: DataUtils.getCodingSchemeURI cannot find coding scheme formalname " + formalname);

				return null;
			}
			return (String) formalName2URI.get(formalname);
		} catch (Exception ex) {
			return null;
		}
	}


    public static String getCodingSchemeName(String formalname, String version) {
        try {
			if (formalName2CodingSchemeName == null) {
				ontologyList = getOntologyList();
			}
			if (!formalName2CodingSchemeName.containsKey(formalname)) return null;
			return (String) formalName2CodingSchemeName.get(formalname);
		} catch (Exception ex) {
			return null;
		}
	}

    public static String getFormalName(String cs_name, String version) {
        try {
			if (codingSchemeName2formalName == null) {
				ontologyList = getOntologyList();
			}
			if (!codingSchemeName2formalName.containsKey(cs_name)) return null;
			return (String) codingSchemeName2formalName.get(cs_name);
		} catch (Exception ex) {
			return null;
		}
	}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Coding scheme version references
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//constructVSDMappings
//Vector of coding scheme names


/*
    public static Vector getCodingSchemesInValueSetDefinition(String uri) {
		HashSet hset = new HashSet();
		try {
			java.net.URI valueSetDefinitionURI = new URI(uri);
			Vector v = new Vector();
			try {
				LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
				AbsoluteCodingSchemeVersionReferenceList codingSchemes =
					vsd_service.getCodingSchemesInValueSetDefinition(valueSetDefinitionURI);

				//output is all of the mapping ontologies that this code participates in.
				for(AbsoluteCodingSchemeVersionReference ref : codingSchemes.getAbsoluteCodingSchemeVersionReference()){
					String urn = ref.getCodingSchemeURN();
					System.out.println("URI: " + ref.getCodingSchemeURN());
					if (!hset.contains(urn)) {
					System.out.println("Version: " + ref.getCodingSchemeVersion());
					    v.add(ref.getCodingSchemeURN());
					    hset.add(urn);
				    }
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return v;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}




*/

    public static AbsoluteCodingSchemeVersionReferenceList
                  getAbsoluteCodingSchemeVersionReferenceListForValueSetDefinition(Vector codingSchemeName_vec) {
		Vector v = getCodingSchemeReferencesInValueSetDefinition(codingSchemeName_vec);
		if (v == null) return null;
		return vector2CodingSchemeVersionReferenceList(v);
	}


    public static Vector getCodingSchemeVersionsByURN(String urn) {
		if (urn == null) return null;

        try {
			Vector v = new Vector();
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            if (lbSvc == null) {
                _logger
                    .warn("WARNING: Unable to connect to instantiate LexBIGService ???");
                return null;
            }
            CodingSchemeRenderingList csrl = null;
            try {
                csrl = lbSvc.getSupportedCodingSchemes();
            } catch (LBInvocationException ex) {
                ex.printStackTrace();
                _logger.error("lbSvc.getSupportedCodingSchemes() FAILED..."
                    + ex.getCause());
                return null;
            }
            CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
            for (int i = 0; i < csrs.length; i++) {
                int j = i + 1;
                CodingSchemeRendering csr = csrs[i];
                CodingSchemeSummary css = csr.getCodingSchemeSummary();
                Boolean isActive =
                        csr.getRenderingDetail().getVersionStatus().equals(
                            CodingSchemeVersionStatus.ACTIVE);

                if (isActive != null && isActive.equals(Boolean.TRUE)) {
                	String uri = css.getCodingSchemeURI();
                	if (uri != null && uri.compareTo(urn) == 0) {
						String representsVersion = css.getRepresentsVersion();
						v.add(representsVersion);
					}
				}
			}
			return v;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}



    public static String getCodingSchemeURN(String cs) {

		if (formalName2URI == null) {
			try {
				ontologyList = getOntologyList();
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
		return (String) formalName2URI.get(cs);

	}

    public static Vector getCodingSchemeURNs(Vector codingSchemeName_vec) {
		if (codingSchemeName_vec == null) return null;

		Vector v = new Vector();
		for (int i=0; i<codingSchemeName_vec.size(); i++) {
            String cs = (String) codingSchemeName_vec.elementAt(i);
            String urn = getCodingSchemeURN(cs);
            v.add(urn);
		}
		return v;
	}




    public static Vector getCodingSchemeReferencesInValueSetDefinition(Vector codingSchemeName_vec) {
		HashSet hset = new HashSet();
		try {
			Vector w = new Vector();
			Vector urn_vec = getCodingSchemeURNs(codingSchemeName_vec);

			if (urn_vec != null) {
				for (int i=0; i<urn_vec.size(); i++) {
					String urn = (String) urn_vec.elementAt(i);
					Vector v = getCodingSchemeVersionsByURN(urn);

					if (v != null) {
						for (int j=0; j<v.size(); j++) {
							String version = (String) v.elementAt(j);
							String urn_version = urn + "|" + version;
							//System.out.println(urn_version);

							if (!hset.contains(urn_version)) {
								hset.add(urn_version);
							    w.add(urn_version);
						    }
						}
					}
				}
				w = SortUtils.quickSort(w);
				hset.clear();
				return w;
		    }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


    public static AbsoluteCodingSchemeVersionReferenceList vector2CodingSchemeVersionReferenceList(Vector v) {
		if (v == null) return null;
		AbsoluteCodingSchemeVersionReferenceList list = new AbsoluteCodingSchemeVersionReferenceList();
		for (int i=0; i<v.size(); i++) {
			String s = (String) v.elementAt(i);
			Vector u = parseData(s);
			String uri = (String) u.elementAt(0);
			String version = (String) u.elementAt(1);

			AbsoluteCodingSchemeVersionReference vAbsoluteCodingSchemeVersionReference
			    = new AbsoluteCodingSchemeVersionReference();
			vAbsoluteCodingSchemeVersionReference.setCodingSchemeURN(uri);
			vAbsoluteCodingSchemeVersionReference.setCodingSchemeVersion(version);
			list.addAbsoluteCodingSchemeVersionReference(vAbsoluteCodingSchemeVersionReference);
		}
		return list;
	}

    public static String getVocabularyVersionTag(String codingSchemeName,
        String version) {

        if (codingSchemeName == null)
            return null;


        if (_codingSchemeTagHashMap != null) {
			String key = null;
			if (version == null) {
				key = codingSchemeName + "$null";
			} else {
				key = codingSchemeName + "$" + version;
			}
			if (_codingSchemeTagHashMap.containsKey(key)) {
				String tag = (String) _codingSchemeTagHashMap.get(key);
				return tag;
			}
		}

        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingSchemeRenderingList lcsrl = lbSvc.getSupportedCodingSchemes();
            CodingSchemeRendering[] csra = lcsrl.getCodingSchemeRendering();
            for (int i = 0; i < csra.length; i++) {
                CodingSchemeRendering csr = csra[i];
                CodingSchemeSummary css = csr.getCodingSchemeSummary();
                if (css.getFormalName().compareTo(codingSchemeName) == 0
                    || css.getLocalName().compareTo(codingSchemeName) == 0
                    || css.getCodingSchemeURI().compareTo(codingSchemeName) == 0) {

					if (version == null) return "PRODUCTION";

					String representsVersion = css.getRepresentsVersion();

                    if (representsVersion.compareTo(version) == 0) {
						RenderingDetail rd = csr.getRenderingDetail();
						CodingSchemeTagList cstl = rd.getVersionTags();
						String tag_str = "";
						java.lang.String[] tags = cstl.getTag();
						if (tags == null)
							return "NOT ASSIGNED";

						if (tags.length > 0) {
							tag_str = "";
							for (int j = 0; j < tags.length; j++) {
								String version_tag = (String) tags[j];
								if (j == 0) {
									tag_str = version_tag;
								} else if (j == tags.length-1) {
									tag_str = tag_str + version_tag;
								} else {
									tag_str = tag_str + version_tag + "|";
								}
							}
						} else {
							return "<NOT ASSIGNED>";
						}

                        if (_codingSchemeTagHashMap == null) {
							_codingSchemeTagHashMap = new HashMap();
						}
						String key = null;
						/*
						if (version == null) {
							key = codingSchemeName + "$null";
						} else
						*/
						{
							key = codingSchemeName + "$" + version;
						}
						_codingSchemeTagHashMap.put(key, tag_str);
						return tag_str;
					}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "<NOT AVAILABLE>";
    }

/*
    public static Vector getCodingSchemeReferencesInValueSetDefinition(String uri, String tag) {
		try {
			Vector w = new Vector();
			Vector urn_vec = getCodingSchemeURNsInValueSetDefinition(uri);
			if (urn_vec != null) {
				for (int i=0; i<urn_vec.size(); i++) {
					String urn = (String) urn_vec.elementAt(i);
					Vector v = getCodingSchemeVersionsByURN(urn, tag);
					if (v != null) {
						for (int j=0; j<v.size(); j++) {
							String version = (String) v.elementAt(j);
							w.add(urn + "|" + version);
						}
					}
				}
				w = SortUtils.quickSort(w);
				return w;
		    }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}



    public static Vector getCodingSchemeVersionsByURN(String urn, String tag) {
        try {
			Vector v = new Vector();
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            if (lbSvc == null) {
                _logger
                    .warn("WARNING: Unable to connect to instantiate LexBIGService ???");
            }
            CodingSchemeRenderingList csrl = null;
            try {
                csrl = lbSvc.getSupportedCodingSchemes();
            } catch (LBInvocationException ex) {
                ex.printStackTrace();
                _logger.error("lbSvc.getSupportedCodingSchemes() FAILED..."
                    + ex.getCause());
                return null;
            }
            CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
            for (int i = 0; i < csrs.length; i++) {
                int j = i + 1;
                CodingSchemeRendering csr = csrs[i];
                CodingSchemeSummary css = csr.getCodingSchemeSummary();
                Boolean isActive =
                        csr.getRenderingDetail().getVersionStatus().equals(
                            CodingSchemeVersionStatus.ACTIVE);

                if (isActive != null && isActive.equals(Boolean.TRUE)) {
                	String uri = css.getCodingSchemeURI();
                	if (uri.compareTo(urn) == 0) {
						String representsVersion = css.getRepresentsVersion();
						if (tag != null) {
							String cs_tag = getVocabularyVersionTag(uri, representsVersion);
							if (cs_tag.compareToIgnoreCase(tag) == 0) {
								v.add(representsVersion);
							}
						} else {
							v.add(representsVersion);
						}
					}
				}
			}
			return v;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

*/
    public static ConceptReferenceList createConceptReferenceList(
        String[] codes, String codingSchemeName) {
        if (codes == null) {
            return null;
        }
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.length; i++) {
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codingSchemeName);
            cr.setConceptCode(codes[i]);
            list.addConceptReference(cr);
        }
        return list;
    }


    public static Entity getConceptByCode(String codingSchemeName, String vers, String ltag, String code) {
        try {
			if (code == null) {
				System.out.println("Input error in DataUtils.getConceptByCode -- code is null.");
				return null;
			}
			if (code.indexOf("@") != -1) return null; // anonymous class

            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
            if (lbSvc == null) {
                System.out.println("lbSvc == null???");
                return null;
            }
            CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
            if (vers != null) versionOrTag.setVersion(vers);

            ConceptReferenceList crefs = createConceptReferenceList(
                    new String[] { code }, codingSchemeName);

            CodedNodeSet cns = null;
            try {
				try {
					cns = getNodeSet(lbSvc, codingSchemeName, versionOrTag);

				} catch (Exception e) {
					e.printStackTrace();
				}

                if (cns == null) {
					System.out.println("getConceptByCode getCodingSchemeConcepts returns null??? " + codingSchemeName);
					return null;
				}

                cns = cns.restrictToCodes(crefs);
 				ResolvedConceptReferenceList matches = null;
				try {
					matches = cns.resolveToList(null, null, null, 1);
				} catch (Exception e) {
					System.out.println("cns.resolveToList failed???");
				}

                if (matches == null) {
                    System.out.println("Concept not found.");
                    return null;
                }
                int count = matches.getResolvedConceptReferenceCount();
                // Analyze the result ...
                if (count == 0)
                    return null;
                if (count > 0) {
                    try {
                        ResolvedConceptReference ref = (ResolvedConceptReference) matches
                                .enumerateResolvedConceptReference()
                                .nextElement();
                        Entity entry = ref.getReferencedEntry();
                        return entry;
                    } catch (Exception ex1) {
                        System.out.println("Exception entry == null");
                        return null;
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


/*
    public static Entity getConceptByCode(String scheme, String version, String code) {

        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            if (lbSvc == null) {
                _logger
                    .warn("WARNING: Unable to connect to instantiate LexBIGService ???");
                return null;
            }
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
			if (version != null)
				versionOrTag.setVersion(version);
			CodedNodeSet cns = getNodeSet(lbSvc, scheme, versionOrTag);
			CodedNodeSet.PropertyType[] propertyTypes = null;
			ConceptReferenceList crefs =
				createConceptReferenceList(new String[] { code }, scheme);
			cns = lbSvc.getCodingSchemeConcepts(scheme, versionOrTag);
			cns = cns.restrictToCodes(crefs);
			boolean resolveConcepts = false;
			ResolvedConceptReferencesIterator iterator = null;
			SortOptionList sortCriteria = null;
			iterator = cns.resolve(sortCriteria, null, null, null, resolveConcepts);
			if (iterator == null) return null;
			while (iterator.hasNext()) {
				ResolvedConceptReference[] refs =
					iterator.next(100).getResolvedConceptReference();
				for (ResolvedConceptReference ref : refs) {
					if (ref.getReferencedEntry().getEntityCode().equals(
						code)) {
						return ref.getReferencedEntry();
					}
				}
		    }
        } catch (Exception e) {
            _logger.error("Method: SearchUtil.matchConceptByCode");
            _logger.error("* ERROR: cns.resolve throws exceptions.");
            _logger.error("* " + e.getClass().getSimpleName() + ": "
                + e.getMessage());
        }
        return null;
    }
*/
     public static CodedNodeSet getNodeSet(LexBIGService lbSvc, String scheme, CodingSchemeVersionOrTag versionOrTag)
        throws Exception {
		CodedNodeSet cns = null;
		try {
			cns = lbSvc.getCodingSchemeConcepts(scheme, versionOrTag);
			CodedNodeSet.AnonymousOption restrictToAnonymous = CodedNodeSet.AnonymousOption.NON_ANONYMOUS_ONLY;
			cns = cns.restrictToAnonymous(restrictToAnonymous);
	    } catch (Exception ex) {
			ex.printStackTrace();
		}

 	return cns;
    }

    public static boolean isNull(String value) {
		if (value == null || value.compareTo("null") == 0 || value.compareTo("undefined") == 0) return true;
		return false;
	}
}
