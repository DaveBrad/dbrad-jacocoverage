/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.plugin.anno;

/**
 * Coverage glyphed annotation which is the color codes displayed against a line
 * of code.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class CoverageGlyphedAnnotation extends CoverageAnnotation {

    private final String desc;

    /**
     * Create a glyphed annotation instance for the state against a line of
     * code.
     *
     * @param state         the state that represents a source code line's color
     *                      annotation
     * @param projectName   the project name of the source
     * @param classFullName the class java file of the source
     * @param lineNum       the line number in the code
     * @param desc          description of the annotation
     * @param theme         the color theme that is applied for the annotation
     */
    public CoverageGlyphedAnnotation(EditorCoverageStateEnum state, String projectName, String classFullName, Integer lineNum,
            String desc, int theme) {
        super(state, projectName, classFullName, lineNum, theme);
        this.desc = desc;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getAnnotationType() {
        return super.getAnnotationType() + "_glyph";
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getShortDescription() {
        return desc;
    }
}
