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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SignUp {
    private static SignUp instance = null;
    private static Stage stage;
    public TextField userName;
    public TextField nickName;
    public PasswordField password;
    public Label error;

    public void start(Stage stage) throws Exception {
        SignUp.stage = stage;
        Image image = new Image(getClass().getResource("/Assets/rsz_loginsignup.jpg").toExternalForm());
        ImageView imageView = new ImageView(image);
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/SignUp.fxml"));
        pane.getChildren().add(0, imageView);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }


    public void signUpClicked() {
        try{
            RegisterController.getInstance().createNewUser(userName.getText(), nickName.getText(), password.getText());
            error.setText("User created successfully");
            error.setVisible(true);
        } catch (Exception e){
            error.setText(e.getMessage());
            error.setVisible(true);
        }
    }

    public void moveToLogin() {
        try {
            Login.getInstance().start(Main.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exitGame() {
        RegisterController.rewriteData();
        DeckController.rewriteData();
        System.exit(0);
    }



    public static SignUp getInstance(){
        if (instance == null)
            instance = new SignUp();
        return instance;
    }


}
