package controllers;

import helpers.GameType;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

import static helpers.GameType.*;

public class MainMenuController {

    @FXML
    ToggleGroup diamond_or_triangle;
    @FXML
    Spinner<Integer> sizeSpinner;
    @FXML
    AnchorPane anchor;

    private int size = 0;

    @FXML
    public void initialize() {
        initSpinner();
    }

    private void initSpinner() {
        sizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
    }

    public void handleStart() {
        RadioButton selectedToggle = (RadioButton) diamond_or_triangle.getSelectedToggle();
        String selectedToggleText = selectedToggle.getText();
        GameType gameType = selectedToggleText.equals("diamond")? DIAMOND : TRIANGLE;
        int size = sizeSpinner.getValue();

        new BoardController(gameType, size);
        anchor.setVisible(false);

    }
}
