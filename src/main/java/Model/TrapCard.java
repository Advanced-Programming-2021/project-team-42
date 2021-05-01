package Model;

import Enums.Effect;
import Enums.Icon;

public class TrapCard extends Card{
    private String cardType = "Trap Card";
    private Icon icon;
    private Effect effect;
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


    public String getType() {
        return cardType;
    }

    public void setType(String type) {
        this.cardType = type;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }
}
