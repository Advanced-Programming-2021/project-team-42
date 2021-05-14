package View;

import Controller.ScoreBoardController;

import java.util.HashMap;
import java.util.regex.Pattern;

public class ScoreBoardMenu extends Menu {
    private static final HashMap<String, Pattern> PATTERNS_COLLECTION;

    static {
        PATTERNS_COLLECTION = new HashMap<>();
        PATTERNS_COLLECTION.put("Invalid Navigations Pattern", Pattern.compile("^menu enter (register|duel|deck|shop|import/export|scoreboard|profile)$"));
        PATTERNS_COLLECTION.put("Valid Navigations Pattern", Pattern.compile("^menu enter main$"));
        PATTERNS_COLLECTION.put("Current Menu Pattern", Pattern.compile("^menu show-current$"));
        PATTERNS_COLLECTION.put("Exit Menu Pattern", Pattern.compile("^menu exit$"));
    }

    public ScoreBoardMenu(Menu parentMenu) {
        super("ScoreBoard Menu", parentMenu);
        subMenus.put(Pattern.compile("scoreboard show"), this.showScores());
    }

    public Menu showScores() {
        return new Menu("Scoreboard Show", this) {
            @Override
            public void executeCommand(String command) {
                ScoreBoardController.getInstance().showScoreboard();
                parentMenu.execute(this.parentMenu, PATTERNS_COLLECTION);
            }
        };
    }

    public void show() {
        System.out.println("\033[1;92m" + "\t\tUse this patterns to Show Scoreboard:\n" + "\033[0m");
        System.out.println("\033[0;97m" + "Scoreboard:\033[0m scoreboard show");
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
