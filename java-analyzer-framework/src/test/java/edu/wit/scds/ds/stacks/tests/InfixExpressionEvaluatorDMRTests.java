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


package edu.wit.scds.ds.stacks.tests ;

import education.the_software_toolsmith.analyzer.framework.dynamicanalysis.TestingBase ;

import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectMethods.invoke ;

import java.io.File ;
import java.io.FileNotFoundException ;
import java.util.Scanner ;

/**
 * Test driver for InfixExpressionEvaluator.
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
 * Optional: Support for valid, single-digit expressions (standard/minimum above) plus:
 * <ul>
 * <li>valid, multi-digit unsigned decimal operands
 * </ul>
 * <p>
 * Optional: Support for valid, single-digit expressions (standard/minimum above) plus invalid expressions
 * containing:
 * <ul>
 * <li>unbalanced parentheses
 * <li>multiple consecutive operators
 * <li>division by zero
 * <li>consecutive operands
 * </ul>
 *
 * @author Dave Rosenberg
 *
 * @version 1.0 2019-02-08 initial implementation
 * @version 2.0 2019-06-08
 *     <ul>
 *     <li>add support for comments in expression files
 *     <li>fix error in calculation of incorrect results
 *     <li>revise output format to resemble JUnit tests
 *     <li>fix error in recognition of errors distinguishing valid/invalid expression evaluation
 *     <li>consolidate all testing into a single run
 *     <ul>
 *     <li>recognize single-digit, multi-digit, and invalid expressions
 *     <li>separate counts for each expression category
 *     <li>enhance reporting per category
 *     <li>calculate %-age correct by category
 *     </ul>
 *     </ul>
 * @version 2.0.1 2019-06-22 minor fix to comment
 * @version 2.1 2019-10-13 remove unused import; reverse order of equals() test to avoid NullPointerException
 * @version 2.1.1 2019-10-15 update to next test data file version
 * @version 2.2 2020-02-17 cleanup toward DRCo coding standard compliance
 * @version 2.3 2020-02-21 fix missing 'incorrect' message
 * @version 2.4 2020-10-07
 *     <ul>
 *     <li>minor revisions to reflect changes to assignment wrt invalid expressions
 *     <li>redirect output to detailed log file
 *     </ul>
 * @version 3.0 2021-06-23
 *     <ul>
 *     <li>track changes to testing infrastructure
 *     <li>convert to non-static to enable inheritance wrt infrastructure
 *     <li>move expressions data file
 *     </ul>
 * @version 3.1 2022-10-29
 *     <ul>
 *     <li>revise expression handling to include leading and trailing spaces as specified in the test data
 *     file
 *     <li>handle thousands grouping separators (,) in expected results
 *     </ul>
 * @version 3.2 2024-11-20 updates to track changes in my testing framework
 * @version 3.3 2025-07-20 updates to track changes in my testing framework
 * @version 4.0 2025-12-27
 *     <ul>
 *     <li>track change from testing framework to analysis framework
 *     <li>track change to Maven
 *     </ul>
 */
