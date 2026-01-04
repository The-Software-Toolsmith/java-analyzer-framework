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


package edu.wit.scds.ds.buildingblocks.enhanced ;

/**
 * demonstrate use of debugging features of enhanced Node
 *
 * @author David M Rosenberg
 *
 * @version 1.0 2025-06-12 Initial implementation
 *
 * @since 1.0
 */
public class DemoEnhancedNode
    {

    /**
     * demo driver
     *
     * @param args
     *     -unused-
     *
     * @since 1.0
     */
    public static void main( final String[] args )
        {

        // create a chain
        System.out.printf( "create a chain with 5 Nodes; data 1..5%n%n" ) ;
        Node<Integer> myChain = new Node<>( 5 ) ;
        myChain = new Node<>( 4, myChain ) ;
        myChain = new Node<>( 3, myChain ) ;
        myChain = new Node<>( 2, myChain ) ;
        myChain = new Node<>( 1, myChain ) ;

        // display the chain reference and contents
        System.out.printf( "myChain: %s%n", myChain ) ;
        System.out.printf( "myChain...: %s%n", Node.chainToString( myChain ) ) ;

        // create a loop in the chain then display its contents
        System.out.printf( "%ncreate a loop in the chain%n%n" ) ;
        myChain.getNext().getNext().setNext( myChain ) ;
        System.out.printf( "myChain...: %s%n", Node.chainToString( myChain ) ) ;

        }	// end main()

    }   // end class DemoEnhancedNode