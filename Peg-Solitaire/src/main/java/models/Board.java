package models;

import helpers.GameType;

import java.util.*;
import java.util.stream.Collectors;

public class Board {

    public static final List<String> NAMES = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T"));
    private int posOffset = 100;
    private int posInterval = 50;
    private HashMap<String, Cell> cells;
    private GameType gameType;
    private int size;

    public Board(GameType gameType, int size) {
        this.gameType = gameType;
        this.size = size;
        initBoard();
    }

    private void initBoard() {
        HashMap<String, Cell> result = new HashMap<>();
        int nameCounter = 0;
        for (int i = 0; i<size; i++) {
            int rowSize = gameType == GameType.DIAMOND ? size : i+1;
            for (int j=0; j<rowSize; j++) {
                int x = posInterval*j+posOffset;
                int y = posInterval*i+posOffset;
                Cell cell = new Cell(true, x, y, NAMES.get(nameCounter));
                result.put(x + " " + y, cell);
                nameCounter += 1;
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

    public void updateHoles(List<String> holes){
        for (String hole : holes) {
            getCellFromName(hole).setPeg(false);
        }
    }

    private void link(String originalPos, int x, int y) {
        String neighbourPos = x + " " + y;
        if (cells.get(neighbourPos) != null){
            cells.get(originalPos).addNeighbour(cells.get(neighbourPos));
        }
    }

    private Cell getCellFromName(String name) {
        Cell res = null;
        for (Cell cell : cells.values()){
            if(name.equals(cell.getName())) {
                res = cell;
            }
        }
        return res;
    }

    private Set<Cell> intersectingNeighbours(Cell cellOne, Cell cellTwo) {
        Set<Cell> fromNeighbours = new HashSet<>(cellOne.getNeighbours());
        Set<Cell> toNeighbours = new HashSet<>(cellTwo.getNeighbours());
        fromNeighbours.retainAll(toNeighbours);
        return fromNeighbours;
    }

    private boolean isNeighbours(Cell cellOne, Cell cellTwo) {
        return cellOne.getNeighbours().contains(cellTwo);
    }

    public HashMap<String, Cell> getCells() { return cells; }

    public boolean isFinished() {
        for (Cell from : cells.values()) {
            for (Cell to : cells.values()) {
                if (isLegalMove(from, to)){
                    return false;
                }
            }
        }
        return cells.values().stream().filter(c -> !c.hasPeg()).count() >= 1;
    }

    public boolean isWon() {
        return isFinished() && cells.values().stream().filter(Cell::hasPeg).count() == 1;
    }

    public boolean makeMove(String from, String to) {
        Cell fromCell = getCellFromName(from);
        Cell toCell = getCellFromName(to);

        if (! isLegalMove(fromCell, toCell)){
            return false;
        }
        else {
            fromCell.setPeg(false);
            toCell.setPeg(true);
            Cell intersection = intersectingNeighbours(fromCell, toCell).iterator().next();
            intersection.setPeg(false);
            return true;
        }
    }

    private boolean isLegalMove(Cell from, Cell to) {
        Set<Cell> intersection = intersectingNeighbours(from, to);
        if (intersection.size() == 1 && !isNeighbours(from, to) && from.hasPeg() && !to.hasPeg())
        {
            return intersection.iterator().next().hasPeg();
        }
        return false;
    }

}
