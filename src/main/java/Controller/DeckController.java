package Controller;

import Model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DeckController {
    private static DeckController instance = null;
    private final static String DECKS_FILE_PATH = "C:\\Users\\Vision\\IdeaProjects\\Game First Phase\\src\\main\\java\\Database\\Decks";
    private final static String MONSTER_CARDS_FILE_PATH = "C:\\Users\\Vision\\IdeaProjects\\Game First Phase\\src\\main\\java\\Database\\Cards\\Monsters";
    private final static String SPELL_TRAP_CARDS_FILE_PATH = "C:\\Users\\Vision\\IdeaProjects\\Game First Phase\\src\\main\\java\\Database\\Cards\\SpellTraps";
    private final static String USERS_FILE_PATH = "C:\\Users\\Vision\\IdeaProjects\\Game First Phase\\src\\main\\java\\Database\\Users";
    private static FileWriter FILE_WRITER;

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

    public void createDeck(String username, String deckName) {
        if (deckNameCheck(deckName))
            System.out.println("deck with name " + deckName + " already exists");
        else {
            try {
                Deck deck = new Deck(deckName, username);
                Gson gson = new GsonBuilder().create();
                FILE_WRITER = new FileWriter(DECKS_FILE_PATH + "\\" + deckName + ".json");
                gson.toJson(deck, FILE_WRITER);
                FILE_WRITER.close();
                User user = User.getUserByUsername(username);
                user.addDeckToList(deckName);
                FILE_WRITER = new FileWriter(USERS_FILE_PATH + "\\" + username + ".json");
                gson.toJson(user, FILE_WRITER);
                FILE_WRITER.close();
                System.out.println("deck created successfully!");
            } catch (IOException e) {
                System.out.println("Can not create new Deck\n" +
                        "Please try again!");
            }
        }
    }

    public boolean deckNameCheck(String deckName) {
        File directory = new File(DECKS_FILE_PATH);
        File[] allDecks = directory.listFiles();
        if (allDecks != null) {
            for (File deck : allDecks) {
                if (deck.getName().equals(deckName + ".json"))
                    return true;
            }
            return false;
        }
        return false;
    }

    public void deleteDeck(String username, String deckName) {
        if (!deckNameCheck(deckName))
            System.out.println("deck with name " + deckName + " does not exists");
        else {
            File directory = new File(DECKS_FILE_PATH);
            File[] allDecks = directory.listFiles();
            if (allDecks != null) {
                for (int i = 0; i < allDecks.length; i++) {
                    if (allDecks[i].getName().equals(deckName + ".json")) {
                        if (allDecks[i].delete()) {
                            try {
                                User user = User.getUserByUsername(username);
                                user.removeDeckFromList(deckName);
                                FILE_WRITER = new FileWriter(USERS_FILE_PATH + "\\" + username + ".json");
                                new Gson().toJson(user, FILE_WRITER);
                                FILE_WRITER.close();
                                System.out.println("deck deleted successfully");
                                break;
                            } catch (Exception e) {
                                System.out.println("Can not delete deck!");
                            }
                        } else
                            System.out.println("Field to delete deck!");
                    }
                }
            }
        }
    }

    public void setActiveDeck(String username, String deckName) {
        if (!deckNameCheck(deckName)) {
            System.out.println("deck with name " + deckName + " does not exist");
        } else {
            try {
                User user = User.getUserByUsername(username);
                user.setActiveDeck(deckName);
                FILE_WRITER = new FileWriter(USERS_FILE_PATH + "\\" + username + ".json");
                new Gson().toJson(user, FILE_WRITER);
                FILE_WRITER.close();
                System.out.println("deck activated successfully");
            } catch (Exception e) {
                System.out.println("Can not do activation process!");
            }
        }

    }


    public void addCardToDeck(String username, String deckName, String cardName, boolean isSide) {
        User user = User.getUserByUsername(username);
        if (!user.doesUserHasThisCard(cardName)) {
            System.out.println("card with name " + cardName + " does not exist");
        } else {
            if (!deckNameCheck(deckName)) {
                System.out.println("deck with name " + deckName + " does not exist");
            } else {
                try {
                    FileReader fileReader = new FileReader(DECKS_FILE_PATH + "\\" + deckName + ".json");
                    Deck deck = new GsonBuilder().create().fromJson(fileReader, Deck.class);
                    int mainDeckAmount = deck.mainDeckSize();
                    int sideDeckAmount = deck.sideDeckSize();
                    if (isSide) {
                        if (sideDeckAmount == 15)
                            System.out.println("side deck is full");
                        else {
                            int cardCount = deck.mainDecksCardCount(cardName) + deck.sideDecksCardCount(cardName);
                            if (cardCount == 3)
                                System.out.println("there are already three cards with name " + cardName + " in deck " + deckName);
                            else {
                                user.decreaseCard(cardName);
                                deck.addCardToSideDeck(cardName);
                                rewriteData(username, deckName, user, deck);
                                System.out.println("card added to deck successfully");
                            }
                        }
                    } else {
                        if (mainDeckAmount == 60)
                            System.out.println("main deck is full");
                        else {
                            int cardCount = deck.mainDecksCardCount(cardName) + deck.sideDecksCardCount(cardName);
                            if (cardCount == 3)
                                System.out.println("there are already three cards with name " + cardName + " in deck " + deckName);
                            else {
                                user.decreaseCard(cardName);
                                deck.addCardToMainDeck(cardName);
                                rewriteData(username, deckName, user, deck);
                                System.out.println("card added to deck successfully");
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("An error occurred while adding Card to Deck!");
                }
            }
        }
    }

    private void rewriteData(String username, String deckName, User user, Deck deck) {
        try {
            FILE_WRITER = new FileWriter(USERS_FILE_PATH + "\\" + username + ".json");
            new Gson().toJson(user, FILE_WRITER);
            FILE_WRITER.close();
            FILE_WRITER = new FileWriter(DECKS_FILE_PATH + "\\" + deckName + ".json");
            new Gson().toJson(deck, FILE_WRITER);
            FILE_WRITER.close();
        } catch (IOException e) {
            System.out.println("Can not rewrite data on files!");
        }
    }

    public void removeCardFromDeck(String username, String deckName, String cardName, boolean isSide) {
        User user = User.getUserByUsername(username);
        if (!deckNameCheck(deckName)) {
            System.out.println("deck with name " + deckName + " does not exist");
        } else {
            try {
                Gson gson = new GsonBuilder().create();
                FileReader fileReader = new FileReader(DECKS_FILE_PATH + "\\" + deckName + ".json");
                Deck deck = gson.fromJson(fileReader, Deck.class);
                if (!isSide) {
                    if (!deck.doesMainDeckHasThisCard(cardName))
                        System.out.println("card with name " + cardName + " does not exist in main deck");
                    else {
                        user.increaseCard(cardName);
                        deck.removeCardFromMainDeck(cardName);
                        rewriteData(username, deckName, user, deck);
                        System.out.println("card removed form deck successfully");
                    }
                } else {
                    if (!deck.doesSideDeckHasThisCard(cardName))
                        System.out.println("card with name " + cardName + " does not exist in side deck");
                    else {
                        user.increaseCard(cardName);
                        deck.removeCardFromSideDeck(cardName);
                        rewriteData(username, deckName, user, deck);
                        System.out.println("card removed form deck successfully");
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while deleting Card from Deck!");
            }
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
        try {
            FileReader fileReader = new FileReader(DECKS_FILE_PATH + "\\" + deckName + ".json");
            Gson gson = new GsonBuilder().create();
            Deck deck = gson.fromJson(fileReader, Deck.class);
            boolean isValid = deck.isValid();
            if (!deckName.equals(User.getUserByUsername(Deck.getUsernameByDeckName(deckName)).getActiveDeck()) || flag == 1)
                System.out.println(deck.getName() + ": main deck " + deck.mainDeckSize() +
                        ",side deck " + deck.sideDeckSize() + (isValid ? ", valid" : ", invalid"));
        } catch (IOException e) {
            System.out.println("An error occurred while printing Deck!\n" +
                    "Deck did not found");
        }
    }

    public void showDeck(String username, String deckName, boolean isSide) {
        if (!deckNameCheck(deckName))
            System.out.println("deck with name " + deckName + " does not exists");
        else {
            try {
                FileReader fileReader = new FileReader(DECKS_FILE_PATH + "\\" + deckName + ".json");
                Gson gson = new GsonBuilder().create();
                Deck deck = gson.fromJson(fileReader, Deck.class);
                System.out.println("Deck name: " + deckName);
                if (isSide) {
                    System.out.println("Side Deck:");
                    printDeckCards(deck.getSideDeckCards());
                } else {
                    System.out.println("Main Deck:");
                    printDeckCards(deck.getMainDeckCards());
                }
            } catch (IOException e) {
                System.out.println("An error occurred while showing Deck!");
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
