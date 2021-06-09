package sample;

public class UpdateMessage {
    public final String position;
    public final boolean ship;
    public final boolean disableBoard;
    public final boolean restartBoard;

    public UpdateMessage(String position, boolean ship) { // Position Update Constructor
        this.position = position;
        this.ship = ship;
        this.disableBoard = false;
        this.restartBoard = false;
    }
    public UpdateMessage(boolean disableBoard, boolean restartBoard) { // Board Update Constructor
        this.position = null;
        this.ship = false;
        this.disableBoard = disableBoard;
        this.restartBoard = restartBoard;
    }
}
