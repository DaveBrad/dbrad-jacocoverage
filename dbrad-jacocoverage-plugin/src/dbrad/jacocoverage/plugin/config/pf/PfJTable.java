/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocoverage.plugin.config.pf;

import dbrad.jacocofpm.mgr.com.FilePackageCoverStateEnum;
import static dbrad.jacocofpm.mgr.com.FilePackageCoverStateEnum.COVER_NO;
import static dbrad.jacocofpm.mgr.com.FilePackageCoverStateEnum.COVER_YES;
import dbrad.jacocofpm.mgr.com.FilePackageOfSrc;
import dbrad.jacocofpm.mgr.com.FileSrcCodeDir;
import dbrad.jacocoverage.plugin.config.JTableConfig;
import dbrad.jacocoverage.plugin.config.JTableConfigDatumObj;
import dbrad.jacocofpm.json.Json1ProjLevel;
import dbrad.jacocofpm.json.Json3PackageFormat;
import dbrad.jacocofpm.json.Json2ProjSrcCodeFolder;
import dbrad.jacocofpm.json.Json4Data;
import dbrad.jacocofpm.config.IdeProjectJacocoverageConfig;
import dbrad.jacocoverage.plugin.config.projconfig.PrjcfgAntJavasePanel;
import dbrad.jacocoverage.plugin.util.NbUtilsPlugin;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author dbradley
 */
public class PfJTable extends JTableConfig implements ActionListener {

    private static final long serialVersionUID = 0L;

    public PrjcfgAntJavasePanel javaSePanel;
    private String nbProjectName;

    public PfJTable() {
        // allocate by 

    }

