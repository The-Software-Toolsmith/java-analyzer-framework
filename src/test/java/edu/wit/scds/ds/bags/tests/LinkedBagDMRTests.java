/* @formatter:off
 *
 * © David M Rosenberg
 *
 * Topic: Bags
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
 * sections of this course
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


package edu.wit.scds.ds.bags.tests ;

import static org.junit.jupiter.api.Assertions.assertEquals ;
import static org.junit.jupiter.api.Assertions.assertFalse ;
import static org.junit.jupiter.api.Assertions.assertNotEquals ;
import static org.junit.jupiter.api.Assertions.assertNull ;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively ;
import static org.junit.jupiter.api.Assertions.assertTrue ;

import education.the_software_toolsmith.analyzer.framework.dynamicanalysis.JUnitTestingBase ;
import education.the_software_toolsmith.analyzer.framework.dynamicanalysis.PlaceholderException ;

import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectBackingStores.getContentsOfArrayBackedDataset ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectBackingStores.getContentsOfChainBackedDataset ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectDataFields.getIntField ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectDataFields.getReferenceField ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectDataFields.setIntField ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectDataFields.setReferenceField ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectMethods.invoke ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectReferenceTypes.instantiate ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.TestData.IS_ORDERED ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.TestData.IS_UNORDERED ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.TestData.compareDatasets ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.TestData.countOccurrences ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.TestData.datasetContains ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.TestData.datasetToString ;

import edu.wit.scds.ds.bags.ArrayBag ;
import edu.wit.scds.ds.bags.BagInterface ;
import edu.wit.scds.ds.bags.LinkedBag ;
import edu.wit.scds.ds.common.chains.enhanced.Node ;

import org.junit.jupiter.api.Disabled ;
import org.junit.jupiter.api.DisplayName ;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation ;
import org.junit.jupiter.api.Order ;
import org.junit.jupiter.api.Test ;
import org.junit.jupiter.api.TestInfo ;
import org.junit.jupiter.api.TestMethodOrder ;
import org.junit.jupiter.params.ParameterizedTest ;
import org.junit.jupiter.params.provider.CsvFileSource ;

import java.util.Arrays ;

/**
 * JUnit tests for the LinkedBag class. All methods are tested. These tests require the API for the
 * {@code LList} class implement {@code BagInterface<T>}.
 *
 * @author David M Rosenberg
 *
 * @version 1.0 2018-05-25 initial set of tests<br>
 * @version 1.1 2018-06-09 revise structure to use TestInfo instead of certain hard-coded text
 * @version 1.2 2018-09-02 add timeouts
 * @version 1.3 2019-01-14 more implementation
 * @version 1.3.1 2019-01-17 cosmetic changes
 * @version 2.0 2019-05-12
 *     <ul>
 *     <li>restructure tests
 *     <li>disable System.exit() during testing
 *     <li>start making each subtest independent so they'll all run even if one fails
 *     </ul>
 * @version 2.1 2019-05-17
 *     <ul>
 *     <li>rename class
 *     <li>remove unnecessary throws clauses from @BeforeXxx and @AfterXxx
 *     <li>more fully utilize JUnit 5.4 features
 *     <li>switch tests to data-driven
 *     </ul>
 * @version 3.0 2019-06-27
 *     <ul>
 *     <li>complete re-write with reusable testing infrastructure
 *     <li>tests are now data-driven
 *     <li>add summary test results
 *     </ul>
 * @version 3.1 2019-06-28 move detailed activity to log file
 * @version 4.0 2019-07-04 split general purpose utilities methods into separate class
 * @version 4.1 2020-01-28
 *     <ul>
 *     <li>test additional methods (toString, array constructor, cloning constructor with non-LinkedBag
 *     argument)
 *     <li>reformat per class standard
 *     </ul>
 * @version 4.2 2020-02-09 make some tests stricter wrt source entry ordering (must be unchanged)
 * @version 4.3 2020-05-15
 *     <ul>
 *     <li>implement testToString()
 *     <li>add/enhance null argument test capabilities
 *     <li>display Exceptions as actual/verification results
 *     <li>verify that all non-destructive/non-modifying methods are repeatable
 *     </ul>
 * @version 4.4 2020-08-07 revisions to interrogate instance variables via testing infrastructure
 * @version 4.5 2020-08-08 track changes to BagInterface - switch to EnhancedUnorderedListInterface and
 *     LinkedBag
 * @version 4.6 2020-09-13
 *     <ul>
 *     <li>track changes to BagInterface - switch back to it
 *     <li>add non-LinkedBag tests of {@code difference()}, {@code intersection()}, and {@code union()}
 *     <li>test 'standard' private methods: {@code getReferenceTo()} and {@code initializeState()}
 *     <li>in {@code populateBag()}, empty the bag before (re-)populating it
 *     </ul>
 * @version 4.7 2020-09-18
 *     <ul>
 *     <li>revise some tests to minimize need for multiple 'other' LinkedBag methods to be implemented
 *     <li>ensure all non-LinkedBag tests correctly instantiate a {@code ArrayBag}
 *     <li>replace {@code catch(Exception)} with {@code catch(Throwable)} to provide more robust error
 *     reporting
 *     </ul>
 * @version 4.8 2021-06-21 (partial) updates to track changes to testing framework
 * @version 4.9 2022-10-07
 *     <ul>
 *     <li>disable tests for provided code
 *     <li>enhance test of {@code clear()} to detect use of {@code initializeState()}
 *     <li>finish switching instantiation of all bags to use Reflection utilities' {@code instantiate()}
 *     </ul>
 * @version 4.10 2024-10-19
 *     <ul>
 *     <li>minor fixes wrt test ordering
 *     <li>TODO
 *     <ul>
 *     <li>finish implementation of test of {@code add(T[])}
 *     <li>add tests for new methods: {@code isDisjointSet()}, {@code isProperSubsetOf()},
 *     {@code isSubsetOf()}
 *     <li>revise all tests to display setup/initial state, expected result and state, actual result and
 *     state, and verified state
 *     </ul>
 *     </ul>
 * @version 5.0 2025-01-28
 *     <ul>
 *     <li>track changes to switch back to use 'bag' instead of 'list' or 'unordered list'
 *     <li>continue incremental switch to reflection methods over explicit class.method() to support partial
 *     implementations
 *     <li>finished implementation of test of {@code add(T[])}
 *     </ul>
 * @version 6.0 2025-12-27
 *     <ul>
 *     <li>track change from testing framework to analysis framework
 *     <li>track change to Maven
 *     </ul>
 */
