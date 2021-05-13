package Model;

import Model.Enums.Attribute;
import Model.Enums.CardType;
import Model.Enums.MonsterType;

import java.util.ArrayList;

public class MonsterCard extends Card {
    private static ArrayList<MonsterCard> allMonsterCards;
    private Attribute attribute;
    private MonsterType monsterType;
    private CardType cardType;
    private final String cardTypeStr = "Monster Card";
    private int level;
    private int attackPoint;
    private int defencePoint;
    private boolean isDefensive;
    private boolean isEffectEnable;
    private boolean isSet;

    static {
        allMonsterCards = new ArrayList<>();
    }

    public MonsterCard (String name, String description, Attribute attribute,
                        MonsterType monsterType, CardType cardType, int attackPoint, int defencePoint, int price, int level){
        super(name, description, price);
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

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }

    public boolean isEffectEnable() {
        return isEffectEnable;
    }

    public void setEffectEnable(boolean effectEnable) {
        isEffectEnable = effectEnable;
    }

    public boolean isDefensive() {
        return isDefensive;
    }

    public void setDefensive(boolean defensive) {
        isDefensive = defensive;
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

    public String getCardTypeStr() {
        return cardTypeStr;
    }
}

