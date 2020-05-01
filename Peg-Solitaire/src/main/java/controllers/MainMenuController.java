package controllers;

import helpers.GameType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.Board;
import java.util.ArrayList;
import java.util.List;
import static helpers.GameType.*;
import agents.rl.RLAgent;

public class MainMenuController {

    @FXML ToggleGroup diamond_or_triangle;
    @FXML Spinner<Integer> sizeSpinner;
    @FXML VBox holesBox;
    @FXML Pane boardPaneOuter;
    @FXML Button run;
    @FXML TextField episodes, actorLR, actorEDR, actorDF, epsilon, criticLR, criticEDR, criticDF;
    @FXML CheckBox includeED, useVA;


    Pane board;
    private RLAgent agent;
    private BoardController boardController;
    private int size = 1;
    private GameType gameType;
    private List<String> checkBoxes = new ArrayList<>();

    @FXML
    public void run() {

        if (!includeED.isSelected() && !useVA.isSelected()) {
            agent = new RLAgent(Integer.valueOf(episodes.getText()), Double.valueOf(actorLR.getText()),
                    Double.valueOf(criticLR.getText()), Double.valueOf(actorEDR.getText()),
                    Double.valueOf(criticEDR.getText()), Double.valueOf(actorDF.getText()),
                    Double.valueOf(criticDF.getText()), Double.valueOf(epsilon.getText()), 0.0,
                    boardController.getBoard().getBoardRepresentation());
        }

        Thread thread = new Thread(() -> {
            Runnable updater = () -> {
                    String action = agent.getAction();
                    String from = action.split(":")[0];
                    String to = action.split(":")[1];
                    boardController.makeMove(from, to);
                    List<Integer> board = boardController.getBoard().getBoardRepresentation();
                    agent.updateState(board);

            };

            while (!boardController.isFinished()) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                }
                // UI update is run on the Application thread
                Platform.runLater(updater);
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

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
            String name = Board.NAMES.get(i);

            CheckBox btn = new CheckBox();
            btn.setText(name);
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
