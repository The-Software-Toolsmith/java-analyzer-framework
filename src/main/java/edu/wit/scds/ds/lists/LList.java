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

import edu.wit.scds.ds.buildingblocks.enhanced.Node ;
import edu.wit.scds.ds.buildingblocks.utilities.VisualizationSupport ;

import java.util.Iterator ;
import java.util.NoSuchElementException ;


/**
 * A class that implements the ADT list by using a chain of nodes that has both
 * a head reference and a tail reference. This implementation is
 * {@code Iterable} using an inner class iterator. Two additional methods may be
 * implemented: {@code sort()} and {@code shuffle()}.
 * <p>
 * This implementation
 * <ul>
 * <li>numbers entries starting at one (1)
 * <li>will not accept {@code null} data
 * </ul>
 *
 * @author Frank M. Carrano
 * @author Timothy M. Henry
 *
 * @version 4.0
 * @version 5.0
 *
 * @author David M Rosenberg
 *
 * @version 4.1 2018-07-11
 *     <ul>
 *     <li>initial version based upon Carrano and Henry implementation in the
 *     4th edition of the textbook
 *     <li>modified per assignment
 *     </ul>
 *     <br>
 * @version 5.1 2019-07-14
 *     <ul>
 *     <li>revise to match the 5th edition of the textbook
 *     <li>revise to match this semester's assignment
 *     <li>switch from 1-based to 0-based positions
 *     </ul>
 *     <br>
 * @version 5.2 2019-11-12 track revised assignment for this semester<br>
 * @version 5.3 2020-07-17
 *     <ul>
 *     <li>move {@code Node} inner class to separate class
 *     <li>update to coding standard
 *     <li>streamline some code
 *     <li>add array constructor
 *     <li>prevent {@code add(null)}
 *     <li>switch to {@code EnhancedListInterface} from {@code ListInterface}
 *     and {@code Iterable}
 *     </ul>
 *     <br>
 * @version 5.4 2020-11-09
 *     <ul>
 *     <li>track changes/consolidation to {@code ListInterface}
 *     <ul>
 *     <li>remove explicit {@code implements Iterable}
 *     <li>rename class from {@code EnhancedLinkedList} to {@code LinkedList}
 *     </ul>
 *     <li>replace {@code toString()} and implement {@code main()} as a
 *     testing/debugging aid
 *     </ul>
 *     <br>
 * @version 5.5 2020-11-11 enhance {@code insertInOrder()} to achieve O(1)
 *     efficiency when list is already sorted<br>
 * @version 5.6 2020-11-21 enhance {@code chainToString()} to detect a loop in
 *     the chain<br>
 * @version 5.7 2021-07-30 remove {@code chainToString()} - use {@code Node}'s
 *     implementation<br>
 * @version 5.8 2021-11-16 reorganize methods to terminate immediately if given
 *     an invalid position<br>
 * @version 5.9 2021-07-30 implement {@code insertionSort()},
 *     {@code insertInOrder()}, {@code shuffle()}, {@code interleave()}, and
 *     {@code appendNode()}<br>
 * @version 5.9.1 2021-08-15 add another example condition in
 *     {@code insertInOrder()} wrt updating {@code this.lastNode}<br>
 * @version 5.10 2021-11-18 mostly cosmetic changes to {@code toString()} and
 *     {@code main()} to display {@code null} references as "&bullet;" for
 *     greater consistency with in-class demos<br>
 * @version 5.10.1 2021-12-06
 *     <ul>
 *     <li>correct comment in {@code shuffle()}
 *     <li>indent contents of chain in {@code toString()}
 *     </ul>
 *     <br>
 * @version 5.11 2022-11-09
 *     <ul>
 *     <li>small modifications/optimizations
 *     <li>move inner class to end of file
 *     </ul>
 *     <br>
 * @version 5.12 2022-11-11 remove portions of implementation for assignment<br>
 * @version 5.13 2023-04-02
 *     <ul>
 *     <li>rename {@code initializeDataFields()} to {@code initializeState()}
 *     <li>minor modifications to improve consistency from method to method
 *     </ul>
 *     <br>
 * @version 5.14 2024-11-17 enhance messages in {@code main()} to alert user to
 *     whether {@code this.numberOfEntries} is meaningful<br>
 * @version 6.0 2025-03-01
 *     <ul>
 *     <li>rename from {@code LinkedList} to {@code LList}
 *     <li>add {@code SHUFFLE_PASSES}
 *     <li>adjustments per lab 2 assignment
 *     </ul>
 *     <br>
 * @version 6.0.1 2025-03-13 correct comment in {@code shuffle()} wrt number of
 *     passes
 * @version 6.0.2 2025-03-18 minor format change in {@code toString()}
 * @version 6.1 2025-07-08 restart numbering {@code Node}s for each test group
 *     in {@code main()}
 * @version 6.1.1 2025-12-10 minor clarifications to sorting and shuffling
 *
 * @param <T>
 *     represents the class of objects the application will give us to store
 *     <p>
 *     note: {@code T} must be comparable to support sorting
 */
