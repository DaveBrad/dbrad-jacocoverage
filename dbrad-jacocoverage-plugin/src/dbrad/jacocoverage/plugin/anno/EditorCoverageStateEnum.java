/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.plugin.anno;

/**
 * Coverage status. For integration in NetBeans code editor.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public enum EditorCoverageStateEnum {

    /** Covered. */
    COVERED,
    /** Partially covered. */
    PARTIALLY_COVERED,
    /** Not covered. */
    NOT_COVERED;

    /**
     * Get the coverage status short description.
     *
     * @return a short description.
     */
    public String getDescription() {
        switch (this) {
            case COVERED:
                return "Covered";
            case PARTIALLY_COVERED:
                return "Partially covered";
            case NOT_COVERED:
                return "Not covered";
            default:
                return "Unknown info about coverage";
        }
    }

    /**
     * Get the coverage type (id) registered in the "layer.xml" NetBeans module file.
     *
     * @return coverage type.
     */
    public String getType() {
        switch (this) {
            case COVERED:
                return "annotation_covered";
            case PARTIALLY_COVERED:
                return "annotation_partiallycovered";
            case NOT_COVERED:
                return "annotation_notcovered";
            default:
                return "annotation_unknown";
        }
    }
}
