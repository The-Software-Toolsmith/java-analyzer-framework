/* @formatter:off
 *
 * © David M Rosenberg
 *
 * Topic: Lists
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


package edu.wit.scds.ds.lists.tests ;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively ;

import education.the_software_toolsmith.analyzer.framework.dynamicanalysis.JUnitTestingBase ;
import education.the_software_toolsmith.analyzer.framework.dynamicanalysis.TestingException ;

import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectBackingStores.getContentsOfChainBackedCollection ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectDataFields.getIntField ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectDataFields.getReferenceField ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectMethods.invoke ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectReferenceTypes.instantiate ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.TestData.COMPARE_EQUALITY ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.TestData.COMPARE_IDENTITY ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.TestData.IS_ORDERED ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.TestData.compareDatasets ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.TestData.datasetContains ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.TestData.datasetToString ;

import edu.wit.scds.ds.buildingblocks.enhanced.Node ;
import edu.wit.scds.ds.buildingblocks.utilities.VisualizationSupport ;
import edu.wit.scds.ds.lists.ListInterface ;

import org.junit.jupiter.api.Disabled ;
import org.junit.jupiter.api.DisplayName ;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation ;
import org.junit.jupiter.api.Order ;
import org.junit.jupiter.api.TestInfo ;
import org.junit.jupiter.api.TestInstance ;
import org.junit.jupiter.api.TestInstance.Lifecycle ;
import org.junit.jupiter.api.TestMethodOrder ;
import org.junit.jupiter.params.ParameterizedTest ;
import org.junit.jupiter.params.provider.CsvFileSource ;

import java.lang.reflect.Array ;
import java.util.Arrays ;

/**
 * JUnit tests for the LList class. All methods are tested. These tests require the API for the {@code LList}
 * class implement {@code ListInterface<T>}.
 *
 * @author David M Rosenberg
 *
 * @version 1.0 2020-07-17 initial set of tests - based on LinkedBagDMRTests<br>
 * @version 1.1 2020-11-09 miscellaneous updates, enhancements, and cleanup<br>
 * @version 2.0 2021-07-29 start migration to new testing infrastructure<br>
 * @version 2.1 2021-11-17 enhance stable sort test<br>
 * @version 2.2 2021-12-01 correct messages for sort/stable sort tests when verifying repeatability and an
 *     exception occurs<br>
 * @version 3.0 2025-02-26
 *     <ul>
 *     <li>rename all {@code LinkedList} references to {@code LList} to match class construct
 *     <li>add tests of private methods: {@code appendNode()}, {@code insertInOrder()},
 *     {@code insertionSort()}, and {@code interleave()} // IN_PROCESS
 *     </ul>
 * @version 3.1 2025-03-15
 *     <ul>
 *     <li>enhance {@code verifyListState()} to display full state for each testing phase: setup (origin),
 *     expected, actual, verify
 *     <li>enhance and reorder all tests to ensure state is logged before it's tested to aid debugging
 *     <li>clean up temporary comments and commented out code during transition from direct method and
 *     constructor invocation in class under test to utilize framework's {@code Reflection}
 *     </ul>
 * @version 3.2 2025-03-17
 *     <ul>
 *     <li>replace Junit @code{assert()}s with conditional tests which log the problem then throw a
 *     {@code TestingException} with a message which matches the problem logged
 *     <li>add {@code verifyListState()} pass-through
 *     <li>enhance tests to provide additional information in the detailed log
 *     </ul>
 * @version 3.3 2025-03-19 reset {@code Node}'s pseudo-addresses before populating lists to make the
 *     pseudo-addresses match so they are easier to compare
 * @version 3.4 2025-09-30
 *     <ul>
 *     <li>cosmetic changes to logged information
 *     <li>updates to reflect current testing framework
 *     </ul>
 * @version 3.5 2025-11-15 direct error messages to the log prior to throwing an exception
 * @version 3.6 2025-12-10 correct misleading failure messages wrt dataset being sorted
 * @version 4.0 2025-12-27
 *     <ul>
 *     <li>track change from testing framework to analysis framework
 *     <li>track change to Maven
 *     </ul>
 */
