/* Copyright (c) 2017 dbradly. All rights reserved. */
package dbrad.jacocoverage.plugin.config.ep;

import dbrad.jacocoverage.plugin.config.JTableConfig;
import dbrad.jacocoverage.plugin.config.JTableConfigDatumObj;
import static dbrad.jacocoverage.plugin.config.ep.EpTableModel.COL_EXCL_APPLY_BOOL;
import static dbrad.jacocoverage.plugin.config.ep.EpTableModel.COL_EXCL_PACKAGE_STRING;
import dbrad.jacocofpm.config.IdeProjectJacocoverageConfig;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.TableColumn;
import org.netbeans.api.project.Project;
import org.openide.util.Exceptions;

/**
 *
 * @author dbradley
 */
public class EpJTable extends JTableConfig {

    private static final long serialVersionUID = 0L;
    
//99    FocusTraversalPolicy focusPolicy;

    public EpJTable() {
        
        
    }

    @Override
    public void setupAndBuild(JTableConfigDatumObj datum) {
        super.setupAndBuild(datum);
        
//        this.focusPolicy = new FocusTraversalPolicy() {
//            @Override
//            public Component getComponentAfter(Container aContainer, Component aComponent) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public Component getComponentBefore(Container aContainer, Component aComponent) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public Component getFirstComponent(Container aContainer) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public Component getLastComponent(Container aContainer) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public Component getDefaultComponent(Container aContainer) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        }
        
        // 
        EpTableModel excludePackagesTableModel = new EpTableModel(this);
        this.setModel(excludePackagesTableModel);

        // Boolean checkbox
        TableColumn tabCol = this.getColumnModel().getColumn(EpTableModel.COL_EXCL_APPLY_BOOL);

        tabCol.setPreferredWidth(54);
        tabCol.setMinWidth(54);
        tabCol.setMaxWidth(54);

        //----------
        tabCol = this.getColumnModel().getColumn(EpTableModel.COL_EXCL_PACKAGE_STRING);

        tabCol.setPreferredWidth(0);
        tabCol.setMinWidth(0);
        tabCol.setMaxWidth(530);

        //----------
        tabCol = this.getColumnModel().getColumn(EpTableModel.COL_EXCL_APPLY_OVERRIDDEN_BY_SUPER);

        tabCol.setPreferredWidth(54);
        tabCol.setMinWidth(0);
        tabCol.setMaxWidth(54);

        this.removeColumn(tabCol);
        // - - - - 
        this.setDefaultRenderer(String.class, new EpTableCellRndrPkgStr());

        // to display the package-string which may have under-scores need to make
        // the row height taller than default
        this.setRowHeight(22);

        this.addMouseListener(excludePackagesTableModel);
    }

    @Override
    public boolean loadTableFromProjectJson(IdeProjectJacocoverageConfig prjCfg) {
        // get the data from the JSON file and apply onto the table
        Map<String, Boolean> excludePkgs = prjCfg.jsonMapData.getExcludePackages();

        EpTableModel epModel = (EpTableModel) this.getModel();

        // if no excludes data may not add-rows to the table
        if (excludePkgs != null) {
            excludePkgs.forEach((packageFormatKey, applyBoolean) -> {
                // data is coming from an assumed to be okay JSON table
                epModel.addRow(applyBoolean, packageFormatKey);

            });
        }
        // now that a lot of rows have been added add the listener for any update changes
        epModel.addTableModelListener(new EpTableModelListener());

        // now do a sort to ensure things are in order, without the model-listener
        // did not do a sort for each row added (optimization for loading data)
        // so doing the just loaded sort now is good practice
        epModel.sortRows();

        return false;
    }

    public HashMap<String, Boolean> excludesPackagesTableToMap(HashMap<String, Boolean> excludeHash) {
        excludeHash.clear();

        int nbLines = this.getRowCount();

        for (int row = 0; row < nbLines; row++) {
            // the string of the package to process
            String packageStringFromRow = (String) this.getValueAt(row, COL_EXCL_PACKAGE_STRING);  // key
            Boolean applySet = (Boolean) this.getValueAt(row, COL_EXCL_APPLY_BOOL);  // element

            excludeHash.put(packageStringFromRow, applySet);
        }
        return excludeHash;
    }

    public String getExcludesAsStrFromTable(Project project) {

        this.setupAndBuild(null);

        try {
            IdeProjectJacocoverageConfig prjCfg = IdeProjectJacocoverageConfig.reuseForProject(project);

            this.loadTableFromProjectJson(prjCfg);

            String testFrameExcludes = "";
            if (prjCfg.isExcludedOrgTestngSet()) {
                testFrameExcludes = "org.testng.*";
            }
            if (prjCfg.isExcludedOrgJunitSet()) {
                if (!testFrameExcludes.isEmpty()) {
                    testFrameExcludes += ":";
                }
                testFrameExcludes += "org.junit.*";
            }
            if (prjCfg.isExcludedJunitExtensionSet()) {
                if (!testFrameExcludes.isEmpty()) {
                    testFrameExcludes += ":";
                }
                testFrameExcludes += "junit.*";
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }
}
