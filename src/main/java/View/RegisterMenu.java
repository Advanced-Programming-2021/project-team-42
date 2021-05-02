package View;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Controller.RegisterController;
import org.apache.commons.lang3.StringUtils;


public class RegisterMenu extends Menu {
    private static String RESPONSE;
    private static final HashMap<String, Pattern> PATTERNS_COLLECTION;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    static{
        PATTERNS_COLLECTION = new HashMap<>();
        PATTERNS_COLLECTION.put("Exit Pattern", Pattern.compile("^menu exit$"));
        PATTERNS_COLLECTION.put("Enter Menu Pattern", Pattern.compile("^menu enter (main menu|duel menu|deck menu|scoreboard menu|profile menu|shop menu|import/export menu)$"));
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
            public void show() {
            }

            @Override
            public void execute() {
                String response = RESPONSE;
                String username = null, password = null;
                Matcher matcher = Pattern.compile("--username (\\w+)").matcher(response);
                if(matcher.find())
                    username = matcher.group(1);

                matcher = Pattern.compile("--password (\\w+)").matcher(response);
                if(matcher.find())
                    password = matcher.group(1);

                RegisterController.getInstance().loginUser(username, password, this.parentMenu);
            }

            @Override
            public void run() {

            }
        };
    }

    public Menu signupMenu() {
        return new Menu("Signup Menu", this) {
            @Override
            public void show() {
            }

            @Override
            public void execute(){
                String response = RESPONSE;
                String username = null, nickname = null, password = null;

                Matcher matcher = Pattern.compile("--username (\\w+)").matcher(response);
                if(matcher.find())
                    username = matcher.group(1);
                matcher = Pattern.compile("--nickname (\\w+)").matcher(response);
                if(matcher.find())
                    nickname = matcher.group(1);
                matcher = Pattern.compile("--password (\\w+)").matcher(response);
                if(matcher.find())
                    password = matcher.group(1);

                RegisterController.getInstance().createNewUser(username, nickname, password);
                parentMenu.execute();
            }

            @Override
            public void run() {
            }
        };
    }

    public void run() {
        show();
        execute();
    }

    public void show() {
        System.out.println("Use this patterns to Login/Signup:\n" +
                "Tip: You can enter fields with dash sign (--) by any desired order!");
        System.out.println("Login: user login --username <username> --password <password>\n" +
                "Signup: user create --username <username> --nickname <nickname> --password <password>");
        System.out.println("Additional options:\n" +
                "Exit the game: menu exit\n" +
                "Enter a menu: menu enter <menu name>\n" +
                "Show current menu: menu show-current");
    }

    public void execute() {
        String response = getValidCommandPattern();
        Matcher responseMatcher;
        Menu nextMenu = null;
        for(Map.Entry<Pattern, Menu> entry : subMenus.entrySet()){
            responseMatcher = entry.getKey().matcher(response);
            if(responseMatcher.matches()) {
                RESPONSE = response;
                nextMenu = entry.getValue();
            }
        }
        if(nextMenu != null)
            nextMenu.execute();
        else {
            responseMatcher = PATTERNS_COLLECTION.get("Exit Pattern").matcher(response);
            if (responseMatcher.find())
                System.exit(1);
            else {
                responseMatcher = PATTERNS_COLLECTION.get("Enter Menu Pattern").matcher(response);
                if (responseMatcher.find())
                    System.out.println("Please login first!");
                else {
                    responseMatcher = PATTERNS_COLLECTION.get("Show Current Menu Pattern").matcher(response);
                    if (responseMatcher.find())
                        System.out.println("Register Menu");
                }
                execute();
            }
        }
    }

    public String getValidCommandPattern(){
        String command;
        Matcher matcher;
        boolean check = false;
        System.out.println("Enter your desired command:");
        do{
            command = RegisterMenu.scanner.nextLine();
            for(Map.Entry<Pattern, Menu> entry : subMenus.entrySet()){
                matcher = entry.getKey().matcher(command);
                if(matcher.matches()){
                    if(!(StringUtils.countMatches(command, "--username") > 1 ||
                            StringUtils.countMatches(command, "--password") > 1 ||
                            StringUtils.countMatches(command, "--nickname") > 1))
                        check = true;
                }
            }
            for(Map.Entry<String, Pattern> entry : PATTERNS_COLLECTION.entrySet()){
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
}
