package SceneController;

import Controller.DuelController;
import Controller.UserController;
import View.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class DuelView {
    private static DuelView instance = null;
    private static Stage stage;
    public Rectangle backGround;
    public Label Error;
    public SplitMenuButton chooseRands;
    public String rands = null;
    public Button applyButton;
    public Button backButton;
    public TextField secondPlayer;
    private int rounds;

    public void start(Stage stage) throws IOException {
        DuelView.stage = stage;
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/DuelScene.fxml"));
        stage.setScene(new Scene(pane));
        stage.show();
    }

    public void initialize() {
        backGround.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/DuelScenePhoto.png").toExternalForm())));
        setCursor(applyButton);
        setCursor(backButton);
    }

    public void setCursor(Button button) {
        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                button.setCursor(Cursor.HAND);
            }
        });
        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                button.setCursor(Cursor.DEFAULT);
            }
        });
    }


    public void applyClicked(MouseEvent mouseEvent) throws Exception {
        if (rands == null) {
            Error.setText("choose number of rands");
            Error.setVisible(true);
            return;
        }
        try {
            DuelController.getInstance().startNewDuel(UserController.getInstance().getLoggedInUser().getUsername()
                    , secondPlayer.getText().trim(), rands);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText(e.getMessage());
            alert.show();
            if (e.getMessage().startsWith("new")) {
                GamePlayView.rounds = rounds;
                GamePlayView.getInstance().start(MainView.stage);
            }
        }
    }


    public static DuelView getInstance() {
        if (instance == null)
            instance = new DuelView();
        return instance;
    }


    public void exitClicked(MouseEvent mouseEvent) {
        try {
            MainView.getInstance().start(Main.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void oneRandSelected(ActionEvent actionEvent) {
        rands = "1";
        rounds = Integer.parseInt(rands);
        chooseRands.setText("1 rand");
    }

    public void threeRandsSelected(ActionEvent actionEvent) {
        rands = "3";
        rounds = Integer.parseInt(rands);
        chooseRands.setText("3 rands");
    }
}
