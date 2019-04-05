/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.analyzer;

import java.util.HashMap;
import java.util.Map;

/**
 * Representation of a Java class code coverage.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
public class JavaClass implements Comparable<JavaClass> {

    /** The package name (with "/" instead of "."). */
    private final String packageName;

    /** The class name (with ".java" extension). */
    private final String className;

    /** Indicate the coverage state of class instructions. */
    private final Map<Integer, CoverageStateEnum> coverage = new HashMap<>(256);

    /** Indicate the coverage description of class instructions. */
    private final Map<Integer, String> coverageDesc = new HashMap<>(128);

    /** Indicate the coverage state of class methods declarations. */
    private final Map<Integer, CoverageStateEnum> methodCoverage = new HashMap<>(32);

    /** Number of covered lines. */
    private int numberCoveredLines = 0;

    /** Number of partially covered lines. */
    private int numberPartiallyCoveredLines = 0;

    /** Number of not covered lines. */
    private int numberNotCoveredLines = 0;

    /**
     * Create a Java class instance which holders data about the
     * coverage of lines of the source code.
     * 
     * @param packageName string package name
     * @param className string class name
     */
    public JavaClass(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }

    /**
     * Add 'covered' line number
     *
     * @param lineNumber
     */
    public void addCoveredLine(int lineNumber) {
        coverage.put(lineNumber, CoverageStateEnum.COVERED);
        numberCoveredLines++;
    }

    /**
     * Add 'partially-covered' line number
     *
     * @param lineNumber
     */
    public void addPartiallyCoveredLine(int lineNumber) {
        coverage.put(lineNumber, CoverageStateEnum.PARTIALLY_COVERED);
        numberPartiallyCoveredLines++;
    }

    /**
     * Add 'not covered' line number
     *
     * @param lineNumber
     */
    public void addNotCoveredLine(int lineNumber) {
        coverage.put(lineNumber, CoverageStateEnum.NOT_COVERED);
        numberNotCoveredLines++;
    }

    /**
     * Add the method coverage line number and coverage state.
     *
     * @param lineNumber
     * @param coverageState
     */
    public void addMethodCoverage(int lineNumber, CoverageStateEnum coverageState) {
        methodCoverage.put(lineNumber, coverageState);
    }

    /**
     * Get the package-name for the Java class object.
     *
     * @return string
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Get the class-name for the Java class object.
     *
     * @return string
     */
    public String getClassName() {
        return className;
    }

    /**
     * Get coverage data for the Java class.
     *
     * @return Map of the coverage state against line number
     */
    public Map<Integer, CoverageStateEnum> getCoverage() {
        return coverage;
    }

    /**
     * Get the coverage description for the Java class.
     *
     * @return Map of the coverage description against line number
     */
    public Map<Integer, String> getCoverageDesc() {
        return coverageDesc;
    }

    /**
     * Get the method coverage for the Java class.
     *
     * @return Map of the methos coverage against line number
     */
    public Map<Integer, CoverageStateEnum> getMethodCoverage() {
        return methodCoverage;
    }

    /**
     * Get the number of covered lines.
     *
     * @return integer
     */
    public int getNumberCoveredLines() {
        return numberCoveredLines;
    }

    /**
     * Get the number of partially-covered lines.
     *
     * @return integer
     */
    public int getNumberPartiallyCoveredLines() {
        return numberPartiallyCoveredLines;
    }

    /**
     * Get the number of not covered lines.
     *
     * @return integer
     */
    public int getNumberNotCoveredLines() {
        return numberNotCoveredLines;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int compareTo(JavaClass o) {
        return (this.getPackageName() + this.getClassName()).compareTo(o.getPackageName() + o.getClassName());
    }
}
