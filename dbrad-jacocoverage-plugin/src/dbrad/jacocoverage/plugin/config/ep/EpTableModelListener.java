/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocoverage.plugin.config.ep;

import javax.swing.event.TableModelEvent;
import static javax.swing.event.TableModelEvent.UPDATE;
import javax.swing.event.TableModelListener;

/**
 *
 * @author dbradley
 */
public class EpTableModelListener implements TableModelListener {

    public EpTableModelListener() {
        // nothing special
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        // sort for an table change (delete, insert, update)
        // this is added or removed
        EpTableModel model = (EpTableModel) e.getSource(); // this.excludePackagesJTable.getModel();

        if (e.getType() != UPDATE) {

            model.sortRows();
        } else {
            // only the apply may be update per say
            if (e.getColumn() == EpTableModel.COL_EXCL_APPLY_BOOL) {
                model.validateOverridden();
            }
        }
    }

}
