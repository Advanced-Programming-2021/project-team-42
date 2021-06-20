package Controller;

import Model.Card;
import Model.Enums.*;
import Model.GameBoard;
import Model.MonsterCard;
import Model.SpellTrapCard;
import View.GamePlay;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

public class CardController {
    private static CardController instance = null;
    private static final String FILE_PATH = "C:\\Users\\Vision\\IdeaProjects\\Game First Phase\\src\\main\\java\\CardsData";

    private CardController() {
    }

    public static void parseCards() {
        try {
            FileReader fileReaderMonster = new FileReader(FILE_PATH + "\\Monster (2).csv");
            Iterable<CSVRecord> recordsMonster = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(fileReaderMonster);
            FileWriter FILE_WRITER;
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
                Gson gson = new GsonBuilder().create();
                FILE_WRITER = new FileWriter("C:\\Users\\Vision\\IdeaProjects\\Game First Phase\\src\\main\\java\\Database\\Cards" + "\\" + monsterCard.getName() + ".json");
                gson.toJson(monsterCard, FILE_WRITER);
                MonsterCard.addMonsterCardToList(monsterCard);
                Card.addCardToList(monsterCard);
                FILE_WRITER.close();
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
                Gson gson = new GsonBuilder().create();
                FILE_WRITER = new FileWriter("C:\\Users\\Vision\\IdeaProjects\\Game First Phase\\src\\main\\java\\Database\\Cards" + "\\" + spellTrapCard.getName() + ".json");
                gson.toJson(spellTrapCard, FILE_WRITER);
                SpellTrapCard.addSpellTrapCardToList(spellTrapCard);
                Card.addCardToList(spellTrapCard);
                FILE_WRITER.close();
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

    public void timeSealEffect(GameBoard playersBoard) {
        playersBoard.setTrapEffect(true);
    }

    public void negateAttackEffect(GamePlay gamePlay, GameBoard firstPlayersBoard, GameBoard secondPlayersBoard) {
        firstPlayersBoard.deselectAll();
        secondPlayersBoard.deselectAll();
        gamePlay.changePhase();
    }

    public void mirrorForceEffect(GameBoard playersBoard) {
        for (Map.Entry<Integer, MonsterCard> entry : playersBoard.getMonstersPlace().entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isDefensive()) {
                playersBoard.addCardToGraveyard(entry.getValue());
                playersBoard.setMonstersPlace(null, entry.getKey());
            }
        }
    }

    public void trapHoleEffect(GameBoard playersBoard, MonsterCard summonedMonster) {
        if (summonedMonster.getAttackPoint() > 1000)
            playersBoard.setMonstersPlace(null, playersBoard.getSelectedMonsterPlace());
    }

    public void torrentialTributeEffect(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard) {
        for (Map.Entry<Integer, MonsterCard> entry : secondPlayersBoard.getMonstersPlace().entrySet()) {
            if (entry.getValue() != null)
                secondPlayersBoard.addCardToGraveyard(entry.getValue());
            secondPlayersBoard.setMonstersPlace(null, entry.getKey());
        }
        for (Map.Entry<Integer, MonsterCard> entry : firstPlayersBoard.getMonstersPlace().entrySet()) {
            if (entry.getValue() != null)
                firstPlayersBoard.addCardToGraveyard(entry.getValue());
            firstPlayersBoard.setMonstersPlace(null, entry.getKey());
        }
//        firstPlayersBoard.deselectAll();
//        secondPlayersBoard.deselectAll();
    }

    public static CardController getInstance() {
        if (instance == null)
            instance = new CardController();
        return instance;
    }
}
