package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class GameBoard {
    private User player;
    private HashMap<Integer, Card> monstersPlace;
    private HashMap<Integer, Card> spellTrapsPlace;
    private ArrayList<Card> graveYard;
    private ArrayList<Card> cardsInHand;
    private Card fieldZone = null;

    public GameBoard (User player){
        this.player = player;
        monstersPlace = new HashMap<>();
        spellTrapsPlace = new HashMap<>();
        graveYard = new ArrayList<>();
        cardsInHand = new ArrayList<>();
    }

    public void setFieldZone (Card fieldZone){
        this.fieldZone = fieldZone;
    }

    public Card getFieldZone() {
        return fieldZone;
    }

    public ArrayList<Card> getGraveYard() {
        return graveYard;
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public HashMap<Integer, Card> getMonstersPlace(){
        return this.monstersPlace;
    }

    public HashMap<Integer, Card> getSpellTrapsPlace(){
        return this.spellTrapsPlace;
    }

    public void setMonstersPlace(Card monstersCard, int place){
        monstersPlace.put(place, monstersCard);
    }

    public Card getMonsterCardByPlace(int place){
        return monstersPlace.get(place);
    }

    public void setSpellTrapsPlace(Card spellTrapCard, int place){
        monstersPlace.put(place, spellTrapCard);
    }

    public Card getSpellTrapCardByPlace(int place){
        return spellTrapsPlace.get(place);
    }

    public void drawBoard(){}
}
