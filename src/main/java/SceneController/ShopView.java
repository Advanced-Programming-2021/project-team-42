package SceneController;


import Controller.ShopController;
import Controller.UserController;
import Model.Card;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class ShopView {
    private static ShopView instance = null;
    private static Stage stage;

    public void start(Stage stage) throws Exception {
        ShopView.stage = stage;
        Image image = new Image(getClass().getResource("/Assets/50061.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(600);
        imageView.setFitWidth(900);
        ScrollPane scrollPane = FXMLLoader.load(getClass().getResource("/FXML/ShopScene.fxml"));
        Pane pane = new Pane();
        pane.getChildren().add(0, imageView);
        fillCards(pane);
        fillBuyItems(pane);
        scrollPane.setContent(pane);
        Scene scene = new Scene(scrollPane);
        stage.setScene(scene);
        stage.show();
    }


    public void fillCards(Pane pane) {
        for (int i = 0 ; i < 49 ; i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setHeight(247.9);
            rectangle.setWidth(170);
            rectangle.setY((i / 4)*300 + 30);
            rectangle.setX((i % 4)*170 + 30*((i % 4) + 1) + 32);
            rectangle.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/" + getCardName(i) + ".jpg").toExternalForm())));
            pane.getChildren().add(i+1 ,rectangle);
        }
    }

    public void fillBuyItems(Pane pane) {
        for (int i = 0 ; i < 49 ; i++) {
            Rectangle buyItem = new Rectangle();
            buyItem.setHeight(30);
            buyItem.setWidth(70);
            buyItem.setY((i / 4)*300 + 30 + 217.9);
            buyItem.setX((i % 4)*170 + 30*((i % 4) + 1) + 32);
            buyItem.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    buyItem.setCursor(Cursor.HAND);
                }
            });
            buyItem.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    buyItem.setCursor(Cursor.DEFAULT);
                }
            });
            int finalI = i;
            buyItem.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    buyItem(pane ,finalI);
                }
            });
            buyItem.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/BuyItem.png").toExternalForm())));
            pane.getChildren().add(i+1+49 ,buyItem);
            if (Card.getAllCards().get(i).getPrice() > UserController.getInstance().getLoggedInUser().getBalance()) {
                buyItem.setVisible(false);
            }
        }
    }

    public void buyItem(Pane pane ,int i) {
        try{
            ShopController.getInstance().buyCard(UserController.getInstance().getLoggedInUser().getUsername() ,Card.getAllCards().get(i).getName());
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setAlertType(Alert.AlertType.INFORMATION);
            if (numberOfCard(i) <= 1) alert.setContentText(e.getMessage());
            else alert.setContentText(e.getMessage() + "\n\nYou've bought " + numberOfCard(i) + " of this card");
            alert.show();
        }
        for (int j = 0 ; j < 49 ; j++) {
            if (Card.getAllCards().get(j).getPrice() > UserController.getInstance().getLoggedInUser().getBalance()) {
                pane.getChildren().get(j+50).setVisible(false);
            }
        }
    }

    public int numberOfCard(int i) {
        int number = 0;
        if (UserController.getInstance().getLoggedInUser().doesUserHasThisCard(Card.getAllCards().get(i).getName()))
            number = UserController.getInstance().getLoggedInUser().getUserAllCards().get(Card.getAllCards().get(i).getName());
        return number;
    }

    public String getCardName(int i) {
        String cardName = Card.getAllCards().get(i).getName();
        return toCamelCase(cardName);
    }

    public String toCamelCase(String s) {
        String result = "";
        String[] tokens = s.split(" ");
        for (int i = 0, L = tokens.length; i<L; i++) {
            String token = tokens[i];
            if (i==0) result += token.toLowerCase();
            else
                result += token.substring(0, 1).toUpperCase() +
                        token.substring(1, token.length()).toLowerCase();
        }
        return result.substring(0, 1).toUpperCase() + result.substring(1);
    }


    public static ShopView getInstance() {
        if (instance == null)
            instance = new ShopView();
        return instance;
    }


    public void exitClicked(MouseEvent mouseEvent) {
        try {
            MainView.getInstance().start(stage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
