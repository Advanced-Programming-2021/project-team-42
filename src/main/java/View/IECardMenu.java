package View;

import Server.Controller.IAndEController;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IECardMenu extends Menu{
    private static final HashMap<String, Pattern> PATTERNS_COLLECTION;

    static {
        PATTERNS_COLLECTION = new HashMap<>();
        PATTERNS_COLLECTION.put("Invalid Navigations Pattern", Pattern.compile("^menu enter (register|duel|deck|shop|import/export|scoreboard|profile)$"));
        PATTERNS_COLLECTION.put("Valid Navigations Pattern", Pattern.compile("^menu enter main$"));
        PATTERNS_COLLECTION.put("Current Menu Pattern", Pattern.compile("^menu show-current$"));
        PATTERNS_COLLECTION.put("Exit Menu Pattern", Pattern.compile("^menu exit$"));
    }
    public IECardMenu(Menu parentMenu) {
        super("Import/Export Menu", parentMenu);
        subMenus.put(Pattern.compile("^import card ([A-Za-z ]+)$"), importCard());
        subMenus.put(Pattern.compile("^export card ([A-Za-z ]+)$"), exportCard());
    }

    public Menu importCard(){
        return new Menu("Import Card", this) {
            @Override
            public void show() {
            }

            @Override
            public void executeCommand(String command) {
                String cardName = null;
                Matcher matcher = Pattern.compile("import card ([A-Za-z ]+)").matcher(command);
                if(matcher.find())
                    cardName = matcher.group(1);

                try {
                    IAndEController.getInstance().importCard(cardName);
                    System.out.println("card imported successfully");
                } catch (IOException e){
                    System.out.println("Can not import card!");
                }

                parentMenu.execute(this.parentMenu, PATTERNS_COLLECTION);
            }
        };
    }

    public Menu exportCard(){
        return new Menu("Export Card", this) {
            @Override
            public void show() {
            }

            @Override
            public void executeCommand(String command) {
                String cardName = null;
                Matcher matcher = Pattern.compile("export card ([A-Za-z ]+)").matcher(command);
                if(matcher.find())
                    cardName = matcher.group(1);

                try {
                    IAndEController.getInstance().exportCard(cardName);
                    System.out.println("card exported successfully");
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }

                parentMenu.execute(this.parentMenu, PATTERNS_COLLECTION);
            }
        };
    }

    @Override
    public void show(){
        System.out.println("\033[1;92m" + "\t\tUse this patterns to Import/Export cards:\n" + "\033[0m" +
        "\033[0;97m" + "Import Card:\033[0m import card <cardname>\n" +
        "\033[0;97m" + "Export Card:\033[0m export card <cardname>");
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
