/* @formatter:off
 *
 * © David M Rosenberg
 *
 * Topic: Queues, Deques, and Priority Queues
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
 * penalties.  My students are permitted to store this code in a private repository
 * or other private cloud-based storage.
 *
 * Do not modify or remove this notice.
 *
 * @formatter:on
 */


package edu.wit.scds.ds.queues.tests ;

import static org.junit.jupiter.api.Assertions.assertEquals ;
import static org.junit.jupiter.api.Assertions.assertFalse ;
import static org.junit.jupiter.api.Assertions.assertNotNull ;
import static org.junit.jupiter.api.Assertions.assertNull ;
import static org.junit.jupiter.api.Assertions.assertThrows ;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively ;
import static org.junit.jupiter.api.Assertions.assertTrue ;

import education.the_software_toolsmith.analyzer.framework.dynamicanalysis.JUnitTestingBase ;
import education.the_software_toolsmith.analyzer.framework.dynamicanalysis.TestingException ;

import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectBackingStores.getContentsOfCircularArrayBackedDataset ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectDataFields.getBooleanField ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectDataFields.getIntField ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectDataFields.getReferenceField ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectDataFields.setBooleanField ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectDataFields.setIntField ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectDataFields.setReferenceField ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectMethods.invoke ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.ReflectReferenceTypes.instantiate ;
import static education.the_software_toolsmith.analyzer.framework.dynamicanalysis.TestData.datasetToString ;

import edu.wit.scds.ds.queues.ArrayQueue ;
import edu.wit.scds.ds.queues.ArrayQueueCapacity ;
import edu.wit.scds.ds.queues.QueueInterface ;

import org.junit.jupiter.api.Disabled ;
import org.junit.jupiter.api.DisplayName ;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation ;
import org.junit.jupiter.api.Order ;
import org.junit.jupiter.api.Test ;
import org.junit.jupiter.api.TestInfo ;
import org.junit.jupiter.api.TestInstance ;
import org.junit.jupiter.api.TestInstance.Lifecycle ;
import org.junit.jupiter.api.TestMethodOrder ;
import org.junit.jupiter.params.ParameterizedTest ;
import org.junit.jupiter.params.provider.CsvFileSource ;

import java.util.NoSuchElementException ;

/**
 * JUnit tests for the ArrayQueue class. All methods are tested. These tests require the API for the
 * {@code LList} class implement {@code ListInterface<T>}.
 *
 * @author David M Rosenberg
 *
 * @version 1.0 2018-06-27 initial set of tests
 * @version 1.1 2018-09-02 add timeouts
 * @version 2.0 2019-05-07
 *     <ul>
 *     <li>start restructuring tests
 *     <li>disable System.exit() during testing
 *     <li>start making each subtest independent so they'll all run even if one fails
 *     </ul>
 * @version 3.0 2019-10-22 switch to new infrastructure
 * @version 3.1 2020-03-01
 *     <ul>
 *     <li>update to coding standard
 *     <li>switch capacity specifications to external enum
 *     <li>implement individual tests
 *     <li>continue switch to new infrastructure
 *     </ul>
 * @version 3.2 2020-06-21
 *     <ul>
 *     <li>remove unnecessary method {@code copyQueueIntoArray()}
 *     <li>move test data to test-data-dmr
 *     </ul>
 * @version 3.3 2020-06-24 fix Full Queue, Queue Growth, and Multiple Queue tests
 * @version 3.1 2020-10-19
 *     <ul>
 *     <li>general cleanup
 *     <li>switch from TestableQueueInterface back to QueueInterface
 *     <ul>
 *     <li>remove tests for {@code size()} and {@code toArray()}
 *     </ul>
 *     <li>continued migration to new testing infrastructure
 *     <li>rename {@code test-data-dmr} folder to {@code test-data-dmr}
 *     </ul>
 * @version 3.2 2021-07-14
 *     <ul>
 *     <li>replace deprecated wrapper class instantiations with {@code valueOf()}
 *     <li>minor updates to switch to current testing framework
 *     </ul>
 * @version 3.3 2021-07-17 manually (re)initialize instances so known valid, empty state
 * @version 3.4 2021-11-22 correct verification of {@code this.frontIndex} and {@code this.backIndex} in
 *     {@code verifyQueueState()}
 * @version 3.5 2021-11-23
 *     <ul>
 *     <li>correct verification of {@code clear()} - must be repeatable
 *     <li>enhance validation of {@code this.frontIndex} and {@code this.backIndex} and error messages in
 *     {@code verifyQueueState()}
 *     <li>reduce dependence upon methods not directly under test
 *     <li>eliminate redundant checks
 *     </ul>
 * @version 3.6 2024-04-02
 *     <ul>
 *     <li>add detailed logging in {@code verifyQueueState()}
 *     <li>remove {@code static} modifier from both {@code verifyQueueState()} methods to enable logging
 *     </ul>
 * @version 3.7 2025-04-04 updates to track with current testing framework
 * @version 3.4 2025-07-29 updates to track with current testing framework
 * @version 3.5 2025-10-23 updates to track with current {@code ArrayQueue}
 * @version 4.0 2025-12-27
 *     <ul>
 *     <li>track change from testing framework to analysis framework
 *     <li>track change to Maven
 *     </ul>
 */
