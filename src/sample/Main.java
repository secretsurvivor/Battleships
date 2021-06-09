package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.Map;
import java.util.regex.Pattern;

public class Main extends Application {
    private final Pattern loadCommandAndPath = Pattern.compile("(-load)\\s(.:)(\\\\(.)*)*\\s");
    private final Pattern path = Pattern.compile("(.:)(\\\\(.)*)*\\s");
    private final Pattern cliCommand = Pattern.compile("(-cli)");

    @Override
    public void start(Stage primaryStage) throws Exception {
        Map<String, String> args = getParameters().getNamed(); // Get named arguments to application
        DefaultModel model = new DefaultModel(new int[] {5, 4, 3, 2, 2}); // Default model, randomly generates 5 ships;
                                                            // used if file not found

        if (args.containsKey("load")) { // If "load" argument is used
            File battleground = new File(args.get("load"));
            if (battleground.isFile()) { // If given file path is valid
                System.out.println("Loading: " + args.get("load"));
                model = new DefaultModel(battleground); // Replace default model with file's ship list
            }
        }

        if (args.containsKey("cli") && Boolean.parseBoolean(args.get("cli"))) { // If argument
            ConsoleController cli = new ConsoleController(model);
            Thread cliThread = new Thread(cli::commandProcess);
            cliThread.setDaemon(true);
            cliThread.start();
        }

        GUIController controller = new GUIController(model);
        BoardView view = new BoardView(controller);
        model.addObserver(view);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Battlefield.fxml"));
        loader.setControllerFactory(type -> {
            if (type == BoardView.class) {
                return view;
            } else {
                throw new IllegalArgumentException("Unexpected controller type: " + type);
            }
        });

        Parent root = loader.load();
        primaryStage.setTitle("Battleships");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }
}
