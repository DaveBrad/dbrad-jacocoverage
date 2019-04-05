/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.analyzer;

/**
 * Coverage status.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public enum CoverageStateEnum {

    /** Covered. */
    COVERED,
    /** Partially covered. */
    PARTIALLY_COVERED,
    /** Not covered. */
    NOT_COVERED;

}
