/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-value-set-editor/LICENSE.txt for details.
 */

package gov.nih.nci.evs.valueseteditor.utilities;

import gov.nih.nci.system.client.ApplicationServiceProvider;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.util.PrintUtility;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;

import java.io.*;
import java.util.*;


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


public class ValueSetOperand
{
// Variable declaration
	private int type;

	private String valueSetDefinitionURI;
	private String referencedValueSetDefinitionURI;

	private String label;
	private String codingScheme;
	private String entityCode;
	private String entityCodeNamespace;
	private String propertyName;
	private String matchValue;
	private String matchAlgorithm;
	private String referenceAssociation;
	private boolean targetToSource;
	private boolean leafOnly;
	private boolean transitiveClosure;
	private boolean applyRecursion;

	private Vector codes;

// Default constructor
	public ValueSetOperand() {
	}

// Constructors

	public ValueSetOperand(
		int type,
		String label,
		String codingScheme) {

		this.type = type;
		this.label = label;
		this.codingScheme = codingScheme;
		this.entityCode = null;
		this.entityCodeNamespace = null;
		this.propertyName = null;
		this.matchValue = null;
		this.matchAlgorithm = null;
		this.referenceAssociation = null;
		this.targetToSource = false;
		this.leafOnly = false;
		this.transitiveClosure = false;
		this.applyRecursion = false;

		this.valueSetDefinitionURI = null;
	    this.referencedValueSetDefinitionURI = null;
	    this.codes = null;
	}


	public ValueSetOperand(
		int type,
		String label,
		String codingScheme,
		String entityCode,
		String entityCodeNamespace) {

		this.type = type;
		this.label = label;
		this.codingScheme = codingScheme;
		this.entityCode = entityCode;
		this.entityCodeNamespace = entityCodeNamespace;
		this.propertyName = null;
		this.matchValue = null;
		this.matchAlgorithm = null;
		this.referenceAssociation = null;
		this.targetToSource = false;
		this.leafOnly = false;
		this.transitiveClosure = false;
		this.applyRecursion = false;

		this.valueSetDefinitionURI = null;
	    this.referencedValueSetDefinitionURI = null;
	    this.codes = null;

	}

	public ValueSetOperand(
		int type,
		String label,
		String codingScheme,
		String entityCode,
		String entityCodeNamespace,
		String propertyName,
		String matchValue,
		String matchAlgorithm,
		String referenceAssociation,
		boolean targetToSource,
		boolean leafOnly,
		boolean transitiveClosure,
		boolean applyRecursion) {

		this.type = type;
		this.label = label;
		this.codingScheme = codingScheme;
		this.entityCode = entityCode;
		this.entityCodeNamespace = entityCodeNamespace;
		this.propertyName = propertyName;
		this.matchValue = matchValue;
		this.matchAlgorithm = matchAlgorithm;
		this.referenceAssociation = referenceAssociation;
		this.targetToSource = targetToSource;
		this.leafOnly = leafOnly;
		this.transitiveClosure = transitiveClosure;
		this.applyRecursion = applyRecursion;

		this.valueSetDefinitionURI = null;
	    this.referencedValueSetDefinitionURI = null;
	    this.codes = null;

	}

// Set methods
	public void setType(int type) {
		this.type = type;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setCodingScheme(String codingScheme) {
		this.codingScheme = codingScheme;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public void setEntityCodeNamespace(String entityCodeNamespace) {
		this.entityCodeNamespace = entityCodeNamespace;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public void setMatchValue(String matchValue) {
		this.matchValue = matchValue;
	}

	public void setMatchAlgorithm(String matchAlgorithm) {
		this.matchAlgorithm = matchAlgorithm;
	}

	public void setReferenceAssociation(String referenceAssociation) {
		this.referenceAssociation = referenceAssociation;
	}

	public void setTargetToSource(boolean targetToSource) {
		this.targetToSource = targetToSource;
	}

	public void setLeafOnly(boolean leafOnly) {
		this.leafOnly = leafOnly;
	}

	public void setTransitiveClosure(boolean transitiveClosure) {
		this.transitiveClosure = transitiveClosure;
	}

	public void setApplyRecursion(boolean applyRecursion) {
		this.applyRecursion = applyRecursion;
	}


	public void setValueSetDefinitionURI(String valueSetDefinitionURI) {
		this.valueSetDefinitionURI = valueSetDefinitionURI;
	}

	public void setReferencedValueSetDefinitionURI(String referencedValueSetDefinitionURI) {
		this.referencedValueSetDefinitionURI = referencedValueSetDefinitionURI;
	}


	public void setCodes(Vector v) {
		this.codes = new Vector();
		for (int i=0; i<v.size(); i++) {
			String s = (String) v.elementAt(i);
			this.codes.add(s);
		}
	}




// Get methods
	public int getType() {
		return this.type;
	}

	public String getLabel() {
		return this.label;
	}

	public String getCodingScheme() {
		return this.codingScheme;
	}

	public String getEntityCode() {
		return this.entityCode;
	}

	public String getEntityCodeNamespace() {
		return this.entityCodeNamespace;
	}

	public String getPropertyName() {
		return this.propertyName;
	}

	public String getMatchValue() {
		return this.matchValue;
	}

	public String getMatchAlgorithm() {
		return this.matchAlgorithm;
	}

	public String getReferenceAssociation() {
		return this.referenceAssociation;
	}

	public boolean getTargetToSource() {
		return this.targetToSource;
	}

	public boolean getLeafOnly() {
		return this.leafOnly;
	}

	public boolean getTransitiveClosure() {
		return this.transitiveClosure;
	}

	public boolean getApplyRecursion() {
		return this.applyRecursion;
	}


	public String getValueSetDefinitionURI() {
		return this.valueSetDefinitionURI;
	}

	public String getReferencedValueSetDefinitionURI() {
		return this.referencedValueSetDefinitionURI;
	}

	public Vector getCodes() {
		return this.codes;
	}

}

