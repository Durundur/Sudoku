package sudoku;

import java.util.ArrayList;

public class Generator {
    Generator(Board board, String level) {
        setRandomNumbersInSquaresOnDiagonal(board);
        Solver.solve(board);
        usunWartosci(board, level);
    }

    static int randomGenerator(int N) {
        return (int) Math.floor((Math.random() * N + 1));
    }

    static void setRandomNumbersInSquaresOnDiagonal(Board board) {
        for (int i = 0; i < 9; i = i + 4) {
            Square square = board.getSquares().get(i);
            setRandomNumbersOnSquare(square);
        }
    }

    static void setRandomNumbersOnSquare(Square square) {
        for(Cell cell : square.getCells()){
            int value;
            ArrayList<Integer> unusedValuesFromSquare = Square.showUnusedValues(square.getCells());
            do {
                value = randomGenerator(9);
            }
            while (!unusedValuesFromSquare.contains(value));
            cell.setValue(value);
        }
    }

    public static void usunWartosci(Board board, String levels) {
        int numberOfEmpytCells = switch (levels) {
            case "easy" -> 12;
            case "medium" -> 17;
            case "hard" -> 24;
            default -> 12;
        };

        while (numberOfEmpytCells != 0) {
            int randomValue = randomGenerator(81) - 1;

            int x = (randomValue / 9);
            int y = randomValue % 9;

            var row = board.getRows().get(x);
            var cell = row.getCells().get(y);

            if (!cell.getValue().equals(0)) {
                numberOfEmpytCells--;
                cell.setValue(0);
            }
        }
    }
}

