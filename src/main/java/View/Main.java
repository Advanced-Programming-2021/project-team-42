package View;

import SceneController.Login;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;
        Login.getInstance().start(Main.stage);
    }
}