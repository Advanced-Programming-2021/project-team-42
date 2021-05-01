package View;

import java.util.regex.Pattern;

public class MainMenu extends Menu {
    public MainMenu(Menu parentMenu){
        super("Main Menu" , parentMenu);
        subMenus.put(Pattern.compile("menu enter duel"), new DuelMenu(this));
        subMenus.put(Pattern.compile("menu enter deck"), new DeckMenu(this));
        subMenus.put(Pattern.compile("enter menu scoreboard"), new ScoreBoardMenu(this));
        subMenus.put(Pattern.compile("enter menu profile"), new ProfileMenu(this));
        subMenus.put(Pattern.compile("enter menu shop"), new ShopMenu(this));
        subMenus.put(Pattern.compile("enter menu import/export"), new IECardMenu(this));
    }

    public void show(){
    }

    public void execute(){
    }

}
