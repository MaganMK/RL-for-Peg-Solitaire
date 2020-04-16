package models;

import helpers.GameType;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private List<Cell> cells;
    private GameType gameType;
    private int size;

    public Board(GameType gameType, int size) {
        this.gameType = gameType;
        this.size = size;
        cells = gameType == GameType.DIAMOND ? initDiamondBoard(size) : initTriangleBoard(size);
    }

    private ArrayList<Cell> initTriangleBoard(int size) {
        ArrayList<Cell> result = new ArrayList<Cell>();
        for (int i = 0; i<size; i++) {
            for (int j=0; j<i; j++) {
                Cell cell = new Cell(true, j, i);
                result.add(cell);
            }
        }
        return result;
    }
    private ArrayList<Cell> initDiamondBoard(int size) {
        return null;
    }

    public List<Cell> getCells() { return cells; }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

}
