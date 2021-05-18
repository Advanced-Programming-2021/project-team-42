package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class GameBoard {
    private User player;
    private HashMap<Integer, MonsterCard> monstersPlace;
    private HashMap<Integer, SpellTrapCard> spellTrapsPlace;
    private ArrayList<Card> graveYard;
    private ArrayList<Card> mainDeckCards;
    private ArrayList<Card> cardsInHand;
    private ArrayList<Card> remainedCards;
    private ArrayList<Card> sideDeckCards;
    private MonsterCard monsterSelectedCard = null;
    private SpellTrapCard spellTrapSelectedCard = null;
    private Card graveyardSelectedCard = null;
    private Card handSelectedCard = null;
    private Card fieldZoneSelectedCard = null;
    private Card fieldZone = null;

    public GameBoard(User player, ArrayList<Card> mainDeckCards, ArrayList<Card> sideDeckCards) {
        this.player = player;
        monstersPlace = new HashMap<>();
        spellTrapsPlace = new HashMap<>();
        fillMonsterPlaces(monstersPlace);
        fillSpellTrapPlaces(spellTrapsPlace);
        graveYard = new ArrayList<>();
        this.cardsInHand = new ArrayList<>();
        this.remainedCards = new ArrayList<>();
        this.sideDeckCards = sideDeckCards;
        this.mainDeckCards = mainDeckCards;
    }

    public void fillMonsterPlaces(HashMap<Integer, MonsterCard> monstersPlace) {
        for (int i = 1; i <= 5; i++)
            monstersPlace.put(i, null);
    }

    public void fillSpellTrapPlaces(HashMap<Integer, SpellTrapCard> spellTrapsPlace){
        for (int i = 1; i <= 5; i++)
            spellTrapsPlace.put(i, null);
    }

    public void setFieldZone(Card fieldZone) {
        this.fieldZone = fieldZone;
    }

    public Card getFieldZone() {
        return fieldZone;
    }

    public ArrayList<Card> getSideDeckCards() {
        return sideDeckCards;
    }

    public void setSideDeckCards(ArrayList<Card> sideDeckCards) {
        this.sideDeckCards = sideDeckCards;
    }

    public ArrayList<Card> getMainDeckCards() {
        return mainDeckCards;
    }

    public void setMainDeckCards(ArrayList<Card> mainDeckCards) {
        this.mainDeckCards = mainDeckCards;
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public void setGraveYard(ArrayList<Card> graveYard) {
        this.graveYard = graveYard;
    }

    public void setCardsInHand(ArrayList<Card> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    public ArrayList<Card> getRemainedCards() {
        return remainedCards;
    }

    public void setMonstersPlace(HashMap<Integer, MonsterCard> monstersPlace) {
        this.monstersPlace = monstersPlace;
    }

    public MonsterCard getMonsterSelectedCard() {
        return monsterSelectedCard;
    }

    public void setMonsterSelectedCard(MonsterCard monsterSelectedCard) {
        this.monsterSelectedCard = monsterSelectedCard;
    }

    public SpellTrapCard getSpellTrapSelectedCard() {
        return spellTrapSelectedCard;
    }

    public void setSpellTrapSelectedCard(SpellTrapCard spellTrapSelectedCard) {
        this.spellTrapSelectedCard = spellTrapSelectedCard;
    }

    public Card getGraveyardSelectedCard() {
        return graveyardSelectedCard;
    }

    public void setGraveyardSelectedCard(Card graveyardSelectedCard) {
        this.graveyardSelectedCard = graveyardSelectedCard;
    }

    public Card getHandSelectedCard() {
        return handSelectedCard;
    }

    public void setHandSelectedCard(Card handSelectedCard) {
        this.handSelectedCard = handSelectedCard;
    }

    public Card getFieldZoneSelectedCard() {
        return fieldZoneSelectedCard;
    }

    public void setFieldZoneSelectedCard(Card fieldZoneSelectedCard) {
        this.fieldZoneSelectedCard = fieldZoneSelectedCard;
    }

    public void setRemainedCards(ArrayList<Card> remainedCards) {
        this.remainedCards = remainedCards;
    }

    public ArrayList<Card> getGraveYard() {
        return graveYard;
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public void setMonstersPlace(MonsterCard monstersCard, int place) {
        monstersPlace.put(place, monstersCard);
    }

    public Card getMonsterCardByPlace(int place) {
        return monstersPlace.get(place);
    }

    public void setSpellTrapsPlace(SpellTrapCard spellTrapCard, int place) {
        spellTrapsPlace.put(place, spellTrapCard);
    }

    public Card getSpellTrapCardByPlace(int place) {
        return spellTrapsPlace.get(place);
    }

    public void drawBoardAsOpponent() {
        System.out.println(this.player.getUsername() + ": " + this.player.getLP());
        for (Card card : cardsInHand)
            System.out.print("c\t");
        System.out.print("\n");
        System.out.println(remainedCards.size());
        //TODO: spellTrap cards
        //TODO: monster cards
        System.out.println(graveYard.size() + "\t\t\t\t\t\t" + (fieldZone == null ? "E" : "O"));

    }

    public void drawBoardAsYourself() {
        System.out.println((fieldZone == null ? "E" : "O") + "\t\t\t\t\t\t" + graveYard.size() );
        //TODO: monster cards
        //TODO: spellTrap cards
        System.out.println("\t\t\t\t\t\t" + remainedCards.size());
        for (Card card : cardsInHand)
            System.out.print("c\t");
        System.out.print("\n");
        System.out.println(this.player.getUsername() + ": " + this.player.getLP());
    }
}
