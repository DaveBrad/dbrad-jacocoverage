/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.plugin.anno;

/**
 * Coverage annotation.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class CoverageAnnotation extends AbstractCoverageAnnotation {

    private final EditorCoverageStateEnum state;

    /**
     * Create a coverage annotation instance for the state against a line of
     * code.
     *
     * @param state         the state that represents a source code line's color
     *                      annotation
     * @param projectName   the project name of the source
     * @param classFullName the class java file of the source
     * @param lineNum       the line number in the code
     * @param theme         the color theme that is applied for the annotation
     */
    public CoverageAnnotation(EditorCoverageStateEnum state, String projectName, String classFullName, Integer lineNum, int theme) {
        super(projectName, classFullName, lineNum, theme);
        this.state = state;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getAnnotationType() {
        return super.getAnnotationType() + state.getType();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getShortDescription() {
        return state.getDescription();
    }
}
