package gov.nih.nci.evs.valueseteditor.utilities;

import java.util.*;
import java.io.*;
import java.net.*;
import org.LexGrid.LexBIG.caCore.interfaces.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Impl.*;

import gov.nih.nci.evs.valueseteditor.beans.*;

//import gov.nih.nci.evs.browser.properties.*;
import gov.nih.nci.system.client.*;
import gov.nih.nci.evs.security.*;


import org.apache.log4j.*;

import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;


import gov.nih.nci.system.client.ApplicationServiceProvider;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.util.PrintUtility;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.PropertyMatchValue;
import org.LexGrid.valueSets.PropertyReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitionReference;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.valueSets.CodingSchemeReference;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;

import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.Exceptions.LBException;

import org.lexgrid.valuesets.dto.ResolvedValueSetCodedNodeSet;


import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;


import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;


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



public class ValueSetUtils //extends Stack
{
	  public static AbsoluteCodingSchemeVersionReferenceList defaultAbsoluteCodingSchemeVersionReferenceList = null;
	  public static HashMap _valueSetDefinitionURI2VSD_map = null;
	  private static Logger _logger = Logger.getLogger(ValueSetUtils.class);

	  private static Vector value_set_uri_vec = null;

	  public static String translateSetOperator(String op) {
		  if (op.compareTo("AND") == 0) return "INTERSECTION";
		  else if (op.compareTo("OR") == 0) return "UNION";
		  else if (op.compareTo("NOT") == 0) return "DIFFERENCE";
		  else return op;
	  }

	  public static boolean evaluate(List list) {
		  Stack stack = new Stack();
		  for (int i=0; i<list.size(); i++) {
			  Object obj = list.get(i);
			  if (obj instanceof String) {
				  // Operator
			      System.out.println("Set operator: " + obj.toString());
			      String operand = obj.toString();

			      //operand = Constant.setOperatorTypes[Integer.parseInt(operand)-1];
			      //stack.size()
			      System.out.println(stack.size());
			      if (stack.size() < 2) {
					  System.out.println("Invalid postfix expression -- operation aborts.");
					  return false;
				  } else {
					  ValueSetOperand operand_2 = (ValueSetOperand) stack.pop();
					  ValueSetOperand operand_1 = (ValueSetOperand) stack.pop();
					  // find operand_1 operator operand_2
					  String new_label = "(" + operand_1.getLabel() + " " + translateSetOperator(operand) + " " + operand_2.getLabel() + ")";
					  System.out.println("Calculating " + new_label);
					  operand_1.setLabel(new_label);
					  stack.push(operand_1);
				  }

			  } else if (obj instanceof ValueSetOperand) {
				  // Operand
			      //System.out.println(obj.toString());
                  ValueSetOperand valueSetOperand = (ValueSetOperand) obj;
                  System.out.println("\tOperand Type: " + Constant.operandTypes[valueSetOperand.getType()]);
                  stack.push(valueSetOperand);
                  System.out.println(stack.size());
			  }
          }
		  if (stack.size() > 0) {
			  ValueSetOperand final_operand = (ValueSetOperand) stack.pop();
			  System.out.println("Final expression: " + final_operand.getLabel());
		  }

          return true;
	  }


      public static void dumpEntityReference(EntityReference entityReference) {
		  if (entityReference == null) return;
		  String entityCode = entityReference.getEntityCode();
		  String entityCodeNamespace = entityReference.getEntityCodeNamespace();
		  Boolean leafOnly = entityReference.getLeafOnly();
		  String referenceAssociation = entityReference.getReferenceAssociation();
		  Boolean targetToSource = entityReference.getTargetToSource();
		  Boolean transitiveClosure = entityReference.getTransitiveClosure();

      	  System.out.println("\tentityCodeNamespace: " + entityCodeNamespace);
      	  System.out.println("\tentityCode: " + entityCode);

      	  System.out.println("\tleafOnly: " + leafOnly);
      	  System.out.println("\treferenceAssociation: " + referenceAssociation);
      	  System.out.println("\ttargetToSource: " + targetToSource);
      	  System.out.println("\ttransitiveClosure: " + transitiveClosure);
	  }


      public static void dumpCodinSchemeReference(CodingSchemeReference codingSchemeReference) {
		  if (codingSchemeReference == null) return;
		  System.out.println("\tcodingScheme: " + codingSchemeReference.getCodingScheme());
	  }

      public static void dumpPropertyReference(PropertyReference propertyReference) {
		  if (propertyReference == null) return;

		  String codingScheme = propertyReference.getCodingScheme();
		  PropertyMatchValue propertyMatchValue = propertyReference.getPropertyMatchValue();

		  String algorithm = propertyMatchValue.getMatchAlgorithm();

      	  String propertyName = propertyReference.getPropertyName();
      	  String content = propertyMatchValue.getContent();

      	  System.out.println("\tcodingScheme: " + codingScheme);
      	  System.out.println("\tmatch value: " + content);
      	  System.out.println("\tpropertyName: " + propertyName);
      	  System.out.println("\talgorithm: " + algorithm);

	  }


      public static ValueSetDefinition getValueSetDefinitionByURI(String valueSetDefinitionURI) {
		  ValueSetDefinition vsd = null;
		  try {
			  LexEVSValueSetDefinitionServices vds = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
			  String valueSetDefinitionRevisionId = null;
			  vsd = vds.getValueSetDefinition(new URI(valueSetDefinitionURI), valueSetDefinitionRevisionId);
		  } catch (Exception ex) {
			  ex.printStackTrace();
		  }
		  return vsd;
	  }



