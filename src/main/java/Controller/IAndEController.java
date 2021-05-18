package Controller;

import Model.Card;
import Model.MonsterCard;
import Model.SpellTrapCard;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class IAndEController {
    private static IAndEController instance = null;
    private static final String I_E_CARDS_PATH = "C:\\Users\\Vision\\IdeaProjects\\Game First Phase\\src\\main\\java\\Database\\ImportExportedCards";
    private static FileWriter FILE_WRITER;
    private static FileReader FILE_READER;

    private IAndEController(){}

    public void importCard(String cardName){
        try {
            Gson gson = new Gson();
            FILE_READER = new FileReader(I_E_CARDS_PATH + "\\" + cardName + ".json");
            if (MonsterCard.isMonsterCard(cardName)) {
                MonsterCard monsterCard = gson.fromJson(FILE_READER, MonsterCard.class);
                Card.addCardToList(monsterCard);
                MonsterCard.addMonsterCardToList(monsterCard);
            } else {
                SpellTrapCard spellTrapCard = gson.fromJson(FILE_READER, SpellTrapCard.class);
                Card.addCardToList(spellTrapCard);
                SpellTrapCard.addSpellTrapCardToList(spellTrapCard);
            }
            System.out.println("card imported successfully");
        } catch (IOException e){
            System.out.println("Can not import card!");
        }
    }

    public void exportCard(String cardName){
        try {
            Gson gson = new Gson();
            FILE_WRITER = new FileWriter(I_E_CARDS_PATH + "\\" + cardName + ".json");
            if (MonsterCard.isMonsterCard(cardName)) {
                MonsterCard monsterCard = MonsterCard.getMonsterCardByName(cardName);
                gson.toJson(monsterCard, FILE_WRITER);
            } else {
                SpellTrapCard spellTrapCard = SpellTrapCard.getSpellTrapCardByName(cardName);
                gson.toJson(spellTrapCard, FILE_WRITER);
            }
            System.out.println("card exported successfully");
        } catch (IOException e){
            System.out.println("Can not export card!");
        }
    }

    public static IAndEController getInstance(){
        if(instance == null)
            instance = new IAndEController();
        return instance;
    }
}
