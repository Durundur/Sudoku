package sudoku;

import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    static boolean solve(Board board) {
        for(Row row: board.getRows()){
            for(Cell cell : row.getCells()){
                if (cell.getValue().equals(0)) {
                    int squareNumber = Board.getNumberOfSquare(cell.getRow(), cell.getColumn());
                    ArrayList<Integer> unusedValuesFromSquare = Square.showUnusedValues(board.getSquares().get(squareNumber).getCells());
                    unusedValuesFromSquare.retainAll(Line.showUnusedValues(row.getCells()));
                    unusedValuesFromSquare.retainAll(Line.showUnusedValues(board.getColumns().get(cell.getColumn()).getCells()));
                    Collections.shuffle(unusedValuesFromSquare);
                    for (int value : unusedValuesFromSquare) {
                        cell.setValue(value);
                        if (solve(board)) {
                            return true;
                        }
                        cell.setValue(0);
                    }
                    return false;
                }
            }
        }
        return true;
    }
}
