package sudoku;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class EmptyCell extends JTextField {
    private final int row;
    private final int column;

    public EmptyCell(int row, int column) {
        Font font = new Font("Serif", Font.PLAIN, 26);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        this.row = row;
        this.column = column;
        this.setColumns(1);
        this.setFont(font);
        this.setMargin(new Insets(5, 31, 5, 31));
        this.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 20, 10, 14)));
    }

    public int getRow() {
        return row;
    }


    public int getColumn() {
        return column;
    }

}
