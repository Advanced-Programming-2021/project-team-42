package Server.Controller;

import Server.Model.Card;
import Server.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShopController {
    public static ShopController instance = null;
    private static HashMap<Card, Integer> shopCards = new HashMap<>();
    private static int limit = 2;

    private ShopController() {
    }

    public static HashMap<Card, Integer> getShopCards() {
        return shopCards;
    }

    public static void setLimit(int limit) {
        ShopController.limit = limit;
    }

    public static void increaseAmount(Card card, int count) {
        shopCards.replace(card, shopCards.get(card) + count);
    }

    public static void decreaseAmount(Card card, int count) {
        if (shopCards.get(card) > count)
            shopCards.replace(card, shopCards.get(card) - count);
        else
            shopCards.replace(card, 0);
    }

    public static void firstCardAdd(Card card) {
        shopCards.put(card, 5);
    }

    public void buyCard(String username, String cardName) throws Exception {
        User user = User.getUserByUsername(username);
        if (Card.getCardByName(cardName) == null)
            throw new Exception("there is no card with this name");
        else {
            Card card = Card.getCardByName(cardName);
            if (shopCards.get(card) == 0)
                throw new Exception("there is no " + cardName + " in shop");
            if (shopCards.get(card) <= limit)
                throw new Exception("You cant buy this card duo to shop limits");
            int userBalance = user.getBalance();
            int cardPrice = card.getPrice();
            if (userBalance < cardPrice) {
                throw new Exception("not enough money");
            } else {
                user.setBalance(userBalance - cardPrice);
                if (user.getUserAllCards() != null)
                    user.increaseCard(cardName);
                decreaseAmount(card, 1);
            }
        }
    }

    public void sellCard(String username, String cardName) throws Exception {
        User user = User.getUserByUsername(username);
        if (Card.getCardByName(cardName) == null)
            throw new Exception("there is no card with this name");
        if (!user.doesUserHasThisCard(cardName))
            throw new Exception("you dont have this card");
        Card card = Card.getCardByName(cardName);
        user.setBalance(user.getBalance() + card.getPrice());
        user.decreaseCard(cardName);
        increaseAmount(card, 1);
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
