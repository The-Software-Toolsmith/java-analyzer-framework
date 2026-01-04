/* @formatter:off
 *
 * © David M Rosenberg
 *
 * Topic: Stacks and Recursion
 *
 * Usage restrictions:
 *
 * You may use this code for exploration, experimentation, and furthering your
 * learning for this course. You may not use this code for any other
 * assignments, in my course or elsewhere, without explicit permission, in
 * advance, from myself (and the instructor of any other course).
 *
 * Further, you may not post (including in a public repository such as on github)
 * nor otherwise share this code with anyone other than current students in my
 * sections of this course.
 *
 * Violation of these usage restrictions will be considered a violation of
 * Wentworth Institute of Technology's Academic Honesty Policy.  Unauthorized posting
 * or use of this code may also be considered copyright infringement and may subject
 * the poster and/or the owners/operators of said websites to legal and/or financial
 * penalties.  Students are permitted to store this code in a private repository
 * or other private cloud-based storage.
 *
 * Do not modify or remove this notice.
 *
 * @formatter:on
 */


package edu.wit.scds.ds.stacks.app.full ;

import edu.wit.scds.ds.stacks.StackInterface ;
import edu.wit.scds.ds.stacks.LinkedStack ;

import java.util.NoSuchElementException ;
import java.util.Scanner ;

/**
 * A class to evaluate infix arithmetic expressions:
 * <ul>
 * <li>all values are represented as {@code long}s
 * <li>all arithmetic operations are performed with integer arithmetic (no
 * fractional values)
 * <li>all arithmetic operations are performed with Java binary operators
 * <li>no Java Class Library (JCL) classes or methods are used to perform the
 * expression evaluation
 * </ul>
 * <p>
 * Standard (minimum): Support for valid expressions containing:
 * <ul>
 * <li>single-digit, unsigned, decimal operands
 * <li>operators: {@code +, -, *, /, %}
 * <li>parenthesized subexpressions, including nested parentheses
 * <li>whitespace (spaces, tabs, etc.) characters, which are ignored
 * </ul>
 * and invalid expressions containing:
 * <ul>
 * <li>invalid characters
 * </ul>
 * <p>
 * Optional: Support for valid, single-digit expressions (standard/minimum
 * above) plus:
 * <ul>
 * <li>valid, multi-digit unsigned decimal operands
 * </ul>
 * <p>
 * Optional: Support for valid, single-digit expressions (standard/minimum
 * above) plus invalid expressions containing:
 * <ul>
 * <li>unbalanced parentheses
 * <li>multiple consecutive operators
 * <li>division by zero
 * <li>consecutive operands
 * </ul>
 * <p>
 * This implementation includes all required, single-digit functionality plus
 * all optional functionality.
 *
 * @author Dave Rosenberg
 *
 * @version 1.0 2019-02-08 initial implementation
 * @version 1.1 2019-06-08
 *     <ul>
 *     <li>rename operandsStack to valuesStack
 *     <li>ignore embedded blanks in expression
 *     <li>replace multi-digit input to hand-rolled instead of building a string
 *     then using JCL to parse it
 *     <li>check for consecutive operands
 *     <li>check for null expression (0-length)
 *     </ul>
 * @version 1.2 2020-02-27
 *     <ul>
 *     <li>make compliant with class coding standard
 *     <li>enhance test for whitespace
 *     </ul>
 * @version 2.0 2020-02-27 extracted from multi-digit InfixExpressionEvaluator
 * @version 2.0.1 2020-02-28
 *     <ul>
 *     <li>remove unneeded validation of {@code expression} argument to
 *     {@code evaluate()}
 *     <li>move {@code ')'} precedence to highest value
 *     </ul>
 * @version 2.0.2 2020-02-28 remove unneeded test for division by zero in
 *     {@code doDivision()}
 * @version 2.1 2020-06-15 modify
 *     {@code doOperations(operatorStack, valuesStack)} to push the result onto
 *     the {@code valuesStack} rather than returning the result
 * @version 2.2 2020-10-07
 *     <ul>
 *     <li>remove doOperations(operatorStack, valuesStack)
 *     <li>minor revisions to make code more succinct
 *     <li>simplify invalid expression testing - limit to empty/non-empty stack
 *     verification
 *     <li>add interactive test driver in {@code main()}
 *     </ul>
 * @version 2.3 2021-10-14 add remainder operator (%)
 * @version 2.4 2022-10-25
 *     <ul>
 *     <li>rename {@code valuesStack} to {@code valueStack} for consistency with
 *     {@code operatorStack} which is singular
 *     </ul>
 * @version 2.5 2022-10-27 finalize solution for current semester
 * @version 2.6 2022-10-29 simplify multi-digit operand loop
 * @version 2.7 2024-11-20
 *     <ul>
 *     <li>reorder arguments to {@code doOperation()}
 *     <li>reorder cases in {@code precedenceOf()}
 *     </ul>
 * @version 2.7.1 2025-03-23 minor tweak
 * @version 2.8 2025-10-09
 *     <ul>
 *     <li>switch stack implementations
 *     <li>switch case statement form in <code>evaluate()</code> main loop
 *     </ul>InfixExpressionEvaluatorDMRTests.java
 * @version 3.0 2025-12-27 
 *     <ul>
 *     <li>track change from testing framework to analysis framework
 *     <li>track change to Maven
 *     </ul>
 */
