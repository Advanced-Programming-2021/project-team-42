package View;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenu extends Menu {
    private static final HashMap<String, Pattern> PATTERN_COLLECTION;

    static {
        PATTERN_COLLECTION = new HashMap<>();
        PATTERN_COLLECTION.put("Menu Exit Pattern", Pattern.compile("^menu exit$"));
        PATTERN_COLLECTION.put("Show Current Menu Pattern", Pattern.compile("^menu show-current$"));
        PATTERN_COLLECTION.put("Logout Pattern", Pattern.compile("user logout"));
    }

    public MainMenu(Menu parentMenu) {
        super("Main Menu", parentMenu);
        subMenus.put(Pattern.compile("^menu enter duel$"), new DuelMenu(this));
        subMenus.put(Pattern.compile("^menu enter deck$"), new DeckMenu(this));
        subMenus.put(Pattern.compile("^menu enter scoreboard$"), new ScoreBoardMenu(this));
        subMenus.put(Pattern.compile("^menu enter profile$"), new ProfileMenu(this));
        subMenus.put(Pattern.compile("^menu enter shop$"), new ShopMenu(this));
        subMenus.put(Pattern.compile("^menu enter import/export$"), new IECardMenu(this));
    }

    public void show() {
        System.out.println("\033[1;92m" + "\t\tUse this Commands to Enter your desired Menu:" + ":\033[0m");
        for (Map.Entry<Pattern, Menu> entry : subMenus.entrySet())
            System.out.println("\033[0;97m" + entry.getValue().name + ":\033[0m " + entry.getKey().toString().replaceAll("\\^|\\$", ""));
        System.out.println("\033[0;97m" + "Logout: user logout" + ":\033[0m");
        System.out.println("\033[1;94m" + "\t\tAdditional options:\n" + "\033[0m" +
                "\033[0;97m" + "Exit this menu:\033[0m menu exit\n" +
                "\033[0;97m" + "Show current menu:\033[0m menu show-current\n");
    }

    public void execute() {
        String command = getValidCommand();
        Matcher commandMatcher;
        Menu nextMenu;
        for (Map.Entry<Pattern, Menu> entry : subMenus.entrySet()) {
            commandMatcher = entry.getKey().matcher(command);
            if (commandMatcher.matches()) {
                nextMenu = entry.getValue();
                nextMenu.run();
            }
        }
        commandMatcher = PATTERN_COLLECTION.get("Menu Exit Pattern").matcher(command);
        if (commandMatcher.matches()) {
            nextMenu = this.parentMenu;
            nextMenu.run();
        } else {
            commandMatcher = PATTERN_COLLECTION.get("Logout Pattern").matcher(command);
            if (commandMatcher.matches()) {
                nextMenu = this.parentMenu;
                nextMenu.run();
            } else {
                commandMatcher = PATTERN_COLLECTION.get("Show Current Menu Pattern").matcher(command);
                if (commandMatcher.matches()) {
                    System.out.println(this.name);
                    execute();
                }
            }
        }
    }

    public void run() {
        show();
        execute();
    }

    public String getValidCommand() {
        String command;
        Matcher matcher;
        boolean check = false;
        System.out.println("Enter your desired command:");
        do {
            command = Menu.scanner.nextLine();
            for (Map.Entry<Pattern, Menu> entry : subMenus.entrySet()) {
                matcher = entry.getKey().matcher(command);
                if (matcher.matches())
                    check = true;
            }
            for (Map.Entry<String , Pattern> entry : PATTERN_COLLECTION.entrySet()) {
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

}
