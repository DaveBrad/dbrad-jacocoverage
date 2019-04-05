/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.menuctxt.action;

import dbrad.jacocofpm.config.IdeProjectJacocoverageConfig;
import static dbrad.jacocofpm.util.NBProjectTypeEnum.J2SE;
import static dbrad.jacocofpm.util.NBProjectTypeEnum.NBMODULE;
import static dbrad.jacocofpm.util.NBProjectTypeEnum.NBMODULE_SUITE;
import dbrad.jacocofpm.util.NBUtils;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.Action;
import javax.swing.JMenuItem;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.awt.DynamicMenuContent;
import org.openide.filesystems.FileObject;
import org.openide.util.ContextAwareAction;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;

/**
 * The projects-view "Run w/JaCoCoverage" contextual action registration. Start
 * the "run" Ant task with the JaCoCo JavaAgent correctly configured, colorize
 * Java source files and show a coverage report.
 * <p>
 * NOTE WARNING: this class is invoked by the IDE in a manner that DOES NOT
 * SUPPORT class variables.
 *
 * @author Jan Lahoda ((pre-2017)
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
@ActionID(category = "Project",
        id = "dbrad.jacocoverage.menuctxt.action.ProjectAntRunJ2SE")
@ActionRegistration(displayName = "#CTL_ProjectAntRunJ2SE",
        lazy = false,
        asynchronous = true,
        surviveFocusChange = true)
@ActionReference(path = "Projects/Actions",
        position = 1984,
        separatorBefore = 1983)
@NbBundle.Messages("CTL_ProjectAntRunJ2SE=Run w/jacocoverage")
@SuppressWarnings("CloneableImplementsClone")
public class ProjectAntRunJ2SE
        extends ActionJacocoOnAntTaskJ2SE
        implements ContextAwareAction, Presenter.Popup {

    private static final long serialVersionUID = 1L;

    // NOTE WARNING: this class is invoked by the IDE in a manner that DOES NOT
    // SUPPORT class variables.
    //
    // Although below was a good idea and typical, the IDE acts like a singleton 
    // for this class and as such changing projects resulted in attempting to 
    // use OLD settings as it called the methods as if a singleton.
    //
    // THUS: any of these data items are acquired in each method.
    // 
    //    private final Project nbProject;
    //    private final boolean isJ2seType;
    //    private final boolean isNbModuleType;
    //
    /**
     * Create the project context menu Run w/jacocoverage item.
     */
    @SuppressWarnings("NestedAssignment")
    public ProjectAntRunJ2SE() {
        super("run");
        // run is done at the project view/level of actions, so the project
        // is selected
        Project nbProject = IdeProjectJacocoverageConfig.getSelectedNbProject();

        // run may occur of J2Se as it has a main (nd if it has a main.class) OR
        // on a NbMOdule or NbModule suite
        boolean isJ2seType = NBUtils.isProjectSupported(nbProject,
                J2SE);
        boolean isNbModuleType = NBUtils.isProjectSupported(nbProject,
                NBMODULE,
                NBMODULE_SUITE);

        // enabled for the dynamic controls and the such
        super.setEnabled(isJ2seType || isNbModuleType);

        super.putValue(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
        super.putValue(Action.NAME, Bundle.CTL_ProjectAntRunJ2SE());
    }

    /**
     * is the J2SE project supporting a main.class project.property
     *
     * @param nbProject Netbeans project to look at
     *
     * @return true if a main.class defined
     */
    private boolean isJ2SeMainClassEnabled() {
        Project nbProject = IdeProjectJacocoverageConfig.getSelectedNbProject();

        // process the project.properties files for  main class being set
        FileObject prjPropsFo = nbProject.getProjectDirectory().getFileObject("nbproject/project.properties");

        // assume all things may fail
        boolean returnCondition = false;

        // the file must exist
        // null would be strange for a J2SE project
        if (prjPropsFo != null) {
            // file is missing?
            if (!prjPropsFo.isVirtual()) {

                // load the properties
                final Properties prjProps = new Properties();
                InputStream ins = null;
                try {
                    prjProps.load(ins = prjPropsFo.getInputStream());

                    // if the there is a main.class and a setting (not isEmpty)
                    returnCondition = !prjProps.getProperty("main.class", "").isEmpty();

                } catch (IOException ex) {
                    // fail, nothing that we can do
                    Exceptions.printStackTrace(ex);

                } finally {
                    try {
                        if (ins != null) {
                            ins.close();
                        }
                    } catch (IOException ex) {
                        // info only
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        }
        return returnCondition;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void actionPerformed(ActionEvent ev
    ) {
        if (isEnabled()) {
            super.actionPerformed(ev);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Action createContextAwareInstance(Lookup context) {
        // run is done at the project view/level of actions, so the project
        // is selected
        Project nbProject = IdeProjectJacocoverageConfig.getSelectedNbProject();

        // run may occur of J2Se as it has a main (nd if it has a main.class) OR
        // on a NbMOdule or NbModule suite
        boolean isJ2seType = NBUtils.isProjectSupported(nbProject, J2SE);
        // for J2SE need to determine if the main.class is set and adjust
        // the enabled state appropriately
        super.setEnabled(false);

        if (isJ2seType) {
            super.setEnabled(isJ2SeMainClassEnabled());
        }
        return new ProjectAntRunJ2SE();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public JMenuItem getPopupPresenter() {
        JMenuItem menuitem = new JMenuItem(this);
        menuitem.putClientProperty(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
        return menuitem;
    }
}
