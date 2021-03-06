package SceneController;

import Server.Controller.DeckController;
import Server.Controller.RegisterController;
import Server.Controller.UserController;
import Server.Model.User;
import View.Main;
import com.google.gson.Gson;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileReader;

public class Login {
    private static Login instance = null;
    private static Stage stage;
    private static MediaPlayer mediaPlayer;
    public TextField userName;
    public PasswordField passWord;
    public Label error;

    public void start(Stage stage) throws Exception {
        Login.stage = stage;
        Image image = new Image(getClass().getResource("/Assets/rsz_loginsignup.jpg").toExternalForm());
        ImageView imageView = new ImageView(image);
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/Login.fxml"));
        pane.getChildren().add(0, imageView);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        playMusic("/Assets/1-05 - Normal Duel (DM／GX).mp3");
        stage.show();
    }

    public void playMusic(String musicPath) {
        Media media = new Media(getClass().getResource(musicPath).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        Login.mediaPlayer = mediaPlayer;
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        mediaPlayer.play();
    }

    public void stopMusic(){
        mediaPlayer.setMute(true);
    }


    public void loginClicked() {
        try{
            Main.dataOutputStream.writeUTF("login," + userName.getText() + "," + passWord.getText());
            Main.dataOutputStream.flush();
            String result = Main.dataInputStream.readUTF();
            if (result.startsWith("error")) {
                error.setText(result.substring(6));
                error.setVisible(true);
            } else {
                Main.token = result;
                Gson gson = new Gson();
                FileReader fileReader = new FileReader("src/main/java/Database/Users" + "/" + userName.getText() + ".json");
                MainView.loggedInUser = gson.fromJson(fileReader, User.class);
                fileReader.close();
                MainView.getInstance().start(Main.stage);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void moveToSignUp() {
        try {
            SignUp.getInstance().start(Main.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exitGame() {
        RegisterController.rewriteData();
        DeckController.rewriteData();
        System.exit(0);
    }


    public static Login getInstance(){
        if (instance == null)
            instance = new Login();
        return instance;
    }
}
