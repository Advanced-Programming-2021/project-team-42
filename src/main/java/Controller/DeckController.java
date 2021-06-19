package Controller;

import Model.*;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DeckController {
    private static DeckController instance = null;
    private final static String DECKS_FILE_PATH = "C:\\Users\\Vision\\IdeaProjects\\Game First Phase\\src\\main\\java\\Database\\Decks";

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


    public void addCardToDeck(String username, String deckName, String cardName, boolean isSide) throws Exception{
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

    private void deleteCardFromSideDeck(String cardName, User user, Deck deck) throws Exception{
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

    public void showAllDecks(String username) {
        User user = User.getUserByUsername(username);
        ArrayList<String> usersAllDecks = user.getUserDecks();
        String activeDeck = user.getActiveDeck();
        usersAllDecks.sort(Comparator.naturalOrder());
        System.out.println("Decks:");
        System.out.println("Active Deck:");
        if (activeDeck != null)
            printDeck(activeDeck, 1);
        System.out.println("Other Decks:");
        for (String deckName : usersAllDecks)
            printDeck(deckName, 0);
    }

    public void printDeck(String deckName, int flag) {
        Deck deck = Deck.getDeckByName(deckName);
        boolean isValid = deck.isValid();
        if (!deckName.equals(User.getUserByUsername(Deck.getUsernameByDeckName(deckName)).getActiveDeck()) || flag == 1)
            System.out.println(deck.getName() + ": main deck " + deck.mainDeckSize() +
                    ",side deck " + deck.sideDeckSize() + (isValid ? ", valid" : ", invalid"));


    }

    public void showDeck(String deckName, boolean isSide) {
        if (Deck.getDeckByName(deckName) == null)
            System.out.println("deck with name " + deckName + " does not exists");
        else {
            Deck deck = Deck.getDeckByName(deckName);
            System.out.println("Deck name: " + deckName);
            if (isSide) {
                System.out.println("Side Deck:");
                printDeckCards(deck.getSideDeckCards());
            } else {
                System.out.println("Main Deck:");
                printDeckCards(deck.getMainDeckCards());
            }
        }
    }

    public void printDeckCards(HashMap<String, Integer> deck) {
        TreeMap<String, Integer> sortedDeck = new TreeMap<>(deck);
        System.out.println("Monsters:");
        for (Map.Entry<String, Integer> entry : sortedDeck.entrySet()) {
            if (MonsterCard.isMonsterCard(entry.getKey())) {
                MonsterCard monsterCard = MonsterCard.getMonsterCardByName(entry.getKey());
                System.out.println(monsterCard);
            }
        }
        System.out.println("Spell and Traps:");
        for (Map.Entry<String, Integer> entry : sortedDeck.entrySet()) {
            if (!MonsterCard.isMonsterCard(entry.getKey())) {
                SpellTrapCard spellTrapCard = SpellTrapCard.getSpellTrapCardByName(entry.getKey());
                System.out.println(spellTrapCard);
            }
        }
    }

    public void showUserCards(String username) {
        TreeMap<String, Integer> sortedCards = new TreeMap<>(User.getUserByUsername(username).getUserAllCards());
        for (Map.Entry<String, Integer> entry : sortedCards.entrySet())
            System.out.println(Card.getCardByName(entry.getKey()));
    }


    public static DeckController getInstance() {
        if (instance == null)
            instance = new DeckController();
        return instance;
    }

}
