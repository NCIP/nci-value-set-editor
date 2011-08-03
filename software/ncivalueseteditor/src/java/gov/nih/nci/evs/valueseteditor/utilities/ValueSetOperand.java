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

