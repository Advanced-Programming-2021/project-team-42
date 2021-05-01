package View;

import java.util.regex.Pattern;

public class IECardMenu extends Menu{
    public IECardMenu(Menu parentMenu) {
        super("Import/Export Menu", parentMenu);
        subMenus.put(Pattern.compile("import card"), importCard());
        subMenus.put(Pattern.compile("export card"), exportCard());
    }

    public Menu importCard(){
        return new Menu("Import Card", this) {
            @Override
            public void show() {
            }

            @Override
            public void execute() {
            }
        };
    }

    public Menu exportCard(){
        return new Menu("Export Card", this) {
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
