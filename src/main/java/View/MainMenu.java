package View;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenu extends Menu {
    public static final HashMap<String, Pattern> PATTERN_COLLECTION;
    private static MainMenu instance = null;

    static {
        PATTERN_COLLECTION = new HashMap<>();
        PATTERN_COLLECTION.put("Menu Exit Pattern", Pattern.compile("^menu exit$"));
        PATTERN_COLLECTION.put("Show Current Menu Pattern", Pattern.compile("^menu show-current$"));
        PATTERN_COLLECTION.put("Logout Pattern", Pattern.compile("^user logout$"));
    }

    private MainMenu(Menu parentMenu) {
        super("Main Menu", parentMenu);
        subMenus.put(Pattern.compile("^menu enter duel$"), new DuelMenu(this));
        subMenus.put(Pattern.compile("^menu enter deck$"), new DeckMenu(this));
        subMenus.put(Pattern.compile("^menu enter scoreboard$"), new ScoreBoardMenu(this));
        subMenus.put(Pattern.compile("^menu enter profile$"), new ProfileMenu(this));
        subMenus.put(Pattern.compile("^menu enter shop$"), new ShopMenu(this));
        subMenus.put(Pattern.compile("^menu enter import/export$"), new IECardMenu(this));
    }

    public void show() {
        System.out.println("\033[1;92m" + "\t\tUse this Commands to Enter your desired Menu:" + "\033[0m");
        for (Map.Entry<Pattern, Menu> entry : subMenus.entrySet())
            System.out.println("\033[0;97m" + entry.getValue().name + ":\033[0m " + entry.getKey().toString().replaceAll("\\^|\\$", ""));
        System.out.println("\033[0;97m" + "Logout:\033[0m user logout");
        System.out.println("\033[1;94m" + "\t\tAdditional options:\n" + "\033[0m" +
                "\033[0;97m" + "Exit this menu:\033[0m menu exit\n" +
                "\033[0;97m" + "Show current menu:\033[0m menu show-current\n");
    }

    public void run() {
        show();
        execute(this, PATTERN_COLLECTION);
    }


    public static MainMenu getInstance(Menu parentMenu) {
        if (instance == null)
            instance = new MainMenu(parentMenu);
        return instance;
    }

    @Override
    public void menuCheck(String command, Menu currentMenu, HashMap<String, Pattern> patternCollection) {
        Matcher matcher = PATTERN_COLLECTION.get("Menu Exit Pattern").matcher(command);
        if (matcher.matches())
            this.parentMenu.run();
        else {
            matcher = PATTERN_COLLECTION.get("Logout Pattern").matcher(command);
            if (matcher.matches()) {
                this.setUsersName(null);
                this.parentMenu.run();
            } else {
                matcher = PATTERN_COLLECTION.get("Show Current Menu Pattern").matcher(command);
                if (matcher.matches()) {
                    System.out.println(this.name);
                    execute(this, PATTERN_COLLECTION);
                }
            }
        }
    }

}