@DisplayName( "ArrayQueue" )
@TestInstance( Lifecycle.PER_CLASS )
@TestMethodOrder( OrderAnnotation.class )
@SuppressWarnings( "unused" )
class ArrayQueueDMRTests extends JUnitTestingBase
    {

    private static final String specifiedTestClassPackageName = "edu.wit.scds.ds.queues" ;
    private static final String specifiedTestClassSimpleName = "ArrayQueue" ;

    private final static String TEST_DATA_DMR_PATH = "/test-data-dmr/queues/" ;


    /**
     * initialize test framework
     */
    protected ArrayQueueDMRTests()
        {

        super( specifiedTestClassPackageName, specifiedTestClassSimpleName ) ;

        }   // end no-arg constructor


    /*
     * local constants
     */
    private static final int DEFAULT_CAPACITY = ArrayQueueCapacity.DEFAULT.capacityValue ;
    private static final int MAX_CAPACITY = ArrayQueueCapacity.MAXIMUM.capacityValue ;

    private static final int SMALL_CAPACITY = ArrayQueueCapacity.SMALL.capacityValue ;
    private static final int MEDIUM_CAPACITY = ArrayQueueCapacity.MEDIUM.capacityValue ;
    private static final int LARGE_CAPACITY = ArrayQueueCapacity.LARGE.capacityValue ;

    private static final int OVER_MAXIMUM_CAPACITY = ArrayQueueCapacity.OVER_MAXIMUM.capacityValue ;
    private static final int WAY_OVER_MAXIMUM_CAPACITY = ArrayQueueCapacity.WAY_OVER_MAXIMUM.capacityValue ;

    private static final int ZERO_CAPACITY = ArrayQueueCapacity.ZERO.capacityValue ;
    private static final int NEGATIVE_CAPACITY = ArrayQueueCapacity.NEGATIVE.capacityValue ;
    private static final int UNDER_MINIMUM_CAPACITY = ArrayQueueCapacity.UNDER_MINIMUM.capacityValue ;

    private static final int QUEUE_1_BASE = 100 ;
    private static final int QUEUE_2_BASE = MAX_CAPACITY * 10 ;

    // ----------

    // individual method tests


    /**
     * Test method for {@link edu.wit.scds.ds.queues.ArrayQueue#ArrayQueue()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-constructor.data", numLinesToSkip = 1 )
    @Order( 100100 )
    @DisplayName( "Queue constructor" )
    final void testArrayQueue( final boolean isLastTest,
                               final boolean isStubBehavior,
                               final String encodedTestConfiguration,
                               final TestInfo testInfo )
        {

        final Object[][] decodedTestConfiguration
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial capacity" }, encodedTestConfiguration ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {

            if ( decodedTestConfiguration[ 0 ].length == 0 )
                {
                // test the no-arg constructor

                writeLog( "\tTesting: new ArrayQueue() with implicit default capacity %,d%n",
                          DEFAULT_CAPACITY ) ;
//                QueueInterface<Object> testQueue = null ; /* IN_PROCESS */

//                testQueue = new ArrayQueue<>() ;
                @SuppressWarnings( "unchecked" )
                final QueueInterface<Object> testQueue
                        = (QueueInterface<Object>) instantiate( super.testClass ) ;

                verifyQueueState( testQueue, 0, DEFAULT_CAPACITY ) ;
                }
            else
                {
                // test the 1-arg constructor

                final ArrayQueueCapacity testCapacity
                        = ArrayQueueCapacity.interpretDescription( (String) decodedTestConfiguration[ 0 ][ 0 ] ) ;
                final int testSize = testCapacity.capacityValue ;

                writeLog( "\tTesting: new ArrayQueue(%,d)%n", testSize ) ;

                if ( ( testSize >= DEFAULT_CAPACITY ) && ( testSize <= MAX_CAPACITY ) )
                    {
                    // test valid initial capacity
                    @SuppressWarnings( "unchecked" )
                    final QueueInterface<Object> testQueue =
//                                                    new ArrayQueue<>( testSize ) ;    /* IN_PROCESS */
                            (QueueInterface<Object>) instantiate( super.testClass,
                                                                  new Class<?>[]
                                                                  { int.class },
                                                                  testSize ) ;

                    verifyQueueState( testQueue, 0, testSize ) ;
                    }   // end test valid initial capacity
                else
                    {
                    // test invalid initial capacity
                    assertThrows( IllegalStateException.class, () ->
                    // TODO switch to try/catch for logging
                        {
//                        @SuppressWarnings( "unused" )
                        @SuppressWarnings( "unchecked" )
                        final QueueInterface<Object> testQueue =
//                                             new ArrayQueue<>( testSize ) ;   /* IN_PROCESS */
                                (QueueInterface<Object>) instantiate( super.testClass,
                                                                      new Class<?>[]
                                                                      { int.class },
                                                                      testSize ) ;
                        } ) ;
                    }   // end test invalid initial capacity

                }

            this.currentTestPassed = true ;
            } ) ;

        }   // end testArrayQueue()


    /**
     * Test method for {@link edu.wit.scds.ds.queues.ArrayQueue#enqueue(java.lang.Object)}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @Test
    @Order( 300100 )
    @Disabled                               // FUTURE
    final void testEnqueue( final boolean isLastTest,
                            final boolean isStubBehavior,
                            final String encodedTestConfiguration,
                            final TestInfo testInfo )
        {

        final Object[][] decodedTestConfiguration
                = startTest( testInfo, isLastTest, isStubBehavior, encodedTestConfiguration ) ;

        // STUB

        } // end testEnqueue()


    /**
     * Test method for {@link edu.wit.scds.ds.queues.ArrayQueue#dequeue()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @Test
    @Order( 300200 )
    @Disabled                               // FUTURE
    final void testDequeue( final boolean isLastTest,
                            final boolean isStubBehavior,
                            final String encodedTestConfiguration,
                            final TestInfo testInfo )
        {

        final Object[][] decodedTestConfiguration
                = startTest( testInfo, isLastTest, isStubBehavior, encodedTestConfiguration ) ;

        // STUB

        } // end testDequeue()


    /**
     * Test method for {@link edu.wit.scds.ds.queues.ArrayQueue#getFront()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @Test
    @Order( 300300 )
    @Disabled                               // FUTURE
    final void testGetFront( final boolean isLastTest,
                             final boolean isStubBehavior,
                             final String encodedTestConfiguration,
                             final TestInfo testInfo )
        {

        final Object[][] decodedTestConfiguration
                = startTest( testInfo, isLastTest, isStubBehavior, encodedTestConfiguration ) ;

        // STUB

        } // end testGetFront()


    /**
     * Test method for {@link edu.wit.scds.ds.queues.ArrayQueue#isEmpty()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-is-empty.data", numLinesToSkip = 1 )
    @DisplayName( "isEmpty()" )
    @Order( 200100 )
    final void testIsEmpty( final boolean isLastTest,
                            final boolean isStubBehavior,
                            final String encodedTestConfiguration,
                            final TestInfo testInfo )
        {

        final Object[][] queueContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "contents" }, encodedTestConfiguration ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // determine expected result
            final int testSize = Math.min( queueContents[ 0 ].length, MAX_CAPACITY ) ;
            final boolean expectedResult = testSize == 0 ;
            final int queueSize = Math.max( testSize, DEFAULT_CAPACITY ) ;

            // display message describing the expected result of this test
            writeLog( "\texpect: %b%n", expectedResult ) ;

            // instantiate testQueue
//            final QueueInterface<Object> testQueue = new ArrayQueue<>( queueSize ) ;  /* IN_PROCESS */
            @SuppressWarnings( "unchecked" )
            final QueueInterface<Object> testQueue = (QueueInterface<Object>) instantiate( super.testClass ) ;

            reinitializeInstance( testQueue, queueSize ) ; /* IN_PROCESS */

            // populate it
            populateQueue( testQueue, queueContents[ 0 ] ) ;

