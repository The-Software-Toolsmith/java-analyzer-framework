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


package edu.wit.scds.ds.common.utilities ;

/**
 * The constants in this class support visualization and debugging tools, primarily in the
 * {@code ...common} package
 * <p>
 * Note: These enhancements are for educational purposes.
 * </p>
 * 
 * @author David M Rosenberg
 *
 * @version 1.0 2024-02-15 Initial implementation taken from {@code ...common.enhanced.Node}<br>
 * @version 1.1 2024-03-09 add {@code FIELD_SEPARATOR}
 * @version 1.2 2024-11-03 add ({@code BIDIRECTIONAL_SIBLING_REFERENCE_MARKER}
 * @version 1.3 2025-01-22 make the class final
 */
public final class VisualizationSupport
    {
    /*
     * symbolic constants for visualization of linked constructs
     */

    /** text to act as a field separator */
    public static final String FIELD_SEPARATOR = "¦" ;
    /** text to mark a pseudo address (to help distinguish it from an integer */
    public static final String PSEUDO_ADDRESS_PREFIX = "@" ;
    /** text to represent a {@code null} pointer */
    public static final String NULL_REFERENCE_MARKER = "•" ;    // "\u2022"
    /** text to represent a pointer to data */
    public static final String DATA_REFERENCE_MARKER = "↓" ;
    /** text to represent a pointer to the left data - for trees */
    public static final String LEFT_DATA_REFERENCE_MARKER = "↙" ;
    /** text to represent a pointer to the middle data - for trees */
    public static final String MIDDLE_DATA_REFERENCE_MARKER = "↓" ;
    /** text to represent a pointer to the right data - for trees */
    public static final String RIGHT_DATA_REFERENCE_MARKER = "↘" ;
    /** text to represent a pointer to the next {@code Node} */
    public static final String NEXT_REFERENCE_MARKER = "→" ;
    /** text to represent a pointer to the previous {@code Node} - for doubly linked chains */
    public static final String PREVIOUS_REFERENCE_MARKER = "←" ;
    /** text to represent a pointer to the left child {@code Node} - for trees */
    public static final String LEFT_CHILD_REFERENCE_MARKER = "↙" ;
    /** text to represent a pointer to the right child {@code Node} - for trees */
    public static final String RIGHT_CHILD_REFERENCE_MARKER = "↘" ;
    /** text to represent a pointer to the right sibling {@code Node} - for trees */
    public static final String RIGHT_SIBLING_REFERENCE_MARKER = "→" ;
    /** text to represent a pointer to the parent {@code Node} - for trees */
    public static final String PARENT_REFERENCE_MARKER = "↑" ;
    /** text to represent a pointer to the left child's parent {@code Node} - for trees */
    public static final String LEFT_CHILD_TO_PARENT_REFERENCE_MARKER = "↗" ;
    /** text to represent a pointer to the right child's parent {@code Node} - for trees */
    public static final String RIGHT_CHILD_TO_PARENT_REFERENCE_MARKER = "↖" ;
    /** text to represent bi-directional references between two sibling nodes */
    public static final String BIDIRECTIONAL_SIBLING_REFERENCE_MARKER = "←→" ;


    /*
     * data fields
     */
    // not applicable


    /*
     * constructors
     */
    /** prevent instantiation */
    private VisualizationSupport()
        {}


    /*
     * API methods
     */
    // not applicable


    /*
     * private utility methods
     */
    // not applicable

    }   // end class VisualizationSupport