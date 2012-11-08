package gov.nih.nci.evs.valueseteditor.utilities;

import java.util.*;
import java.io.*;

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

/** a constructor
 */
public class RPN extends Stack
{
    /** a static constant, 0
     */
    final public static int ACTION_INFIX_TO_RPN_STACK = 0;
    /** a static constant, 1
     */
    final public static int ACTION_INFIX_TO_HOLD_STACK = 1;
    /** a static constant, 2
     */
    final public static int ACTION_HOLD_TO_RPN_STACK = 2;
    /** a static constant, 3
     */
    final public static int ACTION_DELETE_HOLD = 3;
    /** a static constant, 4
     */
    final public static int ACTION_COMPLETE = 4;
    /** a static constant, 5
     */
    final public static int ACTION_ERRUNION = 5;

    /** infix expression, for example, (4 + 5) × 6
     */
    private String infixExpression = "";

    /** a constructor
     *
     * @exception Exception an Exception instance
     */
    public RPN() throws Exception {}

    /** a constructor
     *
     * @param infixExpression infix expression
     * @exception Exception an Exception instance
     */
    public RPN(String infixExpression) throws Exception
    {
        setInfixExpression(infixExpression);
    }


	/** Replaces a substring.
	 *
	 * @param aInput an input string
	 * @param aOldPattern substring to be replaced
	 * @param aNewPattern new substring to replace aOldPattern
	 * @return a new string
	 */
	public static String replace(
          final String aInput,
          final String aOldPattern,
          final String aNewPattern
        ){
           if ( aOldPattern.equals("") ) {
              throw new IllegalArgumentException("Old pattern must have content.");
           }

           final StringBuffer result = new StringBuffer();
           //startIdx and idxOld delimit various chunks of aInput; these
           //chunks always end where aOldPattern begins
           int startIdx = 0;
           int idxOld = 0;
           while ((idxOld = aInput.indexOf(aOldPattern, startIdx)) >= 0) {
             //grab a part of aInput which does not include aOldPattern
             result.append( aInput.substring(startIdx, idxOld) );
             //add aNewPattern to take place of aOldPattern
             result.append( aNewPattern );

             //reset the startIdx to just after the current match, to see
             //if there are any further matches
             startIdx = idxOld + aOldPattern.length();
           }
           //the final chunk will go to the end of aInput
           result.append( aInput.substring(startIdx) );
           return result.toString();
        }


    /** @param infixExpression
     * @exception Exception
     */
    public Vector convertToPostfixExpression(String infixExpression) throws Exception
    {

	    Vector v = new Vector();
        if (infixExpression == null || infixExpression.length() < 1)
        {
            return v;
        }


        infixExpression = replace(infixExpression, "&#8746;", "UNION");
        infixExpression = replace(infixExpression, "&#8745;", "INTERSECTION");
        infixExpression = replace(infixExpression, "/", "DIFFERENCE");


        infixExpression = replace(infixExpression, " ", "^");
	    infixExpression = replace(infixExpression, "^INTERSECTION^", " INTERSECTION ");
	    infixExpression = replace(infixExpression, "^UNION^", " UNION ");
	    infixExpression = replace(infixExpression, "^DIFFERENCE^", " DIFFERENCE ");

		char delim = ' ';
        ExpressionParser exprParse = new ExpressionParser(infixExpression, delim);
        Stack reversePostfix = new Stack();
        Stack holdStack = new Stack();
        Stack infixStack = exprParse.getExpressionItemStack();

        ExpressionItem exprItem = (ExpressionItem) infixStack.pop();
        int action = 5;
        ExpressionItem topOfHold;
        whileLoop:while (exprItem != null)
        {
            if (holdStack.size() > 0)
            {
                topOfHold = (ExpressionItem) holdStack.peek();
            }
            else
            {
                topOfHold = null;
            }
            action = exprItem.getAction(topOfHold);
            switch (action)
            {
                case ACTION_INFIX_TO_RPN_STACK:
                    reversePostfix.push(exprItem);
                    break;
                case ACTION_INFIX_TO_HOLD_STACK:
                    holdStack.push(exprItem);
                    break;
                case ACTION_HOLD_TO_RPN_STACK:
                    reversePostfix.push(holdStack.pop());
                    continue; //don't read another item
                case ACTION_DELETE_HOLD:
                    holdStack.pop();
                    break;
                case ACTION_COMPLETE:
                    break whileLoop;
                case ACTION_ERRUNION:
				{
                    throw new Exception(
                        "Error Converting to Postfix");
				}
            }
            try
            {
                exprItem = (ExpressionItem) infixStack.pop();
            }
            catch (Exception e)
            {
                exprItem = null;
            }
        }
        if (holdStack.size() > 0)
        {
            topOfHold = (ExpressionItem) holdStack.peek();
        }
        else
        {
            topOfHold = null;
        }

        while (topOfHold != null &&
               topOfHold instanceof ExpressionItem.BiOperator)
        {
            reversePostfix.push(holdStack.pop());
            if (holdStack.size() > 0)
            {
                topOfHold = (ExpressionItem) holdStack.peek();
            }
            else
            {
                topOfHold = null;
            }
        }

        if (holdStack.size() > 0)
        {
            throw new Exception("Error Converting to Postfix");
        }
        while (this.size() > 0)
        {
            this.removeElementAt(0);
        }
        while (!reversePostfix.empty())
        {
            this.push(reversePostfix.pop());
        }
        while (!empty())
        {
			Object ob = pop();
			String t = ob.toString();
			t = replace(t, "^", " ");

			t = trimRight(t);
			v.addElement(t);
		}
		return v;
    }


