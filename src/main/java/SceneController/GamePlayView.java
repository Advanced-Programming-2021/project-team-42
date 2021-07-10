package SceneController;


import Controller.DuelController;
import Controller.UserController;
import Model.GameBoard;
import Model.MonsterCard;
import Model.SpellTrapCard;
import View.GamePhases;
import View.GamePlay;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GamePlayView {
    private static GamePlayView instance = null;


    public static Stage stage;
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
    public static GameBoard firstPlayersBoard = UserController.getInstance().getFirstPlayersBoard();
    public static GameBoard secondPlayersBoard = UserController.getInstance().getSecondPlayersBoard();
    public Pane firstPlayersMonsterZone;
    public Pane firstPlayersSpellTrapZone;
    public Pane firstPlayersFieldZone;
    public Pane firstPlayersGraveyard;
    public ScrollPane firstPlayersCardsInHand;
    public Pane secondPlayersMonsterZone;
    public Pane secondPlayersSpellTrapZone;
    public Pane secondPlayersFieldZone;
    public Pane secondPlayersGraveyard;
    public ScrollPane secondPlayersCardsInHand;
    public static int rounds;
    private static boolean FirstTime = true;
    private static boolean SummonedOrSetInThisPhase = false;
    public TextField cardNumber;
    private boolean CardAddedToHandInThisPhase = false;
    public Pane mainPane;
    public Pane previousFirstPlayerMonsterPane;
    public Pane previousFirstPlayerSpellTrapPane;
    public Pane previousSecondPlayerMonsterPane;
    public Pane previousSecondPlayerSpellTrapPane;


    public Pane previousFirstPlayerFieldZonePane;
    public Pane previousSecondPlayerFieldZonePane;


    public void start(Stage stage) throws Exception {
        Login.getInstance().stopMusic();
        Login.getInstance().playMusic("/Assets/Ramin Djawadi _ Game Of Thrones (320).mp3");
        GamePlayView.stage = stage;
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/GamePlayScene.fxml"));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() throws Exception {
        gameEndCheck();
        gamePhaseLabel.setText(currentPhase.name() + " PHASE");
//        firstPlayersBoard = UserController.getInstance().getFirstPlayersBoard();
//        secondPlayersBoard = UserController.getInstance().getSecondPlayersBoard();
        this.mainPane.getChildren().add(loadFirstPlayersMonsterCards());
        this.mainPane.getChildren().add(loadFirstPlayersSpellTrapCards());
        if (loadFirstPlayersFieldZoneCard() != null)
            this.mainPane.getChildren().add(loadFirstPlayersFieldZoneCard());
        loadFirstPlayersGraveyard(firstPlayersGraveyard);
        loadFirstPlayersCardsInHand(firstPlayersCardsInHand);
        this.mainPane.getChildren().add(loadSecondPlayersMonsterCards());
        this.mainPane.getChildren().add(loadSecondPlayersSpellTrapCards());
        if (loadSecondPlayersFieldZoneCard() != null)
            this.mainPane.getChildren().add(loadSecondPlayersFieldZoneCard());
        loadSecondPlayersGraveyard(secondPlayersGraveyard);
        loadSecondPlayersCardsInHand(secondPlayersCardsInHand);
        loadProfiles(yourProfile, opponentProfile, yourUsername, opponentUsername, yourLP, opponentLP, yourNickname, opponentNickname, firstPlayersBoard, secondPlayersBoard);
    }

        public void gameEndCheck() throws Exception {
        if (firstPlayersBoard.getPlayer().getLP() <= 0 ||
                secondPlayersBoard.getMainDeckCards().size() == 0) {
            stage.close();
            DuelController.getInstance().setWinner(firstPlayersBoard, secondPlayersBoard,
                    rounds, false);
        }
        if (secondPlayersBoard.getPlayer().getLP() <= 0 ||
                firstPlayersBoard.getMainDeckCards().size() == 0) {
            stage.close();
            DuelController.getInstance().setWinner(secondPlayersBoard, firstPlayersBoard,
                    rounds, false);
        }
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





    public static GamePhases getCurrentPhase() {
        return currentPhase;
    }

    public static void setCurrentPhase(GamePhases currentPhase) {
        GamePlayView.currentPhase = currentPhase;
    }

    public static boolean isFirstTime() {
        return FirstTime;
    }

    public static void setFirstTime(boolean firstTime) {
        FirstTime = firstTime;
    }

    public static boolean isSummonedOrSetInThisPhase() {
        return SummonedOrSetInThisPhase;
    }

    public static void setSummonedOrSetInThisPhase(boolean summonedOrSetInThisPhase) {
        SummonedOrSetInThisPhase = summonedOrSetInThisPhase;
    }

    public boolean isCardAddedToHandInThisPhase() {
        return CardAddedToHandInThisPhase;
    }

    public void setCardAddedToHandInThisPhase(boolean cardAddedToHandInThisPhase) {
        CardAddedToHandInThisPhase = cardAddedToHandInThisPhase;
    }


    public Pane loadFirstPlayersMonsterCards() {
        if (mainPane.getChildren().contains(previousFirstPlayerMonsterPane))
            mainPane.getChildren().remove(previousFirstPlayerMonsterPane);
        HBox hBox = new HBox();
        HashMap<Integer, MonsterCard> cards = firstPlayersBoard.getMonstersPlace();
        for (Map.Entry<Integer, MonsterCard> entry : cards.entrySet()) {
            if (entry.getValue() == null) continue;
            Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(entry.getValue().getName()) + ".jpg").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(67.2);
            imageView.setFitHeight(98);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    firstPlayersBoard.deselectAll();
                    secondPlayersBoard.deselectAll();
                    firstPlayersBoard.setMonsterSelectedCard(entry.getValue());
                    firstPlayersBoard.setSelectedMonsterPlace(entry.getKey());
                }
            });
            hBox.getChildren().add(imageView);
        }
        Pane pane = new Pane();
        pane.setLayoutX(288.0);
        pane.setLayoutY(309);
        pane.setPrefHeight(88);
        pane.setPrefWidth(339);
        pane.getChildren().add(hBox);
        previousFirstPlayerMonsterPane = pane;
        return pane;
    }



    private Pane loadFirstPlayersSpellTrapCards() {
        if (mainPane.getChildren().contains(previousFirstPlayerSpellTrapPane))
            mainPane.getChildren().remove(previousFirstPlayerSpellTrapPane);
        HBox hBox = new HBox();
        HashMap<Integer, SpellTrapCard> cards = firstPlayersBoard.getSpellTrapsPlace();
        for (Map.Entry<Integer, SpellTrapCard> entry : cards.entrySet()) {
            if (entry.getValue() == null) continue;
            Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(entry.getValue().getName()) + ".jpg").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(67.2);
            imageView.setFitHeight(98);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    firstPlayersBoard.deselectAll();
                    secondPlayersBoard.deselectAll();
                    firstPlayersBoard.setSpellTrapSelectedCard(entry.getValue());
                    firstPlayersBoard.setSelectedMonsterPlace(entry.getKey());
                }
            });
            hBox.getChildren().add(imageView);
        }
        Pane pane = new Pane();
        pane.setLayoutX(288.0);
        pane.setLayoutY(397);
        pane.setPrefHeight(88);
        pane.setPrefWidth(339);
        pane.getChildren().add(hBox);
        previousFirstPlayerSpellTrapPane = pane;
        return pane;
    }



    public Pane loadFirstPlayersFieldZoneCard() {
        if (firstPlayersBoard.getFieldZone() == null) return null;
        if (mainPane.getChildren().contains(previousFirstPlayerFieldZonePane))
            mainPane.getChildren().remove(previousFirstPlayerFieldZonePane);
        HBox hBox = new HBox();
        Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(firstPlayersBoard.getFieldZone().getName()) + ".jpg").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(67.2);
        imageView.setFitHeight(98);
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                firstPlayersBoard.deselectAll();
                secondPlayersBoard.deselectAll();
                firstPlayersBoard.setFieldZoneSelectedCard(firstPlayersBoard.getFieldZone());
            }
        });
        Pane pane = new Pane();
        pane.setLayoutX(635);
        pane.setLayoutY(333);
        pane.setPrefHeight(88);
        pane.setPrefWidth(60);
        pane.getChildren().add(hBox);
        previousFirstPlayerFieldZonePane = pane;
        return pane;
    }



    public void loadFirstPlayersGraveyard(Pane pane) {
        for (int i = 0 ; i < firstPlayersBoard.getGraveYard().size() ; i++) {
            Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(firstPlayersBoard.getGraveYard().get(i).getName()) + ".jpg").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(67.2);
            imageView.setFitHeight(98);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                }
            });
            pane.getChildren().add(0 ,imageView);
        }
    }



    public void loadFirstPlayersCardsInHand(ScrollPane scrollPane) {
        HBox hBox = new HBox();
        for (int i = 0 ; i < firstPlayersBoard.getCardsInHand().size() ; i++) {
            Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(firstPlayersBoard.getCardsInHand().get(i).getName()) + ".jpg").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(67.2);
            imageView.setFitHeight(98);
            int finalI = i;
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    firstPlayersBoard.deselectAll();
                    secondPlayersBoard.deselectAll();
                    firstPlayersBoard.setHandSelectedCard(firstPlayersBoard.getCardsInHand().get(finalI));
                    firstPlayersBoard.setSelectedHandPlace(finalI + 1);
                }
            });
            hBox.getChildren().add(imageView);
        }
        scrollPane.setContent(hBox);
    }



    public Pane loadSecondPlayersMonsterCards() {
        if (mainPane.getChildren().contains(previousSecondPlayerMonsterPane))
            mainPane.getChildren().remove(previousSecondPlayerMonsterPane);
        HBox hBox = new HBox();
        HashMap<Integer, MonsterCard> cards = secondPlayersBoard.getMonstersPlace();
        for (Map.Entry<Integer, MonsterCard> entry : cards.entrySet()) {
            if (entry.getValue() == null) continue;
            Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(entry.getValue().getName()) + ".jpg").toExternalForm());
            if (entry.getValue().isSet()) image = new Image(getClass().getResource("/Assets/4009.png").toExternalForm());
            if (entry.getValue().isDefensive()) image = new Image(getClass().getResource("/Assets/4014.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(67.2);
            imageView.setFitHeight(98);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    firstPlayersBoard.deselectAll();
                    secondPlayersBoard.deselectAll();
                    secondPlayersBoard.setMonsterSelectedCard(entry.getValue());
                    secondPlayersBoard.setSelectedMonsterPlace(entry.getKey());
                }
            });
            hBox.getChildren().add(imageView);
        }
        Pane pane = new Pane();
        pane.setLayoutX(288.0);
        pane.setLayoutY(201);
        pane.setPrefHeight(88);
        pane.setPrefWidth(339);
        pane.getChildren().add(hBox);
        previousSecondPlayerMonsterPane = pane;
        return pane;
    }




    private Pane loadSecondPlayersSpellTrapCards() {
        if (mainPane.getChildren().contains(previousSecondPlayerSpellTrapPane))
            mainPane.getChildren().remove(previousSecondPlayerSpellTrapPane);
        HBox hBox = new HBox();
        HashMap<Integer, SpellTrapCard> cards = secondPlayersBoard.getSpellTrapsPlace();
        for (Map.Entry<Integer, SpellTrapCard> entry : cards.entrySet()) {
            if (entry.getValue() == null) continue;
            Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(entry.getValue().getName()) + ".jpg").toExternalForm());
            if (entry.getValue().isSet()) image = new Image(getClass().getResource("/Assets/4010.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(67.2);
            imageView.setFitHeight(98);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    firstPlayersBoard.deselectAll();
                    secondPlayersBoard.deselectAll();
                    secondPlayersBoard.setSpellTrapSelectedCard(entry.getValue());
                    secondPlayersBoard.setSelectedMonsterPlace(entry.getKey());
                }
            });
            hBox.getChildren().add(imageView);
        }
        Pane pane = new Pane();
        pane.setLayoutX(288.0);
        pane.setLayoutY(113);
        pane.setPrefHeight(88);
        pane.setPrefWidth(339);
        pane.getChildren().add(hBox);
        previousSecondPlayerSpellTrapPane = pane;
        return pane;
    }




    public Pane loadSecondPlayersFieldZoneCard() {
        if (secondPlayersBoard.getFieldZone() == null) return null;
        if (mainPane.getChildren().contains(previousSecondPlayerFieldZonePane))
            mainPane.getChildren().remove(previousSecondPlayerFieldZonePane);
        HBox hBox = new HBox();
        Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(secondPlayersBoard.getFieldZone().getName()) + ".jpg").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(67.2);
        imageView.setFitHeight(98);
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                firstPlayersBoard.deselectAll();
                secondPlayersBoard.deselectAll();
                secondPlayersBoard.setFieldZoneSelectedCard(secondPlayersBoard.getFieldZone());
            }
        });
        Pane pane = new Pane();
        pane.setLayoutX(219);
        pane.setLayoutY(172);
        pane.setPrefHeight(88);
        pane.setPrefWidth(60);
        pane.getChildren().add(hBox);
        previousSecondPlayerFieldZonePane = pane;
        return pane;
    }




    public void loadSecondPlayersGraveyard(Pane pane) {
        for (int i = 0 ; i < secondPlayersBoard.getGraveYard().size() ; i++) {
            Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(secondPlayersBoard.getGraveYard().get(i).getName()) + ".jpg").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(67.2);
            imageView.setFitHeight(98);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                }
            });
            pane.getChildren().add(0 ,imageView);
        }
    }




    public void loadSecondPlayersCardsInHand(ScrollPane scrollPane) {
        HBox hBox = new HBox();
        for (int i = 0 ; i < secondPlayersBoard.getCardsInHand().size() ; i++) {
            Image image = new Image(getClass().getResource("/Assets/4009.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(67.2);
            imageView.setFitHeight(98);
            int finalI = i;
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    firstPlayersBoard.deselectAll();
                    secondPlayersBoard.deselectAll();
                    secondPlayersBoard.setHandSelectedCard(secondPlayersBoard.getCardsInHand().get(finalI));
                    secondPlayersBoard.setSelectedHandPlace(finalI + 1);
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

    public void changePhase() throws Exception {
        DuelController.getInstance().changePhase(firstPlayersBoard, secondPlayersBoard,
                currentPhase);
        phaseChangeToDo();
        System.out.println(currentPhase);
        initialize();
    }

    public void phaseChangeToDo() throws Exception {
        if (currentPhase.equals(GamePhases.DRAW)) {
            if (!FirstTime && !CardAddedToHandInThisPhase && firstPlayersBoard.getCardsInHand().size() < 6) {
                System.out.println("new card added to hand: " +
                        DuelController.getInstance().addOneCardToHand(firstPlayersBoard));
                CardAddedToHandInThisPhase = true;
            }
            setSummonedOrSetInThisPhase(false);
        } else if (currentPhase.equals(GamePhases.END)) {
            DuelController.getInstance().changePositionReset(firstPlayersBoard);
            firstPlayersBoard.setTrapEffect(false);
            swapPlayers();
            FirstTime = false;
            CardAddedToHandInThisPhase = false;
            secondPlayersBoard.setTrapEffect(false);
            initialize();
        }
    }

    public void settingPopup(MouseEvent mouseEvent) throws IOException {
        Stage popup = new Stage();
        SettingView.rounds = rounds;
        SettingView.popup = popup;
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/SettingPopup.fxml"));
        Scene scene = new Scene(pane);
        popup.setScene(scene);

        popup.initModality(Modality.APPLICATION_MODAL);
//        popup.showAndWait();
        popup.show();
    }

    public void summon() {
        try{
            DuelController.getInstance().summonMonsterCard(firstPlayersBoard, secondPlayersBoard,
                    currentPhase, SummonedOrSetInThisPhase);
            initialize();
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }




    public void set() {
        try {
            DuelController.getInstance().generalCardSet(firstPlayersBoard, secondPlayersBoard,
                    currentPhase, SummonedOrSetInThisPhase);
            initialize();
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }




    public void directAttack() {
        try {
            DuelController.getInstance().directAttack(firstPlayersBoard, secondPlayersBoard,
                    currentPhase, FirstTime);
            initialize();
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    public void attackToCard() {
        try {
            int place = Integer.parseInt(cardNumber.getText());
            if (DuelController.getInstance().canAttackToCard(firstPlayersBoard, secondPlayersBoard, currentPhase, place))
                System.out.println(DuelController.getInstance().attackToCard(firstPlayersBoard, secondPlayersBoard, place));
            initialize();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }




    public void changePosition(MouseEvent mouseEvent) {
        try {
            DuelController.getInstance().changePosition(firstPlayersBoard, secondPlayersBoard,
                    currentPhase);
            initialize();
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }



    public void yourGraveyard(MouseEvent mouseEvent) {
        Stage stage = new Stage();
        stage.setHeight(270);
        stage.setWidth(185);
        ScrollPane scrollPane = new ScrollPane();
        HBox hBox = new HBox();
        for (int i = 0 ; i < firstPlayersBoard.getGraveYard().size() ; i++) {
            Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(firstPlayersBoard.getGraveYard().get(i).getName()) + ".jpg").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(185);
            imageView.setFitHeight(270);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                }
            });
            hBox.getChildren().add(imageView);
        }
        scrollPane.setContent(hBox);
        stage.setScene(new Scene(scrollPane));
        stage.show();
    }




    public void opponentGraveyard(MouseEvent mouseEvent) {
        Stage stage = new Stage();
        stage.setHeight(270);
        stage.setWidth(185);
        ScrollPane scrollPane = new ScrollPane();
        HBox hBox = new HBox();
        for (int i = 0 ; i < secondPlayersBoard.getGraveYard().size() ; i++) {
            Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(secondPlayersBoard.getGraveYard().get(i).getName()) + ".jpg").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(185);
            imageView.setFitHeight(270);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                }
            });
            hBox.getChildren().add(imageView);
        }
        scrollPane.setContent(hBox);
        stage.setScene(new Scene(scrollPane));
        stage.show();
    }




    public void swapPlayers() throws Exception {
        GameBoard temp = firstPlayersBoard;
        firstPlayersBoard = secondPlayersBoard;
        secondPlayersBoard = temp;
        DuelController.getInstance().checkFieldZone(this.firstPlayersBoard ,this.secondPlayersBoard);
        initialize();
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
