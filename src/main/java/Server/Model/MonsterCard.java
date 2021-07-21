package Server.Model;

import Server.Model.Enums.Attribute;
import Server.Model.Enums.CardType;
import Server.Model.Enums.MonsterType;

import java.util.ArrayList;

public class MonsterCard extends Card {
    private static ArrayList<MonsterCard> allMonsterCards;
    private Attribute attribute;
    private MonsterType monsterType;
    private CardType cardType;
    private int level;
    private int attackPoint;
    private int defencePoint;
    private boolean isSummoned;
    private boolean isDefensive;
    private boolean isReadyToAttack = true;
    private boolean isPositionChangedInThisTurn;

    static {
        allMonsterCards = new ArrayList<>();
    }

    public MonsterCard (String name, String description, Attribute attribute,
                        MonsterType monsterType, CardType cardType, String cardPath, int attackPoint, int defencePoint, int price, int level){
        super(name, description, cardPath, price);
        this.attribute = attribute;
        this.monsterType = monsterType;
        this.cardType = cardType;
        this.attackPoint = attackPoint;
        this.defencePoint = defencePoint;
        this.level = level;
    }

    public static ArrayList<MonsterCard> getAllMonsterCards (){
        return allMonsterCards;
    }

    public static MonsterCard getMonsterCardByName(String monsterCardName){
        for(MonsterCard monsterCard : allMonsterCards){
            if(monsterCard.getName().equals(monsterCardName))
                return monsterCard;
        }
        return null;
    }

    public static boolean isMonsterCard(String monsterCardName){
        for(MonsterCard monsterCard : allMonsterCards){
            if (monsterCard.getName().equals(monsterCardName))
                return true;
        }
        return false;
    }


    public static void addMonsterCardToList(MonsterCard monsterCard){
        allMonsterCards.add(monsterCard);
    }

    public static MonsterCard getInstance(MonsterCard monsterCard){
        return new MonsterCard(monsterCard.getName(), monsterCard.getDescription(), monsterCard.getAttribute(),
                monsterCard.getMonsterType(), monsterCard.getCardType(),
                monsterCard.getCardPath(), monsterCard.getAttackPoint(),
                monsterCard.getDefencePoint(), monsterCard.getPrice(), monsterCard.getLevel());
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public boolean isSummoned() {
        return isSummoned;
    }

    public void setSummoned(boolean summoned) {
        isSummoned = summoned;
    }

    public boolean isDefensive() {
        return isDefensive;
    }

    public void setDefensive(boolean defensive) {
        isDefensive = defensive;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public int getAttackPoint() {
        return attackPoint;
    }

    public MonsterType getMonsterType() {
        return monsterType;
    }

    public void setMonsterType(MonsterType monsterType) {
        this.monsterType = monsterType;
    }

    public void setAttackPoint(int attackPoint) {
        this.attackPoint = attackPoint;
    }

    public int getDefencePoint() {
        return defencePoint;
    }

    public void setDefencePoint(int defencePoint) {
        this.defencePoint = defencePoint;
    }

    public boolean isPositionChangedInThisTurn() {
        return isPositionChangedInThisTurn;
    }

    public void setPositionChangedInThisTurn(boolean positionChangedInThisTurn) {
        isPositionChangedInThisTurn = positionChangedInThisTurn;
    }

    public boolean isReadyToAttack(){
        return this.isReadyToAttack;
    }

    public void setReadyToAttack(boolean isReadyToAttack){
        this.isReadyToAttack = isReadyToAttack;
    }

    public String exclusiveToString(){
        return "Name: " + this.getName() + "\nLevel: " + this.getLevel() + "\nType: " + this.getMonsterType() +
                "\nATK: " + this.getAttackPoint() + "\nDEF: " + this.getDefencePoint() + "\nDescription: " + this.getDescription();
    }
}

