package View;

import Controller.CardController;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeckMenu extends Menu{

    private static String COMMAND;
    private static final HashMap<String, Pattern> PATTERNS_COLLECTION;
    static {
        PATTERNS_COLLECTION = new HashMap<>();
        PATTERNS_COLLECTION.put("Invalid Navigations Pattern", Pattern.compile("^menu enter (register|duel|deck|shop|import/export|scoreboard|profile)$"));
        PATTERNS_COLLECTION.put("Valid Navigations Pattern", Pattern.compile("^menu enter main$"));
        PATTERNS_COLLECTION.put("Current Menu Pattern", Pattern.compile("^menu show-current$"));
        PATTERNS_COLLECTION.put("Exit Menu Pattern", Pattern.compile("^menu exit$"));
    }

    public DeckMenu(Menu parentMenu){
        super("Deck Menu", parentMenu);
        subMenus.put(Pattern.compile("deck create"), createDeck());
        subMenus.put(Pattern.compile("deck delete"), deleteDeck());
        subMenus.put(Pattern.compile("deck set-activate"), setActive());
        subMenus.put(Pattern.compile("deck add-card"), addCard());
        subMenus.put(Pattern.compile("deck rm-card"), removeCard());
        subMenus.put(Pattern.compile("^deck show --all$"), showDecks());
        subMenus.put(Pattern.compile("deck show"), showDeck());
        subMenus.put(Pattern.compile("^deck show --cards$"), userCards());
    }

    public Menu createDeck(){
        return new Menu("Create New Deck", this) {
            @Override
            public void show() {

            }

            @Override
            public void execute() {
                String deckName = null;
                Pattern pattern = Pattern.compile("deck create (\\w+)");
                Matcher matcher = pattern.matcher(COMMAND);
                if (matcher.find())
                    deckName = matcher.group(1).trim();
                CardController.getInstance().createDeck(this.parentMenu.parentMenu.usersName,deckName);
                parentMenu.execute();
            }
        };
    }

    public Menu deleteDeck(){
        return new Menu("Delete Deck", this) {
            @Override
            public void show() {
                super.show();
            }

            @Override
            public void execute() {
                String deckName = null;
                Pattern pattern = Pattern.compile("deck delete (\\w+)");
                Matcher matcher = pattern.matcher(COMMAND);
                if (matcher.find())
                    deckName = matcher.group(1).trim();
                CardController.getInstance().deleteDeck(this.parentMenu.parentMenu.usersName,deckName);
                parentMenu.execute();
            }
        };
    }

    public Menu setActive(){
        return new Menu("Set Deck Active", this) {
            @Override
            public void show() {
                super.show();
            }

            @Override
            public void execute() {
                String deckName = null;
                Pattern pattern = Pattern.compile("deck set-active (\\w+)");
                Matcher matcher = pattern.matcher(COMMAND);
                if (matcher.find())
                    deckName = matcher.group(1).trim();
                CardController.getInstance().setActiveDeck(this.parentMenu.parentMenu.usersName,deckName);
                parentMenu.execute();
            }
        };
    }

    public Menu addCard(){
        return new Menu("Add Card To Deck", this) {
            @Override
            public void show() {
                super.show();
            }

            @Override
            public void execute() {
                String cardName = "";
                String deckName = "";
                boolean isSideDeck = false;
                Pattern pattern = Pattern.compile("--card (\\w+)");
                Matcher matcher = pattern.matcher(COMMAND);
                if (matcher.find())
                    cardName = matcher.group(1).trim();
                pattern = Pattern.compile("--deck (\\w+)");
                matcher = pattern.matcher(COMMAND);
                if (matcher.find())
                    deckName = matcher.group(1).trim();
                pattern = Pattern.compile("--side");
                matcher = pattern.matcher(COMMAND);
                if (matcher.find())
                    isSideDeck = true;
                CardController.getInstance().addCardToDeck(this.parentMenu.parentMenu.usersName,deckName,cardName,isSideDeck);
                parentMenu.execute();
            }
        };
    }

    public Menu removeCard(){
        return new Menu("Remove Card From Deck", this) {
            @Override
            public void show() {
                super.show();
            }

            @Override
            public void execute() {
                String cardName = "";
                String deckName = "";
                boolean isSideDeck = false;
                Pattern pattern = Pattern.compile("--card (\\w+)");
                Matcher matcher = pattern.matcher(COMMAND);
                if (matcher.find())
                    cardName = matcher.group(1).trim();
                pattern = Pattern.compile("--deck (\\w+)");
                matcher = pattern.matcher(COMMAND);
                if (matcher.find())
                    deckName = matcher.group(1).trim();
                pattern = Pattern.compile("--side");
                matcher = pattern.matcher(COMMAND);
                if (matcher.find())
                    isSideDeck = true;
                CardController.getInstance().removeCardFromDeck(this.parentMenu.parentMenu.usersName,deckName,cardName,isSideDeck);
                parentMenu.execute();
            }
        };
    }

    public Menu showDecks(){
        return new Menu("Show User All Decks", this) {
            @Override
            public void show() {
                super.show();
            }

            @Override
            public void execute() {
                CardController.getInstance().showAllDecks(this.parentMenu.parentMenu.usersName);
                parentMenu.execute();
            }
        };
    }

    public Menu showDeck(){
        return new Menu("Show Deck", this) {
            @Override
            public void show() {
                super.show();
            }

            @Override
            public void execute() {
                String deckName = "";
                boolean isSideDeck = false;
                Pattern pattern = Pattern.compile("--deck-name (\\w+)");
                Matcher matcher = pattern.matcher(COMMAND);
                if (matcher.find())
                    deckName = matcher.group(1).trim();
                pattern = Pattern.compile("--side");
                matcher = pattern.matcher(COMMAND);
                if (matcher.find())
                    isSideDeck = true;
                CardController.getInstance().showDeck(this.parentMenu.parentMenu.usersName,deckName , isSideDeck);
                parentMenu.execute();
            }
        };
    }

    public Menu userCards(){
        return new Menu("Show User All Cards", this) {
            @Override
            public void show() {
                super.show();
            }

            @Override
            public void execute() {
                CardController.getInstance().showUserCards(this.parentMenu.parentMenu.usersName);
                parentMenu.execute();
            }
        };
    }

    public void show(){

    }

    public void execute(){
        String command = getValidCommand();
        Matcher matcher;
        Menu nextMenu = null;
        for(Map.Entry<Pattern, Menu> entry : subMenus.entrySet()){
            matcher = entry.getKey().matcher(command);
            if(matcher.matches()){
                nextMenu = entry.getValue();
                COMMAND = command;
            }
        }
        if(nextMenu != null)
            nextMenu.execute();
        else{
            matcher = PATTERNS_COLLECTION.get("Valid Navigations Pattern").matcher(command);
            if(matcher.matches())
                this.parentMenu.run();
            else{
                matcher = PATTERNS_COLLECTION.get("Exit Menu Pattern").matcher(command);
                if(matcher.matches())
                    this.parentMenu.run();
                else{
                    matcher = PATTERNS_COLLECTION.get("Invalid Navigations Pattern").matcher(command);
                    if(matcher.matches())
                        System.out.println("Menu navigation is not possible!");
                    else{
                        matcher = PATTERNS_COLLECTION.get("Current Menu Pattern").matcher(command);
                        if(matcher.matches())
                            System.out.println(this.name);
                    }
                    execute();
                }
            }
        }
    }

    public String getValidCommand(){
        System.out.println("Enter your command:");
        String command;
        Matcher matcher;
        boolean check = false;
        do{
            command = Menu.scanner.nextLine();
            for(Map.Entry<Pattern, Menu> entry : subMenus.entrySet()){
                matcher = entry.getKey().matcher(command);
                if(matcher.matches()){
                        check = true;
                }
            }
            for(Map.Entry<String , Pattern> entry : PATTERNS_COLLECTION.entrySet()){
                matcher = entry.getValue().matcher(command);
                if(matcher.matches())
                    check = true;
            }
            if(!check)
                System.out.println("invalid command\n" +
                        "Try Again!");
        }while (!check);
        return command;
    }

    public void run(){
        show();
        execute();
    }
}
