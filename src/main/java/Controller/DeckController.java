package Controller;

import Model.*;

import javax.jws.soap.SOAPBinding;
import java.util.HashMap;
import java.util.Map;

public class DeckController {
    private static DeckController instance = null;

    private DeckController(){
    }

    public void createDeck(String username, String deckName){
        if (User.getUserByUsername(username).getUserDecks().containsKey(deckName))
            System.out.println("deck with name " + deckName + " already exist");
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
        else System.out.println("deck with name " + deckName + " does not exist");
    }

    public void setActiveDeck(String username, String deckName){
        if (User.getUserByUsername(username).getUserDecks().containsKey(deckName)){
            System.out.println("deck with name " + deckName + " does not exist");
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
        if (!Cards.containsKey(cardName)){
            System.out.println("card with name " + cardName + " does not exist");
        }
        else {
            HashMap<String,Deck> userDecks = User.getUserByUsername(username).getUserDecks();
            if (!userDecks.containsKey(deckName)){
                System.out.println("deck with name " + deckName + " does not exist");
            }
            else {
                int sideDeckAmount = userDecks.get(deckName).getSideDeckAmount();
                if (sideDeckAmount >= 15 && isSide) {
                    System.out.println("side deck is full");
                }
                else {
                    int  mainDeckAmount = userDecks.get(deckName).getMainDeckAmount();
                    if (mainDeckAmount >= 60 && (!isSide)){
                        System.out.println("main deck is full");
                    }
                    else {
                        int amountInMainDeck = 0;
                        int amountInSideDeck = 0;
                        if (userDecks.get(deckName).getAllCards().containsKey(cardName))
                            amountInMainDeck = (int) userDecks.get(deckName).getAllCards().get(cardName);
                        if (userDecks.get(deckName).sideDeck.getAllCards().containsKey(cardName))
                            amountInSideDeck = (int) userDecks.get(deckName).sideDeck.getAllCards().get(cardName);
                        if (amountInMainDeck + amountInSideDeck >= 3){
                            System.out.println("there are already three cards with name " + cardName + " in deck " + deckName);
                        }
                        else {
                            if(isSide) {
                                userDecks.get(deckName).sideDeck.increaseCard(cardName);
                                System.out.println("card added to deck successfully");
                            }
                            else {
                                userDecks.get(deckName).increaseCard(cardName);
                                System.out.println("card added to deck successfully");
                            }
                        }
                    }
                }
            }
        }
    }

    public void removeCardFromDeck(String username, String deckName, String cardName, boolean isSide){
        HashMap<String,Deck> userDecks = User.getUserByUsername(username).getUserDecks();
        if (!userDecks.containsKey(deckName)){
            System.out.println("deck with name " + deckName + " does not exist");
        }
        else {
            HashMap<String , Card> Cards = Card.getAllCards();
            if (!Cards.containsKey(cardName)){
                System.out.println("card with name " + cardName + " does not exist");
            }
            else {
                if (isSide) {
                    userDecks.get(deckName).sideDeck.decreaseCard(cardName);
                    System.out.println("card removed successfully");
                }
                else {
                    userDecks.get(deckName).decreaseCard(cardName);
                    System.out.println("card removed successfully");
                }
            }
        }
    }

    public void showAllDecks(String username){
        System.out.println("active deck:");
        HashMap<String,Deck> userDecks = User.getUserByUsername(username).getUserDecks();
        for (HashMap.Entry<String, Deck> entry : userDecks.entrySet()){
            if (entry.getValue().isActive()){
                int mainDeckAmount = entry.getValue().getMainDeckAmount();
                int sideDeckAmount = entry.getValue().getSideDeckAmount();
                if (mainDeckAmount >= 40 && mainDeckAmount <= 60 && sideDeckAmount >= 0 && sideDeckAmount <= 15)
                    System.out.println(entry.getKey() + ": main deck " + mainDeckAmount + " side deck " + sideDeckAmount + " valid");
                else System.out.println(entry.getKey() + ": main deck " + mainDeckAmount + " side deck " + sideDeckAmount + " invalid");
            }
        }
        System.out.println("other decks:");
        for (HashMap.Entry<String, Deck> entry : userDecks.entrySet()){
            if (!entry.getValue().isActive()){
                int mainDeckAmount = entry.getValue().getMainDeckAmount();
                int sideDeckAmount = entry.getValue().getSideDeckAmount();
                if (mainDeckAmount >= 40 && mainDeckAmount <= 60 && sideDeckAmount >= 0 && sideDeckAmount <= 15)
                    System.out.println(entry.getKey() + ": main deck " + mainDeckAmount + " side deck " + sideDeckAmount + " valid");
                else System.out.println(entry.getKey() + ": main deck " + mainDeckAmount + " side deck " + sideDeckAmount + " invalid");
            }
        }
    }

    public void showDeck(String username ,String deckName, boolean isSide){
        if (!User.getUserByUsername(username).getUserDecks().containsKey(deckName)){
            System.out.println("deck with this name does not exist");
        }
        else{
            User user = User.getUserByUsername(username);
            if (!isSide){
                System.out.println("main deck:");
                System.out.println("monsters:");
                HashMap<String, Integer> userAllCards = user.getUserAllCards();
                for (Map.Entry<String, Integer> entry : userAllCards.entrySet()){
                    if (MonsterCard.getAllCards().containsKey(entry.getKey()))
                        System.out.println(MonsterCard.getAllCards().get(entry.getKey()).getName() + " : " + MonsterCard.getAllCards().get(entry.getKey()).getDescription());
                }
                System.out.println("spell and traps:");
                for (Map.Entry<String, Integer> entry : userAllCards.entrySet()){
                    if (!MonsterCard.getAllCards().containsKey(entry.getKey()))
                        if (SpellCard.getAllCards().containsKey(entry.getKey()))
                        System.out.println(SpellCard.getAllCards().get(entry.getKey()).getName() + " : " + SpellCard.getAllCards().get(entry.getKey()).getDescription());
                        else System.out.println(TrapCard.getAllCards().get(entry.getKey()).getName() + " : " + TrapCard.getAllCards().get(entry.getKey()).getDescription());
                }
            }
            else {
                System.out.println("side deck:");
                System.out.println("monsters:");
                HashMap<String, Integer> userAllCards = user.getUserAllCards();
                for (Map.Entry<String, Integer> entry : userAllCards.entrySet()){
                    if (MonsterCard.getAllCards().containsKey(entry.getKey()))
                        System.out.println(MonsterCard.getAllCards().get(entry.getKey()).getName() + " : " + MonsterCard.getAllCards().get(entry.getKey()).getDescription());
                }
                System.out.println("spell and traps:");
                for (Map.Entry<String, Integer> entry : userAllCards.entrySet()){
                    if (!MonsterCard.getAllCards().containsKey(entry.getKey()))
                        if (SpellCard.getAllCards().containsKey(entry.getKey()))
                            System.out.println(SpellCard.getAllCards().get(entry.getKey()).getName() + " : " + SpellCard.getAllCards().get(entry.getKey()).getDescription());
                        else System.out.println(TrapCard.getAllCards().get(entry.getKey()).getName() + " : " + TrapCard.getAllCards().get(entry.getKey()).getDescription());
                }
            }
        }
    }

    public void showUserCards(String username){
    }







    public static DeckController getInstance(){
        if(instance == null)
            instance = new DeckController();
        return instance;
    }

}
