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
    private ArrayList<Card> sideDeckCards;
    private MonsterCard monsterSelectedCard = null;
    private SpellTrapCard spellTrapSelectedCard = null;
    private Card graveyardSelectedCard = null;
    private Card handSelectedCard = null;
    private Card fieldZoneSelectedCard = null;
    private Card fieldZone = null;
    private int maxLP = 0;
    private int WinsCount = 0;

    public GameBoard(User player, ArrayList<Card> mainDeckCards, ArrayList<Card> sideDeckCards) {
        this.player = player;
        monstersPlace = new HashMap<>();
        spellTrapsPlace = new HashMap<>();
        fillMonsterPlaces(monstersPlace);
        fillSpellTrapPlaces(spellTrapsPlace);
        graveYard = new ArrayList<>();
        this.cardsInHand = new ArrayList<>();
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

    public void addCardToHand(Card card){
        cardsInHand.add(card);
    }

    public void addCardToGraveyard(Card card){
        graveYard.add(card);
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public int getWinsCount() {
        return WinsCount;
    }

    public void setWinsCount(int winsCount) {
        WinsCount = winsCount;
    }

    public int getMaxLP() {
        return maxLP;
    }

    public void setMaxLP(int maxLP) {
        this.maxLP = maxLP;
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
        System.out.println(mainDeckCards.size());
        //TODO: spellTrap cards
        //TODO: monster cards
        System.out.println(graveYard.size() + "\t\t\t\t\t\t" + (fieldZone == null ? "E" : "O"));

    }

    public void drawBoardAsYourself() {
        System.out.println((fieldZone == null ? "E" : "O") + "\t\t\t\t\t\t" + graveYard.size());
        //TODO: monster cards
        //TODO: spellTrap cards
        System.out.println("\t\t\t\t\t\t" + mainDeckCards.size());
        for (Card card : cardsInHand)
            System.out.print("c\t");
        System.out.print("\n");
        System.out.println(this.player.getUsername() + ": " + this.player.getLP());
    }
}