	public String trimRight(String s)
	{
	    int len = s.length();
		if (len == 0) return s;
		while (Character.isWhitespace(s.charAt(len-1)))
        {
            s = s.substring(0, len-1);
			len = s.length();
			if (s.length() == 0) return s;
        }
		return s;
	}

    //testing...
	public boolean equals(RPN rpn) {
		return true;
	}



    public void setInfixExpression(String infixExpression) throws
        Exception
    {
        this.infixExpression = infixExpression;
        Vector v = convertToPostfixExpression(infixExpression);

		for (int i=0; i<v.size(); i++)
		{
			String s = (String) v.elementAt(i);
		}
    }

    private static class ExpressionParser
    {
        String expression;
        StringBuffer expr;
		char delim;

        public ExpressionParser(String expression, char delim)
        {
            this.expression = expression;
			this.delim = delim;
        }

        private ExpressionItem readExpressionItem(StringBuffer str)
        {
            if (str.length() <= 0)
            {
                return null;
            }

            while (Character.isWhitespace(str.charAt(0)))
            {
                str.delete(0, 1);
            }

            char c = str.charAt(0);

            //check for Parenthesis
            if (c == '(' || c == ')')
            {
                str.delete(0, 1);
                return new ExpressionItem.Parenthesis(
                    c == '(' ? ExpressionItem.Parenthesis.LEFT :
                    ExpressionItem.Parenthesis.RIGHT);
            }

            int newKind = -1;

            //check for bi operator
            String strString = str.toString();
            for (int i = 0; i < ExpressionItem.BiOperator.OPERATUNIONS.length; i++)
            {
                if (strString.startsWith(ExpressionItem.BiOperator.OPERATUNIONS[i]))
                {
                    newKind = i;
                    str.delete(0, ExpressionItem.BiOperator.OPERATUNIONS[i].length());
                    break;
                }
            }
            if (newKind != -1)
            {
                return new ExpressionItem.BiOperator(newKind);
            }


            //check for string
			String thisString = str.toString();

			//int n1 = thisString.indexOf(' ');

			int n1 = thisString.indexOf(delim);

			int n2 = thisString.indexOf(')');
		    if (n1 != -1 && n2 == -1)
			{
			    String substr = thisString.substring(0, n1);
				str.delete(0, n1);
				return new ExpressionItem.VariableString(substr);
			}
			else if (n1 != -1 && n2 != -1)
			{
				int min = n1;
				if (n2 < min) min = n2;
			    String substr = thisString.substring(0, min);
				str.delete(0, min);
				return new ExpressionItem.VariableString(substr);

			}
			else if (n1 == -1 && n2 != -1)
			{
			    String substr = thisString.substring(0, n2);
				str.delete(0, n2);
				return new ExpressionItem.VariableString(substr);

			}
			else
			{
                thisString = str.toString();
				str.delete(0, str.length());
				return new ExpressionItem.VariableString(thisString);
			}

            //return null;
        }

