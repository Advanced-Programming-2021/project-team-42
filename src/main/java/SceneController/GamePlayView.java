package SceneController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GamePlayView {
    private static GamePlayView instance = null;

    public void start(Stage stage) throws Exception {
        Image image = new Image(getClass().getResource("/Assets/fie_dark.bmp").toExternalForm());
        ImageView imageView = new ImageView(image);
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/GamePlayScene.fxml"));
        imageView.setX(315);
        imageView.setY(100);
        pane.getChildren().add(0, imageView);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public static GamePlayView getInstance() {
        if (instance == null)
            instance = new GamePlayView();
        return instance;
    }
}
