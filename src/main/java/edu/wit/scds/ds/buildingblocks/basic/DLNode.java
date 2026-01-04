/* @formatter:off
 *
 * © David M Rosenberg
 *
 * Topic: general applicability for chains/linked constructs
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


package edu.wit.scds.ds.buildingblocks.basic ;

/**
 * Class {@code DLNode} provides the basis for doubly-linked chain functionality.
 * <p>
 * Extracted from {@code LinkedDeque.java}.
 *
 * @author Frank M. Carrano
 * @author Timothy M. Henry
 *
 * @version 5.0
 *
 * @author David M Rosenberg
 *
 * @version 5.1 2020-10-23 revise 1-arg constructor to invoke 3-arg constructor
 * @version 5.2 2021-07-11
 *     <ul>
 *     <li>add {@code toString()} and {@code chainToString()}s to aid debugging (borrowed from
 *     enhanced {@code Node.java})
 *     <li>add {@code displayTraversals()}
 *     <li>enhance testing in {@code main()} to demonstrate several common errors in doubly-linked
 *     chain construction/manipulation
 *     </ul>
 * @version 5.3 2021-10-24
 *     <ul>
 *     <li>rename {@code id} and {@code nextId} to {@code pseudoAddress} and
 *     {@code nextPseudoAddress} to better reflect their purpose
 *     <li>add getter for {@code pseudoAddress}
 *     <li>add 1-arg convenience method {@code chainToString()}
 *     <li>enhance {@code toString()} to handle {@code null} {@code data}
 *     </ul>
 * @version 5.4 2022-10-30
 *     <ul>
 *     <li>add no-arg constructor
 *     <li>replace "null" pointer text with "&bullet;" in {@code toString()} and
 *     {@code chainToString()} for consistency with in-class demos
 *     <li>revise {@code toString()} to reduce space and better resemble an in-class diagram of a
 *     {@code DLNode}
 *     <li>track updates to {@code Node} to more clearly reflect similarities and differences
 *     </ul>
 * @version 6.0 2022-10-30 Initial implementation based on enhanced {@code DLNode}
 * @version 6.1 2022-11-02 Remove unnecessary constants - remnants of visualization code<br>
 * @version 7.0 2024-03-19 Rename getter and setter methods to remove 'Node'
 * @version 7.0.1 2025-07-21 minor cosmetic changes
 *
 * @param <T>
 *     The class of item the {@code DLNode} will reference.
 */
public class DLNode<T>
    {

    /*
     * data fields
     */
    /** reference to the entry */
    private T data ;

    /** link to the next node in the chain */
    private DLNode<T> next ;

    /** link to the previous node in the chain */
    private DLNode<T> previous ;


    /**
     * Sets up a node: {@code previous}, {@code data}, and {@code next} pointers are set to
     * {@code null}.
     */
    public DLNode()
        {

        this( null, null, null ) ;

        }   // end no-arg constructor


    /**
     * Set up a {@code DLNode} given supplied {@code data}; {@code next} and {@code previous}
     * references are set to {@code null}.
     *
     * @param dataPortion
     *     the information this node holds
     */
    public DLNode( final T dataPortion )
        {

        this( null, dataPortion, null ) ;

        } // end 1-arg constructor


    /**
     * Set up a node given supplied {@code data}, {@code next}, and {@code previous} references.
     *
     * @param previousNode
     *     reference to the previous node in the linked list
     * @param dataPortion
     *     the information this node holds
     * @param nextNode
     *     reference to the next node in the linked list
     */
    public DLNode( final DLNode<T> previousNode,
                   final T dataPortion,
                   final DLNode<T> nextNode )
        {

        this.data = dataPortion ;
        this.next = nextNode ;
        this.previous = previousNode ;

        } // end 3-arg constructor


    /**
     * Retrieve the {@code data} referenced by this {@code DLNode}
     *
     * @return a reference to the data stored in this {@code DLNode}
     */
    public T getData()
        {

        return this.data ;

        } // end getData()


    /**
     * Retrieve the {@code next} field
     *
     * @return reference to the next {@code DLNode} in the chain
     */
    public DLNode<T> getNext()
        {

        return this.next ;

        } // end getNext()


    /**
     * Retrieve the {@code previous} field
     *
     * @return reference to the previous {@code DLNode} in the chain
     */
    public DLNode<T> getPrevious()
        {

        return this.previous ;

        } // end getPrevious()


    /**
     * Point the {@code data} field at the supplied data
     *
     * @param newData
     *     reference to the data to store
     */
    public void setData( final T newData )
        {

        this.data = newData ;

        } // end setData()


    /**
     * Set the {@code next} field
     *
     * @param nextNode
     *     another {@code DLNode} in the chain or {@code null} to indicate none
     */
    public void setNext( final DLNode<T> nextNode )
        {

        this.next = nextNode ;

        } // end setNext()


    /**
     * Set the {@code previous} field
     *
     * @param previousNode
     *     another {@code DLNode} in the chain or {@code null} to indicate none
     */
    public void setPrevious( final DLNode<T> previousNode )
        {

        this.previous = previousNode ;

        } // end setPrevious()

    }   // end class DLNode