package Model;

import java.util.HashMap;

public class Deck {
    String name;
    boolean isSide;
    boolean isActive;
    HashMap <String ,Integer> cards;

    public String getName(){
        return this.name;
    }

    public boolean isSide(){
        return this.isSide;
    }

    public boolean isActive(){
        return this.isActive;
    }

    public void setActive(boolean isActive){
        this.isActive = isActive;
    }

    public HashMap getCards(){
        return this.cards;
    }

}
