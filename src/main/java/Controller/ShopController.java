package Controller;

import Model.User;
import Model.Card;

import java.util.HashMap;

public class ShopController {
    public ShopController(){

    }
    public void sellCard(String userName,String cardName){

        User user = User.getUserByUsername(userName);
        if (!Card.allCards.containsKey(cardName))
            System.out.println("there is no card with this name");
            else {
            Card card = Card.allCards.get(cardName);
            int userBalance = user.getBalance();
            int cardPrice = card.getCardPrice();
            if (userBalance < cardPrice) {
                System.out.println("not enough money");
            }
            else {
                user.setBalance(userBalance - cardPrice);
                user.increaseCard(cardName);
                System.out.println("card " + cardName + " added to your cards successfully");
            }
        }

    }

    public void showAllCards(){
        for (HashMap.Entry<String, Card> entry : Card.allCards.entrySet()){
            System.out.println(entry.getValue().getName() + " : " + entry.getValue().getDescription());
        }
    }

}
