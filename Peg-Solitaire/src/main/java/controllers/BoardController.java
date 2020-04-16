package controllers;

import helpers.GameType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import models.Board;

public class BoardController {

    private Board board;

    @FXML
    AnchorPane anchor_board;
    @FXML
    Group group;
    @FXML
    AnchorPane index;

    public BoardController() { }

    public BoardController(GameType gameType, int size) {
        board = new Board(gameType, size);
        //Group groupp = new Group();
        //groupp.getChildren().add(new Circle(0, 0, 10));
       // anchor.getChildren().add(groupp);

    }


}
