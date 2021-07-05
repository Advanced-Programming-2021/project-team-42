package SceneController;

import Controller.UserController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ProfileView extends Application {
    private static ProfileView instance = null;
    public TextField newNickName;
    public TextField currentPassWord;
    public TextField newPassWord;
    public TextField changeNickNameError;
    public TextField changePassWordError;


    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/FXML/ProfileScene.fxml"))));
        stage.show();
    }



    public static ProfileView getInstance() {
        if (instance == null)
            instance = new ProfileView();
        return instance;
    }

    public void changeNickName(MouseEvent mouseEvent) {
        String userName = UserController.getInstance().userName;
        changeNickNameError.setText(UserController.getInstance().changeNickname(userName ,newNickName.getText().trim()));
    }

    public void changePassWord(MouseEvent mouseEvent) {
        String userName = UserController.getInstance().userName;
        changePassWordError.setText(UserController.getInstance().changePassword(userName ,currentPassWord.getText().trim() ,newPassWord.getText().trim()));
    }
}
