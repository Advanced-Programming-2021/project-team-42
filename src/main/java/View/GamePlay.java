package View;

import Controller.DeckController;
import Controller.DuelController;
import Controller.UserController;
import Model.Deck;
import Model.GameBoard;
import Model.User;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GamePlay extends Menu {
    private static final HashMap<String, Pattern> CHEAT_SHEET;

    private GameBoard fistPlayersBoard;
    private GameBoard secondPlayersBoard;
    private static GamePhases currentPhase = GamePhases.DRAW;
    private int rounds;
    private static boolean isFirstTime = true;
    private static boolean isSummonedOrSetInThisPhase = false;
    private boolean isCardAddedToHandInThisPhase = false;

    static {
        CHEAT_SHEET = new HashMap<>();
        CHEAT_SHEET.put("Select Additional Card", Pattern.compile("^select --hand ([A-Za-z0-9 ]+) --force$"));
        CHEAT_SHEET.put("Increase LP", Pattern.compile("^increase --LP (\\d+)$"));
        CHEAT_SHEET.put("Set Game Winner", Pattern.compile("^duel set-winner (\\w+)$"));
    }


    public GamePlay(Menu parentMenu, GameBoard fistPlayersBoard, GameBoard secondPlayersBoard, int rounds) {
        super("Game Play", parentMenu);
        this.fistPlayersBoard = fistPlayersBoard;
        this.secondPlayersBoard = secondPlayersBoard;
        this.rounds = rounds;
        subMenus.put(Pattern.compile("^select --(monster|spell|field|hand)( --opponent)?( \\d+)?$"), selectCard());
        subMenus.put(Pattern.compile("^select -d$"), deselectCard());
        subMenus.put(Pattern.compile("^set$"), setCard());
        subMenus.put(Pattern.compile("^summon$"), summonCard());
        subMenus.put(Pattern.compile("^set --position (attack|defense)$"), changeCardPosition());
        subMenus.put(Pattern.compile("^activate effect$"), activeEffect());
        subMenus.put(Pattern.compile("^flip-summon$"), flipSummonCard());
        subMenus.put(Pattern.compile("^attack ([1-5])$"), attackToCard());
        subMenus.put(Pattern.compile("^direct attack$"), directAttack());
        subMenus.put(Pattern.compile("^show graveyard$"), showGraveyard());
        subMenus.put(Pattern.compile("^card show --selected$"), showSelectedCard());
        subMenus.put(Pattern.compile("^next phase$"), changePhase());
        subMenus.put(Pattern.compile("^surrender$"), surrender());
    }

    public Menu selectCard() {
        return new Menu("Select Card", this) {
            @Override
            public void executeCommand(String command) {
                String field = null;
                int place = -1;
                boolean isOpponentSelected = false;
                Matcher matcher = Pattern.compile("select --(monster|spell|field|hand)( --opponent)?( \\d+)?").matcher(command);
                if (matcher.find()) {
                    field = matcher.group(1);
                    isOpponentSelected = (matcher.group(2) != null);
                    if (matcher.group(3) != null)
                        place = Integer.parseInt(matcher.group(3).trim());
                }

                if ((field.equals("field") && place != -1) ||
                        (!field.equals("field") && place == -1))
                    System.out.println("invalid command");
                else {

                    try {
                        DuelController.getInstance().selectCard((isOpponentSelected ? secondPlayersBoard : fistPlayersBoard), field, place);

                        System.out.println("card selected");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    parentMenu.execute();
                }
            }
        };
    }

    public Menu deselectCard() {
        return new Menu("Deselect Card", this) {
            @Override
            public void executeCommand(String command) {
                try {
                    DuelController.getInstance().deselectCard(fistPlayersBoard, secondPlayersBoard);
                    System.out.println("card deselected");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                parentMenu.execute();
            }
        };
    }

    public Menu setCard() {
        return new Menu("Set Card", this) {
            @Override
            public void executeCommand(String command) {
                try {
                    DuelController.getInstance().generalCardSet(fistPlayersBoard, secondPlayersBoard,
                            currentPhase, isSummonedOrSetInThisPhase);
                    System.out.println("set successfully");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                parentMenu.execute();
            }
        };
    }

    public Menu summonCard() {
        return new Menu("Summon Card", this) {
            @Override
            public void executeCommand(String command) {
                try {
                    int response = DuelController.getInstance().summonMonsterCard(fistPlayersBoard, secondPlayersBoard,
                            currentPhase, isSummonedOrSetInThisPhase);
                    if (response == 1)
                        System.out.println("summoned successfully");
                    else if (response == 2)
                        lowLevelSummon();
                    else
                        highLevelSummon();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                parentMenu.execute();
            }

            public void highLevelSummon() {
                System.out.println("Please Enter two number to tribute cards in that places");
                String firstChosenPlace = getValidCardNumber();
                if (firstChosenPlace.equals("exit"))
                    parentMenu.execute();
                else {
                    String secondChosenPlace = getValidCardNumber();
                    if (secondChosenPlace.equals("exit"))
                        parentMenu.execute();
                    else {
                        try {
                            DuelController.getInstance().highLevelSummon(fistPlayersBoard,
                                    Integer.parseInt(firstChosenPlace), Integer.parseInt(secondChosenPlace));
                            System.out.println("summoned successfully");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }

            public void lowLevelSummon() {
                System.out.println("Enter one number to tribute card in that place:");
                String chosenPlace = getValidCardNumber();
                if (chosenPlace.equals("exit"))
                    parentMenu.execute();
                else {
                    try {
                        DuelController.getInstance().lowLevelSummon(fistPlayersBoard, Integer.parseInt(chosenPlace));
                        System.out.println("summoned successfully");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

            public String getValidCardNumber() {
                String chosenPlace = scanner.nextLine();
                if (chosenPlace.matches("^[1-5]$"))
                    return chosenPlace;
                else if (chosenPlace.equalsIgnoreCase("back"))
                    return "exit";
                else {
                    System.out.println("Please Enter Valid number");
                    return getValidCardNumber();
                }
            }
        }

                ;
    }

    public Menu changeCardPosition() {
        return new Menu("Change Card Position", this) {
            @Override
            public void executeCommand(String command) {
                String selectedPosition = null;
                Matcher matcher = Pattern.compile("set --position (attack|defense)").matcher(command);
                if (matcher.find())
                    selectedPosition = matcher.group(1);

                try {
                    DuelController.getInstance().changePosition(fistPlayersBoard, secondPlayersBoard,
                            selectedPosition, currentPhase);
                    System.out.println("card position changed successfully");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                parentMenu.execute();
            }
        };
    }

    public Menu activeEffect() {
        return new Menu("Active Effect", this) {
            @Override
            public void executeCommand(String command) {

                try{
                    DuelController.getInstance().activationCheck(fistPlayersBoard, secondPlayersBoard, currentPhase);
                    System.out.println("spell activated");
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }

                parentMenu.execute();
            }
        };
    }

    public Menu flipSummonCard() {
        return new Menu("Flip-Summon Card", this) {
            @Override
            public void executeCommand(String command) {

                try {
                    DuelController.getInstance().flipSummon(fistPlayersBoard, secondPlayersBoard, currentPhase);
                    System.out.println("flip summoned successfully");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                parentMenu.run();
            }
        };
    }

    public Menu attackToCard() {
        return new Menu("Attack to Card", this) {
            @Override
            public void executeCommand(String command) {
                int place = 0;
                Pattern pattern = Pattern.compile("(\\d)");
                Matcher matcher = pattern.matcher(command);
                if (matcher.find())
                    place = Integer.parseInt(matcher.group(1));

                try {
                    if (DuelController.getInstance().canAttackToCard(fistPlayersBoard, secondPlayersBoard, currentPhase, place))
                        System.out.println(DuelController.getInstance().attackToCard(fistPlayersBoard, secondPlayersBoard, place));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                parentMenu.run();
            }
        };
    }

    public Menu directAttack() {
        return new Menu("Direct Attack", this) {
            @Override
            public void executeCommand(String command) {
                try {
                    int attackPoint = DuelController.getInstance().directAttack(fistPlayersBoard, secondPlayersBoard, currentPhase, isFirstTime);
                    System.out.println("you opponent receives " + attackPoint + " battle damage");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                parentMenu.run();
            }
        };
    }

    public Menu showGraveyard() {
        return new Menu("Show Graveyard", this) {
            @Override
            public void executeCommand(String command) {
                String result = DuelController.getInstance().showGraveyard(fistPlayersBoard);
                System.out.println(result);
                System.out.println("Please enter \"Back\" to back");
                String response;
                while (true) {
                    response = scanner.nextLine();
                    if (response.equalsIgnoreCase("back"))
                        parentMenu.run();
                    else
                        System.out.println("Please enter valid command");
                }
            }
        };
    }

    public Menu showSelectedCard() {
        return new Menu("Show Selected Card", this) {
            @Override
            public void executeCommand(String command) {
                try {
                    String cardInfo = DuelController.getInstance().showSelectedCard(fistPlayersBoard, secondPlayersBoard);
                    System.out.println(cardInfo);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                parentMenu.run();
            }
        };
    }

    public Menu changePhase() {
        return new Menu("Change Phase", this) {
            @Override
            public void executeCommand(String command) {

                DuelController.getInstance().changePhase(fistPlayersBoard, currentPhase);

                parentMenu.execute();
            }
        };
    }

    public Menu surrender() {
        return new Menu("Surrender", this) {
            @Override
            public void executeCommand(String command) {

            }
        };
    }


    public void run() {
        show();
        execute();
    }

    public void show() {
        System.out.println("\033[1;92m" + "\t\tUse these Patterns to Play:" + "\033[0m\n");
        System.out.println("\033[0;97m" + "Select Monster Card:\033[0m select --monster <number>\n" +
                "\033[0;97m" + "Select Opponents Monster Card:\033[0m select --monster --opponent <number>\n" +
                "\033[0;97m" + "Select SpellTrap Card:\033[0m select --spell <number>\n" +
                "\033[0;97m" + "Select Opponents SpellTrap Card:\033[0m select --spell --opponent <number>\n" +
                "\033[0;97m" + "Select FieldZone Card:\033[0m select --field\n" +
                "\033[0;97m" + "Select Opponent FieldZone Card:\033[0m select --field --opponent\n" +
                "\033[0;97m" + "Select Hand Card:\033[0m select --hand <number>\n" +
                "\033[0;97m" + "Select Opponent Hand Card:\033[0m select --hand --opponent <number>\n" +
                "\033[0;97m" + "Set Card:\033[0m set\n" +
                "\033[0;97m" + "Summon Monster Card:\033[0m summon\n" +
                "\033[0;97m" + "Change Card Position:\033[0m set --position <attack/defense>\n" +
                "\033[0;97m" + "Flip-Summon Monster Card:\033[0m flip-summon\n" +
                "\033[0;97m" + "Active SpellTrap Card Effect:\033[0m active effect\n" +
                "\033[0;97m" + "Attack to Card:\033[0m attack <number>\n" +
                "\033[0;97m" + "Direct Attack to Opponent:\033[0m direct attack\n" +
                "\033[0;97m" + "Show GraveYard:\033[0m show graveyard\n" +
                "\033[0;97m" + "Show Selected Card:\033[0m card show --selected\n" +
                "\033[0;97m" + "Goto Next Phase:\033[0m next phase\n" +
                "\033[0;97m" + "Surrender:\033[0m surrender\n");
    }

    public void execute() {
        gameEndCheck();
        phaseChangeToDo();
        secondPlayersBoard.drawBoardAsOpponent();
        System.out.println("\n--------------------------");
        fistPlayersBoard.drawBoardAsYourself();
        System.out.println("Enter your command:");
        String command = scanner.nextLine();
        for (Map.Entry<Pattern, Menu> entry : subMenus.entrySet()) {
            Matcher matcher = entry.getKey().matcher(command);
            if (matcher.matches())
                entry.getValue().executeCommand(command);
        }
        Matcher matcher = CHEAT_SHEET.get("Select Additional Card").matcher(command);
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
                    if (command.equals("?"))
                        this.show();
                    else
                        System.out.println("invalid command");
                }
            }
        }
        execute();
    }

    public void phaseChangeToDo() {
        if (currentPhase.equals(GamePhases.DRAW)) {
            System.out.println("Current Phase: Draw Phase");
            if (!isFirstTime && !isCardAddedToHandInThisPhase && fistPlayersBoard.getCardsInHand().size() < 6) {
                System.out.println("new card added to hand: " +
                        DuelController.getInstance().addOneCardToHand(fistPlayersBoard));
                isCardAddedToHandInThisPhase = true;
            }
            setSummonOrSet(false);
        } else if (currentPhase.equals(GamePhases.STANDBY))
            System.out.println("Current Phase: StandBy Phase");
        else if (currentPhase.equals(GamePhases.FIRST_MAIN))
            System.out.println("Current Phase: First Main Phase");
        else if (currentPhase.equals(GamePhases.BATTLE))
            System.out.println("Current Phase: Battle Phase");
        else if (currentPhase.equals(GamePhases.SECOND_MAIN))
            System.out.println("Current Phase: Second Main Phase");
        else if (currentPhase.equals(GamePhases.END)) {
            System.out.println("Current Phase: End Phase");
            System.out.println("now its " + secondPlayersBoard.getPlayer().getNickname() + " turn");
            swapPlayers(fistPlayersBoard, secondPlayersBoard);
            isFirstTime = false;
            isCardAddedToHandInThisPhase = false;
            setPhase(GamePhases.DRAW);
            execute();
        }
    }

    public void gameEndCheck() {
        if (fistPlayersBoard.getPlayer().getLP() <= 0 ||
                secondPlayersBoard.getMainDeckCards().size() == 0)
            DuelController.getInstance().setWinner(fistPlayersBoard, secondPlayersBoard,
                    rounds, this.parentMenu.parentMenu, this,
                    this.parentMenu);
        else if (secondPlayersBoard.getPlayer().getLP() <= 0 ||
                fistPlayersBoard.getMainDeckCards().size() == 0)
            DuelController.getInstance().setWinner(secondPlayersBoard, fistPlayersBoard,
                    rounds, this.parentMenu.parentMenu, this,
                    this.parentMenu);
    }

    public boolean exchangeCardsCheck(String nickName) {
        System.out.println(nickName + " Do you want to exchange cards between MainDeck and SideDeck?");
        String response;
        while (true) {
            response = scanner.nextLine();
            if (response.equalsIgnoreCase("yes"))
                return true;
            else if (response.equalsIgnoreCase("no"))
                return false;
            else
                System.out.println("Please enter valid command\n" +
                        "Yes or No!!!");
        }
    }

    public void getCardsName(User user) {
        System.out.println("Main Deck Cards:");
        DeckController.getInstance().printDeckCards(Deck.getDeckByName(user.getActiveDeck()).getMainDeckCards());
        System.out.println("Side Deck Cards:");
        DeckController.getInstance().printDeckCards(Deck.getDeckByName(user.getActiveDeck()).getSideDeckCards());
        System.out.println("if you want to end exchange process Enter 1\n" +
                "else type your card name in MainDeck");
        String cardsName = scanner.nextLine();
        if (!cardsName.equals("1")) {
            System.out.println("now enter card name in SideDeck");
            String sideDeckCardName = scanner.nextLine();
            cardsName += ",";
            cardsName += sideDeckCardName;

            try {
                DuelController.getInstance().exchangeCard(user, cardsName);
                System.out.println("Cards exchanged successfully");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            getCardsName(user);
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
        if (matcher.matches())
            cardName = matcher.group(1);

        try {
            DuelController.getInstance().addCardToHand(fistPlayersBoard, cardName);
            System.out.println("Card added to your Hand successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        execute();
    }

    public void increaseLP(String command) {
        int LP = 0;
        Matcher matcher = CHEAT_SHEET.get("Set Game Winner").matcher(command);
        if (matcher.matches())
            LP = Integer.parseInt(matcher.group(1));
        UserController.getInstance().increaseLP(fistPlayersBoard.getPlayer(), LP);
        execute();
    }

    public void setDuelWinner(String command) {
        String playerNickName = null;
        Matcher matcher = CHEAT_SHEET.get("Set Game Winner").matcher(command);
        if (matcher.matches())
            playerNickName = matcher.group(1);

        try {
            DuelController.getInstance().setDuelWinner(playerNickName,
                    fistPlayersBoard, secondPlayersBoard, rounds);
            System.out.println("Player with Nickname " + playerNickName + " Wins the game!");
            execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.parentMenu.parentMenu.run();
        }
    }


    public static boolean isFirstTime() {
        return isFirstTime;
    }

    public static void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }

    public static void setPhase(GamePhases currentPhase) {
        GamePlay.currentPhase = currentPhase;
    }

    public static GamePhases getCurrentPhase() {
        return GamePlay.currentPhase;
    }

    public static void setSummonOrSet(boolean summonedOrSet) {
        GamePlay.isSummonedOrSetInThisPhase = summonedOrSet;
    }

    public static boolean getSummonOrSet() {
        return GamePlay.isSummonedOrSetInThisPhase;
    }

    public void swapPlayers(GameBoard fistPlayersBoard, GameBoard secondPlayersBoard) {
        GameBoard temp = fistPlayersBoard;
        this.fistPlayersBoard = secondPlayersBoard;
        this.secondPlayersBoard = temp;
    }

}