@DisplayName( "LList" )
@TestInstance( Lifecycle.PER_CLASS )
@TestMethodOrder( OrderAnnotation.class )
class LListDMRTests extends JUnitTestingBase
    {

    private static final String specifiedTestClassPackageName = "edu.wit.scds.ds.lists" ;
    private static final String specifiedTestClassSimpleName = "LList" ;

    private final static String TEST_DATA_DMR_PATH = "/test-data-dmr/lists/" ;


    /**
     * initialize test framework
     */
    protected LListDMRTests()
        {

        super( specifiedTestClassPackageName, specifiedTestClassSimpleName ) ;

        }   // end no-arg constructor


    /**
     * Test method for {@link edu.wit.scds.ds.lists.LList#LList(java.lang.Comparable[])}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param arrayContentsArgument
     *     contents for initialContents array
     * @param testInfo
     *     info about the test
     *
     * @throws TestingException
     *     any wrapped exceptions which may be thrown by reflection
     * @throws Throwable
     *     anything thrown by the named method
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-array-constructor.data", numLinesToSkip = 1 )
    @DisplayName( "array constructor" )
    @Order( 100200 )
    @Disabled
    void testArrayConstructor( final boolean isLastTest,
                               final boolean isStubBehavior,
                               final String arrayContentsArgument,
                               final TestInfo testInfo ) throws TestingException, Throwable
        {

// TODO revise per sort() IN_PROCESS

        final Object[][] arrayContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initialContents" }, arrayContentsArgument ) ;

        final Comparable<?>[] comparableContents = arrayContentsArgument == null
                ? null  // initialContents array is null
                : // instantiate and populate comparableContents array
                (Comparable[]) copyObjectArrayToSpecificArray( arrayContents[ 0 ] ) ;

        final boolean arrayContentsContainsNull = datasetContains( comparableContents, null ) ;

        // display message describing the expected result of this test
        if ( arrayContentsContainsNull )
            {
            writeLog( "\texpect: IllegalArgumentException: \"new entry cannot be null\"%n" ) ;
            }
        else
            {

            try
                {
                verifyListState( populateList( comparableContents ),
                                 ( comparableContents == null
                                         ? 0
                                         : comparableContents.length ),
                                 "expect" ) ;
                }
            catch ( final TestingException thrown )
                {
                writeLog( "unable to instantiate and populate 'expected' list%n\t%s%n",
                          ( thrown.getMessage() == null
                                  ? ""
                                  : ": \"" + thrown.getMessage() + "\"" ) ) ;

                throw thrown ;   // re-throw it
                }
            catch ( final Throwable thrown )
                {
                writeLog( "unable to instantiate and populate 'expected' list%n\t%s%s%n",
                          thrown.getClass().getSimpleName(),
                          ( thrown.getMessage() == null
                                  ? ""
                                  : ": \"" + thrown.getMessage() + "\"" ) ) ;

                throw thrown ;   // re-throw it
                }   // end try/catch

            }   // end if ( arrayContentsContainsNull )

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {

            // instantiate testList
            if ( arrayContentsContainsNull )
                {

                try
                    {
                    // instantiating the LList is expected to throw an IllegalArgumentException
                    final ListInterface<?> testList
                            = (ListInterface<?>) instantiate( super.testClass, new Class<?>[]
                            { comparableContents == null
                                    ? null
                                    : Comparable[].class }, new Object[]
                                    { comparableContents } ) ;

                    // failed to throw any exception

                    // display message describing the actual result of this test
                    writeLog( "\tactual: IllegalArgumentException wasn't thrown%n" ) ;
                    verifyListState( testList,
                                     comparableContents == null
                                             ? 0
                                             : comparableContents.length,
                                     "actual" ) ;

                    writeLog( "IllegalArgumentException wasn't thrown%n" ) ;

                    throw new TestingException( "IllegalArgumentException wasn't thrown" ) ;
                    }
                catch ( final Throwable thrown )
                    {
                    // display message describing the actual result of this test
                    writeLog( "\tactual: %s%s%n",
                              thrown.getClass().getSimpleName(),
                              thrown.getMessage() == null
                                      ? ""
                                      : ": \"" + thrown.getMessage() + "\"" ) ;

                    // instantiating the LList is expected to throw an IllegalArgumentException
                    if ( thrown.getClass() != IllegalArgumentException.class )
                        {
                        final String failureMessage
                                = String.format( "incorrect exception class, expected %s but caught %s",
                                                 IllegalArgumentException.class.getSimpleName(),
                                                 thrown.getClass().getSimpleName() ) ;

                        writeLog( "%s%n", failureMessage ) ;

                        throw new TestingException( failureMessage ) ;
                        }

                    // verify the message is correct
                    final String expectedMessage = "new entry cannot be null" ;

                    if ( ! expectedMessage.equals( thrown.getMessage() ) )
                        {
                        final String failureMessage
                                = String.format( "incorrect exception message, expected \"%s\" but was \"%s\"",
                                                 expectedMessage,
                                                 thrown.getMessage() ) ;

                        writeLog( "%s%n", failureMessage ) ;

                        throw new TestingException( failureMessage ) ;
                        }

                    }

                }
            else
                {

                // instantiate testList
                try
                    {
                    final ListInterface<?> testList
                            = (ListInterface<?>) instantiate( super.testClass, new Class<?>[]
                            { Comparable[].class }, new Object[] { comparableContents } ) ;


                    // display message describing the actual result of this test
                    verifyListState( testList,
                                     arrayContents[ 0 ] == null
                                             ? 0
                                             : arrayContents[ 0 ].length,
                                     "actual" ) ;


                    // correct entries?
                    final Object[] testListContents = getContentsOfChainBackedCollection( testList ) ;

                    // verify the test list's contents // 2xCk
                    try
                        {
                        compareDatasets( arrayContents[ 0 ] == null
                                ? new Object[ 0 ]
                                : arrayContents[ 0 ], testListContents, IS_ORDERED, COMPARE_IDENTITY ) ;
                        }
                    catch ( final TestingException thrown )
                        {
                        writeLog( "\terror during comparison: %s%n",
                                  thrown.getMessage() == null
                                          ? ""
                                          : ": \"" + thrown.getMessage() + "\"" ) ;

                        throw thrown ;   // re-throw it
                        }
                    catch ( final Throwable thrown )
                        {
                        writeLog( "\tactual: %s%s%n",
                                  thrown.getClass().getSimpleName(),
                                  thrown.getMessage() == null
                                          ? ""
                                          : ": \"" + thrown.getMessage() + "\"" ) ;

                        throw thrown ;   // re-throw it
                        }

                    // verify initialContents array's contents - must be a non-destructive operation

                    // correct entries?
                    compareDatasets( arrayContents[ 0 ], comparableContents, IS_ORDERED, COMPARE_IDENTITY ) ;
                    }
                catch ( final Throwable thrown )
                    {
                    writeLog( "\tactual: %s%s%n",
                              thrown.getClass().getSimpleName(),
                              thrown.getMessage() == null
                                      ? ""
                                      : ": \"" + thrown.getMessage() + "\"" ) ;

                    throw thrown ;   // re-throw it
                    }   // end try/catch

                }

            this.currentTestPassed = true ;
            } ) ;

        }   // end testArrayConstructor()


    /**
     * Test method for {@link edu.wit.scds.ds.lists.LList#clear()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param listContentsArgument
     *     contents to add to the list
     * @param testInfo
     *     info about the test
     *
     * @throws TestingException
     *     any wrapped exceptions which may be thrown by reflection
     * @throws Throwable
     *     anything thrown by the named method
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-clear.data", numLinesToSkip = 1 )
    @DisplayName( "clear()" )
    @Order( 200100 )
    @Disabled
    void testClear( final boolean isLastTest,
                    final boolean isStubBehavior,
                    final String listContentsArgument,
                    final TestInfo testInfo ) throws TestingException, Throwable
        {

// TODO revise per sort()

        final Object[][] listContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents" }, listContentsArgument ) ;

        final ListInterface<?> emptyList ;

        try
            {
            emptyList = (ListInterface<?>) instantiate( super.testClass ) ;
            }
        catch ( final Throwable thrown )
            {
            writeLog( "unable to instantiate empty %s%n\t%s%s%n",
                      super.testClassSimpleName,
                      thrown.getClass().getSimpleName(),
                      thrown.getMessage() == null
                              ? ""
                              : ": \"" + thrown.getMessage() + "\"" ) ;

            throw thrown ;   // re-throw it
            }   // end try/catch

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // instantiate and populate testList
            final Comparable<?>[] comparableContents
                    = (Comparable[]) copyObjectArrayToSpecificArray( listContents[ 0 ] ) ;

            final ListInterface<?> testList = populateList( comparableContents ) ;

            // display message describing the initial list for this test
            verifyListState( testList, comparableContents.length, "initial" ) ;

            // display message describing the expected result of this test
            verifyListState( emptyList, 0, "expect" ) ;

            // clear it
            try
                {
                invoke( super.testClass, testList, "clear" ) ;

                // display message describing the actual result of this test
                verifyListState( testList, 0, "actual" ) ;
                }
            catch ( final Throwable thrown )
                {
                writeLog( "\tactual: %s%s%n",
                          thrown.getClass().getSimpleName(),
                          thrown.getMessage() == null
                                  ? ""
                                  : ": \"" + thrown.getMessage() + "\"" ) ;

                throw thrown ;   // re-throw it
                }   // end try/catch

            this.currentTestPassed = true ;
            } ) ;

        }   // end testClear()


    /**
     * Test method for {@link edu.wit.scds.ds.lists.LList#shuffle()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param listContentsArgument
     *     contents to add to the list
     * @param firstPassContentsArgument
     *     contents of the list after the first invocation of {@code shuffle()}
     * @param secondPassContentsArgument
     *     contents of the list after the second invocation of {@code shuffle()}
     * @param thirdPassContentsArgument
     *     contents of the list after the third invocation of {@code shuffle()}
     * @param fourthPassContentsArgument
     *     contents of the list after the fourth invocation of {@code shuffle()}
     * @param testInfo
     *     info about the test
     *
     * @throws TestingException
     *     any wrapped exceptions which may be thrown by reflection
     * @throws Throwable
     *     anything thrown by the named method
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-shuffle.data", numLinesToSkip = 1 )
    @DisplayName( "shuffle()" )
    @Order( 500300 )
//    @Disabled // DBG
    void testShuffle( final boolean isLastTest,
                      final boolean isStubBehavior,
                      final String listContentsArgument,
                      final String firstPassContentsArgument,
                      final String secondPassContentsArgument,
                      final String thirdPassContentsArgument,
                      final String fourthPassContentsArgument,
                      final TestInfo testInfo ) throws TestingException, Throwable
        {

        final Object[][] listContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents", "after first pass", "after second pass", "after third pass",
                  "after fourth pass" },
                             listContentsArgument,
                             firstPassContentsArgument,
                             secondPassContentsArgument,
                             thirdPassContentsArgument,
                             fourthPassContentsArgument ) ;


        // instantiate and populate initialContents
        final Object[] initialContents = (Object[]) copyObjectArrayToSpecificArray( listContents[ 0 ] ) ;


        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            /*
             * set up moved here to satisfy compiler wrt potentially uninitialized variable: testList
             */

            // instantiate an LList and populate it with its initial contents

            ListInterface<?> testList = null ;

            try
                {
                Node.resetNextPseudoAddress() ;

                testList = populateList( initialContents ) ;

                // describe the initial state for this test
                verifyListState( testList, initialContents.length, "initial" ) ;
                }
            catch ( final Throwable thrown )
                {
                exceptionHandler( thrown, "unable to instantiate and populate 'initial' list" ) ;
                }

            for ( int pass = 1 ; pass < listContents.length ; pass++ )
                {
                writeLog( "pass: %,d%n", pass ) ;
                // i-th pass
                final Object[] expectedContents
                        = (Object[]) copyObjectArrayToSpecificArray( listContents[ pass ] ) ;

                ListInterface<?> expectedList ;

                try
                    {
                    Node.resetNextPseudoAddress() ;

                    expectedList = populateList( expectedContents ) ;

                    // describe the expected result of this test
                    verifyListState( expectedList, expectedContents.length, "expect" ) ;
                    }
                catch ( final Throwable thrown )
                    {
                    exceptionHandler( thrown, "unable to instantiate and populate 'expected' list" ) ;
                    }

                /*
                 * perform the test: shuffle the contents of the test list
                 */

                try
                    {
                    invoke( super.testClass, testList, "shuffle" ) ;

                    // display message describing the actual result of this test
                    verifyListState( testList, expectedContents.length, "actual" ) ;
                    }
                catch ( final Throwable thrown )
                    {
                    exceptionHandler( thrown, "shuffle() failed" ) ;
                    }

                // verify the list's contents
                final Object[] actualContents = getContentsOfChainBackedCollection( testList ) ;

                try
                    {
                    compareDatasets( expectedContents, actualContents, IS_ORDERED, COMPARE_EQUALITY ) ;
                    }
                catch ( final Throwable thrown )
                    {
                    exceptionHandler( thrown, "dataset contents and/or order don't match expected" ) ;
                    }

                }   // end for(pass)

            this.currentTestPassed = true ;
            } ) ;

        }   // end testShuffle()


    /**
     * Test method for {@link edu.wit.scds.ds.lists.LList#sort()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param listContentsArgument
     *     contents to add to the list
     * @param testInfo
     *     info about the test
     *
     * @throws TestingException
     *     any wrapped exceptions which may be thrown by reflection
     * @throws Throwable
     *     anything thrown by the named method
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-sort.data", numLinesToSkip = 1 )
    @DisplayName( "sort()" )
    @Order( 500100 )
//    @Disabled   // DBG
    void testSort( final boolean isLastTest,
                   final boolean isStubBehavior,
                   final String listContentsArgument,
                   final TestInfo testInfo ) throws TestingException, Throwable
        {

        final Object[][] listContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents" }, listContentsArgument ) ;

        // instantiate and populate initialContents
        final Comparable<?>[] initialContents
                = (Comparable[]) copyObjectArrayToSpecificArray( listContents[ 0 ] ) ;


        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            /*
             * set up moved here to satisfy compiler wrt potentially uninitialized variable: testList
             */

            // instantiate an LList and populate it with its initial contents

            ListInterface<?> testList = null ;

            try
                {
                Node.resetNextPseudoAddress() ;

                testList = populateList( initialContents ) ;

                // describe the initial state for this test
                verifyListState( testList, initialContents.length, "initial" ) ;
                }
            catch ( final Throwable thrown )
                {
                exceptionHandler( thrown, "unable to instantiate and populate 'initial' list" ) ;
                }

            // expected results ==> initial contents sorted
            final Comparable<?>[] expectedContents
                    = Arrays.copyOf( initialContents, initialContents.length ) ;
            Arrays.sort( expectedContents ) ;

            ListInterface<?> expectedList ;

            try
                {
                Node.resetNextPseudoAddress() ;

                expectedList = populateList( expectedContents ) ;

                // describe the expected result of this test
                verifyListState( expectedList, expectedContents.length, "expect" ) ;
                }
            catch ( final Throwable thrown )
                {
                exceptionHandler( thrown, "unable to instantiate and populate 'expected' list" ) ;
                }

            /*
             * perform the test: sort the contents of the test list
             */

            try
                {
                invoke( super.testClass, testList, "sort" ) ;

                // display message describing the actual result of this test
                verifyListState( testList, expectedContents.length, "actual" ) ;
                }
            catch ( final Throwable thrown )
                {
                exceptionHandler( thrown, "sort() failed" ) ;
                }

            // verify the list's contents
            final Object[] actualContents = getContentsOfChainBackedCollection( testList ) ;

            try
                {
                compareDatasets( expectedContents, actualContents, IS_ORDERED, COMPARE_EQUALITY ) ;   // not
                                                                                                      // testing
                                                                                                      // for
                                                                                                      // stable
                }
            catch ( final Throwable thrown )
                {
                exceptionHandler( thrown, "dataset contents and/or order don't match expected" ) ;
                }

            // this operation must be repeatable - do it again to make sure...

            try
                {
                invoke( super.testClass, testList, "sort" ) ;

                // display message describing the actual result of this test
                verifyListState( testList, expectedContents.length, "verify" ) ;
                }
            catch ( final Throwable thrown )
                {
                exceptionHandler( thrown, "re-sort() failed" ) ;
                }

            // verify the list's contents
            final Object[] verifyContents = getContentsOfChainBackedCollection( testList ) ;

            try
                {
                compareDatasets( expectedContents, verifyContents, IS_ORDERED, COMPARE_EQUALITY ) ;   // not
                                                                                                      // testing
                                                                                                      // for
                                                                                                      // stable
                }
            catch ( final Throwable thrown )
                {
                exceptionHandler( thrown, "dataset contents and/or order don't match expected" ) ;
                }

            this.currentTestPassed = true ;
            } ) ;

        }   // end testSort()


    /**
     * Test method for stability of {@link edu.wit.scds.ds.lists.LList#sort()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param orderFrontToBackArgument
     *     flag to control order to add elements to list:<br>
     *     true: add at end<br>
     *     false: add at beginning
     * @param listContentsArgument
     *     contents to add to the list
     * @param testInfo
     *     info about the test
     *
     * @throws TestingException
     *     any wrapped exceptions which may be thrown by reflection
     * @throws Throwable
     *     anything thrown by the named method
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-sort-stability.data", numLinesToSkip = 1 )
    @DisplayName( "sort() stability" )
    @Order( 500200 )
//    @Disabled   // DBG
    void testSortStability( final boolean isLastTest,
                            final boolean isStubBehavior,
                            final String orderFrontToBackArgument,
                            final String listContentsArgument,
                            final TestInfo testInfo ) throws TestingException, Throwable
        {

        final Object[][] listContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "add first..last", "initial values" }, orderFrontToBackArgument, listContentsArgument ) ;

        // instantiate and populate initialContents
        final Paired[] initialContents = new Paired[ listContents[ 1 ].length ] ;

        final boolean orderFirstToLast = (boolean) listContents[ 0 ][ 0 ] ;

        for ( int i = 0 ; i < listContents[ 1 ].length ; i++ )
            {
            // reverse order of initialContents if necessary
            final int storeAtI = orderFirstToLast
                    ? i
                    : listContents[ 1 ].length - 1 - i ;

            initialContents[ storeAtI ] = new Paired( (int) (long) listContents[ 1 ][ i ] ) ;
            }

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            /*
             * set up moved here to satisfy compiler wrt potentially uninitialized variable: testList
             */

            // instantiate an LList and populate it with its initial contents

            ListInterface<?> testList = null ;

            try
                {
                Node.resetNextPseudoAddress() ;

                testList = populateList( initialContents ) ;

                // describe the initial state for this test
                verifyListState( testList, initialContents.length, "initial" ) ;
                }
            catch ( final Throwable thrown )
                {
                exceptionHandler( thrown, "unable to instantiate and populate 'initial' list" ) ;
                }

            // expected results ==> initial contents (stable) sorted
            final Paired[] expectedContents = Arrays.copyOf( initialContents, initialContents.length ) ;
            Arrays.sort( expectedContents ) ;

            ListInterface<?> expectedList ;

            try
                {
                Node.resetNextPseudoAddress() ;

                expectedList = populateList( expectedContents ) ;

                // describe the expected result of this test
                verifyListState( expectedList, expectedContents.length, "expect" ) ;
                }
            catch ( final Throwable thrown )
                {
                exceptionHandler( thrown, "unable to instantiate and populate 'expected' list" ) ;
                }

            /*
             * perform the test: sort the contents of the test list
             */

            try
                {
                invoke( super.testClass, testList, "sort" ) ;

                // display message describing the actual result of this test
                verifyListState( testList, expectedContents.length, "actual" ) ;
                }
            catch ( final Throwable thrown )
                {
                exceptionHandler( thrown, "sort() failed" ) ;
                }

            // verify the list's contents
            final Object[] actualContents = getContentsOfChainBackedCollection( testList ) ;

            try
                {
                compareDatasets( expectedContents, actualContents, IS_ORDERED, COMPARE_IDENTITY ) ;   // required
                                                                                                      // for
                                                                                                      // stable
                                                                                                      // sort
                }
            catch ( final Throwable thrown )
                {
                exceptionHandler( thrown, "dataset contents and/or order don't match expected" ) ;
                }

            // this operation must be repeatable - do it again to make sure...

            try
                {
                invoke( super.testClass, testList, "sort" ) ;

                // display message describing the actual result of this test
                verifyListState( testList, expectedContents.length, "verify" ) ;
                }
            catch ( final Throwable thrown )
                {
                exceptionHandler( thrown, "re-sort() failed" ) ;
                }

            // verify the list's contents
            final Object[] verifyContents = getContentsOfChainBackedCollection( testList ) ;

            try
                {
                compareDatasets( expectedContents, verifyContents, IS_ORDERED, COMPARE_IDENTITY ) ;   // required
                                                                                                      // for
                                                                                                      // stable
                                                                                                      // sort
                }
            catch ( final Throwable thrown )
                {
                exceptionHandler( thrown, "dataset contents and/or order don't match expected" ) ;
                }

            this.currentTestPassed = true ;
            } ) ;

        }   // end testSortStability()


    // -------------------------------------------------- The following utilities are used by the test methods
    // --------------------------------------------------


    /**
     * utility to consolidate exception handling and reporting
     *
     * @param thrown
     *     the exception (or other throwable)
     * @param message
     *     description of the activity during which the exception occurred
     *
     * @throws Throwable
     *     we re-throw whatever we're given
     *
     * @since 3.2
     */
    private void exceptionHandler( final Throwable thrown,
                                   final String message ) throws Throwable
        {

        exceptionHandler( thrown, message, null, null, null ) ;

        }   // end exceptionHandler() pass-through


    /**
     * utility to consolidate exception handling and reporting
     *
     * @param thrown
     *     the exception (or other throwable)
     * @param message
     *     description of the activity during which the exception occurred
     * @param stage
     *     text to be passed to {@code verifyListState()} if {@code referenceObject} is an LList
     * @param referenceObjectDescription
     *     optional, if non-null, a description the {@code referenceObject}
     * @param referenceObject
     *     optional, if non-null, an object to include in the detailed log
     *
     * @throws Throwable
     *     we re-throw whatever we're given
     *
     * @since 3.2
     */
    private void exceptionHandler( final Throwable thrown,
                                   final String message,
                                   final String stage,
                                   final String referenceObjectDescription,
                                   final Object referenceObject ) throws Throwable
        {
        /*
         * @param testList the {@code LList} instance to test
         * @param expectedNumberOfEntries the number of entries that should be in the list
         * @param stageLabel description of the testing phase, typically one of "initial", "expect", "actual",
         * or "verify" // FUTURE switch to enum?
         * @param errorMessagePrefix text added to the beginning of error message generated or detected
         */

// FUTURE dump list state to the detailed log file

//        verifyListState( stage, referenceObject, expectedElementCount, referenceObjectMessage  ) ;


        // FUTURE handle chained exceptions (cause(s))

        final String referenceObjectMessage = String.format( "%s%s%s",
                                                             ( referenceObjectDescription == null
                                                                     ? ""
                                                                     : String.format( "%s: ",
                                                                                      referenceObjectDescription ) ),
                                                             ( ( referenceObjectDescription == null )
                                                               || ( referenceObject == null )
                                                                       ? ""
                                                                       : ": " ),
                                                             ( referenceObject == null
                                                                     ? ""
                                                                     : String.format( "%s%n",
                                                                                      referenceObject ) ) ) ;

        if ( thrown.getClass() == TestingException.class )
            {
            // one of ours - usually wrapping another exception

            writeLog( "%s: %s%n%s",
                      message,
                      ( thrown.getMessage() == null
                              ? ""
                              : "\"" + thrown.getMessage() + "\"" ),
                      referenceObjectMessage ) ;
            }
        else
            {
            // anything else that can be thrown

            writeLog( "%s: %s%s%n%s",
                      message,
                      thrown.getClass().getSimpleName(),
                      ( thrown.getMessage() == null
                              ? ""
                              : ": \"" + thrown.getMessage() + "\"" ),
                      referenceObjectMessage ) ;
            }

        throw thrown ;   // re-throw it

        }   // end exceptionHandler()


