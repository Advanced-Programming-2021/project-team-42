package View;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Controller.RegisterController;


public class RegisterMenu extends Menu {
    private static final HashMap<String, Pattern> PATTERNS_COLLECTION;

    static {
        PATTERNS_COLLECTION = new HashMap<>();
        PATTERNS_COLLECTION.put("Exit Pattern", Pattern.compile("^menu exit$"));
        PATTERNS_COLLECTION.put("Enter Menu Pattern", Pattern.compile("^menu enter (main|duel|deck|scoreboard|profile|shop|import/export)$"));
        PATTERNS_COLLECTION.put("Show Current Menu Pattern", Pattern.compile("^menu show-current$"));
    }

    public RegisterMenu(Menu parentMenu) {
        super("Register Menu", parentMenu);
        subMenus.put(Pattern.compile("^user login --(username|password) (\\w+) --(username|password) (\\w+)$"), loginMenu());
        subMenus.put(Pattern.compile("^user create --(username|nickname|password) (\\w+) --(username|nickname|password) (\\w+) --(username|nickname|password) (\\w+)$"), signupMenu());
    }

    public Menu loginMenu() {
        return new Menu("Login Menu", this) {
            @Override
            public void executeCommand(String command) {
                String username = null, password = null;
                Matcher matcher = Pattern.compile("--username (\\w+)").matcher(command);
                if (matcher.find())
                    username = matcher.group(1);
                matcher = Pattern.compile("--password (\\w+)").matcher(command);
                if (matcher.find())
                    password = matcher.group(1);

                if (username != null && password != null)
                    RegisterController.getInstance().loginUser(username, password, this.parentMenu);
                else
                    System.out.println("invalid command!");

                parentMenu.execute(this.parentMenu, PATTERNS_COLLECTION);
            }

        };
    }

    public Menu signupMenu() {
        return new Menu("Signup Menu", this) {
            @Override
            public void executeCommand(String command) {
                String username = null, nickname = null, password = null;

                Matcher matcher = Pattern.compile("--username (\\w+)").matcher(command);
                if (matcher.find())
                    username = matcher.group(1);
                matcher = Pattern.compile("--nickname (\\w+)").matcher(command);
                if (matcher.find())
                    nickname = matcher.group(1);
                matcher = Pattern.compile("--password (\\w+)").matcher(command);
                if (matcher.find())
                    password = matcher.group(1);

                if (username != null && nickname != null && password != null)
                    RegisterController.getInstance().createNewUser(username, nickname, password);
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

    @Override
    public void show() {
        System.out.println("\033[1;92m" + "\t\tUse this patterns to Login/Signup:\n" + "\033[0m" +
                "\033[4;31m" + "Tip:\033[0m You can enter fields with dash sign (--) by any desired order!\n");
        System.out.println("\033[0;97m" + "Login:\033[0m user login --username <username> --password <password>\n" +
                "\033[0;97m" + "Signup:\033[0m user create --username <username> --nickname <nickname> --password <password>");
        System.out.println("\033[1;94m" + "\t\tAdditional options:\n" + "\033[0m" +
                "\033[0;97m" + "Exit the game:\033[0m menu exit\n" +
                "\033[0;97m" + "Enter a menu:\033[0m menu enter <menu name>\n" +
                "\033[0;97m" + "Show current menu:\033[0m menu show-current\n");
    }

    @Override
    public void menuCheck(String command, Menu currentMenu, HashMap<String, Pattern> patternCollection) {
        Matcher matcher = PATTERNS_COLLECTION.get("Exit Pattern").matcher(command);
        if (matcher.find())
            System.exit(0);
        else {
            matcher = PATTERNS_COLLECTION.get("Enter Menu Pattern").matcher(command);
            if (matcher.find()) {
                if (MainMenu.getInstance(this).usersName == null)
                    System.out.println("Please login first!");
                else
                    MainMenu.getInstance(this).run();
            } else {
                matcher = PATTERNS_COLLECTION.get("Show Current Menu Pattern").matcher(command);
                if (matcher.find())
                    System.out.println(this.name);
            }
            execute(this, PATTERNS_COLLECTION);
        }
    }

}