public final class InfixExpressionEvaluator
    {

    /**
     * prevent instantiation
     */
    private InfixExpressionEvaluator()
        {

        // can't instantiate an InfixExpressionEvaluator

        }   // end constructor


    /**
     * Evaluate an infix arithmetic expression.
     *
     * @param expression
     *     an infix expression composed of unsigned decimal operands and a
     *     combination of operators ({@code +, -, *, /, %}) including
     *     parenthesized (sub-)expressions; may include whitespace characters
     *
     * @return the result of evaluating {@code expression}
     *
     * @throws ArithmeticException
     *     if {@code expression} is syntactically invalid, is null/0-length,
     *     attempts to divide by zero, or contains unrecognized characters
     */
    public static long evaluate( final String expression ) throws ArithmeticException
        {

        // verify that we were given an expression
        if ( expression == null )
            {
            throw new ArithmeticException( "null expression" ) ;
            }

        // we'll use 2 stacks to remember what's left to evaluate
        final StackInterface<Character> operatorStack = new LinkedStack<>() ;
        final StackInterface<Long> valueStack = new LinkedStack<>() ;

        // evaluate the expression one character at a time
        for ( int i = 0 ; i < expression.length() ; i++ )
            {
            // get the next character to process from the expression
            final char currentCharacter = expression.charAt( i ) ;

            // quietly ignore whitespace
            if ( Character.isWhitespace( currentCharacter ) )
                {
                continue ;  // done processing this whitespace character
                } // end if whitespace

            // evaluate operands
            if ( Character.isDigit( currentCharacter ) )
                {
                // convert current character to its equivalent numeric value
                long operand = currentCharacter - '0' ;

                // index of next character in expression
                int nextI = i + 1 ;

                // keep looking for additional digits for this operand
                while ( ( nextI < expression.length() ) && Character.isDigit( expression.charAt( nextI ) ) )
                    {
                    // the next character is a digit, include its value in this
                    // operand
                    operand = ( operand * 10 ) + ( expression.charAt( nextI ) - '0' ) ;

                    i = nextI ;     // reflect that we processed this character
                    nextI++ ;
                    }   // end while

                // we have the current operand, save it
                valueStack.push( operand ) ;

                continue ;  // done processing this operand
                }   // end evaluate operand

            // handle an operator, parenthesis, or unexpected character
            switch ( currentCharacter )
                {
                // valid binary operators
                case '*',
                     '/',
                     '%',
                     '+',
                     '-' ->
                    {

                    while ( ( !operatorStack.isEmpty() )
                            && ( precedenceOf( currentCharacter )
                                 <= precedenceOf( peekOperator( operatorStack ) ) ) )
                        {
                        final char operator = popOperator( operatorStack ) ;
                        final long rightOperand = popOperand( valueStack ) ;
                        final long leftOperand = popOperand( valueStack ) ;

                        valueStack.push( doOperation( leftOperand, operator, rightOperand ) ) ;
                        }

                    operatorStack.push( currentCharacter ) ;
                    }

                // beginning of a parenthesized subexpression
                case '('
                    -> operatorStack.push( currentCharacter ) ;

                // end of a parenthesized subexpression
                // evaluate all operators on the operator stack until we get to
                // the matching open parenthesis
                case ')' ->
                    {

                    while ( peekOperator( operatorStack ) != '(' )
                        {
                        final char operator = popOperator( operatorStack ) ;
                        final long rightOperand = popOperand( valueStack ) ;
                        final long leftOperand = popOperand( valueStack ) ;

                        valueStack.push( doOperation( leftOperand, operator, rightOperand ) ) ;
                        }

                    // remove the open parenthesis
                    popOperator( operatorStack ) ;
                    }

                default
                    -> throw new ArithmeticException( "unrecognized character: '" + currentCharacter + "'" ) ;
                }   // end switch

            }   // end for

        // process all operators left on the stack
        while ( !operatorStack.isEmpty() )
            {
            // retrieve the next operator to evaluate
            final char operator = popOperator( operatorStack ) ;

            // check for unbalanced parenthesis
            if ( operator == '(' )
                {
                throw new ArithmeticException( "invalid expression" ) ;
                }

            // must have 2 operands available
            final long rightOperand = popOperand( valueStack ) ;
            final long leftOperand = popOperand( valueStack ) ;

            // perform the operation and save the result
            valueStack.push( doOperation( leftOperand, operator, rightOperand ) ) ;
            }   // end while

        // assertion: the operatorStack is empty
        // assertion: for a valid expression, the valueStack contains one value

        if ( valueStack.isEmpty() )
            {
            throw new ArithmeticException( "null expression" ) ;
            }

        // the result should be on the operand stack
        // - must have 1 value
        final long result = popOperand( valueStack ) ;

        // - make sure all operands have been used
        if ( !valueStack.isEmpty() )
            {
            throw new ArithmeticException( "invalid expression" ) ;
            }

        return result ;

        }   // end evaluate()


    /**
     * Calculate {@code leftOperand operator rightOperand}
     *
     * @param leftOperand
     *     the operand appearing to the left of {@code operator}
     * @param operator
     *     one of {@code +, -, *, /, %} representing addition, subtraction,
     *     multiplication, division, remainder respectively
     * @param rightOperand
     *     the operand appearing to the right of {@code operator}
     *
     * @return the result of calculating
     *     {@code leftOperand operator rightOperand}
     *
     * @throws ArithmeticException
     *     if the operator is unrecognized
     */
    private static long doOperation( final long leftOperand,
                                     final char operator,
                                     final long rightOperand )
            throws ArithmeticException
        {

        return switch ( operator )
            {
            case '+'
                -> doAddition( leftOperand, rightOperand ) ;

            case '-'
                -> doSubtraction( leftOperand, rightOperand ) ;

            case '*'
                -> doMultiplication( leftOperand, rightOperand ) ;

            case '/'
                -> doDivision( leftOperand, rightOperand ) ;

            case '%'
                -> doRemainder( leftOperand, rightOperand ) ;

            default
                -> throw new IllegalStateException( "unrecognized operator: '" + operator + "'" ) ;
            } ;

        }   // end doOperation()


    /**
     * Calculate the sum: {@code leftAddend + rightAddend}
     *
     * @param leftAddend
     *     the first addend
     * @param rightAddend
     *     the second addend
     *
     * @return the resulting sum
     */
    private static long doAddition( final long leftAddend,
                                    final long rightAddend )
        {

        return leftAddend + rightAddend ;

        }   // end doAddition()


    /**
     * Calculate the difference: {@code minuend - subtrahend}
     *
     * @param minuend
     *     the minuend
     * @param subtrahend
     *     the subtrahend
     *
     * @return the resulting difference
     */
    private static long doSubtraction( final long minuend,
                                       final long subtrahend )
        {

        return minuend - subtrahend ;

        }   // end doSubtraction()


    /**
     * Calculate the product: {@code multiplicand * multiplier}
     *
     * @param multiplicand
     *     the multiplicand
     * @param multiplier
     *     the multiplier
     *
     * @return the resulting product
     */
    private static long doMultiplication( final long multiplicand,
                                          final long multiplier )
        {

        return multiplicand * multiplier ;

        }   // end doMultiplication()


    /**
     * Calculate the quotient: {@code dividend / divisor}
     *
     * @param dividend
     *     the dividend
     * @param divisor
     *     the divisor
     *
     * @return the resulting quotient
     *
     * @throws ArithmeticException
     *     if the divisor is zero (0)
     */
    private static long doDivision( final long dividend,
                                    final long divisor )
            throws ArithmeticException
        {

        if ( divisor == 0 )
            {
            throw new ArithmeticException( "division by zero" ) ;
            }

        return dividend / divisor ;

        }   // end doDivision()


    /**
     * Calculate the remainder: {@code dividend % divisor}
     *
     * @param dividend
     *     the dividend
     * @param divisor
     *     the divisor
     *
     * @return the resulting remainder
     *
     * @throws ArithmeticException
     *     if the divisor is zero (0)
     */
    private static long doRemainder( final long dividend,
                                     final long divisor )
            throws ArithmeticException
        {

        if ( divisor == 0 )
            {
            throw new ArithmeticException( "division by zero" ) ;
            }

        return dividend % divisor ;

        }   // end doRemainder()


    /**
     * Retrieve the top operator from the {@code operatorStack}
     *
     * @param operatorStack
     *     the stack containing the operator(s)
     *
     * @return the top operator
     *
     * @throws ArithmeticException
     *     if the stack is empty
     */
    private static char peekOperator( final StackInterface<Character> operatorStack )
        {

        if ( operatorStack.isEmpty() )
            {
            throw new ArithmeticException( "invalid expression" ) ;
            }

        return operatorStack.peek() ;

        }   // end peekOperator()


    /**
     * Remove the top operator from the {@code operatorStack}
     *
     * @param operatorStack
     *     the stack containing the operator(s)
     *
     * @return the top operator
     *
     * @throws ArithmeticException
     *     if the stack is empty
     */
    private static char popOperator( final StackInterface<Character> operatorStack )
        {

        if ( operatorStack.isEmpty() )
            {
            throw new ArithmeticException( "invalid expression" ) ;
            }

        return operatorStack.pop() ;

        }   // end popOperator()


    /**
     * Remove the top operand from the {@code valueStack}
     *
     * @param valueStack
     *     the stack containing the operand(s)
     *
     * @return the top operand
     *
     * @throws ArithmeticException
     *     if the stack is empty
     */
    private static long popOperand( final StackInterface<Long> valueStack )
        {

        if ( valueStack.isEmpty() )
            {
            throw new ArithmeticException( "invalid expression" ) ;
            }

        return valueStack.pop() ;

        }   // end popOperand()


    /**
     * Determine the operator's precedence - based on PEMDAS.
     *
     * @param operator
     *     a character representing the specified operation
     *
     * @return a numeric values representing the {@code operator}'s precedence
     *     such that higher precedence {@code operator}s have a higher value -
     *     {@code operator}s with the same precedence will return the same value
     *
     * @throws IllegalStateException
     *     if {@code operator} is unrecognized
     */
    private static int precedenceOf( final char operator )
        {

        return switch ( operator )
            {
            case '*',
                 '/',
                 '%'
                -> 1 ;

            case '+',
                 '-'
                -> 0 ;

            case '('
                -> -1 ;

            default
                ->   // if this executes, you have a bug in your implementation
                throw new IllegalStateException( "unexpected operator: '" + operator + "'" ) ;
            } ;

        }   // end precedenceOf()


    /**
     * Interactive driver for testing/debugging.
     * <p>
     * Facilitates interactive testing of {@code evaluate()}. You may modify or
     * replace this code but any changes must compile cleanly.
     *
     * @param args
     *     -unused-
     */
    public static void main( final String[] args )
        {

        // interactive via console
        try ( Scanner input = new Scanner( System.in ) )
            {

            while ( true )
                {
                System.out.printf( "%nEnter expression (or quit): " ) ;
                final String expression = input.nextLine() ;

                // continue or exit?
                final String trimmedExpression = expression.trim() ;

                if ( ( trimmedExpression.length() <= 4 ) && !"".equals( trimmedExpression )
                     && trimmedExpression.equalsIgnoreCase( "quit".substring( 0,
                                                                              trimmedExpression.length() ) ) )
                    {
                    break ; // exit
                    }

                System.out.print( "Result: " ) ;

                try
                    {
                    System.out.printf( "%,d%n", evaluate( expression ) ) ;
                    }
                catch ( final Throwable e )
                    {
                    e.printStackTrace( System.out ) ;
                    }   // end try/catch

                }   // end while

            }
        catch ( final NoSuchElementException e )
            {
            // ignore end-of-file/input - done processing
            }
        catch ( final Throwable e )
            {
            e.printStackTrace( System.out ) ;
            }   // end try/catch

        System.out.printf( "%ndone%n" ) ;

        }   // end main()

    }   // end class InfixExpressionEvaluator