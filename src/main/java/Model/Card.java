package Model;


import java.util.ArrayList;

public abstract class Card {
    private static ArrayList<Card> allCards;
    protected String name;
    protected int price;
    protected boolean isSet;
    protected String description;
    protected String cardPath;

    static {
        allCards = new ArrayList<>();
    }

    public Card(String name, String description, String cardPath, int price){
        this.name = name;
        this.description = description;
        this.price = price;
        this.cardPath = cardPath;
    }

    public static void addCardToList(Card card){
        allCards.add(card);
    }

    public static ArrayList<Card> getAllCards(){
        return allCards;
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

    public String getCardPath() {
        return cardPath;
    }

    public void setCardPath(String cardPath) {
        this.cardPath = cardPath;
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

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }

    @Override
    public String toString(){
        return "\033[0;97m" + this.name + "\033[0m: " + this.description;
    }
}
