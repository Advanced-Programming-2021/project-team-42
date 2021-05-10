package Model;

import Enums.Attribute;
import Enums.Effect;
import Model.Enums.Type;

public class MonsterCard extends Card {
    private Attribute attribute;
    private Type type;
    private Effect effect;
    private String cardType = "Monster Card";
    private int level;
    private int attackPoint;
    private int defencePoint;
    private boolean isDefensive;
    private boolean isEffectEnable;
    private boolean isSet;

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

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getAttackPoint() {
        return attackPoint;
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

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}

