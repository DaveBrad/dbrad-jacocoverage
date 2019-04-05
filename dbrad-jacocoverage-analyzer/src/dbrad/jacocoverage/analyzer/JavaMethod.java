/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.analyzer;

/**
 * Representation of a Java method coverage.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class JavaMethod {

    /** Method's name. */
    private String name;

    /** Method's coverage state. */
    private CoverageStateEnum coverageState;

    /** Number of covered instructions. */
    private int instructionsCovered = 0;

    /** Number of not covered instructions. */
    private int instructionsMissed = 0;

    /** Number of covered lines. */
    private int linesCovered = 0;

    /** Number of not covered lines. */
    private int linesMissed = 0;

    /** Description of method's coverage state. */
    private String coverageDesc;

    /**
     * Line number of method's declaration in NetBeans source code editor.
     * Warning: in NetBeans editor starting index is 0, not 1. */
    private int lineNumber;

    /**
     * Create an object for a methods representation.
     */
    public JavaMethod() {
    }

    /**
     * Get the methods name.
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Set the method name this instance represents.
     *
     * @param name string of method name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the coverage state for method.
     *
     * @return coverage state CoverageStateEnum
     */
    public CoverageStateEnum getCoverageState() {
        return coverageState;
    }

    /**
     * Set the coverage state for method.
     *
     * @param coverageState CoverageStateEnum
     */
    public void setCoverageState(CoverageStateEnum coverageState) {
        this.coverageState = coverageState;
    }

    /**
     * Get covered instruction counter;
     *
     * @return integer
     */
    public int getInstructionsCovered() {
        return instructionsCovered;
    }

    /**
     * Set covered instruction counter;
     *
     * @param instructionsCovered integer
     */
    public void setInstructionsCovered(int instructionsCovered) {
        this.instructionsCovered = instructionsCovered;
    }

    /**
     * Get missed instruction counter;
     *
     * @return integer
     */
    public int getInstructionsMissed() {
        return instructionsMissed;
    }

    /**
     * Set missed instruction counter;
     *
     * @param instructionsMissed integer
     */
    public void setInstructionsMissed(int instructionsMissed) {
        this.instructionsMissed = instructionsMissed;
    }

    /**
     * Get covered lines counter;
     *
     * @return integer
     */
    public int getLinesCovered() {
        return linesCovered;
    }

    /**
     * Set covered lines counter;
     *
     * @param linesCovered integer
     */
    public void setLinesCovered(int linesCovered) {
        this.linesCovered = linesCovered;
    }

    /**
     * Get missed lines counter;
     *
     * @return integer
     */
    public int getLinesMissed() {
        return linesMissed;
    }

    /**
     * Set missed lines counter;
     *
     * @param linesMissed integer
     */
    public void setLinesMissed(int linesMissed) {
        this.linesMissed = linesMissed;
    }

    /**
     * Get the description of the coverage details.
     * 
     * @return string
     */
    public String getCoverageDesc() {
        return coverageDesc;
    }

    /**
     * Set the description of the coverage details.
     * 
     * @param coverageDesc string
     */
    public void setCoverageDesc(String coverageDesc) {
        this.coverageDesc = coverageDesc;
    }

    /**
     * Get source code line number (for Netbeans editor).
     *
     * @return integer
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Set source code line number (for Netbeans editor).
     *
     * @param lineNumber integer
     */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
