package SceneController;

import Controller.ScoreBoardController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ScoreBoardView extends Application {
    private static ScoreBoardView instance = null;
    public TextArea ScoreBoard;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/FXML/ScoreBoardScene.fxml"))));
        stage.show();
    }

    public void initialize() {
        ScoreBoard.setText(ScoreBoardController.getInstance().showScoreboard());
    }

    public static ScoreBoardView getInstance() {
        if (instance == null)
            instance = new ScoreBoardView();
        return instance;
    }
}
