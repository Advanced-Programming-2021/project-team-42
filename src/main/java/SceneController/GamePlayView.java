package SceneController;


import Controller.UserController;
import Model.GameBoard;
import Model.MonsterCard;
import Model.SpellTrapCard;
import View.GamePhases;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

public class GamePlayView {
    private static GamePlayView instance = null;
    public ImageView opponentProfile;
    public ImageView yourProfile;
    public Label opponentLP;
    public Label opponentNickname;
    public Label yourLP;
    public Label yourNickname;
    public Label yourUsername;
    public Label opponentUsername;
    private static GamePhases currentPhase = GamePhases.DRAW;
    public Label gamePhaseLabel;
    private GameBoard firstPlayersBoard = null;
    private GameBoard secondPlayersBoard = null;
    public Pane firstPlayersMonsterZone;
    public Pane firstPlayersSpellTrapZone;
    public Pane firstPlayersFieldZone;
    public ScrollPane firstPlayersCardsInHand;
    public Pane secondPlayersMonsterZone;
    public Pane secondPlayersSpellTrapZone;
    public Pane secondPlayersFieldZone;
    public ScrollPane secondPlayersCardsInHand;

    public void start(Stage stage) throws Exception {
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/GamePlayScene.fxml"));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }



    
    public void initialize() {
        gamePhaseLabel.setText(currentPhase.name() + " PHASE");
        firstPlayersBoard = UserController.getInstance().getFirstPlayersBoard();
        secondPlayersBoard = UserController.getInstance().getSecondPlayersBoard();
        loadFirstPlayersMonsterCards(firstPlayersMonsterZone);
        loadFirstPlayersSpellTrapCards(firstPlayersSpellTrapZone);
        loadFirstPlayersFieldZoneCard(firstPlayersFieldZone);
        loadFirstPlayersCardsInHand(firstPlayersCardsInHand);
        loadSecondPlayersMonsterCards(secondPlayersMonsterZone);
        loadSecondPlayersSpellTrapCards(secondPlayersSpellTrapZone);
        loadSecondPlayersFieldZoneCard(secondPlayersFieldZone);
        loadSecondPlayersCardsInHand(secondPlayersCardsInHand);
        loadProfiles(yourProfile, opponentProfile, yourUsername, opponentUsername, yourLP, opponentLP, yourNickname, opponentNickname, firstPlayersBoard, secondPlayersBoard);
    }

    private void loadProfiles(ImageView yourProfile, ImageView opponentProfile, Label yourUsername, Label opponentUsername,Label yourLP, Label opponentLP, Label yourNickname, Label opponentNickname, GameBoard firstPlayersBoard, GameBoard secondPlayersBoard) {
        yourProfile.setImage(new Image(getClass().getResource(firstPlayersBoard.getPlayer().getPathToProfilePhoto()).toExternalForm()));
        opponentProfile.setImage(new Image(getClass().getResource(secondPlayersBoard.getPlayer().getPathToProfilePhoto()).toExternalForm()));
        yourLP.setText("LP: " + firstPlayersBoard.getPlayer().getLP());
        opponentLP.setText("LP: " + secondPlayersBoard.getPlayer().getLP());
        yourUsername.setText("Username: " + firstPlayersBoard.getPlayer().getUsername());
        opponentUsername.setText("Username: " + secondPlayersBoard.getPlayer().getUsername());
        yourNickname.setText("Nickname: " + firstPlayersBoard.getPlayer().getNickname());
        opponentNickname.setText("Nickname: " + secondPlayersBoard.getPlayer().getNickname());
    }

    public void loadFirstPlayersMonsterCards(Pane pane) {
        HBox hBox = new HBox();
        HashMap<Integer, MonsterCard> cards = UserController.getInstance().getFirstPlayersBoard().getMonstersPlace();
        for (Map.Entry<Integer, MonsterCard> entry : cards.entrySet()) {
            if (entry.getValue() == null) continue;
            Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(entry.getValue().getName()) + ".jpg").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(67.2);
            imageView.setFitHeight(98);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                }
            });
            hBox.getChildren().add(imageView);
        }
        pane.getChildren().add(hBox);
    }



    private void loadFirstPlayersSpellTrapCards(Pane pane) {
        HBox hBox = new HBox();
        HashMap<Integer, SpellTrapCard> cards = UserController.getInstance().getFirstPlayersBoard().getSpellTrapsPlace();
        for (Map.Entry<Integer, SpellTrapCard> entry : cards.entrySet()) {
            if (entry.getValue() == null) continue;
            Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(entry.getValue().getName()) + ".jpg").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(67.2);
            imageView.setFitHeight(98);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                }
            });
            hBox.getChildren().add(imageView);
        }
        pane.getChildren().add(hBox);
    }



    public void loadFirstPlayersFieldZoneCard(Pane pane) {
        if (firstPlayersBoard.getFieldZone() == null) return;
        HBox hBox = new HBox();
        Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(firstPlayersBoard.getFieldZone().getName()) + ".jpg").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(67.2);
        imageView.setFitHeight(98);
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

            }
        });
        hBox.getChildren().add(imageView);
        pane.getChildren().add(hBox);
    }



    public void loadFirstPlayersCardsInHand(ScrollPane scrollPane) {
        HBox hBox = new HBox();
        for (int i = 0 ; i < firstPlayersBoard.getCardsInHand().size() ; i++) {
            Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(firstPlayersBoard.getCardsInHand().get(i).getName()) + ".jpg").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(67.2);
            imageView.setFitHeight(98);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                }
            });
            hBox.getChildren().add(imageView);
        }
        scrollPane.setContent(hBox);
    }


    public void loadSecondPlayersMonsterCards(Pane pane) {
        HBox hBox = new HBox();
        HashMap<Integer, MonsterCard> cards = UserController.getInstance().getSecondPlayersBoard().getMonstersPlace();
        for (Map.Entry<Integer, MonsterCard> entry : cards.entrySet()) {
            if (entry.getValue() == null) continue;
            Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(entry.getValue().getName()) + ".jpg").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(67.2);
            imageView.setFitHeight(98);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                }
            });
            hBox.getChildren().add(imageView);
        }
        pane.getChildren().add(hBox);
    }



    private void loadSecondPlayersSpellTrapCards(Pane pane) {
        HBox hBox = new HBox();
        HashMap<Integer, SpellTrapCard> cards = UserController.getInstance().getSecondPlayersBoard().getSpellTrapsPlace();
        for (Map.Entry<Integer, SpellTrapCard> entry : cards.entrySet()) {
            if (entry.getValue() == null) continue;
            Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(entry.getValue().getName()) + ".jpg").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(67.2);
            imageView.setFitHeight(98);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                }
            });
            hBox.getChildren().add(imageView);
        }
        pane.getChildren().add(hBox);
    }



    public void loadSecondPlayersFieldZoneCard(Pane pane) {
        if (secondPlayersBoard.getFieldZone() == null) return;
        HBox hBox = new HBox();
        Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(secondPlayersBoard.getFieldZone().getName()) + ".jpg").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(67.2);
        imageView.setFitHeight(98);
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

            }
        });
        hBox.getChildren().add(imageView);
        pane.getChildren().add(hBox);
    }



    public void loadSecondPlayersCardsInHand(ScrollPane scrollPane) {
        HBox hBox = new HBox();
        for (int i = 0 ; i < secondPlayersBoard.getCardsInHand().size() ; i++) {
            Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(secondPlayersBoard.getCardsInHand().get(i).getName()) + ".jpg").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(67.2);
            imageView.setFitHeight(98);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                }
            });
            hBox.getChildren().add(imageView);
        }
        scrollPane.setContent(hBox);
    }




    public String toCamelCase(String s) {
        String result = "";
        String[] tokens = s.split(" ");
        for (int i = 0, L = tokens.length; i<L; i++) {
            String token = tokens[i];
            if (i==0) result += token.toLowerCase();
            else
                result += token.substring(0, 1).toUpperCase() +
                        token.substring(1).toLowerCase();
        }
        return result.substring(0, 1).toUpperCase() + result.substring(1);
    }






    public static GamePlayView getInstance() {
        if (instance == null)
            instance = new GamePlayView();
        return instance;
    }

    public void changePhase(MouseEvent mouseEvent) {
    }

    public void settingPopup(MouseEvent mouseEvent) {
    }

    public void summon(MouseEvent mouseEvent) {
    }

    public void set(MouseEvent mouseEvent) {
    }

    public void directAttack(MouseEvent mouseEvent) {
    }

    public void attackToCard(MouseEvent mouseEvent) {
    }

    public void changePosition(MouseEvent mouseEvent) {
    }

    public void yourGraveyard(MouseEvent mouseEvent) {
    }

    public void opponentGraveyard(MouseEvent mouseEvent) {
    }


