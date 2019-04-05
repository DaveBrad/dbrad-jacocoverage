/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package packg.appfunc;

import packg.zoperation.tstenv.PrepareProject;
import static dbrad.jacocofpm.config.IdeProjectJacocoverageConfig.JACOCO_JSON_FILE_PATH;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.tree.TreePath;
import org.netbeans.jellytools.Bundle;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JRadioButtonOperator;
import org.netbeans.jemmy.operators.JTabbedPaneOperator;
import org.netbeans.jemmy.operators.JTableOperator;
import org.netbeans.jemmy.operators.JTextAreaOperator;
import org.netbeans.jemmy.operators.JTreeOperator;
import packg.zoperation.tstenv.TestBasicUtils;

/**
 * The project-specific is a tabbed pane with many tabs
 * <p>
 * General
 * <p>
 * &lt;main tab for project as named*gt;
 * <p>
 * Associated projects
 * <p>
 * Excludes packages
 * <p>
 * Additional packages
 *
 * @author dbradley
 */
public class FuncProjectProperties implements WindowListener {

    /**
     *
     */
    protected static String ASSOCIATED_PROJECTS = null;
    /**
     *
     */
    protected static String DBRAD_JACOCOVERAGE = "JaCoCoverage-dbrad";

    private static ArrayList<FuncProjectProperties> openedFcppList = new ArrayList<>();
    /**
     *
     */
    protected FuncProjectProperties fcPpInternal;

    /**
     *
     */
    protected JDialogOperator projPropertiesDialog;
    /**
     *
     */
    protected JTreeOperator catTree;

    /**
     *
     */
    protected JRadioButtonOperator globalsRadio;
    /**
     *
     */
    protected JRadioButtonOperator projectSpecRadio;

    private PrepareProject prepareProject;
    private boolean hasClosed;

    private File jsonFile;

    JTabbedPaneOperator projSpecificTabbPane;

    FuncGeneralTab generalTabb;
    FuncExcludeTab excludeTabb;
    FuncPkgFilterTable rootFuncPkgFilterTable;
    FuncAssociateProjectsTabb associateProjsTabb;

    /**
     *
     */
    protected JTextAreaOperator includesPkgTextarea;

    FuncProjectProperties() {
        // do nothing
    }

    /**
     *
     * @param prepareProject
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public FuncProjectProperties(PrepareProject prepareProject) {
        if (ASSOCIATED_PROJECTS == null) {
            ASSOCIATED_PROJECTS = Bundle.getString("dbrad.jacocoverage.plugin.config.projconfig.Bundle",
                    "PrjcfgAntJavasePanel.jPanelAssociatedProjects.TabConstraints.tabTitle");
        }

        this.prepareProject = prepareProject;
        this.fcPpInternal = this;

        openedFcppList.add(this);

        projPropertiesDialog = new JDialogOperator(
                String.format("Project Properties - %s", prepareProject.getRootProjName()));
        catTree = new JTreeOperator(projPropertiesDialog);

        JDialog dialog = (JDialog) projPropertiesDialog.getSource();

        dialog.addWindowListener(this);

        // depending on the project type, there maybe a dbrad-jacocoverage
        // menu item or not
        Object[] rootOfCatTree = catTree.getChildren(catTree.getRoot());

        boolean foundDbradJacocoTreeItem = false;

        for (Object o : rootOfCatTree) {
            String objStr = o.toString();
            if (objStr.equals(DBRAD_JACOCOVERAGE)) {
                foundDbradJacocoTreeItem = true;
                break;
            }
        }
        if (foundDbradJacocoTreeItem) {
            // place the dialog in context
            selectJacocoverageDbrad();
        }
    }

    /** nojdoc */
    private void selectJacocoverageDbrad() {

        catTree.clearSelection();
        TreePath treePath = catTree.findPath(DBRAD_JACOCOVERAGE);

        // if the project has a json file, we can avoid doing the popup and 
        // reset action thread below
        this.jsonFile = prepareProject.getForProjNameFileItem(prepareProject.getRootProjName(), JACOCO_JSON_FILE_PATH);

        if (!this.jsonFile.isFile()) {
            // a dialog popup will cause the following click to freeze 
            // test code execution catTree.clickOnPath(treePath);
            //
            // so do the click in a a thread for the popup dialog
            Thread someThread = new Thread() {

                @Override
                public void run() {
                    pauseMs(1000);
                    // if the project has no JSON data file then a popup dialog pops up indicating 
                    // that a new file will be created
                    try {
                        JDialogOperator dialogOpWarning = new JDialogOperator("Warning");

                        JButtonOperator dialogOkButton = new JButtonOperator(dialogOpWarning, "OK");
                        dialogOkButton.clickMouse();

                    } catch (org.netbeans.jemmy.TimeoutExpiredException te) {
                        // there is the possibilty
                    }
                }
            };
            // Start a thread to respond to a popup message dialog that is expected
            someThread.start();
        }
        catTree.clickOnPath(treePath);
    }

