package Server.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Deck {
    private static ArrayList<Deck> allDecks;
    private String name;
    private String username;
    private HashMap<String, Integer> mainDeckCards;
    private HashMap<String, Integer> sideDeckCards;

    static {
        allDecks = new ArrayList<>();
    }

    public Deck(String name, String username) {
        this.name = name;
        this.username = username;
        mainDeckCards = new HashMap<>();
        sideDeckCards = new HashMap<>();
        if (!allDecks.contains(this))
            allDecks.add(this);
    }

    public static ArrayList<Deck> getAllDecks(){
        return allDecks;
    }

    public static void addDeckToList(Deck deck) {
        allDecks.add(deck);
    }

    public static Deck getDeckByName (String deckName){
        for(Deck deck : allDecks){
            if(deck.getName().equals(deckName))
                return deck;
        }
        return null;
    }

    public static String getUsernameByDeckName(String deckName) {
        for (Deck deck : allDecks) {
            if (deck.getName().equals(deckName))
                return deck.getUsername();
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int sideDeckSize() {
        int size = 0;
        for (Map.Entry<String, Integer> entry : sideDeckCards.entrySet())
            size += entry.getValue();
        return size;
    }

    public int mainDeckSize() {
        int size = 0;
        for (Map.Entry<String, Integer> entry : mainDeckCards.entrySet())
            size += entry.getValue();
        return size;
    }

    public int mainDecksCardCount(String cardName) {
        if (mainDeckCards.containsKey(cardName))
            return mainDeckCards.get(cardName);
        return 0;
    }

    public int sideDecksCardCount(String cardName) {
        if (sideDeckCards.containsKey(cardName))
            return sideDeckCards.get(cardName);
        return 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isValid() {
        return (sideDeckSize() >= 0 && sideDeckSize() <= 15) &&
                (mainDeckSize() >= 40 && mainDeckSize() <= 60);
    }

    public void addCardToMainDeck(String cardName) {
        if (mainDeckCards.containsKey(cardName))
            mainDeckCards.put(cardName, mainDeckCards.get(cardName) + 1);
        else
            mainDeckCards.put(cardName, 1);
    }

    public void addCardToSideDeck(String cardName) {
        if (sideDeckCards.containsKey(cardName))
            sideDeckCards.put(cardName, sideDeckCards.get(cardName) + 1);
        else
            sideDeckCards.put(cardName, 1);
    }

    public boolean doesMainDeckHasThisCard(String cardName) {
        return mainDeckCards.containsKey(cardName);
    }

    public boolean doesSideDeckHasThisCard(String cardName) {
        return sideDeckCards.containsKey(cardName);
    }


    public void removeCardFromMainDeck(String cardName) {
        mainDeckCards.put(cardName, mainDeckCards.get(cardName) - 1);
        if (mainDeckCards.get(cardName) == 0)
            mainDeckCards.remove(cardName);
    }

    public void removeCardFromSideDeck(String cardName) {
        sideDeckCards.put(cardName, sideDeckCards.get(cardName) - 1);
        if (sideDeckCards.get(cardName) == 0)
            sideDeckCards.remove(cardName);
    }

    public HashMap<String, Integer> getSideDeckCards() {
        return this.sideDeckCards;
    }

    public HashMap<String, Integer> getMainDeckCards() {
        return this.mainDeckCards;
    }


}
