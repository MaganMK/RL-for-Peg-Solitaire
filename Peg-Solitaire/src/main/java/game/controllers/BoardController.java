package game.controllers;

import game.helpers.GameType;
import game.interfaces.BoardInterface;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import game.models.Board;
import game.models.Cell;

import java.util.HashMap;
import java.util.List;

public class BoardController implements BoardInterface {

    private final static int RADIUS = 10;
    private Board board;
    private GameType gameType;
    private List<String> intialEmptyCells;
    private int size;
    private boolean showBoard = true;
    @FXML
    Pane paneBoard;

    @Override
    public void generateBoard(GameType gameType, int size, List<String> emptyCells) {
        this.gameType = gameType;
        this.size = size;
        this.intialEmptyCells = emptyCells;
        board = new Board(gameType, size);
        board.updateHoles(emptyCells);

        drawBoard();
    }

    @Override
    public void reGenerateBoard() {
        board = new Board(gameType, size);
        board.updateHoles(intialEmptyCells);

        if(showBoard) {
            drawBoard();
        }
    }

    public Board getBoard() {
        return board;
    }

    private Circle createNode(){
        Circle circle = new Circle(RADIUS);
        circle.setStroke(Color.FORESTGREEN);
        circle.setStrokeWidth(10);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setFill(Color.GREEN);
        circle.relocate(0, 0);
        return circle;
    }

    private Circle createEmptyNode() {
        Circle circle = new Circle(RADIUS);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(10);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setFill(Color.GREY);
        circle.relocate(0, 0);
        return circle;
    }

    public void drawBoard() {
        HashMap<String, Cell> cells = board.getCells();
        paneBoard.getChildren().clear();

        for (String posString : cells.keySet()){
            String[] pos = posString.split(" ");
            int x = Integer.valueOf(pos[0]);
            int y = Integer.valueOf(pos[1]);

            Circle node = cells.get(posString).hasPeg() ? createNode() : createEmptyNode();
            Text text = new Text(cells.get(posString).getName());
            text.setBoundsType(TextBoundsType.VISUAL);
            StackPane stack = new StackPane();
            stack.setLayoutX(x-RADIUS);
            stack.setLayoutY(y-RADIUS);
            stack.getChildren().addAll(node, text);
            paneBoard.getChildren().add(stack);
            for (Cell neighbour : cells.get(posString).getNeighbours()) {
                int xTo = neighbour.getX();
                int yTo = neighbour.getY();
                paneBoard.getChildren().add(new Line(x, y, xTo, yTo));
            }
        }

        if(board.isFinished()) {
            StackPane stack = new StackPane();
            Text endText = new Text();
            stack.getChildren().add(endText);
            if (board.isWon()) {
                endText.setText("Finished! Congratulation, you won.");
            } else {
                endText.setText("Finished! Sorry, but you lost.");
            }
            paneBoard.getChildren().add(stack);
        }
    }

    public int makeMove(String from, String to) {
        int reward = board.makeMove(from, to);
        if(showBoard) {
            drawBoard();
        }
        return reward;
    }

    @Override
    public List<String> getLegalMoves() {
        return board.getLegalMoves();
    }

    public boolean isFinished() {
        return board.isFinished();
    }

    @Override
    public int pegsLeft() {
        return board.getPegsLeft();
    }

    @Override
    public List<Integer> getState() {
        return this.board.getBoardRepresentation();
    }

    @Override
    public void setShowBoard(boolean show) {
        this.showBoard = show;
    }

}
