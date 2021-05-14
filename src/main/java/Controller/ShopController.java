package Controller;

import Model.Card;
import Model.MonsterCard;
import Model.SpellTrapCard;
import Model.User;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.util.ArrayList;

public class ShopController {
    public static ShopController instance = null;
    private final static String USERS_FILE_PATH = "C:\\Users\\Vision\\IdeaProjects\\Game First Phase\\src\\main\\java\\Database\\Users";

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
                try {
                    user.setBalance(userBalance - cardPrice);
                    user.increaseCard(cardName);
                    FileWriter fileWriter = new FileWriter(USERS_FILE_PATH + "\\" + username + ".json");
                    new Gson().toJson(user, fileWriter);
                    System.out.println("card " + cardName + " added to your cards successfully");
                } catch (Exception e){
                    System.out.println("Can not add card to user cards");
                }
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
