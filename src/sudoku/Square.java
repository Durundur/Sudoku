package sudoku;

import java.util.ArrayList;
import java.util.List;

public class Square {

    private final List<Cell> cells = new ArrayList<>(9);

    public static ArrayList<Integer> showUnusedValues(List<Cell> cells) {
        ArrayList<Integer> values = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        for (Cell cell : cells) {
            values.remove(cell.getValue());
        }
        return values;
    }

    public List<Cell> getCells() {
        return cells;
    }

}
