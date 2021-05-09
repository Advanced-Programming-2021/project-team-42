package Controller;

public class CardController {
    private static CardController instance = null;

    private CardController(){

    }

    public void createDeck(String userName, String deckName){

    }

    public void deleteDeck(String userName, String deckName){

    }

    public void setActiveDeck(String userName, String deckName){

    }

    public void addCardToDeck(String userName, String deckName, String cardName){

    }

    public void removeCardFromDeck(String userName, String deckName, String cardName){

    }

    public void showAllDecks(String userName){

    }

    public void showDeck(String userName ,String deckName){

    }

    public void showUserCards(String userName){

    }

    public static CardController getInstance() {
        if (instance == null)
            instance = new CardController();
        return instance;
    }
}
