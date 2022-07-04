package sudoku;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GUI {
    private final JFrame frame;
    private final JPanel sudokuBoard;
    private final Board board;
    private int emptyCells;

    GUI() {
        frame = new JFrame("Sudoku");
        frame.setSize(540, 600);
        String[] levels = {"easy", "medium", "hard"};
        JComboBox<String> levelsList = new JComboBox<>(levels);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton generateButton = new JButton("New Game");
        JButton checkButton = new JButton("Check");
        buttonsPanel.add(levelsList);
        buttonsPanel.add(generateButton);
        buttonsPanel.add(checkButton);
        JButton loadGameButton = new JButton("Load Game");
        buttonsPanel.add(loadGameButton);
        JButton saveGameButton = new JButton("Save Game");
        buttonsPanel.add(saveGameButton);
        frame.add(buttonsPanel);
        GridLayout grid = new GridLayout(9, 9);
        sudokuBoard = new JPanel(grid);
        frame.add(sudokuBoard);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        board = new Board();


        generateButton.addActionListener(e -> {
            String level = levels[levelsList.getSelectedIndex()];
            board.resetBoard();
            new Generator(board, level);
            if (sudokuBoard.getComponentCount() == 0) {
                createGameBoard();
            } else {
                sudokuBoard.removeAll();
                createGameBoard();
            }
            emptyCells = board.getNumberOfEmptyCells();
            SwingUtilities.updateComponentTreeUI(frame);
            Solver.solve(board);
            board.printBoard();

        });
        saveGameButton.addActionListener(e -> {
            try {
                saveToJson();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        loadGameButton.addActionListener(e -> {
            try {
                loadGameFromJson();
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        });


        checkButton.addActionListener(e -> {
            int correctCells = 0;
            for (Component component : sudokuBoard.getComponents()) {
                var cell = (EmptyCell) component;
                if (cell.isEditable()) {
                    int row = cell.getRow();
                    int col = cell.getColumn();
                    int correctValue = board.getRows().get(row).getCells().get(col).getValue();
                    if (cell.getText().equals(String.valueOf(correctValue))) {
                        correctCells++;
                    }
                    if (!cell.getText().equals(String.valueOf(correctValue)) && !cell.getText().equals("")) {
                        cell.setForeground(Color.red);
                    }
                }
            }
            if (correctCells == emptyCells) {
                JOptionPane.showMessageDialog(frame, "You Won!");
                generateButton.doClick();
            } else {
                JOptionPane.showMessageDialog(frame, "Not every cell is correct!");
            }
        });
    }

    public int parseCellStringToInt(String toParse) {
        try {
            return Integer.parseInt(toParse);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void saveToJson() {
        if (!(sudokuBoard.getComponentCount() == 0)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String time = formatter.format(date);
            time = time.replace("/", "-");
            time = time.replace(":", "-");
            time = time.replace(" ", "-");
            String name = "Gra-" + time + ".json";
            JSONArray list = new JSONArray();
            for (Component component : sudokuBoard.getComponents()) {
                JSONObject cell = new JSONObject();
                var cellInGui = (EmptyCell) component;
                cell.put("value", cellInGui.getText());
                cell.put("isEditable", cellInGui.isEditable());
                list.add(cell);
            }
            try (FileWriter file = new FileWriter(name)) {
                file.write(list.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadGameFromJson() throws IOException, ParseException {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int result = chooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            if (!(sudokuBoard.getComponentCount() == 0)) sudokuBoard.removeAll();
            File selectedFile = chooser.getSelectedFile();
            JSONParser jsonParser = new JSONParser();
            try (FileReader reader = new FileReader(selectedFile)) {
                Object obj = jsonParser.parse(reader);
                JSONArray list = (JSONArray) obj;
                int index = 0;
                for (Object el : list) {
                    JSONObject cell = (JSONObject) el;
                    int row = index / 9;
                    int col = index % 9;
                    var cellInGui = new EmptyCell(row, col);
                    if ((Boolean) cell.get("isEditable")) {
                        board.getRows().get(row).getCells().get(col).setValue(0);
                        cellInGui.setEditable(true);
                        cellInGui.setText((String) cell.get("value"));
                        cellInGui.setBackground(Color.cyan);
                        cellInGui.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyTyped(KeyEvent e) {
                                cellInGui.setForeground(Color.black);
                                if (cellInGui.getText().length() >= 1) e.consume();
                            }
                        });
                    } else {
                        board.getRows().get(row).getCells().get(col).setValue(parseCellStringToInt((String) cell.get("value")));
                        cellInGui.setText((String) cell.get("value"));
                        cellInGui.setEditable(false);
                    }
                    sudokuBoard.add(cellInGui);
                    index++;
                }
            }
            emptyCells = board.getNumberOfEmptyCells();
            SwingUtilities.updateComponentTreeUI(frame);
            Solver.solve(board);
            board.printBoard();
        }
    }

    public void createGameBoard(){
        for(Row row:board.getRows()){
            for(Cell cell:row.getCells()){
                int value = cell.getValue();
                var cellInGui = new EmptyCell(cell.getRow(), cell.getColumn());
                if (value==0) {
                    cellInGui.setText("");
                    cellInGui.setBackground(Color.cyan);
                    cellInGui.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            cellInGui.setForeground(Color.black);
                            if (cellInGui.getText().length() >= 1) e.consume();
                        }
                    });
                } else {
                    cellInGui.setEditable(false);
                    cellInGui.setText(String.valueOf(value));
                }
                sudokuBoard.add(cellInGui);
            }
        }
    }


}
