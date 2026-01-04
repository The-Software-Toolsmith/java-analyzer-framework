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


package edu.wit.scds.ds.lists ;

import java.util.Iterator ;
import java.util.ListIterator ;

/**
 * An interface for the ADT list. Entries in a list have positions that begin with 1. This
 * implementation is iterable.
 *
 * @author Frank M. Carrano
 * @author Timothy M. Henry
 *
 * @version 5.0
 *
 * @param <T>
 *     The class of items the List will reference.
 * 
 * @author David M Rosenberg
 *
 * @version 5.1 2025-02-18 reorder method declarations
 * @version 6.0 2025-02-28
 *     <ul>
 *     <li>merge in iterable declarations
 *     <li>add declarations for lab assignment
 *     </ul>
 */
public interface ListInterface<T extends Comparable<? super T>> extends Iterable<T>
    {

    /**
     * Adds a new entry to the end of this list. Entries currently in the list are unaffected. The
     * list's size is increased by 1.
     *
     * @param newEntry
     *     The object to be added as a new entry.
     * 
     * @since 1.0
     */
    public void add( T newEntry ) ;


    /**
     * Adds a new entry at a specified position within this list. Entries originally at and above
     * the specified position are at the next higher position within the list. The list's size is
     * increased by 1.
     *
     * @param newPosition
     *     An integer that specifies the desired position of the new entry.
     * @param newEntry
     *     The object to be added as a new entry.
     *
     * @throws IndexOutOfBoundsException
     *     if either newPosition < 1 or newPosition > getLength() + 1.
     * 
     * @since 1.0
     */
    public void add( int newPosition,
                     T newEntry ) ;


    /**
     * Removes all entries from this list.
     * 
     * @since 1.0
     */
    public void clear() ;


    /**
     * Sees whether this list contains a given entry.
     *
     * @param anEntry
     *     The object that is the desired entry.
     *
     * @return True if the list contains anEntry, or false if not.
     * 
     * @since 1.0
     */
    public boolean contains( T anEntry ) ;


    /**
     * Retrieves the entry at a given position in this list.
     *
     * @param givenPosition
     *     An integer that indicates the position of the desired entry.
     *
     * @return A reference to the indicated entry.
     *
     * @throws IndexOutOfBoundsException
     *     if either givenPosition < 1 or givenPosition > getLength().
     * 
     * @since 1.0
     */
    public T getEntry( int givenPosition ) ;

    
    /**
     * creates a new {@code ListIterator}
     *
     * @return the new {@code ListIterator}
     *
     * @since 6.0
     */
    public default ListIterator<T> getIterator()
        {

        throw new UnsupportedOperationException( "getIterator()" ) ;

        }   // end default getIterator()


    /**
     * Gets the length of this list.
     *
     * @return The integer number of entries currently in the list.
     * 
     * @since 1.0
     */
    public int getLength() ;


    /**
     * Sees whether this list is empty.
     *
     * @return True if the list is empty, or false if not.
     * 
     * @since 1.0
     */
    public boolean isEmpty() ;


    /**
     * Removes the entry at a given position from this list. Entries originally at positions higher
     * than the given position are at the next lower position within the list, and the list's size
     * is decreased by 1.
     *
     * @param givenPosition
     *     An integer that indicates the position of the entry to be removed.
     *
     * @return A reference to the removed entry.
     *
     * @throws IndexOutOfBoundsException
     *     if either givenPosition < 1 or givenPosition > getLength().
     * 
     * @since 1.0
     */
    public T remove( int givenPosition ) ;


    /**
     * Replaces the entry at a given position in this list.
     *
     * @param givenPosition
     *     An integer that indicates the position of the entry to be replaced.
     * @param newEntry
     *     The object that will replace the entry at the position givenPosition.
     *
     * @return The original entry that was replaced.
     *
     * @throws IndexOutOfBoundsException
     *     if either givenPosition < 1 or givenPosition > getLength().
     * 
     * @since 1.0
     */
    public T replace( int givenPosition,
                      T newEntry ) ;


    /**
     * Retrieves all entries that are in this list in the order in which they occur in the list.
     *
     * @return A newly allocated array of all the entries in the list. If the list is empty, the
     *     returned array is empty.
     * 
     * @since 1.0
     */
    public T[] toArray() ;


    /**
     * Rearranges the entries in this list in (pseudo-)random order using an algorithm which
     * approximates shuffling a deck of cards.
     * 
     * @since 5.3
     */
    public default void shuffle()
        {
        throw new UnsupportedOperationException( "shuffle() is not supported" ) ;

        }   // end default shuffle()


    /**
     * Rearranges the entries in this list according to the objects' natural ordering. This sort is
     * stable.
     * 
     * @since 5.3
     */
    public default void sort()
        {
        throw new UnsupportedOperationException( "sort() is not supported" ) ;

        }   // end default sort()


    @Override
    public default Iterator<T> iterator()
        {
        throw new UnsupportedOperationException( " iterator() is not supported" ) ;

        }   // end default iterator()

    } // end interface ListInterface