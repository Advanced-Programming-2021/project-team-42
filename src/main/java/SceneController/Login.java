package SceneController;

import Controller.DeckController;
import Controller.RegisterController;
import View.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

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
        playMusic();
        stage.show();
    }

    public void playMusic() {
        Media media = new Media(getClass().getResource("/Assets/1-05 - Normal Duel (DM／GX).mp3").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        Login.mediaPlayer = mediaPlayer;
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        mediaPlayer.play();
    }


    public void loginClicked() {
        try{
            RegisterController.getInstance().loginUser(userName.getText(), passWord.getText());
            MainView.getInstance().start(Main.stage);

        } catch (Exception e){
            error.setText(e.getMessage());
            error.setVisible(true);
        }
    }



    public void moveToSignUp() {
        try {
            SignUp.getInstance().start(Main.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void exitGame(MouseEvent mouseEvent) {
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
