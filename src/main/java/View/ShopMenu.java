package View;

import Controller.ShopController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopMenu extends Menu{
    public ShopMenu(Menu parentMenu){
        super("Shop Menu", parentMenu);
        subMenus.put(Pattern.compile("shop buy"), buyItem());
        subMenus.put(Pattern.compile("shop show"), showCards());
    }

    public Menu buyItem(){
        return new Menu("Buy Card", this) {
            @Override
            public void show() {
                super.show();
            }

            @Override
            public void execute() {
                super.execute();
            }
        };
    }

    public Menu showCards(){
        return new Menu("Show Shop All Cards", this) {
            @Override
            public void show() {
                super.show();
            }

            @Override
            public void execute() {
                super.execute();
            }
        };
    }

    public void run(){
        String command;
        Pattern byeItemPattern = Pattern.compile("^shop bye ([a-zA-Z ]+)$");
        Pattern showShopPattern = Pattern.compile("^show shop --all$");

        while (!(command = Menu.scanner.nextLine()).equals("menu exit")){
            Matcher byeItemMatcher = byeItemPattern.matcher(command);
            Matcher showShopMatcher = showShopPattern.matcher(command);
            if (byeItemMatcher.find()){
                new ShopController().sellCard(this.usersName,byeItemMatcher.group(1).trim());
                continue;
            }
            if (showShopMatcher.find()){
                new ShopController().showAllCards();
                continue;
            }
            System.out.println("invalid command");
            System.out.println("try again");
        }
        this.parentMenu.run();
    }
}



