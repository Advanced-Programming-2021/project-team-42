package View;

import Server.Controller.CardController;
import Server.Controller.UserController;
import Server.Model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Menu {
    protected String name;
    protected String usersName;
    Menu parentMenu;
    protected HashMap<Pattern, Menu> subMenus;
    static Scanner scanner;

    public Menu(String name, Menu parentMenu) {
        this.name = name;
        this.parentMenu = parentMenu;
        subMenus = new HashMap<>();
    }

    public static void setScanner(Scanner scanner) {
        Menu.scanner = scanner;
    }

    public void setUsersName(String usersName) {
        this.usersName = usersName;
    }

    public void show() {
    }

    public void execute(Menu currentMenu, HashMap<String, Pattern> patternsCollection) {
        String command = getValidCommand(currentMenu, patternsCollection);
        Matcher matcher;
        Menu nextMenu = null;
        for (Map.Entry<Pattern, Menu> entry : currentMenu.subMenus.entrySet()) {
            matcher = entry.getKey().matcher(command);
            if (matcher.matches())
                nextMenu = entry.getValue();
        }
        if (nextMenu != null) {
            nextMenu.executeCommand(command);
        } else
            currentMenu.menuCheck(command, currentMenu, patternsCollection);
    }

    public void run() {
    }

    public void execute() {
    }

    public void executeCommand(String command) {
        for (Map.Entry<Pattern, Menu> entry : MainMenu.getInstance(null).subMenus.entrySet()) {
            if (entry.getKey().matcher(command).matches())
                entry.getValue().run();
        }
    }

    public String getValidCommand(Menu currentMenu, HashMap<String, Pattern> patternsCollection) {
        System.out.println("Enter your desired command:");
        String command;
        Matcher matcher;
        boolean check = false;
        do {
            command = Menu.scanner.nextLine();
            for (Map.Entry<Pattern, Menu> entry : currentMenu.subMenus.entrySet()) {
                matcher = entry.getKey().matcher(command);
                if (matcher.matches()) {
                    check = true;
                }
            }
            for (Map.Entry<String, Pattern> entry : patternsCollection.entrySet()) {
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

    public void menuCheck(String command, Menu currentMenu, HashMap<String, Pattern> patternCollection) {
        Matcher matcher;
        matcher = patternCollection.get("Valid Navigations Pattern").matcher(command);
        if (matcher.matches())
            currentMenu.parentMenu.run();
        else {
            matcher = patternCollection.get("Exit Menu Pattern").matcher(command);
            if (matcher.matches())
                currentMenu.parentMenu.run();
            else {
                matcher = patternCollection.get("Invalid Navigations Pattern").matcher(command);
                if (matcher.matches())
                    System.out.println("Menu navigation is not possible!");
                else {
                    matcher = patternCollection.get("Current Menu Pattern").matcher(command);
                    if (matcher.matches())
                        System.out.println(currentMenu.name);
                    if (patternCollection.containsKey("Increase Money")) {
                        matcher = patternCollection.get("Increase Money").matcher(command);
                        if (matcher.matches()) {
                            UserController.getInstance().increaseMoney(User.getUserByUsername(usersName),
                                    Integer.parseInt(matcher.group(1)));
                            System.out.println("Done! :))");
                        }
                    }
                }
                currentMenu.execute(currentMenu, patternCollection);
            }
        }
    }

    public void showCard(String command) {
        String cardName = null;
        Pattern pattern = Pattern.compile("^card show ([A-Za-z0-9 ]+)$");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find())
            cardName = matcher.group(1);

        try {
            String result = CardController.getInstance().showCard(cardName);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
