package Model;


import java.util.ArrayList;

public abstract class Card {
    private static ArrayList<Card> allCards;
    protected String name;
    protected int price;
    protected String description;

    static {
        allCards = new ArrayList<>();
    }

    public Card(String name, String description, int price){
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public static void addCardToList(Card card){
        allCards.add(card);
    }

    public static ArrayList<Card> getAllCards(){
        return allCards;
    }

    public static boolean isCardExists(String cardName){
        for(Card card : allCards){
            if(card.getName().equals(cardName))
                return true;
        }
        return false;
    }

    public static Card getCardByName(String cardName){
        for(Card card : allCards){
            if(card.getName().equals(cardName))
                return card;
        }
        return null;
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
