package Controller;

public class DeckController {
    private static DeckController instance = null;

    private DeckController(){
    }

    public void deleteDeck(String username, String deckName){}

    public void createDeck(String username, String deckName){}

    public void setActiveDeck(String username, String deckName){}

    public void addCardToDeck(String username, String deckName, String cardName, boolean isSide){}

    public void removeCardFromDeck(String username, String deckName, String cardName, boolean isSide){}

    public void showAllDecks(String username){}

    public void showDeck(String deckName, boolean isSide){}

    public void showUserCards(String username){}







    public static DeckController getInstance(){
        if(instance == null)
            instance = new DeckController();
        return instance;
    }

}
