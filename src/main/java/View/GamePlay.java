package View;

import Controller.DuelController;
import Controller.UserController;
import Model.GameBoard;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GamePlay extends Menu {
    private static final HashMap<String, Pattern> CHEAT_SHEET;

    private GameBoard fistPlayersBoard;
    private GameBoard secondPlayersBoard;
    private GamePhases currentPhase = GamePhases.DRAW;
    private int rounds;
    private boolean isFirstTime = true;

    static {
        CHEAT_SHEET = new HashMap<>();
        CHEAT_SHEET.put("Increase Money", Pattern.compile("^increase --money (\\d+)$"));
        CHEAT_SHEET.put("Select Additional Card", Pattern.compile("^select --hand ([A-Za-z0-9 ]+) --force$"));
        CHEAT_SHEET.put("Increase LP", Pattern.compile("^increase --LP (\\d+)$"));
        CHEAT_SHEET.put("Set Game Winner", Pattern.compile("^duel set-winner (\\w+)$"));
    }

    public GamePlay(Menu parentMenu, GameBoard fistPlayersBoard, GameBoard secondPlayersBoard, int rounds) {
        super("Game Play", parentMenu);
        this.fistPlayersBoard = fistPlayersBoard;
        this.secondPlayersBoard = secondPlayersBoard;
        this.rounds = rounds;
        subMenus.put(Pattern.compile("^select --(monster|spell|field|hand)( --opponent)? (\\d+)$"), selectCard());
        subMenus.put(Pattern.compile(""), deselectCard());
        subMenus.put(Pattern.compile(""), setCard());
        subMenus.put(Pattern.compile(""), summonCard());
        subMenus.put(Pattern.compile(""), changeCardPosition());
        subMenus.put(Pattern.compile(""), flipSummonCard());
        subMenus.put(Pattern.compile(""), attackToCard());
        subMenus.put(Pattern.compile(""), directAttack());
        subMenus.put(Pattern.compile(""), showGraveyard());
        subMenus.put(Pattern.compile(""), showSelectedCard());
        subMenus.put(Pattern.compile(""), changePhase());
        subMenus.put(Pattern.compile(""), surrender());
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

    public Menu changePhase() {
        return new Menu("Show Selected Card", this) {
            @Override
            public void executeCommand(String command) {

            }
        };
    }

    public Menu surrender() {
        return new Menu("Show Selected Card", this) {
            @Override
            public void executeCommand(String command) {

            }
        };
    }


    public void run() {
        show();
        execute();
    }

    public void execute() {
        System.out.println("Enter your command:");
        String command = scanner.nextLine();
        for (Map.Entry<Pattern, Menu> entry : subMenus.entrySet()) {
            Matcher matcher = entry.getKey().matcher(command);
            if (matcher.matches())
                entry.getValue().executeCommand(command);
        }
        Matcher matcher = CHEAT_SHEET.get("Increase Money").matcher(command);
        if (matcher.matches())
            increaseMoney(command);
        else {
            matcher = CHEAT_SHEET.get("Select Additional Card").matcher(command);
            if (matcher.matches())
                selectAdditionalCard(command);
            else {
                matcher = CHEAT_SHEET.get("Increase LP").matcher(command);
                if (matcher.matches())
                    increaseLP(command);
                else {
                    matcher = CHEAT_SHEET.get("Set Game Winner").matcher(command);
                    if (matcher.matches())
                        setDuelWinner(command);
                    else {
                        System.out.println("invalid command");
                        execute();
                    }
                }
            }
        }


    }

    public void increaseMoney(String command) {
        int amount = 0;
        Matcher matcher = CHEAT_SHEET.get("Increase Money").matcher(command);
        if (matcher.matches())
            amount = Integer.parseInt(matcher.group(1));
        UserController.getInstance().increaseMoney(fistPlayersBoard.getPlayer(), amount);
        execute();
    }

    public void selectAdditionalCard(String command) {
        String cardName = null;
        Matcher matcher = CHEAT_SHEET.get("Select Additional Card").matcher(command);
        if(matcher.matches())
            cardName = matcher.group(1);
        DuelController.getInstance().addCardToHand(fistPlayersBoard, cardName);
        execute();
    }

    public void increaseLP(String command) {
        int LP = 0;
        Matcher matcher = CHEAT_SHEET.get("Set Game Winner").matcher(command);
        if(matcher.matches())
            LP = Integer.parseInt(matcher.group(1));
        UserController.getInstance().increaseLP(fistPlayersBoard.getPlayer(), LP);
        execute();
    }

    public void setDuelWinner(String command) {
        String playerNickName = null;
        Matcher matcher = CHEAT_SHEET.get("Set Game Winner").matcher(command);
        if(matcher.matches())
            playerNickName = matcher.group(1);
        if(!DuelController.getInstance().setDuelWinner(playerNickName,
                fistPlayersBoard, secondPlayersBoard, rounds))
        execute();
        else
            this.parentMenu.parentMenu.run();
    }

    public void show() {
    }

    public void setCurrentPhase(GamePhases currentPhase) {
        this.currentPhase = currentPhase;
    }

    public GamePhases getCurrentPhase() {
        return this.currentPhase;
    }

    public boolean isFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }

    public void swapPlayers(GameBoard fistPlayersBoard, GameBoard secondPlayersBoard) {
        GameBoard temp;
        temp = fistPlayersBoard;
        fistPlayersBoard = secondPlayersBoard;
        secondPlayersBoard = temp;
    }

}
