package View;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        RegisterMenu registerMenu = new RegisterMenu(null);
        MainMenu mainMenu = new MainMenu(registerMenu);
        Menu.setScanner(new Scanner(System.in));
        registerMenu.run();
    }
}