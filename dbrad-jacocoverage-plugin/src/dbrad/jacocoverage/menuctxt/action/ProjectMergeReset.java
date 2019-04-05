/* Copyright (c) 2018 dbradley. All rights reserved. */
package dbrad.jacocoverage.menuctxt.action;

import dbrad.jacocofpm.config.IdeProjectJacocoverageConfig;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.awt.DynamicMenuContent;
import org.openide.util.ContextAwareAction;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

/**
 * Class for the merge-clear action which optionally displays when the
 * merge-processing-on is set in a project-specific configuration. This object
 * is a menu item that will cause the merge data file to be cleared.
 *
 * @author dbradley
 */
@ActionID(
        category = "Project",
        id = "dbrad.jacocoverage.menuctxt.action.ProjectMergeReset"
)
@ActionRegistration(
        displayName = "#CTL_ProjectMergeReset",
        lazy = false,
        asynchronous = true,
        surviveFocusChange = true
)
@ActionReference(path = "Projects/Actions",
        position = 1987)
@Messages("CTL_ProjectMergeReset=Merge clear (w/jacocoverage)")

public final class ProjectMergeReset
        extends AbstractAction
        implements ActionListener, ContextAwareAction, Presenter.Popup {

    /**
     * Create the project merge clear action (project and file view) for a
     * project.
     */
    public ProjectMergeReset() {
        // run is done at the project view/level of actions, so the project
        // is selected
        Project nbProject = IdeProjectJacocoverageConfig.getSelectedNbProject();

        super.putValue(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
        super.putValue(Action.NAME, Bundle.CTL_ProjectMergeReset());
    }

    /**
     * Perform the action to clear the merge file data.
     *
     * @param ev the event action (has not bearing)
     */
    @Override
    public void actionPerformed(ActionEvent ev) {
        Project nbProject = IdeProjectJacocoverageConfig.getSelectedNbProject();
        IdeProjectJacocoverageConfig.clearMergeData(nbProject);
    }

    /**
     * The context awareness always applies, however if no merge setting for the
     * project then will be overridden.
     *
     * @param actionContext an instance of this class to process against
     *
     * @return
     */
    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new ProjectMergeReset();
    }

    /**
     * Provide (get) the popup menu item for this project if the merge
     * capability is selected and applicable.
     *
     * @return JMenuItem object or null if not applicable.
     */
    @Override
    public JMenuItem getPopupPresenter() {
        JMenuItem menuitem = new JMenuItem(this);
        menuitem.setEnabled(false);
        menuitem.putClientProperty(DynamicMenuContent.HIDE_WHEN_DISABLED, true);

        try {
            // if no JSON file for the project then we know for sure that
            // the merge clear is not present
            Project nbProject = IdeProjectJacocoverageConfig.getSelectedNbProject();

            if (IdeProjectJacocoverageConfig.isJsonFilePresent(nbProject)) {
                // if there is a .jacocodbrad file then we may have a setting 
                // and can build the IdeProjectJacocoverageConfig
                IdeProjectJacocoverageConfig nbIdeJacocoProj
                        = IdeProjectJacocoverageConfig.reuseForProject(nbProject);

                // the enable is selected
                if (nbIdeJacocoProj.isMergeOnSet()) {
                    menuitem.setEnabled(true);
                }
            }
        } catch (IOException ex) {
            // oops, but no idea why
            Exceptions.printStackTrace(ex);
        }
        // do not apply the merge clear menu item
        return menuitem;
    }
}
