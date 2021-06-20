package View;

import Controller.CardController;
import Controller.ShopController;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopMenu extends Menu {
    private static final HashMap<String, Pattern> PATTERNS_COLLECTION;

    static {
        PATTERNS_COLLECTION = new HashMap<>();
        PATTERNS_COLLECTION.put("Increase Money", Pattern.compile("^increase --money (\\d+)$"));
        PATTERNS_COLLECTION.put("Invalid Navigations Pattern", Pattern.compile("^menu enter (register|duel|deck|shop|import/export|scoreboard|profile)$"));
        PATTERNS_COLLECTION.put("Valid Navigations Pattern", Pattern.compile("^menu enter main$"));
        PATTERNS_COLLECTION.put("Current Menu Pattern", Pattern.compile("^menu show-current$"));
        PATTERNS_COLLECTION.put("Exit Menu Pattern", Pattern.compile("^menu exit$"));
    }

    public ShopMenu(Menu parentMenu) {
        super("Shop Menu", parentMenu);
        subMenus.put(Pattern.compile("^shop buy ([a-zA-Z ]+)$"), buyItem());
        subMenus.put(Pattern.compile("^shop show --all$"), showCards());
        subMenus.put(Pattern.compile("^card show ([A-Za-z0-9 ]+)$"), showSpecificCard());
    }

    public Menu buyItem() {
        return new Menu("Buy Card", this) {
            @Override
            public void executeCommand(String command) {
                String cardName = null;
                Pattern pattern = Pattern.compile("shop buy ([a-zA-Z ]+)");
                Matcher matcher = pattern.matcher(command);
                if(matcher.find())
                    cardName = matcher.group(1);

                try {
                    ShopController.getInstance().buyCard(this.parentMenu.parentMenu.usersName, cardName);
                    System.out.println("card " + cardName + " added to your cards successfully");
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }

                parentMenu.execute(this.parentMenu, PATTERNS_COLLECTION);
            }
        };
    }

    public Menu showCards() {
        return new Menu("Show Shop All Cards", this) {
            @Override
            public void executeCommand(String command) {
                System.out.println(ShopController.getInstance().showAllCards());

                parentMenu.execute(this.parentMenu, PATTERNS_COLLECTION);
            }
        };
    }

    public Menu showSpecificCard(){
        return new Menu("Show Specific Card", this) {
            @Override
            public void executeCommand(String command) {
                showCard(command);
                this.parentMenu.execute();
            }
        };
    }

    public void run() {
        show();
        execute(this, PATTERNS_COLLECTION);
    }

    public void show() {
        System.out.println("\033[1;92m" + "\t\tUse this Patterns to Enter your desired Menu:" + "\033[0m\n");
        System.out.println("\033[0;97m" + "Buy Card:\033[0m shop buy <card name>\n" +
                "\033[0;97m" + "Show all Cards:\033[0m shop show --all" +
                "\033[0;97m" + "Show Specific Cards:\033[0m card show <card name>");
        System.out.println("\033[1;94m" + "\t\tAdditional options:\n" + "\033[0m" +
                "\033[0;97m" + "Exit the game:\033[0m menu exit\n" +
                "\033[0;97m" + "Enter a menu:\033[0m menu enter <menu name>\n" +
                "\033[0;97m" + "Show current menu:\033[0m menu show-current\n");
    }


}
