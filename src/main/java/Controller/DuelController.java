package Controller;

import Model.Card;
import Model.Deck;
import Model.GameBoard;
import Model.User;
import View.GamePlay;
import View.Menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DuelController {
    private static DuelController instance = null;

    private DuelController() {
    }

    public void startNewDuel(String player1, String player2, String rounds, Menu parentMenu) {
        if (User.getUserByUsername(player2) == null)
            System.out.println("user with username " + player2 + " does mot exists!");
        else {
            if (User.getUserByUsername(player1).getActiveDeck() == null)
                System.out.println(player1 + " has no active deck");
            else {
                if (User.getUserByUsername(player2).getActiveDeck() == null)
                    System.out.println(player2 + " has no active deck");
                else {
                    if (!Deck.getDeckByName(User.getUserByUsername(player1).getActiveDeck()).isValid())
                        System.out.println(player1 + "`s deck is invalid");
                    else {
                        if (!Deck.getDeckByName(User.getUserByUsername(player2).getActiveDeck()).isValid())
                            System.out.println(player2 + "`s deck is invalid");
                        else {
                            if (!rounds.matches("1|3"))
                                System.out.println("number of rounds is not supported");
                            else {
                                System.out.println("new game between " + player1 + " and " +
                                        player2 + " started");
                                gamePreparation(parentMenu, User.getUserByUsername(player1), User.getUserByUsername(player2));
                            }
                        }
                    }
                }
            }
        }
    }

    public void gamePreparation(Menu parentMenu, User firstPlayer, User secondPlayer) {
        ArrayList<Card> firstPlayersMainCards = createCardsFromDeck(Deck.getDeckByName(firstPlayer.getActiveDeck()).getMainDeckCards());
        ArrayList<Card> firstPlayersSideCards = createCardsFromDeck(Deck.getDeckByName(firstPlayer.getActiveDeck()).getSideDeckCards());
        ArrayList<Card> secondPlayersMainCards = createCardsFromDeck(Deck.getDeckByName(secondPlayer.getActiveDeck()).getMainDeckCards());
        ArrayList<Card> secondPlayersSideCards = createCardsFromDeck(Deck.getDeckByName(secondPlayer.getActiveDeck()).getSideDeckCards());
        Collections.shuffle(firstPlayersMainCards);
        Collections.shuffle(secondPlayersMainCards);
        GameBoard firstPlayersBoard = new GameBoard(firstPlayer, firstPlayersMainCards, firstPlayersSideCards);
        GameBoard secondPlayersBoard = new GameBoard(secondPlayer, secondPlayersMainCards, secondPlayersSideCards);
        GamePlay gamePlay = new GamePlay(parentMenu, firstPlayersBoard, secondPlayersBoard);
        gamePlay.run();
    }


    public ArrayList<Card> createCardsFromDeck(HashMap<String, Integer> deckCards) {
        ArrayList<Card> cards = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : deckCards.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++)
                cards.add(Card.getCardByName(entry.getKey()));
        }
        return cards;
    }

    public static DuelController getInstance() {
        if (instance == null)
            instance = new DuelController();
        return instance;
    }
}
