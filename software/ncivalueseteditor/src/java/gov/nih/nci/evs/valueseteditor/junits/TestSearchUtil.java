/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-value-set-editor/LICENSE.txt for details.
 */

package gov.nih.nci.evs.valueseteditor.junits;

import java.util.Iterator;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;

import gov.nih.nci.evs.valueseteditor.utilities.ValueSetSearchUtil;
import junit.framework.TestCase;

/**
 * 
 */

/**
 * Search Utility JUnits
 * @author garciawa2
 */
public class TestSearchUtil extends TestCase {

      public void testListConceptDomains() throws Exception {
          ValueSetSearchUtil util = new ValueSetSearchUtil();
          Map<String,String> map = util.getConceptDomainNames();
          
          System.out.println("\ngetConceptDomainNames map:");
          Iterator<?> it = map.entrySet().iterator();
          while (it.hasNext()) {
              @SuppressWarnings("unchecked")
              Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
              System.out.println(pairs.getKey() + " = " + pairs.getValue());
          }
      }

      public void testGetOntologyList() throws Exception {
          ValueSetSearchUtil util = new ValueSetSearchUtil();
          Map<String,String> map = (Map<String,String>) util.getOntologyList();
          
          System.out.println("\ngetOntologyList map:");
          Iterator<?> it = map.entrySet().iterator();
          while (it.hasNext()) {
              @SuppressWarnings("unchecked")
              Map.Entry<String,CodingSchemeSummary> pairs 
              	= (Map.Entry<String,CodingSchemeSummary>)it.next();
              String key = pairs.getKey();              
              System.out.println(key + " = " + pairs.getValue());
          }    	  
      }
      
} // End of TestSearchUtil
