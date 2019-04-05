/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocoverage.plugin.config.pf;

import dbrad.jacocofpm.mgr.com.FilePackageOfSrc;

public class PfItem {

    /**
     * The on-code state for the source-code-dir / test-source-dir.
     */
    public Boolean onCodeSelectState;
    /**
     * if the package is selected for inclusion
     */
    public Boolean selectState;

    /**
     * the package string of the source code
     */
    public FilePackageOfSrc srcPackageFile;

    public PfItem(Boolean onCodeSelectState, Boolean selectState, FilePackageOfSrc packageInfo) {
        this.onCodeSelectState = onCodeSelectState;
        this.selectState = selectState;
        this.srcPackageFile = packageInfo;

        // the state needs to be changed on the file so they match the
        // Pfitem used to represent the loading of the tables
        packageInfo.setOnCodeFolderState(onCodeSelectState);
    }
}
