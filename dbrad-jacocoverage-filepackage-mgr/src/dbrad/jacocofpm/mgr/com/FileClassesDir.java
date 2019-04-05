/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package dbrad.jacocofpm.mgr.com;

import java.io.File;

/**
 * Class that represents the File for the classes directory of a source-code
 * pair. That is, the path to the classes folder that Netbeans stores compiled
 * '.class' files in.
 *
 * @author dbradley
 */
public class FileClassesDir extends File {

    private static final long serialVersionUID = 1L;

    /**
     * Create a classes-binary File instance.
     * 
     * @param pathname classes-binary file path
     */
    public FileClassesDir(String pathname) {
        super(pathname);
    }
}
