package Controller;

import Model.Card;
import Model.User;

import java.util.HashMap;

public class ShopController {
    public static ShopController instance = null;

    private ShopController() {
    }

    public void buyCard(String username, String cardName) {
        User user = User.getUserByUsername(username);
        if (!Card.allCards.containsKey(cardName))
            System.out.println("there is no card with this name");
        else {
            Card card = Card.allCards.get(cardName);
            int userBalance = user.getBalance();
            int cardPrice = card.getPrice();
            if (userBalance < cardPrice) {
                System.out.println("not enough money");
            } else {
                user.setBalance(userBalance - cardPrice);
                user.increaseCard(cardName);
                System.out.println("card " + cardName + " added to your cards successfully");
            }
        }
    }

    public void showAllCards() {
        HashMap<String, Card> allCards = Card.getAllCards();
        for (HashMap.Entry<String, Card> entry : allCards.entrySet())
            System.out.println(entry.getValue().getName() + " : " + entry.getValue().getDescription());
    }


    public static ShopController getInstance() {
        if (instance == null)
            instance = new ShopController();
        return instance;
    }
}