      public static ValueSetDefinition createNCItValueSetDefinition() {
			ValueSetDefinition def = new ValueSetDefinition();
			def.setDefaultCodingScheme("NCI_Thesaurus");
			def.setValueSetDefinitionName("testName");
			def.setValueSetDefinitionURI("testUri");

			def.setMappings(new Mappings());
			SupportedCodingScheme scs = new SupportedCodingScheme();
			scs.setLocalId("NCI_Thesaurus");
			scs.setUri("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
			def.getMappings().addSupportedCodingScheme(scs);

			DefinitionEntry entry = new DefinitionEntry();
			entry.setRuleOrder(0l);
			entry.setOperator(DefinitionOperator.OR);

			EntityReference ref = new EntityReference();
			ref.setEntityCode("C3262");
			ref.setReferenceAssociation("subClassOf");
			ref.setTransitiveClosure(true);
			ref.setLeafOnly(false);
			ref.setTargetToSource(true);
			entry.setEntityReference(ref);
			def.addDefinitionEntry(entry);

			return def;

      }


      public static ValueSetDefinition createValueSetDefinition() {
          ValueSetDefinition vsd = new ValueSetDefinition();
          vsd.setValueSetDefinitionName("myValueSetDefinitionName");
		  vsd.setValueSetDefinitionURI("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
		  vsd.setConceptDomain("myConceptDomain");
		  vsd.setDefaultCodingScheme("NCI_Thesaurus");

/*
//Type 0:
          ValueSetDefinitionReference valueSetDefinitionReference = new ValueSetDefinitionReference();
		  valueSetDefinitionReference.setValueSetDefinitionURI("myReferencedValueSetDefinitionURI");

          DefinitionEntry de = new DefinitionEntry();
          de.setValueSetDefinitionReference(valueSetDefinitionReference);
*/

          DefinitionEntry de = new DefinitionEntry();
          EntityReference er = new EntityReference();
          er.setEntityCode("T090"); //Occupation or Discipline
          vsd.addDefinitionEntry(de);
//Type 1:




          return vsd;
	  }



      public static DefinitionOperator string2DefinitionOperator(String operator) {
		  if (operator.compareTo("OR") == 0) {
			  return DefinitionOperator.OR;
		  }
		  else if (operator.compareTo("AND") == 0) {
			  return DefinitionOperator.AND;
		  }
		  else if (operator.compareTo("NOT") == 0) {
			  return DefinitionOperator.SUBTRACT;
		  }
		  return DefinitionOperator.OR;
	  }


	public static void run2() {

		try {
			String URL = "http://ncias-q541-v.nci.nih.gov:29080/lexevsapi60";
			LexEVSDistributed distributed =
				(LexEVSDistributed)
				ApplicationServiceProvider.getApplicationServiceFromUrl(URL, "EvsServiceInfo");

			LexEVSValueSetDefinitionServices vds = distributed.getLexEVSValueSetDefinitionServices();

			ValueSetDefinition def = new ValueSetDefinition();
			def.setDefaultCodingScheme("NCI_Thesaurus");
			def.setValueSetDefinitionName("testName");
			def.setValueSetDefinitionURI("testUri");

			EntityReference ref1 = new EntityReference();
			ref1.setEntityCode("C12727");
			ref1.setTransitiveClosure(false);
			ref1.setLeafOnly(false);
			ref1.setTargetToSource(true);

			EntityReference ref2 = new EntityReference();
			ref2.setEntityCode("C82547");
			ref2.setTransitiveClosure(true);
			ref2.setReferenceAssociation("subClassOf");
			ref2.setLeafOnly(false);
			ref2.setTargetToSource(false);

			DefinitionEntry entry = new DefinitionEntry();
			entry.setOperator(DefinitionOperator.OR);
			entry.setEntityReference(ref1);
			entry.setEntityReference(ref2);
			def.addDefinitionEntry(entry);

			AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
			csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "10.08e"));

			ResolvedValueSetDefinition rvsd = vds.resolveValueSetDefinition(def, csvList, null, null);

			ResolvedConceptReferencesIterator itr = rvsd.getResolvedConceptReferenceIterator();

			while(itr.hasNext()){
				PrintUtility.print(itr.next());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


    //testing
	public static void runMultipleTypes() {
		try {
			String URL = "http://ncias-q541-v.nci.nih.gov:29080/lexevsapi60";
			LexEVSDistributed distributed =
				(LexEVSDistributed)
				ApplicationServiceProvider.getApplicationServiceFromUrl(URL, "EvsServiceInfo");

			LexEVSValueSetDefinitionServices vds = distributed.getLexEVSValueSetDefinitionServices();

			ValueSetDefinition def = new ValueSetDefinition();
			def.setDefaultCodingScheme("NCI_Thesaurus");
			def.setValueSetDefinitionName("testName");
			def.setValueSetDefinitionURI("testUri");

            DefinitionEntry entry = new DefinitionEntry();
			PropertyReference pr = new PropertyReference();
			PropertyMatchValue pmv = new PropertyMatchValue();
			pmv.setMatchAlgorithm("exactMatch");
			pmv.setContent("blood");
			pr.setPropertyMatchValue(pmv);
			entry.setPropertyReference(pr);

			EntityReference ref1 = new EntityReference();
			ref1.setEntityCode("C12727");
			ref1.setTransitiveClosure(false);
			ref1.setLeafOnly(false);
			ref1.setTargetToSource(true);

			//entry = new DefinitionEntry();
			entry.setOperator(DefinitionOperator.OR);
			entry.setEntityReference(ref1);
			def.addDefinitionEntry(entry);

			AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
			csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "10.08e"));

			ResolvedValueSetDefinition rvsd = vds.resolveValueSetDefinition(def, csvList, null, null);

			ResolvedConceptReferencesIterator itr = rvsd.getResolvedConceptReferenceIterator();

			while(itr.hasNext()){
				PrintUtility.print(itr.next());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}



/*
Definition Resolve Time: 268008
Result Return Time: 74264
Results returned: 14588
*/


	public static void run() {
        //String URL = "http://ncias-d488-v.nci.nih.gov:29080/lexevsapi60";
        //String URL = "http://ncias-d499-v.nci.nih.gov:29080/lexevsapi60";

        String URL = "http://ncias-q541-v.nci.nih.gov:29080/lexevsapi60";


		LexEVSDistributed distributed = null;
		LexEVSValueSetDefinitionServices vds = null;
		try {
		distributed =
			(LexEVSDistributed)
			ApplicationServiceProvider.getApplicationServiceFromUrl(URL, "EvsServiceInfo");

		    vds = distributed.getLexEVSValueSetDefinitionServices();
			//ValueSetDefinition def = createValueSetDefinition();

			ValueSetDefinition def = new ValueSetDefinition();
			def.setDefaultCodingScheme("NCI_Thesaurus");
			def.setValueSetDefinitionName("testName");
			def.setValueSetDefinitionURI("myValueSetDefinitionURI#");

			def.setMappings(new Mappings());
			SupportedCodingScheme scs = new SupportedCodingScheme();
			scs.setLocalId("NCI_Thesaurus");
			scs.setUri("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
			def.getMappings().addSupportedCodingScheme(scs);

			DefinitionEntry entry = new DefinitionEntry();
			entry.setRuleOrder(0l);
			entry.setOperator(DefinitionOperator.OR);

			EntityReference ref = new EntityReference();
			ref.setEntityCode("C3262");
			ref.setReferenceAssociation("subClassOf");
			ref.setTransitiveClosure(true);
			ref.setLeafOnly(false);
			ref.setTargetToSource(true);
			entry.setEntityReference(ref);
			def.addDefinitionEntry(entry);

			AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
			//csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("urn:lsid:nlm.nih.gov:semnet", "3.2"));
			csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "10.08a"));

			long time = System.currentTimeMillis();
			ResolvedValueSetDefinition rvsd = null;
			try {
				rvsd = vds.resolveValueSetDefinition(def, csvList, null, null);

			} catch (Exception ex) {
				System.out.println("??? vds.resolveValueSetDefinition throws exception");
				return;
			}

			ResolvedConceptReferencesIterator itr = rvsd.getResolvedConceptReferenceIterator();
			System.out.println("Definition Resolve Time: " + (System.currentTimeMillis() - time));

            if (itr == null) {
				System.out.println("vds.resolveValueSetDefinition returns null???");
			}

			int count = 0;

			time = System.currentTimeMillis();
			while(itr.hasNext()){
				count += itr.next(1000).getResolvedConceptReferenceCount();
			}

			System.out.println("Result Return Time: " + (System.currentTimeMillis() - time));
			System.out.println("Results returned: " + count);

	    } catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

	}

	  public static ValueSetDefinition generateValueSetDefinition(List list, String name, String URI) {
		  Stack stack = new Stack();
		  System.out.println("\nGenerating VSD ...");
		  DefinitionEntry vDefinitionEntry = null;

// Stoppping rule -- list is empty and stack is empty

		  for (int i=0; i<list.size(); i++) {
			  Object obj = list.get(i);

			  if (obj instanceof ValueSetOperand) {
				  // Operand
                  stack.push(obj);

			  }	else if (obj instanceof String) {
				  // Operator
			      System.out.println("Set operator: " + obj.toString());
			      String operator = obj.toString();
			      if (stack.size() < 2) {
					  System.out.println("Invalid postfix expression -- operation aborts. stack.size() = " + stack.size());
					  return null;
				  } else {
					  Object operand_2_obj = stack.pop();
					  Object operand_1_obj = stack.pop();
					  // four different cases:
					  // case 1: operand_1_obj is ValueSetOperand && operand_2_obj is ValueSetOperand
					  // create a VSD with two DefinitionEntry to hold both ValueSetOperand
					  if (operand_1_obj instanceof ValueSetOperand && operand_2_obj instanceof ValueSetOperand) {
						  ValueSetDefinition new_vsd = new ValueSetDefinition();

						  ValueSetOperand operand_1 = (ValueSetOperand) operand_1_obj;
						  ValueSetOperand operand_2 = (ValueSetOperand) operand_2_obj;

						  vDefinitionEntry = valueSetOperand2DefinitionEntry(operand_1);
						  vDefinitionEntry.setOperator(DefinitionOperator.OR);
						  vDefinitionEntry.setRuleOrder(new Long((long) 1));
						  new_vsd.addDefinitionEntry(vDefinitionEntry);

						  vDefinitionEntry = valueSetOperand2DefinitionEntry(operand_2);
						  vDefinitionEntry.setOperator(string2DefinitionOperator(operator));
						  vDefinitionEntry.setRuleOrder(new Long((long) 2));
						  new_vsd.addDefinitionEntry(vDefinitionEntry);

                          String new_label = "(" + operand_1.getLabel() + " " + translateSetOperator(operator) + " " + operand_2.getLabel() + ")";
                          EntityDescription entityDescription = new EntityDescription();
                          entityDescription.setContent(new_label);
                          new_vsd.setEntityDescription(entityDescription);

                          // push new_vsd to stack
						  stack.push(new_vsd);
					  } else if (operand_1_obj instanceof ValueSetDefinition && operand_2_obj instanceof ValueSetOperand) {

						  ValueSetDefinition new_vsd = (ValueSetDefinition) operand_1_obj;
                          /*
						  vDefinitionEntry = new DefinitionEntry();
						  vDefinitionEntry.setOperator(DefinitionOperator.OR);
						  vDefinitionEntry.setRuleOrder(new Long((long) 1));

						  ValueSetDefinition operand_1 = (ValueSetDefinition) operand_1_obj;
						  ValueSetOperand operand_2 = (ValueSetOperand) operand_2_obj;

						  ValueSetDefinitionReference valueSetDefinitionReference = new ValueSetDefinitionReference();
						  valueSetDefinitionReference.setValueSetDefinitionURI(operand_1.getValueSetDefinitionURI());
						  vDefinitionEntry.setValueSetDefinitionReference(valueSetDefinitionReference);
						  new_vsd.addDefinitionEntry(vDefinitionEntry);
						  */
						  ValueSetDefinition operand_1 = (ValueSetDefinition) operand_1_obj;
						  ValueSetOperand operand_2 = (ValueSetOperand) operand_2_obj;

						  vDefinitionEntry = valueSetOperand2DefinitionEntry(operand_2);
						  vDefinitionEntry.setOperator(string2DefinitionOperator(operator));
						  vDefinitionEntry.setRuleOrder(new Long((long) (new_vsd.getDefinitionEntryCount() + 1)));
						  new_vsd.addDefinitionEntry(vDefinitionEntry);

                          String new_label = "(" + operand_1.getEntityDescription().getContent() + " " + translateSetOperator(operator) + " " + operand_2.getLabel() + ")";
                          EntityDescription entityDescription = new EntityDescription();
                          entityDescription.setContent(new_label);
                          new_vsd.setEntityDescription(entityDescription);

						  /*
						  ValueSetDefinition new_vsd = new ValueSetDefinition();

						  vDefinitionEntry = new DefinitionEntry();
						  vDefinitionEntry.setOperator(DefinitionOperator.OR);
						  vDefinitionEntry.setRuleOrder(new Long((long) 1));

						  ValueSetDefinition operand_1 = (ValueSetDefinition) operand_1_obj;
						  ValueSetOperand operand_2 = (ValueSetOperand) operand_2_obj;

						  ValueSetDefinitionReference valueSetDefinitionReference = new ValueSetDefinitionReference();
						  valueSetDefinitionReference.setValueSetDefinitionURI(operand_1.getValueSetDefinitionURI());
						  vDefinitionEntry.setValueSetDefinitionReference(valueSetDefinitionReference);
						  new_vsd.addDefinitionEntry(vDefinitionEntry);

						  vDefinitionEntry = new DefinitionEntry();
						  vDefinitionEntry.setOperator(string2DefinitionOperator(operator));
						  vDefinitionEntry.setRuleOrder(new Long((long) 2));
						  new_vsd.addDefinitionEntry(vDefinitionEntry);

                          String new_label = "(" + operand_1.getEntityDescription().getContent() + " " + translateSetOperator(operator) + " " + operand_2.getLabel() + ")";
                          EntityDescription entityDescription = new EntityDescription();
                          entityDescription.setContent(new_label);
                          new_vsd.setEntityDescription(entityDescription);
                          */

						  stack.push(new_vsd);

					  } else if (operand_1_obj instanceof ValueSetOperand && operand_2_obj instanceof ValueSetDefinition) {
						  ValueSetDefinition new_vsd = new ValueSetDefinition();

                          ValueSetOperand operand_1 = (ValueSetOperand) operand_1_obj;
                          ValueSetDefinition operand_2 = (ValueSetDefinition) operand_2_obj;

						  vDefinitionEntry = valueSetOperand2DefinitionEntry(operand_1);
						  vDefinitionEntry.setOperator(DefinitionOperator.OR);
						  vDefinitionEntry.setRuleOrder(new Long((long) 1));
						  new_vsd.addDefinitionEntry(vDefinitionEntry);

						  vDefinitionEntry = new DefinitionEntry();
						  vDefinitionEntry.setOperator(string2DefinitionOperator(operator));
						  vDefinitionEntry.setRuleOrder(new Long((long) 2));

						  ValueSetDefinitionReference valueSetDefinitionReference = new ValueSetDefinitionReference();
						  valueSetDefinitionReference.setValueSetDefinitionURI(operand_2.getValueSetDefinitionURI());
						  vDefinitionEntry.setValueSetDefinitionReference(valueSetDefinitionReference);
						  new_vsd.addDefinitionEntry(vDefinitionEntry);

                          String new_label = "(" + operand_1.getLabel() + " " + translateSetOperator(operator) + " " + operand_2.getEntityDescription().getContent() + ")";
                          EntityDescription entityDescription = new EntityDescription();
                          entityDescription.setContent(new_label);
                          new_vsd.setEntityDescription(entityDescription);

						  stack.push(new_vsd);

					  } else if (operand_1_obj instanceof ValueSetDefinition && operand_2_obj instanceof ValueSetDefinition) {
						  ValueSetDefinition new_vsd = new ValueSetDefinition();

						  vDefinitionEntry = new DefinitionEntry();

						  ValueSetDefinition operand_1 = (ValueSetDefinition) operand_1_obj;
						  ValueSetDefinition operand_2 = (ValueSetDefinition) operand_2_obj;

						  ValueSetDefinitionReference valueSetDefinitionReference = new ValueSetDefinitionReference();
						  valueSetDefinitionReference.setValueSetDefinitionURI(operand_1.getValueSetDefinitionURI());
						  vDefinitionEntry.setValueSetDefinitionReference(valueSetDefinitionReference);
						  vDefinitionEntry.setOperator(DefinitionOperator.OR);
						  vDefinitionEntry.setRuleOrder(new Long((long) 1));
						  new_vsd.addDefinitionEntry(vDefinitionEntry);

						  vDefinitionEntry = new DefinitionEntry();

						  valueSetDefinitionReference = new ValueSetDefinitionReference();
						  valueSetDefinitionReference.setValueSetDefinitionURI(operand_2.getValueSetDefinitionURI());
						  vDefinitionEntry.setValueSetDefinitionReference(valueSetDefinitionReference);
						  vDefinitionEntry.setOperator(string2DefinitionOperator(operator));
						  vDefinitionEntry.setRuleOrder(new Long((long) 2));
						  new_vsd.addDefinitionEntry(vDefinitionEntry);

						  stack.push(new_vsd);
					  }
				  }
			  }


          }
          ValueSetDefinition final_vsd = null;

          String infixExpression = null;
		  if (stack.size() > 0) {
			  Object final_operand_obj = stack.pop();
			  if (final_operand_obj instanceof ValueSetDefinition) {
				  final_vsd = (ValueSetDefinition) final_operand_obj;
				  final_vsd.setValueSetDefinitionURI(URI);
				  final_vsd.setValueSetDefinitionName(name);

				    System.out.println("\nInfix Expression:\n" + final_vsd.getEntityDescription().getContent());

					infixExpression = final_vsd.getEntityDescription().getContent();
					try {
						RPN rpn = new RPN(infixExpression);

					    System.out.println("\nPostfix Expression:");

						Vector v = rpn.convertToPostfixExpression(infixExpression);
						for (int i=0; i<v.size(); i++) {
							String s = (String) v.elementAt(i);
							System.out.println(s);
						}
				    } catch (Exception ex) {
						ex.printStackTrace();
					}

			  } else if (final_operand_obj instanceof ValueSetOperand) {
				  ValueSetOperand final_operand = (ValueSetOperand) stack.pop();

				  final_vsd = new ValueSetDefinition();
				  final_vsd.setValueSetDefinitionURI(URI);
				  final_vsd.setValueSetDefinitionName(name);

				  vDefinitionEntry = new DefinitionEntry();

				  vDefinitionEntry.setOperator(DefinitionOperator.OR);
				  vDefinitionEntry.setRuleOrder(new Long((long) 1));
				  final_vsd.addDefinitionEntry(vDefinitionEntry);

				  System.out.println("\nInfix Expression: \n" + final_operand.getLabel());

					infixExpression = final_operand.getLabel();
					try {
						RPN rpn = new RPN(infixExpression);
                        System.out.println("\nPostfix Expression:");
						Vector v = rpn.convertToPostfixExpression(infixExpression);
						for (int i=0; i<v.size(); i++) {
							String s = (String) v.elementAt(i);
							System.out.println(s);
						}
				    } catch (Exception ex) {
						ex.printStackTrace();
					}
			  }

		  }
          return final_vsd;
	  }



      public static void dumpDefinitionEntry(String vsdURI, DefinitionEntry definitionEntry) {
		  if (definitionEntry == null) return;

          CodingSchemeReference csr = definitionEntry.getCodingSchemeReference();
          String codingScheme = csr.getCodingScheme();

		  // referenced VSD
 		  ValueSetDefinitionReference valueSetDefinitionReference = definitionEntry.getValueSetDefinitionReference();
 		  if (valueSetDefinitionReference != null) {
			  String referenced_vsdURI = valueSetDefinitionReference.getValueSetDefinitionURI();
			  ValueSetDefinition vsd = getValueSetDefinitionByURI(referenced_vsdURI);
			  dumpValueSetDefinition(vsd);
		  }

		  // propertyReference
		  PropertyReference pr = definitionEntry.getPropertyReference();
		  dumpPropertyReference(pr);

		  // entityReference
		  EntityReference er = definitionEntry.getEntityReference();
          dumpEntityReference(er);

 	  }




	  public static void dumpValueSetDefinition(ValueSetDefinition vsd) {
		  if (vsd == null) return;
		  System.out.println("\n\n=========== ValueSetDefinition ===========================");
		  System.out.println("Name: " + vsd.getValueSetDefinitionName());
		  System.out.println("URI: " + vsd.getValueSetDefinitionURI());
		  System.out.println("DefinitionEntry Count: " + vsd.getDefinitionEntryCount());
          System.out.println("\n");
          java.util.Enumeration<? extends DefinitionEntry> defEntryEnum = vsd.enumerateDefinitionEntry();
          int lcv = 0;
		  while (defEntryEnum.hasMoreElements()) {
		      DefinitionEntry de = (DefinitionEntry) defEntryEnum.nextElement();
		      lcv++;

		      System.out.println("\nDefinitionEntry #" + lcv);
		      int definitionEntryType = findDefinitionEntryType(de);
		      System.out.println("\tType: " + Constant.operandTypes[findDefinitionEntryType(de)]);

              if (de.getEntityReference() != null) {
				  dumpEntityReference(de.getEntityReference());
			  }

              if (de.getPropertyReference() != null) {
 				  dumpPropertyReference(de.getPropertyReference());
			  }

			  if (de.getCodingSchemeReference() != null) {
			      dumpCodinSchemeReference(de.getCodingSchemeReference());
			  }

		      Long ruleOrder = de.getRuleOrder();
		      System.out.println("\trule order: " + ruleOrder);

		      DefinitionOperator operator = de.getOperator();
		      if (operator != null) {
				  System.out.println("\toperator: " + operator.toString());
			  } else {
				  System.out.println("\toperator is NULL???");
			  }
		  }
	  }



      public static DefinitionEntry valueSetOperand2DefinitionEntry(ValueSetOperand operand) {
          if (operand == null) return null;

          DefinitionEntry de = new DefinitionEntry();
          EntityReference entity_ref = null;
          de.setEntityReference(entity_ref);

          CodingSchemeReference codingSchemeReference = new CodingSchemeReference();

          String cs_name = operand.getCodingScheme();

          codingSchemeReference.setCodingScheme(operand.getCodingScheme());
          de.setCodingSchemeReference(codingSchemeReference);

          PropertyReference prop_ref = null;
          de.setPropertyReference(prop_ref);

          ValueSetDefinitionReference valueSetDefinitionReference = null;
          de.setValueSetDefinitionReference(valueSetDefinitionReference);

          PropertyMatchValue pmv = null;

          if (operand.getEntityCode() != null) {
			  entity_ref = new EntityReference();
			  entity_ref.setEntityCode(operand.getEntityCode());
			  entity_ref.setEntityCodeNamespace(operand.getEntityCodeNamespace());
		  }

		  switch (operand.getType()) {
				case Constant.ENTRY_TYPE_VOCABULARY:
				    de.setCodingSchemeReference(codingSchemeReference);
					break;

				case Constant.ENTRY_TYPE_CODE:
				    de.setCodingSchemeReference(codingSchemeReference);
				    de.setEntityReference(entity_ref);
					break;

				case Constant.ENTRY_TYPE_NAME:
				    // presentation property match
				    prop_ref = new PropertyReference();
				    prop_ref.setCodingScheme(operand.getCodingScheme());
				    pmv = new PropertyMatchValue();
				    pmv.setContent(operand.getMatchValue());
				    pmv.setMatchAlgorithm(operand.getMatchAlgorithm());
				    prop_ref.setPropertyMatchValue(pmv);
				    prop_ref.setPropertyName(operand.getPropertyName());
				    de.setPropertyReference(prop_ref);
					break;

				case Constant.ENTRY_TYPE_PROPERTY:
				    prop_ref = new PropertyReference();
				    prop_ref.setCodingScheme(operand.getCodingScheme());
				    pmv = new PropertyMatchValue();
				    pmv.setContent(operand.getMatchValue());
				    pmv.setMatchAlgorithm(operand.getMatchAlgorithm());
				    prop_ref.setPropertyMatchValue(pmv);
				    prop_ref.setPropertyName(operand.getPropertyName());
				    de.setPropertyReference(prop_ref);
					break;

				case Constant.ENTRY_TYPE_ASSOCIATION:
                    entity_ref.setLeafOnly(new Boolean(operand.getLeafOnly()));
                    entity_ref.setReferenceAssociation(operand.getReferenceAssociation());
                    entity_ref.setTargetToSource(new Boolean(operand.getTargetToSource()));
                    entity_ref.setTransitiveClosure(new Boolean(operand.getTransitiveClosure()));
                    de.setEntityReference(entity_ref);
					break;

				case Constant.ENTRY_TYPE_ENUMERATION:
				    // to be modified:
				    // ValuSetDefinition vsd = new ValueSetDefinition();
				    valueSetDefinitionReference = new ValueSetDefinitionReference();
				    Vector codes = operand.getCodes();
				    String codes_str = "";
				    for (int i=0; i<codes.size(); i++) {
						String s = (String) codes.elementAt(i);
						codes_str = codes_str + s;
						if (i < codes.size()-1) codes_str = codes_str + ";";
					}
				    String valueSetDefinitionURI = codes_str; // to be modified
				    valueSetDefinitionReference.setValueSetDefinitionURI(valueSetDefinitionURI);
				    de.setValueSetDefinitionReference(valueSetDefinitionReference);
				    break;

				default:
		  }
          return de;
	  }

      public static int findDefinitionEntryType(DefinitionEntry de) {
		  ValueSetDefinitionReference valueSetDefinitionReference = de.getValueSetDefinitionReference();
		  if (valueSetDefinitionReference != null) {
			  if (valueSetDefinitionReference.getValueSetDefinitionURI() != null) {
			  	  return Constant.ENTRY_TYPE_REFERENCE;
			  }
		  }
		  else if (de.getPropertyReference() != null) {
			  PropertyReference prop_ref = de.getPropertyReference();
			  if (prop_ref.getPropertyName() == null) {
				  return Constant.ENTRY_TYPE_NAME;
			  } else {
				  return Constant.ENTRY_TYPE_PROPERTY;
			  }
		  } else {
			  EntityReference er = de.getEntityReference();
			  if (er != null) {
				  if (er.getReferenceAssociation() != null) return Constant.ENTRY_TYPE_ASSOCIATION;
                  else {
					  return Constant.ENTRY_TYPE_CODE;
				  }
			  } else if (de.getCodingSchemeReference() != null) {
				  if (de.getCodingSchemeReference().getCodingScheme() != null) return Constant.ENTRY_TYPE_VOCABULARY;
			  }
		  }
		  return Constant.ENTRY_TYPE_UNKNOWN;
	  }


      public static void exportValueSetDefinition(String serviceUrl, ValueSetDefinition vsd) {
          LexEVSValueSetDefinitionServices service = RemoteServerUtil.getLexEVSValueSetDefinitionServices(serviceUrl);
          // Dependant on the feature request:
          // [GF#30262] Export ValueSetDefinition to StringBuffer in LexGrid XML format through distributed API


	  }



	protected static void displayRef(int count, ResolvedConceptReference ref){
		System.out.println("(" + count + ") " + ref.getConceptCode() + " (" + ref.getEntityDescription().getContent()
		    + ") namespace: " + ref.getCodeNamespace() + ", coding scheme: " + ref.getCodingSchemeName() + ", version: " + ref.getCodingSchemeVersion());
	}


	public static void resolveValueSetDefinition(ValueSetDefinition vsd) {
        //String URL = "http://ncias-d488-v.nci.nih.gov:29080/lexevsapi60";
        //String URL = "http://ncias-d499-v.nci.nih.gov:29080/lexevsapi60";

        //String URL = "http://ncias-q541-v.nci.nih.gov:29080/lexevsapi60";
        String URL = "http://localhost:8080/lexevsapi60";

		LexEVSDistributed distributed = null;
		LexEVSValueSetDefinitionServices vds = null;
		try {
		    vds = RemoteServerUtil.getLexEVSValueSetDefinitionServices(URL);

			AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
			//csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("urn:lsid:nlm.nih.gov:semnet", "3.2"));
			csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("Automobiles", "1.0"));

			long time = System.currentTimeMillis();
			ResolvedValueSetDefinition rvsd = null;
			try {
				rvsd = vds.resolveValueSetDefinition(vsd, csvList, null, null);

			} catch (Exception ex) {
				System.out.println("??? vds.resolveValueSetDefinition throws exception");
				return;
			}

			ResolvedConceptReferencesIterator itr = rvsd.getResolvedConceptReferenceIterator();
			System.out.println("Definition Resolve Time: " + (System.currentTimeMillis() - time));

            if (itr == null) {
				System.out.println("vds.resolveValueSetDefinition returns null???");
			}

			int count = 0;

			time = System.currentTimeMillis();
			/*
			while(itr.hasNext()){
				count += itr.next(1000).getResolvedConceptReferenceCount();
			}
			*/

		    while(itr.hasNext()){
				ResolvedConceptReference[] refs = itr.next(100).getResolvedConceptReference();
				for(ResolvedConceptReference ref : refs){
					count++;
					displayRef(count, ref);
				}
			}

			System.out.println("Result Return Time: " + (System.currentTimeMillis() - time));
			System.out.println("Results returned: " + count);

	    } catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

	}

	public static void exportValueSetDefinition(ValueSetDefinition vsd) {
        //String URL = "http://ncias-d488-v.nci.nih.gov:29080/lexevsapi60";
        //String URL = "http://ncias-d499-v.nci.nih.gov:29080/lexevsapi60";

        //String URL = "http://ncias-q541-v.nci.nih.gov:29080/lexevsapi60";
        String URL = "http://localhost:8080/lexevsapi60";

		LexEVSDistributed distributed = null;
		LexEVSValueSetDefinitionServices vds = null;
		try {
		    vds = RemoteServerUtil.getLexEVSValueSetDefinitionServices(URL);

			long time = System.currentTimeMillis();
			String valueSetDefinitionRevisionId = null;
			try {
				StringBuffer sb = vds.exportValueSetDefinition(vsd);
                System.out.println(sb.toString());

			} catch (Exception ex) {
				System.out.println("??? vds.resolveValueSetDefinition throws exception");
				return;
			}

			System.out.println("Result Return Time: " + (System.currentTimeMillis() - time));

	    } catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

	}



    public static void testResolveValueSetDefinition() {
		try {
			String versionTag = "PRODUCTION";
			HashMap<String, ValueSetDefinition> referencedVSDs = new HashMap();
			AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
		    csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("Automobiles", "1.0"));

			SortOptionList sortOptionList = null;

			ValueSetDefinition def = new ValueSetDefinition();
			def.setDefaultCodingScheme("Automobiles");
			def.setValueSetDefinitionName("testName");
			def.setValueSetDefinitionURI("testUri");

            DefinitionEntry entry = new DefinitionEntry();
				PropertyReference pr = new PropertyReference();
				pr.setCodingScheme("Automobiles");

				PropertyMatchValue pmv = new PropertyMatchValue();
				pmv.setMatchAlgorithm("exactMatch");
				pmv.setContent("Ford");
				pr.setPropertyMatchValue(pmv);
				entry.setPropertyReference(pr);
				entry.setOperator(DefinitionOperator.OR);
			def.addDefinitionEntry(entry);


            DefinitionEntry entry_2 = new DefinitionEntry();

				ValueSetDefinition ref_valueSetDefinition = new ValueSetDefinition();
				ref_valueSetDefinition.setDefaultCodingScheme("Automobiles");
				ref_valueSetDefinition.setValueSetDefinitionName("ref_testName");
				ref_valueSetDefinition.setValueSetDefinitionURI("ref_testUri");

				DefinitionEntry def_entry = new DefinitionEntry();
				PropertyReference pr2 = new PropertyReference();
				pr2.setCodingScheme("Automobiles");
				PropertyMatchValue pmv2 = new PropertyMatchValue();
				pmv2.setMatchAlgorithm("exactMatch");
				pmv2.setContent("GM");
				pr2.setPropertyMatchValue(pmv2);
				def_entry.setPropertyReference(pr2);
				def_entry.setOperator(DefinitionOperator.OR);
				ref_valueSetDefinition.addDefinitionEntry(def_entry);

				ValueSetDefinitionReference valueSetDefinitionReference = new ValueSetDefinitionReference();
				valueSetDefinitionReference.setValueSetDefinitionURI("ref_testUri");

                entry_2.setValueSetDefinitionReference(valueSetDefinitionReference);
                entry_2.setOperator(DefinitionOperator.OR);

                referencedVSDs.put("ref_testUri", ref_valueSetDefinition);

            def.addDefinitionEntry(entry_2);


            ResolvedValueSetDefinition rvsd = resolveValueSetDefinition(def,csvList,versionTag,referencedVSDs,sortOptionList);
            if (rvsd == null) {
				System.out.println("resolveValueSetDefinition returns ResolvedValueSetDefinition == NULL???");
			} else {
				System.out.println("resolveValueSetDefinition returns ResolvedValueSetDefinition != NULL.");
				ResolvedConceptReferencesIterator itr = rvsd.getResolvedConceptReferenceIterator();
				if (itr == null) {
					System.out.println("resolveValueSetDefinition returns ResolvedConceptReferencesIterator == NULL???");
				} else {
					while(itr.hasNext()){
						PrintUtility.print(itr.next());
					}
				}
		    }

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}





      public static ResolvedValueSetDefinition resolveValueSetDefinition(ValueSetDefinition vsDef,
                                                                  AbsoluteCodingSchemeVersionReferenceList csVersionList,
                                                                  String versionTag,
                                                                  HashMap<String, ValueSetDefinition> referencedVSDs,
                                                                  SortOptionList sortOptionList) throws LBException {

		  try {
				String URL = "http://ncias-q541-v.nci.nih.gov:29080/lexevsapi60";
				URL = "http://localhost:8080/lexevsapi60";
				LexEVSDistributed distributed =
					(LexEVSDistributed)
					ApplicationServiceProvider.getApplicationServiceFromUrl(URL, "EvsServiceInfo");

				LexEVSValueSetDefinitionServices vds = distributed.getLexEVSValueSetDefinitionServices();
                return vds.resolveValueSetDefinition(vsDef,csVersionList,versionTag,referencedVSDs,sortOptionList);
		  } catch (Exception ex) {
				ex.printStackTrace();
		  }
		  return null;
	  }

/*
 AbsoluteCodingSchemeVersionReference isEntityInValueSet(java.lang.String entityCode, java.net.URI entityCodeNamespace, java.net.URI valueSetDefinitionURI, java.lang.String valueSetDefinitionRevisionId, AbsoluteCodingSchemeVersionReferenceList csVersionList, java.lang.String versionTag)
 */


/*
 void setCodingSchemeURN(java.lang.String codingSchemeURN)
          Sets the value of field 'codingSchemeURN'.
 void setCodingSchemeVersion(java.lang.String codingSchemeVersion)
          Sets the value of field 'codingSchemeVersion'.

*/
      public static AbsoluteCodingSchemeVersionReferenceList getEntireAbsoluteCodingSchemeVersionReferenceList() {
        if (defaultAbsoluteCodingSchemeVersionReferenceList != null) return defaultAbsoluteCodingSchemeVersionReferenceList;

        boolean includeInactive = false;
        defaultAbsoluteCodingSchemeVersionReferenceList = new AbsoluteCodingSchemeVersionReferenceList();
        try {
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

            System.out.println("csrs.length: " + csrs.length);


            for (int i = 0; i < csrs.length; i++) {
                int j = i + 1;
                CodingSchemeRendering csr = csrs[i];
                CodingSchemeSummary css = csr.getCodingSchemeSummary();
                String formalname = css.getFormalName();
				String representsVersion = css.getRepresentsVersion();
				//_logger.debug("(" + j + ") " + formalname + "  version: " + representsVersion);

                //if (formalname.compareTo("Terminology Value Set") != 0) {

					Boolean isActive = null;
					if (csr == null) {
						_logger.warn("\tcsr == null???");
					} else if (csr.getRenderingDetail() == null) {
						_logger.warn("\tcsr.getRenderingDetail() == null");
					} else if (csr.getRenderingDetail().getVersionStatus() == null) {
						_logger
							.warn("\tcsr.getRenderingDetail().getVersionStatus() == null");
					} else {

						isActive =
							csr.getRenderingDetail().getVersionStatus().equals(
								CodingSchemeVersionStatus.ACTIVE);
					}

					//_logger.debug("\tActive? " + isActive);

					if ((includeInactive && isActive == null)
						|| (isActive != null && isActive.equals(Boolean.TRUE))
						|| (includeInactive && (isActive != null && isActive
							.equals(Boolean.FALSE)))) {
						// nv_vec.add(value);
						// csnv2codingSchemeNameMap.put(value, formalname);
						// csnv2VersionMap.put(value, representsVersion);

						// KLO 010810
						CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
						vt.setVersion(representsVersion);

						try {
							String cs_uri = DataUtils.getCodingSchemeURI(formalname, representsVersion);
							CodingScheme cs = lbSvc.resolveCodingScheme(cs_uri, vt);

                            if (cs == null) {
								//System.out.println("(***) Unable to resolve cs " + cs_uri + "(" + formalname + ")");
							} else {
								//System.out.println("(*) Resolved cs " + cs_uri + "(" + formalname + ")");
								AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
								acsvr.setCodingSchemeURN(cs.getCodingSchemeURI());
								acsvr.setCodingSchemeVersion(representsVersion);

								//System.out.println(cs.getCodingSchemeURI() + " " + representsVersion);
								defaultAbsoluteCodingSchemeVersionReferenceList.addAbsoluteCodingSchemeVersionReference(acsvr);
							}

						} catch (Exception ex) {
							//ex.printStackTrace();

							System.out.println("(***) resolveCodingScheme exception -- Unable to resolve cs " + formalname);

						}

					} else {
						_logger.error("\tWARNING: setCodingSchemeMap discards "
							+ formalname);
						_logger.error("\t\trepresentsVersion " + representsVersion);
					}

			    //}
            }
        } catch (Exception e) {
            // e.printStackTrace();
            // return null;
        }
        return defaultAbsoluteCodingSchemeVersionReferenceList;
    }




      public static Boolean isCodeInValueSet(String code, String codingScheme, String vsd_uri) {
		  try {
				String URL = "http://ncias-q541-v.nci.nih.gov:29080/lexevsapi60";
				URL = "http://localhost:19280/lexevsapi60";
				LexEVSDistributed distributed =
					(LexEVSDistributed)
					ApplicationServiceProvider.getApplicationServiceFromUrl(URL, "EvsServiceInfo");

				LexEVSValueSetDefinitionServices vds = distributed.getLexEVSValueSetDefinitionServices();

                java.lang.String valueSetDefinitionRevisionId = null;
                AbsoluteCodingSchemeVersionReferenceList csVersionList = getEntireAbsoluteCodingSchemeVersionReferenceList();
                java.lang.String csVersionTag = null;

                ResolvedValueSetCodedNodeSet rvs_cns = vds.getCodedNodeSetForValueSetDefinition(
					 new URI(vsd_uri),
                     valueSetDefinitionRevisionId,
                     csVersionList,
                     csVersionTag);

                if (rvs_cns == null) return false;
                CodedNodeSet cns = rvs_cns.getCodedNodeSet();
                ConceptReference conceptReference = new ConceptReference();
                conceptReference.setConceptCode(code);
                if (codingScheme != null) {
					conceptReference.setCodingSchemeName(codingScheme);
				}
                java.lang.Boolean bool_obj = cns.isCodeInSet(conceptReference);
                return bool_obj;

		  } catch (Exception ex) {
				ex.printStackTrace();
		  }

          return null;
	  }


/*
      public ResolvedConceptReferencesIterator searchByName(
        Vector vsd_uri_vec, String matchText, String source,
        String matchAlgorithm, boolean ranking, int maxToReturn) {
        String matchText0 = matchText;
        String matchAlgorithm0 = matchAlgorithm;
        matchText0 = matchText0.trim();

        _logger.debug("searchByName ..." + matchText);

        long ms = System.currentTimeMillis(), delay = 0;
        long tnow = System.currentTimeMillis();
        long total_delay = 0;
        boolean debug_flag = false;

        // if (debug_flag)
        // _logger.debug("Entering SearchUtils searchByName ...");

        boolean preprocess = true;
        if (matchText == null || matchText.length() == 0) {
            return null;
        }

        matchText = matchText.trim();
        if (matchAlgorithm.compareToIgnoreCase("contains") == 0) // p11.1-q11.1
                                                                 // /100{WBC}
        {
            // matchAlgorithm = Constants.CONTAIN_SEARCH_ALGORITHM;
            matchAlgorithm = findBestContainsAlgorithm(matchText);
        }

        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;

        String scheme = null;
        String version = null;

        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();

            if (lbSvc == null) {
                _logger.warn("lbSvc = null");
                return null;
            }

            Vector cns_vec = new Vector();
            for (int i = 0; i < schemes.size(); i++) {

                cns = null;
                iterator = null;
                scheme = (String) schemes.elementAt(i);

                ms = System.currentTimeMillis();

                CodingSchemeVersionOrTag versionOrTag =
                    new CodingSchemeVersionOrTag();
                version = (String) versions.elementAt(i);
                if (version != null)
                    versionOrTag.setVersion(version);
                try {
                    if (lbSvc == null) {
                        _logger.warn("lbSvc = null");
                        return null;
                    }
                    //cns = lbSvc.getNodeSet(scheme, versionOrTag, null);

                    cns = getNodeSet(lbSvc, scheme, versionOrTag);
                    if (cns != null) {
                        try {
                            cns =
                                cns.restrictToMatchingDesignations(matchText,
                                    null, matchAlgorithm, null);
                            cns = restrictToSource(cns, source);
                        } catch (Exception ex) {
                            return null;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // return null;
                }
                if (cns != null) {
                    cns_vec.add(cns);
                }

                delay = System.currentTimeMillis() - ms;
                if (debug_flag)
                    _logger.debug("Restricting CNS on " + scheme
                        + " delay (millisec.): " + delay);
                if (debug_flag)
                    System.out.flush();

            }

            iterator = null;


            LocalNameList restrictToProperties = new LocalNameList();
            // boolean resolveConcepts = true;
            // if (!ranking) resolveConcepts = false;
            boolean resolveConcepts = false;

            SortOptionList sortCriteria = null;

            if (ranking) {
                sortCriteria = null;// Constructors.createSortOptionList(new
                                    // String[]{"matchToQuery"});

            } else {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "entityDescription" }); // code
                _logger.debug("*** Sort alphabetically...");
                resolveConcepts = false;
            }
            try {
                try {
                    ms = System.currentTimeMillis();
                    if (debug_flag)
                        _logger
                            .debug("Calling  cns.resolve to resolve the union CNS ... ");
                    // iterator = cns.resolve(sortCriteria, null,
                    // restrictToProperties, null, resolveConcepts);


                    iterator =
                        new QuickUnionIterator(cns_vec, sortCriteria, null,
                            restrictToProperties, null, resolveConcepts);

                    delay = System.currentTimeMillis() - ms;
                    if (debug_flag)
                        _logger.debug("Resolve CNS union "
                            + "delay (millisec.): " + delay);

                    // Debug.println("cns.resolve delay ---- Run time (ms): " +
                    // (delay = System.currentTimeMillis() - ms) +
                    // " -- matchAlgorithm " + matchAlgorithm);
                    // DBG.debugDetails(delay, "cns.resolve",
                    // "searchByName, CodedNodeSet.resolve");

                } catch (Exception e) {
                    _logger.error("Method: SearchUtils.searchByName 2");
                    _logger.error("* ERROR: cns.resolve throws exceptions.");
                    _logger.error("* " + e.getClass().getSimpleName() + ": "
                        + e.getMessage());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        int lcv = 0;
        int iterator_size = 0;
        if (iterator != null) {
            try {
                iterator_size = iterator.numberRemaining();
                _logger.debug("Number of matches: " + iterator_size);
            } catch (Exception ex) {

            }
        }

        ms = System.currentTimeMillis();
        while (iterator_size == 0 && lcv < schemes.size()) {
            scheme = (String) schemes.elementAt(lcv);

            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            version = (String) versions.elementAt(lcv);
            if (version != null)
                versionOrTag.setVersion(version);
            iterator =
                matchConceptCode(scheme, version, matchText0, source,
                    "LuceneQuery");
            if (iterator != null) {
                try {
                    iterator_size = iterator.numberRemaining();
                    _logger.debug("Number of matches: " + iterator_size);
                } catch (Exception ex) {

                }
            }
            lcv++;
        }

        delay = System.currentTimeMillis() - ms;
        if (debug_flag)
            _logger
                .debug("Match concept code " + "delay (millisec.): " + delay);

        total_delay = System.currentTimeMillis() - tnow;
        _logger.debug("Total search delay: (millisec.): " + total_delay);

        // return iterator;
        return new ResolvedConceptReferencesIteratorWrapper(iterator);

    }

*/



    public static void testExportResolvedValueSetDefinition() {
		try {
			String versionTag = "PRODUCTION";
			HashMap<String, ValueSetDefinition> referencedVSDs = new HashMap();
			AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
		    csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("Automobiles", "1.0"));

			SortOptionList sortOptionList = null;

			ValueSetDefinition def = new ValueSetDefinition();
			def.setDefaultCodingScheme("Automobiles");
			def.setValueSetDefinitionName("testName");
			def.setValueSetDefinitionURI("testUri");

            DefinitionEntry entry = new DefinitionEntry();
				PropertyReference pr = new PropertyReference();
				pr.setCodingScheme("Automobiles");

				PropertyMatchValue pmv = new PropertyMatchValue();
				pmv.setMatchAlgorithm("exactMatch");
				pmv.setContent("Ford");
				pr.setPropertyMatchValue(pmv);
				entry.setPropertyReference(pr);
				entry.setOperator(DefinitionOperator.OR);
			def.addDefinitionEntry(entry);


            DefinitionEntry entry_2 = new DefinitionEntry();

				ValueSetDefinition ref_valueSetDefinition = new ValueSetDefinition();
				ref_valueSetDefinition.setDefaultCodingScheme("Automobiles");
				ref_valueSetDefinition.setValueSetDefinitionName("ref_testName");
				ref_valueSetDefinition.setValueSetDefinitionURI("ref_testUri");

				DefinitionEntry def_entry = new DefinitionEntry();
				PropertyReference pr2 = new PropertyReference();
				pr2.setCodingScheme("Automobiles");
				PropertyMatchValue pmv2 = new PropertyMatchValue();
				pmv2.setMatchAlgorithm("exactMatch");
				pmv2.setContent("GM");
				pr2.setPropertyMatchValue(pmv2);
				def_entry.setPropertyReference(pr2);
				def_entry.setOperator(DefinitionOperator.OR);
				ref_valueSetDefinition.addDefinitionEntry(def_entry);

				ValueSetDefinitionReference valueSetDefinitionReference = new ValueSetDefinitionReference();
				valueSetDefinitionReference.setValueSetDefinitionURI("ref_testUri");

                entry_2.setValueSetDefinitionReference(valueSetDefinitionReference);
                entry_2.setOperator(DefinitionOperator.OR);

                referencedVSDs.put("ref_testUri", ref_valueSetDefinition);

            def.addDefinitionEntry(entry_2);

			String URL = "http://localhost:8080/lexevsapi60";
			LexEVSDistributed distributed =
				(LexEVSDistributed)
				ApplicationServiceProvider.getApplicationServiceFromUrl(URL, "EvsServiceInfo");

			LexEVSValueSetDefinitionServices vsd_service = distributed.getLexEVSValueSetDefinitionServices();


            String csVersionTag = versionTag;
            boolean failOnAllErrors = false;

			InputStream reader =  vsd_service.exportValueSetResolution(def, referencedVSDs,
			   csvList, csVersionTag, failOnAllErrors);

			if (reader != null) {
				StringBuffer sb = new StringBuffer();
				try {
					for(int c = reader.read(); c != -1; c = reader.read()) {
						sb.append((char)c);
					}
					System.out.println(sb.toString());

				} catch(IOException e) {
					//throw e;
					System.out.println("buffer reader failed???");

				} finally {
					try {
						reader.close();
					} catch(Exception e) {
						// ignored
					}
			    }
			} else {
				System.out.println("reader == null???");
			}


		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("testExportResolvedValueSetDefinition exception???");
		}
	}





