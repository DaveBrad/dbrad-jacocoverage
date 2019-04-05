/* Copyright (c) 2017 dbradly. All rights reserved. */
package dbrad.jacocoverage.menuctxt.action;

import dbrad.jacocofpm.config.IdeProjectJacocoverageConfig;
import dbrad.jacocofpm.util.NBProjectTypeEnum;
import static dbrad.jacocofpm.util.NBProjectTypeEnum.J2EE;
import static dbrad.jacocofpm.util.NBProjectTypeEnum.J2EE_EAR;
import static dbrad.jacocofpm.util.NBProjectTypeEnum.J2EE_EJB;
import static dbrad.jacocofpm.util.NBProjectTypeEnum.J2EE_WEB;
import static dbrad.jacocofpm.util.NBProjectTypeEnum.NBMODULE;
import dbrad.jacocofpm.util.NBUtils;
import dbrad.jacocoverage.plugin.util.NbUtilsPlugin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JMenuItem;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.awt.DynamicMenuContent;
import org.openide.filesystems.FileObject;
import org.openide.util.ContextAwareAction;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

/**
 * Class for the RUN FILE W/jacocoverage for a file menu context.
 *
 * @author dbradley
 */
@ActionID(
        category = "Test",
        id = "dbrad.jacocoverage.menuctxt.action.reqproc.FileContextRunWithJacocoverageAntRunSE"
)
@ActionRegistration(
        displayName = "#CTL_FileContextRunWithJacocoverageAntRunSE",
        lazy = false,
        asynchronous = true,
        surviveFocusChange = true)
@ActionReferences({
    @ActionReference(path = "Loaders/text/x-java/Actions", 
            position = 1285)
    ,
    @ActionReference(path = "Loaders/text/xml/Actions", 
            position = 1285)
})
@Messages("CTL_FileContextRunWithJacocoverageAntRunSE=Run File w/jacocoverage")
public final class FileContextRunWithJacocoverageAntRunSE
        extends ActionJacocoOnAntTaskJ2SE
        implements ContextAwareAction, Presenter.Popup, ActionListener {

    private static final long serialVersionUID = 0L;

    /** The project this context item associates with */
    private Project nbContextProject;

    /** The file-object that is being action requested upon */
    private FileObject contextFileObj;

    /** The file-context as a string to match with main.class project.properties */
    private String filesSrcCodeFolderPath;

    /** The file-object is in a test-source-directory indicator */
    private boolean isTestDir = false;

    /**
     * Create the RUN file context menu instance for all run file
     * w/jacocoverage.
     */
    public FileContextRunWithJacocoverageAntRunSE() {
        super("run");

        putValue(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
        putValue(Action.NAME, Bundle.CTL_FileContextRunWithJacocoverageAntRunSE());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Action createContextAwareInstance(Lookup context) {

        // lookup the file that is doing a file-context action
        this.contextFileObj = context.lookup(FileObject.class);

        // get the NB project and then check if the file-in-context is a test directory
        this.nbContextProject = NbUtilsPlugin.getNbProjectForNbFileObject(contextFileObj);

        this.isTestDir = NbUtilsPlugin
                .isFileObjectInTestDir(this.nbContextProject, this.contextFileObj);

        this.filesSrcCodeFolderPath = NbUtilsPlugin
                .getFilesSrcCodeFolderPath(this.nbContextProject, this.contextFileObj);

        // isTestDir is set so the menu item only applies to test-source-dir files
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JMenuItem getPopupPresenter() {

        // dynamic-hide the menu-item settings
        JMenuItem menuitem = new JMenuItem(this);
        menuitem.putClientProperty(DynamicMenuContent.HIDE_WHEN_DISABLED, true);

        // determine hide or show (assume hide)
        boolean showMenuitemDynamic;
        try {
            // determine if the file selected is the main.class
            boolean mainClzSelected = false;

            if (this.filesSrcCodeFolderPath != null) {
                // get the IDE's configuration representation so as to get the
                // 'main.class' project.property
                IdeProjectJacocoverageConfig idePrgConfig
                        = IdeProjectJacocoverageConfig.createForGivenProject(this.nbContextProject);

                // format is 'pck1.pack2.Classname' (pck are optional)
                String mainClz = idePrgConfig.getProperty("main.class");

                // may only process if the main class is defined
                if (mainClz != null) {
                    if (!mainClz.isEmpty()) {
                        // need to match the main-class with the context-file path
                        // which requires some manipulation for its path
                        // [format is: 'dir1/dir2/src/pck1/pck2/Classname.java' )
                        //
                        String contextFilePath = this.contextFileObj.getPath();

                        contextFilePath = contextFilePath
                                .substring(this.filesSrcCodeFolderPath.length(), // project and src dir
                                        contextFilePath.length() - 5 // .java ending removed
                                );
                        contextFilePath = NbUtilsPlugin.convertPathToFwdSlash(contextFilePath).replaceAll("/", ".");

                        // the formats for the main-class and the file-context 
                        // should now match
                        mainClzSelected = contextFilePath.equals(mainClz);
                    }
                }
            }
            // all matches means run file w/jacocoverage may be unhidden
            // 1) is source-directory, and 
            // 2) a valid netbeans Java project-type, and
            // 3) main-class support the menuitem
            showMenuitemDynamic
                    = // (1)
                    !this.isTestDir
                    // (2)
                    && NBUtils.isProjectSupported(this.nbContextProject,
                            NBProjectTypeEnum.J2SE, J2EE, J2EE_EAR, J2EE_EJB, J2EE_WEB, NBMODULE, NBProjectTypeEnum.NBMODULE_SUITE)
                    // (3)
                    && mainClzSelected;

        } catch (IOException ex) {
            // failed to access the project.properties file, assume hidden
            Exceptions.printStackTrace(ex);
            showMenuitemDynamic = false;
        }
        // set the enabled condition for the menuitem (in Netbeans dynamic-menu-content
        // disable will also hide the menuitem too for this implementation
        menuitem.setEnabled(showMenuitemDynamic);

        return menuitem;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (isEnabled()) {
            super.actionPerformed(ev);
        }
    }
}
