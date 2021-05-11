package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Deck {
    String name;
    boolean isActive;
    private HashMap<String ,Integer> cards = new HashMap<>(); // name and amount of each card
    private int amount;
    public Deck sideDeck = new Deck();

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public boolean isActive(){
        return this.isActive;
    }

    public void setActive(boolean isActive){
        this.isActive = isActive;
    }

    public HashMap<String,Integer> getAllCards(){
        return this.cards;
    }

    public int getMainDeckAmount(){
        return this.amount;
    }

    public int getSideDeckAmount(){
        return this.sideDeck.amount;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }

    public void increaseCard(String cardName){
        int x = cards.get(cardName);
        cards.put(cardName, x+1);
        this.amount += 1;
    }

    public void decreaseCard(String cardName){
        int x = cards.get(cardName);
        cards.put(cardName, x-1);
        if (x-1 == 0)
            cards.remove(cardName);
        this.amount -= 1;
    }


}
