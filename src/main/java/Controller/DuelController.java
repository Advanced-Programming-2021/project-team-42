package Controller;

import Model.Deck;
import Model.GameBoard;
import Model.User;
import View.GamePlay;
import View.Menu;

public class DuelController {
    private static DuelController instance = null;

    private DuelController(){}

    public void startNewDuel(String player1, String player2, String rounds, Menu parentMenu){
        if(User.getUserByUsername(player2) == null)
            System.out.println("user with username " + player2 + " does mot exists!");
        else{
            if(User.getUserByUsername(player1).getActiveDeck() == null)
                System.out.println(player1 + " has no active deck");
            else{
                if(User.getUserByUsername(player2).getActiveDeck() == null)
                    System.out.println(player2 + " has no active deck");
                else{
                    if(!Deck.getDeckByName(User.getUserByUsername(player1).getActiveDeck()).isValid())
                        System.out.println(player1 + "`s deck is invalid");
                    else{
                        if(!Deck.getDeckByName(User.getUserByUsername(player2).getActiveDeck()).isValid())
                            System.out.println(player2 + "`s deck is invalid");
                        else{
                            if(!rounds.matches("1|3"))
                                System.out.println("number of rounds is not supported");
                            else{
                                System.out.println("new game between " + player1 + " and " +
                                        player2 + " started");
                                // TODO: prepration for start game
                                GameBoard gameBoard1 = new GameBoard(User.getUserByUsername(player1));
                                GameBoard gameBoard2 = new GameBoard(User.getUserByUsername(player2));
                                GamePlay gamePlay = new GamePlay(parentMenu, gameBoard1, gameBoard2);
                                gamePlay.run();
                            }
                        }
                    }
                }
            }
        }
    }


    public static DuelController getInstance(){
        if(instance == null)
            instance = new DuelController();
        return instance;
    }
}
