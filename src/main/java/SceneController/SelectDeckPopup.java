package SceneController;

import Controller.UserController;
import Model.Deck;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SelectDeckPopup {
    private static SelectDeckPopup instance = null;
    private static Stage popupStage;
    public TextField deckName;
    public Label selectionResponse;

    public void start() throws Exception{
        Stage popupStage = new Stage();
        SelectDeckPopup.popupStage = popupStage;
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/SelectDeckScene.fxml"));
        Image image = new Image(getClass().getResource("/Assets/selectback.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        pane.getChildren().add(0, imageView);
        imageView.setOpacity(0.6);
        Scene scene = new Scene(pane);
        popupStage.setScene(scene);
        popupStage.show();
    }

    public static SelectDeckPopup getInstance() {
        if(instance == null)
            instance = new SelectDeckPopup();
        return instance;
    }

    public void closePopup() throws Exception{
        SelectDeckPopup.popupStage.close();
        DeckView.getInstance().start(DeckView.stage);
    }

    public void selectDeck() throws Exception{
        String deckNameStr = deckName.getText();
        if(!UserController.getInstance().getLoggedInUser().getUserDecks().contains(deckNameStr)){
            selectionResponse.setText("You have no deck with name " + deckNameStr);
            selectionResponse.setVisible(true);
        } else {
            SelectDeckPopup.popupStage.close();
            DeckControlView.deck = Deck.getDeckByName(deckNameStr);
            DeckControlView deckControlView = new DeckControlView();
            deckControlView.start(DeckView.stage);
        }
    }
}
