/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocoverage.plugin.config;

import dbrad.jacocofpm.config.IdeProjectJacocoverageConfig;
import javax.swing.JTable;

/**
 *
 * @author dbradley
 */
abstract public class JTableConfig extends JTable {

    private static final long serialVersionUID = 0L;

    //99 getter needed
    public JTableConfigDatumObj datum;

    public JTableConfig() {
        // needed for Netbeans GUI builder
    }

    public void setupAndBuild(JTableConfigDatumObj datum) {
        this.datum = datum;
    }

    /**
     * Load an IDE project for its PfTable (root and associate)
     * @param prjCfg the project configuration 
     * @return true if any item is not in the JSON configuration file
     */
    abstract public boolean loadTableFromProjectJson(IdeProjectJacocoverageConfig prjCfg);
}
