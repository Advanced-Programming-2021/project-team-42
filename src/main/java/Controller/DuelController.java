package Controller;

import Model.*;
import View.GamePhases;
import View.GamePlay;
import View.Menu;

import javax.swing.*;
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

    public void flipSummon(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard, GamePhases currentPhase){
        boolean isProperCard = true;
        boolean isSelectedCard = false;
        MonsterCard monsterCard = null;

        if (secondPlayersBoard.getHandSelectedCard() != null
                || secondPlayersBoard.getFieldZoneSelectedCard() != null
                || secondPlayersBoard.getGraveyardSelectedCard() != null
                || secondPlayersBoard.getSpellTrapSelectedCard() != null
                || secondPlayersBoard.getMonsterSelectedCard() != null
                || firstPlayersBoard.getHandSelectedCard() != null
                || firstPlayersBoard.getFieldZoneSelectedCard() != null
                || firstPlayersBoard.getGraveyardSelectedCard() != null
                || firstPlayersBoard.getSpellTrapSelectedCard() != null){
            isSelectedCard = true;
            isProperCard = false;
        }
        if (firstPlayersBoard.getMonsterSelectedCard() != null){
            monsterCard = firstPlayersBoard.getMonsterSelectedCard();
            isSelectedCard = true;
            isProperCard = true;
        }
        if (!isSelectedCard)
            System.out.println("you dont select any card yet");
        else {
            if (!isProperCard)
                System.out.println("you can’t change this cards position");
            else {
                if (!currentPhase.name().equals("first-main") && !currentPhase.name().equals("second-main"))
                    System.out.println("you can’t do this action in this phase");
                else {
                    if (!monsterCard.isSet() || !monsterCard.isReadyToAttack())
                        System.out.println("you can’t flip summon this card");
                    else {
                        monsterCard.setSet(false);
                        monsterCard.setSummoned(true);
                        monsterCard.setReadyToAttack(true);
                        System.out.println("flip summoned successfully");
                    }
                }
            }
        }
    }

    public boolean canAttackToCard(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard, GamePhases currentPhase, int number){
        boolean isProperCard = true;
        boolean isSelectedCard = false;
        MonsterCard monsterCard = null;

        if (secondPlayersBoard.getHandSelectedCard() != null
                || secondPlayersBoard.getFieldZoneSelectedCard() != null
                || secondPlayersBoard.getGraveyardSelectedCard() != null
                || secondPlayersBoard.getSpellTrapSelectedCard() != null
                || secondPlayersBoard.getMonsterSelectedCard() != null
                || firstPlayersBoard.getHandSelectedCard() != null
                || firstPlayersBoard.getFieldZoneSelectedCard() != null
                || firstPlayersBoard.getGraveyardSelectedCard() != null
                || firstPlayersBoard.getSpellTrapSelectedCard() != null){
            isSelectedCard = true;
            isProperCard = false;
        }
        if (firstPlayersBoard.getMonsterSelectedCard() != null){
            monsterCard = firstPlayersBoard.getMonsterSelectedCard();
            isSelectedCard = true;
            isProperCard = true;
        }
        if (!isSelectedCard){
            System.out.println("you dont select any card yet");
            return false;
        }
        else {
            if (!isProperCard){
                System.out.println("you cant attack with this card");
                return false;
            }
            else {
                if (!currentPhase.name().equals("BATTLE")){
                    System.out.println("you can’t do this action in this phase");
                    return false;
                }
                else {
                    if (monsterCard.isDefensive()){
                        System.out.println("this card is in defensive position");
                        return false;
                    }
                    else {
                        if (monsterCard.isSet()){
                            System.out.println("you cant attack with set monster");
                            return false;
                        }
                        else {
                            if (!monsterCard.isReadyToAttack()){
                                System.out.println("this card already attacked");
                                return false;
                            }
                            else {
                                if (!secondPlayersBoard.getMonstersPlace().containsKey(number)){
                                    System.out.println("there is no card to attack here");
                                    return false;
                                }
                                else return true;
                            }
                        }
                    }
                }
            }
        }
    }

    public void attackToCard(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard, int number){
        firstPlayersBoard.getMonsterSelectedCard().setReadyToAttack(false);
        if (secondPlayersBoard.getMonstersPlace().get(number).isSummoned()){
            int firstPlayerAP = firstPlayersBoard.getMonsterSelectedCard().getAttackPoint();
            int secondPlayerAP = secondPlayersBoard.getMonstersPlace().get(number).getAttackPoint();
            int damage = Math.abs(firstPlayerAP - secondPlayerAP);
            if (firstPlayerAP > secondPlayerAP){
                secondPlayersBoard.getPlayer().decreaseLP(damage);
                secondPlayersBoard.addToGraveyard(secondPlayersBoard.getMonstersPlace().get(number));
                secondPlayersBoard.removeFromMonsterPlace(number);
                System.out.println("your opponent’s monster is destroyed and your opponent receives" + damage + " battle damage");
            }
            if (secondPlayerAP > firstPlayerAP){
                firstPlayersBoard.getPlayer().decreaseLP(damage);
                firstPlayersBoard.addToGraveyard(firstPlayersBoard.getMonsterSelectedCard());
                firstPlayersBoard.removeFromMonsterPlace(firstPlayersBoard.getMonsterSelectedCard().getNumber());
                System.out.println("Your monster card is destroyed and you received " + damage + " battle damage");
            }
            if (secondPlayerAP == firstPlayerAP){
                firstPlayersBoard.addToGraveyard(firstPlayersBoard.getMonsterSelectedCard());
                secondPlayersBoard.addToGraveyard(secondPlayersBoard.getMonstersPlace().get(number));
                firstPlayersBoard.removeFromMonsterPlace(firstPlayersBoard.getMonsterSelectedCard().getNumber());
                secondPlayersBoard.removeFromMonsterPlace(number);
                System.out.println("both you and your opponent monster cards are destroyed and no one receives damage");
            }
        }
        else {
            boolean isSet = secondPlayersBoard.getMonstersPlace().get(number).isSet();
            String opponentsCardName = secondPlayersBoard.getMonstersPlace().get(number).getName();
            int firstPlayerAP = firstPlayersBoard.getMonsterSelectedCard().getAttackPoint();
            int secondPlayerDP = secondPlayersBoard.getMonstersPlace().get(number).getDefencePoint();
            int damage = Math.abs(firstPlayerAP - secondPlayerDP);
            if (firstPlayerAP > secondPlayerDP){
                secondPlayersBoard.addToGraveyard(secondPlayersBoard.getMonstersPlace().get(number));
                secondPlayersBoard.removeFromMonsterPlace(number);
                if (isSet) System.out.println("opponent’s monster card was " + opponentsCardName + " and destroyed");
                else System.out.println("the defense position monster is destroyed");
            }
            if (firstPlayerAP < secondPlayerDP){
                firstPlayersBoard.getPlayer().decreaseLP(damage);
                if (isSet) System.out.println("opponent’s monster card was " + opponentsCardName + " and no card is destroyed and you received " + damage + " battle damage");
                else System.out.println("no card is destroyed and you received " + damage + " battle damage");
            }
            if (firstPlayerAP == secondPlayerDP){
                if (isSet) System.out.println("opponent’s monster card was " + opponentsCardName + " and no card is destroyed");
                else System.out.println("no card is destroyed");
            }
        }
    }

    public void directAttack(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard, GamePhases currentPhase){
        boolean isProperCard = true;
        boolean isSelectedCard = false;
        MonsterCard monsterCard = null;
        if (secondPlayersBoard.getHandSelectedCard() != null
                || secondPlayersBoard.getFieldZoneSelectedCard() != null
                || secondPlayersBoard.getGraveyardSelectedCard() != null
                || secondPlayersBoard.getSpellTrapSelectedCard() != null
                || secondPlayersBoard.getMonsterSelectedCard() != null
                || firstPlayersBoard.getHandSelectedCard() != null
                || firstPlayersBoard.getFieldZoneSelectedCard() != null
                || firstPlayersBoard.getGraveyardSelectedCard() != null
                || firstPlayersBoard.getSpellTrapSelectedCard() != null){
            isSelectedCard = true;
            isProperCard = false;
        }
        if (firstPlayersBoard.getMonsterSelectedCard() != null){
            monsterCard = firstPlayersBoard.getMonsterSelectedCard();
            isSelectedCard = true;
            isProperCard = true;
        }
        if (!isSelectedCard)
            System.out.println("you dont select any card yet");
        else {
            if (!isProperCard)
                System.out.println("you cant attack with this card");
            else {
                if (!currentPhase.name().equals("BATTLE"))
                    System.out.println("you can’t do this action in this phase");
                else {
                    if (monsterCard.isDefensive())
                        System.out.println("this card is in defensive position");
                    else {
                        if (monsterCard.isSet())
                            System.out.println("you cant attack with set card");
                        else {
                            if (!monsterCard.isReadyToAttack())
                                System.out.println("this card already attacked");
                            else {
                                int lp = secondPlayersBoard.getPlayer().getLP();
                                int attackPoint = monsterCard.getAttackPoint();
                                secondPlayersBoard.getPlayer().setLP(lp - attackPoint);
                                System.out.println("you opponent receives " + attackPoint + " battle damage");
                                monsterCard.setReadyToAttack(false);
                            }
                        }
                    }
                }
            }
        }
    }

    public User getWinner(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard){
        if (firstPlayersBoard.getPlayer().getLP() <= 0)
            return firstPlayersBoard.getPlayer();
        if (secondPlayersBoard.getPlayer().getLP() <= 0)
            return firstPlayersBoard.getPlayer();
        return null;
    }

    public void resetMonsters(GameBoard firstPlayersBoard){
        for (Map.Entry<Integer , MonsterCard> entry : firstPlayersBoard.getMonstersPlace().entrySet()){
            entry.getValue().setReadyToAttack(true);
        }
    }

    public void showGraveyard(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard){
        if (!firstPlayersBoard.getGraveYard().isEmpty())
            for (Card card : firstPlayersBoard.getGraveYard()){
                card.toString();
            }
        if (!secondPlayersBoard.getGraveYard().isEmpty()){
            for (Card card : secondPlayersBoard.getGraveYard()){
                card.toString();
            }
        }
    }

    public void showSelectedCard(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard){
        Card selectedCard = null;
        boolean isVisible = true;

        if (firstPlayersBoard.getHandSelectedCard() != null)
            selectedCard = firstPlayersBoard.getHandSelectedCard();
        if (firstPlayersBoard.getFieldZoneSelectedCard() != null)
            selectedCard = firstPlayersBoard.getFieldZoneSelectedCard();
        if (firstPlayersBoard.getGraveyardSelectedCard() != null)
            selectedCard = firstPlayersBoard.getGraveyardSelectedCard();
        if (firstPlayersBoard.getMonsterSelectedCard() != null)
            selectedCard = firstPlayersBoard.getMonsterSelectedCard();
        if (firstPlayersBoard.getSpellTrapSelectedCard() != null)
            selectedCard = firstPlayersBoard.getSpellTrapSelectedCard();

        if (secondPlayersBoard.getHandSelectedCard() != null){
            selectedCard = secondPlayersBoard.getHandSelectedCard();
            isVisible = false;
        }
        if (secondPlayersBoard.getFieldZoneSelectedCard() != null){
            selectedCard = secondPlayersBoard.getFieldZoneSelectedCard();
        }
        if (secondPlayersBoard.getGraveyardSelectedCard() != null){
            selectedCard = secondPlayersBoard.getGraveyardSelectedCard();
        }
        if (secondPlayersBoard.getSpellTrapSelectedCard() != null){
            selectedCard = secondPlayersBoard.getSpellTrapSelectedCard();
            isVisible = false;
        }
        if (secondPlayersBoard.getMonsterSelectedCard() != null){
            selectedCard = secondPlayersBoard.getMonsterSelectedCard();
            if (selectedCard.isSet())
                isVisible = false;
        }
        if (selectedCard == null){
            System.out.println("no card is selected yet");
        }
        else {
            if (!isVisible) System.out.println("card is not visible");
            else System.out.println(selectedCard.toString());
        }
    }

    public static DuelController getInstance() {
        if (instance == null)
            instance = new DuelController();
        return instance;
    }
}
