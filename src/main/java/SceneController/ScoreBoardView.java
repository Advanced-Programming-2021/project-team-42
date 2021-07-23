package SceneController;

import View.Main;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ScoreBoardView {
    private static ScoreBoardView instance = null;
    private static Stage stage;
    public VBox vBox;
    public ScrollPane scrollPane;
    public AnchorPane anchorPane;

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
        try {
            Main.dataOutputStream.writeUTF("updateScoreBoard");
            Main.dataOutputStream.flush();
            String result = Main.dataInputStream.readUTF();
            createVBox(vBox, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createVBox(VBox vBox, String result) {
        vBox.getChildren().clear();
        String[] ranks = result.split("\n");
        for (String rank : ranks) {
            Text text = new Text(rank + "\n");
            text.setFill(Color.WHITE);
            Font font = new Font("Book Antiqua", 24);
            text.setFont(font);
            vBox.getChildren().add(text);
            vBox.setAlignment(Pos.TOP_CENTER);
        }
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

    public void refresh() {
        initialize();
    }
}
