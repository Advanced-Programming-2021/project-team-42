package Controller;

import Model.*;
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
                                gamePreparation(parentMenu, User.getUserByUsername(player1), User.getUserByUsername(player2), Integer.parseInt(rounds));
                            }
                        }
                    }
                }
            }
        }
    }

    public void gamePreparation(Menu parentMenu, User firstPlayer, User secondPlayer, int rounds) {
        ArrayList<Card> firstPlayersMainCards = createCardsFromDeck(Deck.getDeckByName(firstPlayer.getActiveDeck()).getMainDeckCards());
        ArrayList<Card> firstPlayersSideCards = createCardsFromDeck(Deck.getDeckByName(firstPlayer.getActiveDeck()).getSideDeckCards());
        ArrayList<Card> secondPlayersMainCards = createCardsFromDeck(Deck.getDeckByName(secondPlayer.getActiveDeck()).getMainDeckCards());
        ArrayList<Card> secondPlayersSideCards = createCardsFromDeck(Deck.getDeckByName(secondPlayer.getActiveDeck()).getSideDeckCards());
        Collections.shuffle(firstPlayersMainCards);
        Collections.shuffle(secondPlayersMainCards);
        GameBoard firstPlayersBoard = new GameBoard(firstPlayer, firstPlayersMainCards, firstPlayersSideCards);
        GameBoard secondPlayersBoard = new GameBoard(secondPlayer, secondPlayersMainCards, secondPlayersSideCards);
        GamePlay gamePlay = new GamePlay(parentMenu, firstPlayersBoard, secondPlayersBoard, rounds);
        gamePlay.run();
    }


    public ArrayList<Card> createCardsFromDeck(HashMap<String, Integer> deckCards) {
        ArrayList<Card> cards = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : deckCards.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                if(MonsterCard.isMonsterCard(entry.getKey()))
                    cards.add(MonsterCard.getInstance(MonsterCard.getMonsterCardByName(entry.getKey())));
                else
                    cards.add(SpellTrapCard.getInstance(SpellTrapCard.getSpellTrapCardByName(entry.getKey())));
            }
        }
        return cards;
    }

    public void addCardToHand(GameBoard gameBoard, String cardName){
        if(Card.getCardByName(cardName) == null)
            System.out.println("There is no Card with given name!");
        else{
            Card card = Card.getCardByName(cardName);
            gameBoard.addCardToHand(card);
            System.out.println("Card added to your Hand successfully!");
        }
    }

    public boolean setDuelWinner(String nickName,
                              GameBoard firstPlayerBoard, GameBoard secondPlayerBoard, int rounds){
        if(!firstPlayerBoard.getPlayer().getNickname().equals(nickName) &&
                !secondPlayerBoard.getPlayer().getNickname().equals(nickName)) {
            System.out.println("Entered NickName does not matches players nickname");
            return false;
        }
        else{
            if(firstPlayerBoard.getPlayer().getNickname().equals(nickName)){
                firstPlayerBoard.getPlayer().setBalance(firstPlayerBoard.getPlayer().getBalance() + rounds * (1000 + firstPlayerBoard.getMaxLP()));
                secondPlayerBoard.getPlayer().setBalance(secondPlayerBoard.getPlayer().getBalance() + rounds * 100);
                firstPlayerBoard.getPlayer().setScore(firstPlayerBoard.getPlayer().getScore() + rounds * 1000);
            }
            else{
                secondPlayerBoard.getPlayer().setBalance(secondPlayerBoard.getPlayer().getBalance() + rounds * (1000 + secondPlayerBoard.getMaxLP()));
                firstPlayerBoard.getPlayer().setBalance(firstPlayerBoard.getPlayer().getBalance() + rounds * 100);
                secondPlayerBoard.getPlayer().setScore(secondPlayerBoard.getPlayer().getScore() + rounds * 1000);
            }
            System.out.println("Player with Nickname " + nickName + "Wins the game!");
            return true;
        }
    }

    public static DuelController getInstance() {
        if (instance == null)
            instance = new DuelController();
        return instance;
    }
}
