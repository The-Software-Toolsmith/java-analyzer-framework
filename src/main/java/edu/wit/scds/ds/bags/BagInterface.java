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

/**
 * An interface that describes the operations of a bag of objects.
 *
 * @author Frank M. Carrano
 * @author Timothy M. Henry
 *
 * @version 5.0
 *
 * @author David M Rosenberg
 *
 * @version 5.1 reformat per class standard
 * @version 6.0 2024-09-17 rename from {@code BagInterface} to {@code UnorderedListInterface}
 * @version 6.1 2024-10-19 add 'enhanced' methods for lab assignment
 * @version 7.0 2025-01-28 rename back to {@code BagInterface}
 *
 * @param <T>
 *     The class of item a Bag can hold.
 */
public interface BagInterface<T>
    {

    /**
     * Adds a new entry to this bag.
     *
     * @param newEntry
     *     The object to be added as a new entry.
     *
     * @return true if the addition is successful, or false if not.
     *
     * @since 5.0
     */
    public boolean add( T newEntry ) ;


    /**
     * Determine if the data in this (context) instance and the data in the argument instance are
     * disjoint sets. The conditions for our data set being a disjoint set from the other bag are:
     * <ul>
     * <li>we're empty and the other bag isn't empty
     * <li>we're not empty and the other bag is {@code null} or empty
     * <li>we have no data in common with the other bag
     * </ul>
     * The bags are considered not disjoint sets if we are empty and the other bag is {@code null}
     * or empty.
     * <p>
     * Post-condition: the contents of {@code this} bag are unchanged.
     *
     * @param anotherBag
     *     The bag containing the data set to evaluate as the superset
     *     <p>
     *     Post-condition: the contents of {@code anotherBag} are unchanged.
     *
     * @return TODO
     *
     * @since 6.1
     */
    public default boolean areDisjointSets( final BagInterface<T> anotherBag )
        {

        throw new UnsupportedOperationException() ;

        }   // end default areDisjointSets()


    /**
     * Removes all entries from this bag.
     */
    public void clear() ;


    /**
     * Tests whether this bag contains a given entry.
     *
     * @param anEntry
     *     The entry to find.
     *
     * @return true if the bag contains the specified entry, or false if not.
     *
     * @since 5.0
     */
    public boolean contains( T anEntry ) ;


    /**
     * Creates a new bag of objects that would be left in this bag after removing those that also
     * occur in {@code anotherBag}.
     * <p>
     * Post-condition: the contents of {@code this} bag are unchanged.
     *
     * @param anotherBag
     *     The bag containing objects to be removed.
     *     <p>
     *     Post-condition: the contents of {@code anotherBag} are unchanged.
     *
     * @return a new, combined bag.
     */
    public default BagInterface<T> difference( final BagInterface<T> anotherBag )
        {

        throw new UnsupportedOperationException() ;

        }   // end default difference()


    /**
     * Gets the current number of entries in this bag.
     *
     * @return The integer number of entries currently in the bag.
     *
     * @since 5.0
     */
    public int getCurrentSize() ;


    /**
     * Counts the number of times a given entry appears in this bag.
     *
     * @param anEntry
     *     The entry to be counted.
     *
     * @return The number of times the specified entry appears in the bag.
     *
     * @since 5.0
     */
    public int getFrequencyOf( T anEntry ) ;


    /**
     * Creates a new bag that contains those objects that occur in both {@code this} bag and
     * {@code anotherBag}.
     * <p>
     * Post-condition: the contents of {@code this} bag are unchanged.
     *
     * @param anotherBag
     *     The bag containing objects to be compared.
     *     <p>
     *     Post-condition: the contents of {@code anotherBag} are unchanged.
     *
     * @return A new, combined bag.
     */
    public default BagInterface<T> intersection( final BagInterface<T> anotherBag )
        {

        throw new UnsupportedOperationException() ;

        }   // end default intersection()


    /**
     * Sees whether this bag is empty.
     *
     * @return true if the bag is empty, or false if not.
     *
     * @since 5.0
     */
    public boolean isEmpty() ;


    /**
     * Determine if the data in this (context) instance is a proper subset of the data in the
     * argument instance. The conditions for our data set being a proper subset of the other bag are
     * any of:
     * <ul>
     * <li>we're empty and the other bag isn't empty
     * <li>all of our data is also in the other bag and the other bag has at least one additional
     * item we don't have
     * </ul>
     * We are not considered a proper subset of the other bag if any of:
     * <ul>
     * <li>the other bag is {@code null} or empty
     * <li>the other bag doesn't contain more data than we do
     * <li>the two bags have identical contents
     * </ul>
     * <p>
     * Post-condition: the contents of {@code this} bag are unchanged.
     *
     * @param anotherBag
     *     The bag containing data set to evaluate as the proper superset
     *     <p>
     *     Post-condition: the contents of {@code anotherBag} are unchanged.
     *
     * @return TODO
     *
     * @since 6.1
     */
    public default boolean isProperSubsetOf( final BagInterface<T> anotherBag )
        {

        throw new UnsupportedOperationException() ;

        }   // end default isProperSubsetOf()


    /**
     * Determine if the data in this (context) instance is a subset of the data in the argument
     * instance. The conditions for our data set being a subset of the other bag are any of:
     * <ul>
     * <li>we're empty
     * <li>all of our data is also in the other bag
     * </ul>
     * We are not considered a subset of the other bag if any of:
     * <ul>
     * <li>we aren't empty and the other bag is {@code null} or empty
     * <li>the other bag doesn't contain at least as much data as we do
     * <li>at least one element in our bag isn't in the other bag
     * </ul>
     * <p>
     * Post-condition: the contents of {@code this} bag are unchanged.
     *
     * @param anotherBag
     *     The bag containing data set to evaluate as the superset
     *     <p>
     *     Post-condition: the contents of {@code anotherBag} are unchanged.
     *
     * @return TODO
     *
     * @since 6.1
     */
    public default boolean isSubsetOf( final BagInterface<T> anotherBag )
        {

        throw new UnsupportedOperationException() ;

        }   // end default isSubsetOf()


    /**
     * Removes one unspecified entry from this bag, if possible.
     *
     * @return Either the removed entry, if the removal was successful, or null.
     *
     * @since 5.0
     */
    public T remove() ;


    /**
     * Removes one occurrence of a given entry from this bag, if possible.
     *
     * @param anEntry
     *     The entry to be removed.
     *
     * @return true if the removal was successful, or false if not.
     *
     * @since 5.0
     */
    public boolean remove( T anEntry ) ;


    /**
     * Retrieves all entries that are in this bag.
     *
     * @return A newly allocated array of all the entries in the bag. Note: If the bag is empty, the
     *     returned array is empty.  The array is sized to match the number of entries in the bag.
     *
     * @since 5.0
     */
    public T[] toArray() ;


    /**
     * Creates a new bag that combines the contents of {@code this} bag and {@code anotherBag}.
     * <p>
     * Post-condition: the contents of {@code this} bag are unchanged.
     *
     * @param anotherBag
     *     The bag that is to be added.
     *     <p>
     *     Post-condition: the contents of {@code anotherBag} are unchanged.
     *
     * @return A new, combined bag.
     */
    public default BagInterface<T> union( final BagInterface<T> anotherBag )
        {

        throw new UnsupportedOperationException() ;

        }   // end default union()

    } // end interface BagInterface