        public Stack getExpressionItemStack()
        {
            expr = new StringBuffer(expression);
            ExpressionItem exprItem = readExpressionItem(expr);
            Stack infixStack = new Stack();
            while (exprItem != null)
            {
                infixStack.push(exprItem);
                exprItem = readExpressionItem(expr);
            }
            Stack resultStack = new Stack();
            while (infixStack.size() > 0)
            {
                resultStack.push(infixStack.pop());
            }
            return resultStack;
        }
    }

/*
    public static void main(String[] args)
    {
	    String pathname = "test.dat";
		try{
            RPN es = new RPN();
			BufferedReader buffReader = new BufferedReader(new FileReader(pathname));
			String infixExpression =buffReader.readLine();

			while(infixExpression != null){
				Vector v = es.convertToPostfixExpression(infixExpression);
				for (int i=0; i<v.size(); i++)
				{
					Object obj = (Object) v.elementAt(i);
				}

				infixExpression=buffReader.readLine();
			}

            buffReader.close();

		}catch(Exception e){}
	}
*/
}

abstract class ExpressionItem
{
    abstract int getAction(ExpressionItem exprItem);

    static class Number extends ExpressionItem
    {
        private int value;
		public Number(int value) {this.value = value;}
		public int getAction(ExpressionItem exprItem)
			{return RPN.ACTION_INFIX_TO_RPN_STACK;}
		public String toString()
			{return Integer.toString(value);}
    }

    static class Variable extends ExpressionItem
    {
        private char value;
	/** a counstructor for the Reverse Polish Notation (RPN) class
	 *
	 * @param value
	 */
        public Variable(char value) {this.value = value;}
        public int getAction(ExpressionItem exprItem)
            {return RPN.ACTION_INFIX_TO_RPN_STACK;}
        public String toString()
			{return ""+value;}
    }

    static class VariableString extends ExpressionItem
    {
        private String value;
	/** @param value
	 */
        public VariableString(String value) {this.value = value;}
        public int getAction(ExpressionItem exprItem)
            {return RPN.ACTION_INFIX_TO_RPN_STACK;}
        public String toString()
			{return ""+value;}
    }

    static class Parenthesis extends ExpressionItem
    {
	/** a static constance, 0
	 */
        public static final int LEFT = 0;
	/** a static constance, 1
	 */
        public static final int RIGHT = 1;
	/** kind
	 */
        private int kind;
	/** constuctor for Parenthesis.
	 *
	 * @param kind
	 */
        public Parenthesis(int kind) {this.kind = kind;}
	/** @param exprItem
	 */
        public int getAction(ExpressionItem exprItem)
        {
            if (kind == LEFT) return RPN.ACTION_INFIX_TO_HOLD_STACK;
            else
            {
                if (exprItem == null) return RPN.ACTION_ERRUNION;
                if (exprItem instanceof Parenthesis)
                    return RPN.ACTION_DELETE_HOLD;
                return RPN.ACTION_HOLD_TO_RPN_STACK;
            }
        }
        public String toString()
			{return (kind==LEFT?")":"(");}
    }

    static class BiOperator extends ExpressionItem
    {
        public static final int INTERSECTION = 0;
        public static final int UNION = 1;
        public static final int DIFFERENCE = 2;

		public static final String[] OPERATUNIONS =
           {"INTERSECTION", "UNION", "DIFFERENCE"};

        private int kind;
        public BiOperator(int kind) {this.kind = kind;}

        int getOrderOfOperationsLevel ()
        {
            switch (kind)
            {
			    case INTERSECTION:
				case UNION:
				case DIFFERENCE:
				    return 1;
            }
            return 0;
        }

        public int getAction(ExpressionItem exprItem)
        {
            if (exprItem == null || exprItem instanceof Parenthesis)
                return RPN.ACTION_INFIX_TO_HOLD_STACK;
            else
            if (exprItem instanceof BiOperator)
            {
                BiOperator topOfHold =
                    (BiOperator)exprItem;
                if (topOfHold.getOrderOfOperationsLevel() <
                    getOrderOfOperationsLevel())
                    return RPN.ACTION_INFIX_TO_HOLD_STACK;
                else return RPN.ACTION_HOLD_TO_RPN_STACK;
            }
            else
            return RPN.ACTION_ERRUNION;
        }

        public String toString()
			{return OPERATUNIONS[kind];}
    }
}
