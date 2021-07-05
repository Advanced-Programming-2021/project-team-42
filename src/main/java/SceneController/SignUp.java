package SceneController;

import Controller.DeckController;
import Controller.LoginController;
import View.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SignUp extends Application {
    private static SignUp instance = null;
    public TextField userName;
    public TextField nickName;
    public TextField password;
    public Label error;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/FXML/SignUp.fxml"))));
        stage.show();
    }


    public void signUpClicked(MouseEvent mouseEvent) throws Exception {
        if (LoginController.getInstance().canSignUp(userName.getText().trim() ,nickName.getText().trim() ,password.getText().trim())) {
            Login.getInstance().start(Main.stage);
        }
        else
            error.setVisible(true);
    }



    public void moveToLogin(MouseEvent mouseEvent) {
        try {
            Login.getInstance().start(Main.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void exitGame(MouseEvent mouseEvent) {
        LoginController.rewriteData();
        DeckController.rewriteData();
        System.exit(0);
    }



    public static SignUp getInstance(){
        if (instance == null)
            instance = new SignUp();
        return instance;
    }


}
