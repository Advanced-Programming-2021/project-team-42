package SceneController;

import View.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MainView extends Application {
    private static MainView instance = null;
    public String userName;
    public Rectangle deckItem;
    public Rectangle shopItem;
    public Rectangle scoreBordItem;
    public Rectangle profileItem;
    public Rectangle duelItem;
    public Rectangle ImpExpItem;
    public Rectangle exitItem;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/FXML/MainScene.fxml"))));
        stage.show();
    }

    public void initialize(){
        fillItems();
    }

    public static MainView getInstance() {
        if (instance == null)
            instance = new MainView();
        return instance;
    }

    public void fillItems() {
        deckItem.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/Deck.jpg").toExternalForm())));
        shopItem.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/Shop.jpg").toExternalForm())));
        scoreBordItem.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/ScoreBoard.jpg").toExternalForm())));
        profileItem.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/Profile.jpg").toExternalForm())));
        duelItem.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/Duel.jpg").toExternalForm())));
        ImpExpItem.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/ImpExp.jpg").toExternalForm())));
        exitItem.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/Exit.jpg").toExternalForm())));
    }

    public void deckClicked(MouseEvent mouseEvent) {
    }

    public void shopClicked(MouseEvent mouseEvent) {
    }

    public void scoreBoardClicked(MouseEvent mouseEvent) {
        try {
            ScoreBoardView.getInstance().start(Main.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void profileClicked(MouseEvent mouseEvent) {
        try {
            ProfileView.getInstance().start(Main.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void duelClicked(MouseEvent mouseEvent) {
    }

    public void ImportExportClicked(MouseEvent mouseEvent) {
    }

    public void ExitClicked(MouseEvent mouseEvent) {
        try {
            Login.getInstance().start(Main.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