//            verifyQueueState( testQueue, testSize, queueSize ) ;  // 2xCk do this here?

            // determine if there are any entries in the queue
            final boolean actualResult ;

            // test isEmpty()
            try
                {
//                actualResult = testQueue.isEmpty() ;    /* IN_PROCESS */
                actualResult = (boolean) invoke( super.testClass, testQueue, "isEmpty" ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "\tactual: %s%s%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "\tactual: %b%n", actualResult ) ;

            // verify behavior against numberOfEntries
            final int actualNumberOfEntries = getIntField( testQueue, "numberOfEntries" ) ;
            writeLog( "\tactual: numberOfEntries: %,d; test data size: %,d; match: %b%n",
                      actualNumberOfEntries,
                      testSize,
                      actualNumberOfEntries == testSize ) ;

            // check for the correct results
            assertEquals( expectedResult, actualResult ) ; /*
                                                            * IN_PROCESS replace with if for logging
                                                            */

            assertEquals( testSize, actualNumberOfEntries ) ; /*
                                                               * IN_PROCESS replace with if for logging
                                                               */

//            // verify that will return to empty when all contents removed   /* IN_PROCESS */
//
//            writeLog( "\treturn queue to empty%n" ) ;
//            
            //// invoke( ArrayQueue.class, testQueue, "initializeState", new Class<?>[] { int.class }, new
            /// Object[] { queueSize } ) ;
            ////
            //// verifyQueueState( testQueue, 0, queueSize ) ; // IN_PROCESS
//            reinitializeInstance( testQueue, queueSize ) ;

            // re-test isEmpty() - must be repeatable
            final boolean verifyResult ;

            try
                {
//                verifyResult = testQueue.isEmpty() ;    /* IN_PROCESS */
                verifyResult = (boolean) invoke( super.testClass, testQueue, "isEmpty", null ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "\tverify: %s%s%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "\tverify: %b%n", verifyResult ) ;

            // verify behavior against numberOfEntries
            final int verifyNumberOfEntries = getIntField( testQueue, "numberOfEntries" ) ;
            writeLog( "\tverify: numberOfEntries: %,d; test data size: %,d; match: %b%n",
                      verifyNumberOfEntries,
                      testSize,
                      actualNumberOfEntries == testSize ) ;

            // check for the correct results
            assertEquals( expectedResult, verifyResult ) ; /*
                                                            * IN_PROCESS replace with if for logging
                                                            */

            assertEquals( testSize, verifyNumberOfEntries ) ; /*
                                                               * IN_PROCESS replace with if for logging
                                                               */

            this.currentTestPassed = true ;
            } ) ;

        } // end testIsEmpty()


    /**
     * Test method for {@link edu.wit.scds.ds.queues.ArrayQueue#clear()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-clear.data", numLinesToSkip = 1 )
    @DisplayName( "clear()" )
    @Order( 200200 )
    final void testClear( final boolean isLastTest,
                          final boolean isStubBehavior,
                          final String encodedTestConfiguration,
                          final TestInfo testInfo )
        {

        final Object[][] queueContents
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "contents" }, encodedTestConfiguration ) ;

        // execute the test
        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // determine expected result
            final int testSize = Math.min( queueContents[ 0 ].length, MAX_CAPACITY ) ;
            final int queueSize = Math.max( testSize, DEFAULT_CAPACITY ) ;

            // display message describing the expected result of this test
            writeLog( "\texpect: %s%n", "[]" ) ;

            // instantiate testQueue
//            final QueueInterface<Object> testQueue = new ArrayQueue<>( queueSize ) ;    /* IN_PROCESS */
            @SuppressWarnings( "unchecked" )
            final QueueInterface<Object> testQueue
                    = (QueueInterface<Object>) instantiate( super.testClass, new Class<?>[]
                    { int.class }, queueSize ) ;
            reinitializeInstance( testQueue, queueSize ) ;  // IN_PROCESS

            // populate it
            populateQueue( testQueue, queueContents[ 0 ] ) ;

            verifyQueueState( testQueue, testSize, queueSize ) ;

            // clear it
            try
                {
                testQueue.clear() ; /* IN_PROCESS */
                invoke( this.testClass, testQueue, "clear" ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "\tactual: %s%s%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // determine the contents of the queue
            final Object[] actualResult = getContentsOfCircularArrayBackedDataset( testQueue, "queue" ) ;

            // display message describing the actual result of this test
            writeLog( "\tactual: %s%n", datasetToString( actualResult ) ) ;

            // verify empty
            verifyQueueState( testQueue, 0, queueSize ) ;

//            // verify that it's empty /* IN_PROCESS 2xCk is this redundant? */
//            assertEquals( 0, actualResult.length ) ;

            // must be repeatable - clear it again
            try
                {
                testQueue.clear() ; /* IN_PROCESS */
                invoke( this.testClass, testQueue, "clear" ) ;
                }
            catch ( final Throwable e )
                {
                writeLog( "\tverify: %s%s%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                                  ? ""
                                  : ": \"" + e.getMessage() + "\"" ) ;

                throw e ;   // re-throw it
                }

            // determine the contents of the queue
            final Object[] verifyResult = getContentsOfCircularArrayBackedDataset( testQueue, "queue" ) ;

            // display message describing the actual result of this test
            writeLog( "\tverify: %s%n", datasetToString( verifyResult ) ) ;

            // verify empty
            verifyQueueState( testQueue, 0, queueSize ) ;

//            // verify that it's empty /* IN_PROCESS 2xCk is this re


            this.currentTestPassed = true ;
            } ) ;

        } // end testClear()


    // private method tests


    /**
     * Test method for {@link edu.wit.scds.ds.queues.ArrayQueue#checkCapacity(int)}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @Test
    @Order( 400100 )
    @Disabled                               // FUTURE
    final void testCheckCapacity( final boolean isLastTest,
                                  final boolean isStubBehavior,
                                  final String encodedTestConfiguration,
                                  final TestInfo testInfo )
        {

        final Object[][] decodedTestConfiguration
                = startTest( testInfo, isLastTest, isStubBehavior, encodedTestConfiguration ) ;

        // STUB

        } // end testCheckCapacity()


    /**
     * Test method for {@link edu.wit.scds.ds.queues.ArrayQueue#initializeState(int)}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @Test
    @Order( 400200 )
    @Disabled                               // FUTURE
    final void testInitializeState( final boolean isLastTest,
                                    final boolean isStubBehavior,
                                    final String encodedTestConfiguration,
                                    final TestInfo testInfo )
        {

        final Object[][] decodedTestConfiguration
                = startTest( testInfo, isLastTest, isStubBehavior, encodedTestConfiguration ) ;

        // STUB

        } // end testInitializeState()


    /**
     * Test method for {@link edu.wit.scds.ds.queues.ArrayQueue#isArrayFull()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @Test
    @Order( 400300 )
    @Disabled                               // FUTURE
    final void testIsArrayFull( final boolean isLastTest,
                                final boolean isStubBehavior,
                                final String encodedTestConfiguration,
                                final TestInfo testInfo )
        {

        final Object[][] decodedTestConfiguration
                = startTest( testInfo, isLastTest, isStubBehavior, encodedTestConfiguration ) ;

        // STUB

        } // end testIsArrayFull()


    // functionality tests


    /**
     * Test method for empty queue.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-empty-queue.data", numLinesToSkip = 1 )
    @Order( 999992 )
    @DisplayName( "Empty Queue" )
    void testEmptyQueue( final boolean isLastTest,
                         final boolean isStubBehavior,
                         final String encodedTestConfiguration,
                         final TestInfo testInfo )
        {

        final Object[][] decodedTestConfiguration
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial capacity" }, encodedTestConfiguration ) ;

        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            final ArrayQueueCapacity testCapacity
                    = ArrayQueueCapacity.interpretDescription( (String) decodedTestConfiguration[ 0 ][ 0 ] ) ;
            final int testSize = testCapacity.capacityValue ;

            writeLog( "\tTesting: new ArrayQueue(%,d)%n", testSize ) ;

//            final QueueInterface<Object> testQueue = new ArrayQueue<>( testSize ) ;   /* IN_PROCESS */
            @SuppressWarnings( "unchecked" )
            final QueueInterface<Object> testQueue = (QueueInterface<Object>) instantiate( super.testClass ) ;

            reinitializeInstance( testQueue, testSize ) ;  // IN_PROCESS
