package sample;

import javafx.application.Platform;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleController {
    private final DefaultModel model;

    private final Pattern intPattern = Pattern.compile("[A-J](10|[1-9])");

    public ConsoleController(DefaultModel model) {
        this.model = model;
    }

    public void commandProcess() {
        try (Scanner in = new Scanner(System.in)) { // Initialise Console Scanner
            while (true) {
                String input = in.next().trim();
                if (intPattern.matcher(input).matches()) {
                    if (model.checkPosRange(input)) {
                        activatePosition(input);
                    } else {
                        System.err.println("Position must be between A1 and J10");
                    }
                } else if (input.equals("show")) {
                    displayShips();
                } else {
                    System.err.println("Unknown Command");
                }
            }
        }
    }

    private void activatePosition(String pos) {
        Platform.runLater(() -> model.activatePosition(pos));
    }

    private void displayShips() {
        Platform.runLater(model::displayShips);
    }
}
