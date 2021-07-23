package SceneController;

import Server.Model.User;
import View.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MainView {
    private static MainView instance = null;
    public static User loggedInUser = null;
    public static Stage stage;
    public String userName;
    public Rectangle deckItem;
    public Rectangle shopItem;
    public Rectangle scoreBoardItem;
    public Rectangle profileItem;
    public Rectangle duelItem;
    public Rectangle ImpExpItem;
    public Rectangle exitItem;

    public void start(Stage stage) throws Exception {
        MainView.stage = stage;
        Image image = new Image(getClass().getResource("/Assets/rsz_newback.jpg").toExternalForm());
        ImageView imageView = new ImageView(image);
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/MainScene.fxml"));
        pane.getChildren().add(0, imageView);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
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
        deckItem.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/decknew.png").toExternalForm())));
        deckItem.setStroke(Color.TRANSPARENT);
        shopItem.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/shopnew.png").toExternalForm())));
        shopItem.setStroke(Color.TRANSPARENT);
        scoreBoardItem.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/scoreboardnew.png").toExternalForm())));
        scoreBoardItem.setStroke(Color.TRANSPARENT);
        profileItem.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/profilenew.png").toExternalForm())));
        profileItem.setStroke(Color.TRANSPARENT);
        duelItem.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/duelnew.png").toExternalForm())));
        duelItem.setStroke(Color.TRANSPARENT);
        ImpExpItem.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/impexpnew.png").toExternalForm())));
        ImpExpItem.setStroke(Color.TRANSPARENT);
        exitItem.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/exitnew.png").toExternalForm())));
        exitItem.setStroke(Color.TRANSPARENT);
    }

    public void deckClicked() {
        try {
            DeckView.getInstance().start(MainView.stage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void shopClicked() {
        try {
            ShopView.getInstance().start(MainView.stage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void scoreBoardClicked() {
        try {
            ScoreBoardView.getInstance().start(Main.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void profileClicked() {
        try {
            ProfileView.getInstance().start(Main.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void duelClicked() {
        try {
            DuelView.getInstance().start(Main.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ImportExportClicked() {
        try {
            IAndEView.getInstance().start(MainView.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ExitClicked() {
        try {
            Main.dataOutputStream.writeUTF("logout," + Main.token);
            Main.dataOutputStream.flush();
            String result = Main.dataInputStream.readUTF();
            Login.getInstance().stopMusic();
            Login.getInstance().start(MainView.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
