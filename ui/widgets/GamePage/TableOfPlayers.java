package ui.widgets.GamePage;

import components.Table;
import java.awt.Color;
import java.awt.Component;
import javax.swing.table.TableCellRenderer;

public class TableOfPlayers extends Table{
    private int currentPlayerIndex = 1;

    public TableOfPlayers(Object[][]info){
        super(new String[]{"Name","Cards Left"}, info);
    }
    public void updateCurrentIndex(int index){
        currentPlayerIndex = index;
    }
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component comp = super.prepareRenderer(renderer, row, column);

        if (row == currentPlayerIndex) {
            comp.setBackground(Color.WHITE);
            comp.setForeground(Color.RED);           
        } else {
            comp.setBackground(Color.WHITE);
            comp.setForeground(Color.BLACK);
        }

        return comp;
    }
    }
    

