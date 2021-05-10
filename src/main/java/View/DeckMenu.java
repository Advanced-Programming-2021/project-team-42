package View;

import Controller.DeckController;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeckMenu extends Menu {

    private static String COMMAND;
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
        subMenus.put(Pattern.compile("^deck add-card --(card|deck) (\\w+) --(deck|card) (\\w+)( --side)?$"), addCard());
        subMenus.put(Pattern.compile("^deck rm-card --(card|deck) (\\w+) --(deck|card) (\\w+)( --side)?$"), removeCard());
        subMenus.put(Pattern.compile("^deck show --all$"), showDecks());
        subMenus.put(Pattern.compile("^deck show --deck-name (\\w+)( --side)?$"), showDeck());
        subMenus.put(Pattern.compile("^deck show --cards$"), userCards());
    }

    public Menu createDeck() {
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
                DeckController.getInstance().createDeck(this.parentMenu.parentMenu.usersName, deckName);
                parentMenu.execute();
            }
        };
    }

    public Menu deleteDeck() {
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
                DeckController.getInstance().deleteDeck(this.parentMenu.parentMenu.usersName, deckName);
                parentMenu.execute();
            }
        };
    }

    public Menu setActive() {
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
                DeckController.getInstance().setActiveDeck(this.parentMenu.parentMenu.usersName, deckName);
                parentMenu.execute();
            }
        };
    }

    public Menu addCard() {
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
                DeckController.getInstance().addCardToDeck(this.parentMenu.parentMenu.usersName, deckName, cardName, isSideDeck);
                parentMenu.execute();
            }
        };
    }

    public Menu removeCard() {
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
                DeckController.getInstance().removeCardFromDeck(this.parentMenu.parentMenu.usersName, deckName, cardName, isSideDeck);
                parentMenu.execute();
            }
        };
    }

    public Menu showDecks() {
        return new Menu("Show User All Decks", this) {
            @Override
            public void show() {
                super.show();
            }

            @Override
            public void execute() {
                DeckController.getInstance().showAllDecks(this.parentMenu.parentMenu.usersName);
                parentMenu.execute();
            }
        };
    }

    public Menu showDeck() {
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
                DeckController.getInstance().showDeck(deckName, isSideDeck);
                parentMenu.execute();
            }
        };
    }

    public Menu userCards() {
        return new Menu("Show User All Cards", this) {
            @Override
            public void show() {
                super.show();
            }

            @Override
            public void execute() {
                DeckController.getInstance().showUserCards(this.parentMenu.parentMenu.usersName);
                parentMenu.execute();
            }
        };
    }

    public void show() {
        System.out.println("\033[1;92m" + "\t\tUse this patterns to manage Decks:\n" + "\033[0m" +
                "\033[4;31m" + "Tip:\033[0m You can enter fields with dash sign (--) by any desired order!\n");
        System.out.println("\033[0;97m" + "Create New Deck:\033[0m deck create <deck name>\n" +
                "\033[0;97m" + "Delete Deck:\033[0m deck delete <deck name>" +
                "\033[0;97m" + "Set Deck Active:\033[0m deck set-activate <deck name>" +
                "\033[0;97m" + "Add Card To Deck:\033[0m deck add-card --card <card name> --deck <deck name> --side(optional)" +
                "\033[0;97m" + "Remove Card From Deck:\033[0m deck rm-card --card <card name> --deck <deck name> --side(optional)" +
                "\033[0;97m" + "Show User All Decks:\033[0m deck show --all" +
                "\033[0;97m" + "Show Deck:\033[0m deck show --deck-name <deck name> --side(Opt)");
        System.out.println("\033[1;94m" + "\t\tAdditional options:\n" + "\033[0m" +
                "\033[0;97m" + "Exit the game:\033[0m menu exit\n" +
                "\033[0;97m" + "Enter a menu:\033[0m menu enter <menu name>\n" +
                "\033[0;97m" + "Show current menu:\033[0m menu show-current\n");
    }

    public void execute() {
        String command = getValidCommand();
        Matcher matcher;
        Menu nextMenu = null;
        for (Map.Entry<Pattern, Menu> entry : subMenus.entrySet()) {
            matcher = entry.getKey().matcher(command);
            if (matcher.matches()) {
                nextMenu = entry.getValue();
                COMMAND = command;
            }
        }
        if (nextMenu != null)
            nextMenu.execute();
        else {
            matcher = PATTERNS_COLLECTION.get("Valid Navigations Pattern").matcher(command);
            if (matcher.matches())
                this.parentMenu.run();
            else {
                matcher = PATTERNS_COLLECTION.get("Exit Menu Pattern").matcher(command);
                if (matcher.matches())
                    this.parentMenu.run();
                else {
                    matcher = PATTERNS_COLLECTION.get("Invalid Navigations Pattern").matcher(command);
                    if (matcher.matches())
                        System.out.println("Menu navigation is not possible!");
                    else {
                        matcher = PATTERNS_COLLECTION.get("Current Menu Pattern").matcher(command);
                        if (matcher.matches())
                            System.out.println(this.name);
                    }
                    execute();
                }
            }
        }
    }

    public String getValidCommand() {
        System.out.println("Enter your command:");
        String command;
        Matcher matcher;
        boolean check = false;
        do {
            command = Menu.scanner.nextLine();
            for (Map.Entry<Pattern, Menu> entry : subMenus.entrySet()) {
                matcher = entry.getKey().matcher(command);
                if (matcher.matches()) {
                    if (!(StringUtils.countMatches(command, "--deck") > 1 ||
                            StringUtils.countMatches(command, "--card") > 1))
                        check = true;
                }
            }
            for (Map.Entry<String, Pattern> entry : PATTERNS_COLLECTION.entrySet()) {
                matcher = entry.getValue().matcher(command);
                if (matcher.matches())
                    check = true;
            }
            if (!check)
                System.out.println("invalid command\n" +
                        "Try Again!");
        } while (!check);
        return command;
    }

    public void run() {
        show();
        execute();
    }
}
