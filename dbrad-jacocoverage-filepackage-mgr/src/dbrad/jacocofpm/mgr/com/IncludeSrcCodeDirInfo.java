/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package dbrad.jacocofpm.mgr.com;

import java.util.Comparator;
import java.util.Objects;

/**
 * Class for holding the data of include packages settings and the source-file
 *
 * @author dbradley
 */
public class IncludeSrcCodeDirInfo implements Comparator {

    /** the string to use in a package format */
    protected String packageFormat;
    /** the origin of the source file which the includes source code directory
     * represents.
     */
    protected FilePackageOfSrc originSrcCodeFile;

    /**
     * Create an object for an include source-code directory/folder.
     *
     * @param packageFormat     string for the package format
     * @param originSrcCodeFile FilePackageOfSrc of the include package object
     */
    IncludeSrcCodeDirInfo(String packageFormat, FilePackageOfSrc originSrcCodeFile) {
        this.packageFormat = packageFormat;
        this.originSrcCodeFile = originSrcCodeFile;
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        // this is a limited equals for processing with Collections
        // only the String part is tested    
        return (this.packageFormat.equals(((IncludeSrcCodeDirInfo) o).packageFormat));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.packageFormat);
        return hash;
    }

    @Override
    public int compare(Object o1, Object o2) {

        return (((IncludeSrcCodeDirInfo) o1).packageFormat.compareTo(((IncludeSrcCodeDirInfo) o2).packageFormat));
    }
}
