package View;

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

    public void run(){}
}
