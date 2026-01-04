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


package edu.wit.scds.ds.stacks ;

import edu.wit.scds.ds.chains.enhanced.Node ;
import java.util.EmptyStackException ;

/**
 * implementation of a stack using a singly-linked chain of {@code Node}s
 *
 * @author David M Rosenberg
 *
 * @version 1.0 2025-10-06 Initial implementation based upon LinkedBag
 *
 * @param <T>
 *     type placeholder for the application's data
 *
 * @since 1.0
 */
public final class LinkedStack<T> implements StackInterface<T>
    {

    /*
     * constants
     */
    // none


    /*
     * instance state/data fields
     */


    /** backing store for application data */
    private Node<T> topNode ;


    /*
     * constructors
     */


    /**
     * set initial state to valid empty
     */
    public LinkedStack()
        {

        initializeState() ;

        }   // end no-arg constructor


    /*
     * API methods
     */


    @Override
    public void clear()
        {

        // reset the state to valid empty
        initializeState() ;

        }   // end clear()


    @Override
    public boolean isEmpty()
        {

        return this.topNode == null ;

        }   // end isEmpty()


    @Override
    public T peek()
        {

        if ( isEmpty() )
            {

            throw new EmptyStackException() ;

            }

        // assertion: there's at least one Node in the chain

        // return the first node's data
        return this.topNode.getData() ;

        }   // end peek()


    @Override
    public T pop()
        {

        final T top = peek() ;  // might throw EmptyStackException

        // assertion: there is at least one item on the stack

        this.topNode = this.topNode.getNext() ;

        return top ;

        }   // end pop()


    @Override
    public void push( final T newEntry )
        {

        // store the new entry
        this.topNode = new Node<>( newEntry, this.topNode ) ;

        }   // end push()


    /**
     * Collections typically don't include {@code toString()}; provided for
     * testing and debugging
     */
    @Override
    public String toString()
        {

        return String.format( "stack: %s",
                              Node.chainToString( this.topNode ) ) ;

        }   // end toString()


    /*
     * private utility methods
     */


    /**
     * (re)set the instance state to valid empty
     */
    private void initializeState()
        {

        this.topNode = null ;

        }   // end initializeState()


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
/* IN_PROCESS */
        }   // end main()


    /*
     * inner classes
     */
    // none


    }   // end class LinkedBag