//
//            writeLog( "\tverifying state%n" );
//            verifyQueueState( testQueue, 0, testSize ) ;    // IN_PROCESS 2xCk where should this be done?

            writeLog( "\ttesting: isEmpty()%n" ) ;
//            assertTrue( testQueue.isEmpty() ) ;   /* IN_PROCESS */
            assertTrue( (boolean) invoke( super.testClass, testQueue, "isEmpty" ) ) ;

            writeLog( "\ttesting: clear()%n" ) ;
//            testQueue.clear() ;
            invoke( this.testClass, testQueue, "clear" ) ;
//            assertTrue( testQueue.isEmpty() ) ;   /* IN_PROCESS */
            assertTrue( (boolean) invoke( super.testClass, testQueue, "isEmpty" ) ) ;

            writeLog( "\ttesting: getFront()%n" ) ; /* IN_PROCESS */
//            assertThrows( EmptyQueueException.class, () -> testQueue.getFront() ) ;
            assertThrows( NoSuchElementException.class,
                          () -> invoke( super.testClass, testQueue, "getFront" ) ) ;

            writeLog( "\ttesting: dequeue()%n" ) ; /* IN_PROCESS */
//            assertThrows( EmptyQueueException.class, () -> testQueue.dequeue() ) ;
            assertThrows( NoSuchElementException.class,
                          () -> invoke( super.testClass, testQueue, "dequeue" ) ) ;

            this.currentTestPassed = true ;
            } ) ;

        }   // end testEmptyQueue()


    /**
     * Test method for full queue.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param initialCapacityConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-full-queue.data", numLinesToSkip = 1 )
    @Order( 999993 )
    @DisplayName( "Full Queue" )
    void testFullQueue( final boolean isLastTest,
                        final boolean isStubBehavior,
                        final String initialCapacityConfiguration,
                        final TestInfo testInfo )
        {

        final Object[][] decodedTestConfiguration
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial capacity" }, initialCapacityConfiguration ) ;

        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            final ArrayQueueCapacity testCapacity
                    = ArrayQueueCapacity.interpretDescription( (String) decodedTestConfiguration[ 0 ][ 0 ] ) ;
            final int testSize = testCapacity.capacityValue ;

            writeLog( "\tTesting: new ArrayQueue(%,d)%n", testSize ) ;

            @SuppressWarnings( "unchecked" )
            final QueueInterface<Object> testQueue =
//                                            new ArrayQueue<>( testSize ) ;  /* IN_PROCESS */
                    (QueueInterface<Object>) instantiate( super.testClass ) ;

            reinitializeInstance( testQueue, testSize ) ;   // IN_PROCESS

//            verifyQueueState( testQueue, 0, testSize ) ;    // IN_PROCESS 2xCk where should we do this?

            // fill it
            writeLog( "\t...filling: 0..%,d%n", testSize - 1 ) ;

            for ( int i = 0 ; i < testSize ; i++ )
                {
//                testQueue.enqueue( i ) ;    /* IN_PROCESS */
                invoke( super.testClass, testQueue, "enqueue", new Class<?>[] { Object.class }, i ) ;
                }

            verifyQueueState( testQueue, testSize, testSize ) ;

            // empty it
            writeLog( "\t...emptying: 0..%,d%n", testSize - 1 ) ;

            for ( int i = 0 ; i < testSize ; i++ )
                {
//                assertEquals( i, (int) testQueue.getFront() ) ; /* IN_PROCESS */
//                assertEquals( i, (int) testQueue.dequeue() ) ;
                assertEquals( i, (int) invoke( super.testClass, testQueue, "getFront" ) ) ;
                assertEquals( i, (int) invoke( super.testClass, testQueue, "dequeue" ) ) ;
                }

            verifyQueueState( testQueue, 0, testSize ) ;

            // advance pointers
            writeLog( "\t...advancing indices%n" ) ;

            for ( int i = 0 ; i < ( testSize / 2 ) ; i++ )
                {
//                testQueue.enqueue( i ) ;   /* IN_PROCESS */
                invoke( super.testClass, testQueue, "enqueue", new Class<?>[] { Object.class }, i ) ;

//                assertEquals( i, testQueue.dequeue() ) ;  /* IN_PROCESS */
                assertEquals( i, (int) invoke( super.testClass, testQueue, "dequeue" ) ) ;
                }

            verifyQueueState( testQueue, 0, testSize ) ;

            // re-fill it
            writeLog( "\t...re-filling: 0..%,d%n", testSize - 1 ) ;

            for ( int i = 0 ; i < testSize ; i++ )
                {
//              testQueue.enqueue( i ) ;   /* IN_PROCESS */
                invoke( super.testClass, testQueue, "enqueue", new Class<?>[] { Object.class }, i ) ;
                }

            verifyQueueState( testQueue, testSize, testSize ) ;

            // empty it
            writeLog( "\t...emptying: 0..%,d%n", testSize - 1 ) ;

            for ( int i = 0 ; i < testSize ; i++ )
                {
//              assertEquals( i, (int) testQueue.getFront() ) ; /* IN_PROCESS */
//              assertEquals( i, (int) testQueue.dequeue() ) ;
                assertEquals( i, (int) invoke( super.testClass, testQueue, "getFront" ) ) ;
                assertEquals( i, (int) invoke( super.testClass, testQueue, "dequeue" ) ) ;
                }

//            assertTrue( testQueue.isEmpty() ) ;   /* IN_PROCESS */
            assertTrue( (boolean) invoke( super.testClass, testQueue, "isEmpty" ) ) ;

            verifyQueueState( testQueue, 0, testSize ) ;

            this.currentTestPassed = true ;
            } ) ;

        }   // end testFullQueue()


    /**
     * Test method for queue growth.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param initialCapacityConfiguration
     *     initial queue capacity/array size
     * @param fillToCapacityConfiguration
     *     size to which the queue/array will be filled
     * @param offsetFillConfiguration
     *     flag to indicate if filling the queue should be 0-based (false) or forced to wrap (true)
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-queue-growth.data", numLinesToSkip = 1 )
    @Order( 999994 )
    @DisplayName( "Queue Growth" )
    void testQueueGrowth( final boolean isLastTest,
                          final boolean isStubBehavior,
                          final String initialCapacityConfiguration,
                          final String fillToCapacityConfiguration,
                          final String offsetFillConfiguration,
                          final TestInfo testInfo )
        {

        final Object[][] decodedTestConfiguration
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "initial capacity", "fill-to capacity", "offset filling queue" },
                             initialCapacityConfiguration,
                             fillToCapacityConfiguration,
                             offsetFillConfiguration ) ;

        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            // initial queue/array capacity
            final ArrayQueueCapacity testInitialCapacity
                    = ArrayQueueCapacity.interpretDescription( (String) decodedTestConfiguration[ 0 ][ 0 ] ) ;
            final int testInitialSize = testInitialCapacity.capacityValue ;

            writeLog( "\tTesting: new ArrayQueue(%,d)%n", testInitialSize ) ;


            @SuppressWarnings( "unchecked" )
            final QueueInterface<Object> testQueue =
//                                            new ArrayQueue<>( testSize ) ;  /* IN_PROCESS */
                    (QueueInterface<Object>) instantiate( super.testClass ) ;

            reinitializeInstance( testQueue, testInitialSize ) ;   // IN_PROCESS