    public static void testExportResolvedValueSetDefinition_NCIDEV() {
		try {
			String versionTag = "PRODUCTION";
			HashMap<String, ValueSetDefinition> referencedVSDs = new HashMap();
			AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
		    csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("NCI_Thesaurus", "11.01e"));

			SortOptionList sortOptionList = null;

			ValueSetDefinition def = new ValueSetDefinition();
			def.setDefaultCodingScheme("NCI_Thesaurus");
			def.setValueSetDefinitionName("testName");
			def.setValueSetDefinitionURI("testUri");

            DefinitionEntry entry = new DefinitionEntry();
				PropertyReference pr = new PropertyReference();
				pr.setCodingScheme("NCI_Thesaurus");

				PropertyMatchValue pmv = new PropertyMatchValue();
				pmv.setMatchAlgorithm("exactMatch");
				pmv.setContent("Blood");
				pr.setPropertyMatchValue(pmv);
				entry.setPropertyReference(pr);
				entry.setOperator(DefinitionOperator.OR);
			def.addDefinitionEntry(entry);


            DefinitionEntry entry_2 = new DefinitionEntry();

				ValueSetDefinition ref_valueSetDefinition = new ValueSetDefinition();
				ref_valueSetDefinition.setDefaultCodingScheme("NCI_Thesaurus");
				ref_valueSetDefinition.setValueSetDefinitionName("ref_testName");
				ref_valueSetDefinition.setValueSetDefinitionURI("ref_testUri");

				DefinitionEntry def_entry = new DefinitionEntry();
				PropertyReference pr2 = new PropertyReference();
				pr2.setCodingScheme("NCI_Thesaurus");
				PropertyMatchValue pmv2 = new PropertyMatchValue();
				pmv2.setMatchAlgorithm("exactMatch");
				pmv2.setContent("Sarcoma");
				pr2.setPropertyMatchValue(pmv2);
				def_entry.setPropertyReference(pr2);
				def_entry.setOperator(DefinitionOperator.OR);
				ref_valueSetDefinition.addDefinitionEntry(def_entry);

				ValueSetDefinitionReference valueSetDefinitionReference = new ValueSetDefinitionReference();
				valueSetDefinitionReference.setValueSetDefinitionURI("ref_testUri");

                entry_2.setValueSetDefinitionReference(valueSetDefinitionReference);
                entry_2.setOperator(DefinitionOperator.OR);

                referencedVSDs.put("ref_testUri", ref_valueSetDefinition);

            def.addDefinitionEntry(entry_2);

			//String URL = "http://localhost:19280/lexevsapi60";

			String URL = "http://ncias-d488-v.nci.nih.gov:29080/lexevsapi60";

			LexEVSDistributed distributed =
				(LexEVSDistributed)
				ApplicationServiceProvider.getApplicationServiceFromUrl(URL, "EvsServiceInfo");

			LexEVSValueSetDefinitionServices vsd_service = distributed.getLexEVSValueSetDefinitionServices();


            String csVersionTag = versionTag;
            boolean failOnAllErrors = false;

			InputStream reader =  vsd_service.exportValueSetResolution(def, referencedVSDs,
			   csvList, csVersionTag, failOnAllErrors);

			if (reader != null) {
				StringBuffer sb = new StringBuffer();
				try {
					for(int c = reader.read(); c != -1; c = reader.read()) {
						sb.append((char)c);
					}
					System.out.println(sb.toString());

				} catch(IOException e) {
					//throw e;
					System.out.println("buffer reader failed???");

				} finally {
					try {
						reader.close();
					} catch(Exception e) {
						// ignored
					}
			    }
			} else {
				System.out.println("reader == null???");
			}


		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("testExportResolvedValueSetDefinition exception???");
		}
	}


