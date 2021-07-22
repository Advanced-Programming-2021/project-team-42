package View;

import SceneController.Login;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Main extends Application {
    public static Stage stage;
    public static Socket socket;
    public static DataOutputStream dataOutputStream;
    public static DataInputStream dataInputStream;
    public static String token;

    public static void main(String[] args) throws IOException {

        new Thread(() -> {
            try {
                socket = new Socket("2.tcp.ngrok.io", 12006);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                System.out.println("socket created successfully");
            } catch (Exception e){
                e.printStackTrace();
            }
        }).start();

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;
        Login.getInstance().start(Main.stage);
    }
}