package sudoku;

import java.util.ArrayList;
import java.util.List;


public class Board {
    private final List<Row> rows = new ArrayList<>(9);
    private final List<Column> columns = new ArrayList<>(9);
    private final List<Square> squares = new ArrayList<>(9);

    Board() {
        for (int i = 0; i < 9; i++) {
            var row = new Row();
            var column = new Column();
            var square = new Square();
            rows.add(row);
            columns.add(column);
            squares.add(square);
        }

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                var cell = new Cell();
                cell.setColumn(col);
                cell.setRow(row);
                cell.setValue(0);
                rows.get(row).getCells().add(cell);
                columns.get(col).getCells().add(cell);
                int squareNumber = getNumberOfSquare(row, col);
                squares.get(squareNumber).getCells().add(cell);
            }
        }
    }

    public static int getNumberOfSquare(int row, int column) {
        int x = row / 3;
        int y = column / 3;
        if (x == 0 && y == 0) return 0;
        if (x == 0 && y == 1) return 1;
        if (x == 0 && y == 2) return 2;
        if (x == 1 && y == 0) return 3;
        if (x == 1 && y == 1) return 4;
        if (x == 1 && y == 2) return 5;
        if (x == 2 && y == 0) return 6;
        if (x == 2 && y == 1) return 7;
        if (x == 2 && y == 2) return 8;
        else return 0;
    }

    public List<Row> getRows() {
        return rows;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Square> getSquares() {
        return squares;
    }

    public void resetBoard() {
        for(Row row : rows){
            for(Cell cell: row.getCells()){
                cell.setValue(0);
            }
        }
    }

    public void printBoard() {
        for(Row row : rows){
            for(Cell cell: row.getCells()){
                int value = cell.getValue();
                if (value == 0) {
                    System.out.print(" ");
                } else {
                    System.out.print(value);
                }
                System.out.print(" | ");
            }
            System.out.println();
        }
        System.out.println("-----------------------------------");
    }

    public int getNumberOfEmptyCells() {
        int counter = 0;
        for(Row row : rows) {
            for (Cell cell : row.getCells()) {
                if(cell.getValue()==0) counter++;
            }
        }
        return counter;
    }
}
