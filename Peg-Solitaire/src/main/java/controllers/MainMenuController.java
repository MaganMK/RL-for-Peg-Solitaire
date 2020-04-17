package controllers;

import helpers.GameType;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.Board;
import org.w3c.dom.events.Event;


import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static helpers.GameType.*;
import static jdk.nashorn.internal.codegen.OptimisticTypesPersistence.load;

public class MainMenuController {

    @FXML
    ToggleGroup diamond_or_triangle;
    @FXML
    Spinner<Integer> sizeSpinner;
    @FXML
    VBox holesBox;
    @FXML
    Pane boardPaneOuter;

    Pane board;

    private BoardController boardController;
    private int size = 1;
    private GameType gameType;
    private List<String> checkBoxes = new ArrayList<>();


    @FXML
    public void initialize() {
        initSpinner();
    }

    private void initSpinner() {
        sizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
    }

    @FXML
    public void spinnerChange() {
        int newValue = sizeSpinner.getValue();
        holesBox.getChildren().clear();
        this.size = newValue;
        int totalCells = gameType == GameType.DIAMOND ? size*size : (size*size + size) / 2;
        for (int i = 0; i<totalCells; i++){
            CheckBox btn = new CheckBox();
            btn.setText(Board.NAMES.get(i));
            btn.setOnAction((ActionEvent e) -> checkboxClick());
            holesBox.getChildren().add(btn);
        }

        checkboxClick();
    }

    @FXML
    public void checkboxClick() {
        List<String> checkBoxes = new ArrayList<>();
        for (Node box : holesBox.getChildren()){
            if (((CheckBox) box).isSelected()){
                checkBoxes.add(((CheckBox) box).getText());
            }
        }
        this.checkBoxes = checkBoxes;
        renderBoard();
    }

    public void handleGameTypeToggle() {
        RadioButton selectedToggle = (RadioButton) diamond_or_triangle.getSelectedToggle();
        String selectedToggleText = selectedToggle.getText();
        this.gameType = selectedToggleText.equals("diamond")? DIAMOND : TRIANGLE;
        spinnerChange();
    }

    public void renderBoard() {
        try{
            FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("/views/board.fxml"));
            board = boardLoader.load();
            this.boardController = boardLoader.getController();
            boardController.generateBoard(gameType, size, checkBoxes);
            boardPaneOuter.getChildren().add(board);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