@DisplayName( "LinkedBag" )
@TestMethodOrder( OrderAnnotation.class )
class LinkedBagDMRTests extends JUnitTestingBase
    {
    /*
     * utility constants
     */

    private final static String TEST_PACKAGE_NAME = "edu.wit.scds.ds.bags" ;
    private final static String TEST_CLASS_NAME = "LinkedBag" ;

    private final static String TEST_DATA_DMR_PATH = "/test-data-dmr/bags/" ;


    /**
     * constructor
     */
    protected LinkedBagDMRTests()
        {

        super( TEST_PACKAGE_NAME, TEST_CLASS_NAME ) ;

        }   // end constructor


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#LinkedBag()}.
     *
     * @param testInfo
     *     info about the test
     */
    @Test
    @DisplayName( "no-arg constructor" )
    @Order( 100100 )
    @Disabled
    void testNoArgConstructor( final TestInfo testInfo )
        {

        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // count this test
            this.currentTestsAttempted++ ;

            // display message describing this test
            writeLog( "[%,d, %,d] Testing: new LinkedBag()%n%n" + "expect: empty bag%n%n",
                      this.currentTestGroup,
                      this.currentTestsAttempted ) ;

            // instantiate testBag
//            final BagInterface<Object> testBag ;  // IN_PROCESS
            Object testBag ;

            try
                {
//                testBag = new LinkedBag<>() ;     // IN_PROCESS
                testBag = instantiate( super.testClass ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // empty?
            assertEquals( 0, getIntField( testBag, "numberOfEntries" ) ) ;
            assertNull( getReferenceField( testBag, "firstNode" ) ) ;

            writeLog( "actual: empty bag%n%n" ) ;

            this.currentTestPassed = true ;
            } ) ;

        }    // end testNoArgConstructor()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#LinkedBag(java.lang.Object[])}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param arrayContentsArgument
     *     contents for initialContents array
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-array-constructor.data", numLinesToSkip = 1 )
    @DisplayName( "array constructor" )
    @Order( 100200 )
    void testArrayConstructor( final boolean isLastTest,
                               final boolean isStubBehavior,
                               final String arrayContentsArgument,
                               final TestInfo testInfo )
        {

        final Object[][] arrayContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initialContents" }, arrayContentsArgument ) ;

        final Object[] initialContents = arrayContentsArgument == null
                ? null  // initialContents array is null
                : // instantiate and populate initialContents array
                Arrays.copyOf( arrayContents[ 0 ], arrayContents[ 0 ].length ) ;

        final boolean arrayContentsContainsNull = datasetContains( initialContents, null ) ;

        // display message describing the expected result of this test
        writeLog( "expect:%n\t\tnumberOfEntries: %,d%n\t\tdata: %s%n%n",
                  arrayContents[ 0 ] == null
                          ? 0
                          : arrayContents[ 0 ].length,
                  arrayContents[ 0 ] == null
                          ? "[]"
                          : arrayContentsContainsNull
                                  ? IllegalArgumentException.class.getSimpleName()
                                    + ": \"entry cannot be null\""
                                  : datasetToString( arrayContents[ 0 ] ) ) ;


        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            Object testBag ;    // IN_PROCESS

            // instantiate testBag
            if ( arrayContentsContainsNull )
                {
                Throwable thrownCondition = new PlaceholderException( new IllegalArgumentException() ) ;

                try
                    {
//                    new LinkedBag<>( initialContents ) ;  // IN_PROCESS
                    testBag = instantiate( super.testClass,
                                           new Class<?>[]
                                           { Object[].class },
                                           new Object[]
                                           { initialContents } ) ;

                    // display message describing the actual result of this test
                    writeLog( "actual: no Exception thrown%n%n" ) ;
                    }
                catch ( final Throwable e )
                    {
                    thrownCondition = e ;

                    // display message describing the actual result of this test
                    writeLog( "actual: %s%s%n%n",
                              e.getClass().getSimpleName(),
                              e.getMessage() == null
                                      ? ""
                                      : ": \"" + e.getMessage() + "\"" ) ;
                    }

                assertEquals( IllegalArgumentException.class, thrownCondition.getClass() ) ;
                }
            else
                {
                // instantiate testBag
//                final BagInterface<Object> testBag ;  // IN_PROCESS

                try
                    {
//                    testBag = new LinkedBag<>( initialContents ) ;  // IN_PROCESS
                    testBag = instantiate( super.testClass,
                                           new Class<?>[]
                                           { Object[].class },
                                           new Object[]
                                           { initialContents } ) ;
                    }
                catch ( final Throwable e )
                    {
                    writeLog( "actual: %s%s%n%n",
                              e.getClass().getSimpleName(),
                              e.getMessage() == null
                                      ? ""
                                      : ": " + e.getMessage() ) ;

                    throw e ;   // re-throw it
                    }

                // actual state
                final int actualNumberOfEntries = getIntField( testBag, "numberOfEntries" ) ;
                final Object[] actualTestBagContents = getContentsOfChainBackedDataset( testBag,
                                                                                        "firstNode",
                                                                                        "numberOfEntries",
                                                                                        actualNumberOfEntries,
                                                                                        "data",
                                                                                        "next" ) ;

                // display message describing the actual result of this test
                writeLog( "actual:%n\t\tnumberOfEntries: %,d%n\t\tdata: %s%n%n",
                          actualNumberOfEntries,
                          datasetToString( actualTestBagContents ) ) ;

                // empty?
                if ( arrayContents[ 0 ] == null )
                    {
                    assertEquals( 0, getIntField( testBag, "numberOfEntries" ) ) ;
                    }
                else
                    {
                    assertEquals( arrayContents[ 0 ].length == 0,
                                  getIntField( testBag, "numberOfEntries" ) == 0 ) ;
                    }

                // correct number of entries?
                assertEquals( arrayContents[ 0 ] == null
                        ? 0
                        : arrayContents[ 0 ].length, getIntField( testBag, "numberOfEntries" ) ) ;

                // verify the test bag's contents
                compareDatasets( arrayContents[ 0 ] == null
                        ? new Object[ 0 ]
                        : arrayContents[ 0 ], actualTestBagContents, IS_UNORDERED ) ;

                // verify initialContents array's contents - must be a non-destructive operation

                if ( initialContents != null )
                    {
                    // display message describing the actual result of this test
                    writeLog( "verify:%n\t\tinitialContents: %s%n%n", datasetToString( initialContents ) ) ;

                    // correct entries?
                    compareDatasets( arrayContents[ 0 ], initialContents, IS_ORDERED ) ;
                    }

                }

            this.currentTestPassed = true ;
            } ) ;

        }   // end testArrayConstructor()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#LinkedBag(edu.wit.scds.ds.bags.BagInterface)}
     * using a {@code LinkedBag} instance as the {@code sourceBag} argument.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param anotherBagContentsArgument
     *     contents for anotherBag
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-cloning-constructor.data", numLinesToSkip = 1 )
    @DisplayName( "cloning constructor with LinkedBag" )
    @Order( 100300 )
    void testCloningConstructorLinkedBag( final boolean isLastTest,
                                          final boolean isStubBehavior,
                                          final String anotherBagContentsArgument,
                                          final TestInfo testInfo )
        {

        final Object[][] anotherBagContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "sourceBag" }, anotherBagContentsArgument ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // display message describing the expected result of this test
            writeLog( "expect:%n\tnew instance%n\t\tnumberOfEntries: %,d%n\t\tdata: %s%n%n",
                      anotherBagContents[ 0 ] == null
                              ? 0
                              : anotherBagContents[ 0 ].length,
                      anotherBagContents[ 0 ] == null
                              ? "[]"
                              : datasetToString( anotherBagContents[ 0 ] ) ) ;

            BagInterface<Object> anotherBag = null ;

            if ( anotherBagContentsArgument == null )
                {
                // anotherBag is null
                }
            else
                {
                // instantiate and populate anotherBag
                anotherBag = new LinkedBag<>() ;    // IN_PROCESS
//                anotherBag = (BagInterface<Object>) instantiate( super.testClass ) ;

                populateBag( anotherBag, anotherBagContents[ 0 ], BYPASS_ADD ) ;
                }

            // instantiate testBag
            final BagInterface<Object> testBag ;

            try
                {
                testBag = new LinkedBag<>( anotherBag ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            final Object[] testBagContents = getContentsOfChainBackedDataset( testBag,
                                                                              "firstNode",
                                                                              "numberOfEntries",
                                                                              -1,
                                                                              "data",
                                                                              "next" ) ;
            writeLog( "actual: %s%n%n", datasetToString( testBagContents ) ) ;

            // empty?
            if ( anotherBagContents[ 0 ] == null )
                {
                assertEquals( 0, getIntField( testBag, "numberOfEntries" ) ) ;
                }
            else
                {
                assertEquals( anotherBagContents[ 0 ].length == 0,
                              getIntField( testBag, "numberOfEntries" ) == 0 ) ;
                }

            // correct number of entries?
            assertEquals( anotherBag == null
                    ? 0
                    : getIntField( anotherBag, "numberOfEntries" ),
                          getIntField( testBag, "numberOfEntries" ) ) ;

            // verify the test bag's contents
            if ( anotherBag == null )
                {
                assertEquals( 0, getIntField( testBag, "numberOfEntries" ) ) ;
                }
            else
                {
                compareDatasets( anotherBagContents[ 0 ], testBagContents, IS_UNORDERED ) ;

                // verify another bag's contents - must be a non-destructive operation

                // correct entries?
                final Object[] retrievedAnotherBagContents
                        = getContentsOfChainBackedDataset( anotherBag,
                                                           "firstNode",
                                                           "numberOfEntries",
                                                           -1,
                                                           "data",
                                                           "next" ) ;

                compareDatasets( anotherBagContents[ 0 ], retrievedAnotherBagContents, IS_UNORDERED ) ;
                }

            // verify another bag's contents - must be a non-destructive operation

            if ( anotherBag != null )
                {
                // correct entries?
                final Object[] retrievedAnotherBagContents
                        = getContentsOfChainBackedDataset( testBag,
                                                           "firstNode",
                                                           "numberOfEntries",
                                                           -1,
                                                           "data",
                                                           "next" ) ;

                // display message describing the actual result of this test
                writeLog( "verify: %s%n%n", datasetToString( retrievedAnotherBagContents ) ) ;

                compareDatasets( anotherBagContents[ 0 ], retrievedAnotherBagContents, IS_UNORDERED ) ;
                }

            this.currentTestPassed = true ;
            } ) ;

        }    // end testCloningConstructorLinkedBag()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#LinkedBag(edu.wit.scds.ds.bags.BagInterface)}
     * using a non-{@code LinkedBag} instance as the {@code sourceBag} argument.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param anotherBagContentsArgument
     *     contents for anotherBag
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-cloning-constructor.data", numLinesToSkip = 1 )
    @DisplayName( "cloning constructor with non-LinkedBag" )
    @Order( 100400 )
    void testCloningConstructorNonLinkedBag( final boolean isLastTest,
                                             final boolean isStubBehavior,
                                             final String anotherBagContentsArgument,
                                             final TestInfo testInfo )
        {

        final Object[][] anotherBagContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "sourceBag" }, anotherBagContentsArgument ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // display message describing the expected result of this test
            writeLog( "expect: %s%n%n",
                      anotherBagContents[ 0 ] == null
                              ? "[]"
                              : datasetToString( anotherBagContents[ 0 ] ) ) ;

            BagInterface<Object> anotherBag = null ;

            if ( anotherBagContentsArgument == null )
                {
                // anotherBag is null
                }
            else
                {
                // instantiate and populate anotherBag
                anotherBag = new ArrayBag<>() ;

                populateBag( anotherBag, anotherBagContents[ 0 ], USE_ADD ) ;
                }

            // instantiate testBag
            final BagInterface<Object> testBag ;

            try
                {
                testBag = new LinkedBag<>( anotherBag ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            final Object[] testBagContents = getContentsOfChainBackedDataset( testBag,
                                                                              "firstNode",
                                                                              "numberOfEntries",
                                                                              -1,
                                                                              "data",
                                                                              "next" ) ;
            writeLog( "actual: %s%n%n", datasetToString( testBagContents ) ) ;

            // empty?
            if ( anotherBagContents[ 0 ] == null )
                {
                assertEquals( 0, getIntField( testBag, "numberOfEntries" ) ) ;
                }
            else
                {
                assertEquals( anotherBagContents[ 0 ].length == 0,
                              getIntField( testBag, "numberOfEntries" ) == 0 ) ;
                }

            // correct number of entries?
            assertEquals( anotherBag == null
                    ? 0
                    : getIntField( anotherBag, "numberOfEntries" ),
                          getIntField( testBag, "numberOfEntries" ) ) ;

            // verify the test bag's contents
            compareDatasets( anotherBag == null
                    ? new Object[ 0 ]
                    : anotherBagContents[ 0 ], testBagContents, IS_UNORDERED ) ;

            // verify another bag's contents - must be a non-destructive operation

            if ( anotherBag != null )
                {
                // correct entries?
                final Object[] retrievedAnotherBagContents
                        = getContentsOfArrayBackedDataset( anotherBag, "bag" ) ;

                // display message describing the actual result of this test
                writeLog( "verify: %s%n%n", datasetToString( retrievedAnotherBagContents ) ) ;

                compareDatasets( anotherBagContents[ 0 ], retrievedAnotherBagContents, IS_UNORDERED ) ;
                }

            this.currentTestPassed = true ;
            } ) ;

        }    // end testCloningConstructorNonLinkedBag()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#add(java.lang.Object)}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param bagContentsArgument
     *     contents to add to the bag
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-add.data", numLinesToSkip = 1 )
    @DisplayName( "add()" )
    @Order( 400100 )
    @Disabled
    void testAdd( final boolean isLastTest,
                  final boolean isStubBehavior,
                  final String bagContentsArgument,
                  final TestInfo testInfo )
        {

        final Object[][] bagContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "newEntry(s)" }, bagContentsArgument ) ;

        final boolean bagContentsContainsNull = datasetContains( bagContents[ 0 ], null ) ;

        // display message describing the expected result of this test
        writeLog( "expect: %s%n%n",
                  bagContents[ 0 ] == null
                          ? "[]"
                          : bagContentsContainsNull
                                  ? IllegalArgumentException.class.getSimpleName()
                                    + ": \"entry cannot be null\""
                                  : datasetToString( bagContents[ 0 ] ) ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // instantiate testBag
            final BagInterface<Object> testBag = new LinkedBag<>() ;

            // populate it
            if ( bagContentsContainsNull )
                {
                Throwable thrownCondition = new PlaceholderException( new IllegalArgumentException() ) ;

                try
                    {
                    populateBag( testBag, bagContents[ 0 ], USE_ADD ) ;

                    // display message describing the actual result of this test
                    writeLog( "actual: no Exception thrown%n%n" ) ;
                    }
                catch ( final Throwable e )
                    {
                    thrownCondition = e ;

                    // display message describing the actual result of this test
                    writeLog( "actual: %s%s%n%n",
                              e.getClass().getSimpleName(),
                              e.getMessage() == null
                                      ? ""
                                      : ": \"" + e.getMessage() + "\"" ) ;
                    }

                assertEquals( IllegalArgumentException.class, thrownCondition.getClass() ) ;
                }
            else
                {

                try
                    {

                    try
                        {
                        populateBag( testBag, bagContents[ 0 ], USE_ADD ) ;
                        }
                    catch ( final Throwable e )
                        {
                        writeLog( "actual: %s%s%n%n",
                                  e.getClass().getSimpleName(),
                                  e.getMessage() == null
                                          ? ""
                                          : ": \"" + e.getMessage() + "\"" ) ;

                        throw e ;   // re-throw it
                        }

                    }
                catch ( final Throwable e )
                    {
                    writeLog( "actual: %s%s%n%n",
                              e.getClass().getSimpleName(),
                              e.getMessage() == null
                                      ? ""
                                      : ": \"" + e.getMessage() + "\"" ) ;

                    throw e ;   // re-throw it
                    }

                // correct number of entries?
                assertEquals( bagContents[ 0 ] == null
                        ? 0
                        : bagContents[ 0 ].length, getIntField( testBag, "numberOfEntries" ) ) ;

                // display message describing the actual result of this test
                final Object[] testBagContents = getContentsOfChainBackedDataset( testBag,
                                                                                  "firstNode",
                                                                                  "numberOfEntries",
                                                                                  -1,
                                                                                  "data",
                                                                                  "next" ) ;
                writeLog( "actual: %s%n%n", datasetToString( testBagContents ) ) ;

                // verify the bag's contents
                if ( bagContents[ 0 ] != null )
                    {
                    compareDatasets( bagContents[ 0 ], testBagContents, IS_UNORDERED ) ;
                    }

                }

            this.currentTestPassed = true ;
            } ) ;

        }   // end testAdd()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#clear()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param bagContentsArgument
     *     contents to add to the bag
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-clear.data", numLinesToSkip = 1 )
    @DisplayName( "clear()" )
    @Order( 200100 )
    @Disabled
    void testClear( final boolean isLastTest,
                    final boolean isStubBehavior,
                    final String bagContentsArgument,
                    final TestInfo testInfo )
        {

        final Object[][] bagContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents" }, bagContentsArgument ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // display message describing the expected result of this test
            writeLog( "expect: %s%n%n", "empty bag" ) ;

            // instantiate testBag
            final BagInterface<Object> testBag = new LinkedBag<>() ;

            // populate it
            populateBag( testBag, bagContents[ 0 ], BYPASS_ADD ) ;

            // bump numberOfEntries to be able to detect use of remove() vs initializeState()
            setIntField( testBag, "numberOfEntries", getIntField( testBag, "numberOfEntries" ) + 1 ) ;  // XXX

            // clear it
            try
                {
                testBag.clear() ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            final Object[] testBagContents = getContentsOfChainBackedDataset( testBag,
                                                                              "firstNode",
                                                                              "numberOfEntries",
                                                                              -1,
                                                                              "data",
                                                                              "next" ) ;
            writeLog( "actual: %s%n%n",
                      testBagContents.length == 0
                              ? "empty bag"
                              : datasetToString( testBagContents ) ) ;

            // empty?
            assertEquals( 0, getIntField( testBag, "numberOfEntries" ) ) ;
            assertNull( getReferenceField( testBag, "firstNode" ) ) ;

            // must be repeatable clear it again
            try
                {
                testBag.clear() ;
                }
            catch ( final Throwable e )
                {
                writeLog( "verify: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the verified result of this test
            final Object[] verifyTestBagContents = getContentsOfChainBackedDataset( testBag,
                                                                                    "firstNode",
                                                                                    "numberOfEntries",
                                                                                    -1,
                                                                                    "data",
                                                                                    "next" ) ;
            writeLog( "verify: %s%n%n",
                      verifyTestBagContents.length == 0
                              ? "empty bag"
                              : datasetToString( verifyTestBagContents ) ) ;

            // empty?
            assertEquals( 0, getIntField( testBag, "numberOfEntries" ) ) ;
            assertNull( getReferenceField( testBag, "firstNode" ) ) ;

            this.currentTestPassed = true ;
            } ) ;

        }    // end testClear()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#contains(java.lang.Object)}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param bagContentsArgument
     *     contents to add to the bag
     * @param notContainedArgument
     *     items that aren't contained in the bag
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-contains.data", numLinesToSkip = 1 )
    @DisplayName( "contains(T anEntry)" )
    @Order( 200200 )
    @Disabled
    void testContains( final boolean isLastTest,
                       final boolean isStubBehavior,
                       final String bagContentsArgument,
                       final String notContainedArgument,
                       final TestInfo testInfo )
        {

        final Object[][] contents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents", "anEntry(s)" }, bagContentsArgument, notContainedArgument ) ;

        final Object searchFor = contents[ 1 ] == null
                ? null
                : contents[ 1 ].length == 0
                        ? ""
                        : contents[ 1 ][ 0 ] ;
        final boolean expectedResult = datasetContains( contents[ 0 ], searchFor ) ;

        // display message describing the expected result of this test
        writeLog( "expect: %b%n%n", expectedResult ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // instantiate testBag
            final BagInterface<Object> testBag = new LinkedBag<>() ;

            // populate it
            populateBag( testBag, contents[ 0 ], BYPASS_ADD ) ;

            final boolean actualResult ;

            // test contains()
            try
                {
                actualResult = testBag.contains( searchFor ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "actual: %b%n%n", actualResult ) ;

            // make sure we got the correct result
            assertEquals( expectedResult, actualResult ) ;

            // this operation is non-destructive so must be repeatable - do it again to make sure...

            // repeat: test contains()
            final boolean verifyResult ;

            try
                {
                verifyResult = testBag.contains( searchFor ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "verify: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "verify: %b%n%n", verifyResult ) ;

            // repeat: check for the correct result
            assertEquals( expectedResult, verifyResult ) ;

            this.currentTestPassed = true ;
            } ) ;

        }    // end testContains()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#difference(edu.wit.scds.ds.bags.BagInterface)}
     * using a {@code LinkedBag} instance as the {@code sourceBag} argument.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param testBagContentsArgument
     *     contents to add to the testBag
     * @param anotherBagContainsArgument
     *     contents to add to anotherBag
     * @param differenceBagContainsArgument
     *     contents expected in bag from testBag.difference(anotherBag)
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-difference.data", numLinesToSkip = 1 )
    @DisplayName( "difference() with LinkedBag" )
    @Order( 500100 )
    void testDifferenceLinkedBag( final boolean isLastTest,
                                  final boolean isStubBehavior,
                                  final String testBagContentsArgument,
                                  final String anotherBagContainsArgument,
                                  final String differenceBagContainsArgument,
                                  final TestInfo testInfo )
        {

        final Object[][] contents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents", "anotherBag", "resultBag" },
                             testBagContentsArgument,
                             anotherBagContainsArgument,
                             differenceBagContainsArgument ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // display message describing the expected result of this test
            writeLog( "expect: %s%n%n", datasetToString( contents[ 2 ] ) ) ;

            // instantiate testBag and populate it
            final BagInterface<Object> testBag = new LinkedBag<>() ;
            populateBag( testBag, contents[ 0 ], BYPASS_ADD ) ;

            // instantiate anotherBag and populate it
            final BagInterface<Object> anotherBag ;

            if ( contents[ 1 ] == null )
                {
                anotherBag = null ;
                }
            else
                {
                anotherBag = new LinkedBag<>() ;
                populateBag( anotherBag, contents[ 1 ], BYPASS_ADD ) ;
                }

            // calculate the difference
            final BagInterface<Object> resultBag ;

            try
                {
                resultBag = testBag.difference( anotherBag ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "actual: %s%n%n",
                      datasetToString( getContentsOfChainBackedDataset( resultBag,
                                                                        "firstNode",
                                                                        "numberOfEntries",
                                                                        -1,
                                                                        "data",
                                                                        "next" ) ) ) ;

            // verify we weren't given one of the input bags as output
            assertNotEquals( testBag, resultBag ) ;
            assertNotEquals( anotherBag, resultBag ) ;

            // verify that the resultBag's contents are correct
            compareDatasets( contents[ 2 ],
                             getContentsOfChainBackedDataset( resultBag,
                                                              "firstNode",
                                                              "numberOfEntries",
                                                              -1,
                                                              "data",
                                                              "next" ),
                             IS_UNORDERED ) ;

            // make sure testBag's contents are unchanged
            compareDatasets( contents[ 0 ],
                             getContentsOfChainBackedDataset( testBag,
                                                              "firstNode",
                                                              "numberOfEntries",
                                                              -1,
                                                              "data",
                                                              "next" ),
                             IS_UNORDERED ) ;

            // make sure anotherBag's contents are unchanged
            if ( anotherBag != null )
                {
                compareDatasets( contents[ 1 ],
                                 getContentsOfChainBackedDataset( anotherBag,
                                                                  "firstNode",
                                                                  "numberOfEntries",
                                                                  -1,
                                                                  "data",
                                                                  "next" ),
                                 IS_UNORDERED ) ;
                }

            this.currentTestPassed = true ;
            } ) ;

        }    // end testDifferenceLinkedBag()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#difference(edu.wit.scds.ds.bags.BagInterface)}
     * using a non-{@code LinkedBag} instance as the {@code sourceBag} argument.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param testBagContentsArgument
     *     contents to add to the testBag
     * @param anotherBagContainsArgument
     *     contents to add to anotherBag
     * @param differenceBagContainsArgument
     *     contents expected in bag from testBag.difference(anotherBag)
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-difference.data", numLinesToSkip = 1 )
    @DisplayName( "difference() with non-LinkedBag" )
    @Order( 500200 )
    void testDifferenceNonLinkedBag( final boolean isLastTest,
                                     final boolean isStubBehavior,
                                     final String testBagContentsArgument,
                                     final String anotherBagContainsArgument,
                                     final String differenceBagContainsArgument,
                                     final TestInfo testInfo )
        {

        final Object[][] contents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents", "anotherBag", "resultBag" },
                             testBagContentsArgument,
                             anotherBagContainsArgument,
                             differenceBagContainsArgument ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // display message describing the expected result of this test
            writeLog( "expect: %s%n%n", datasetToString( contents[ 2 ] ) ) ;

            // instantiate testBag and populate it
            final BagInterface<Object> testBag = new LinkedBag<>() ;
            populateBag( testBag, contents[ 0 ], BYPASS_ADD ) ;

            // instantiate anotherBag and populate it
            final BagInterface<Object> anotherBag ;

            if ( contents[ 1 ] == null )
                {
                anotherBag = null ;
                }
            else
                {
                anotherBag = new ArrayBag<>() ;
                populateBag( anotherBag, contents[ 1 ], BYPASS_ADD ) ;
                }

            // calculate the difference
            final BagInterface<Object> resultBag ;

            try
                {
                resultBag = testBag.difference( anotherBag ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "actual: %s%n%n",
                      datasetToString( getContentsOfChainBackedDataset( resultBag,
                                                                        "firstNode",
                                                                        "numberOfEntries",
                                                                        -1,
                                                                        "data",
                                                                        "next" ) ) ) ;

            // verify we weren't given one of the input bags as output
            assertNotEquals( testBag, resultBag ) ;
            assertNotEquals( anotherBag, resultBag ) ;

            // verify that the resultBag's contents are correct
            compareDatasets( contents[ 2 ],
                             getContentsOfChainBackedDataset( resultBag,
                                                              "firstNode",
                                                              "numberOfEntries",
                                                              -1,
                                                              "data",
                                                              "next" ),
                             IS_UNORDERED ) ;

            // make sure testBag's contents are unchanged
            compareDatasets( contents[ 0 ],
                             getContentsOfChainBackedDataset( testBag,
                                                              "firstNode",
                                                              "numberOfEntries",
                                                              -1,
                                                              "data",
                                                              "next" ),
                             IS_UNORDERED ) ;

            // make sure anotherBag's contents are unchanged
            if ( anotherBag != null )
                {
                compareDatasets( contents[ 1 ],
                                 getContentsOfArrayBackedDataset( anotherBag, "bag" ),
                                 IS_UNORDERED ) ;
                }

            this.currentTestPassed = true ;
            } ) ;

        }    // end testDifferenceNonLinkedBag()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#getCurrentSize()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param bagContentsArgument
     *     contents to add to the bag
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-get-current-size.data", numLinesToSkip = 1 )
    @DisplayName( "getCurrentSize()" )
    @Order( 200300 )
    @Disabled
    void testGetCurrentSize( final boolean isLastTest,
                             final boolean isStubBehavior,
                             final String bagContentsArgument,
                             final TestInfo testInfo )
        {

        final Object[][] bagContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents" }, bagContentsArgument ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // determine expected result
            final int expectedResult = bagContents[ 0 ].length ;

            // display message describing the expected result of this test
            writeLog( "expect: %,d%n%n", expectedResult ) ;

            // instantiate testBag
            final BagInterface<Object> testBag = new LinkedBag<>() ;

            // populate it
            populateBag( testBag, bagContents[ 0 ], BYPASS_ADD ) ;

            // determine the number of entries in the bag
            final int actualResult ;

            try
                {
                actualResult = testBag.getCurrentSize() ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "actual: %,d%n%n", actualResult ) ;

            // check for the correct result
            assertEquals( expectedResult, actualResult ) ;

            // this operation is non-destructive so must be repeatable - do it again to make sure...

            // repeat: determine the number of times the 'search for' entry appears in the bag
            final int verifyResult = testBag.getCurrentSize() ;

            // display message describing the actual result of this test
            writeLog( "verify: %,d%n%n", verifyResult ) ;

            // repeat: check for the correct result
            assertEquals( expectedResult, verifyResult ) ;

            this.currentTestPassed = true ;
            } ) ;

        }    // end testGetCurrentSize()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#getFrequencyOf(java.lang.Object)}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param bagContentsArgument
     *     contents to add to the bag
     * @param searchForEntryArgument
     *     item for which we want its frequency in the bag
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-get-frequency-of.data", numLinesToSkip = 1 )
    @DisplayName( "getFrequencyOf(T anEntry)" )
    @Order( 200400 )
    @Disabled
    void testGetFrequencyOf( final boolean isLastTest,
                             final boolean isStubBehavior,
                             final String bagContentsArgument,
                             final String searchForEntryArgument,
                             final TestInfo testInfo )
        {

        final Object[][] contents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents", "anEntry(s)" }, bagContentsArgument, searchForEntryArgument ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // determine number of times the 'search for' entry should appear in the bag
            final Object searchFor = contents[ 1 ] == null
                    ? null
                    : contents[ 1 ].length == 0
                            ? ""
                            : contents[ 1 ][ 0 ] ;
            final int expectedResult = countOccurrences( contents[ 0 ], searchFor ) ;

            // display message describing the expected result of this test
            writeLog( "expect: %,d%n%n", expectedResult ) ;

            // instantiate testBag
            final BagInterface<Object> testBag = new LinkedBag<>() ;

            // populate it
            populateBag( testBag, contents[ 0 ], BYPASS_ADD ) ;

            // determine the number of times the 'search for' entry appears in the bag
            final int actualResult ;

            try
                {
                actualResult = testBag.getFrequencyOf( searchFor ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "actual: %,d%n%n", actualResult ) ;

            // check for the correct frequency
            assertEquals( expectedResult, actualResult ) ;

            // this operation is non-destructive so must be repeatable - do it again to make sure...

            // repeat: determine the number of times the 'search for' entry appears in the bag
            final int verifyResult ;

            try
                {
                verifyResult = testBag.getFrequencyOf( searchFor ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "verify: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "verify: %,d%n%n", verifyResult ) ;

            // repeat: check for the correct frequency
            assertEquals( expectedResult, verifyResult ) ;

            this.currentTestPassed = true ;
            } ) ;

        }    // end testGetFrequencyOf()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#intersection(edu.wit.scds.ds.bags.BagInterface)}
     * using a {@code LinkedBag} instance as the {@code sourceBag} argument.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param testBagContentsArgument
     *     contents to add to the testBag
     * @param anotherBagContainsArgument
     *     contents to add to anotherBag
     * @param intersectionBagContainsArgument
     *     contents expected in bag from testBag.intersection(anotherBag)
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-intersection.data", numLinesToSkip = 1 )
    @DisplayName( "intersection() with LinkedBag" )
    @Order( 500300 )
    void testIntersectionLinkedBag( final boolean isLastTest,
                                    final boolean isStubBehavior,
                                    final String testBagContentsArgument,
                                    final String anotherBagContainsArgument,
                                    final String intersectionBagContainsArgument,
                                    final TestInfo testInfo )
        {

        final Object[][] contents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents", "anotherBag", "resultBag" },
                             testBagContentsArgument,
                             anotherBagContainsArgument,
                             intersectionBagContainsArgument ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // display message describing the expected result of this test
            writeLog( "expect: %s%n%n", datasetToString( contents[ 2 ] ) ) ;

            // instantiate testBag and populate it
            final BagInterface<Object> testBag = new LinkedBag<>() ;
            populateBag( testBag, contents[ 0 ], BYPASS_ADD ) ;

            // instantiate anotherBag
            final BagInterface<Object> anotherBag ;

            if ( contents[ 1 ] == null )
                {
                anotherBag = null ;
                }
            else
                {
                anotherBag = new LinkedBag<>() ;
                populateBag( anotherBag, contents[ 1 ], BYPASS_ADD ) ;
                }

            // calculate the intersection
            final BagInterface<Object> resultBag ;

            try
                {
                resultBag = testBag.intersection( anotherBag ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "actual: %s%n%n",
                      datasetToString( getContentsOfChainBackedDataset( resultBag,
                                                                        "firstNode",
                                                                        "numberOfEntries",
                                                                        -1,
                                                                        "data",
                                                                        "next" ) ) ) ;

            // verify we weren't given one of the input bags as output
            assertNotEquals( testBag, resultBag ) ;
            assertNotEquals( anotherBag, resultBag ) ;

            // verify that the resultBag's contents are correct
            compareDatasets( contents[ 2 ],
                             getContentsOfChainBackedDataset( resultBag,
                                                              "firstNode",
                                                              "numberOfEntries",
                                                              -1,
                                                              "data",
                                                              "next" ),
                             IS_UNORDERED ) ;

            // make sure testBag's contents are unchanged
            compareDatasets( contents[ 0 ],
                             getContentsOfChainBackedDataset( testBag,
                                                              "firstNode",
                                                              "numberOfEntries",
                                                              -1,
                                                              "data",
                                                              "next" ),
                             IS_UNORDERED ) ;

            // make sure anotherBag's contents are unchanged
            if ( anotherBag != null )
                {
                compareDatasets( contents[ 1 ],
                                 getContentsOfChainBackedDataset( anotherBag,
                                                                  "firstNode",
                                                                  "numberOfEntries",
                                                                  -1,
                                                                  "data",
                                                                  "next" ),
                                 IS_UNORDERED ) ;
                }

            this.currentTestPassed = true ;
            } ) ;

        }    // end testIntersectionLinkedBag()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#intersection(edu.wit.scds.ds.bags.BagInterface)}
     * using a non-{@code LinkedBag} instance as the {@code sourceBag} argument.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param testBagContentsArgument
     *     contents to add to the testBag
     * @param anotherBagContainsArgument
     *     contents to add to anotherBag
     * @param intersectionBagContainsArgument
     *     contents expected in bag from testBag.intersection(anotherBag)
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-intersection.data", numLinesToSkip = 1 )
    @DisplayName( "intersection() with non-LinkedBag" )
    @Order( 500400 )
    void testIntersectionNonLinkedBag( final boolean isLastTest,
                                       final boolean isStubBehavior,
                                       final String testBagContentsArgument,
                                       final String anotherBagContainsArgument,
                                       final String intersectionBagContainsArgument,
                                       final TestInfo testInfo )
        {

        final Object[][] contents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents", "anotherBag", "resultBag" },
                             testBagContentsArgument,
                             anotherBagContainsArgument,
                             intersectionBagContainsArgument ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // display message describing the expected result of this test
            writeLog( "expect: %s%n%n", datasetToString( contents[ 2 ] ) ) ;

            // instantiate testBag and populate it
            final BagInterface<Object> testBag = new LinkedBag<>() ;
            populateBag( testBag, contents[ 0 ], BYPASS_ADD ) ;

            // instantiate anotherBag and populate it
            final BagInterface<Object> anotherBag ;

            if ( contents[ 1 ] == null )
                {
                anotherBag = null ;
                }
            else
                {
                anotherBag = new ArrayBag<>() ;
                populateBag( anotherBag, contents[ 1 ], USE_ADD ) ;
                }

            // calculate the intersection
            final BagInterface<Object> resultBag ;

            try
                {
                resultBag = testBag.intersection( anotherBag ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "actual: %s%n%n",
                      datasetToString( getContentsOfChainBackedDataset( resultBag,
                                                                        "firstNode",
                                                                        "numberOfEntries",
                                                                        -1,
                                                                        "data",
                                                                        "next" ) ) ) ;

            // verify we weren't given one of the input bags as output
            assertNotEquals( testBag, resultBag ) ;
            assertNotEquals( anotherBag, resultBag ) ;

            // verify that the resultBag's contents are correct
            compareDatasets( contents[ 2 ],
                             getContentsOfChainBackedDataset( resultBag,
                                                              "firstNode",
                                                              "numberOfEntries",
                                                              -1,
                                                              "data",
                                                              "next" ),
                             IS_UNORDERED ) ;

            // make sure testBag's contents are unchanged
            compareDatasets( contents[ 0 ],
                             getContentsOfChainBackedDataset( testBag,
                                                              "firstNode",
                                                              "numberOfEntries",
                                                              -1,
                                                              "data",
                                                              "next" ),
                             IS_UNORDERED ) ;

            // make sure anotherBag's contents are unchanged
            if ( anotherBag != null )
                {
                compareDatasets( contents[ 1 ],
                                 getContentsOfArrayBackedDataset( anotherBag, "bag" ),
                                 IS_UNORDERED ) ;
                }

            this.currentTestPassed = true ;
            } ) ;

        }    // end testIntersectionNonLinkedBag()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#isEmpty()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param bagContentsArgument
     *     contents to add to the bag
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-is-empty.data", numLinesToSkip = 1 )
    @DisplayName( "isEmpty()" )
    @Order( 200500 )
    @Disabled
    void testIsEmpty( final boolean isLastTest,
                      final boolean isStubBehavior,
                      final String bagContentsArgument,
                      final TestInfo testInfo )
        {

        final Object[][] bagContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents" }, bagContentsArgument ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // determine expected result
            final boolean expectedResult = bagContents[ 0 ].length == 0 ;

            // display message describing the expected result of this test
            writeLog( "expect: %b%n%n", expectedResult ) ;

            // instantiate testBag
            final BagInterface<Object> testBag = new LinkedBag<>() ;

            // populate it
            populateBag( testBag, bagContents[ 0 ], BYPASS_ADD ) ;

            // determine the actual result
            final boolean actualResult ;

            try
                {
                actualResult = testBag.isEmpty() ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "actual: %b%n%n", actualResult ) ;

            // check for the correct result
            assertEquals( expectedResult, actualResult ) ;

            // this operation is non-destructive so must be repeatable - do it again to make sure...

            // repeat: determine the actual result
            final boolean verifyResult ;

            try
                {
                verifyResult = testBag.isEmpty() ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "verify: %b%n%n", verifyResult ) ;

            // repeat: check for the correct result
            assertEquals( expectedResult, verifyResult ) ;

            this.currentTestPassed = true ;
            } ) ;

        }    // end testIsEmpty()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#remove()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param bagContentsArgument
     *     contents to add to the bag
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-remove-unspecified.data", numLinesToSkip = 1 )
    @DisplayName( "remove() unspecified" )
    @Order( 400200 )
    @Disabled
    void testRemoveUnspecified( final boolean isLastTest,
                                final boolean isStubBehavior,
                                final String bagContentsArgument,
                                final TestInfo testInfo )
        {

        final Object[][] contents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents" }, bagContentsArgument ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // instantiate testBag and populate it
            final BagInterface<Object> testBag = new LinkedBag<>() ;
            populateBag( testBag, contents[ 0 ], BYPASS_ADD ) ;

            // remove every item from the bag
            final Object[] removedContents = new Object[ contents[ 0 ].length ] ;

            for ( int i = 0 ; i < contents[ 0 ].length ; i++ )
                {

                try
                    {
                    removedContents[ i ] = testBag.remove() ;
                    }
                catch ( final Throwable e )
                    {
                    writeLog( "actual: %s%s%n%n",
                              e.getClass().getSimpleName(),
                              e.getMessage() == null
                                      ? ""
                                      : ": \"" + e.getMessage() + "\"" ) ;

                    throw e ;   // re-throw it
                    }

                }

            // display message describing the actual result of this test
            writeLog( "actual: %s%n%n", datasetToString( removedContents ) ) ;

            // at this point the bag must be empty
            assertNull( testBag.remove() ) ;

            assertEquals( 0, getIntField( testBag, "numberOfEntries" ) ) ;
            assertNull( getReferenceField( testBag, "firstNode" ) ) ;

            // make sure the items removed match the items added
            compareDatasets( contents[ 0 ], removedContents, IS_UNORDERED ) ;

            this.currentTestPassed = true ;
            } ) ;

        }    // end testRemoveUnspecified() unspecified entry


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#remove(java.lang.Object)}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param testBagContentsArgument
     *     contents to add to the testBag
     * @param testBagContainsArgument
     *     entries to remove successfully from testBag
     * @param testBagDoesNotContainsArgument
     *     entries we can't remove from testBag
     * @param testBagRemainderContentsArgument
     *     the expected entries remaining in testBag
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-remove-specified.data", numLinesToSkip = 1 )
    @DisplayName( "remove( T anEntry )" )
    @Order( 400300 )
    @Disabled
    void testRemoveSpecified( final boolean isLastTest,
                              final boolean isStubBehavior,
                              final String testBagContentsArgument,
                              final String testBagContainsArgument,
                              final String testBagDoesNotContainsArgument,
                              final String testBagRemainderContentsArgument,
                              final TestInfo testInfo )
        {

        final Object[][] contents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents", "removable anEntry(s)", "unremovable anEntry(s)", "result" },
                             testBagContentsArgument,
                             testBagContainsArgument,
                             testBagDoesNotContainsArgument,
                             testBagRemainderContentsArgument ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // instantiate testBag and populate it
            final BagInterface<Object> testBag = new LinkedBag<>() ;
            populateBag( testBag, contents[ 0 ], BYPASS_ADD ) ;

            // display message describing the expected result of this test
            writeLog( "expect can be removed: %s%n%n", datasetToString( contents[ 1 ] ) ) ;

            // remove entries the testBag contains
            for ( int i = 0 ; i < contents[ 1 ].length ; i++ )
                {
                assertTrue( testBag.remove( contents[ 1 ][ i ] ) ) ;
                }

            // display message describing the expected result of this test
            writeLog( "expect cannot be removed: %s%n%n", datasetToString( contents[ 2 ] ) ) ;

            // remove entries the testBag doesn't contain
            for ( int i = 0 ; i < contents[ 2 ].length ; i++ )
                {
                assertFalse( testBag.remove( contents[ 2 ][ i ] ) ) ;
                }

            // display message describing the expected result of this test
            writeLog( "expect what's left: %s%n%n", datasetToString( contents[ 3 ] ) ) ;

            // display message describing the actual result of this test
            writeLog( "actual what's left: %s%n%n",
                      datasetToString( getContentsOfChainBackedDataset( testBag,
                                                                        "firstNode",
                                                                        "numberOfEntries",
                                                                        -1,
                                                                        "data",
                                                                        "next" ) ) ) ;

            // verify that testBag contains the correct entries
            compareDatasets( contents[ 3 ],
                             getContentsOfChainBackedDataset( testBag,
                                                              "firstNode",
                                                              "numberOfEntries",
                                                              -1,
                                                              "data",
                                                              "next" ),
                             IS_UNORDERED ) ;

            this.currentTestPassed = true ;
            } ) ;

        }    // end testRemoveSpecified() specified entry


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#toArray()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param bagContentsArgument
     *     contents to add to the bag
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-to-array.data", numLinesToSkip = 1 )
    @DisplayName( "toArray()" )
    @Order( 300100 )
    @Disabled
    void testToArray( final boolean isLastTest,
                      final boolean isStubBehavior,
                      final String bagContentsArgument,
                      final TestInfo testInfo )
        {

        final Object[][] bagContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents" }, bagContentsArgument ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // display message describing the expected result of this test
            writeLog( "expect: %s%n%n", datasetToString( bagContents[ 0 ] ) ) ;

            // instantiate testBag
            final BagInterface<Object> testBag = new LinkedBag<>() ;

            // populate it
            populateBag( testBag, bagContents[ 0 ], BYPASS_ADD ) ;

            // retrieve the contents of the test bag in an array
            final Object[] actualResult ;

            try
                {
                actualResult = testBag.toArray() ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "actual: %s%n%n", datasetToString( actualResult ) ) ;

            // FUTURE verify testBag's contents weren't changed

            // verify the bag's contents
            compareDatasets( bagContents[ 0 ], actualResult, IS_UNORDERED ) ;

            // this operation is non-destructive so must be repeatable - do it again to make sure...

            // retrieve the contents of the test bag in an array
            final Object[] verifyResult ;

            try
                {
                verifyResult = testBag.toArray() ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "verify: %s%n%n", datasetToString( verifyResult ) ) ;

            // verify the bag's contents
            compareDatasets( bagContents[ 0 ], verifyResult, IS_UNORDERED ) ;

            this.currentTestPassed = true ;
            } ) ;

        }    // end testToArray()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#toString()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param bagContentsArgument
     *     contents to add to the bag
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-to-array.data", numLinesToSkip = 1 )
    @DisplayName( "toString()" )
    @Order( 300200 )
    @Disabled
    void testToString( final boolean isLastTest,
                       final boolean isStubBehavior,
                       final String bagContentsArgument,
                       final TestInfo testInfo )
        {

        final Object[][] bagContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents" }, bagContentsArgument ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            final Object[] expectedResult = new Object[ bagContents[ 0 ].length ] ;

            for ( int i = 0 ; i < expectedResult.length ; i++ )
                {
                expectedResult[ i ] = bagContents[ 0 ][ expectedResult.length - i - 1 ] ;
                }

            final String expectedString = Arrays.toString( expectedResult ) ;

            // display message describing expected result of this test
            writeLog( "expect: \"%s\"%n%n", expectedString ) ;

            // instantiate testBag
            final BagInterface<Object> testBag = new LinkedBag<>() ;

            // populate it
            populateBag( testBag, bagContents[ 0 ], BYPASS_ADD ) ;

            // retrieve the contents of the test bag in String form
//            final String actualResult ;   // IN_PROGRESS
            final Object actualResult ;

            try
                {
//                actualResult = testBag.toString() ;
                actualResult = invoke( super.testClass, testBag, "toString" ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "actual: \"%s\"%n%n", actualResult ) ;

            // verify the returned string
            assertEquals( expectedString, actualResult ) ;

            // this operation is non-destructive so must be repeatable - do it again to make sure...

            // retrieve the contents of the test bag in String form
            final String verifyResult ;

            try
                {
                verifyResult = testBag.toString() ;
                }
            catch ( final Throwable e )
                {
                writeLog( "verify: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "verify: \"%s\"%n%n", verifyResult ) ;

            // verify the returned string
            assertEquals( expectedString, verifyResult ) ;

            this.currentTestPassed = true ;
            } ) ;

        }    // end testToString()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#union(edu.wit.scds.ds.bags.BagInterface)} using a
     * {@code LinkedBag} instance as the {@code sourceBag} argument.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param testBagContentsArgument
     *     contents to add to the testBag
     * @param anotherBagContainsArgument
     *     contents to add to anotherBag
     * @param unionBagContainsArgument
     *     contents expected in bag from testBag.union(anotherBag)
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-union.data", numLinesToSkip = 1 )
    @DisplayName( "union() with LinkedBag" )
    @Order( 500500 )
    void testUnionLinkedBag( final boolean isLastTest,
                             final boolean isStubBehavior,
                             final String testBagContentsArgument,
                             final String anotherBagContainsArgument,
                             final String unionBagContainsArgument,
                             final TestInfo testInfo )
        {

        final Object[][] contents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents", "anotherBag", "resultBag" },
                             testBagContentsArgument,
                             anotherBagContainsArgument,
                             unionBagContainsArgument ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // display message describing the expected result of this test
            writeLog( "expect: %s%n%n", datasetToString( contents[ 2 ] ) ) ;

            // instantiate testBag and populate it
            final BagInterface<Object> testBag = new LinkedBag<>() ;
            populateBag( testBag, contents[ 0 ], BYPASS_ADD ) ;

            // instantiate anotherBag and populate it
            final BagInterface<Object> anotherBag ;

            if ( contents[ 1 ] == null )
                {
                anotherBag = null ;
                }
            else
                {
                anotherBag = new LinkedBag<>() ;
                populateBag( anotherBag, contents[ 1 ], BYPASS_ADD ) ;
                }

            // calculate the union
            final BagInterface<Object> resultBag ;

            try
                {
                resultBag = testBag.union( anotherBag ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "actual: %s%n%n",
                      datasetToString( getContentsOfChainBackedDataset( resultBag,
                                                                        "firstNode",
                                                                        "numberOfEntries",
                                                                        -1,
                                                                        "data",
                                                                        "next" ) ) ) ;

            // verify we weren't given one of the input bags as output
            assertNotEquals( testBag, resultBag ) ;
            assertNotEquals( anotherBag, resultBag ) ;

            // verify that the resultBag's contents are correct
            compareDatasets( contents[ 2 ],
                             getContentsOfChainBackedDataset( resultBag,
                                                              "firstNode",
                                                              "numberOfEntries",
                                                              -1,
                                                              "data",
                                                              "next" ),
                             IS_UNORDERED ) ;

            // make sure testBag's contents are unchanged
            compareDatasets( contents[ 0 ],
                             getContentsOfChainBackedDataset( testBag,
                                                              "firstNode",
                                                              "numberOfEntries",
                                                              -1,
                                                              "data",
                                                              "next" ),
                             IS_UNORDERED ) ;

            // make sure anotherBag's contents are unchanged
            if ( anotherBag != null )
                {
                compareDatasets( contents[ 1 ],
                                 getContentsOfChainBackedDataset( anotherBag,
                                                                  "firstNode",
                                                                  "numberOfEntries",
                                                                  -1,
                                                                  "data",
                                                                  "next" ),
                                 IS_UNORDERED ) ;
                }

            this.currentTestPassed = true ;
            } ) ;

        }    // end testUnionLinkedBag()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#union(edu.wit.scds.ds.bags.BagInterface)} using a
     * non-{@code LinkedBag} instance as the {@code sourceBag} argument.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param testBagContentsArgument
     *     contents to add to the testBag
     * @param anotherBagContainsArgument
     *     contents to add to anotherBag
     * @param unionBagContainsArgument
     *     contents expected in bag from testBag.union(anotherBag)
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-union.data", numLinesToSkip = 1 )
    @DisplayName( "union() with non-LinkedBag" )
    @Order( 500600 )
    void testUnionNonLinkedBag( final boolean isLastTest,
                                final boolean isStubBehavior,
                                final String testBagContentsArgument,
                                final String anotherBagContainsArgument,
                                final String unionBagContainsArgument,
                                final TestInfo testInfo )
        {

        final Object[][] contents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents", "anotherBag", "resultBag" },
                             testBagContentsArgument,
                             anotherBagContainsArgument,
                             unionBagContainsArgument ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // display message describing the expected result of this test
            writeLog( "expect: %s%n%n", datasetToString( contents[ 2 ] ) ) ;

            // instantiate testBag and populate it
            final BagInterface<Object> testBag = new LinkedBag<>() ;
            populateBag( testBag, contents[ 0 ], BYPASS_ADD ) ;

            // instantiate anotherBag and populate it
            final BagInterface<Object> anotherBag ;

            if ( contents[ 1 ] == null )
                {
                anotherBag = null ;
                }
            else
                {
                anotherBag = new ArrayBag<>() ;
                populateBag( anotherBag, contents[ 1 ], BYPASS_ADD ) ;
                }

            // calculate the union
            final BagInterface<Object> resultBag ;

            try
                {
                resultBag = testBag.union( anotherBag ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "actual: %s%n%n",
                      datasetToString( getContentsOfChainBackedDataset( resultBag,
                                                                        "firstNode",
                                                                        "numberOfEntries",
                                                                        -1,
                                                                        "data",
                                                                        "next" ) ) ) ;

            // verify we weren't given one of the input bags as output
            assertNotEquals( testBag, resultBag ) ;
            assertNotEquals( anotherBag, resultBag ) ;

            // verify that the resultBag's contents are correct
            compareDatasets( contents[ 2 ],
                             getContentsOfChainBackedDataset( resultBag,
                                                              "firstNode",
                                                              "numberOfEntries",
                                                              -1,
                                                              "data",
                                                              "next" ),
                             IS_UNORDERED ) ;

            // make sure testBag's contents are unchanged
            compareDatasets( contents[ 0 ],
                             getContentsOfChainBackedDataset( testBag,
                                                              "firstNode",
                                                              "numberOfEntries",
                                                              -1,
                                                              "data",
                                                              "next" ),
                             IS_UNORDERED ) ;

            // make sure anotherBag's contents are unchanged
            if ( anotherBag != null )
                {
                compareDatasets( contents[ 1 ],
                                 getContentsOfArrayBackedDataset( anotherBag, "bag" ),
                                 IS_UNORDERED ) ;
                }

            this.currentTestPassed = true ;
            } ) ;

        }    // end testUnionNonLinkedBag()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#add(java.lang.Object)}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param initialContents
     *     initial contents of the bag
     * @param bagContentsArgument
     *     contents to add to the bag
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-add-array.data", numLinesToSkip = 1 )
    @DisplayName( "add(T[] newEntries)" )
    @Order( 90100 )
