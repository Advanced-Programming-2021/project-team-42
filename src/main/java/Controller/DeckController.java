package Controller;

import Model.Card;
import Model.Deck;
import Model.User;

import java.util.HashMap;
import java.util.Map;

public class DeckController {
    private static DeckController instance = null;

    private DeckController(){
    }

    public void createDeck(String username, String deckName){
        if (User.getUserByUsername(username).getUserDecks().containsKey(deckName))
            System.out.println("deck with name " + deckName + " already exists");
        else{
            User.getUserByUsername(username).getUserDecks().put(deckName,new Model.Deck());
            System.out.println("deck create successfully");
        }
    }

    public void deleteDeck(String username, String deckName){
        if (User.getUserByUsername(username).getUserDecks().containsKey(deckName)) {
            User.getUserByUsername(username).getUserDecks().remove(deckName);
            System.out.println("deck deleted successfully");
        }
        else System.out.println("deck with name " + deckName + " does not exists");
    }

    public void setActiveDeck(String username, String deckName){
        if (User.getUserByUsername(username).getUserDecks().containsKey(deckName)){
            System.out.println("deck with name " + deckName + " does not exists");
        }
        else {
            HashMap<String , Deck> userDeck = User.getUserByUsername(username).getUserDecks();
            for (HashMap.Entry<String, Deck> entry : userDeck.entrySet()){
                if (entry.getValue().isActive())
                    entry.getValue().setActive(false);
            }
            userDeck.get(deckName).setActive(true);
            System.out.println("deck activated successfully");
        }

    }

    public void addCardToDeck(String username, String deckName, String cardName, boolean isSide){
        HashMap<String , Card> Cards = Card.getAllCards();
        for (HashMap.Entry<String, Card> entry : Cards.entrySet()){
            if (!Cards.containsKey(cardName)){
                System.out.println("card with name " + cardName + " does not exists");
            }
            else {

            }
        }
    }

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
