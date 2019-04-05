/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocofpm.config;

import dbrad.jacocofpm.mgr.com.FileClassesDir;
import java.io.File;
import java.net.URI;
import org.openide.filesystems.FileObject;

/**
 * Class of one of the source-code files within the Netbeans project.
 * <p>
 * A file source-code can be from different areas: src, test, src2, test2, ....
 * arrangements.
 *
 * @author dbradley
 */
public class NbFileSrcCodePair extends File {

    private static final long serialVersionUID = 1L;

    /** is test-source-code file */
    private boolean isTestSrcCodeBool;

    /** The name that displays in Netbeans for the source-code */
    private String codesFolderNbDisplayName;

    /** the IDE file object of the source-code file (src/test) used to interact
     * with Netbeans sub-systems directly.
     */
    public FileObject openIdeFileObject;

    /** The classes directory that associates with this source-code */
    private FileClassesDir classesAssociatedFile;

    /**
     * Create a file source-code pair object for the parameters provided.
     *
     * @param pathname                 the source-code (src/test) path name
     * @param openIdeFileObject        IDE file object of the source-code file
     * @param isTestCode               true if test-source-code
     * @param codesFolderNbDisplayName the source-code (src/test) folders
     *                                 display name
     * @param classesAssociatedFile    the File location of the classes binary
     *                                 folder location
     */
    public NbFileSrcCodePair(String pathname,
            FileObject openIdeFileObject,
            boolean isTestCode,
            String codesFolderNbDisplayName, FileClassesDir classesAssociatedFile) {
        //
        super(pathname);

        this.openIdeFileObject = openIdeFileObject;

        this.isTestSrcCodeBool = isTestCode;
        this.codesFolderNbDisplayName = codesFolderNbDisplayName;
        this.classesAssociatedFile = classesAssociatedFile;
    }

    /**
     * Create a file source-code pair object for the parameters provided.
     *
     * @param pathname          the source-code (src/test) path name
     * @param openIdeFileObject IDE file object of the source-code file
     * @param isTestCode        true if test-source-code
     */
    public NbFileSrcCodePair(String pathname, FileObject openIdeFileObject, boolean isTestCode) {
        //
        super(pathname);

        this.openIdeFileObject = openIdeFileObject;

        this.isTestSrcCodeBool = isTestCode;
    }

    /**
     * Is the source-code a test-source-code kind.
     *
     * @return true if source is test-source-code
     */
    final public boolean isTestSrcCode() {
        return this.isTestSrcCodeBool;
    }

    /**
     * Get the source (src/test) folder display name.
     *
     * @return String of folder display name
     */
    public String getSrcFolderDirDisplayName() {
        return codesFolderNbDisplayName;
    }

    /**
     * Get the classes binary directory for associated file.
     *
     * @return File of location
     */
    final public FileClassesDir getClassesAssociatedFile() {
        return classesAssociatedFile;
    }

    // - - - - - - - only the above kinds of objects may be created
    private NbFileSrcCodePair(String pathname) {
        super(pathname);
        // no blanks of this class allowed
    }

    private NbFileSrcCodePair(String parent, String child) {
        super(parent, child);
    }

    private NbFileSrcCodePair(File parent, String child) {
        super(parent, child);
    }

    private NbFileSrcCodePair(URI uri) {
        super(uri);
    }
}
