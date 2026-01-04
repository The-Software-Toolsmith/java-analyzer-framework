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


package edu.wit.scds.ds.queues ;

/**
 * Enumeration of capacities to use for sizing ArrayQueues
 *
 * @author David M Rosenberg
 *
 * @version 1.0 2020-02-24 Initial implementation
 * @version 1.0.1 2025-07-29 miscellaneous cosmetic changes
 */
public enum ArrayQueueCapacity
    {

    // @formatter:off

    // Capacity         Display Name    Numeric Value
    // valid sizes
    /** DEFAULT_CAPACITY */
    DEFAULT (           "Default",      3 )

    , /** small capacity */
    SMALL (             "Small",        13 )

    , /** medium capacity */
    MEDIUM (            "Medium",       13 + ( 13 / 2 ) )

    , /** large capacity */
    LARGE (             "Large",        10_000 / 2 )

    , /** MAX_CAPACITY capacity */
    MAXIMUM (           "Maximum",      10_000 )

    // invalid sizes
    , /** under minimum (DEFAULT_CAPACITY) capacity */
    UNDER_MINIMUM (     "Under minimum", 3 - 1 )

    , /** zero (0) capacity */
    ZERO (              "Zero",         0 )

    , /** negative capacity - arbitrary negative number */
    NEGATIVE (          "Negative",     -1_000 )

    , /** over MAX_CAPACITY capacity */
    OVER_MAXIMUM (      "Over maximum", 10_000 + 1 )

    , /** way over MAX_CAPACITY capacity */
    WAY_OVER_MAXIMUM (  "Way over maximum", 10_000 + 1_000 )
    ;

    // @formatter:on

    // instance variables


    /** nicely formatted name for display */
    public final String displayName ;
    /** integer specification of the capacity */
    public final int capacityValue ;


    // constructor


    /**
     * @param testCapacityName
     *     nicely formatted name for display
     * @param testCapacityValue
     *     integer equivalent of the size
     */
    private ArrayQueueCapacity( final String testCapacityName,
                                final int testCapacityValue )
        {

        this.displayName = testCapacityName ;
        this.capacityValue = testCapacityValue ;

        } // end constructor


    // public methods


    /**
     * Parse a text description of capacity
     *
     * @param capacityDescription
     *     a name to parse
     *
     * @return the corresponding enum constant or FIRM if the name is unrecognized
     */
    public static ArrayQueueCapacity interpretDescription(
                                                           String capacityDescription )
        {

        ArrayQueueCapacity correspondingCapacity ;

        // convert the description to all lowercase for comparison
        capacityDescription = capacityDescription.toLowerCase() ;

        // rudimentary strategy: only look at first character(s)
        switch ( capacityDescription.charAt( 0 ) )
            {
            case 'd'
                -> correspondingCapacity = DEFAULT ;

            case 's'
                -> correspondingCapacity = SMALL ;

            case 'm' ->
                {
                correspondingCapacity = switch ( capacityDescription.charAt( 1 ) )
                    {
                    case 'a'
                        -> MAXIMUM ;
                    case 'e'
                        -> MEDIUM ;
                    default
                        -> null ;
                    } ;
                }

            case 'l'
                -> correspondingCapacity = LARGE ;

            case 'z'
                -> correspondingCapacity = ZERO ;

            case 'n'
                -> correspondingCapacity = NEGATIVE ;

            case 'u'
                -> correspondingCapacity = UNDER_MINIMUM ;

            case 'o'
                -> correspondingCapacity = OVER_MAXIMUM ;

            case 'w'
                -> correspondingCapacity = WAY_OVER_MAXIMUM ;

            default
                -> correspondingCapacity = null ;
            }   // end switch

        return correspondingCapacity ;

        }   // end method interpretDescription()


    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
        {

        return this.displayName ;

        }   // end method toString()


    /**
     * Test driver - displays all constants for this enumeration
     *
     * @param args
     *     -unused-
     */
    public static void main( final String[] args )
        {

        // display column headers
        System.out.printf( "%-5s %-17s %-17s %-17s %-15s %-15s%n",
                           "#",
                           "Size",
                           "Name",
                           "Display Name",
                           "Size Value",
                           "Interpreted Size" ) ;

        // display each element of the enumeration
        for ( final ArrayQueueCapacity anItemSize : ArrayQueueCapacity.values() )
            {
            System.out.printf( "%-5d %-17s %-17s %-17s %,9d       %-15s%n",
                               anItemSize.ordinal(),
                               anItemSize,
                               anItemSize.name(),
                               anItemSize.displayName,
                               anItemSize.capacityValue,
                               interpretDescription( anItemSize.toString() ) ) ;
            }   // end for

        }   // end main()

    }	// end enum ArrayQueueTestCapacity