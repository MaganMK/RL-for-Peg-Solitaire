package controllers;

import agents.rl.RLAgent;
import helpers.GameType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import models.Board;
import models.Cell;

import java.util.HashMap;
import java.util.List;

public class BoardController {

    private final static int RADIUS = 10;
    private Board board;
    private GameType gameType;
    private int size;
    @FXML
    Pane paneBoard;

    public void generateBoard(GameType gameType, int size, List<String> emptyCells) {
        this.gameType = gameType;
        this.size = size;
        board = new Board(gameType, size);
        board.updateHoles(emptyCells);
        drawBoard();
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

    public void makeMove(String from, String to) {

        if (board.makeMove(from, to)){
            System.out.println(from+to);
            drawBoard();
        } else {
            System.out.println("Illegal move");
        }
    }

    public boolean isFinished() {
        return board.isFinished();
    }

}
