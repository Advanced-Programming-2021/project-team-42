package Controller;

import Model.Card;
import Model.MonsterCard;
import Model.SpellTrapCard;
import Model.User;

import java.io.File;
import java.util.ArrayList;

public class ShopController {
    public static ShopController instance = null;

    private ShopController() {
    }

    public void buyCard(String username, String cardName) {
        User user = User.getUserByUsername(username);
        if (!Card.isCardExists(cardName))
            System.out.println("there is no card with this name");
        else {
            Card card = Card.getCardByName(cardName);
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
        ArrayList<MonsterCard> allMonsterCards = MonsterCard.getAllMonsterCards();
        ArrayList<SpellTrapCard> allSpellTrapCards = SpellTrapCard.getAllSpellTrapCards();
        for(MonsterCard monsterCard : allMonsterCards)
            System.out.println(monsterCard.getName() + ": " + monsterCard.getDescription());
        for(SpellTrapCard spellTrapCard : allSpellTrapCards)
            System.out.println(spellTrapCard.getName() + ": " + spellTrapCard.getDescription());
    }


    public static ShopController getInstance() {
        if (instance == null)
            instance = new ShopController();
        return instance;
    }
}
