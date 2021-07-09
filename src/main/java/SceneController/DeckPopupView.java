package SceneController;

import Controller.DeckController;
import Controller.UserController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class DeckPopupView {
    private static DeckPopupView instance = null;
    private static Stage popupStage;
    public TextField deckName;
    public Label creationResponse;


    public static DeckPopupView getInstance() {
        if (instance == null)
            instance = new DeckPopupView();
        return instance;
    }

    public void start() throws Exception {
        Stage popupStage = new Stage();
        DeckPopupView.popupStage = popupStage;
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/newDeckPopup.fxml"));
        Image image = new Image(getClass().getResource("/Assets/popupback.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        pane.getChildren().add(0, imageView);
        Scene scene = new Scene(pane);
        popupStage.setScene(scene);
        popupStage.show();
    }

    public void closePopup() throws Exception{
        DeckPopupView.popupStage.close();
        DeckView.getInstance().start(DeckView.stage);
    }

    public void createDeck() {
        try {
            DeckController.getInstance().createDeck(UserController.getInstance().getLoggedInUser().getUsername(),
                    deckName.getText());
            creationResponse.setText("Deck created successfully");
            creationResponse.setVisible(true);
        } catch (Exception e) {
            creationResponse.setText(e.getMessage());
            creationResponse.setVisible(true);
        }
    }
}
