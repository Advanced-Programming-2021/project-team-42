package View;


import Model.GameBoard;

import java.util.regex.Pattern;

public class GamePlay extends Menu{
    private GameBoard fistPlayersBoard;
    private GameBoard secondPlayersBoard;
    private GamePhases currentPhase;

    public GamePlay(Menu parentMenu, GameBoard fistPlayersBoard, GameBoard secondPlayersBoard){
        super("Game Play", parentMenu);
        this.fistPlayersBoard = fistPlayersBoard;
        this.secondPlayersBoard = secondPlayersBoard;
        subMenus.put(Pattern.compile(""), selectCard());
        subMenus.put(Pattern.compile(""), deselectCard());
        subMenus.put(Pattern.compile(""), setCard());
        subMenus.put(Pattern.compile(""), summonCard());
        subMenus.put(Pattern.compile(""), changeCardPosition());
        subMenus.put(Pattern.compile(""), flipSummonCard());
        subMenus.put(Pattern.compile(""), attackToCard());
        subMenus.put(Pattern.compile(""), directAttack());
        subMenus.put(Pattern.compile(""), showGraveyard());
        subMenus.put(Pattern.compile(""), showSelectedCard());
    }

    public Menu selectCard() {
        return new Menu("Select Card", this) {
            @Override
            public void executeCommand(String command) {

            }
        };
    }

    public Menu deselectCard() {
        return new Menu("Deselect Card", this) {
            @Override
            public void executeCommand(String command) {

            }
        };
    }

    public Menu setCard() {
        return new Menu("Set Card", this) {
            @Override
            public void executeCommand(String command) {

            }
        };
    }

    public Menu summonCard() {
        return new Menu("Summon Card", this) {
            @Override
            public void executeCommand(String command) {

            }
        };
    }

    public Menu changeCardPosition() {
        return new Menu("Change Card Position", this) {
            @Override
            public void executeCommand(String command) {

            }
        };
    }

    public Menu flipSummonCard() {
        return new Menu("Flip-Summon Card", this) {
            @Override
            public void executeCommand(String command) {

            }
        };
    }

    public Menu attackToCard() {
        return new Menu("Attack to Card", this) {
            @Override
            public void executeCommand(String command) {

            }
        };
    }

    public Menu directAttack() {
        return new Menu("Direct Attack", this) {
            @Override
            public void executeCommand(String command) {

            }
        };
    }

    public Menu showGraveyard() {
        return new Menu("Show Graveyard", this) {
            @Override
            public void executeCommand(String command) {

            }
        };
    }

    public Menu showSelectedCard() {
        return new Menu("Show Selected Card", this) {
            @Override
            public void executeCommand(String command) {

            }
        };
    }

    public void run(){
        show();
        execute();
    }

    public void execute(){}

    public void show(){

    }

    public void setCurrentPhase (GamePhases currentPhase){
        this.currentPhase = currentPhase;
    }

    public void swapPlayers (GameBoard fistPlayersBoard, GameBoard secondPlayersBoard){
        GameBoard temp;
        temp = fistPlayersBoard;
        fistPlayersBoard = secondPlayersBoard;
        secondPlayersBoard = temp;
    }

}
