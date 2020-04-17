package controllers;

import helpers.GameType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.Board;
import org.w3c.dom.events.Event;

import static helpers.GameType.*;
import static jdk.nashorn.internal.codegen.OptimisticTypesPersistence.load;

public class MainMenuController {

    @FXML
    ToggleGroup diamond_or_triangle;
    @FXML
    Spinner<Integer> sizeSpinner;
    @FXML
    AnchorPane anchor;
    @FXML
    VBox holesBox;
    @FXML
    Pane boardPaneOuter;

    Pane board;

    private BoardController boardController;

    private int size = 0;

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
        for (int i = 0; i<newValue; i++){
            CheckBox btn = new CheckBox();
            btn.setText(Board.NAMES.get(i));
            holesBox.getChildren().add(btn);
        }
        handleStart();
    }

    public void handleStart() {
        RadioButton selectedToggle = (RadioButton) diamond_or_triangle.getSelectedToggle();
        try{
            String selectedToggleText = selectedToggle.getText();
            GameType gameType = selectedToggleText.equals("diamond")? DIAMOND : TRIANGLE;
            int size = sizeSpinner.getValue();

            FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("/views/board.fxml"));
            board = boardLoader.load();
            this.boardController = boardLoader.getController();
            boardController.generateBoard(gameType, size);

            boardPaneOuter.getChildren().add(board);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
