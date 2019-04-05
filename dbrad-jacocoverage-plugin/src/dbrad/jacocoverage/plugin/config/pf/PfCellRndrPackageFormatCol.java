/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocoverage.plugin.config.pf;

import dbrad.jacocofpm.mgr.com.FilePackageOfSrc;
import static dbrad.jacocoverage.plugin.config.pf.PfTableModel.PKGFLTR_PACKAGE;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author dbradley
 */
public class PfCellRndrPackageFormatCol extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    public final static Color LITE_GRAY = new Color(244, 244, 244);

    public final static Color LITE_BLUE = new Color(100, 244, 244);
    
    private final static LineBorder cellFocusOn = new LineBorder(Color.black, 2);
    
    private static Border originalBorder = null;

    public PfCellRndrPackageFormatCol() {
        // 
        originalBorder = super.getBorder();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        PfCellRndrPackageFormatCol c = (PfCellRndrPackageFormatCol)super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        c.setForeground(Color.black);

        if (((String) value).isEmpty()) {
            this.setText("*");
        }

        if (column != PKGFLTR_PACKAGE) {
            c.setBackground(Color.white);
            return c;
        }
        int rowConverted = table.convertRowIndexToModel(row);

        FilePackageOfSrc fileSrc = (FilePackageOfSrc) table.getModel().getValueAt(rowConverted, PfTableModel.PKGFLTR_FILE_HIDDEN);

        c.setBackground(fileSrc.getAffectedBySibling().getColor());
        
        if(hasFocus){
            c.setBorder(cellFocusOn);
        }else{
            c.setBorder(originalBorder);
        }
        
//        if(hasFocus){
//           c.set
//        

//        int count = fileSrc.flashInDisplayCount;
//        if (count == FileSrcCodeDir.FLASHER_NUMBER) {
//             System.out.printf("flash start: %s\n", fileSrc.flashInDisplayCount);
//            new Flasher(fileSrc, table, row);
//        }
//        
//        if(count > 0){
//             System.out.printf("render: %s\n", fileSrc.flashInDisplayCount);
//        }
      
        return c;
    }
}

//class Flasher implements ActionListener {
//
//    private FilePackageOfSrc fileSrc;
//    private JTable table;
//    private int row;
//    
//    private Object initialValue;
//    
//    private Timer tmr;
//
//    Flasher(FilePackageOfSrc fileSrc, JTable table, int row) {
//        this.fileSrc = fileSrc;
//        this.table = table;
//        this.row = row;
//        
//        int rowConverted = table.convertRowIndexToModel(this.row);
//        this.initialValue = table.getModel().getValueAt(rowConverted, PfTableModel.PKGFLTR_PACKAGE);
//
//        this.fileSrc.flashInDisplayCount--;
//
//        // prepare a timer for displaying the items
//        this.tmr = new javax.swing.Timer(1000, this);
//
//        this.tmr.addActionListener(this);
//        this.tmr.setInitialDelay(0);
//        this.tmr.setRepeats(true);
//        this.tmr.start();
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent evt) {
//        
//        int rowConverted = table.convertRowIndexToModel(this.row);
//        
////        table.getModel().setValueAt(rowConverted, PfTableModel.PKGFLTR_PACKAGE)
////
//        System.out.printf("flash: %s %s\n", this.fileSrc.flashInDisplayCount, this.fileSrc.getAbsolutePath());
////        
//        int count = this.fileSrc.flashInDisplayCount;
//          //
//        if ((count % 2) == 1) {
//            table.getModel().setValueAt(initialValue, rowConverted, PfTableModel.PKGFLTR_PACKAGE);
//        }else{
//           table.getModel().setValueAt("", rowConverted, PfTableModel.PKGFLTR_PACKAGE); 
//        }
//
//        if (this.fileSrc.flashInDisplayCount < 1) {
//            table.getModel().setValueAt(initialValue, rowConverted, PfTableModel.PKGFLTR_PACKAGE);
//            this.tmr.stop();
//        } else {
//            this.fileSrc.flashInDisplayCount--;
//        }
//        table.repaint();
//    }
//}
