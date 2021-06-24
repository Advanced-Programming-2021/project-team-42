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
    private static final String EXPORTED_CARDS_PATH = "src\\main\\java\\Database\\ExportedCards";
    private static final String IMPORTED_CARDS_PATH = "src\\main\\java\\Database\\ImportedCards";

    private IAndEController() {
    }

    public void importCard(String cardName) throws IOException {
        Gson gson = new Gson();
        FileReader FILE_READER = new FileReader(IMPORTED_CARDS_PATH + "\\" + cardName + ".json");
        if (MonsterCard.isMonsterCard(cardName)) {
            MonsterCard monsterCard = gson.fromJson(FILE_READER, MonsterCard.class);
            Card.addCardToList(monsterCard);
            MonsterCard.addMonsterCardToList(monsterCard);
        } else {
            SpellTrapCard spellTrapCard = gson.fromJson(FILE_READER, SpellTrapCard.class);
            Card.addCardToList(spellTrapCard);
            SpellTrapCard.addSpellTrapCardToList(spellTrapCard);
        }
        FILE_READER.close();
    }

    public void exportCard(String cardName) throws Exception {
        if (Card.getCardByName(cardName) == null)
            throw new Exception("there is no card with given name!");
        else {
            Gson gson = new Gson();
            FileWriter FILE_WRITER = new FileWriter(EXPORTED_CARDS_PATH + "\\" + cardName + ".json");
            if (MonsterCard.isMonsterCard(cardName)) {
                MonsterCard monsterCard = MonsterCard.getMonsterCardByName(cardName);
                gson.toJson(monsterCard, FILE_WRITER);
            } else {
                SpellTrapCard spellTrapCard = SpellTrapCard.getSpellTrapCardByName(cardName);
                gson.toJson(spellTrapCard, FILE_WRITER);
            }
            FILE_WRITER.close();

        }
        System.out.println("Can not export card!");

    }

    public static IAndEController getInstance() {
        if (instance == null)
            instance = new IAndEController();
        return instance;
    }
}
