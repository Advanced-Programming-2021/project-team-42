package View;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public abstract class Menu {
    protected String name;
    protected String usersName;
    Menu parentMenu;
    public static Menu mainMenu;
    public static Menu userMenu;
    protected HashMap<Pattern, Menu> subMenus;
    static Scanner scanner;

    public Menu(String name, Menu parentMenu){
        this.name = name;
        this.parentMenu = parentMenu;
        subMenus = new HashMap<>();
    }

    public static void setScanner(Scanner scanner){
        Menu.scanner = scanner;
    }

    public void setUsersName(String usersName) {
        this.usersName = usersName;
    }

    public void show(){}

    public void execute() {}

    public void run(){}
}
