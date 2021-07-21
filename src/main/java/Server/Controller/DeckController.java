package Server.Controller;

import SceneController.MainView;
import Server.Model.*;
import SceneController.DeckControlView;
import com.google.gson.Gson;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DeckController {
    private static DeckController instance = null;
    private final static String DECKS_FILE_PATH = "src\\main\\java\\Database\\Decks";

    private DeckController() {
    }

    public static void parseDecks() {
        Gson gson = new Gson();
        try {
            File directory = new File(DECKS_FILE_PATH);
            File[] allDecks = directory.listFiles();
            if (allDecks != null) {
                for (File deckFile : allDecks) {
                    FileReader fileReader = new FileReader(DECKS_FILE_PATH + "\\" + deckFile.getName());
                    Deck deck = gson.fromJson(fileReader, Deck.class);
                    Deck.addDeckToList(deck);
                    fileReader.close();
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while parsing Cards!");
        }
    }

    public static void rewriteData() {
        Gson gson = new Gson();
        for (Deck deck : Deck.getAllDecks()) {
            try {
                FileWriter FILE_WRITER = new FileWriter(DECKS_FILE_PATH + "\\" + deck.getName() + ".json");
                gson.toJson(deck, FILE_WRITER);
                FILE_WRITER.close();
            } catch (IOException e) {
                System.out.println("An error occurred while writing deck data");
            }
        }
    }

    public void createDeck(String username, String deckName) throws Exception {
        if (Deck.getDeckByName(deckName) != null)
            throw new Exception("deck with name " + deckName + " already exists");
        else {
            Deck deck = new Deck(deckName, username);
            User user = User.getUserByUsername(username);
            user.addDeckToList(deck.getName());
        }
    }

    public void deleteDeck(String username, String deckName) throws Exception {
        if (Deck.getDeckByName(deckName) == null)
            throw new Exception("deck with name " + deckName + " does not exists");
        else {
            File directory = new File(DECKS_FILE_PATH);
            File[] allDecks = directory.listFiles();
            if (allDecks != null) {
                for (File deck : allDecks) {
                    if (deck.getName().equals(deckName + ".json")) {
                        if (deck.delete()) {
                            User user = User.getUserByUsername(username);
                            user.removeDeckFromList(deckName);
                            break;
                        } else
                            throw new Exception("Failed to delete deck!");
                    }
                }
            }
        }
    }

    public void setActiveDeck(String username, String deckName) throws Exception {
        if (Deck.getDeckByName(deckName) == null) {
            throw new Exception("deck with name " + deckName + " does not exist");
        } else {
            User user = User.getUserByUsername(username);
            user.setActiveDeck(deckName);
        }
    }


    public void addCardToDeck(String username, String deckName, String cardName, boolean isSide) throws Exception {
        User user = User.getUserByUsername(username);
        if (!user.doesUserHasThisCard(cardName))
            throw new Exception("card with name " + cardName + " does not exist");
        else {
            if (Deck.getDeckByName(deckName) == null)
                throw new Exception("deck with name " + cardName + " does not exist");
            else {
                Deck deck = Deck.getDeckByName(deckName);
                int mainDeckAmount = deck.mainDeckSize();
                int sideDeckAmount = deck.sideDeckSize();
                if (isSide)
                    addCardToSideDeck(deckName, cardName, user, deck, sideDeckAmount);
                else
                    addCardToMainDeck(deckName, cardName, user, deck, mainDeckAmount);
            }
        }
    }

    private void addCardToMainDeck(String deckName, String cardName, User user, Deck deck, int mainDeckAmount) throws Exception {
        if (mainDeckAmount == 60)
            throw new Exception("main deck is full");
        else {
            int cardCount = deck.mainDecksCardCount(cardName) + deck.sideDecksCardCount(cardName);
            if (cardCount == 3)
                throw new Exception("there are already three cards with name " + cardName + " in deck " + deckName);
            else {
                user.decreaseCard(cardName);
                deck.addCardToMainDeck(cardName);
            }
        }
    }

    private void addCardToSideDeck(String deckName, String cardName, User user, Deck deck, int sideDeckAmount) throws Exception {
        if (sideDeckAmount == 15)
            throw new Exception("side deck is full");
        else {
            int cardCount = deck.mainDecksCardCount(cardName) + deck.sideDecksCardCount(cardName);
            if (cardCount == 3)
                throw new Exception("there are already three cards with name " + cardName + " in deck " + deckName);
            else {
                user.decreaseCard(cardName);
                deck.addCardToSideDeck(cardName);
            }
        }
    }

    public void removeCardFromDeck(String username, String deckName, String cardName, boolean isSide) throws Exception {
        User user = User.getUserByUsername(username);
        if (Deck.getDeckByName(deckName) == null) {
            throw new Exception("deck with name " + deckName + " does not exist");
        } else {
            Deck deck = Deck.getDeckByName(deckName);
            if (!isSide)
                deleteCardFromMainDeck(cardName, user, deck);
            else
                deleteCardFromSideDeck(cardName, user, deck);
        }
    }

    private void deleteCardFromSideDeck(String cardName, User user, Deck deck) throws Exception {
        if (!deck.doesSideDeckHasThisCard(cardName))
            throw new Exception("card with name " + cardName + " does not exist in side deck");
        else {
            user.increaseCard(cardName);
            deck.removeCardFromSideDeck(cardName);
        }
    }

    private void deleteCardFromMainDeck(String cardName, User user, Deck deck) throws Exception {
        if (!deck.doesMainDeckHasThisCard(cardName))
            throw new Exception("card with name " + cardName + " does not exist in main deck");
        else {
            user.increaseCard(cardName);
            deck.removeCardFromMainDeck(cardName);
        }
    }

    public String showAllDecks(String username) {
        StringBuilder result = new StringBuilder();
        User user = User.getUserByUsername(username);
        ArrayList<String> usersAllDecks = user.getUserDecks();
        String activeDeck = user.getActiveDeck();
        usersAllDecks.sort(Comparator.naturalOrder());
        result.append("Decks:\nActive Deck:\n");
        if (activeDeck != null) {
            result.append(printDeck(activeDeck));
            usersAllDecks.remove(activeDeck);
        }
        result.append("Other Decks:\n");
        for (String deckName : usersAllDecks)
            result.append(printDeck(deckName));
        return result.toString();
    }

    public void showAllDecks(User user, VBox vBox){
        ArrayList<String> usersAllDecks = user.getUserDecks();
        usersAllDecks.sort(Comparator.naturalOrder());
        Font font = new Font("Book Antiqua", 24);
        for (String deckName : usersAllDecks){
            Text text = new Text(printDeck(deckName));
            text.setFill(Color.BLACK);
            text.setFont(font);
            vBox.getChildren().add(text);
            vBox.setAlignment(Pos.TOP_CENTER);
        }
    }

    public String printDeck(String deckName) {
        Deck deck = Deck.getDeckByName(deckName);
        boolean isValid = deck.isValid();
        return deck.getName() + ": main deck " + deck.mainDeckSize() +
                ",side deck " + deck.sideDeckSize() + (isValid ? ", valid" : ", invalid") + "\n";
    }

    public String showDeck(String deckName, boolean isSide) throws Exception{
        if (Deck.getDeckByName(deckName) == null)
            throw new Exception("deck with name " + deckName + " does not exists");
        else {
            String result = "";
            Deck deck = Deck.getDeckByName(deckName);
            result += ("Deck name: " + deckName + "\n");
            if (isSide) {
                result += "Side Deck:\n";
                result += printDeckCards(deck.getSideDeckCards());
            } else {
                result += "Main Deck:\n";
                result += printDeckCards(deck.getMainDeckCards());
            }
            return result;
        }
    }

    public String printDeckCards(HashMap<String, Integer> deck) {
        StringBuilder result = new StringBuilder();
        TreeMap<String, Integer> sortedDeck = new TreeMap<>(deck);
        result.append("Monsters:\n");
        for (Map.Entry<String, Integer> entry : sortedDeck.entrySet()) {
            if (MonsterCard.isMonsterCard(entry.getKey())) {
                MonsterCard monsterCard = MonsterCard.getMonsterCardByName(entry.getKey());
                result.append(monsterCard).append("\n");
            }
        }
        result.append("Spell and Traps:\n");
        for (Map.Entry<String, Integer> entry : sortedDeck.entrySet()) {
            if (!MonsterCard.isMonsterCard(entry.getKey())) {
                SpellTrapCard spellTrapCard = SpellTrapCard.getSpellTrapCardByName(entry.getKey());
                result.append(spellTrapCard).append("\n");
            }
        }
        return result.toString();
    }

    public String showUserCards(String username) {
        StringBuilder result = new StringBuilder();
        TreeMap<String, Integer> sortedCards = new TreeMap<>(User.getUserByUsername(username).getUserAllCards());
        for (Map.Entry<String, Integer> entry : sortedCards.entrySet())
            result.append(Card.getCardByName(entry.getKey()).toString()).append("\n");
        return result.toString();
    }


    public static DeckController getInstance() {
        if (instance == null)
            instance = new DeckController();
        return instance;
    }

    public void loadCards(ScrollPane deckBox, Deck deck, boolean isSide,
                          Button addToMainDeck, Button addToSideDeck, Button removeFromDeck) {
        HashMap<String, Integer> cards;
        if(!isSide)
            cards = deck.getMainDeckCards();
        else
            cards = deck.getSideDeckCards();
        HBox hBox = new HBox();
        for(Map.Entry<String, Integer> entry : cards.entrySet()){
            for (int i = 0; i < entry.getValue(); i++) {
                Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(entry.getKey()) + ".jpg").toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(67.2);
                imageView.setFitHeight(98);
                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        DeckControlView.selectedCard = entry.getKey();
                        DeckControlView.isSide = isSide;
                        removeFromDeck.setDisable(false);
                        addToMainDeck.setDisable(true);
                        addToSideDeck.setDisable(true);
                    }
                });
                hBox.getChildren().add(imageView);
            }
        }
        deckBox.setContent(hBox);
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

    public void loadYourCards(ScrollPane yourCards,
                              Button removeFromDeck, Button addToMainDeck, Button addToSideDeck) {
        HBox hBox = new HBox();
        HashMap<String, Integer> cards = MainView.loggedInUser.getUserAllCards();
        for(Map.Entry<String, Integer> entry : cards.entrySet()){
            for (int i = 0; i < entry.getValue(); i++) {
                Image image = new Image(getClass().getResource("/Assets/" + toCamelCase(entry.getKey()) + ".jpg").toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(67.2);
                imageView.setFitHeight(98);
                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        DeckControlView.selectedCard = entry.getKey();
                        addToMainDeck.setDisable(false);
                        addToSideDeck.setDisable(false);
                        removeFromDeck.setDisable(true);
                    }
                });
                hBox.getChildren().add(imageView);
            }
        }
        yourCards.setContent(hBox);
    }
}
