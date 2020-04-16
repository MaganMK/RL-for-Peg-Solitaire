package models;

import helpers.Tuple;
import java.util.ArrayList;
import java.util.List;

public class Cell {

    private Tuple<Integer, Integer> position;
    private boolean hasPeg;
    private List<Cell> neighbours;

    public Cell(boolean hasPeg, int x, int y) {
        this.hasPeg = hasPeg;
        neighbours = new ArrayList<>();
        this.position = new Tuple<>(x, y);
    }

    public boolean hasPeg() { return hasPeg; }
    public List<Cell> getNeighbours() { return neighbours; }
    public int getX() { return position.x; }
    public int getY() { return position.y; }


    public void addNeighbour (Cell neighbour) {
        if (! neighbours.contains(neighbour)) {
            neighbours.add(neighbour);
            neighbour.addNeighbour(this);
        }
    }

    public void removeNeighbour (Cell neighbour) {
        if (neighbours.contains(neighbour)) {
            neighbours.remove(neighbour);
            neighbour.removeNeighbour(this);
        }
    }
}