//          verifyQueueState( testQueue, 0, testInitialSize ) ;    // IN_PROCESS 2xCk where should we do this?


            // fill-to capacity
            final ArrayQueueCapacity testGrowToCapacity
                    = ArrayQueueCapacity.interpretDescription( (String) decodedTestConfiguration[ 1 ][ 0 ] ) ;
            final int testFillToSize = testGrowToCapacity.capacityValue ;

            int testFilledCapacity = testInitialSize ;

            while ( testFilledCapacity < testFillToSize )
                {
                testFilledCapacity *= 2 ;
                }

            writeLog( "\tfill-to: %,d%n", testFillToSize ) ;


            // fill offset or aligned?
            final boolean offsetFill = (boolean) decodedTestConfiguration[ 2 ][ 0 ] ;

            if ( offsetFill )
                {
                // advance the internal pointers approximately 1/2 way
                final int offsetDistance = testInitialSize / 2 ;
                writeLog( "\t...advancing pointers by %,d elements%n", offsetDistance ) ;

                for ( int i = 0 ; i < offsetDistance ; i++ )
                    {
//                  testQueue.enqueue( i ) ;   /* IN_PROCESS */
//                    testQueue.dequeue() ;
                    invoke( super.testClass, testQueue, "enqueue", new Class<?>[] { Object.class }, i ) ;
                    invoke( super.testClass, testQueue, "dequeue" ) ;
                    }

//              assertTrue( testQueue.isEmpty() ) ;   /* IN_PROCESS */
                assertTrue( (boolean) invoke( super.testClass, testQueue, "isEmpty" ) ) ;

                verifyQueueState( testQueue, 0, testInitialSize ) ;
                }

            // fill it
            writeLog( "\t...filling queue: 0..%,d%n", testFillToSize ) ;

            for ( int i = 0 ; i < testFillToSize ; i++ )
                {
//              testQueue.enqueue( i ) ;   /* IN_PROCESS */
                invoke( super.testClass, testQueue, "enqueue", new Class<?>[] { Object.class }, i ) ;
                }

            verifyQueueState( testQueue, testFillToSize, testFilledCapacity ) ;

            // empty it
            writeLog( "\t...emptying queue: 0..%,d%n", testFillToSize ) ;

            for ( int i = 0 ; i < testFillToSize ; i++ )
                {
//                assertEquals( i, (int) testQueue.getFront() ) ; /* IN_PROCESS */
//                assertEquals( i, (int) testQueue.dequeue() ) ;
                assertEquals( i, (int) invoke( super.testClass, testQueue, "getFront" ) ) ;
                assertEquals( i, (int) invoke( super.testClass, testQueue, "dequeue" ) ) ;
                }

            // verify that it's empty
            writeLog( "\tverifying empty%n" ) ;
