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


package edu.wit.scds.ds.bags ;

import java.util.Arrays ;


/**
 * resizable array-backed bag implementation
 *
 * @author David M Rosenberg
 *
 * @version 1.0 2025-09-24 initial version based upon fixed ArrayBag v4
 *
 * @param <T>
 *     type placeholder for the application's data
 *
 * @since 1.0
 */
public final class ArrayBag<T> implements BagInterface<T>
    {

    /*
     * constants
     */


    /** default when the application doesn't specify the initial capacity */
    private final static int DEFAULT_CAPACITY = 10 ;

    /** maximum capacity of an {@code ArrayBag} */
    private final static int MAX_CAPACITY = Integer.MAX_VALUE / 2 ;


    /*
     * instance state / data fields
     */


    /** backing store for application data */
    private T[] bag ;
    /** current data entry count */
    private int numberOfEntries ;

    /** for integrity checking */
    private boolean integrityOk = false ;


    /*
     * constructors
     */


    /**
     * set initial state to valid empty with default capacity
     *
     * @since 1.0
     */
    public ArrayBag()
        {

        this( DEFAULT_CAPACITY ) ;

        }   // end no-arg constructor


    /**
     * set initial state to valid empty with a specified capacity
     *
     * @param initialCapacity
     *     the initial number of items we can hold
     *
     * @since 1.0
     */
    public ArrayBag( final int initialCapacity )
        {

        // state isn't valid yet
        this.integrityOk = false ;

        // make sure the initial capacity is acceptable
        checkCapacity( initialCapacity ) ;

        // cast is safe because the new array is null-filled
        @SuppressWarnings( "unchecked" )
        final T[] tempBag = (T[]) new Object[ initialCapacity ] ;
        this.bag = tempBag ;

        this.numberOfEntries = 0 ;

        // state is now valid
        this.integrityOk = true ;

        }   // end 1-arg constructor


    /*
     * API methods
     */


    @Override
    public boolean add( final T newEntry )
        {

        // make sure our state is valid
        checkIntegrity() ;

        // reject null as data
        if ( newEntry == null )
            {

            return false ;

            }

        // assertion: we have an actual object (non-null) reference to store

        // make sure there's available space
        ensureCapacity() ;

        // assertion: at least one element of the array is available

        // store the new entry
        this.bag[ this.numberOfEntries ] = newEntry ;

        this.numberOfEntries++ ;

        // success
        return true ;

        }   // end add()


    @Override
    public void clear()
        {

        // make sure our state is valid
        checkIntegrity() ;


        // state may become invalid
        this.integrityOk = false ;

        // remove all the in-use references
        Arrays.fill( this.bag, 0, this.numberOfEntries, null ) ;

        // reset the in-use counter
        this.numberOfEntries = 0 ;

        // state is valid
        this.integrityOk = true ;

        // assertion: we are in a valid, empty state

        }   // end clear()


    @Override
    public boolean contains( final T anEntry )
        {

        // make sure our state is valid
        checkIntegrity() ;


        return getIndexOf( anEntry ) >= 0 ;

        }   // end contains()


    @Override
    public int getCurrentSize()
        {

        // make sure our state is valid
        checkIntegrity() ;


        return this.numberOfEntries ;

        }   // end getCurrentSize()


    @Override
    public int getFrequencyOf( final T anEntry )
        {

        // make sure our state is valid
        checkIntegrity() ;

        // we don't store null so can't find one
        if ( anEntry == null )
            {

            return 0 ;

            }

        // assertion: we are looking for non-null data

        int timesSeen = 0 ; // initially none

        // count all matching entries
        for ( int i = 0 ; i < this.numberOfEntries ; i++ )
            {

            if ( this.bag[ i ].equals( anEntry ) )
                {

                // found a match - count it
                timesSeen++ ;

                }

            }   // end for

        // assertion: timesSeen is in the range 0..numberOfEntries, inclusive

        return timesSeen ;

        }   // end getFrequencyOf()


    @Override
    public boolean isEmpty()
        {

        // make sure our state is valid
        checkIntegrity() ;


        return this.numberOfEntries == 0 ;

        }   // end isEmpty()


    @Override
    public T remove()
        {

        // make sure our state is valid
        checkIntegrity() ;


        return removeEntry( this.numberOfEntries - 1 ) ;

        }   // end no-arg/unspecified remove()


    @Override
    public boolean remove( final T anEntry )
        {

        // make sure our state is valid
        checkIntegrity() ;


        // find the first matching entry
        final int whereFound = getIndexOf( anEntry ) ;

        // if found, replace it with the last entry then remove the last entry
        return removeEntry( whereFound ) != null ;  // indicate success/failure

        }   // end 1-arg/specified remove()


    @Override
    public T[] toArray()
        {

        // make sure our state is valid
        checkIntegrity() ;


        // copy in-use portion of the bag array into a new array for the caller
        return Arrays.copyOf( this.bag, this.numberOfEntries ) ;

        }   // end toArray()


    /**
     * Collections typically don't include {@code toString()}; provided for
     * testing and debugging
     *
     * @since 1.0
     */
    @Override
    public String toString()
        {

        return String.format( "expect: %s%n\tactual: iOk: %b; nOE: %,d; bag: %s",
                              Arrays.toString( Arrays.copyOf( this.bag, this.numberOfEntries ) ),
                              this.integrityOk,
                              this.numberOfEntries,
                              Arrays.toString( this.bag ) ) ;

        }   // end toString()


    /*
     * private utility methods
     */


    /**
     * ensure the specified desired capacity is acceptable
     *
     * @param desiredCapacity
     *     the specified capacity to validate
     *
     * @throws IllegalStateException
     *     occurs when the desired capacity is outside the acceptable limits
     */
    private static void checkCapacity( final int desiredCapacity ) throws IllegalStateException
        {

        // check for too small
        if ( desiredCapacity <= 0 )
            {

            throw new IllegalStateException( String.format( "desired capacity is too small: %,d",
                                                            desiredCapacity ) ) ;

            }

        // check for too large
        if ( desiredCapacity > MAX_CAPACITY )
            {

            throw new IllegalStateException( String.format( "desired capacity is too large: %,d",
                                                            desiredCapacity ) ) ;

            }

        // assertion: desiredCapacity is in the acceptable range,
        // 1..MAX_CAPACITY, inclusive

        }   // end checkCapacity()


    /**
     * prevent continued execution if our state is invalid
     *
     * @throws SecurityException
     *     occurs when our state is invalid
     */
    private void checkIntegrity() throws SecurityException
        {

        if ( !this.integrityOk )
            {

            throw new SecurityException( "state is not valid" ) ;

            }

        // assertion: our state is valid

        }   // end checkIntegrity()


    /**
     * make sure there's at least one available element in the bag array
     *
     * @throws IllegalStateException
     *     attempted to grow the array beyond the maximum allowed
     */
    private void ensureCapacity() throws IllegalStateException
        {

        // if there's space available, there's nothing for us to do
        if ( !isArrayFull() )
            {

            return ;

            }

        // we need a bigger array
        final int newCapacity = this.bag.length * 2 ;

        // make sure the larger size is acceptable
        checkCapacity( newCapacity ) ;

        // indicate that the state is at risk
        this.integrityOk = false ;

        // copy the current contents into a larger array
        final T[] tempBag = Arrays.copyOf( this.bag, newCapacity ) ;

        // make the new array active
        this.bag = tempBag ;

        // the state is valid and usable
        this.integrityOk = true ;

        }   // end ensureCapacity()


    /**
     * locate the first entry that matches the argument
     *
     * @param anEntry
     *     the entry to find
     *
     * @return the index of the first occurrence of {@code anEntry} if found, or
     *     -1 if not found
     */
    private int getIndexOf( final T anEntry )
        {

        // can't find null because we won't store it
        if ( anEntry == null )
            {

            return -1 ;

            }

        // look for the first matching entry
        for ( int i = 0 ; i < this.numberOfEntries ; i++ )
            {

            if ( this.bag[ i ].equals( anEntry ) )
                {

                return i ;  // found it

                }

            }

        return -1 ;  // didn't find it

        }   // end getIndexOf()


    /**
     * test the {@code ArrayBag}'s capacity for (lack of) room to add another
     * entry
     *
     * @return {@code true} if all elements of the array are in use,
     *     {@code false} if there's at least one unused element
     */
    private boolean isArrayFull()
        {

        // assertion: our state is valid

        // check for available space in the array
        return this.bag.length == this.numberOfEntries ;

        }   // end isArrayFull()


    /**
     * remove and return the entry at the specified index
     *
     * @param givenIndex
     *     the index of the entry to remove/return
     *
     * @return the removed entry or {@code null} if {@code givenIndex} is
     *     negative
     */
    private T removeEntry( final int givenIndex )
        {

        if ( isEmpty() || ( givenIndex < 0 ) )
            {

            // nothing to remove
            return null ;

            }

        // save the entry at the specified index
        final T result = this.bag[ givenIndex ] ;

        // replace its reference with the last one then remove the last one
        this.bag[ givenIndex ] = this.bag[ this.numberOfEntries - 1 ] ;
        this.bag[ this.numberOfEntries - 1 ] = null ;
        this.numberOfEntries-- ;

        return result ;

        }   // end removeEntry()


    /*
     * testing/debugging
     */


    /**
     * test driver
     *
     * @param args
     *     -unused-
     */
    public static void main( final String[] args )
        {

        // make sure specified remove correctly removes last item

        final BagInterface<String> testBag = new ArrayBag<>( 4 ) ;
        final String format = "%n%s:%n\t%s%n%n" ;

        testBag.add( "A" ) ;
        testBag.add( "B" ) ;
        testBag.add( "C" ) ;

        System.out.printf( format, "A, B, C", testBag ) ;

        System.out.printf( "remove %s%n", "C" ) ;
        testBag.remove( "C" ) ;

        System.out.printf( format, "A, B", testBag ) ;


        testBag.clear() ;

        testBag.add( "A" ) ;
        testBag.add( "B" ) ;
        testBag.add( "C" ) ;

        System.out.printf( format, "A, B, C", testBag ) ;

        System.out.printf( "remove %s%n", "B" ) ;
        testBag.remove( "B" ) ;

        System.out.printf( format, "A, C", testBag ) ;


        testBag.clear() ;

        testBag.add( "A" ) ;
        testBag.add( "B" ) ;
        testBag.add( "C" ) ;

        System.out.printf( format, "A, B, C", testBag ) ;

        System.out.printf( "remove %s%n", "A" ) ;
        testBag.remove( "A" ) ;

        System.out.printf( format, "C, B", testBag ) ;


        testBag.clear() ;

        testBag.add( "A" ) ;
        testBag.add( "B" ) ;
        testBag.add( "C" ) ;

        System.out.printf( format, "A, B, C", testBag ) ;

        System.out.printf( "remove %s%n", "A, B, C" ) ;
        testBag.remove( "A" ) ;
        testBag.remove( "B" ) ;
        testBag.remove( "C" ) ;

        System.out.printf( format, "[empty]", testBag ) ;

        }   // end main()

    }   // end class ArrayBag