package SceneController;


import Server.Controller.CardController;
import Server.Controller.RegisterController;
import Server.Controller.ShopController;
import Server.Controller.UserController;
import Server.Model.Card;
import View.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class ShopView {
    private static ShopView instance = null;

    public void start(Stage stage) throws Exception {
        CardController.parseCards();
        Image image = new Image(getClass().getResource("/Assets/50061.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(600);
        imageView.setFitWidth(900);
        ScrollPane scrollPane = FXMLLoader.load(getClass().getResource("/FXML/ShopScene.fxml"));
        Pane pane = new Pane();
        pane.getChildren().add(0, imageView);
        fillCards(pane);
        fillBuyItems(pane);
        setBackButton(pane);
        scrollPane.setContent(pane);
        Scene scene = new Scene(scrollPane);
        stage.setScene(scene);
        stage.show();
    }


    public void fillCards(Pane pane) {
        for (int i = 0; i < 49; i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setHeight(247.9);
            rectangle.setWidth(170);
            rectangle.setY((i / 4) * 300 + 30);
            rectangle.setX((i % 4) * 170 + 30 * ((i % 4) + 1) + 32);
            rectangle.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/" + getCardName(i) + ".jpg").toExternalForm())));
            pane.getChildren().add(i + 1, rectangle);
        }
    }

    public void fillBuyItems(Pane pane) {
        for (int i = 0; i < 49; i++) {
            Rectangle buyItem = new Rectangle();
            buyItem.setHeight(30);
            buyItem.setWidth(70);
            buyItem.setY((i / 4) * 300 + 30 + 217.9);
            buyItem.setX((i % 4) * 170 + 30 * ((i % 4) + 1) + 32);
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
                    if (mouseEvent.getButton() == MouseButton.PRIMARY)
                        buySell(pane, finalI, 1);
                    else
                        buySell(pane, finalI, 0);
                }
            });
            buyItem.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/BuyItem.png").toExternalForm())));
            pane.getChildren().add(i + 1 + 49, buyItem);
            if (Card.getAllCards().get(i).getPrice() > MainView.loggedInUser.getBalance()) {
                buyItem.setVisible(false);
            }
        }
    }

    public void buySell(Pane pane, int i, int flag) {
        try {
            System.out.println("buyCard," + Main.token + "," + Card.getAllCards().get(i).getName());
            if (flag == 1)
                Main.dataOutputStream.writeUTF("buyCard," + Main.token + "," + Card.getAllCards().get(i).getName());
            else
                Main.dataOutputStream.writeUTF("sellCard," + Main.token + "," + Card.getAllCards().get(i).getName());
            Main.dataOutputStream.flush();
            String result = Main.dataInputStream.readUTF();
            Alert alert;
            if (result.startsWith("error")) {
                alert = new Alert(Alert.AlertType.NONE);
                alert.setAlertType(Alert.AlertType.INFORMATION);
                if (numberOfCard(i) <= 1) alert.setContentText(result.substring(6));
                else
                    alert.setContentText(result.substring(6) + "\n\nYou've bought " + numberOfCard(i) + " of this card");
            } else {
                alert = new Alert(Alert.AlertType.INFORMATION);
                if (flag == 1)
                    alert.setContentText("card " + Card.getAllCards().get(i).getName() + " added to your cards successfully");
                else
                    alert.setContentText("card " + Card.getAllCards().get(i).getName() + " sold successfully");
            }
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int j = 0; j < 49; j++) {
            if (Card.getAllCards().get(j).getPrice() > MainView.loggedInUser.getBalance()) {
                pane.getChildren().get(j + 50).setVisible(false);
            }
        }
    }

    public int numberOfCard(int i) {
        int number = 0;
        if (MainView.loggedInUser.doesUserHasThisCard(Card.getAllCards().get(i).getName()))
            number = MainView.loggedInUser.getUserAllCards().get(Card.getAllCards().get(i).getName());
        return number;
    }

    public String getCardName(int i) {
        String cardName = Card.getAllCards().get(i).getName();
        return toCamelCase(cardName);
    }

    public String toCamelCase(String s) {
        String result = "";
        String[] tokens = s.split(" ");
        for (int i = 0, L = tokens.length; i < L; i++) {
            String token = tokens[i];
            if (i == 0) result += token.toLowerCase();
            else
                result += token.substring(0, 1).toUpperCase() +
                        token.substring(1, token.length()).toLowerCase();
        }
        return result.substring(0, 1).toUpperCase() + result.substring(1);
    }


    public void setBackButton(Pane pane) {
        Button button = new Button();
        button.setVisible(true);
        button.setLayoutX(5);
        button.setLayoutY(5);
        button.setMinHeight(25.0);
        button.setMinWidth(92.0);
        button.setText("Back");
        Font font = Font.font("Courier New", FontWeight.BOLD, 12);
        button.setFont(font);
        button.setMnemonicParsing(false);
        button.setStyle("-fx-background-color: #d73535;");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    RegisterController.rewriteData();
                    MainView.getInstance().start(MainView.stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        pane.getChildren().add(99, button);
    }


    public static ShopView getInstance() {
        if (instance == null)
            instance = new ShopView();
        return instance;
    }


}
