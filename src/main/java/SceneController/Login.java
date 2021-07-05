package SceneController;

import Controller.DeckController;
import Controller.LoginController;
import Controller.UserController;
import View.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Login extends Application {
    private static Login instance = null;
    public TextField userName;
    public TextField passWord;
    public Label error;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/FXML/Login.fxml"))));
        stage.show();
    }



    public void loginClicked(MouseEvent mouseEvent) throws Exception {
        if (LoginController.getInstance().canLogin(userName.getText() ,passWord.getText())) {
            UserController.getInstance().userName = this.userName.getText().trim();
            MainView.getInstance().start(Main.stage);
        }
        else
            error.setVisible(true);
    }



    public void moveToSignUp(MouseEvent mouseEvent) {
        try {
            SignUp.getInstance().start(Main.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void exitGame(MouseEvent mouseEvent) {
        LoginController.rewriteData();
        DeckController.rewriteData();
        System.exit(0);
    }


    public static Login getInstance(){
        if (instance == null)
            instance = new Login();
        return instance;
    }
}
