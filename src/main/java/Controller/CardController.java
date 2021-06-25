package Controller;

import Model.Card;
import Model.Enums.*;
import Model.MonsterCard;
import Model.SpellTrapCard;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;

public class CardController {
    private static CardController instance = null;
    private static final String FILE_PATH = "src\\main\\java\\CardsData";

    private CardController() {
    }

    public static void parseCards() {
        try {
            FileReader fileReaderMonster = new FileReader(FILE_PATH + "\\Monster (2).csv");
            Iterable<CSVRecord> recordsMonster = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(fileReaderMonster);
            for (CSVRecord record : recordsMonster) {
                String name = record.get("Name");
                int level = Integer.parseInt(record.get("Level"));
                Attribute attribute = Attribute.valueOf(record.get("Attribute").toUpperCase());
                MonsterType monsterType = MonsterType.valueOf(record.get("Monster Type").replaceAll(" ", "_").
                        toUpperCase());
                CardType cardType = CardType.valueOf(record.get("Card Type").toUpperCase());
                int attackPoint = Integer.parseInt(record.get("Attack Point"));
                int defencePoint = Integer.parseInt(record.get("Defence Point"));
                String description = record.get("Description");
                int price = Integer.parseInt(record.get("Price"));
                MonsterCard monsterCard = new MonsterCard(name, description, attribute, monsterType,
                        cardType, attackPoint, defencePoint, price, level);
                MonsterCard.addMonsterCardToList(monsterCard);
                Card.addCardToList(monsterCard);
            }
            FileReader fileReaderSpellTrap = new FileReader(FILE_PATH + "\\SpellTrap (1).csv");
            Iterable<CSVRecord> recordsSpellTrap = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(fileReaderSpellTrap);
            for (CSVRecord record : recordsSpellTrap) {
                String name = record.get("Name");
                String type = record.get("Type");
                Icon icon = Icon.valueOf(record.get("Icon").replaceAll("-", "_").
                        toUpperCase());
                String description = record.get("Description");
                Status status = Status.valueOf(record.get("Status").toUpperCase());
                int price = Integer.parseInt(record.get("Price"));
                SpellTrapCard spellTrapCard = new SpellTrapCard(name, description, type + " Card",
                        icon, status, price);
                SpellTrapCard.addSpellTrapCardToList(spellTrapCard);
                Card.addCardToList(spellTrapCard);
            }
        } catch (Exception e) {
            System.out.println("An Error occurred");
        }
    }

    public String showCard(String cardName) throws Exception {
        if (Card.getCardByName(cardName) == null)
            throw new Exception("there is no card with given name");
        else {
            if (MonsterCard.isMonsterCard(cardName))
                return MonsterCard.getMonsterCardByName(cardName).exclusiveToString();
            else
                return SpellTrapCard.getSpellTrapCardByName(cardName).exclusiveToString();
        }
    }

    public static CardController getInstance() {
        if (instance == null)
            instance = new CardController();
        return instance;
    }
}
