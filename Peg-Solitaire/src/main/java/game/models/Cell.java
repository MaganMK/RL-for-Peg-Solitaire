package game.models;

import game.helpers.Tuple;
import java.util.ArrayList;
import java.util.List;

public class Cell {

    private Tuple<Integer, Integer> position;
    private boolean hasPeg;
    private List<Cell> neighbours;
    private String name;

    public Cell(boolean hasPeg, int x, int y, String name) {
        this.hasPeg = hasPeg;
        neighbours = new ArrayList<>();
        this.position = new Tuple<>(x, y);
        this.name = name;
    }

    public boolean hasPeg() { return hasPeg; }
    public List<Cell> getNeighbours() { return neighbours; }
    public int getX() { return position.x; }
    public int getY() { return position.y; }
    public String getName() { return name; }


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

    @Override
    public String toString(){
        return "Position: " + position.toString();
    }

    public void setPeg(boolean value) {
        hasPeg = value;
    }
}
