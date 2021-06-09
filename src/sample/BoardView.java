package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.Observable;
import java.util.Observer;

public class BoardView implements Observer {
    private final GUIController controller;

    @FXML
    GridPane root;

    @Override
    public void update(Observable o, Object arg) {
        assert arg instanceof UpdateMessage;
        UpdateMessage info = (UpdateMessage)arg;
        if (info.disableBoard) {
            root.setDisable(true);
        } else {
            assert info.position != null;
            positionUpdate(info, (Button)root.lookup("#" + info.position));
        }
    }

    protected void positionUpdate(UpdateMessage info, Button button) {
        if (info.ship) { // If the position is a ship
            button.setStyle("-fx-background-color: red");
            System.out.println("You hit a ship!");
        } else {
            System.out.println("Missed");
        }
        button.setText("X");
    }

    public BoardView(GUIController controller) {
        this.controller = controller;
    }

    public void OnClicked(ActionEvent actionEvent) {
        Button buttonClicked = (Button)actionEvent.getSource();
        if (controller.activatePosition(buttonClicked.getId())) {
            buttonClicked.setDisable(true);
        }
    }
}
