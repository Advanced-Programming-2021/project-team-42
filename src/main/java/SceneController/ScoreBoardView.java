package SceneController;

import Controller.ScoreBoardController;
import View.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ScoreBoardView{
    private static ScoreBoardView instance = null;
    private static Stage stage;
    public VBox scoreBoard;

    public void start(Stage stage) throws Exception {
        ScoreBoardView.stage = stage;
        Image image = new Image(getClass().getResource("/Assets/rsz_profilebackground.jpg").toExternalForm());
        ImageView imageView = new ImageView(image);
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/ScoreBoardScene.fxml"));
        pane.getChildren().add(0, imageView);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        ScoreBoardController.getInstance().showScoreboard(scoreBoard);
    }

    public static ScoreBoardView getInstance() {
        if (instance == null)
            instance = new ScoreBoardView();
        return instance;
    }

    public void backToMainView() {
        try {
            MainView.getInstance().start(Main.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
