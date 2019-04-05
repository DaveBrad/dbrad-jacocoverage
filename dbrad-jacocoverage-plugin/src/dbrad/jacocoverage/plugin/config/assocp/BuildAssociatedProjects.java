/* Copyright (c) 2017 dbradly. All rights reserved. */

package dbrad.jacocoverage.plugin.config.assocp;

import dbrad.jacocofpm.config.IdeProjectJacocoverageConfig;
import dbrad.jacocofpm.mgr.com.NbProjectJacoco;
import dbrad.jacocoverage.plugin.config.pf.PfJPanel;
import dbrad.jacocoverage.plugin.config.pf.PfJTable;
import dbrad.jacocoverage.plugin.config.pf.PfJTableDatum;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JTabbedPane;
import org.openide.util.Exceptions;

/**
 *
 * @author dbradley
 */
public class BuildAssociatedProjects {

    /**
     * Create and fill in the Associated-projects tab from the root Netbeans
     * project. The associated-projects are the roots depending-on-projects (or
     * in Netbeans terms sub-projects).
     *
     * @param nbProjectJacocoRoot
     */
    public static void buildAndLoadAssociatedProjects(JTabbedPane tabbedPaneHolder, NbProjectJacoco nbProjectJacocoRoot) {

        // only if there are depending-on-projects
        if (nbProjectJacocoRoot.getDependingOnNbProjectsKeyArr().isEmpty()) {
            // find the parent tabbed-pane and disable it as associated-projects
            // are not present
            //
            // locate the parent tabbed-pane
            Component jComp = tabbedPaneHolder.getParent();
            int limit = 8;
            while (!(jComp instanceof JTabbedPane)) {
                jComp = jComp.getParent();
                limit--;
                if (limit < 1) {
                    throw new RuntimeException("exceeded limit: internal error.");
                }
            }
            // now disable the Associated project tab
            JTabbedPane rootTabbedPane = (JTabbedPane) jComp;

            int indexOfAssociateTab = rootTabbedPane.indexOfTab("Associated projects");
            rootTabbedPane.setEnabledAt(indexOfAssociateTab, false);

            return;
        }

        // create a tab for each depending-On item (NB SubprojectProviderItem)
        for (String orderedKey : nbProjectJacocoRoot.getDependingOnNbProjectsKeyArr()) {
            
            NbProjectJacoco tabNDependingOnProjJacoco 
                    = nbProjectJacocoRoot.getDependingOnNbProjectsHash().get(orderedKey);

            // the PfJPanel is a container for all of a Netbeans-projects 
            // packaging information
            PfJPanel pfPanel = new PfJPanel();
            pfPanel.setMaximumSize(new Dimension(32000, 32000));
            pfPanel.setPreferredSize(new Dimension(400, 400));

            pfPanel.setProjectConfig(tabNDependingOnProjJacoco);

            // pfpanel comes with a PfJTable so populate it once it is
            // part of the pfPanel
            PfJTable pfJTablex = pfPanel.jTablePackageFilter;
            tabbedPaneHolder.addTab(tabNDependingOnProjJacoco.getNbProjectDisplayName(), pfPanel);

            // populate
            pfJTablex.setupAndBuild(new PfJTableDatum(tabNDependingOnProjJacoco));

            try {
                // all associated tables need to be built from the JSON-data in the root
                // Netbeans project
                IdeProjectJacocoverageConfig idePrjCfg
                        = IdeProjectJacocoverageConfig.reuseForProject(nbProjectJacocoRoot.getPrincipleNbProject());

                pfJTablex.loadTableFromProjectJson(idePrjCfg);

            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
