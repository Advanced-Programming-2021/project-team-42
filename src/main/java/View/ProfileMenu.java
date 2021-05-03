package View;

import Controller.UserController;
import Model.User;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileMenu extends Menu{
    private static String COMMAND;
    private static final HashMap<String, Pattern> PATTERNS_COLLECTION;

    static {
        PATTERNS_COLLECTION = new HashMap<>();
        PATTERNS_COLLECTION.put("Invalid Navigations Pattern", Pattern.compile("^menu enter (register|duel|deck|shop|import/export|scoreboard|profile)$"));
        PATTERNS_COLLECTION.put("Valid Navigations Pattern", Pattern.compile("^menu enter main$"));
        PATTERNS_COLLECTION.put("Current Menu Pattern", Pattern.compile("^menu show-current$"));
        PATTERNS_COLLECTION.put("Exit Menu Pattern", Pattern.compile("^menu exit$"));
    }
    public ProfileMenu(Menu parentMenu){
        super("Profile Menu" , parentMenu);
        subMenus.put(Pattern.compile("^profile change --nickname (\\w+)$"), changeNickname());
        subMenus.put(Pattern.compile("^profile change --password --(current|new) (\\w+) --(current|new) (\\w+)$"), changePassword());
    }

    public Menu changeNickname(){
        return new Menu("Change Nickname", this) {
            @Override
            public void show() {
            }

            @Override
            public void execute() {
                String newNickname = null;
                Pattern pattern = Pattern.compile("--nickname (\\w+)");
                Matcher matcher = pattern.matcher(COMMAND);
                if(matcher.find())
                    newNickname = matcher.group(1);

                UserController.getInstance().changeNickname(this.parentMenu.parentMenu.usersName, newNickname);
                parentMenu.execute();
            }
        };
    }

    public Menu changePassword(){
        return new Menu("Change Password", this) {
            @Override
            public void show() {
            }

            @Override
            public void execute() {
                String newPassword = null, currentPassword = null;
                 Pattern pattern = Pattern.compile("--new (\\w+)");
                 Matcher matcher = pattern.matcher(COMMAND);
                 if(matcher.find())
                     newPassword = matcher.group(1);
                 pattern = Pattern.compile("--current (\\w+)");
                 matcher = pattern.matcher(COMMAND);
                 if(matcher.find())
                     currentPassword = matcher.group(1);

                UserController.getInstance().changePassword(this.parentMenu.parentMenu.usersName, currentPassword, newPassword);
                parentMenu.execute();
            }
        };
    }

    public void show(){
        System.out.println("\033[1;92m" + "\t\tUse this Patterns to Enter your desired Menu:" + "\033[0m\n" +
                "\033[4;31m" + "Tip:\033[0m You can enter fields with dash sign (--) by any desired order!\n");
        System.out.println("\033[0;97m" + "Change Nickname:\033[0m profile change --nickname <nickname>\n" +
                "\033[0;97m" + "Change Password:\033[0m profile change --password --current <current password> --new <newpassword>");
        System.out.println("\033[1;94m" + "\t\tAdditional options:\n" + "\033[0m" +
                "\033[0;97m" + "Exit the game:\033[0m menu exit\n" +
                "\033[0;97m" + "Enter a menu:\033[0m menu enter <menu name>\n" +
                "\033[0;97m" + "Show current menu:\033[0m menu show-current\n");

    }

    public void execute(){
        String command = getValidCommand();
        Matcher matcher;
        Menu nextMenu = null;
        for(Map.Entry<Pattern, Menu> entry : subMenus.entrySet()){
            matcher = entry.getKey().matcher(command);
            if(matcher.matches()){
                nextMenu = entry.getValue();
                COMMAND = command;
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

    public void run(){
        show();
        execute();
    }

    public String getValidCommand(){
        System.out.println("Enter your desired command:");
        String command;
        Matcher matcher;
        boolean check = false;
        do{
            command = Menu.scanner.nextLine();
            for(Map.Entry<Pattern, Menu> entry : subMenus.entrySet()){
                matcher = entry.getKey().matcher(command);
                if(matcher.matches()){
                    if(!(StringUtils.countMatches(command, "--new") > 1 ||
                            StringUtils.countMatches(command, "--current") > 1))
                        check = true;
                }
            }
            for(Map.Entry<String , Pattern> entry : PATTERNS_COLLECTION.entrySet()){
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