//    public void refreshCards() {
//        for (int i = 0 ; i < 5 ; i++){
//            if (firstPlayersBoard.getMonstersPlace().containsKey(i+1))
//                drawFirstPlayersCard(firstPlayersBoard.getMonstersPlace().get(i+1) ,i);
//        }
//        for (int i = 0 ; i < 5 ; i++){
//            if (firstPlayersBoard.getSpellTrapsPlace().containsKey(i+1))
//                drawFirstPlayersCard(firstPlayersBoard.getSpellTrapsPlace().get(i+1) ,i);
//        }
//        if (firstPlayersBoard.getFieldZone() != null) {
//            Rectangle rectangle = new Rectangle();
//            rectangle.setX(635.0);
//            rectangle.setY(333);
//            rectangle.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/"+ toCamelCase(firstPlayersBoard.getFieldZone().getName()) +".png").toExternalForm())));
//
//        }
//    }

//    public void drawFirstPlayersCard(Card card ,int number) {
//        Rectangle rectangle = new Rectangle();
//        rectangle.setY(303);
//        rectangle.setX(292 + 67*(number+1));
//        rectangle.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/"+ toCamelCase(card.getName()) +".png").toExternalForm())));
//        if (MonsterCard.getAllMonsterCards().contains(card)){
//            rectangle.setY(393);
//            monsterZone.getChildren().add(rectangle);
//            monsterCards.put(rectangle ,(MonsterCard) card);
//        }
//        else {
//            spellTrapZone.getChildren().add(rectangle);
//            spellTrapCards.put(rectangle ,(SpellTrapCard) card);
//        }
//    }
}
