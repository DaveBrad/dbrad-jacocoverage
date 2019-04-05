/* Copyright (c) 2017 dbradly. All rights reserved. */
package dbrad.jacocoverage.menuctxt.action;

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

@ActionID(
        category = "Test",
        id = "dbrad.jacocoverage.menuctxt.action.EditorContextFileTestWithJacocoverageAntTestSE"
)
@ActionRegistration(
        displayName = "#CTL_EditorContextFileTestWithJacocoverageAntTestSE",
        lazy = false,
        asynchronous = true,
        surviveFocusChange = true
)
@ActionReferences({
    @ActionReference(path = "Editors/text/x-java/Popup", 
            position = 1792)
    ,
    @ActionReference(path = "Editors/text/x-testng+xml/Popup", 
            position = 1792, 
            separatorBefore = 1791)
})
//??@ActionReference(path = "Editors/text/x-java/Popup", position = 1792)
@Messages("CTL_EditorContextFileTestWithJacocoverageAntTestSE=Test File w/jacocoverage")
public final class EditorContextFileTestWithJacocoverageAntTestSE
        extends ActionJacocoOnAntTaskJ2SE
        implements ActionListener, ContextAwareAction, Presenter.Menu {

    private static final long serialVersionUID = 0L;

    /** The project this context item associates with */
    protected Project nbContextProject;

    /** The file-object that is being action requested upon */
    protected FileObject contextFileObj;

    /** The file-object is in a test-source-directory indicator */
    protected boolean isTestDir = false;

    public EditorContextFileTestWithJacocoverageAntTestSE() {
        super("test-single");

        putValue(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
        putValue(Action.NAME, Bundle.CTL_EditorContextFileTestWithJacocoverageAntTestSE());
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (isEnabled()) {
            super.actionPerformed(ev);
        }
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

        this.setEnabled(this.isTestDir);

        // isTestDir is set so the menu item only applies to test-source-dir files
        return this;
    }

    @Override
    public JMenuItem getMenuPresenter() {
        return new JMenuItem(this);
    }
}
