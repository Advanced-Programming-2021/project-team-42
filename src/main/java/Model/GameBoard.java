package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class GameBoard {
    private HashMap<Integer, Card> places = new HashMap<>();
    private ArrayList<Card> graveYard = new ArrayList<>();
    private ArrayList<Card> cardsInHand = new ArrayList<>();
    private ArrayList<Card> remainedCards = new ArrayList<>();
    private Card fieldZone = null;

    public Card getCardByPlace(int place){
        return places.get(place);
    }

    public void placeCardByPlace(int place, Card card){
        places.put(place, card);
    }

    public ArrayList<Card> getGraveYard() {
        return graveYard;
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public ArrayList<Card> getRemainedCards() {
        return remainedCards;
    }

    public Card getFieldZone() {
        return fieldZone;
    }

    public void drawBoard(){}
}
