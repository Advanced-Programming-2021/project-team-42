package Controller;

import Model.Card;
import Model.User;

import java.util.ArrayList;

public class ShopController {
    public static ShopController instance = null;

    private ShopController() {
    }

    public void buyCard(String username, String cardName) throws Exception{
        User user = User.getUserByUsername(username);
        if (Card.getCardByName(cardName) == null)
           throw new Exception("there is no card with this name");
        else {
            Card card = Card.getCardByName(cardName);
            int userBalance = user.getBalance();
            int cardPrice = card.getPrice();
            if (userBalance < cardPrice) {
                throw new Exception("not enough money");
            } else {
                user.setBalance(userBalance - cardPrice);
                user.increaseCard(cardName);
                throw new Exception("Card added successfully");
            }
        }
    }

    public String showAllCards() {
        StringBuilder result = new StringBuilder();
        ArrayList<Card> allCards = Card.getAllCards();
        for (Card card : allCards)
            result.append(card.toString()).append("\n");
        return result.toString();
    }


    public static ShopController getInstance() {
        if (instance == null)
            instance = new ShopController();
        return instance;
    }
}
