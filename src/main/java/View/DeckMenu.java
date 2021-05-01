package View;

import java.util.regex.Pattern;

public class DeckMenu extends Menu{
    public DeckMenu(Menu parentMenu){
        super("Deck Menu", parentMenu);
        subMenus.put(Pattern.compile("deck create"), createDeck());
        subMenus.put(Pattern.compile("deck delete"), deleteDeck());
        subMenus.put(Pattern.compile("deck set-activate"), setActive());
        subMenus.put(Pattern.compile("deck add-card"), addCard());
        subMenus.put(Pattern.compile("deck rm-card"), removeCard());
        subMenus.put(Pattern.compile("deck show"), showDecks());
        subMenus.put(Pattern.compile("deck show"), showDeck());
        subMenus.put(Pattern.compile("deck show"), userCards());
    }

    public Menu createDeck(){
        return new Menu("Create New Deck", this) {
            @Override
            public void show() {

            }

            @Override
            public void execute() {

            }
        };
    }

    public Menu deleteDeck(){
        return new Menu("Delete Deck", this) {
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

    public Menu setActive(){
        return new Menu("Set Deck Active", this) {
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

    public Menu addCard(){
        return new Menu("Add Card To Deck", this) {
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

    public Menu removeCard(){
        return new Menu("Remove Card From Deck", this) {
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

    public Menu showDecks(){
        return new Menu("Show User All Decks", this) {
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

    public Menu showDeck(){
        return new Menu("Show Deck", this) {
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

    public Menu userCards(){
        return new Menu("Show User All Cards", this) {
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