//    /**
//     * Utility to populate a list from the contents of an array
//     *
//     * @param listToFill
//     *     the list to populate
//     * @param entries
//     *     the entries to add to the listToFill
//     */
//    @SuppressWarnings( "unused" )
//    private static <T extends Comparable<? super T>> void populateList(
//                                       final ListInterface<T> listToFill,
//                                       final T[] entries )
//        {
//        // FUTURE enhance with manual fill option/flag a la populateBag()
//        if ( entries != null )
//            {
//            for ( final T anEntry : entries )
//                {
//                listToFill.add( anEntry ) ;
//                }
//            }
//
//        }   // end populateList()


    /**
     * Utility to instantiate and populate a list from the contents of an array
     *
     * @param entries
     *     the entries to add to the new list
     *
     * @return an instantiated and populated list containing the provided entries
     *
     * @throws TestingException
     *     any wrapped exceptions which may be thrown by reflection
     * @throws Throwable
     *     anything thrown by {@code add()}
     */
    private ListInterface<?> populateList( final Object[] entries ) throws TestingException, Throwable
        {

        ListInterface<?> newList = null ;

        // instantiate an empty LList
        try
            {
            newList = (ListInterface<?>) instantiate( super.testClass ) ;
            }
        catch ( final TestingException thrown )
            {
            writeLog( "unable to instantiate empty %s: %s%n",
                      super.testClassSimpleName,
                      ( thrown.getMessage() == null
                              ? ""
                              : "\"" + thrown.getMessage() + "\"" ) ) ;

            throw thrown ;   // re-throw it
            }
        catch ( final Throwable thrown )
            {
            writeLog( "unable to instantiate empty %s: %s%s%n",
                      super.testClassSimpleName,
                      thrown.getClass().getSimpleName(),
                      ( thrown.getMessage() == null
                              ? ""
                              : ": \"" + thrown.getMessage() + "\"" ) ) ;

            throw thrown ;   // re-throw it
            }   // end try/catch

        // populate the LList
        if ( ( entries != null ) && ( entries.length > 0 ) )
            {

            try
                {
                final Class<?> firstEntryType = entries[ 0 ] instanceof Comparable
                        ? Comparable.class
                        : Object.class ;

                for ( final Object entry : entries )
                    {
                    // FUTURE support direct additions in addition to using the list's add() add each entry to
                    // the end of the list
                    invoke( super.testClass, newList, "add", new Class<?>[] { firstEntryType }, entry ) ;
                    }

                }
            catch ( final TestingException thrown )
                {
                writeLog( "unable to populate newly instantiated %s: %s%n",
                          super.testClassSimpleName,
                          ( thrown.getMessage() == null
                                  ? ""
                                  : "\"" + thrown.getMessage() + "\"" ) ) ;

                throw thrown ;   // re-throw it
                }
            catch ( final Throwable thrown )
                {
                writeLog( "unable to populate newly instantiated %s: %s%s%n",
                          super.testClassSimpleName,
                          thrown.getClass().getSimpleName(),
                          ( thrown.getMessage() == null
                                  ? ""
                                  : ": \"" + thrown.getMessage() + "\"" ) ) ;

                throw thrown ;   // re-throw it
                }   // end try/catch

            }

        return newList ;

        }   // end populateList()


    /**
     * Create and populate an array of a specific type/class from an {@code Object[]}
     *
     * @param objectArray
     *     the {@code Object[]} containing the elements to copy into the specific type array
     *
     * @return an array of the type of element of the first element in {@code objectArray}<br>
     *     if the parameter array is empty or the first element is {@code null}, the returned array is of type
     *     {@code String[]}<br>
     *     if the parameter is {@code null} returns {@code null}
     */
    private static Object copyObjectArrayToSpecificArray( final Object[] objectArray )
        {

        if ( objectArray == null )
            {
            return null ;
            }

        // instantiate an array of String references (if no entries) or an array of references of the class of
        // the first element of objectArray
        final Object specificArray
                = Array.newInstance( ( objectArray.length == 0 ) || ( objectArray[ 0 ] == null )
                        ? String.class
                        // 2xCk is this the best choice for default type?
                        : objectArray[ 0 ].getClass(), objectArray.length ) ;

        // copy each element from objectArray to specificArray
        System.arraycopy( objectArray, 0, specificArray, 0, objectArray.length ) ;

        return specificArray ;

        }   // end copyObjectArrayToSpecificArray()


    /**
     * Test the provided LList instance to ensure it's in a valid state:
     * <ul>
     * <li>numberOfEntries is non-negative and matches expected value
     * <li>the chain is the correct length
     * <li>none of the data references is null
     * <li>lastNode points to the correct Node
     * </ul>
     * Note: {@code testList}'s contents are displayed but not verified Note: pass-through method: empty error
     * message prefix is supplied
     *
     * @param testList
     *     the {@code LList} instance to test
     * @param expectedNumberOfEntries
     *     the number of entries that should be in the list
     * @param stageLabel
     *     description of the testing phase, typically one of "initial", "expect", "actual", or "verify" //
     *     FUTURE switch to enum?
     *
     * @throws TestingException
     *     any wrapped exceptions which may be thrown by reflection
     * @throws Throwable
     *     anything thrown by the named method
     *
     * @since 3.2
     */
    private void verifyListState( final ListInterface<?> testList,
                                  final int expectedNumberOfEntries,
                                  final String stageLabel ) throws TestingException, Throwable
        {

        verifyListState( testList, expectedNumberOfEntries, stageLabel, "" ) ;

        }   // end 3-arg pass-through verifyListState()


    /**
     * Test the provided LList instance to ensure it's in a valid state:
     * <ul>
     * <li>numberOfEntries is non-negative and matches expected value
     * <li>the chain is the correct length
     * <li>none of the data references is null
     * <li>lastNode points to the correct Node
     * </ul>
     * Note: {@code testList}'s contents are displayed but not verified
     *
     * @param testList
     *     the {@code LList} instance to test
     * @param expectedNumberOfEntries
     *     the number of entries that should be in the list
     * @param stageLabel
     *     description of the testing phase, typically one of "initial", "expect", "actual", or "verify" //
     *     FUTURE switch to enum?
     * @param errorMessagePrefix
     *     text added to the beginning of error message generated or detected
     *
     * @throws TestingException
     *     any wrapped exceptions which may be thrown by reflection
     * @throws Throwable
     *     anything thrown by the named method
     */
    private void verifyListState( final ListInterface<?> testList,
                                  final int expectedNumberOfEntries,
                                  final String stageLabel,
                                  final String errorMessagePrefix ) throws TestingException, Throwable
        {

        // retrieve the pieces

        final int numberOfEntries = getIntField( testList, "numberOfEntries" ) ;
        final Node<?> firstNode = (Node<?>) getReferenceField( testList, "firstNode" ) ;
        final Node<?> lastNode = (Node<?>) getReferenceField( testList, "lastNode" ) ;

        // display message describing the expected result of this test
        writeLog( """
                  %s: %s: %s

                          %s state:
                              %s: %,d
                              %s: %s
                              %s: %s
                              %s: %s

                  """,
                  stageLabel,
                  "data",
                  datasetToString( getContentsOfChainBackedCollection( testList ) ),
                  testList.getClass().getSimpleName(),
                  "numberOfEntries",
                  numberOfEntries,
                  "firstNode",
                  ( firstNode == null
                          ? VisualizationSupport.NULL_REFERENCE_MARKER
                          : firstNode ),
                  "lastNode",
                  ( lastNode == null
                          ? VisualizationSupport.NULL_REFERENCE_MARKER
                          : lastNode ),
                  "contents",
                  Node.chainToString( firstNode ) ) ;

        if ( testList.getClass() != super.testClass )   // this test rejects subclasses of LList
        // keep as is until verification our Reflection.java properly handles inheritance if it does, probably
        // switch to instanceof
            {
            final String failureMessage = String.format( "%sExpected argument of type %s but given %s",
                                                         errorMessagePrefix,
                                                         super.testClassSimpleName,
                                                         testList.getClass().getSimpleName() ) ;

            writeLog( "%s%n", failureMessage ) ;

            throw new TestingException( failureMessage ) ;
            }

        if ( expectedNumberOfEntries != numberOfEntries )
            {
            final String failureMessage
                    = String.format( "%sIncorrect numberOfEntries, is %,d but should be %,d",
                                     errorMessagePrefix,
                                     numberOfEntries,
                                     expectedNumberOfEntries ) ;

            writeLog( "%s%n", failureMessage ) ;

            throw new TestingException( failureMessage ) ;
            }

        if ( expectedNumberOfEntries == 0 )
            {
            // list is empty

            if ( firstNode != null )
                {
                final String failureMessage
                        = String.format( "%sfirstNode should be null but is referencing %s",
                                         errorMessagePrefix,
                                         firstNode ) ;

                writeLog( "%s%n", failureMessage ) ;

                throw new TestingException( failureMessage ) ;
                }

            if ( lastNode != null )
                {
                final String failureMessage
                        = String.format( "%slastNode should be null but is referencing %s",
                                         errorMessagePrefix,
                                         lastNode ) ;

                writeLog( "%s%n", failureMessage ) ;

                throw new TestingException( failureMessage ) ;
                }

            }   // end list is empty
        else
            {
            // list is non-empty

            if ( firstNode == null )
                {
                final String failureMessage
                        = String.format( "%sfirstNode should not be null", errorMessagePrefix ) ;

                writeLog( "%s%n", failureMessage ) ;

                throw new TestingException( failureMessage ) ;
                }

            if ( lastNode == null )
                {
                final String failureMessage
                        = String.format( "%slastNode should not be null", errorMessagePrefix ) ;

                writeLog( "%s%n", failureMessage ) ;

                throw new TestingException( failureMessage ) ;
                }

            int nodeCount = 0 ;
            Node<?> currentNode = firstNode ;

            // count the number of Nodes
            while ( currentNode != null )
                {
                nodeCount++ ;

                if ( currentNode.getData() == null )
                    {
                    final String failureMessage = String.format( "%sfound null data reference in node %s",
                                                                 errorMessagePrefix,
                                                                 currentNode ) ;

                    writeLog( "%s%n", failureMessage ) ;

                    throw new TestingException( failureMessage ) ;
                    }

                if ( ( nodeCount == expectedNumberOfEntries ) && ( currentNode != lastNode ) )
                    {
                    final String failureMessage
                            = String.format( "%slastNode is referencing the wrong node: %s",
                                             errorMessagePrefix,
                                             lastNode ) ;

                    writeLog( "%s%n", failureMessage ) ;

                    throw new TestingException( failureMessage ) ;
                    }

                if ( nodeCount > expectedNumberOfEntries )
                    {
                    final String failureMessage
                            = String.format( "%sthere are too many Nodes or a loop in the chain, detected at node: %s",
                                             errorMessagePrefix,
                                             currentNode ) ;

                    writeLog( "%s%n", failureMessage ) ;

                    throw new TestingException( failureMessage ) ;
                    }

                currentNode = currentNode.getNext() ;
                }   // end traversing the chain

            if ( expectedNumberOfEntries != nodeCount )
                {
                final String failureMessage
                        = String.format( "%sIncorrect number of Nodes, is %,d but should be %,d",
                                         errorMessagePrefix,
                                         nodeCount,
                                         expectedNumberOfEntries ) ;

                writeLog( "%s%n", failureMessage ) ;

                throw new TestingException( failureMessage ) ;
                }

            }   // end list is non-empty

        }   // end verifyListState()

    }   // end class LListDMRTests
