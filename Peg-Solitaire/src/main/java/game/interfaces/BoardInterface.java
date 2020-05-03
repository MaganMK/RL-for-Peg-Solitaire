package game.interfaces;

import game.helpers.GameType;

import java.util.List;

public interface BoardInterface {

    void generateBoard(GameType gameType, int size, List<String> emptyCells);
    void reGenerateBoard();
    int makeMove(String from, String to);
    List<String> getLegalMoves();
    boolean isFinished();
    int pegsLeft();
    List<Integer> getState();
    void setShowBoard(boolean show);
}
