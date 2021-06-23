package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameBoard {
    private User player;
    private HashMap<Integer, MonsterCard> monstersPlace;
    private HashMap<Integer, SpellTrapCard> spellTrapsPlace;
    private ArrayList<Card> graveYard;
    private ArrayList<Card> mainDeckCards;
    private ArrayList<Card> cardsInHand;
    private ArrayList<Card> sideDeckCards;
    private MonsterCard monsterSelectedCard = null;
    private int selectedMonsterPlace;
    private SpellTrapCard spellTrapSelectedCard = null;
    private int selectedSpellTrapPlace;
    private Card graveyardSelectedCard = null;
    private Card handSelectedCard = null;
    private int selectedHandPlace;
    private Card fieldZoneSelectedCard = null;
    private Card fieldZone = null;
    private int maxLP = 0;
    private int WinsCount = 0;
    private boolean trapEffect;

    public GameBoard(User player, ArrayList<Card> mainDeckCards, ArrayList<Card> sideDeckCards, ArrayList<Card> cardsInHand) {
        this.player = player;
        monstersPlace = new HashMap<>();
        spellTrapsPlace = new HashMap<>();
        fillMonsterPlaces(monstersPlace);
        fillSpellTrapPlaces(spellTrapsPlace);
        graveYard = new ArrayList<>();
        this.cardsInHand = cardsInHand;
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

    public void deselectAll(){
        this.monsterSelectedCard = null;
        this.spellTrapSelectedCard = null;
        this.graveyardSelectedCard = null;
        this.handSelectedCard = null;
        this.fieldZoneSelectedCard = null;
        this.selectedMonsterPlace = 0;
        this.selectedSpellTrapPlace = 0;
        this.selectedHandPlace = 0;
    }

    public boolean checkSelections(){
        return !(this.monsterSelectedCard == null &&
                this.spellTrapSelectedCard == null &&
                this.graveyardSelectedCard == null &&
                this.handSelectedCard == null &&
                this.fieldZoneSelectedCard == null);
    }

    public Card getSelectedCard(){
        if(this.monsterSelectedCard != null)
            return this.monsterSelectedCard;
        else if(this.spellTrapSelectedCard != null)
            return this.spellTrapSelectedCard;
        else if(this.graveyardSelectedCard != null)
            return this.graveyardSelectedCard;
        else if(this.handSelectedCard != null)
            return this.handSelectedCard;
        else if(this.fieldZoneSelectedCard != null)
            return this.fieldZoneSelectedCard;
        else
            return null;
    }

    public int monsterPlacesSize(){
        int size = 0;
        for(Map.Entry<Integer, MonsterCard> entry : monstersPlace.entrySet()){
            if(entry.getValue() != null)
                size++;
        }
        return size;
    }

    public int spellTrapPlacesSize(){
        int size = 0;
        for(Map.Entry<Integer, SpellTrapCard> entry : spellTrapsPlace.entrySet()){
            if(entry.getValue() != null)
                size++;
        }
        return size;
    }

    public MonsterCard getMonsterSelectedCard() {
        return monsterSelectedCard;
    }

    public Card getFieldZone() {
        return fieldZone;
    }

    public void setFieldZone(Card fieldZone) {
        this.fieldZone = fieldZone;
    }

    public HashMap<Integer, MonsterCard> getMonstersPlace() {
        return monstersPlace;
    }

    public HashMap<Integer, SpellTrapCard> getSpellTrapsPlace() {
        return spellTrapsPlace;
    }

    public ArrayList<Card> getGraveYard() {
        return graveYard;
    }

    public ArrayList<Card> getMainDeckCards() {
        return mainDeckCards;
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public Card getHandCardByPlace(int place){
        return cardsInHand.get(place - 1);
    }

    public ArrayList<Card> getSideDeckCards() {
        return sideDeckCards;
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

    public int getSelectedMonsterPlace() {
        return selectedMonsterPlace;
    }

    public void setSelectedMonsterPlace(int selectedMonsterPlace) {
        this.selectedMonsterPlace = selectedMonsterPlace;
    }

    public int getSelectedSpellTrapPlace() {
        return selectedSpellTrapPlace;
    }

    public int getSelectedHandPlace() {
        return selectedHandPlace;
    }

    public void removeCardFromHand(int place){
        cardsInHand.remove(place);
    }

    public void setSelectedHandPlace(int selectedHandPlace) {
        this.selectedHandPlace = selectedHandPlace;
    }

    public void setSelectedSpellTrapPlace(int selectedSpellTrapPlace) {
        this.selectedSpellTrapPlace = selectedSpellTrapPlace;
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

    public boolean isTrapEffect() {
        return trapEffect;
    }

    public void setTrapEffect(boolean trapEffect) {
        this.trapEffect = trapEffect;
    }

    public void setMonstersPlace(MonsterCard monstersCard, int place) {
        monstersPlace.put(place, monstersCard);
    }

    public MonsterCard getMonsterCardByPlace(int place) {
        return monstersPlace.get(place);
    }

    public void setSpellTrapsPlace(SpellTrapCard spellTrapCard, int place) {
        spellTrapsPlace.put(place, spellTrapCard);
    }

    public SpellTrapCard getSpellTrapCardByPlace(int place) {
        return spellTrapsPlace.get(place);
    }

    public void drawBoardAsOpponent() {
        System.out.println(this.player.getNickname() + ": " + this.player.getLP());
        for (int i = 0; i < cardsInHand.size(); i++)
            System.out.print("c\t");
        System.out.print("\n");
        System.out.println(mainDeckCards.size());
        printSpellTrapFlag(getSpellTrapCardByPlace(4));
        printSpellTrapFlag(getSpellTrapCardByPlace(2));
        printSpellTrapFlag(getSpellTrapCardByPlace(1));
        printSpellTrapFlag(getSpellTrapCardByPlace(3));
        printSpellTrapFlag(getSpellTrapCardByPlace(5));
        System.out.print("\n");
        printMonsterFlag(getMonsterCardByPlace(4));
        printMonsterFlag(getMonsterCardByPlace(2));
        printMonsterFlag(getMonsterCardByPlace(1));
        printMonsterFlag(getMonsterCardByPlace(3));
        printMonsterFlag(getMonsterCardByPlace(5));
        System.out.print("\n");
        System.out.println(graveYard.size() + "\t\t\t\t\t\t" + (fieldZone == null ? "E" : "O"));

    }

    public void drawBoardAsYourself() {
        System.out.println((fieldZone == null ? "E" : "O") + "\t\t\t\t\t\t" + graveYard.size());
        printMonsterFlag(getMonsterCardByPlace(5));
        printMonsterFlag(getMonsterCardByPlace(3));
        printMonsterFlag(getMonsterCardByPlace(1));
        printMonsterFlag(getMonsterCardByPlace(2));
        printMonsterFlag(getMonsterCardByPlace(4));
        System.out.print("\n");
        printSpellTrapFlag(getSpellTrapCardByPlace(5));
        printSpellTrapFlag(getSpellTrapCardByPlace(3));
        printSpellTrapFlag(getSpellTrapCardByPlace(1));
        printSpellTrapFlag(getSpellTrapCardByPlace(2));
        printSpellTrapFlag(getSpellTrapCardByPlace(4));
        System.out.print("\n");
        System.out.println("\t\t\t\t\t\t" + mainDeckCards.size());
        for (int i = 0; i < cardsInHand.size(); i++)
            System.out.print("c\t");
        System.out.print("\n");
        System.out.println(this.player.getNickname() + ": " + this.player.getLP());
    }

    public void printMonsterFlag(MonsterCard monsterCard){
        if(monsterCard == null)
            System.out.print("E\t");
        else{
            if(monsterCard.isSet() && !monsterCard.isSummoned())
                System.out.print("DH\t");
            else{
                if(monsterCard.isSummoned() && monsterCard.isDefensive())
                    System.out.print("DO\t");
                else if(monsterCard.isSummoned() && !monsterCard.isDefensive())
                    System.out.print("OO\t");
            }
        }
    }

    public void printSpellTrapFlag(SpellTrapCard spellTrapCard){
        if(spellTrapCard == null)
            System.out.print("E\t");
        else{
            if(spellTrapCard.isSet() && spellTrapCard.isEffectActive())
                System.out.print("O\t");
            else if(spellTrapCard.isSet() && !spellTrapCard.isEffectActive())
                System.out.print("H\t");
        }
    }

    public boolean doesSpellTrapZoneContainsCard(String cardName){
        for(Map.Entry<Integer, SpellTrapCard> entry : spellTrapsPlace.entrySet()){
            if(entry.getValue() != null && entry.getValue().getName().equals(cardName))
                return true;
        }
        return false;
    }

    public boolean doesMonsterZoneContainsCard(String cardName){
        for(Map.Entry<Integer, MonsterCard> entry : monstersPlace.entrySet()){
            if(entry.getValue() != null && entry.getValue().getName().equals(cardName))
                return true;
        }
        return false;
    }

    public boolean doesMonsterZoneContainsSummonedMirageDragon(){
        for(Map.Entry<Integer, MonsterCard> entry : monstersPlace.entrySet()){
            if(entry.getValue().getName().equals("Mirage Dragon") && entry.getValue().isSummoned())
                return true;
        }
        return false;
    }
}
