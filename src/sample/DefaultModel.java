package sample;

import java.io.*;
import java.util.*;

public class DefaultModel extends Observable {
    private final Vector<Ship> shipList; // List of ships and their positions
    private int counter = 0; // Counter of all tries
    private final int shipPositions; // Sum of all Ship Positions
    private int shipHits = 0; // Sum of all hits on a ship

    public Vector<Ship> getShipList() {
        return shipList;
    }

    public int getShipCount() {
        return shipList.size();
    }

    public int getCounter() {
        return counter;
    }

    public int getShipPositions() {
        return shipPositions;
    }

    public int getShipHits() {
        return shipHits;
    }

    private int randInt(int min, int max) {
        return (int)(Math.random() * (max - min + 1) + min);
    }

    private boolean checkPos(Vector<Ship> list, String position) { // Check parsed position against
                                                                   // parsed list of ships;
                                                                   // return true for any matching positions
        if (list.isEmpty()) {
            return false;
        }
        for (Ship ship: list) { // For each Ship Object
            for (String pos: ship.positions) { // For each Ship positions
                if (position.equals(pos)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static class Position { // Battleship Board position object
        public int x; // X Axis, in integer type for easier modification
        public int y; // Y Axis

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean isValid() {
            return x >= 1 && x <= 10 && y >= 1 && y <= 10;
        }

        public Position nextPos(int direction) {
            switch (direction) {
                case 1:
                    return new Position(x - 1, y);
                case 2:
                    return new Position(x, y + 1);
                case 3:
                    return new Position(x + 1, y);
                case 4:
                    return new Position(x, y - 1);
            }
            throw new IllegalArgumentException("Direction must be between 1 and 4");
        }

        public String toPos() {
            return Character.toString((char)x + 64) + y;
        }
    }

    public DefaultModel(int[] sizeList) { // For automatic ship layout building
        shipList = new Vector<>();
        int sumShipPos = 0; // Sum of Ship Positions

        for (int size: sizeList) {
            sumShipPos += size;

            Vector<String> shipPositions = new Vector<>();
            boolean validShip = false;

            while (!validShip) { //
                Stack<Position> positions = new Stack<>(); // Allow latest ship to be worked on and
                                                           // automatically stored

                validShip = true; // Assumes ship positions are valid until proven otherwise
                int direction = randInt(1, 4); // 1:North, 2:East, 3:South, 4:West
                for (int i = 1; i <= size; i++) {
                    if (positions.empty()) {
                        positions.push(new Position(randInt(1, 10), randInt(1, 10)));
                    } else {
                        positions.push(positions.peek().nextPos(direction));
                        if (!positions.peek().isValid()) { // If the position is not on the board
                            validShip = false; // Invalidate the ship configuration
                            break;
                        }
                    }

                    if (checkPos(shipList, positions.peek().toPos())) { // If the position overlaps with another
                        validShip = false; // Invalidate the ship configuration
                        break;
                    }
                }

                if (positions.size() != size) { // If the ship is not the correct size
                    validShip = false;
                }

                if (validShip) { // Valid ship, now to convert to String Positions
                    for (Position pos: positions) {
                        shipPositions.add(pos.toPos());
                    }
                }
            }

            shipList.add(new Ship(shipPositions));
        }
        shipPositions = sumShipPos;
    }

    private int getSizeSum(Vector<Ship> list) {
        int sumShipPos = 0;
        for (Ship ship: list) {
            sumShipPos += ship.size;
        }
        return sumShipPos;
    }

    public DefaultModel(Vector<Ship> shipList) { // For pre-built ship list
        this.shipList = shipList;

        shipPositions = getSizeSum(shipList);
    }

    public DefaultModel(Ship[] shipArray) {  // For pre-built ship list
        shipList = new Vector<>();
        shipList.addAll(Arrays.asList(shipArray));

        shipPositions = getSizeSum(shipList);
    }

    private Ship ShipBlock(StreamTokenizer streamTokenizer) throws IOException {
        assert streamTokenizer.ttype == '{';
        streamTokenizer.nextToken();
        Vector<String> positions = new Vector<>();
        while (streamTokenizer.ttype != '}') {
            assert streamTokenizer.ttype == StreamTokenizer.TT_WORD; // If token is a word
            positions.add(streamTokenizer.sval);
            streamTokenizer.nextToken();
        }
        streamTokenizer.nextToken();
        return new Ship(positions);
    }

    private Vector<Ship> loadFromFileStream(FileReader reader) throws IOException {
        Vector<Ship> shipList = new Vector<>();
        StreamTokenizer streamTokenizer = new StreamTokenizer(reader);
        streamTokenizer.nextToken();
        while (streamTokenizer.ttype != StreamTokenizer.TT_EOF) {
            shipList.add(ShipBlock(streamTokenizer));
        }
        return shipList;
    }

    public DefaultModel(File file) throws IOException { // For reading ship list from file
        this.shipList = loadFromFileStream(new FileReader(file));
        int sumShipPos = 0;
        for (Ship ship: this.shipList) {
            sumShipPos += ship.size;
        }
        this.shipPositions = sumShipPos;
    }

    public boolean checkPos(String position) { // Checks position against ship positions
        return checkPos(this.shipList, position);
    }

    static int getIntPos(String position) { // Convert String position to Integer Position; 1..100
        return Integer.parseInt(position.substring(1)) + (position.charAt(0) - 65) * 10; // Convert A..J to 0..90
    }

    public boolean checkPosRange(String position) {
        int num = getIntPos(position);
        return num >= 1 && num <= 100;
    }

    private void endGame() {
        updateView(new UpdateMessage(true, false));
        System.out.println("You hit all the ships!");
        System.out.println("Tries: " + counter);
    }

    public void activatePosition(String position){
        counter += 1;
        boolean shipHit = checkPos(position);
        updateView(new UpdateMessage(position, shipHit));
        if (shipHit) {
            shipHits += 1;
        }
        if (shipHits == shipPositions) {
            endGame();
        }
    }

    public void displayShips() {
        for (Ship ship: shipList) {
            for (String pos: ship.positions) {
                activatePosition(pos);
            }
        }
        updateView(new UpdateMessage(true, false));
    }

    private void updateView(UpdateMessage message) { // Update View through Observer
        setChanged();
        notifyObservers(message);
    }
}
