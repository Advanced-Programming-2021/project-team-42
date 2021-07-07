package SceneController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DeckView {
    private static DeckView instance = null;
    private static Stage stage;

    private DeckView(){}

    public void start(Stage stage) throws Exception{
        DeckView.stage = stage;
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/DeckScene.fxml"));
        Image image = new Image(getClass().getResource("").toExternalForm());
        ImageView imageView = new ImageView(image);
        pane.getChildren().add(0, imageView);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize(){

    }



    public static DeckView getInstance(){
        if(instance == null)
            instance = new DeckView();
        return instance;
    }
}