public class InfixExpressionEvaluatorDMRTests extends TestingBase
    {

    /*
     * utility constants
     */

    // specify the class under test
    private final static String TEST_PACKAGE_NAME_BASE = "edu.wit.scds.ds.stacks" ;
    private final static String TEST_CLASS_NAME = "InfixExpressionEvaluator" ;

    // choose the particular implementation invocation: - single-digit, valid expressions:
    @SuppressWarnings( "unused" )
    private final static String TEST_PACKAGE_NAME_SINGLE_DIGIT = TEST_PACKAGE_NAME_BASE + ".single_digit" ;  // in
                                                                                                             // ...single_digit
    // - multi-digit, valid expressions:
    @SuppressWarnings( "unused" )
    private final static String TEST_PACKAGE_NAME_MULTI_DIGIT = TEST_PACKAGE_NAME_BASE + ".multi_digit" ;   // in
                                                                                                            // ...multi_digit
    // - single-digit, invalid expressions:
    @SuppressWarnings( "unused" )
    private final static String TEST_PACKAGE_NAME_INVALID_EXPRESSIONS = TEST_PACKAGE_NAME_BASE + ".invalid" ;       // in
                                                                                                                    // ...invalid
    // - multi-digit, invalid expressions:
    @SuppressWarnings( "unused" )
    private final static String TEST_PACKAGE_NAME_FULL = TEST_PACKAGE_NAME_BASE + ".full" ; // full solution

    private final static String TEST_PACKAGE_NAME = TEST_PACKAGE_NAME_BASE ;

    private final static String TEST_DATA_DMR_PATH = "/test-data-dmr/stacks/" ;


    /**
     * constructor
     */
    protected InfixExpressionEvaluatorDMRTests()
        {

        super( TEST_PACKAGE_NAME, TEST_CLASS_NAME ) ;

        }   // end constructor


    /**
     * Test driver for the InfixExpressionEvaluator's evaluate()
     *
     * @param args
     *     -unused-
     *
     * @throws FileNotFoundException
     *     if the expressions file can't be opened
     */
    public static void main( final String[] args ) throws FileNotFoundException
        {

        final InfixExpressionEvaluatorDMRTests testInstance = new InfixExpressionEvaluatorDMRTests() ;

        testInstance.runTests() ;

        }   // end main()


    /**
     * Actual test driver for the InfixExpressionEvaluator's evaluate()
     *
     * @throws FileNotFoundException
     *     if the expressions file can't be opened
     */
    private void runTests() throws FileNotFoundException
        {

        // convenience variable
        final String expressionsFilename = findFiles( "infix-expressions.dat" ).get( 0 ).toString() ;
        writeConsole( "Using test data from: %s%n%n", expressionsFilename ) ;

        // counters for statistics reporting
        int lineCount = 0 ;

        // counters for each test type
        final int[] expressionCounts = { 0, 0, 0 } ;
        final int[] correctResultCounts = { 0, 0, 0 } ;
        final String[] expressionTypes = { "single-digit", "multi-digit", "invalid" } ;

        int expressionType = -1 ;

        // these declarations are outside try{} so they're available to catch{}
        String fullLine = null ;
        String messagePrefix = "[]" ;

        /*
         * evaluate all expressions in the data file
         */
        try ( Scanner expressions = new Scanner( new File( expressionsFilename ) ) )
            {

            while ( expressions.hasNextLine() )
                {
                // get an expression from the file
                fullLine = expressions.nextLine() ;

                lineCount++ ;

                final String[] fullLineParts = fullLine.split( "#" ) ;
                // # is the comment delimiter - everything from the delimiter to the end of the line is
                // ignored/discarded

                // only evaluate the expression if there is one
                if ( ( fullLineParts.length > 0 ) && ( fullLineParts[ 0 ].length() > 0 ) )
                    {
                    /* @formatter:off
                     *
                     * expression lines are formatted as:
                     *  {t}::{expression}={expected result}
                     * where
                     *  {t} is a single-digit representing the expression type
                     *  {expression} is the expression to evaluate
                     *  {expected result} is the correct response from
                     *      InfixExpressionEvaluator.evaluate()
                     *
                     * note: whitespace in {expression} is retained and passed to evaluate() as
                     * part of the expression
                     *
                     * @formatter:on
                     */
                    expressionType = fullLineParts[ 0 ].charAt( 0 ) - '0' ;
                    expressionCounts[ expressionType ]++ ;

                    // extract the expression and the expected result
                    final String[] expressionParts = fullLineParts[ 0 ].split( "::" )[ 1 ].split( "=" ) ;

                    final String expression = expressionParts[ 0 ] ;
                    final String expressionResult = expressionParts[ 1 ].trim() ;

                    boolean expressionIsValid = true ;

                    long expectedResult ;
                    long actualResult ;

                    // if we can convert the info to the right of the '=' to a value, the expression is valid,
                    // otherwise, it's the message evaluate() will include when it throws an
                    // ArithmeticException
                    try
                        {
                        // accept thousands grouping separators in the result values
                        expectedResult = Long.parseLong( expressionResult.replace( ",", "" ) ) ;
                        }
                    catch ( final NumberFormatException e )
                        {
                        expectedResult = 0 ;
                        expressionIsValid = false ;
                        }   // end try

                    messagePrefix = String.format( "[%,d, %s, %,d]",
                                                   lineCount,
                                                   expressionTypes[ expressionType ],
                                                   expressionCounts[ expressionType ] ) ;

                    // display what we're evaluating
                    writeLog( "%s expression: \"%s\"%n", messagePrefix, expression ) ;

                    if ( expressionIsValid )
                        {
                        writeLog( "%s expect: %,d%n", messagePrefix, expectedResult ) ;
                        }
                    else
                        {
                        writeLog( "%s expect: ArithmeticException( \"%s\" )%n",
                                  messagePrefix,
                                  expressionResult ) ;
                        }   // end if

                    // evaluate the expression
                    try
                        {
                        // evaluate the expression
                        actualResult
                                = (long) invoke( super.testClass, null, "evaluate", new Class<?>[]
                                { String.class }, expression ) ;

                        // no exception thrown: display the actual result
                        writeLog( "%s actual: %,d%n", messagePrefix, actualResult ) ;

                        // and whether it's correct
                        if ( ( expressionIsValid ) && ( actualResult == expectedResult ) )
                            {
                            correctResultCounts[ expressionType ]++ ;
                            // count it if it is

                            writeLog( "%s correct%n", messagePrefix ) ;
                            }
                        else    // incorrect - didn't throw an exception
                            {
                            writeLog( "%s incorrect%n", messagePrefix ) ;
                            }

                        }
                    catch ( final ArithmeticException e )
                        {
                        // all errors are reported by throwing an ArithmeticException with an informative
                        // message

                        if ( ! expressionIsValid )
                            {
                            // display the actual result
                            writeLog( "%s actual: ArithmeticException( \"%s\" )%n",
                                      messagePrefix,
                                      e.getMessage() ) ;

                            // and whether it's correct
                            if ( expressionResult.equals( e.getMessage() ) )
                                {
                                correctResultCounts[ expressionType ]++ ;

                                writeLog( "%s correct%n", messagePrefix ) ;
                                }
                            else    // incorrect
                                {
                                writeLog( "%s incorrect%n", messagePrefix ) ;
                                }

                            }
                        else    // incorrect - expression is valid
                            {
                            writeLog( "%s incorrect%n", messagePrefix ) ;
                            }

                        }
                    catch ( final Throwable e )
                        {
                        writeLog( "%s actual: %s%s%n",
                                  messagePrefix,
                                  e.getClass().getSimpleName(),
                                  e.getMessage() == null
                                          ? ""
                                          : ": \"" + e.getMessage() + "\"" ) ;

                        writeLog( "%s incorrect%n", messagePrefix ) ;
                        }   // end try

                    writeLog( "%n" ) ;
                    }   // end only evaluate the expression if there is one
                else // but skip 'deleted' tests
                if ( ( fullLine.length() >= 4 ) && ! "###-".equals( fullLine.substring( 0, 4 ) ) )
                    {
                    writeLog( "[%,d] %s%n", lineCount, fullLine ) ;
                    }

                }   // end while

            }
        catch ( final Throwable e )
            {
            // typically indicates an error in the expressions file
            writeLog( "%s Unexpected exception:%n\t\"%s\"%n\t\"%s\"%n\tinput: %s%n",
                      messagePrefix,
                      e.toString(),
                      e.getMessage(),
                      fullLine ) ;
            }   // end try

        // display test execution summary
        writeLog( "-----\n" ) ;

        for ( expressionType = 0 ; expressionType < expressionTypes.length ; expressionType++ )
            {

            if ( expressionCounts[ expressionType ] > 0 )
                {
                writeConsole( "%,d of %,d %s expressions (%3d%%) evaluated correctly%n",
                              correctResultCounts[ expressionType ],
                              expressionCounts[ expressionType ],
                              expressionTypes[ expressionType ],
                              ( correctResultCounts[ expressionType ] * 100 )
                                                                 / expressionCounts[ expressionType ] ) ;
                }
            else
                {
                writeLog( "No %s expressions evaluated%n", expressionTypes[ expressionType ] ) ;
                }

            }

        /*
         * cleanup and exit
         */

        // close the detailed log file
        super.closeLog() ;

        // re-enable System.exit() - was automatically disabled in TestingBase
        super.enableExit() ;

        }   // end main()

    }   // end class InfixExpressionEvaluatorDMRTests
