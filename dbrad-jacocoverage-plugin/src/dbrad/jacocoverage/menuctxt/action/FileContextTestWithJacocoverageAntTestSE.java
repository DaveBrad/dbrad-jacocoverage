/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocoverage.menuctxt.action;

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
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

/**
 * Class for the Test File w/Jacocoverage menu-item action on test-source-code
 * directory individual files.
 *
 * @author dbradley
 */
@ActionID(
        category = "Test",
        id = "dbrad.jacocoverage.menuctxt.action.FileContextTestWithJacocoverageAntTestSE"
)
@ActionRegistration(displayName = "#CTL_FileContextTestWithJacocoverageAntTestSE",
        lazy = false,
        asynchronous = true,
        surviveFocusChange = true)
@ActionReferences({
    @ActionReference(path = "Loaders/text/x-java/Actions",
            position = 1290)
    ,
    @ActionReference(path = "Loaders/text/x-testng+xml/Actions", 
            // position 260-269 between Test File and Debug Test File
            // position 270-279? after Debug Test File
            position = 275)  
})
@Messages("CTL_FileContextTestWithJacocoverageAntTestSE=Test File w/jacocoverage")
public final class FileContextTestWithJacocoverageAntTestSE
        extends ActionJacocoOnAntTaskJ2SE
        implements ActionListener, ContextAwareAction, Presenter.Popup {

    private static final long serialVersionUID = 0L;

    /** The project this context item associates with */
    protected Project nbContextProject;

    /** The file-object that is being action requested upon */
    protected FileObject contextFileObj;

    /** The file-object is in a test-source-directory indicator */
    protected boolean isTestDir = false;

    /**
     * Create the TEST file context menu instance for all test file
     * w/jacocoverage.
     */
    public FileContextTestWithJacocoverageAntTestSE() {
        super("test-single");

        putValue(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
        putValue(Action.NAME, Bundle.CTL_FileContextTestWithJacocoverageAntTestSE());
    }

    /**
     * {
     *
     * @inherirDoc}
     */
    @Override
    public Action createContextAwareInstance(Lookup context) {

        // lookup the file that is doing a file-context action
        this.contextFileObj = context.lookup(FileObject.class);

        // get the NB project and then check if the file-in-context is a test directory
        this.nbContextProject = NbUtilsPlugin.getNbProjectForNbFileObject(contextFileObj);

        this.isTestDir = NbUtilsPlugin
                .isFileObjectInTestDir(this.nbContextProject, this.contextFileObj);

        // isTestDir is set so the menu item only applies to test-source-dir files
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JMenuItem getPopupPresenter() {
        JMenuItem menuitem = new JMenuItem(this);
        menuitem.putClientProperty(DynamicMenuContent.HIDE_WHEN_DISABLED, true);

        // both a test-source-directory and a valid netbeans Java project
        // support the menuitem
        boolean areValidTypesForMenuitem
                = this.isTestDir && NBUtils.isProjectSupported(this.nbContextProject,
                        NBProjectTypeEnum.J2SE, J2EE, J2EE_EAR,
                        J2EE_EJB, J2EE_WEB, NBMODULE, NBProjectTypeEnum.NBMODULE_SUITE);

        // set the enabled condition for the menuitem (in Netbeans dynamic-mneu-content
        // disable will also hide the menuitem too for this implementation
        menuitem.setEnabled(areValidTypesForMenuitem);

        return menuitem;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (isEnabled()) {
            super.actionPerformed(ev);
        }
    }
}
