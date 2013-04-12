/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-value-set-editor/LICENSE.txt for details.
 */

package gov.nih.nci.evs.valueseteditor.utilities;

import java.io.*;
import java.util.*;

public class Constant
{
    static int SET = 1;
    static int GRAPH = 2;

    static int OPERAND = 1;
    static int OPERATOR = 2;

    static int EXACT_MATCH = 0;
    static int START_WITH = 1;
    static int CONTAINS = 2;

    static int NONE = 0;

    static String EXACT_MATCH_ALGORITHM = "exactMatch";
    static String START_WITH_ALGORITHM = "startsWith";
    static String CONTAINS_ALGORITHM = "contains";

    static String OR = "OR"; // set union
    static String AND = "AND"; // set intersection
    static String NOT = "NOT";  // set difference

    static String NULL = "null";  // null-valued


    public static final int ENTRY_TYPE_REFERENCE = 0;
	public static final int ENTRY_TYPE_VOCABULARY = 1;
	public static final int ENTRY_TYPE_CODE = 2;
	public static final int ENTRY_TYPE_NAME = 3;
	public static final int ENTRY_TYPE_PROPERTY = 4;
	public static final int ENTRY_TYPE_ASSOCIATION = 5;
	public static final int ENTRY_TYPE_ENUMERATION = 6;
	public static final int ENTRY_TYPE_UNKNOWN = 7;


    static String[] operandTypes = {"REFERENCE", "VOCABULARY", "CODE", "NAME", "PROPERTY", "ASSOCIATION", "ENUMERATION", "UNKNOWN"};
    static String[] operatorTypes = {"OR", "AND", "NOT"};
    static String[] setOperatorTypes = {"UNION", "INTERSECTION", "DIFFERENCE"};

    static String[] matchingAlgorithms = {"exactMatch", "startsWith", "contains"};

    public static final int MAJOR_VER = 1;
    public static final int MINOR_VER = 0;
    public static final String CONFIG_FILE = "NCItBrowserProperties.xml";
    public static final String CODING_SCHEME_NAME = "NCI Thesaurus";
    public static final String NCI_THESAURUS = "NCI Thesaurus";
    public static final String NCI_METATHESAURUS = "NCI Metathesaurus";

    // Application constants
    public static final String NA = "N/A";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String EMPTY = "";

    public static final String ALL = "ALL";

    // Application error constants
    public static final String INIT_PARAM_ERROR_PAGE = "errorPage";
    public static final String ERROR_MESSAGE = "systemMessage";
    public static final String ERROR_UNEXPECTED =
        "Warning: An unexpected processing error has occurred.";

    public static final int DEFAULT_PAGE_SIZE = 50;

    public static final String ERROR_NO_VOCABULARY_SELECTED =
        "Please select at least one terminology.";
    public static final String ERROR_NO_SEARCH_STRING_ENTERED =
        "Please enter a search string.";
    public static final String ERROR_NO_MATCH_FOUND = "No match found.";
    public static final String ERROR_NO_MATCH_FOUND_TRY_OTHER_ALGORITHMS =
        "No match found. Please try 'Begins With' or 'Contains' search instead.";

    public static final String ERROR_ENCOUNTERED_TRY_NARROW_QUERY =
        "Unable to perform search successfully. Please narrow your query.";
    public static final String ERROR_REQUIRE_MORE_SPECIFIC_QUERY_STRING =
        "Please provide a more specific search criteria.";

    public static final String EXACT_SEARCH_ALGORITHM = "exactMatch";// "literalSubString";//"subString";
    public static final String STARTWITH_SEARCH_ALGORITHM = "startsWith";// "literalSubString";//"subString";
    public static final String CONTAIN_SEARCH_ALGORITHM =
        "nonLeadingWildcardLiteralSubString";// "literalSubString";//"subString";
    public static final String LICENSE_STATEMENT = "license_statement";// "literalSubString";//"subString";

    public static final int SEARCH_BOTH_DIRECTION = 0;
    public static final int SEARCH_SOURCE = 1;
    public static final int SEARCH_TARGET = 2;

    public static final String TREE_ACCESS_ALLOWED = "tree_access_allowed";

    public static String TYPE_ROLE = "type_role";
    public static String TYPE_ASSOCIATION = "type_association";
    public static String TYPE_SUPERCONCEPT = "type_superconcept";
    public static String TYPE_SUBCONCEPT = "type_subconcept";
    public static String TYPE_INVERSE_ROLE = "type_inverse_role";
    public static String TYPE_INVERSE_ASSOCIATION = "type_inverse_association";


    public static int MINIMUM_SEARCH_STRING_LENGTH = 3;


}