public class LList<T extends Comparable<? super T>> implements ListInterface<T>
    {

    /*
     * constants
     */

    /** number of passes for {@code interleave()} */
    private final static int SHUFFLE_PASSES = 3 ;


    /*
     * instance state / data fields
     */

    private Node<T> firstNode ;
    private Node<T> lastNode ;

    private int numberOfEntries ;


    /*
     * constructor(s)
     */


    /**
     * initialize instance to a valid empty state
     */
    public LList()
        {

        initializeState() ;

        }   // end no-arg constructor


    /*
     * API methods
     */


    /**
     * Initialize a new LList and populate it with the contents of
     * {@code initialContents}
     *
     * @param initialContents
     *     an array of zero or more entries to copy to the newly instantiated
     *     LList - it could be null
     */
    public LList( final T[] initialContents )
        {

        // initialize state: empty
        this() ;

        // if an array is available, add its contents to 'this' in the same
        // order
        if ( initialContents != null )
            {

            for ( final T initialItem : initialContents )
                {
                this.add( initialItem ) ;
                }

            }

        }   // end array constructor


    /*
     * API methods
     */


    @Override
    public void add( final T newEntry )
        {

        add( this.numberOfEntries + 1, newEntry ) ;

        }   // end add() at end of list


    @Override
    public void add( final int givenPosition,
                     final T newEntry )
        {

        // validate the position
        if ( ( givenPosition < 1 ) || ( givenPosition > ( this.numberOfEntries + 1 ) ) )
            {
            throw new IndexOutOfBoundsException( givenPosition ) ;
            }

        // assertion: we have a valid position

        // don't store null
        if ( newEntry == null )
            {
            throw new IllegalArgumentException( "new entry cannot be null" ) ;
            }

        // explicit specification of next reference being null
        final Node<T> newNode = new Node<>( newEntry, null ) ;

        // we have 'meaningful' data to store
        // four scenarios for where/how to add
        if ( isEmpty() )
            {
            // list is empty - new entry will be first and last

            this.firstNode = newNode ;
            this.lastNode = newNode ;
            }
        else if ( givenPosition == 1 )
            {
            // there's at least 1 entry in the list

            // adding in the first position/at the beginning of the chain
            newNode.setNext( this.firstNode ) ;
            this.firstNode = newNode ;
            }
        else if ( givenPosition == ( this.numberOfEntries + 1 ) )
            {
            // adding at the end of the list
            this.lastNode.setNext( newNode ) ;
            this.lastNode = newNode ;
            }
        else
            {
            // inserting between two existing entries

            // reference the node immediately prior to the insertion point
            final Node<T> nodeBefore = getNodeAt( givenPosition - 1 ) ;

            // insert the new node into the chain
            newNode.setNext( nodeBefore.getNext() ) ;
            nodeBefore.setNext( newNode ) ;
            }

        this.numberOfEntries++ ;

        }   // end add() at specified position


    @Override
    public void clear()
        {

        initializeState() ;

        }   // end clear()


    @Override
    public boolean contains( final T anEntry )
        {

        // if we get a non-null result, we found a match
        return getReferenceTo( anEntry ) != null ;

        }   // end contains()


    @Override
    public T getEntry( final int givenPosition )
        {

        // validate the position
        if ( ( givenPosition < 1 ) || ( givenPosition > this.numberOfEntries ) )
            {
            throw new IndexOutOfBoundsException( givenPosition ) ;
            }

        // we have a valid position
        return getNodeAt( givenPosition ).getData() ;

        }   // end getEntry()


    @Override
    public int getLength()
        {

        return this.numberOfEntries ;

        }   // end getLength()


    @Override
    public boolean isEmpty()
        {

        return this.numberOfEntries == 0 ;

        }   // end isEmpty()


    @Override
    public Iterator<T> iterator()
        {

        return new LListIterator() ;

        }   // end iterator()


    @Override
    public T remove( final int givenPosition )
        {

        // validate the position
        if ( ( givenPosition < 1 ) || ( givenPosition > this.numberOfEntries ) )
            {
            throw new IndexOutOfBoundsException( givenPosition ) ;
            }

        // assertion: givenPosition is valid

        final T removedEntry ;

        if ( givenPosition == 1 )
            {
            // removing from the first position/at the beginning of the chain
            removedEntry = this.firstNode.getData() ;
            this.firstNode = this.firstNode.getNext() ;

            if ( this.numberOfEntries == 1 )
                {
                // removed the only entry in the list
                this.lastNode = null ;
                }

            }
        else
            {
            // removing beyond the first position

            // reference the node immediately prior to the insertion point
            final Node<T> nodeBefore = getNodeAt( givenPosition - 1 ) ;
            final Node<T> nodeToRemove = nodeBefore.getNext() ;
            final Node<T> nodeAfter = nodeToRemove.getNext() ;

            removedEntry = nodeToRemove.getData() ;
            nodeBefore.setNext( nodeAfter ) ;

            if ( givenPosition == this.numberOfEntries )
                {
                // removed the last entry in the list
                this.lastNode = nodeBefore ;
                }

            }

        this.numberOfEntries-- ;

        return removedEntry ;

        }   // end remove()


    @Override
    public T replace( final int givenPosition,
                      final T newEntry )
        {

        // validate the position
        if ( ( givenPosition < 1 ) || ( givenPosition > this.numberOfEntries ) )
            {
            throw new IndexOutOfBoundsException( givenPosition ) ;
            }

        // assertion: givenPosition is valid

        // don't store null
        if ( newEntry == null )
            {
            throw new IllegalArgumentException( "new entry cannot be null" ) ;
            }

        final Node<T> foundNode = getNodeAt( givenPosition ) ;

        final T oldEntry = foundNode.getData() ;
        foundNode.setData( newEntry ) ;

        return oldEntry ;

        }   // end replace()


    @Override
    public void shuffle()
        {
        /* @formatter:off
         *
         * To shuffle the contents of the list:
         * - split the list into two pieces; interleave the two halves, one entry at a
         *      time starting with the entry at the beginning of the first half of the list
         * - split the list into two pieces; interleave the two halves, one entry at a
         *      time starting with the entry in the middle of the list (at the beginning
         *      of the second half)
         * - split the list into two pieces; interleave the two halves, one entry at a
         *      time starting with the entry at the beginning of the first half of the list
         *
         * You are not permitted to:
         * - copy the contents of the chain into an array
         * - use any Java Class Library classes/methods
         * - use any LinkedList methods other than getNodeAt() and interleave()
         *
         * Your implementation of appendNode() can significantly affect the efficiency of
         * shuffle().  You must implement it with O(1) efficiency so the overall efficiency
         * is O(n), not O(n²) (or worse).
         *
         * @formatter:on
         */

        // if there aren't at least two entries, there's nothing for us to do
        if ( this.numberOfEntries < 2 )
            {
            return ;
            }   // nothing to do if empty or only one element

        // do the specified number of passes: split then interleave
        for ( int pass = 1 ; pass <= SHUFFLE_PASSES ; pass++ )
            {
            /*
             * split the chain into two sub-chains
             */

            // first half starts at the beginning of the chain
            final Node<T> firstHalf = this.firstNode ;

            // get the Node immediately before the midpoint
            final Node<T> tempNode = getNodeAt( this.numberOfEntries / 2 ) ;

            // the second half starts following it
            final Node<T> secondHalf = tempNode.getNext() ;

            // disconnect the two sub-chains
            tempNode.setNext( null ) ;

            /*
             * remove all references to the chain from the instance
             */
            this.firstNode = null ;
            this.lastNode = null ;

            /* @formatter:off
             *
             * - interleave the two sub-chains back into the instance chain
             * - alternate first/second half each time through the loop
             * - test is safe because pass is always positive
             *
             * @formatter:on
             */
            if ( ( pass % 2 ) == 1 )
                {
                interleave( firstHalf, secondHalf ) ;
                }
            else
                {
                interleave( secondHalf, firstHalf ) ;
                }

            // assertion: this.lastNode.getNext() is null
            
            }   // end for() multiple passes

        }   // end shuffle()


    /**
     * Utility method to re-populate the instance chain from the two sub-chains.
     * <p>
     * Preconditions:
     * <ul>
     * <li>the data fields ({@code firstNode} and {@code lastNode}) are both
     * {@code null}
     * <li>both sub-chains are {@code null}-terminated
     * </ul>
     *
     * @param firstPart
     *     a reference to one sub-chain containing zero or more elements in the
     *     instance - reconstruction of the instance chain will begin with the
     *     first entry in this sub-chain (if any)
     * @param secondPart
     *     a reference to the other sub-chain - also contains zero or more
     *     elements
     */
    private void interleave( Node<T> firstPart,
                             Node<T> secondPart )
        {
        // assertion: firstPart and secondPart are completely disconnected and
        // are both null-terminated

        Node<T> nodeToAppend ;
        
        do
            {
            /*
             * for each sub-chain, if it contains another Node, append its now
             * first Node to the instance's chain then advance to the next Node
             */

            if ( firstPart != null )
                {
                nodeToAppend = firstPart ;
                firstPart = firstPart.getNext() ;
                nodeToAppend.setNext( null ) ;
                appendNode( nodeToAppend ) ;
                }

            if ( secondPart != null )
                {
                nodeToAppend = secondPart ;
                secondPart = secondPart.getNext() ;
                nodeToAppend.setNext( null ) ;
                appendNode( nodeToAppend ) ;
                }

            }
        while ( ( firstPart != null ) || ( secondPart != null ) ) ;

        /*
         * assertion: this.lastNode's next is null because it was at the end of
         * one of the sub-chains
         */

        }   // end interleave()


    /**
     * Add a {@code Node} to the end of the instance's chain
     * <p>
     * This method operates with O(1) efficiency.
     *
     * @param aNode
     *     the {@code Node} to add
     */
    private void appendNode( final Node<T> aNode )
        {
        
        // assertion: aNode is completely disconnected from the chain

        if ( this.lastNode != null )
            {
            // chain isn't empty - just add to end of chain
            this.lastNode.setNext( aNode ) ;
            }
        else
            {
            // the chain is empty - this Node is the first one in the chain
            this.firstNode = aNode ;
            }

        // this Node is the last one in the chain
        this.lastNode = aNode ;

        }   // end appendNode()


    @Override
    public void sort()
        {

        // only need to sort if have multiple entries
        if ( this.numberOfEntries > 1 )
            {
            insertionSort() ;
            }

        }   // end sort()


    /**
     * A linked implementation of a stable, iterative, insertion sort.
     * <p>
     * Precondition: There are at least 2 elements in this list.
     */
    private void insertionSort()
        {
        /* @formatter:off
         *
         * - use a rudimentary insertion sort
         * - your sort must be stable
         * - you are not permitted to copy the contents of the chain into an array
         * - you are not permitted to use any Java Class Library classes
         *
         * @formatter:on
         */

        // assertion: there are at least 2 nodes in the chain
        // note: if we replace the do/while with a while, we can cleanly handle
        // a chain with a single node
        
        // assertion: the chain is null-terminated

        // Break chain into 2 pieces: sorted and unsorted
        Node<T> unsortedPart = this.firstNode.getNext() ;

        // assertion: unsortedPart != null

        // fix pointers for the sorted portion of the chain
        this.firstNode.setNext( null ) ;
        this.lastNode = this.firstNode ;

        // take the next item from the front of the unsorted portion and insert
        // it into the sorted portion at its correct location
        do
            {
            final Node<T> nodeToInsert = unsortedPart ;
            unsortedPart = unsortedPart.getNext() ;
            nodeToInsert.setNext( null ) ;

            insertInOrder( nodeToInsert ) ;
            }   // end do/while
        while ( unsortedPart != null ) ;

        }   // end insertionSort()


    /**
     * Utility method to insert a {@code Node} in its correct position in the
     * chain based upon T's natural ordering. This sort is stable.
     * <p>
     * Precondition: There is at least 1 element in this list.
     *
     * @param nodeToInsert
     *     the {@code Node} to insert
     */
    private void insertInOrder( final Node<T> nodeToInsert )
        {

        final T item = nodeToInsert.getData() ;

        // ENHANCEMENT short-circuit append to end of list if already sorted
        if ( item.compareTo( this.lastNode.getData() ) >= 0 )
            {
            this.lastNode.setNext( nodeToInsert ) ;
            this.lastNode = nodeToInsert ;

            return ;    // done
            }
        // end ENHANCEMENT

        Node<T> currentNode = this.firstNode ;
        Node<T> previousNode = null ;

        // Locate insertion point
        while ( ( currentNode != null ) && ( item.compareTo( currentNode.getData() ) >= 0 ) )
            {
            previousNode = currentNode ;
            currentNode = currentNode.getNext() ;
            }   // end while

        // Make the insertion
        if ( previousNode != null )
            {   // Insert between previousNode and currentNode
            nodeToInsert.setNext( currentNode ) ;
            previousNode.setNext( nodeToInsert ) ;
            }
        else    // insert at beginning
            {
            nodeToInsert.setNext( this.firstNode ) ;
            this.firstNode = nodeToInsert ;
            }   // end if

// @formatter:off
//        // the following will never execute with the above ENHANCEMENT
//        // if added at end of chain, update lastNode - several ways to determine this:
//        if ( previousNode == this.lastNode )
//        if ( this.lastNode.getNext() == nodeToInsert )
//        if ( nodeToInsert.getNext() == null )
//        if ( currentNode == null )
//            {
//            this.lastNode = nodeToInsert ;
//            }
// @formatter:on

        }   // end insertInOrder()


    @Override
    public T[] toArray()
        {

        // the cast is safe because the array is null filled
        @SuppressWarnings( "unchecked" )
        final T[] result = (T[]) new Object[ this.numberOfEntries ] ;

        Node<T> currentNode = this.firstNode ;
        int i = 0 ;

        while ( currentNode != null )
            {
            result[ i ] = currentNode.getData() ;

            // move to the next node and next position in the array
            currentNode = currentNode.getNext() ;
            i++ ;
            }

        return result ;

        }   // end toArray()


    @Override
    public String toString()
        {

        // toString() is generally not implemented - I provided it as a
        // debugging aid
        return String.format( "numberOfEntries: %,d;%n\tfirstNode: %s; lastNode: %s%n\tchain: %s",
                              this.numberOfEntries,
                              this.firstNode == null
                                      ? VisualizationSupport.NULL_REFERENCE_MARKER
                                      : this.firstNode,
                              this.lastNode == null
                                      ? VisualizationSupport.NULL_REFERENCE_MARKER
                                      : this.lastNode,
                              Node.chainToString( this.firstNode ) ) ;

        }   // end toString()


    /*
     * private utility methods
     */


    /**
     * get a reference to the node at a given position
     *
     * @param givenPosition
     *     the position we want
     *
     * @return a reference to the Node at the givenPosition
     */
    private Node<T> getNodeAt( final int givenPosition )
        {

        // assertion: givenPosition is valid

        // do we want the last entry?
        if ( givenPosition == this.numberOfEntries )
            {
            // position is the last entry
            return this.lastNode ;
            }

        // need to traverse the chain to get to givenPosition

        Node<T> currentNode = this.firstNode ;

        for ( int i = 1 ; i < givenPosition ; i++ )
            {
            currentNode = currentNode.getNext() ;
            }

        return currentNode ;

        }   // end getNodeAt()


    /**
     * locate the first {@code Node} containing the specified entry
     *
     * @param anEntry
     *     the entry to locate
     *
     * @return a reference to the first {@code Node} containing the entry or
     *     {@code null} if the entry isn't found
     */
    private Node<T> getReferenceTo( final T anEntry )
        {

        // don't bother looking for null - we won't store them
        if ( anEntry == null )
            {
            return null ;
            }

        // traverse the chain looking for a match
        Node<T> currentNode = this.firstNode ;

        while ( currentNode != null )
            {

            if ( currentNode.getData().equals( anEntry ) )
                {
                // found a match
                return currentNode ;
                }   // end if

            currentNode = currentNode.getNext() ;
            }   // end while

        // didn't find a match
        return null ;

        }   // end getReferenceTo()


    /**
     * (re)set the instance state to valid empty
     *
     * @since 4.1
     */
    private void initializeState()
        {

        this.firstNode = null ;
        this.lastNode = null ;
        this.numberOfEntries = 0 ;

        }   // end initializeState()


    /*
     * for testing/debugging
     */


    /**
     * Test driver
     *
     * @param args
     *     -unused-
     */
    public static void main( final String[] args )
        {
        // for testing/debugging

        System.out.printf( "Testing appendNode():%n%n" ) ;
        System.out.printf( "NOTE: this.numberOfEntries will remain at 0%n%n" ) ;

        LList<String> demoList = new LList<>() ;
        System.out.printf( "empty list:%n%s%n%n", demoList ) ;

        demoList.appendNode( new Node<>( "A" ) ) ;
        System.out.printf( "append 1st item (A):%n%s%n%n", demoList ) ;

        demoList.appendNode( new Node<>( "B" ) ) ;
        System.out.printf( "append 2nd item (B):%n%s%n%n", demoList ) ;

        demoList.appendNode( new Node<>( "C" ) ) ;
        System.out.printf( "append 3rd item (C):%n%s%n%n", demoList ) ;


        System.out.printf( "%n================%n%n" ) ;

        // restart numbering Nodes at 1
        Node.resetNextPseudoAddress() ;

        System.out.printf( "%nTesting interleave():%n%n" ) ;
        System.out.printf( "NOTE: this.numberOfEntries will remain at 0%n%n" ) ;

        demoList = new LList<>() ;
        System.out.printf( "empty list:%n%s%n%n", demoList ) ;

        Node<String> firstHalf = new Node<>( "A", new Node<>( "B" ) ) ;
        System.out.printf( "firstHalf: %s%n", Node.chainToString( firstHalf ) ) ;
        Node<String> secondHalf = new Node<>( "C", new Node<>( "D" ) ) ;
        System.out.printf( "secondHalf: %s%n%n", Node.chainToString( secondHalf ) ) ;
        demoList.interleave( firstHalf, secondHalf ) ;
        System.out.printf( "after interleave( firstHalf, secondHalf ):%n%s%n%n", demoList ) ;


        // restart numbering Nodes at 1
        Node.resetNextPseudoAddress() ;

        demoList = new LList<>() ;
        System.out.printf( "%nempty list:%n%s%n%n", demoList ) ;

        firstHalf = new Node<>( "A", new Node<>( "B" ) ) ;
        System.out.printf( "firstHalf: %s%n", Node.chainToString( firstHalf ) ) ;
        secondHalf = new Node<>( "C", new Node<>( "D" ) ) ;
        System.out.printf( "secondHalf: %s%n%n", Node.chainToString( secondHalf ) ) ;
        demoList.interleave( secondHalf, firstHalf ) ;
        System.out.printf( "after interleave( secondHalf, firstHalf ):%n%s%n%n", demoList ) ;


        // restart numbering Nodes at 1
        Node.resetNextPseudoAddress() ;

        demoList = new LList<>() ;
        System.out.printf( "%nempty list:%n%s%n%n", demoList ) ;

        firstHalf = new Node<>( "A", new Node<>( "B" ) ) ;
        System.out.printf( "firstHalf: %s%n", Node.chainToString( firstHalf ) ) ;
        secondHalf = new Node<>( "C", new Node<>( "D", new Node<>( "E" ) ) ) ;
        System.out.printf( "secondHalf: %s%n%n", Node.chainToString( secondHalf ) ) ;
        demoList.interleave( firstHalf, secondHalf ) ;
        System.out.printf( "after interleave( firstHalf, secondHalf ):%n%s%n%n", demoList ) ;


        // restart numbering Nodes at 1
        Node.resetNextPseudoAddress() ;

        demoList = new LList<>() ;
        System.out.printf( "%nempty list:%n%s%n%n", demoList ) ;

        firstHalf = new Node<>( "A", new Node<>( "B" ) ) ;
        System.out.printf( "firstHalf: %s%n", Node.chainToString( firstHalf ) ) ;
        secondHalf = new Node<>( "C", new Node<>( "D", new Node<>( "E" ) ) ) ;
        System.out.printf( "secondHalf: %s%n%n", Node.chainToString( secondHalf ) ) ;
        demoList.interleave( secondHalf, firstHalf ) ;
        System.out.printf( "after interleave( secondHalf, firstHalf ):%n%s%n%n", demoList ) ;


        // restart numbering Nodes at 1
        Node.resetNextPseudoAddress() ;

        demoList = new LList<>() ;
        System.out.printf( "%nempty list:%n%s%n%n", demoList ) ;

        firstHalf = new Node<>( "A", new Node<>( "B", new Node<>( "C" ) ) ) ;
        System.out.printf( "firstHalf: %s%n", Node.chainToString( firstHalf ) ) ;
        secondHalf = new Node<>( "D", new Node<>( "E" ) ) ;
        System.out.printf( "secondHalf: %s%n%n", Node.chainToString( secondHalf ) ) ;
        demoList.interleave( firstHalf, secondHalf ) ;
        System.out.printf( "after interleave( firstHalf, secondHalf ):%n%s%n%n", demoList ) ;


        // restart numbering Nodes at 1
        Node.resetNextPseudoAddress() ;

        demoList = new LList<>() ;
        System.out.printf( "%nempty list:%n%s%n%n", demoList ) ;

        firstHalf = new Node<>( "A", new Node<>( "B", new Node<>( "C" ) ) ) ;
        System.out.printf( "firstHalf: %s%n", Node.chainToString( firstHalf ) ) ;
        secondHalf = new Node<>( "D", new Node<>( "E" ) ) ;
        System.out.printf( "secondHalf: %s%n%n", Node.chainToString( secondHalf ) ) ;
        demoList.interleave( secondHalf, firstHalf ) ;
        System.out.printf( "after interleave( secondHalf, firstHalf ):%n%s%n%n", demoList ) ;


        // restart numbering Nodes at 1
        Node.resetNextPseudoAddress() ;

        demoList = new LList<>() ;
        System.out.printf( "%nempty list:%n%s%n%n", demoList ) ;

        firstHalf = new Node<>( "A", new Node<>( "B", new Node<>( "C", new Node<>( "D" ) ) ) ) ;
        System.out.printf( "firstHalf: %s%n", Node.chainToString( firstHalf ) ) ;
        secondHalf = new Node<>( "E", new Node<>( "F" ) ) ;
        System.out.printf( "secondHalf: %s%n%n", Node.chainToString( secondHalf ) ) ;
        demoList.interleave( firstHalf, secondHalf ) ;
        System.out.printf( "after interleave( firstHalf, secondHalf ):%n%s%n%n", demoList ) ;


        // restart numbering Nodes at 1
        Node.resetNextPseudoAddress() ;

        demoList = new LList<>() ;
        System.out.printf( "%nempty list:%n%s%n%n", demoList ) ;

        firstHalf = new Node<>( "A", new Node<>( "B", new Node<>( "C", new Node<>( "D" ) ) ) ) ;
        System.out.printf( "firstHalf: %s%n", Node.chainToString( firstHalf ) ) ;
        secondHalf = new Node<>( "E", new Node<>( "F" ) ) ;
        System.out.printf( "secondHalf: %s%n%n", Node.chainToString( secondHalf ) ) ;
        demoList.interleave( secondHalf, firstHalf ) ;
        System.out.printf( "after interleave( secondHalf, firstHalf ):%n%s%n%n", demoList ) ;


        System.out.printf( "%n================%n%n" ) ;


        // restart numbering Nodes at 1
        Node.resetNextPseudoAddress() ;

        System.out.printf( "%nTesting add() and remove():%n%n" ) ;
        System.out.printf( "NOTE: this.numberOfEntries will reflect the number of items in the list%n%n" ) ;

        demoList = new LList<>() ;
        System.out.printf( "empty list:%n%s%n%n", demoList ) ;

        demoList.add( "A" ) ;
        System.out.printf( "add 1st item (A) at end:%n%s%n%n", demoList ) ;

        demoList.add( 1, "B" ) ;
        System.out.printf( "add another item (B) at beginning (0):%n%s%n%n", demoList ) ;

        demoList.add( "C" ) ;
        System.out.printf( "add another item (C) at end:%n%s%n%n", demoList ) ;

        demoList.add( 3, "D" ) ;
        System.out.printf( "add another item (D) in between (2):%n%s%n%n", demoList ) ;

        demoList.remove( 1 ) ;
        System.out.printf( "remove the first item (0):%n%s%n%n", demoList ) ;

        final int listLength = demoList.getLength() ;
        demoList.remove( listLength - 1 ) ;
        System.out.printf( "remove the last item (%,d):%n%s%n%n", listLength - 1, demoList ) ;

        demoList.add( "E" ) ;
        demoList.add( "F" ) ;
        System.out.printf( "add more items (E, F) at end:%n%s%n%n", demoList ) ;

        demoList.remove( 2 ) ;
        System.out.printf( "remove an in-between item (1):%n%s%n%n", demoList ) ;


        System.out.printf( "%n================%n%n" ) ;


        // restart numbering Nodes at 1
        Node.resetNextPseudoAddress() ;

        System.out.printf( "%nTesting shuffle():%n%n" ) ;
        System.out.printf( "NOTE: this.numberOfEntries will reflect the number of items in the list%n%n" ) ;

        demoList = new LList<>() ;
        System.out.printf( "empty list:%n%s%n%n", demoList ) ;

        demoList.add( "A" ) ;
        demoList.add( "B" ) ;
        demoList.add( "C" ) ;
        demoList.add( "D" ) ;
        System.out.printf( "populated: A → B → C → D%n%s%n%n", demoList ) ;

        demoList.shuffle() ;
        System.out.printf( "shuffled:%n%s%n%n", demoList ) ;


        demoList = new LList<>() ;
        System.out.printf( "%nempty list:%n%s%n%n", demoList ) ;

        demoList.add( "D" ) ;
        demoList.add( "A" ) ;
        demoList.add( "C" ) ;
        demoList.add( "B" ) ;
        System.out.printf( "populated: D → A → C → B%n%s%n%n", demoList ) ;

        demoList.sort() ;
        System.out.printf( "sorted:%n%s%n%n", demoList ) ;


        System.out.printf( "%n================%n%n" ) ;


        // restart numbering Nodes at 1
        Node.resetNextPseudoAddress() ;

        System.out.printf( "%nTesting corrupted chains (with loops):%n%n" ) ;
        System.out.printf( "NOTE: ignore this.numberOfEntries%n%n" ) ;

        // create a loop
        demoList = new LList<>() ;
        System.out.printf( "empty list:%n%s%n%n", demoList ) ;

        demoList.add( "A" ) ;
        demoList.lastNode.setNext( demoList.firstNode ) ;
        System.out.printf( "with a loop (A → A...):%n%s%n%n", demoList ) ;

        // create a different loop
        demoList.lastNode.setNext( null ) ;
        demoList.add( "B" ) ;
        demoList.add( "C" ) ;
        demoList.add( "D" ) ;
        demoList.lastNode.setNext( demoList.firstNode ) ;
        System.out.printf( "with a loop (A → B → C → D → A...):%n%s%n%n", demoList ) ;

        // create a different loop
        demoList.lastNode.setNext( demoList.firstNode.getNext().getNext() ) ;
        System.out.printf( "with a loop (A → B → C → D → C...):%n%s%n%n", demoList ) ;

        // create a different loop
        demoList.lastNode.setNext( demoList.lastNode ) ;
        System.out.printf( "with a loop (A → B → C → D → D...):%n%s%n%n", demoList ) ;

        // create a different loop
        demoList.firstNode.setNext( demoList.firstNode ) ;
        System.out.printf( "with a loop (A → BA...):%n%s%n%n", demoList ) ;

        demoList.shuffle() ;
        System.out.printf( "shuffled:%n%s%n%n", demoList ) ;

        demoList.sort() ;
        System.out.printf( "sorted:%n%s%n%n", demoList ) ;

        }   // end main()


    /*
     * inner class(es)
     */


    /**
     * iterator for {@code LList}<br>
     * does not implement {@code remove()}
     */
    private class LListIterator implements Iterator<T>
        {

        private Node<T> nextNode ;


        /**
         * set initial state at beginning of the chain
         */
        private LListIterator()
            {

            this.nextNode = LList.this.firstNode ;

            }   // end no-arg constructor


        @Override
        public T next()
            {

            T result ;

            if ( !hasNext() )
                {
                throw new NoSuchElementException( "Illegal call to next(); iterator is after end of list." ) ;
                }

            result = this.nextNode.getData() ;
            this.nextNode = this.nextNode.getNext() ; // Advance iterator

            return result ; // Return next entry in iteration

            }   // end next()


        @Override
        public boolean hasNext()
            {

            return this.nextNode != null ;

            }   // end hasNext()

        } // end LListIterator

    }   // end class LList