//          assertTrue( testQueue.isEmpty() ) ;   /* IN_PROCESS */
            assertTrue( (boolean) invoke( super.testClass, testQueue, "isEmpty" ) ) ;

            verifyQueueState( testQueue, 0, testFilledCapacity ) ;


            // success
            this.currentTestPassed = true ;
            } ) ;

        }   // end testQueueGrowth()


    /**
     * Test method for multiple queues.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the stubbed behavior
     * @param passConfiguration
     *     temporary fix
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = TEST_DATA_DMR_PATH + "test-multiple-queues.data", numLinesToSkip = 1 )
    @Order( 999995 )
    @DisplayName( "Multiple Queues" )
    void testMultipleQueues( final boolean isLastTest,
                             final boolean isStubBehavior,
                             final String passConfiguration,
                             final TestInfo testInfo )
        {

        final Object[][] decodedTestConfiguration
                = startTest( testInfo, isLastTest, isStubBehavior, new String[]
                { "pass" }, passConfiguration ) ;

        assertTimeoutPreemptively( super.testTimeLimit, () ->
            {
            QueueInterface<Integer> testQueue1 ;
            QueueInterface<Integer> testQueue2 ;

            final Integer testValue37 = 37 ;
            final Integer testValue42 = 42 ;

            /* ----- */

            final int passNumber = (int) (long) decodedTestConfiguration[ 0 ][ 0 ] ;

            switch ( passNumber )
                {
                case 1 :
                    /* @formatter:off
                     *
                     * - instantiate 2 queues
                     * - add an item to one queue
                     * - make sure it contains the item and other is still empty
                     * - repeat test with opposite queues
                     * - repeat test with both queues simultaneously
                     * - remove the items and make sure both queues are empty
                     *
                     * @formatter:on
                     */
                    writeLog( "Testing: multiple queue instances (%,d)%n", passNumber ) ;

                    writeLog( "\t...instantiate 2 queues%n" ) ;
                    testQueue1 = null ;  // reset the pointer
//                    testQueue1 = new ArrayQueue<>( DEFAULT_CAPACITY ) ;  /* IN_PROCESS */
                    @SuppressWarnings( "unchecked" )
                    final QueueInterface<Integer> tempTestQueue1
                            = (QueueInterface<Integer>) instantiate( super.testClass ) ;
                    testQueue1 = tempTestQueue1 ;

                    reinitializeInstance( testQueue1, DEFAULT_CAPACITY ) ;   // IN_PROCESS

                    testQueue2 = null ;  // reset the pointer
//                    testQueue2 = new ArrayQueue<>( MEDIUM_CAPACITY ) ;  /* IN_PROCESS */
                    @SuppressWarnings( "unchecked" )
                    final QueueInterface<Integer> tempTestQueue2
                            = (QueueInterface<Integer>) instantiate( super.testClass ) ;
                    testQueue2 = tempTestQueue2 ;

                    reinitializeInstance( testQueue2, MEDIUM_CAPACITY ) ;   // IN_PROCESS

                    // make sure first queue is unchanged
                    verifyQueueState( testQueue1, 0, DEFAULT_CAPACITY ) ;
                    verifyQueueState( testQueue2, 0, MEDIUM_CAPACITY ) ;  // IN_PROCESS 2xCk ensure
                                                                          // constructor didn't break anything

                    // add an item to testQueue1
                    writeLog( "\t...enqueue 1 item onto queue 1%n" ) ;
                    testQueue1.enqueue( testValue42 ) ;

                    /* IN_PROCESS finish switch from direct calls to reflective invocations */

                    writeLog( "\t...test for item on queue 1%n" ) ;
//                    assertFalse( testQueue1.isEmpty() ) ; /* IN_PROCESS */
//                    assertEquals( testValue42, testQueue1.getFront() ) ;
                    assertFalse( (boolean) invoke( super.testClass, testQueue1, "isEmpty" ) ) ;
                    assertEquals( testValue42, (int) invoke( super.testClass, testQueue1, "getFront" ) ) ;

                    // testQueue2 must still be empty
                    writeLog( "\t...test queue 2 for empty%n" ) ;
//                    assertTrue( testQueue2.isEmpty() ) ;    /* IN_PROCESS */
                    assertTrue( (boolean) invoke( super.testClass, testQueue2, "isEmpty" ) ) ;

                    verifyQueueState( testQueue1, 1, DEFAULT_CAPACITY ) ;
                    verifyQueueState( testQueue2, 0, MEDIUM_CAPACITY ) ;

                    // we can remove the item from testQueue1 and both queues are now empty
                    writeLog( "\t...dequeue item from queue 1%n" ) ;
                    assertEquals( testValue42, testQueue1.dequeue() ) ;

                    writeLog( "\t...verify both queues empty%n" ) ;
//                    assertTrue( testQueue1.isEmpty() ) ;
//                    assertTrue( testQueue2.isEmpty() ) ;
                    assertTrue( (boolean) invoke( super.testClass, testQueue1, "isEmpty" ) ) ;
                    assertTrue( (boolean) invoke( super.testClass, testQueue2, "isEmpty" ) ) ;

                    verifyQueueState( testQueue1, 0, DEFAULT_CAPACITY ) ;
                    verifyQueueState( testQueue2, 0, MEDIUM_CAPACITY ) ;

                    // add an item to testQueue2
                    writeLog( "\t...enqueue 1 item onto queue 2%n" ) ;
                    testQueue2.enqueue( testValue37 ) ;

                    writeLog( "\t...test for item on queue 2%n" ) ;
//                    assertFalse( testQueue2.isEmpty() ) ;
                    assertFalse( (boolean) invoke( super.testClass, testQueue2, "isEmpty" ) ) ;
                    assertEquals( testValue37, testQueue2.getFront() ) ;

                    // testQueue1 must still be empty
                    writeLog( "\t...test queue 1 for empty%n" ) ;
//                    assertTrue( testQueue1.isEmpty() ) ;
                    assertTrue( (boolean) invoke( super.testClass, testQueue1, "isEmpty" ) ) ;

                    verifyQueueState( testQueue1, 0, DEFAULT_CAPACITY ) ;
                    verifyQueueState( testQueue2, 1, MEDIUM_CAPACITY ) ;

                    // we can remove the item from testQueue2 and both queues are now empty
                    writeLog( "\t...dequeue item from queue 2%n" ) ;
                    assertEquals( testValue37, testQueue2.dequeue() ) ;

                    writeLog( "\t...verify both queues empty%n" ) ;
//                    assertTrue( testQueue1.isEmpty() ) ;
//                    assertTrue( testQueue2.isEmpty() ) ;
//
                    verifyQueueState( testQueue1, 0, DEFAULT_CAPACITY ) ;
                    verifyQueueState( testQueue2, 0, MEDIUM_CAPACITY ) ;

                    // add an item to testQueue1
                    writeLog( "\t...enqueue 1 item onto each queue%n" ) ;
                    testQueue1.enqueue( testValue42 ) ;
                    testQueue2.enqueue( testValue37 ) ;

                    writeLog( "\t...test for correct items on each queue%n" ) ;
//                    assertFalse( testQueue1.isEmpty() ) ;
                    assertEquals( testValue42, testQueue1.getFront() ) ;
//                    assertFalse( testQueue2.isEmpty() ) ;
                    assertEquals( testValue37, testQueue2.getFront() ) ;
                    assertFalse( (boolean) invoke( super.testClass, testQueue1, "isEmpty" ) ) ;
                    assertFalse( (boolean) invoke( super.testClass, testQueue2, "isEmpty" ) ) ;

                    verifyQueueState( testQueue1, 1, DEFAULT_CAPACITY ) ;
                    verifyQueueState( testQueue2, 1, MEDIUM_CAPACITY ) ;

                    // we can remove the items from each and both queues are now empty
                    writeLog( "\t...dequeue items from each queue%n" ) ;
                    assertEquals( testValue42, testQueue1.dequeue() ) ;
                    assertEquals( testValue37, testQueue2.dequeue() ) ;

                    writeLog( "\t...verify both queues empty%n" ) ;
//                    assertTrue( testQueue1.isEmpty() ) ;
//                    assertTrue( testQueue2.isEmpty() ) ;
//
                    verifyQueueState( testQueue1, 0, DEFAULT_CAPACITY ) ;
                    verifyQueueState( testQueue2, 0, MEDIUM_CAPACITY ) ;

                    break ;

                case 2 :
                    /* @formatter:off
                     *
                     * - instantiate queue 1
                     * - add an item to one queue
                     * - instantiate queue 2
                     * - make sure queue 1 contains the item and queue 2 is empty
                     * - remove the item from queue 1 and make sure both queues are empty
                     *
                     * @formatter:on
                     */
                    writeLog( "Testing: multiple queue instances (2)%n" ) ;

                    writeLog( "\t...instantiate queue 1%n" ) ;
                    testQueue1 = null ;  // reset the pointer
                    testQueue1 = new ArrayQueue<>( MEDIUM_CAPACITY ) ;
                    reinitializeInstance( testQueue1, MEDIUM_CAPACITY ) ;   // IN_PROCESS

//                    verifyQueueState( testQueue1, 0, MEDIUM_CAPACITY ) ;    // IN_PROCESS 2xCk where to do this?

                    // add an item to testQueue1
                    writeLog( "\t...enqueue 1 item onto queue 1%n" ) ;
                    testQueue1.enqueue( testValue42 ) ;

                    writeLog( "\t...test for item on queue 1%n" ) ;
                    assertFalse( testQueue1.isEmpty() ) ;
                    assertEquals( testValue42, testQueue1.getFront() ) ;

                    verifyQueueState( testQueue1, 1, MEDIUM_CAPACITY ) ;

                    writeLog( "\t...instantiate queue 2%n" ) ;
                    testQueue2 = null ;  // reset the pointer
                    testQueue2 = new ArrayQueue<>( DEFAULT_CAPACITY ) ;
                    reinitializeInstance( testQueue2, DEFAULT_CAPACITY ) ;   // IN_PROCESS

                    verifyQueueState( testQueue1, 1, MEDIUM_CAPACITY ) ;    // IN_PROCESS verify first
                                                                            // instance wasn't adversely
                                                                            // affected by constructor

                    // testQueue2 must be empty
                    writeLog( "\t...test queue 2 for empty%n" ) ;
                    assertTrue( testQueue2.isEmpty() ) ;

                    verifyQueueState( testQueue2, 0, DEFAULT_CAPACITY ) ;

                    // we can remove the item from testQueue1 and both queues are now empty
                    writeLog( "\t...dequeue item from queue 1%n" ) ;
                    assertEquals( testValue42, testQueue1.dequeue() ) ;

                    writeLog( "\t...verify both queues empty%n" ) ;
//                    assertTrue( testQueue1.isEmpty() ) ;
//                    assertTrue( testQueue2.isEmpty() ) ;
//
                    verifyQueueState( testQueue1, 0, MEDIUM_CAPACITY ) ;
                    verifyQueueState( testQueue2, 0, DEFAULT_CAPACITY ) ;

                    break ;

                case 3 :
                    /* @formatter:off
                     *
                     * - instantiate 2 queues
                     * - add items to each queue
                     * - make sure each queue contains the correct items
                     * - remove the items from both queues and make sure they are both empty
                     *
                     * @formatter:on
                     */
                    writeLog( "Testing: multiple queue instances (3)%n" ) ;

                    writeLog( "\t...instantiate 2 queues%n" ) ;
                    testQueue1 = null ;  // reset the pointer
                    testQueue1 = new ArrayQueue<>( SMALL_CAPACITY ) ;
                    reinitializeInstance( testQueue1, SMALL_CAPACITY ) ;   // IN_PROCESS

                    testQueue2 = null ;  // reset the pointer
                    testQueue2 = new ArrayQueue<>( MEDIUM_CAPACITY ) ;
                    reinitializeInstance( testQueue2, MEDIUM_CAPACITY ) ;   // IN_PROCESS

                    verifyQueueState( testQueue1, 0, SMALL_CAPACITY ) ; // IN_PROCESS 2xCk ensure constructor
                                                                        // didn't adversely affect other
                                                                        // instance
//                    verifyQueueState( testQueue2, 0, MEDIUM_CAPACITY ) ;

                    // add items to testQueue1
                    writeLog( "\t...enqueue multiple items onto queue 1%n" ) ;
                    for ( int i = 0 ; i < SMALL_CAPACITY ; i++ )
                        {
                        testQueue1.enqueue( QUEUE_1_BASE + i ) ;
                        }

                    verifyQueueState( testQueue1, SMALL_CAPACITY, SMALL_CAPACITY ) ;
                    verifyQueueState( testQueue2, 0, MEDIUM_CAPACITY ) ;

                    // add an items to testQueue2
                    writeLog( "\t...enqueue multiple items onto queue 2%n" ) ;
                    for ( int i = 0 ; i < MEDIUM_CAPACITY ; i++ )
                        {
                        testQueue2.enqueue( QUEUE_2_BASE + i ) ;
                        }

                    verifyQueueState( testQueue1, SMALL_CAPACITY, SMALL_CAPACITY ) ;
                    verifyQueueState( testQueue2, MEDIUM_CAPACITY, MEDIUM_CAPACITY ) ;

                    // remove items from testQueue1
                    writeLog( "\t...test for items on queue 1%n" ) ;
                    assertFalse( testQueue1.isEmpty() ) ;

                    for ( int i = 0 ; i < SMALL_CAPACITY ; i++ )
                        {
                        assertEquals( Integer.valueOf( QUEUE_1_BASE + i ), testQueue1.dequeue() ) ;
                        }

//                    assertTrue( testQueue1.isEmpty() ) ;
//
                    verifyQueueState( testQueue1, 0, SMALL_CAPACITY ) ;
                    verifyQueueState( testQueue2, MEDIUM_CAPACITY, MEDIUM_CAPACITY ) ;

                    // remove items from testQueue2
                    writeLog( "\t...test for items on queue 2%n" ) ;
                    assertFalse( testQueue2.isEmpty() ) ;

                    for ( int i = 0 ; i < MEDIUM_CAPACITY ; i++ )
                        {
                        assertEquals( Integer.valueOf( QUEUE_2_BASE + i ), testQueue2.dequeue() ) ;
                        }

//                    assertTrue( testQueue2.isEmpty() ) ;
//
                    verifyQueueState( testQueue1, 0, SMALL_CAPACITY ) ;
                    verifyQueueState( testQueue2, 0, MEDIUM_CAPACITY ) ;

                    break ;

                case 4 :
                    /* @formatter:off
                     *
                     * - instantiate 2 queues
                     * - add items to each queue causing them to resize
                     * - make sure each queue contains the correct items
                     * - remove the items from both queues and make sure they are both empty
                     *
                     * @formatter:on
                     */

                    writeLog( "Testing: multiple queue instances with growth (4)%n" ) ;

                    writeLog( "\t...instantiate 2 queues%n" ) ;
                    testQueue1 = null ;  // reset the pointer
                    testQueue1 = new ArrayQueue<>( SMALL_CAPACITY ) ;
                    reinitializeInstance( testQueue1, SMALL_CAPACITY ) ;   // IN_PROCESS

//                  verifyQueueState( testQueue1, 0, SMALL_CAPACITY ) ;    // IN_PROCESS 2xCk where to do this?


                    testQueue2 = null ;  // reset the pointer
                    testQueue2 = new ArrayQueue<>( DEFAULT_CAPACITY ) ;
                    reinitializeInstance( testQueue2, DEFAULT_CAPACITY ) ;   // IN_PROCESS

                    verifyQueueState( testQueue1, 0, SMALL_CAPACITY ) ;  // IN_PROCESS 2xCk verify constructor
                                                                         // didn't incorrectly affect other
                                                                         // instance
//                    verifyQueueState( testQueue2, 0, DEFAULT_CAPACITY ) ;

                    int testFilledCapacity1 = SMALL_CAPACITY ;
                    while ( testFilledCapacity1 < LARGE_CAPACITY )
                        {
                        testFilledCapacity1 *= 2 ;
                        }

                    int testFilledCapacity2 = DEFAULT_CAPACITY ;
                    while ( testFilledCapacity2 < LARGE_CAPACITY )
                        {
                        testFilledCapacity2 *= 2 ;
                        }

                    // add items to testQueue1
                    writeLog( "\t...enqueue multiple items onto queue 1%n" ) ;
                    for ( int i = 0 ; i < LARGE_CAPACITY ; i++ )
                        {
                        testQueue1.enqueue( QUEUE_1_BASE + i ) ;
                        }

                    verifyQueueState( testQueue1, LARGE_CAPACITY, testFilledCapacity1 ) ;
                    verifyQueueState( testQueue2, 0, DEFAULT_CAPACITY ) ;

                    // add an items to testQueue2
                    writeLog( "\t...enqueue multiple items onto queue 2%n" ) ;
                    for ( int i = 0 ; i < LARGE_CAPACITY ; i++ )
                        {
                        testQueue2.enqueue( QUEUE_2_BASE + i ) ;
                        }

                    verifyQueueState( testQueue1, LARGE_CAPACITY, testFilledCapacity1 ) ;
                    verifyQueueState( testQueue2, LARGE_CAPACITY, testFilledCapacity2 ) ;

                    // remove items from testQueue2
                    writeLog( "\t...test for items on queue 2%n" ) ;
//                    assertFalse( testQueue2.isEmpty() ) ;

                    for ( int i = 0 ; i < LARGE_CAPACITY ; i++ )
                        {
                        assertEquals( Integer.valueOf( QUEUE_2_BASE + i ), testQueue2.dequeue() ) ;
                        }

//                    assertTrue( testQueue2.isEmpty() ) ;
//
                    verifyQueueState( testQueue1, LARGE_CAPACITY, testFilledCapacity1 ) ;
                    verifyQueueState( testQueue2, 0, testFilledCapacity2 ) ;

                    // remove items from testQueue1
                    writeLog( "\t...test for items on queue 1%n" ) ;
                    assertFalse( testQueue1.isEmpty() ) ;

                    for ( int i = 0 ; i < LARGE_CAPACITY ; i++ )
                        {
                        assertEquals( Integer.valueOf( QUEUE_1_BASE + i ), testQueue1.dequeue() ) ;
                        }

//                    assertTrue( testQueue1.isEmpty() ) ;
//
                    verifyQueueState( testQueue1, 0, testFilledCapacity1 ) ;
                    verifyQueueState( testQueue2, 0, testFilledCapacity2 ) ;

                    break ;

                default :
                    throw new TestingException( "unexpected pass number: " + passNumber ) ;
                }

            this.currentTestPassed = true ;
            } ) ;

        }   // end testMultipleQueues()


    // -------------------------------------------------- The following utilities are used by the test methods
    // --------------------------------------------------


    /**
     * Utility to populate a queue from the contents of an array
     *
     * @param queueToFill
     *     the queue to populate
     * @param entries
     *     the entries to add to the queueToFill
     */
    private static void populateQueue( final QueueInterface<Object> queueToFill,
                                       final Object[] entries )
    // FUTURE add switch to use enqueue() or manually add entries
        {

        if ( entries != null )
            {
            final int iterations = Math.min( entries.length, MAX_CAPACITY ) ;

            for ( int i = 0 ; i < iterations ; i++ )
                {
                queueToFill.enqueue( entries[ i ] ) ;
                }

            }

        }   // end populateQueue()


    /**
     * Test the provided ArrayQueue instance to ensure it's in a valid state with the specified number of
     * entries
     *
     * @param testQueue
     *     the {@code ArrayQueue} instance to test
     * @param expectedNumberOfEntries
     *     the number of entries that should be in the queue
     */
    private void verifyQueueState( final QueueInterface<?> testQueue,
                                   final int expectedNumberOfEntries )
        {

        verifyQueueState( testQueue, expectedNumberOfEntries, -1 ) ;

        }   // end 2-arg verifyQueueState()


    /**
     * Test the provided ArrayQueue instance to ensure it's in a valid state with the specified number of
     * entries and queue array length
     *
     * @param testQueue
     *     the {@code ArrayQueue} instance to test
     * @param expectedNumberOfEntries
     *     the number of entries that should be in the queue
     * @param expectedQueueLength
     *     the length the queue array should be
     */
    private void verifyQueueState( final QueueInterface<?> testQueue,
                                   final int expectedNumberOfEntries,
                                   final int expectedQueueLength )
        {

        writeLog( "\tverify:%n" ) ;
        writeLog( "\t\tclass: ArrayQueue%n" ) ;
        assertTrue( testQueue instanceof ArrayQueue ) ;

        writeLog( "\t\tqueue is present%n" ) ;
        final Object[] queue = (Object[]) getReferenceField( testQueue, "queue" ) ;
        assertNotNull( queue ) ;

        if ( expectedQueueLength > 0 )
            {
            writeLog( "\t\tqueue array length: expect %,d; actual: %,d%n",
                      expectedQueueLength,
                      queue.length ) ;
            assertEquals( expectedQueueLength, queue.length, "incorrect queue array length" ) ;
            }

        final int numberOfEntries = getIntField( testQueue, "numberOfEntries" ) ;
        writeLog( "\t\tnumberOfEntries: expect %,d; actual: %,d%n",
                  expectedNumberOfEntries,
                  numberOfEntries ) ;
        assertEquals( expectedNumberOfEntries, numberOfEntries, "incorrect numberOfEntries" ) ;

        // validate front and back indices
        final int frontIndex = getIntField( testQueue, "frontIndex" ) ;
        final int backIndex = getIntField( testQueue, "backIndex" ) ;

        writeLog( "\t\tvalidating indices: front: %,d; back: %,d%n", frontIndex, backIndex ) ;
        final String stateDescription = numberOfEntries == 0
                ? "empty"
                : numberOfEntries == queue.length
                        ? "full"
                        : "partially full" ;
        assertTrue( frontIndex >= 0,
                    String.format( "incorrect %s state - this.frontIndex (%,d) is invalid/negative",
                                   stateDescription,
                                   frontIndex ) ) ;
        assertTrue( frontIndex < queue.length,
                    String.format( "incorrect %s state - this.frontIndex (%,d) is invalid/too large",
                                   stateDescription,
                                   frontIndex ) ) ;
        assertTrue( backIndex >= 0,
                    String.format( "incorrect %s state - this.backIndex (%,d) is invalid/negative",
                                   stateDescription,
                                   backIndex ) ) ;
        assertTrue( backIndex < queue.length,
                    String.format( "incorrect %s state - this.backIndex (%,d) is invalid/too large",
                                   stateDescription,
                                   backIndex ) ) ;
        assertEquals( ( ( ( frontIndex + numberOfEntries ) - 1 ) + queue.length ) % queue.length,
                      backIndex,
                      String.format( "incorrect %s state - this.frontIndex (%,d) and this.backIndex (%,d) are wrong given %,d %s",
                                     stateDescription,
                                     frontIndex,
                                     backIndex,
                                     numberOfEntries,
                                     numberOfEntries == 1
                                             ? "entry"
                                             : "entries" ) ) ;
        writeLog( "\t\t\tindices ok%n" ) ;

        final boolean integrityOK = getBooleanField( testQueue, "integrityOK" ) ;
        writeLog( "\t\tintegrityOk: expect: %b; actual: %b%n", true, integrityOK ) ;
        assertTrue( integrityOK, "incorrect value in this.integrityOK" ) ;

        writeLog( "\t\tensuring no null entries in in-use portion of array%n" ) ;

        for ( int i = 0 ; i < numberOfEntries ; i++ )
            {
            assertNotNull( queue[ ( frontIndex + i ) % queue.length ],
                           String.format( "unexpected null in in-use portion of queue array at index %,d",
                                          i ) ) ;
            }

        writeLog( "\t\tensuring no non-null entries in unused portion of array%n" ) ;

        for ( int i = 0 ; i < ( queue.length - numberOfEntries ) ; i++ )
            {
            assertNull( queue[ ( frontIndex + numberOfEntries + i ) % queue.length ],
                        String.format( "unexpected non-null in unused portion of queue array at index %,d",
                                       i ) ) ;
            }

        }   // end 3-arg verifyQueueState()


    /**
     * 're'-initialize an instance to a valid, empty state
     *
     * @param instance
     *     the ArrayQueue instance to initialize
     * @param desiredCapacity
     *     the desired capacity
     */
    private static void reinitializeInstance( final Object instance,
                                              final int desiredCapacity )
        {

        setBooleanField( instance, "integrityOK", false ) ;
        setReferenceField( instance, "queue", new Object[ desiredCapacity ] ) ;
        setIntField( instance, "frontIndex", 0 ) ;
        setIntField( instance, "backIndex", desiredCapacity - 1 ) ;
        setIntField( instance, "numberOfEntries", 0 ) ;
        setBooleanField( instance, "integrityOK", true ) ;

        }   // end reinitializeInstance()

    }   // end class ArrayQueueDMRTests
