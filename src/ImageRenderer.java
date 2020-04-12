import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

// class to render images in a JTable
class ImageRenderer extends DefaultTableCellRenderer {
 
   // method to render JTable cells
    @Override
    public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected,boolean hasFocus, int row, int column)   {
        JLabel label = new JLabel(); 
        if (value!=null) {
           label.setHorizontalAlignment(JLabel.CENTER);
           label.setIcon(new ImageIcon((byte[])value));
        } // close if
        return label;
    } // close  getTableCellRendererComponent
    
} // close ImageRenderer
