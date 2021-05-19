package Model;

import Model.Enums.Icon;
import Model.Enums.Status;

import java.util.ArrayList;

public class SpellTrapCard extends Card{
    private static ArrayList<SpellTrapCard> allSpellTrapCards;
    private String cardType;
    private Icon icon;
    private Status status;
    private boolean isSet;
    private boolean isEffectActive;

    static {
        allSpellTrapCards = new ArrayList<>();
    }

    public SpellTrapCard (String name, String description, String cardType, Icon icon,
                      Status status, int price){
        super(name, description, price);
        this.icon = icon;
        this.status = status;
        this.cardType = cardType;
    }

    public static ArrayList<SpellTrapCard> getAllSpellTrapCards(){
        return allSpellTrapCards;
    }

    public static void addSpellTrapCardToList(SpellTrapCard spellTrapCard){
        allSpellTrapCards.add(spellTrapCard);
    }

    public static SpellTrapCard getSpellTrapCardByName(String cardName){
        for(SpellTrapCard spellTrapCard : allSpellTrapCards){
            if(spellTrapCard.getName().equals(cardName))
                return spellTrapCard;
        }
        return null;
    }

    public static SpellTrapCard getInstance(SpellTrapCard spellTrapCard){
        return new SpellTrapCard(spellTrapCard.getName(), spellTrapCard.getDescription(),
                spellTrapCard.getCardType(), spellTrapCard.getIcon(), spellTrapCard.getStatus(), spellTrapCard.getPrice());
    }

    public String getType() {
        return this.cardType;
    }

    public Icon getIcon() { return icon; }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }

    public boolean isEffectActive() {
        return isEffectActive;
    }

    public void setEffectActive(boolean effectActive) {
        isEffectActive = effectActive;
    }

    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