///////////////////////////////////////////////////////////////


//isIsActive
//public java.lang.Boolean isIsActive()

    public static HashMap getValueSetDefinitionURI2VSD_map() {
		if (_valueSetDefinitionURI2VSD_map != null) {
			return _valueSetDefinitionURI2VSD_map;
		}
		_valueSetDefinitionURI2VSD_map = new HashMap();

		try {

			String URL = "http://ncias-d488-v.nci.nih.gov:29080/lexevsapi60";


			LexEVSDistributed distributed =
				(LexEVSDistributed)
				ApplicationServiceProvider.getApplicationServiceFromUrl(URL, "EvsServiceInfo");

			LexEVSValueSetDefinitionServices vsd_service = distributed.getLexEVSValueSetDefinitionServices();

			/*
			String URL = "http://ncias-d488-v.nci.nih.gov:29080/lexevsapi60";

			LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices(URL);
			*/
			String valueSetDefinitionRevisionId = null;
			List list = vsd_service.listValueSetDefinitionURIs();
			for (int i=0; i<list.size(); i++) {
				String uri = (String) list.get(i);
			    ValueSetDefinition vsd = vsd_service.getValueSetDefinition(new URI(uri), valueSetDefinitionRevisionId);

			    System.out.println(uri + " -- active? " + vsd.isIsActive());
			    _valueSetDefinitionURI2VSD_map.put(uri, vsd);
		    }
		    return _valueSetDefinitionURI2VSD_map;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}



/*

 java.util.List<java.lang.String> listValueSetsWithEntityCode(java.lang.String entityCode,
          java.net.URI entityCodeNamespace, AbsoluteCodingSchemeVersionReferenceList csVersionList, java.lang.String versionTag)
          Returns all the value set definition uris that contains supplied entity code.




*/


    public static AbsoluteCodingSchemeVersionReferenceList getAbsoluteCodingSchemeVersionReferenceList() {
		AbsoluteCodingSchemeVersionReferenceList list = new AbsoluteCodingSchemeVersionReferenceList();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            if (lbSvc == null) return null;
            CodingSchemeRenderingList csrl = null;
            try {
                csrl = lbSvc.getSupportedCodingSchemes();
            } catch (LBInvocationException ex) {
                ex.printStackTrace();
                System.out.println("lbSvc.getSupportedCodingSchemes() FAILED..."
                    + ex.getCause());
                return list;
            }
            CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
            for (int i = 0; i < csrs.length; i++) {
                int j = i + 1;
                CodingSchemeRendering csr = csrs[i];
                CodingSchemeSummary css = csr.getCodingSchemeSummary();
                String formalname = css.getFormalName();

                Boolean isActive = null;
                if (csr == null) {
                    System.out.println("\tcsr == null???");
                } else if (csr.getRenderingDetail() == null) {
                    System.out.println("\tcsr.getRenderingDetail() == null");
                } else if (csr.getRenderingDetail().getVersionStatus() == null) {
                    System.out.println("\tcsr.getRenderingDetail().getVersionStatus() == null");
                } else {
                    isActive =
                        csr.getRenderingDetail().getVersionStatus().equals(
                            CodingSchemeVersionStatus.ACTIVE);
                }

                String representsVersion = css.getRepresentsVersion();
                boolean includeInactive = false;

                if ((includeInactive && isActive == null)
                    || (isActive != null && isActive.equals(Boolean.TRUE))
                    || (includeInactive && (isActive != null && isActive
                        .equals(Boolean.FALSE)))) {


                    CodingSchemeVersionOrTag vt =
                        new CodingSchemeVersionOrTag();
                    vt.setVersion(representsVersion);

                    try {
                        CodingScheme cs =
                            lbSvc.resolveCodingScheme(formalname, vt);

                        AbsoluteCodingSchemeVersionReference csv_ref = new AbsoluteCodingSchemeVersionReference();
                        csv_ref.setCodingSchemeURN(cs.getCodingSchemeURI());
                        csv_ref.setCodingSchemeVersion(representsVersion);

                        System.out.println(cs.getCodingSchemeURI() + " (version: " + representsVersion + ")");

                        list.addAbsoluteCodingSchemeVersionReference(csv_ref);
                    } catch (Exception ex) {

                    }
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            // return null;
        }
        return list;
    }



    public static void listValueSetsContainingEntityCode(String entityCode) {

		try {
			String URL = "http://ncias-d488-v.nci.nih.gov:29080/lexevsapi60";

			LexEVSDistributed distributed =
				(LexEVSDistributed)
				ApplicationServiceProvider.getApplicationServiceFromUrl(URL, "EvsServiceInfo");

			LexEVSValueSetDefinitionServices vsd_service = distributed.getLexEVSValueSetDefinitionServices();

            HashMap hmap = getValueSetDefinitionURI2VSD_map();
            Iterator it = hmap.keySet().iterator();
            while (it.hasNext()) {
				String uri = (String) it.next();

				System.out.println("Checking " + uri);
				java.net.URI entityCodeNamespace = null;
				java.net.URI valueSetDefinitionURI = new URI(uri);
				java.lang.String valueSetDefinitionRevisionId = null;
				AbsoluteCodingSchemeVersionReferenceList csVersionList = null;
				String versionTag = "PRODUCTION";

                AbsoluteCodingSchemeVersionReference acsvr = vsd_service.isEntityInValueSet(
					   entityCode, entityCodeNamespace, valueSetDefinitionURI,
					   valueSetDefinitionRevisionId,
					   csVersionList, versionTag);
				if (acsvr != null) {
					System.out.println(entityCode + " is found in " + uri);

				} else {
					System.out.println(entityCode + " is not found in " + uri);
				}
			}

		} catch (Exception ex) {
			System.out.println("(*) Exception thrown by listValueSetsWithEntityCode");
			ex.printStackTrace();
		}

	}

/*
 java.util.List<java.lang.String> listValueSetsWithEntityCode(java.lang.String entityCode,
          java.net.URI entityCodeNamespace,
          AbsoluteCodingSchemeVersionReferenceList csVersionList, java.lang.String versionTag)
          Returns all the value set definition uris that contains supplied entity code.

*/

    public static void listValueSetsWithEntityCode(String matchText) {
		try {

			String URL = "http://ncias-d488-v.nci.nih.gov:29080/lexevsapi60";


			LexEVSDistributed distributed =
				(LexEVSDistributed)
				ApplicationServiceProvider.getApplicationServiceFromUrl(URL, "EvsServiceInfo");

			LexEVSValueSetDefinitionServices vsd_service = distributed.getLexEVSValueSetDefinitionServices();
            AbsoluteCodingSchemeVersionReferenceList csVersionList = getAbsoluteCodingSchemeVersionReferenceList();
            String versionTag = "PRODUCTION";
			List list = vsd_service.listValueSetsWithEntityCode(matchText, null, csVersionList, versionTag);

			if (list != null) {
				System.out.println("listValueSetsWithEntityCode returns " + list.size() + " VSD URIs.");
				for (int j=0; j<list.size(); j++) {
					String uri = (String) list.get(j);
					System.out.println(uri);
				}
			}
		} catch (Exception ex) {
			System.out.println("(*) Exception thrown by listValueSetsWithEntityCode");
			ex.printStackTrace();
		}
 	}


	public static Vector getValueSetURIs() {
		if (value_set_uri_vec != null) return value_set_uri_vec;
		value_set_uri_vec = new Vector();
		LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
        List list = vsd_service.listValueSetDefinitionURIs();
        for (int i=0; i<list.size(); i++) {
			String t = (String) list.get(i);
			value_set_uri_vec.add(t);
		}
		value_set_uri_vec = SortUtils.quickSort(value_set_uri_vec);
		return value_set_uri_vec;
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

    public static ValueSetDefinition findValueSetDefinitionByURI(String uri) {
		if (uri == null) return null;
		/*
	    if (uri.indexOf("|") != -1) {
			Vector u = parseData(uri);
			uri = (String) u.elementAt(1);
		}
		*/

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


/**
* Resolve a value set definition provided using the supplied set of coding scheme versions.
* This method also takes in list of ValueSetDefinitions (referencedVSDs) that are referenced by the ValueSetDefinition (vsDef).
* If referencedVSDs list is provided, these ValueSetDefinitions will be used to resolve vsDef.
*
* @param valueSetDefinition - value set definition object
* @param csVersionList - list of coding scheme versions to use in resolution. IF the value set definition uses a version that isn't mentioned in this list,
* the resolve function will return the codingScheme and version that was used as a default for the resolution.
* @param versionTag - the tag (e.g. "devel", "production", ...) to be used to determine which coding scheme to be used
* @param referencedVSDs - List of ValueSetDefinitions referenced by vsDef. If provided, these ValueSetDefinitions will be used to resolve vsDef.
* @param sortOptionList - List of sort options to apply during resolution. If supplied, the sort algorithms will be applied in the order provided. Any
* algorithms not valid to be applied in context of node set iteration, as specified in the sort extension description,
* will result in a parameter exception. Available algorithms can  be retrieved through the LexBIGService getSortExtensions()
* method after being defined to the LexBIGServiceManager extension registry.
* @return Resolved Value Domain Definition
* @throws LBException
*/
//public ResolvedValueSetDefinition resolveValueSetDefinition(ValueSetDefinition vsDef, AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag, HashMap<String, ValueSetDefinition> referencedVSDs, SortOptionList sortOptionList) throws LBException;


//////////////////////////////////////////////////////////////////////////////////

    public static ResolvedConceptReferencesIterator resolveValueSetDefinition(ValueSetDefinition vsd, HashMap<String, ValueSetDefinition> referencedVSDs) {
		ResolvedConceptReferencesIterator iterator = null;
		boolean failOnAllErrors = false;
		String csVersionTag = null;//"PRODUCTION";

		SortOptionList sortOptionList = null;

		AbsoluteCodingSchemeVersionReferenceList csVersionList = getEntireAbsoluteCodingSchemeVersionReferenceList();

        try {
        	LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
			ResolvedValueSetDefinition rvsd = null;
			try {
				//?????????????????????????????????????????????????????????????
				System.out.println("Calling vsd_service.resolveValueSetDefinition ...");

				rvsd = vsd_service.resolveValueSetDefinition(vsd, csVersionList, csVersionTag, referencedVSDs, sortOptionList);

				System.out.println("Exiting vsd_service.resolveValueSetDefinition ...");

				if (rvsd != null) {
					System.out.println("rvsd != null -- returns iterator ...");
			    	iterator = rvsd.getResolvedConceptReferenceIterator();
			    	return iterator;
				} else {
					System.out.println("rvsd == null???");
				}


			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Error: vsd_service.resolveValueSetDefinition throws exception.");
			}

		} catch (Exception ex) {
			//ex.printStackTrace();
			System.out.println("Error: getLexEVSValueSetDefinitionServices throws exception.");
		}
		return null;
	}


}

/*

Three new methods:

/**
* Exports Value Set Definition to StringBuffer in LexGrid XML format.
*
* @param valueSetDefinitionURI - Value Set Definition URI to be exported
* @param valueSetDefintionRevisionId - (Optional) The version of the value set definition
* @return StringBuffer containing value set definition in LexGrid XML format
* @throws LBException
*/

//public StringBuffer exportValueSetDefinition(URI valueSetDefinitionURI, String valueSetDefinitionRevisionId) throws LBException;

/**
* Exports supplied Value Set Definition to StringBuffer in LexGrid XML format.
*
* @param valueSetDefinition - Value Set Definition object to be exported to StringBuffer in LexGrid XML format
* @return StringBuffer containing value set definition in LexGrid XML format
* @throws LBException
*/

//public StringBuffer exportValueSetDefinition(ValueSetDefinition valueSetDefinition) throws LBException;

/**
* Resolve a value set definition provided using the supplied set of coding scheme versions.
* This method also takes in list of ValueSetDefinitions (referencedVSDs) that are referenced by the ValueSetDefinition (vsDef).
* If referencedVSDs list is provided, these ValueSetDefinitions will be used to resolve vsDef.
*
* @param valueSetDefinition - value set definition object
* @param csVersionList - list of coding scheme versions to use in resolution. IF the value set definition uses a version that isn't mentioned in this list,
* the resolve function will return the codingScheme and version that was used as a default for the resolution.
* @param versionTag - the tag (e.g. "devel", "production", ...) to be used to determine which coding scheme to be used
* @param referencedVSDs - List of ValueSetDefinitions referenced by vsDef. If provided, these ValueSetDefinitions will be used to resolve vsDef.
* @param sortOptionList - List of sort options to apply during resolution. If supplied, the sort algorithms will be applied in the order provided. Any
* algorithms not valid to be applied in context of node set iteration, as specified in the sort extension description,
* will result in a parameter exception. Available algorithms can  be retrieved through the LexBIGService getSortExtensions()
* method after being defined to the LexBIGServiceManager extension registry.
* @return Resolved Value Domain Definition
* @throws LBException
*/
//public ResolvedValueSetDefinition resolveValueSetDefinition(ValueSetDefinition vsDef, AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag, HashMap<String, ValueSetDefinition> referencedVSDs, SortOptionList sortOptionList) throws LBException;
