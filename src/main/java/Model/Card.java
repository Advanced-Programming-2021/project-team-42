package Model;

import java.util.HashMap;

public abstract class  Card {
    public static HashMap<String, Card> allCards;
    protected String name;
    protected int price;
    protected String description;

    static {
        allCards = new HashMap<>();
    }

    public Card(String name, String description, int price){
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public static HashMap<String, Card> getAllCards(){
        return allCards;
    }

    public static void addCardToList(Card card){
        allCards.put(card.getName(), card);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