//     @Disabled
    void testAddArray( final boolean isLastTest,
                       final boolean isStubBehavior,
                       final String initialContents,
                       final String bagContentsArgument,
                       final TestInfo testInfo )
        {

        final Object[][] bagContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "context", "newEntries" }, initialContents, bagContentsArgument ) ;

        final boolean bagContentsContainsNull = datasetContains( bagContents[ 1 ], null ) ;

        // calculate the maximum number of entries which should be in the bag after add(T[])ing
        final int numberOfResultingEntries = bagContents[ 0 ].length + bagContents[ 1 ].length ;

        // build the expected resulting entries
        final Object[] expectedResultingContents
                = Arrays.copyOf( bagContents[ 0 ], numberOfResultingEntries ) ;

        if ( ! bagContentsContainsNull )
            {
            System.arraycopy( bagContents[ 1 ],
                              0,
                              expectedResultingContents,
                              bagContents[ 0 ].length,
                              bagContents[ 1 ].length ) ;
            }

        // display message describing the expected result of this test
        writeLog( "expect: %s%n%n",
                  bagContentsContainsNull
                          ? IllegalArgumentException.class.getSimpleName() + ": \"entry cannot be null\""
                          : datasetToString( expectedResultingContents ) ) ;

        // execute the test // IN_PROCESS
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // instantiate and populate testBag
//            final BagInterface<Object> testBag = new LinkedBag<>() ;
            final Object testBag = instantiate( super.testClass ) ;

            populateBag( testBag, bagContents[ 0 ], BYPASS_ADD ) ;

            // add the new entries to it
            if ( bagContentsContainsNull )
                {
                Throwable thrownCondition
                        = new PlaceholderException( new IllegalArgumentException( "entry cannot be null" ) ) ;

                try
                    {
                    // add the additional contents to the test bag
                    invoke( super.testClass,
                            testBag,
                            "add",
                            new Class<?>[]
                            { Object[].class },
                            new Object[]
                            { bagContents[ 1 ] } ) ;


                    // display message describing the actual result of this test
                    writeLog( "actual: no Exception thrown%n%n" ) ;
                    }
                catch ( final Throwable e )
                    {
                    thrownCondition = e ;

                    // display message describing the actual result of this test
                    writeLog( "actual: %s%s%n%n",
                              e.getClass().getSimpleName(),
                              e.getMessage() == null
                                      ? ""
                                      : ": \"" + e.getMessage() + "\"" ) ;
                    }

                assertEquals( IllegalArgumentException.class, thrownCondition.getClass() ) ;

                assertEquals( thrownCondition.getMessage(), "entry cannot be null" ) ;
                }
            else
                {

                try
                    {
//System.out.println( "testBag before: " + testBag.toString() ) ;    // DEBUG
//System.out.println( "newEntries: " + datasetToString( bagContents[ 1 ] ) ) ;    // DEBUG
                    // add the new entries to the test bag
                    invoke( super.testClass,
                            testBag,
                            "add",
                            new Class<?>[]
                            { Object[].class },
                            new Object[]
                            { bagContents[ 1 ] } ) ;
//System.out.println( "testBag after: " + testBag.toString() ) ;    // DEBUG
                    }
                catch ( final Throwable e )
                    {
                    writeLog( "actual: %s%s%n%n",
                              e.getClass().getSimpleName(),
                              e.getMessage() == null
                                      ? ""
                                      : ": \"" + e.getMessage() + "\"" ) ;

                    throw e ;   // re-throw it
                    }

                // correct number of entries?
                assertEquals( numberOfResultingEntries, getIntField( testBag, "numberOfEntries" ) ) ;

                // display message describing the actual result of this test
                final Object[] testBagContents = getContentsOfChainBackedDataset( testBag,
                                                                                  "firstNode",
                                                                                  "numberOfEntries",
                                                                                  -1,
                                                                                  "data",
                                                                                  "next" ) ;
                writeLog( "actual: %s%n%n", datasetToString( testBagContents ) ) ;

                // verify the bag's contents
                compareDatasets( expectedResultingContents, testBagContents, IS_UNORDERED ) ;
                }

            this.currentTestPassed = true ;
            } ) ;

        }   // end testAddArray()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#initializeState()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param bagContentsArgument
     *     contents to add to the bag
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-initialize-state.data", numLinesToSkip = 1 )
    @DisplayName( "initializeState()" )
    @Order( 100050 )
    @Disabled
    void testInitializeState( final boolean isLastTest,
                              final boolean isStubBehavior,
                              final String bagContentsArgument,
                              final TestInfo testInfo )
        {

        final Object[][] bagContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents" }, bagContentsArgument ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // display message describing the expected result of this test
            writeLog( "expect: %s%n%n", "empty bag" ) ;

            // instantiate testBag
            final BagInterface<Object> testBag = new LinkedBag<>() ;

            // populate it
            populateBag( testBag, bagContents[ 0 ], BYPASS_ADD ) ;

            // correct number of entries?
            assertEquals( bagContents[ 0 ] == null
                    ? 0
                    : bagContents[ 0 ].length, getIntField( testBag, "numberOfEntries" ) ) ;

            // initialize its state
            try
                {
                invoke( super.testClass, testBag, "initializeState", new Class<?>[] {} ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            final Object[] testBagContents = getContentsOfChainBackedDataset( testBag,
                                                                              "firstNode",
                                                                              "numberOfEntries",
                                                                              -1,
                                                                              "data",
                                                                              "next" ) ;
            writeLog( "actual: %s%n%n",
                      testBagContents.length == 0
                              ? "empty bag"
                              : datasetToString( testBagContents ) ) ;

            // empty?
            assertEquals( 0, getIntField( testBag, "numberOfEntries" ) ) ;
            assertNull( getReferenceField( testBag, "firstNode" ) ) ;

            // must be repeatable initialize it again
            try
                {
                invoke( super.testClass, testBag, "initializeState", new Class<?>[] {} ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the verified result of this test
            final Object[] verifyTestBagContents = getContentsOfChainBackedDataset( testBag,
                                                                                    "firstNode",
                                                                                    "numberOfEntries",
                                                                                    -1,
                                                                                    "data",
                                                                                    "next" ) ;
            writeLog( "verify: %s%n%n",
                      verifyTestBagContents.length == 0
                              ? "empty bag"
                              : datasetToString( verifyTestBagContents ) ) ;

            // empty?
            assertEquals( 0, getIntField( testBag, "numberOfEntries" ) ) ;
            assertNull( getReferenceField( testBag, "firstNode" ) ) ;

            this.currentTestPassed = true ;
            } ) ;

        }   // end testInitializeState()


    /**
     * Test method for {@link edu.wit.scds.ds.bags.LinkedBag#getReferenceTo(java.lang.Object)}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param bagContentsArgument
     *     contents to add to the bag
     * @param itemContainedArgument
     *     items that aren't contained in the bag
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-get-reference-to.data", numLinesToSkip = 1 )
    @DisplayName( "getReferenceTo()" )
    @Order( 900200 )
    @Disabled
    void testGetReferenceTo( final boolean isLastTest,
                             final boolean isStubBehavior,
                             final String bagContentsArgument,
                             final String itemContainedArgument,
                             final TestInfo testInfo )
        {

        final Object[][] contents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial contents", "anEntry" }, bagContentsArgument, itemContainedArgument ) ;

        final Object searchFor = contents[ 1 ] == null
                ? null
                : contents[ 1 ].length == 0
                        ? ""
                        : contents[ 1 ][ 0 ] ;

        final boolean anEntryIsContained = searchFor == null
                ? false
                : datasetContains( contents[ 0 ], searchFor ) ;

        // display message describing the expected result of this test
        writeLog( "expect: %s: %b%n%n", "in bag", anEntryIsContained ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // instantiate testBag
            final BagInterface<Object> testBag = new LinkedBag<>() ;

            // populate it
            populateBag( testBag, contents[ 0 ], BYPASS_ADD ) ;

            Object foundNode = null ;
            Object foundData = null ;

            // test getReferenceTo()
            try
                {
                // get the node
                foundNode
                        = invoke( super.testClass, testBag, "getReferenceTo", new Class<?>[]
                        { Object.class }, searchFor ) ;

                // get its data
                if ( foundNode != null )
                    {
                    foundData = getReferenceField( foundNode, "data" ) ;
                    }

                }
            catch ( final Throwable e )
                {
                writeLog( "actual: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "actual: %s: %b%n%n", "in bag", foundNode != null ) ;

            // make sure the data from the referenced Node matches
            if ( foundNode != null )
                {
                assertEquals( searchFor, foundData ) ;
                }

            // find the node in the chain
            Object currentNode = getReferenceField( testBag, "firstNode" ) ;
            Object currentData = null ;

            while ( currentNode != null )
                {
                // retrieve the Node's data
                currentData = getReferenceField( currentNode, "data" ) ;

                if ( ( currentData != null ) && ( currentData.equals( searchFor ) ) )
                    {
                    break ;
                    }

                // move to the next Node
                currentNode = getReferenceField( currentNode, "next" ) ;
                }

            // make sure we got the correct result note: we intentionally compare instance references, not
            // their contents
            writeLog( "verify: %s: %b%n%n", "correct Node returned", currentNode == foundNode ) ;
            assertTrue( currentNode == foundNode ) ;


            // this operation is non-destructive so must be repeatable - do it again to make sure...

            // repeat: test contains()
            final Object verifyNode ;

            try
                {
                verifyNode
                        = invoke( super.testClass, testBag, "getReferenceTo", new Class<?>[]
                        { Object.class }, searchFor ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "verify: %s%s%n%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "verify: %s: %b%n%n", "correct Node returned", currentNode == verifyNode ) ;

            // repeat: check for the correct result
            assertTrue( currentNode == verifyNode ) ;

            this.currentTestPassed = true ;
            } ) ;

        }   // end testGetReferenceTo()


    // -------------------------------------------------- The following utilities are used by the test methods
    // --------------------------------------------------


    // symbolic constants for populateBag()
    private static final boolean USE_ADD = false ;
    private static final boolean BYPASS_ADD = true ;


    /**
     * Utility to populate a bag using the contents of an array
     *
     * @param bagToPopulate
     *     the bag to populate
     * @param entries
     *     the entries to add to the bagToFill
     * @param bypassAdd
     *     don't use the bag's {@code add()} even if {@code bagToFill} is a {@code LinkedBag}
     */
    @SuppressWarnings( "unchecked" )
    private static void populateBag( final Object bagToPopulate,
                                     final Object[] entries,
                                     final boolean bypassAdd )
        {

        // make the bag empty
        if ( bagToPopulate instanceof LinkedBag )
            {
            setIntField( bagToPopulate, "numberOfEntries", 0 ) ;
            setReferenceField( bagToPopulate, "firstNode", null ) ;
            }
        else
            {

//            ((BagInterface<Object>) bagToPopulate).clear() ;  /* IN_PROCESS */
            try
                {
                invoke( bagToPopulate.getClass(), bagToPopulate, "clear" ) ;
                }
            catch ( final Throwable e )
                {
                // TODO Auto-generated catch block
                e.printStackTrace() ;
                }

            }

        // now fill it in order of the elements in the entries array
        if ( entries != null )
            {

            for ( final Object anEntry : entries )
                {

                if ( bypassAdd && ( bagToPopulate instanceof LinkedBag ) )
                    {
                    final Node<Object> currentFirstNode
                            = (Node<Object>) getReferenceField( bagToPopulate, "firstNode" ) ;
                    final Node<Object> newNode = new Node<>( anEntry, currentFirstNode ) ;
                    setReferenceField( bagToPopulate, "firstNode", newNode ) ;
                    setIntField( bagToPopulate,
                                 "numberOfEntries",
                                 getIntField( bagToPopulate, "numberOfEntries" ) + 1 ) ;
                    }
                else
                    {
                    ( (BagInterface<Object>) bagToPopulate ).add( anEntry ) ;   // DMR why not invoke()
                    }

                }

            }

        }    // end populateBag()

    }    // end class LinkedBagDMRTests
