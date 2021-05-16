package View;

import Controller.DuelController;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DuelMenu extends Menu {
    private static final HashMap<String, Pattern> PATTERNS_COLLECTION;

    static {
        PATTERNS_COLLECTION = new HashMap<>();
        PATTERNS_COLLECTION.put("Invalid Navigations Pattern", Pattern.compile("^menu enter (register|duel|deck|shop|import/export|scoreboard|profile)$"));
        PATTERNS_COLLECTION.put("Valid Navigations Pattern", Pattern.compile("^menu enter main$"));
        PATTERNS_COLLECTION.put("Current Menu Pattern", Pattern.compile("^menu show-current$"));
        PATTERNS_COLLECTION.put("Exit Menu Pattern", Pattern.compile("^menu exit$"));
    }

    public DuelMenu(Menu parentMenu) {
        super("Duel Menu", parentMenu);
        subMenus.put(Pattern.compile("duel --new --(second-player|rounds) (\\w+) --(rounds|second-player) (\\w+)"), newGameMenu());
    }

    public Menu newGameMenu() {
        return new Menu("New Duel Menu", this) {
            @Override
            public void executeCommand(String command) {
                String secondPlayerName = null, rounds = null;
                Matcher matcher = Pattern.compile("--second-player (\\w+)").matcher(command);
                if(matcher.find())
                    secondPlayerName = matcher.group(1);
                matcher = Pattern.compile("--rounds (\\w+)").matcher(command);
                if(matcher.find())
                    rounds = matcher.group(1);

                if(secondPlayerName != null && rounds != null)
                    DuelController.getInstance().startNewDuel(this.parentMenu.parentMenu.usersName, secondPlayerName, rounds, this.parentMenu);
                else
                    System.out.println("invalid command!");

                parentMenu.execute(this.parentMenu, PATTERNS_COLLECTION);
            }
        };
    }

    public void run() {
        show();
        execute(this, PATTERNS_COLLECTION);
    }

    public void show() {
        System.out.println("\033[1;92m" + "\t\tUse this pattern to start new duel:\n" + "\033[0m" +
                "\033[0;97m" + "Start new Duel:\033[0m duel --new --second-player <player2 username> --rounds <1/3>");
        System.out.println("\033[1;94m" + "\t\tAdditional options:\n" + "\033[0m" +
                "\033[0;97m" + "Exit the game:\033[0m menu exit\n" +
                "\033[0;97m" + "Enter a menu:\033[0m menu enter <menu name>\n" +
                "\033[0;97m" + "Show current menu:\033[0m menu show-current\n");
    }

}
