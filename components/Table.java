package components;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Table extends JTable {
    public Table(String[] labels,Object[][] info){
        super(new DefaultTableModel(info,labels));
    }
}
