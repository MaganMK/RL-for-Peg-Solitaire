package game.models;

import game.controllers.BoardController;
import game.helpers.GameType;
import game.interfaces.BoardInterface;

import java.util.*;

public class Board {

    public static final List<String> NAMES = new ArrayList<>(Arrays.asList("A", "B", "C", "D",
            "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
            "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));
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

    public List<Integer> getBoardRepresentation() {
        List<Integer> representation = new ArrayList<>();
        for (int i = 0; i<cells.keySet().size(); i++) {
            if (getCellFromName(NAMES.get(i)).hasPeg()) {
                representation.add(1);
            } else {
                representation.add(0);
            }
        }
        return representation;
    }

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

    public int makeMove(String from, String to) {
        Cell fromCell = getCellFromName(from);
        Cell toCell = getCellFromName(to);

        if (!isLegalMove(fromCell, toCell)){
            return 0;
        }
        else {
            fromCell.setPeg(false);
            toCell.setPeg(true);
            Cell intersection = intersectingNeighbours(fromCell, toCell).iterator().next();
            intersection.setPeg(false);
            if(isFinished() && isWon()) {
                return BoardController.WINREWARD;
            } else if (isFinished()) {
                return BoardController.LOOSEREWARD;
            }
            return BoardController.STEPREWARD;
        }
    }

    public int getPegsLeft() {
        return (int) cells.values().stream().filter(Cell::hasPeg).count();
    }

    private boolean isLegalMove(Cell from, Cell to) {
        Set<Cell> intersection = intersectingNeighbours(from, to);
        if (intersection.size() == 1 && !isNeighbours(from, to) && from.hasPeg() && !to.hasPeg())
        {
            return intersection.iterator().next().hasPeg();
        }
        return false;
    }

    private List<String> allMoves() {
        List<String> moves = new ArrayList<>();
        int searchSpaceSize = cells.keySet().size();
        for (int i = 0; i<searchSpaceSize; i++) {
            for (int j = 0; j<searchSpaceSize; j++) {
                String from = NAMES.get(i);
                String to = NAMES.get(j);
                moves.add(from + " " + to);
            }
        }
        return moves;
    }

    public List<String> getLegalMoves() {
        List<String> allMoves = allMoves();
        List<String> legalMoves = new ArrayList<>();
        for (String move : allMoves) {
            Cell from = getCellFromName(move.split(" ")[0]);
            Cell to = getCellFromName(move.split(" ")[1]);
            if(isLegalMove(from, to)) {
                legalMoves.add(move);
            }
        }

        return legalMoves;
    }

}
