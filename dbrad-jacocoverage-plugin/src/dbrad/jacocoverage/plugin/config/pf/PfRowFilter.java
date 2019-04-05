/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocoverage.plugin.config.pf;

import dbrad.jacocofpm.mgr.com.FilePackageOfSrc;
import javax.swing.RowFilter;

/**
 *
 * @author dbradley
 */
@SuppressWarnings("rawtypes")
public class PfRowFilter extends RowFilter {

    @Override
    public boolean include(Entry entry) {
        Integer rowIdx = (Integer) entry.getIdentifier();
        PfTableModel model = (PfTableModel) entry.getModel();

        FilePackageOfSrc fileData = (FilePackageOfSrc) model.getValueAt(rowIdx, PfTableModel.PKGFLTR_FILE_HIDDEN);

        boolean rowFilterBool = (fileData.isBaseFile() || fileData.isOnCodeFolderState());
        return rowFilterBool;
    }
}