    /** nojdoc
     *
     * @param milliSeconds */
    final public void pauseMs(int milliSeconds) {
        TestBasicUtils.pauseMs(milliSeconds);
    }

    /** nojdoc
     *
     * @return */
    public JDialogOperator operator() {
        hasClosedCondition();

        return this.projPropertiesDialog;
    }

    /** nojdoc
     *
     * @return */
    public JTabbedPaneOperator tabbPaneOperator() {
        hasClosedCondition();

        if (this.projSpecificTabbPane == null) {
            this.projSpecificTabbPane = new JTabbedPaneOperator(this.projPropertiesDialog);
        }
        return this.projSpecificTabbPane;
    }

    /** nojdoc
     *
     * @return */
    public JRadioButtonOperator radioGlobal() {
        hasClosedCondition();

        if (globalsRadio == null) {
            String sText = Bundle.getString("dbrad.jacocoverage.plugin.config.projconfig.Bundle",
                    "PrjcfgAntJavasePanel.jRadioButtonUseGlobalOptions.text");
            globalsRadio = new JRadioButtonOperator(projPropertiesDialog, sText);
        }
        return globalsRadio;
    }

    /** nojdoc
     *
     * @return */
    public JRadioButtonOperator radioProjectSpecific() {
        hasClosedCondition();

        if (projectSpecRadio == null) {
            String sText = Bundle.getString("dbrad.jacocoverage.plugin.config.projconfig.Bundle",
                    "PrjcfgAntJavasePanel.jRadioButtonUseProjectSpecificOptions.text");

            projectSpecRadio = new JRadioButtonOperator(projPropertiesDialog, sText);
        }
        return projectSpecRadio;
    }

    /** nojdoc
     *
     * @return */
    public boolean isProjectSpecific() {
        hasClosedCondition();

        return radioProjectSpecific().isSelected();
    }

    /** nojdoc
     *
     * @return */
    public String getIncludesTextArea() {
        hasClosedCondition();

        if (isProjectSpecific()) {
            if (includesPkgTextarea == null) {
                this.includesPkgTextarea = new JTextAreaOperator(this.projPropertiesDialog);
            }
            // return the string contents of the includes-package textarea
            return this.includesPkgTextarea.getText();
        }
        throw new AssertionError("Not in project specific radio setting.");
    }

    /** nojdoc
     *
     * @return */
    public FuncAssociateProjectsTabb funcAssociateProjects() {
        hasClosedCondition();

        if (associateProjsTabb == null) {
            // create the object
            associateProjsTabb = new FuncAssociateProjectsTabb(projPropertiesDialog);

            associateProjsTabb.projPropertiesDialog = this.projPropertiesDialog;
        }
        return associateProjsTabb;
    }

    /** nojdoc
     *
     * @return */
    public FuncPkgFilterTable funcRootTable() {
        hasClosedCondition();

        if (rootFuncPkgFilterTable == null) {

            JTabbedPaneOperator tabpane1 = new JTabbedPaneOperator(projPropertiesDialog);
            tabpane1.selectPage(this.prepareProject.getRootProjName());

            JTableOperator jtableOperator = new JTableOperator(tabpane1);

            rootFuncPkgFilterTable = new FuncPkgFilterTable(jtableOperator, true);
        }
        return rootFuncPkgFilterTable;
    }

    /** nojdoc
     *
     * @return */
    public FuncGeneralTab funcGeneralTabb() {
        hasClosedCondition();

        if (generalTabb == null) {
            JTabbedPaneOperator tabpane1 = new JTabbedPaneOperator(projPropertiesDialog);
            generalTabb = new FuncGeneralTab(prepareProject, tabpane1);

            generalTabb.projPropertiesDialog = this.projPropertiesDialog;
        }
        return generalTabb;
    }

