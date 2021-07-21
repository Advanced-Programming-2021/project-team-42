package SceneController;

import Server.Controller.DeckController;
import Server.Controller.UserController;
import View.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class DeckView {
    private static DeckView instance = null;
    public static Stage stage;
    public Rectangle selectDeck;
    public Rectangle newDeck;
    public VBox decksName;

    public void start(Stage stage) throws Exception {
        DeckView.stage = stage;
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/DeckScene.fxml"));
        Image image = new Image(getClass().getResource("/Assets/rsz_deckback.jpg").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setOpacity(0.6);
        pane.getChildren().add(0, imageView);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        selectDeck.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/selectDeck.png").toExternalForm())));
        selectDeck.setStroke(Color.TRANSPARENT);
        newDeck.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/createNewDeck.png").toExternalForm())));
        newDeck.setStroke(Color.TRANSPARENT);
        DeckController.getInstance().showAllDecks(MainView.loggedInUser,
                decksName);
    }

    public static DeckView getInstance() {
        if (instance == null)
            instance = new DeckView();
        return instance;
    }

    public void selectDeck() throws Exception {
        SelectDeckPopup.getInstance().start();
    }

    public void createNewDeck() throws Exception {
        DeckPopupView.getInstance().start();
    }

    public void backToMainView() {
        try {
            MainView.getInstance().start(Main.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
