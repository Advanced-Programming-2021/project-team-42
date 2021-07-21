package SceneController;

import Server.Controller.DeckController;
import Server.Controller.UserController;
import Server.Model.Deck;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DeckControlView {
//    private static DeckControlView instance = null;
    public static Deck deck;
    public static Stage stage;
    public ScrollPane mainDeck;
    public ScrollPane yourCards;
    public ScrollPane sideDeck;
    public Label activationResponse;
    public Button addToMainDeck;
    public Button addToSideDeck;
    public Button removeFromDeck;
    public static String selectedCard;
    public static boolean isSide;


    public void start(Stage stage) throws Exception {
        DeckControlView.stage = stage;
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/DeckControlScene.fxml"));
        Image image = new Image(getClass().getResource("/Assets/rsz_deckcontrol.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setOpacity(0.6);
        pane.getChildren().add(0, imageView);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        DeckController.getInstance().loadCards(mainDeck, deck, false, addToMainDeck, addToSideDeck, removeFromDeck);
        DeckController.getInstance().loadCards(sideDeck, deck, true, addToMainDeck, addToSideDeck, removeFromDeck);
        DeckController.getInstance().loadYourCards(yourCards, removeFromDeck, addToMainDeck, addToSideDeck);

    }

    public void addToMainDeck(MouseEvent mouseEvent) {
        try {
            DeckController.getInstance().addCardToDeck(MainView.loggedInUser.getUsername(),
                    deck.getName(), selectedCard, false);
            initialize();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    public void addToSideDeck(MouseEvent mouseEvent) {
        try {
            DeckController.getInstance().addCardToDeck(MainView.loggedInUser.getUsername(),
                    deck.getName(), selectedCard, true);
            initialize();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    public void removeFromDeck(MouseEvent mouseEvent) {
        try {
            DeckController.getInstance().removeCardFromDeck(MainView.loggedInUser.getUsername(),
                    deck.getName(), selectedCard, isSide);
            initialize();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    public void setActive(MouseEvent mouseEvent) {
        try {
            DeckController.getInstance().setActiveDeck(MainView.loggedInUser.getUsername(),
                    deck.getName());
            activationResponse.setVisible(true);
        } catch (Exception e){
            activationResponse.setText(e.getMessage());
            activationResponse.setVisible(true);
        }
    }

    public void deleteDeck() {
        try {
            DeckController.getInstance().deleteDeck(MainView.loggedInUser.getUsername(),
                    deck.getName());
            DeckView.getInstance().start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void exitClicked(MouseEvent mouseEvent) {
        try {
            DeckView.getInstance().start(stage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
