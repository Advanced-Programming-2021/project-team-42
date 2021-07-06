package View;

import SceneController.Login;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.Scanner;

public class Main extends Application {
    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
        RegisterMenu registerMenu = new RegisterMenu(null);
        Menu.setScanner(new Scanner(System.in));
        registerMenu.run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;
        Login.getInstance().start(Main.stage);
    }
}