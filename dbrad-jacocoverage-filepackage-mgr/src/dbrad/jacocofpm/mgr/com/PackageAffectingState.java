/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package dbrad.jacocofpm.mgr.com;

import java.awt.Color;

/**
 * ENUM for affecting state of package-string selection over "source code" (src
 * and/or test) packages.
 * <p>
 * The state will be displayed as a color in the package format string in the
 * various tables for filtering or excludes.
 *
 * @author dbradley
 */
public enum PackageAffectingState {
    /**
     * nothing is affecting this package
     */
    NOT_AFFECTED,
    /**
     * a sibling source-code package is affecting this package
     */
    AFFECTED_BY_A_SIBLING,
    /**
     * this source-code package is affecting other source-code folder(s) with
     * the same package name
     */
    AFFECTING_OTHERS,
    /**
     * the explicit exclude packages is excluding the package
     */
    AFFECTED_BY_EXPLICIT_EXCLUDE_PACKAGES;

    // the colors used for the various states and displayed in the package
    // tables (filters or excludes)
    /** Color that represent a not affected by any other package setting in the
     * Package filter tables. */
    public final static Color WHITE_NOT_AFFECTED = Color.white;

    /** Color for packages that are affected-by another package settings in the
     * Package filter tables. */
    public final static Color LITE_GRAY_AFFECTED_BY_A_SIBLING = new Color(244, 244, 244);

    /** Color for package that is affecting other package settings in the
     * Package filter tables. */
    public final static Color LITE_BLUE_AFFECTING_OTHERS = new Color(100, 244, 244);

    /** Color for packages that are explicitly excluded via the Exclude packages
     * tab. */
    public final static Color PINK_AFFECTED_BY_EXPLICIT_EXCLUDE_PACKAGES = Color.pink;

    /**
     * Get the color that is to be used for the table package field for the
     * state.
     *
     * @return color object
     */
    final public Color getColor() {
        Color thisColor = Color.white;
        switch (this) {
            case NOT_AFFECTED:
                thisColor = WHITE_NOT_AFFECTED;
                break;
            case AFFECTED_BY_A_SIBLING:
                thisColor = LITE_GRAY_AFFECTED_BY_A_SIBLING;
                break;
            case AFFECTING_OTHERS:
                thisColor = LITE_BLUE_AFFECTING_OTHERS;
                break;
            case AFFECTED_BY_EXPLICIT_EXCLUDE_PACKAGES:
                thisColor = PINK_AFFECTED_BY_EXPLICIT_EXCLUDE_PACKAGES;
                break;
            default:
                throw new AssertionError(this.name());
        }
        return thisColor;
    }
}
