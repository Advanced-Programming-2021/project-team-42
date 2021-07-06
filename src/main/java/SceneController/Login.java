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
import javafx.stage.Stage;

public class Login {
    private static Login instance = null;
    private static Stage stage;
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
        stage.show();
    }

    public void loginClicked() throws Exception {
        try{
            RegisterController.getInstance().loginUser(userName.getText(), passWord.getText());
            MainView.getInstance().start(Main.stage);

        } catch (Exception e){
            error.setText(e.getMessage());
            error.setVisible(true);
        }
//        if (LoginController.getInstance().canLogin(userName.getText() ,passWord.getText())) {
//            UserController.getInstance().userName = this.userName.getText().trim();
//            MainView.getInstance().start(Main.stage);
//        }
//        else
//            error.setVisible(true);
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
