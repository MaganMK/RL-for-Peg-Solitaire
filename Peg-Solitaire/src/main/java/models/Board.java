package models;

import helpers.GameType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Board {

    public static final List<String> NAMES = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"));
    private int posOffset = 100;
    private int posInterval = 50;
    private HashMap<String, Cell> cells;
    private GameType gameType;
    private int size;

    public Board(GameType gameType, int size) {
        this.gameType = gameType;
        this.size = size;
        initBoard();
        System.out.println(cells.get("100 100").getNeighbours());
    }

    private void initBoard() {
        HashMap<String, Cell> result = new HashMap<>();
        for (int i = 0; i<size; i++) {
            int rowSize = gameType == GameType.DIAMOND ? size : i+1;
            for (int j=0; j<rowSize; j++) {
                int x = posInterval*j+posOffset;
                int y = posInterval*i+posOffset;
                Cell cell = new Cell(true, x, y, NAMES.get(i+j));
                result.put(x + " " + y, cell);
            }
        }
        this.cells = result;
        createLinks();
    }

    private void createLinks() {
        for (String posString : cells.keySet()) {
            String[] pos = posString.split(" ");
            int x = Integer.valueOf(pos[0]);
            int y = Integer.valueOf(pos[1]);
            link(posString, x + posInterval, y);
            link(posString, x - posInterval, y);
            link(posString, x, y + posInterval);
            link(posString, x, y - posInterval);
            if (gameType == GameType.DIAMOND){
                link(posString, x - posInterval, y + posInterval);
                link(posString, x + posInterval, y - posInterval);
            } else {
                link(posString, x + posInterval, y + posInterval);
                link(posString, x - posInterval, y - posInterval);
            }
        }
    }

    private void link(String originalPos, int x, int y) {
        String neighbourPos = x + " " + y;
        if (cells.get(neighbourPos) != null){
            cells.get(originalPos).addNeighbour(cells.get(neighbourPos));
        }
    }

    public HashMap<String, Cell> getCells() { return cells; }

}