    /** nojdoc
     *
     * @return */
    public FuncExcludeTab funcExcludeTabb() {
        hasClosedCondition();

        if (this.excludeTabb == null) {
            JTabbedPaneOperator tabpane1 = new JTabbedPaneOperator(projPropertiesDialog);
            this.excludeTabb = new FuncExcludeTab(prepareProject, tabpane1);

            this.excludeTabb.projPropertiesDialog = this.projPropertiesDialog;
        }
        return this.excludeTabb;
    }

    /** nojdoc
     *
     * @return */
    public JButtonOperator buttonCancel() {
        hasClosedCondition();

        JButtonOperator butt = new JButtonOperator(projPropertiesDialog, "Cancel");
        butt.clickMouse();
        return butt;
    }

    /** nojdoc
     *
     * @return */
    public JButtonOperator buttonOK() {
        hasClosedCondition();

        JButtonOperator butt = new JButtonOperator(projPropertiesDialog, "OK");
        butt.clickMouse();
        return butt;
    }

    /** nojdoc */
    final protected void hasClosedCondition() {
        // may not use this object as it is closed
        if (hasClosed) {
            throw new RuntimeException("The project-properties has closed: thus instance is invalid.");
        }
    }

    @Override
    final public void windowOpened(WindowEvent e) {
        /// do nothing
    }

    /** nojdoc */
    private void closer() {
        hasClosed = true;
    }

    @Override
    final public void windowClosing(WindowEvent e) {
        closer();
    }

    @Override
    final public void windowClosed(WindowEvent e) {
        closer();
    }

    @Override
    final public void windowIconified(WindowEvent e) {
        /// do nothing
    }

    @Override
    final public void windowDeiconified(WindowEvent e) {
        /// do nothing
    }

    @Override
    final public void windowActivated(WindowEvent e) {
        /// do nothing    
    }

    @Override
    final public void windowDeactivated(WindowEvent e) {
        /// do nothing    
    }

    /** nojdoc
     *
     * @param nbProjectName
     *
     * @return */
    public FuncPkgFilterTable getPfJTableForProjectName(String nbProjectName) {
        FuncAssociateProjectsTabb assocTabb = funcAssociateProjects();

        if (this.rootFuncPkgFilterTable == null) {
            this.funcRootTable();
        }

        ArrayList<FuncPkgFilterTable> pkgFilterTableArr
                = assocTabb.getAssociatePfJTablesOperators();

        pkgFilterTableArr.add(0, this.rootFuncPkgFilterTable);

        for (FuncPkgFilterTable fcPft : pkgFilterTableArr) {
            fcPft.projPropertiesDialog = this.projPropertiesDialog;
        }

        for (FuncPkgFilterTable fcPft : pkgFilterTableArr) {
            if (fcPft.getProjNameOfTable().equals(nbProjectName)) {
                return fcPft;
            }
        }
        throw new RuntimeException(String.format("Project name unknown:%s\n", nbProjectName));
    }

    /** nojdoc
     *
     * @return */
    public ArrayList<FuncPkgFilterTable> getAllPfJTablesOperatorsFromProjProp() {
        FuncPkgFilterTable rootTable = funcRootTable();

        FuncAssociateProjectsTabb assocTabb = funcAssociateProjects();

        ArrayList<FuncPkgFilterTable> pkgFilterTableArr = assocTabb.getAssociatePfJTablesOperators();

        pkgFilterTableArr.add(0, rootTable);

        return pkgFilterTableArr;
    }

    /** nojdoc
     *
     * @return */
    public String getJsonFilePath() {
        return this.jsonFile.getAbsolutePath();
    }

    /** nojdoc
     *
     * @return */
    public FuncPkgFilterTable getActiveTable() {
        ArrayList<FuncPkgFilterTable> allTables = getAllPfJTablesOperatorsFromProjProp();

        for (FuncPkgFilterTable tab : allTables) {
            if (tab.isSelectedTable()) {
                return tab;
            }
        }
        return null;
    }

    /** nojdoc */
    public static void closeHangingDialogs() {

        for (FuncProjectProperties fcPP : openedFcppList) {

            if (!fcPP.hasClosed) {
                try {
                    fcPP.buttonCancel();
                } catch (Exception waitE) {
                    // most likely a WaitComponentTimerout condition, but
                    // there is nothing that may be done
                }
            }
        }
        openedFcppList.clear();
    }
}
