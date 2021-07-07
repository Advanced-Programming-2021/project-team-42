package SceneController;

import Controller.UserController;
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

public class ProfileView {
    private static ProfileView instance = null;
    private static Stage stage;
    public TextField newNickName;
    public PasswordField currentPassWord;
    public PasswordField newPassWord;
    public ImageView profilePic;
    public ImageView shownProfilePic;
    public Label nickname;
    public Label username;
    public Label changeNickNameError;
    public Label changePassWordError;


    public void start(Stage stage) throws Exception {
        ProfileView.stage = stage;
        Image image = new Image(getClass().getResource("/Assets/rsz_2scoreboardnew.jpg").toExternalForm());
        ImageView imageView = new ImageView(image);
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/ProfileScene.fxml"));
        pane.getChildren().add(0, imageView);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        UserController.getInstance().load(username, nickname, profilePic);
        shownProfilePic.setImage(new Image(getClass().getResource("/Assets/ProfileImages/Chara001.dds1.png").toExternalForm()));
    }

    public static ProfileView getInstance() {
        if (instance == null)
            instance = new ProfileView();
        return instance;
    }

    public void changeNickName() {
        String username = UserController.getInstance().getLoggedInUser().getUsername();
        try {
            UserController.getInstance().changeNickname(username, newNickName.getText().trim());
            changeNickNameError.setText("Nickname changed successfully");
            changeNickNameError.setVisible(true);
        } catch (Exception e) {
            changeNickNameError.setText(e.getMessage());
            changeNickNameError.setVisible(true);
        }
    }

    public void changePassWord() {
        String userName = UserController.getInstance().getLoggedInUser().getUsername();
        try {
            UserController.getInstance().changePassword(userName, currentPassWord.getText().trim(), newPassWord.getText().trim());
            changePassWordError.setText("Password changed successfully");
            changePassWordError.setVisible(true);
        } catch (Exception e) {
            changePassWordError.setText(e.getMessage());
            changePassWordError.setVisible(true);
        }
    }

    public void exitClicked() {
        try {
            MainView.getInstance().start(Main.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nextPic() {
        UserController.getInstance().loadImage(shownProfilePic, true);
    }

    public void previousPic() {
        UserController.getInstance().loadImage(shownProfilePic, false);
    }

    public void changeProfilePic() {
        UserController.getInstance().setProfilePic(profilePic);
    }
}
