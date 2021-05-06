package Model;

import java.util.HashMap;

public abstract class Card {
    protected String name;
    protected int cardPrice;
    protected int cardNumber;
    protected String description;
    public static HashMap<String , Card> allCards;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCardPrice(){
        return this.cardPrice;
    }

}