    @Override
    @SuppressWarnings({"unchecked", "ResultOfObjectAllocationIgnored"})
    public void setupAndBuild(JTableConfigDatumObj datum) {

        // turn of the header sorting capability of the table by
        // disabling the sorter
        final JTable self = this;
        //here you make the click avaible ONLY on Table Header 
        this.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                // the left mouse click causes the sort, which we wish to
                // disable
                if (SwingUtilities.isLeftMouseButton(me)) {
                    // disabling the sorting   
                    self.setRowSorter(null);
                }
            }
        });

        // need to explicitly control the cell focus of the table for forward
        // and backward
        // (Provided for support of accessibility.)
        new PfTableCellFocusControlAction(this, KeyStroke.getKeyStroke("TAB"));
        new PfTableCellFocusControlAction(this, KeyStroke.getKeyStroke("shift TAB"));
        
        new PfTableCellAccessiblityAction(this, KeyStroke.getKeyStroke("SPACE"));

        // now construct the table and its needs
        super.setupAndBuild(datum);

        PfJTableDatum myDatum = (PfJTableDatum) datum;

        // change the setting of the tab's string
        // find my tabbed pane and set the name
        JTabbedPane tabPane = findMyTabbedPane(this);
        try {
            int idex = tabPane.indexOfTab("Package filter");
            this.nbProjectName = myDatum.nbProjectJacoco.getNbProjectDisplayName();

            if (idex != -1) {
                tabPane.setTitleAt(idex, this.nbProjectName);
            }
        } catch (ClassCastException ce) {
            // ce.setMEssage("Internal error: code refactor has exceeded a limit.");
            throw ce;
        }
        // find the PrjcfgAntJavasePanel for message propagation 
        Component comp = this;

        while (!(comp instanceof PrjcfgAntJavasePanel)) {
            comp = comp.getParent();
        }
        this.javaSePanel = (PrjcfgAntJavasePanel) comp;

        // create the table model, and set settings for columns, cell renderering
        // and a better row height to deal with underscores
        PfTableModel pkgFltrModel = new PfTableModel(this);

        this.setModel(pkgFltrModel);

        // create a filter for the row so as to hide hidden ones
        TableRowSorter<PfTableModel> sorter = new TableRowSorter<>(pkgFltrModel);
        sorter.setRowFilter(new PfRowFilter());

        this.setRowSorter(sorter);

        // need to listen for mouse event for certain columns
        this.addMouseListener(pkgFltrModel);

        // cell renderers for columns of the class types below
        this.setDefaultRenderer(Boolean.class, new PfCellRndrOnCodeFolderState());
        this.setDefaultRenderer(FilePackageCoverStateEnum.class, new PfCellRndrCoverCol());
        this.setDefaultRenderer(boolean.class, new PfCellRndrSrcTypeCol());
        this.setDefaultRenderer(String.class, new PfCellRndrPackageFormatCol());

        // the layout/presentation settings of the columns and rows
        pkgFltrModel.setColumnSettingsOfTable();

        this.setRowHeight(21);

        this.setVisible(true);
    }

    final public String getNbProjectName() {
        return this.nbProjectName;
    }

    @Override
    public boolean loadTableFromProjectJson(IdeProjectJacocoverageConfig prjCfg) {
        boolean jsonVersusMemoryData = false;

        PfTableModel pfModel = (PfTableModel) this.getModel();
        PfJTableDatum myDatum = (PfJTableDatum) this.datum;

        Json1ProjLevel allJsonDataPackages = prjCfg.jsonMapData.getProjectIncludePackages();

        ArrayList<String> excludesList
                = NbUtilsPlugin.getExcludesPatternsAsArrayList(prjCfg.nbProjectJacoco.getPrincipleNbProject());

        // the loading depends on Json-data or nothing present, assume JSON for
        // now
        boolean loadViaJson = true;
        // the Json file has no data so treat as select-all, or if the project to display
        // has no associated JSON data
        if (allJsonDataPackages.isEmpty()) {
            loadViaJson = false;
            jsonVersusMemoryData = true;
        }
        if (!allJsonDataPackages.containsKey(myDatum.nbProjectJacoco.getNbProjectDisplayName())) {
            loadViaJson = false;
            jsonVersusMemoryData = true;
        }
        // packages are within a source-dir (src or test)
        ArrayList<PfItem> packageListUniqueArr = new ArrayList<>(6);
        ArrayList<String> packageFormatUniqueArr = new ArrayList<>(6);

        for (FileSrcCodeDir srcCodeFile : myDatum.nbProjectJacoco.getSrcCodeDirPairList()) {
            // now process the data that is going to be shown to the table 
            // (for each source-code-folder), ensuring
            // the package-format is unique within the collection
            packageFormatUniqueArr.clear();

            ArrayList<FilePackageOfSrc> packagesList = srcCodeFile.getListOfPackagesFromRoot();

            for (FilePackageOfSrc filePackageOfSrc : packagesList) {
                //
                boolean select;
                boolean onOffCodeSelectState;

                String pkgDisplay = filePackageOfSrc.getPackageFormat();

                // test on the package-format for each sub-directory of the
                // source-code-folder
                if (packageFormatUniqueArr.contains(pkgDisplay)) {
                    continue;
                }

                if (loadViaJson) {
                    // determine if the data is set for the source-code file
                    // and package
                    select = false;

                    // the onCodeSelectState is false for test-source-code-dirs
                    // but may be overridden by the JSON-configuration file
                    onOffCodeSelectState = !filePackageOfSrc.isTestSrcCode();

                    String srcFolderName = filePackageOfSrc.getSrcCodeDirBase().getSrcFolderDirDisplayName();

                    Json2ProjSrcCodeFolder json2SrcCodeFolder = allJsonDataPackages.get(myDatum.nbProjectJacoco.getNbProjectDisplayName());

                    // if we have the data then use the settings from the JSON-configuration.
                    if (json2SrcCodeFolder.containsKey(srcFolderName)) {
                        Json3PackageFormat packageWithinSrcHash = json2SrcCodeFolder.get(srcFolderName);

                        String packageString = filePackageOfSrc.getPackageFormat();
                        if (packageWithinSrcHash.containsKey(packageString)) {

                            Json4Data pfJsonDataItem = packageWithinSrcHash.get(packageString);

                            if (pfJsonDataItem.getPfCvr() == COVER_YES) {
                                select = true;
                            }
                            onOffCodeSelectState = pfJsonDataItem.isOn();

                            filePackageOfSrc.setOnCodeFolderState(pfJsonDataItem.isOn());
                        }
                    }
                    jsonVersusMemoryData = true;
                } else {
                    // load NOT Json-data
                    select = true;
                    onOffCodeSelectState = !filePackageOfSrc.isTestSrcCode();

                    jsonVersusMemoryData = true;

                    // for the creation of asscicate tables the inital setting for the
                    // onOff state should be off
                    if (!myDatum.nbProjectJacoco.isPrincipleProject()) {
                        onOffCodeSelectState = false;
                    }
                }
                // drop/filter duplicate package-names already ignored
                filePackageOfSrc.setCoverState(COVER_NO);
                packageListUniqueArr.add(new PfItem(onOffCodeSelectState, select, filePackageOfSrc));

                packageFormatUniqueArr.add(pkgDisplay);

                filePackageOfSrc.setExcludePackages(excludesList);
            }
        }
        // have all the packages that need to be procsssed
        //
        // post the src.dir's first and then the test.src.dir's
        packageListUniqueArr.forEach((pfi) -> {
            FilePackageCoverStateEnum coverState;
            coverState = (pfi.selectState) ? COVER_YES : COVER_NO;

            boolean isTestCode = pfi.srcPackageFile.isTestSrcCode();

            if (!isTestCode) {
                pfModel.addRow(pfi.onCodeSelectState,
                        coverState, isTestCode,
                        pfi.srcPackageFile.getSrcFolderDirDisplayName(),
                        pfi.srcPackageFile.getPackageFormat(),
                        pfi.srcPackageFile
                );
            }
        });
        // all test directory sources will be set not covered initially
        packageListUniqueArr.forEach((pfi) -> {
            FilePackageCoverStateEnum coverState;
            coverState = (pfi.selectState) ? COVER_YES : COVER_NO;

            boolean isTestCode = pfi.srcPackageFile.isTestSrcCode();

            if (isTestCode) {
                pfModel.addRow(pfi.onCodeSelectState,
                        coverState, isTestCode,
                        pfi.srcPackageFile.getSrcFolderDirDisplayName(),
                        pfi.srcPackageFile.getPackageFormat(),
                        pfi.srcPackageFile);
            }
        });

        return jsonVersusMemoryData;
    }

    Json2ProjSrcCodeFolder createJson2MapFromProjFilterTable() {
        return ((PfTableModel) this.getModel()).createJson2MapFromProjFilterTable();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // this is invoked by a popup menu to position the
        // file-package-of-source in the configuration display
        //
        // this allows affecting and affected file-package-of-source objects to
        // transition between the various tables and rows.
        //
        FilePackageOfSrc someFileInTable = (FilePackageOfSrc) e.getSource();

        // find file-package-of-source in the rows
        int rowCount = this.getRowCount();
        int foundRow = -1;
        for (int i = 0; i < rowCount; i++) {
            File tableRowFile = (File) this.getModel().getValueAt(i, PfTableModel.PKGFLTR_FILE_HIDDEN);

            if (someFileInTable == tableRowFile) {
                foundRow = i;
            }
        }
        // is the file an associated-project item
        if (!someFileInTable.nbProjectJacoco.isPrincipleProject()) {
            // it be an associated project and as such the main-associated-tab needs to 
            // selected before the files-pfTable tab
            JTabbedPane innerTabPane = findMyTabbedPane(this);
            JTabbedPane outerTabPane = findMyTabbedPane(innerTabPane);

            //99 need this to be localized
            int idex = outerTabPane.indexOfTab("Associated projects");
            outerTabPane.setSelectedIndex(idex);
        }

        this.scrollRectToVisible(new Rectangle(this.getCellRect(foundRow, 0, true)));

        selectMyTabAndScrollTo();
    }

    private JTabbedPane findMyTabbedPane(Component baseComp) {
        // change the setting of the tab's string
        // find my tabbed pane and set the name
        Component jComp = baseComp.getParent();
        int limitCount = 8;

        while (!(jComp instanceof JTabbedPane)) {
            jComp = jComp.getParent();
            limitCount--;
            if (limitCount < 1) {
                break;
            }
        }
        JTabbedPane tabPane = (JTabbedPane) jComp;

        return tabPane;
    }

    public void selectMyTabAndScrollTo() {
        JTabbedPane tabPane = findMyTabbedPane(this);

        PfJTableDatum myDatum = (PfJTableDatum) datum;
        int idex = tabPane.indexOfTab(myDatum.nbProjectJacoco.getNbProjectDisplayName());

        tabPane.setSelectedIndex(idex);
    }

    public static JLabel createLabelHasFocusObj(JLabel sourceLabel) {
        JLabel nuLabel = new JLabel("", SwingConstants.CENTER);
        nuLabel.setIcon(sourceLabel.getIcon());

        Border nuBorder = new LineBorder(Color.blue, 2);
        nuLabel.setBorder(nuBorder);

        return nuLabel;
    }
}
