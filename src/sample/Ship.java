package sample;

import java.util.Arrays;
import java.util.Vector;

public class Ship {
    final public int size;
    final public Vector<String> positions;

    public Ship(Vector<String> positions) {
        this.positions = positions;
        this.size = positions.size();
    }
    
    public Ship(String[] positions) {
        this.positions = new Vector<>();
        this.positions.addAll(Arrays.asList(positions));
        this.size = this.positions.size();
    }
}
