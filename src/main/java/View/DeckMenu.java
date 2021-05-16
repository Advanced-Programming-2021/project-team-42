package View;

import Controller.DeckController;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeckMenu extends Menu {

    private static final HashMap<String, Pattern> PATTERNS_COLLECTION;

    static {
        PATTERNS_COLLECTION = new HashMap<>();
        PATTERNS_COLLECTION.put("Invalid Navigations Pattern", Pattern.compile("^menu enter (register|duel|deck|shop|import/export|scoreboard|profile)$"));
        PATTERNS_COLLECTION.put("Valid Navigations Pattern", Pattern.compile("^menu enter main$"));
        PATTERNS_COLLECTION.put("Current Menu Pattern", Pattern.compile("^menu show-current$"));
        PATTERNS_COLLECTION.put("Exit Menu Pattern", Pattern.compile("^menu exit$"));
    }

    public DeckMenu(Menu parentMenu) {
        super("Deck Menu", parentMenu);
        subMenus.put(Pattern.compile("^deck create (\\w+)$"), createDeck());
        subMenus.put(Pattern.compile("^deck delete (\\w+)$"), deleteDeck());
        subMenus.put(Pattern.compile("^deck set-activate (\\w+)$"), setActive());
        subMenus.put(Pattern.compile("^deck add-card --(card|deck) ([A-Za-z0-9 ]+) --(deck|card) ([A-Za-z0-9 ]+)( --side)?$"), addCard());
        subMenus.put(Pattern.compile("^deck rm-card --(card|deck) ([A-Za-z0-9 ]+) --(deck|card) ([A-Za-z0-9 ]+)( --side)?$"), removeCard());
        subMenus.put(Pattern.compile("^deck show --all$"), showDecks());
        subMenus.put(Pattern.compile("^deck show --deck-name (\\w+)( --side)?$"), showDeck());
        subMenus.put(Pattern.compile("^deck show --cards$"), userCards());
    }

    public Menu createDeck() {
        return new Menu("Create New Deck", this) {
            @Override
            public void executeCommand(String command) {
                String deckName = null;
                Pattern pattern = Pattern.compile("deck create (\\w+)");
                Matcher matcher = pattern.matcher(command);
                if (matcher.find())
                    deckName = matcher.group(1).trim();
                DeckController.getInstance().createDeck(this.parentMenu.parentMenu.usersName, deckName);
                parentMenu.execute(this.parentMenu, PATTERNS_COLLECTION);
            }
        };
    }

    public Menu deleteDeck() {
        return new Menu("Delete Deck", this) {
            @Override
            public void executeCommand(String command) {
                String deckName = null;
                Pattern pattern = Pattern.compile("deck delete (\\w+)");
                Matcher matcher = pattern.matcher(command);
                if (matcher.find())
                    deckName = matcher.group(1).trim();
                DeckController.getInstance().deleteDeck(this.parentMenu.parentMenu.usersName, deckName);
                parentMenu.execute(this.parentMenu, PATTERNS_COLLECTION);
            }
        };
    }

    public Menu setActive() {
        return new Menu("Set Deck Active", this) {
            @Override
            public void executeCommand(String command) {
                String deckName = null;
                Pattern pattern = Pattern.compile("deck set-activate (\\w+)");
                Matcher matcher = pattern.matcher(command);
                if (matcher.find())
                    deckName = matcher.group(1).trim();
                DeckController.getInstance().setActiveDeck(this.parentMenu.parentMenu.usersName, deckName);
                parentMenu.execute(this.parentMenu, PATTERNS_COLLECTION);
            }
        };
    }

    public Menu addCard() {
        return new Menu("Add Card To Deck", this) {
            @Override
            public void executeCommand(String command) {
                processingCard(command, 1);
            }
        };
    }

    public Menu removeCard() {
        return new Menu("Remove Card From Deck", this) {
            @Override
            public void executeCommand(String command) {
                processingCard(command, 0);
            }

        };
    }

    private void processingCard(String command, int flag) {
        String cardName = null, deckName = null;
        boolean isSideDeck = false;
        Pattern pattern = Pattern.compile("--card ([A-Za-z ]+)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find())
            cardName = matcher.group(1).trim();
        pattern = Pattern.compile("--deck ([A-Za-z0-9 ]+)");
        matcher = pattern.matcher(command);
        if (matcher.find())
            deckName = matcher.group(1).trim();
        pattern = Pattern.compile("--side");
        matcher = pattern.matcher(command);
        if (matcher.find())
            isSideDeck = true;
        if(cardName != null && deckName != null) {
            if (flag == 0)
                DeckController.getInstance().removeCardFromDeck(this.parentMenu.usersName, deckName, cardName, isSideDeck);
            else
                DeckController.getInstance().addCardToDeck(this.parentMenu.usersName, deckName, cardName, isSideDeck);
        }
        else
            System.out.println("invalid command!");
        this.execute(this, PATTERNS_COLLECTION);
    }


    public Menu showDecks() {
        return new Menu("Show User All Decks", this) {
            @Override
            public void executeCommand(String command) {
                DeckController.getInstance().showAllDecks(this.parentMenu.parentMenu.usersName);
                parentMenu.execute(this.parentMenu, PATTERNS_COLLECTION);
            }
        };
    }

    public Menu showDeck() {
        return new Menu("Show Deck", this) {
            @Override
            public void executeCommand(String command) {
                String deckName = null;
                boolean isSideDeck = false;
                Pattern pattern = Pattern.compile("--deck-name (\\w+)");
                Matcher matcher = pattern.matcher(command);
                if (matcher.find())
                    deckName = matcher.group(1).trim();
                pattern = Pattern.compile("--side");
                matcher = pattern.matcher(command);
                if (matcher.find())
                    isSideDeck = true;
                DeckController.getInstance().showDeck(this.parentMenu.parentMenu.usersName,deckName, isSideDeck);
                parentMenu.execute(this.parentMenu, PATTERNS_COLLECTION);
            }
        };
    }

    public Menu userCards() {
        return new Menu("Show User All Cards", this) {
            @Override
            public void executeCommand(String command) {
                DeckController.getInstance().showUserCards(this.parentMenu.parentMenu.usersName);
                parentMenu.execute(this.parentMenu, PATTERNS_COLLECTION);
            }
        };
    }

    @Override
    public void show() {
        System.out.println("\033[1;92m" + "\t\tUse this patterns to manage Decks:\n" + "\033[0m" +
                "\033[4;31m" + "Tip:\033[0m You can enter fields with dash sign (--) by any desired order!\n");
        System.out.println("\033[0;97m" + "Create New Deck:\033[0m deck create <deck name>\n" +
                "\033[0;97m" + "Delete Deck:\033[0m deck delete <deck name>\n" +
                "\033[0;97m" + "Set Deck Active:\033[0m deck set-activate <deck name>\n" +
                "\033[0;97m" + "Add Card To Deck:\033[0m deck add-card --card <card name> --deck <deck name> --side(optional)\n" +
                "\033[0;97m" + "Remove Card From Deck:\033[0m deck rm-card --card <card name> --deck <deck name> --side(optional)\n" +
                "\033[0;97m" + "Show User All Decks:\033[0m deck show --all\n" +
                "\033[0;97m" + "Show User Cards:\033[0m deck show --cards\n" +
                "\033[0;97m" + "Show Deck:\033[0m deck show --deck-name <deck name> --side(Opt)\n");
        System.out.println("\033[1;94m" + "\t\tAdditional options:\n" + "\033[0m" +
                "\033[0;97m" + "Exit the game:\033[0m menu exit\n" +
                "\033[0;97m" + "Enter a menu:\033[0m menu enter <menu name>\n" +
                "\033[0;97m" + "Show current menu:\033[0m menu show-current\n");
    }

    public void run() {
        show();
        execute(this, PATTERNS_COLLECTION);
    }
}
