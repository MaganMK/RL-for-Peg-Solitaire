package controllers;

import helpers.GameType;
import javafx.fxml.FXML;
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

public class BoardController {

    private final static int RADIUS = 10;
    private Board board;

    @FXML
    Pane paneBoard;

    public void generateBoard(GameType gameType, int size) {
        board = new Board(gameType, size);
        HashMap<String, Cell> cells = board.getCells();
        for (String posString : cells.keySet()){
            String[] pos = posString.split(" ");
            int x = Integer.valueOf(pos[0]);
            int y = Integer.valueOf(pos[1]);

            Circle node = createCircle();
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
    }

    private Circle createCircle(){
        Circle circle = new Circle(RADIUS);
        circle.setStroke(Color.FORESTGREEN);
        circle.setStrokeWidth(10);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setFill(Color.AZURE);
        circle.relocate(0, 0);

        return circle;
    }
}
