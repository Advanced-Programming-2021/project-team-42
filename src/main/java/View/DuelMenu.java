package View;

import Controller.DuelController;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DuelMenu extends Menu {
    private static String COMMAND;
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
            public void show() {
            }

            @Override
            public void execute() {
                String secondPlayerName = null, rounds = null;
                Matcher matcher = Pattern.compile("--second-player (\\w+)").matcher(COMMAND);
                if(matcher.find())
                    secondPlayerName = matcher.group(1);
                matcher = Pattern.compile("--rounds (\\w+)").matcher(COMMAND);
                if(matcher.find())
                    rounds = matcher.group(1);

                DuelController.getInstance().startNewDuel(this.parentMenu.parentMenu.usersName, secondPlayerName, rounds);

            }
        };
    }

    public void run() {
        show();
        execute();
    }

    public void show() {
        System.out.println("\033[1;92m" + "\t\tUse this pattern to start new duel:\n" + "\033[0m" +
                "\033[0;97m" + "Start new Duel:\033[0m export card <cardname>");
        System.out.println("\033[1;94m" + "\t\tAdditional options:\n" + "\033[0m" +
                "\033[0;97m" + "Exit the game:\033[0m menu exit\n" +
                "\033[0;97m" + "Enter a menu:\033[0m menu enter <menu name>\n" +
                "\033[0;97m" + "Show current menu:\033[0m menu show-current\n");
    }

    public void execute() {
        String command = getValidCommand();
        Matcher matcher;
        Menu nextMenu = null;
        for(Map.Entry<Pattern, Menu> entry : subMenus.entrySet()){
            matcher = entry.getKey().matcher(command);
            if(matcher.matches()) {
                COMMAND = command;
                nextMenu = entry.getValue();
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

    public String getValidCommand() {
        System.out.println("Enter your desired command:");
        String command;
        Matcher matcher;
        boolean check = false;
        do {
            command = Menu.scanner.nextLine();
            for (Map.Entry<Pattern, Menu> entry : subMenus.entrySet()) {
                matcher = entry.getKey().matcher(command);
                if (matcher.matches()) {
                    if (!(StringUtils.countMatches(command, "--rounds") > 1 ||
                            StringUtils.countMatches(command, "--second-player") > 1))
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
}
