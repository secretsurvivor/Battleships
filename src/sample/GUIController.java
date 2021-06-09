package sample;

public class GUIController {
    private final DefaultModel model;

    public GUIController(DefaultModel model) {
        this.model = model;
    }

    public boolean activatePosition(String position) {
        if (model.checkPosRange(position)) {
            model.activatePosition(position);
            return true;
        }
        return false;
    }
}
