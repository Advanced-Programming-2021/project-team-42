package Controller;

import Model.*;
import View.GamePhases;
import View.GamePlay;
import View.Menu;

import java.util.*;

public class DuelController {
    private static DuelController instance = null;

    private DuelController() {
    }

    public void startNewDuel(String player1, String player2, String rounds, Menu parentMenu) throws Exception {
        if (User.getUserByUsername(player2) == null)
            throw new Exception("user with username " + player2 + " does mot exists!");
        else {
            if (User.getUserByUsername(player1).getActiveDeck() == null)
                throw new Exception(player1 + " has no active deck");
            else {
                if (User.getUserByUsername(player2).getActiveDeck() == null)
                    throw new Exception(player2 + " has no active deck");
                else {
                    if (!Deck.getDeckByName(User.getUserByUsername(player1).getActiveDeck()).isValid())
                        throw new Exception(player1 + "`s deck is invalid");
                    else {
                        if (!Deck.getDeckByName(User.getUserByUsername(player2).getActiveDeck()).isValid())
                            throw new Exception(player2 + "`s deck is invalid");
                        else {
                            if (!rounds.matches("1|3"))
                                throw new Exception("number of rounds is not supported");
                            else {
                                System.out.println("new game between " + player1 + " and " +
                                        player2 + " started");
                                gamePreparation(parentMenu, User.getUserByUsername(player1), User.getUserByUsername(player2), Integer.parseInt(rounds),
                                        null, null);
                            }
                        }
                    }
                }
            }
        }
    }

    public void gamePreparation(Menu parentMenu, User firstPlayer, User secondPlayer, int rounds,
                                GameBoard firstPlayerBoard, GameBoard secondPlayerBoard) {
        ArrayList<Card> firstPlayersMainCards = createCardsFromDeck(Deck.getDeckByName(firstPlayer.getActiveDeck()).getMainDeckCards());
        ArrayList<Card> firstPlayersSideCards = createCardsFromDeck(Deck.getDeckByName(firstPlayer.getActiveDeck()).getSideDeckCards());
        ArrayList<Card> secondPlayersMainCards = createCardsFromDeck(Deck.getDeckByName(secondPlayer.getActiveDeck()).getMainDeckCards());
        ArrayList<Card> secondPlayersSideCards = createCardsFromDeck(Deck.getDeckByName(secondPlayer.getActiveDeck()).getSideDeckCards());
        ArrayList<Card> firstPlayersHand = new ArrayList<>();
        ArrayList<Card> secondPlayersHand = new ArrayList<>();
        Collections.shuffle(firstPlayersMainCards);
        Collections.shuffle(secondPlayersMainCards);
        for (int i = 0; i < 6; i++) {
            firstPlayersHand.add(firstPlayersMainCards.get(0));
            secondPlayersHand.add(secondPlayersMainCards.get(0));
            firstPlayersMainCards.remove(0);
            secondPlayersMainCards.remove(0);
        }
        if (firstPlayerBoard == null && secondPlayerBoard == null) {
            GameBoard firstPlayersBoard = new GameBoard(firstPlayer, firstPlayersMainCards, firstPlayersSideCards, firstPlayersHand);
            GameBoard secondPlayersBoard = new GameBoard(secondPlayer, secondPlayersMainCards, secondPlayersSideCards, secondPlayersHand);
            GamePlay gamePlay = new GamePlay(parentMenu, firstPlayersBoard, secondPlayersBoard, rounds);
            gamePlay.run();
        } else {
            GamePlay gamePlay = new GamePlay(parentMenu, firstPlayerBoard, secondPlayerBoard, rounds);
            gamePlay.run();
        }
    }


    public ArrayList<Card> createCardsFromDeck(HashMap<String, Integer> deckCards) {
        ArrayList<Card> cards = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : deckCards.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                if (MonsterCard.isMonsterCard(entry.getKey()))
                    cards.add(MonsterCard.getInstance(MonsterCard.getMonsterCardByName(entry.getKey())));
                else
                    cards.add(SpellTrapCard.getInstance(SpellTrapCard.getSpellTrapCardByName(entry.getKey())));
            }
        }
        return cards;
    }

    public void addCardToHand(GameBoard gameBoard, String cardName) throws Exception {
        if (Card.getCardByName(cardName) == null)
            throw new Exception("There is no Card with given name!");
        else {
            Card card = Card.getCardByName(cardName);
            gameBoard.addCardToHand(card);
        }
    }

    public void setDuelWinner(String nickName,
                              GameBoard firstPlayerBoard, GameBoard secondPlayerBoard, int rounds) throws Exception {
        if (!firstPlayerBoard.getPlayer().getNickname().equals(nickName) &&
                !secondPlayerBoard.getPlayer().getNickname().equals(nickName)) {
            throw new Exception("Entered NickName does not matches players nickname");
        } else {
            if (firstPlayerBoard.getPlayer().getNickname().equals(nickName)) {
                firstPlayerBoard.getPlayer().setBalance(firstPlayerBoard.getPlayer().getBalance() + rounds * (1000 + firstPlayerBoard.getMaxLP()));
                secondPlayerBoard.getPlayer().setBalance(secondPlayerBoard.getPlayer().getBalance() + rounds * 100);
                firstPlayerBoard.getPlayer().setScore(firstPlayerBoard.getPlayer().getScore() + rounds * 1000);
            } else {
                secondPlayerBoard.getPlayer().setBalance(secondPlayerBoard.getPlayer().getBalance() + rounds * (1000 + secondPlayerBoard.getMaxLP()));
                firstPlayerBoard.getPlayer().setBalance(firstPlayerBoard.getPlayer().getBalance() + rounds * 100);
                secondPlayerBoard.getPlayer().setScore(secondPlayerBoard.getPlayer().getScore() + rounds * 1000);
            }
        }
    }

    public void activationCheck(GameBoard firstPlayerBoard, GameBoard secondPlayerBoard,
                                GamePhases currentPhase) throws Exception {
        if (firstPlayerBoard.checkSelections() && secondPlayerBoard.checkSelections())
            throw new Exception("no card is selected yet");
        else {
            if (firstPlayerBoard.getHandSelectedCard() == null && firstPlayerBoard.getSpellTrapSelectedCard() == null)
                throw new Exception("you can not do this action with this cards");
            else {
                Card handCard = firstPlayerBoard.getHandSelectedCard();
                SpellTrapCard spellTrapCard = firstPlayerBoard.getSpellTrapSelectedCard();
                boolean phaseCheck = !currentPhase.equals(GamePhases.FIRST_MAIN) && !currentPhase.equals(GamePhases.SECOND_MAIN);
                if (handCard != null) {
                    if (SpellTrapCard.getSpellTrapCardByName(handCard.getName()) != null && !((SpellTrapCard) handCard).getCardType().equals("Spell Card"))
                        throw new Exception("activate effect is only for spell cards");
                    else {
                        if (phaseCheck)
                            throw new Exception("you can’t activate an effect on this turn");
                        else {
                            SpellTrapCard choseSpellCard = (SpellTrapCard) handCard;
                            if (firstPlayerBoard.spellTrapPlacesSize() > 5)
                                throw new Exception("spell card zone is full");
                            else {
                                choseSpellCard.setEffectActive(true);
                                choseSpellCard.setSet(false);
                                firstPlayerBoard.setSpellTrapsPlace(choseSpellCard, firstPlayerBoard.spellTrapPlacesSize() + 1);
                                firstPlayerBoard.removeCardFromHand(firstPlayerBoard.getSelectedHandPlace());
                                firstPlayerBoard.deselectAll();
                                secondPlayerBoard.deselectAll();
                            }
                        }
                    }

                } else if (spellTrapCard != null) {
                    if (phaseCheck)
                        throw new Exception("you can’t activate an effect on this turn");
                    else {
                        if (spellTrapCard.isEffectActive())
                            throw new Exception("you have already activated this card");
                        else {
                            if (firstPlayerBoard.spellTrapPlacesSize() > 5)
                                throw new Exception("spell card zone is full");
                            else {
                                spellTrapCard.setEffectActive(true);
                                spellTrapCard.setSet(false);
                                firstPlayerBoard.deselectAll();
                                secondPlayerBoard.deselectAll();
                            }
                        }

                    }
                }
            }
        }
    }

    public void selectCard(GameBoard gameBoard, String field, int place) throws Exception {
        switch (field) {
            case "monster":
                selectMonster(gameBoard, place);
                break;
            case "spell":
                selectSpell(gameBoard, place);
                break;
            case "field":
                selectFieldZone(gameBoard);
                break;
            case "hand":
                selectHand(gameBoard, place);
                break;
        }
    }

    public void selectMonster(GameBoard gameBoard, int place) throws Exception {
        if (place > 5)
            throw new Exception("invalid selection");
        else {
            if (gameBoard.getMonsterCardByPlace(place) == null)
                throw new Exception("no card found in the given position");
            else {
                gameBoard.deselectAll();
                gameBoard.setMonsterSelectedCard(gameBoard.getMonsterCardByPlace(place));
                gameBoard.setSelectedMonsterPlace(place);
            }
        }
    }

    public void selectSpell(GameBoard gameBoard, int place) throws Exception {
        if (place > 5)
            throw new Exception("invalid selection");
        else {
            if (gameBoard.getSpellTrapCardByPlace(place) == null)
                throw new Exception("no card found in the given position");
            else {
                gameBoard.deselectAll();
                gameBoard.setSpellTrapSelectedCard(gameBoard.getSpellTrapCardByPlace(place));
                gameBoard.setSelectedSpellTrapPlace(place);
            }
        }
    }

    public void selectFieldZone(GameBoard gameBoard) throws Exception {
        if (gameBoard.getFieldZone() == null)
            throw new Exception("no card found in the given position");
        else {
            gameBoard.deselectAll();
            gameBoard.setFieldZoneSelectedCard(gameBoard.getFieldZone());
        }
    }

    public void selectHand(GameBoard gameBoard, int place) throws Exception {
        if (place > gameBoard.getCardsInHand().size())
            throw new Exception("invalid selection");
        else {
            if (gameBoard.getHandCardByPlace(place) == null)
                throw new Exception("no card found in the given position");
            else {
                gameBoard.deselectAll();
                gameBoard.setHandSelectedCard(gameBoard.getHandCardByPlace(place));
                gameBoard.setSelectedHandPlace(place);
            }
        }
    }

    public void deselectCard(GameBoard firstPlayerBoard, GameBoard secondPlayerBoard) throws Exception {
        if (firstPlayerBoard.checkSelections() && secondPlayerBoard.checkSelections())
            throw new Exception("no card is selected yet");
        else {
            firstPlayerBoard.deselectAll();
            secondPlayerBoard.deselectAll();
        }
    }

    public int summonMonsterCard(GameBoard firstPlayerBoard,
                                 GameBoard secondPlayerBoard, GamePhases currentPhase,
                                 GamePlay currentMenu, boolean isSummonedOrSetInThisPhase) throws Exception {
        if (firstPlayerBoard.checkSelections() && secondPlayerBoard.checkSelections())
            throw new Exception("no card is selected yet");
        else {
            Card selectedCard = extractCard(firstPlayerBoard, secondPlayerBoard);
            if (firstPlayerBoard.getHandSelectedCard() == null ||
                    !MonsterCard.isMonsterCard(selectedCard.getName()) ||
                    MonsterCard.getMonsterCardByName(selectedCard.getName()).getName().equals("Skull Guardian") ||
                    MonsterCard.getMonsterCardByName(selectedCard.getName()).getName().equals("Crab Turtle") ||
                    MonsterCard.getMonsterCardByName(selectedCard.getName()).getName().equals("Beast King Barbaros"))
                throw new Exception("you can’t summon this card");
            else {
                if (!currentPhase.equals(GamePhases.FIRST_MAIN) &&
                        !currentPhase.equals(GamePhases.SECOND_MAIN))
                    throw new Exception("action not allowed in this phase");
                else {
                    if (firstPlayerBoard.monsterPlacesSize() == 5)
                        throw new Exception("monster card zone is full");
                    else {
                        if (isSummonedOrSetInThisPhase)
                            throw new Exception("you already summoned/set on this turn");
                        else {
                            MonsterCard selectedMonsterCard = MonsterCard.getMonsterCardByName(selectedCard.getName());
                            if (selectedMonsterCard.getLevel() <= 4) {
                                simpleSummon(firstPlayerBoard, secondPlayerBoard, currentMenu, selectedMonsterCard);
                                return 1;
                            } else if (selectedMonsterCard.getLevel() >= 5 && selectedMonsterCard.getLevel() <= 6) {
                                if (firstPlayerBoard.monsterPlacesSize() == 0)
                                    throw new Exception("there are not enough cards for tribute");
                                else
                                    return 2;
                            } else if (selectedMonsterCard.getLevel() >= 7 && selectedMonsterCard.getLevel() <= 8) {
                                if (firstPlayerBoard.monsterPlacesSize() < 2)
                                    throw new Exception("there are not enough cards for tribute");
                                else
                                    return 3;
                            } else {
                                if (firstPlayerBoard.monsterPlacesSize() < 3)
                                    throw new Exception("there are not enough cards for tribute");
                                else
                                    return 4;
                            }
                        }
                    }
                }
            }
        }
    }

    public void simpleSummon(GameBoard playersBoard, GameBoard opponentBoard,
                             GamePlay currentMenu, MonsterCard selectedCard) {
        selectedCard.setSummoned(true);
        selectedCard.setSet(false);
        selectedCard.setDefensive(false);
        playersBoard.setSummonedMonster(selectedCard);
        playersBoard.removeCardFromHand(playersBoard.getSelectedHandPlace());
        whileSummonTrapsActivation(playersBoard, opponentBoard, currentMenu, selectedCard);
        playersBoard.deselectAll();
        GamePlay.setSummonOrSet(true);
    }

    public void lowLevelSummon(GameBoard playersBoard, GameBoard opponentBoard,
                               GamePlay currentMenu, int choice) throws Exception {
        MonsterCard selectedCard = playersBoard.getMonsterSelectedCard();
        if (playersBoard.getMonsterCardByPlace(choice) == null) {
            throw new Exception("there no monsters one this address");
        } else {
            selectedCard.setSummoned(true);
            selectedCard.setSet(false);
            selectedCard.setDefensive(false);
            MonsterCard chosenMonsterCard = playersBoard.getMonsterCardByPlace(choice);
            playersBoard.addCardToGraveyard(chosenMonsterCard);
            playersBoard.setMonstersPlace(null, choice);
            playersBoard.setSummonedMonster(selectedCard);
            playersBoard.removeCardFromHand(playersBoard.getSelectedHandPlace());
            whileSummonTrapsActivation(playersBoard, opponentBoard, currentMenu, selectedCard);
            playersBoard.deselectAll();
            GamePlay.setSummonOrSet(true);
        }
    }

    public void highLevelSummon(GameBoard playersBoard, GameBoard opponentBoard,
                                GamePlay currentMenu, int firstChoice, int secondChoice) throws Exception {
        MonsterCard selectedCard = playersBoard.getMonsterSelectedCard();
        if (playersBoard.getMonsterCardByPlace(firstChoice) == null ||
                playersBoard.getMonsterCardByPlace(secondChoice) == null)
            throw new Exception("there is no monster on one of these addresses");
        else {
            selectedCard.setSummoned(true);
            selectedCard.setSet(false);
            selectedCard.setDefensive(false);
            MonsterCard firstTribute = playersBoard.getMonsterCardByPlace(firstChoice);
            MonsterCard secondTribute = playersBoard.getMonsterCardByPlace(secondChoice);
            playersBoard.addCardToGraveyard(firstTribute);
            playersBoard.addCardToGraveyard(secondTribute);
            playersBoard.setMonstersPlace(null, firstChoice);
            playersBoard.setMonstersPlace(null, secondChoice);
            playersBoard.setSummonedMonster(selectedCard);
            playersBoard.removeCardFromHand(playersBoard.getSelectedHandPlace());
            whileSummonTrapsActivation(playersBoard, opponentBoard, currentMenu, selectedCard);
            playersBoard.deselectAll();
            GamePlay.setSummonOrSet(true);
        }
    }

    public void gateGuardianSummon(GameBoard playersBoard, GameBoard opponentBoard,
                                   GamePlay currentMenu, int firstChoice, int secondChoice, int thirdChoice) throws Exception {
        MonsterCard selectedCard = playersBoard.getMonsterSelectedCard();
        if (playersBoard.getMonsterCardByPlace(firstChoice) == null ||
                playersBoard.getMonsterCardByPlace(secondChoice) == null ||
                playersBoard.getMonsterCardByPlace(thirdChoice) == null)
            throw new Exception("there is no monster on one of these addresses");
        else {
            selectedCard.setSummoned(true);
            selectedCard.setSet(false);
            selectedCard.setDefensive(false);
            MonsterCard firstTribute = playersBoard.getMonsterCardByPlace(firstChoice);
            MonsterCard secondTribute = playersBoard.getMonsterCardByPlace(secondChoice);
            MonsterCard thirdTribute = playersBoard.getMonsterCardByPlace(thirdChoice);
            playersBoard.addCardToGraveyard(firstTribute);
            playersBoard.addCardToGraveyard(secondTribute);
            playersBoard.addCardToGraveyard(thirdTribute);
            playersBoard.setMonstersPlace(null, firstChoice);
            playersBoard.setMonstersPlace(null, secondChoice);
            playersBoard.setMonstersPlace(null, thirdChoice);
            playersBoard.setSummonedMonster(selectedCard);
            playersBoard.removeCardFromHand(playersBoard.getSelectedHandPlace());
            whileSummonTrapsActivation(playersBoard, opponentBoard, currentMenu, selectedCard);
            playersBoard.deselectAll();
            GamePlay.setSummonOrSet(true);
        }

    }

    public void generalCardSet(GameBoard firstPlayerBoard, GameBoard secondPlayerBoard,
                               GamePhases currentPhase, boolean isSummonedOrSetInThisPhase) throws Exception {
        if (firstPlayerBoard.checkSelections() && secondPlayerBoard.checkSelections())
            throw new Exception("no card is selected yet");
        else {
            if (firstPlayerBoard.getHandSelectedCard() == null)
                throw new Exception("you can’t set this card");
            else {
                Card selectedCard = firstPlayerBoard.getHandSelectedCard();
                if (!currentPhase.equals(GamePhases.FIRST_MAIN) &&
                        !currentPhase.equals(GamePhases.SECOND_MAIN))
                    throw new Exception("you can’t do this action in this phase");
                else {
                    if (MonsterCard.isMonsterCard(selectedCard.getName()))
                        monsterSet(firstPlayerBoard,
                                MonsterCard.getMonsterCardByName(selectedCard.getName()), isSummonedOrSetInThisPhase);
                    else
                        spellTrapSet(firstPlayerBoard,
                                SpellTrapCard.getSpellTrapCardByName(selectedCard.getName()));
                }
            }
        }
    }

    public void monsterSet(GameBoard playerBoard, MonsterCard selectedCard,
                           boolean isSummonedOrSetInThisPhase) throws Exception {
        if (playerBoard.monsterPlacesSize() == 5)
            throw new Exception("monster card zone is full");
        else {
            if (isSummonedOrSetInThisPhase)
                throw new Exception("you already summoned/set on this turn");
            else {
                selectedCard.setSet(true);
                selectedCard.setSummoned(false);
                selectedCard.setDefensive(true);
                selectedCard.setReadyToAttack(false);
                playerBoard.setSummonedMonster(selectedCard);
                playerBoard.removeCardFromHand(playerBoard.getSelectedHandPlace());
                GamePlay.setSummonOrSet(true);
                playerBoard.deselectAll();
            }
        }
    }

    public void spellTrapSet(GameBoard playerBoard, SpellTrapCard selectedCard) throws Exception {
        if (playerBoard.spellTrapPlacesSize() == 5)
            throw new Exception("spell card zone is full");
        else {
            selectedCard.setSet(true);
            selectedCard.setEffectActive(false);
            playerBoard.setSpellTrapsPlace(selectedCard, playerBoard.spellTrapPlacesSize() + 1);
            playerBoard.removeCardFromHand(playerBoard.getSelectedHandPlace());
            playerBoard.deselectAll();
            GamePlay.setSummonOrSet(true);
        }
    }

    public void flipSummon(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard,
                           GamePhases currentPhase, GamePlay currentMenu) throws Exception {
        if (firstPlayersBoard.checkSelections() && secondPlayersBoard.checkSelections())
            throw new Exception("you dont select any card yet");
        else {
            if (firstPlayersBoard.getMonsterSelectedCard() == null)
                throw new Exception("you can’t change this cards position");
            else {
                MonsterCard monsterCard = firstPlayersBoard.getMonsterSelectedCard();
                if (!currentPhase.equals(GamePhases.FIRST_MAIN) &&
                        !currentPhase.equals(GamePhases.SECOND_MAIN))
                    throw new Exception("you can’t do this action in this phase");
                else {
                    if (!monsterCard.isSet() || !monsterCard.isReadyToAttack())
                        throw new Exception("you can’t flip summon this card");
                    else {
                        whileSummonTrapsActivation(firstPlayersBoard, secondPlayersBoard, currentMenu, monsterCard);
                        monsterCard.setSet(false);
                        monsterCard.setSummoned(true);
                        monsterCard.setReadyToAttack(true);
                        whileSummonTrapsActivation(firstPlayersBoard, secondPlayersBoard,
                                currentMenu, monsterCard);
                        firstPlayersBoard.deselectAll();
                        secondPlayersBoard.deselectAll();
                    }
                }
            }
        }
    }

    public boolean canAttackToCard(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard,
                                   GamePhases currentPhase, int number) throws Exception {

        if (firstPlayersBoard.checkSelections() && secondPlayersBoard.checkSelections())
            throw new Exception("you dont select any card yet");
        else {
            if (firstPlayersBoard.getMonsterSelectedCard() == null)
                throw new Exception("you cant attack with this card");
            else {
                MonsterCard monsterCard = firstPlayersBoard.getMonsterSelectedCard();
                if (!currentPhase.equals(GamePhases.BATTLE))
                    throw new Exception("you can’t do this action in this phase");
                else {
                    if (monsterCard.isDefensive())
                        throw new Exception("this card is in defensive position");
                    else {
                        if (monsterCard.isSet())
                            throw new Exception("you cant attack with set monster");
                        else {
                            if (!monsterCard.isReadyToAttack())
                                throw new Exception("this card already attacked");
                            else {
                                if (!secondPlayersBoard.getMonstersPlace().containsKey(number))
                                    throw new Exception("there is no card to attack here");
                                else
                                    return true;
                            }
                        }
                    }
                }
            }
        }
    }

    public String attackToCard(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard,
                               GamePlay currentMenu, int number) {

        whileAttackTrapsActivation(firstPlayersBoard, secondPlayersBoard, currentMenu);

        if (firstPlayersBoard.getMonsterSelectedCard().getName().equals("The Calculator")) {
            for (Map.Entry<Integer, MonsterCard> entry : firstPlayersBoard.getMonstersPlace().entrySet()) {
                if (entry.getValue() != null && entry.getValue().isSummoned())
                    firstPlayersBoard.getMonsterSelectedCard().setAttackPoint(
                            firstPlayersBoard.getMonsterSelectedCard().getAttackPoint() +
                                    entry.getValue().getLevel() * 300);
            }
        }

        firstPlayersBoard.getMonsterSelectedCard().setReadyToAttack(false);

        if (secondPlayersBoard.getMonstersPlace().get(number).isSummoned())
            return summonedState(firstPlayersBoard, secondPlayersBoard, number);
        else
            return setState(firstPlayersBoard, secondPlayersBoard, number);
    }

    public void whileAttackTrapsActivation(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard, GamePlay currentMenu) {
        if ((secondPlayersBoard.doesSpellTrapZoneContainsCard("Mirror Force") ||
                secondPlayersBoard.doesSpellTrapZoneContainsCard("Negate Attack")) &&
                !firstPlayersBoard.doesMonsterZoneContainsSummonedMirageDragon()) {
            if (currentMenu.wantToActiveEffect()) {

                String possibleCard = "";
                if (secondPlayersBoard.doesSpellTrapZoneContainsCard("Mirror Force"))
                    possibleCard += "Mirror Force";
                if (secondPlayersBoard.doesSpellTrapZoneContainsCard("Negate Attack"))
                    possibleCard += ", Negate Attack";

                String activatedCardName = currentMenu.activeEffect(possibleCard);
                if (activatedCardName.equals("Negate Attack"))
                    CardController.getInstance().negateAttackEffect(currentMenu, firstPlayersBoard, secondPlayersBoard);
                else if (activatedCardName.equals("Mirror Force"))
                    CardController.getInstance().mirrorForceEffect(firstPlayersBoard);
            }
        }
    }

    public void whileSummonTrapsActivation(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard,
                                           GamePlay currentMenu, MonsterCard summonedMonster) {
        if ((secondPlayersBoard.doesSpellTrapZoneContainsCard("Trap Hole") ||
                secondPlayersBoard.doesSpellTrapZoneContainsCard("Torrential Tribute")) &&
                !firstPlayersBoard.doesMonsterZoneContainsSummonedMirageDragon()) {
            if (currentMenu.wantToActiveEffect()) {

                String possibleCard = "";
                if (secondPlayersBoard.doesSpellTrapZoneContainsCard("Trap Hole"))
                    possibleCard += "Trap Hole";
                if (secondPlayersBoard.doesSpellTrapZoneContainsCard("Torrential Tribute"))
                    possibleCard += ", Torrential Tribute";

                String activatedCardName = currentMenu.activeEffect(possibleCard);
                if (activatedCardName.equals("Trap Hole"))
                    CardController.getInstance().trapHoleEffect(firstPlayersBoard, summonedMonster);
                else if (activatedCardName.equals("Torrential Tribute"))
                    CardController.getInstance().torrentialTributeEffect(firstPlayersBoard, secondPlayersBoard);
            }
        }

    }

    public String setState(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard, int number) {
        String opponentsCardName = secondPlayersBoard.getMonsterCardByPlace(number).getName();
        int firstPlayerAP = firstPlayersBoard.getMonsterSelectedCard().getAttackPoint();
        int secondPlayerDP = secondPlayersBoard.getMonsterCardByPlace(number).getDefencePoint();
        int damage = Math.abs(firstPlayerAP - secondPlayerDP);
        if (firstPlayerAP > secondPlayerDP) {
            secondPlayersBoard.addCardToGraveyard(secondPlayersBoard.getMonsterCardByPlace(number));
            yomiShipAndExploderDragonCheck(firstPlayersBoard, secondPlayersBoard, number);
            secondPlayersBoard.setMonstersPlace(null, number);
            firstPlayersBoard.deselectAll();
            secondPlayersBoard.deselectAll();
            return "opponent’s monster card was " + opponentsCardName + " and destroyed";
        } else if (firstPlayerAP < secondPlayerDP) {
            firstPlayersBoard.getPlayer().decreaseLP(damage);
            firstPlayersBoard.deselectAll();
            secondPlayersBoard.deselectAll();
            return "opponent’s monster card was " + opponentsCardName + " and no card is destroyed and you received " + damage + " battle damage";
        } else
            return "opponent’s monster card was " + opponentsCardName + " and no card is destroyed";
    }

    public String summonedState(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard, int number) {
        if (!secondPlayersBoard.getMonsterCardByPlace(number).isDefensive())
            return attackingState(firstPlayersBoard, secondPlayersBoard, number);
        else
            return defensiveState(firstPlayersBoard, secondPlayersBoard, number);
    }

    public String defensiveState(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard, int number) {
        int firstPlayerAP = firstPlayersBoard.getMonsterSelectedCard().getAttackPoint();
        int secondPlayerAP = secondPlayersBoard.getMonstersPlace().get(number).getDefencePoint();
        int damage = Math.abs(firstPlayerAP - secondPlayerAP);
        if (firstPlayerAP > secondPlayerAP) {
            secondPlayersBoard.addCardToGraveyard(secondPlayersBoard.getMonsterCardByPlace(number));
            yomiShipAndExploderDragonCheck(firstPlayersBoard, secondPlayersBoard, number);
            secondPlayersBoard.setMonstersPlace(null, number);
            firstPlayersBoard.deselectAll();
            secondPlayersBoard.deselectAll();
            return "the defense position monster is destroyed";
        } else if (firstPlayerAP < secondPlayerAP) {
            firstPlayersBoard.getPlayer().decreaseLP(damage);
            firstPlayersBoard.deselectAll();
            secondPlayersBoard.deselectAll();
            return "no card is destroyed and you received" + damage + " battle damage";
        } else
            return "no card is destroyed";
    }

    public String attackingState(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard, int number) {
        int firstPlayerAP = firstPlayersBoard.getMonsterSelectedCard().getAttackPoint();
        int secondPlayerAP = secondPlayersBoard.getMonstersPlace().get(number).getAttackPoint();
        int damage = Math.abs(firstPlayerAP - secondPlayerAP);
        if (firstPlayerAP > secondPlayerAP) {
            if (!secondPlayersBoard.getMonsterCardByPlace(number).getName().equals("Exploder Dragon"))
                secondPlayersBoard.getPlayer().decreaseLP(damage);
            secondPlayersBoard.addCardToGraveyard(secondPlayersBoard.getMonsterCardByPlace(number));
            yomiShipAndExploderDragonCheck(firstPlayersBoard, secondPlayersBoard, number);
            secondPlayersBoard.setMonstersPlace(null, number);
            firstPlayersBoard.deselectAll();
            secondPlayersBoard.deselectAll();
            return "your opponent’s monster is destroyed and your opponent receives" + damage + " battle damage";
        } else if (secondPlayerAP > firstPlayerAP) {
            firstPlayersBoard.getPlayer().decreaseLP(damage);
            firstPlayersBoard.addCardToGraveyard(firstPlayersBoard.getMonsterSelectedCard());
            firstPlayersBoard.setMonstersPlace(null, firstPlayersBoard.getSelectedMonsterPlace());
            firstPlayersBoard.deselectAll();
            secondPlayersBoard.deselectAll();
            return "Your monster card is destroyed and you received " + damage + " battle damage";
        } else {
            firstPlayersBoard.addCardToGraveyard(firstPlayersBoard.getMonsterSelectedCard());
            secondPlayersBoard.addCardToGraveyard(secondPlayersBoard.getMonsterCardByPlace(number));
            firstPlayersBoard.setMonstersPlace(null, firstPlayersBoard.getSelectedMonsterPlace());
            secondPlayersBoard.setMonstersPlace(null, number);
            firstPlayersBoard.deselectAll();
            secondPlayersBoard.deselectAll();
            return "both you and your opponent monster cards are destroyed and no one receives damage";
        }
    }

    private void yomiShipAndExploderDragonCheck(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard, int number) {
        if (secondPlayersBoard.getMonsterCardByPlace(number).getName().equals("Yomi Ship") ||
                secondPlayersBoard.getMonsterCardByPlace(number).getName().equals("Exploder Dragon")) {
            firstPlayersBoard.addCardToGraveyard(firstPlayersBoard.getMonsterSelectedCard());
            firstPlayersBoard.setMonstersPlace(null, firstPlayersBoard.getSelectedMonsterPlace());
        }
    }

    public int directAttack(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard,
                            GamePhases currentPhase, GamePlay currentMenu,
                            boolean isFirstTime) throws Exception {
        if (firstPlayersBoard.checkSelections() && secondPlayersBoard.checkSelections())
            throw new Exception("you dont select any card yet");
        else {
            if (firstPlayersBoard.getMonsterSelectedCard() == null)
                throw new Exception("you cant attack with this card");
            else {
                MonsterCard monsterCard = firstPlayersBoard.getMonsterSelectedCard();
                if (!currentPhase.equals(GamePhases.BATTLE))
                    throw new Exception("you can’t do this action in this phase");
                else {
                    if (!monsterCard.isReadyToAttack())
                        throw new Exception("this card already attacked");
                    else {
                        if (isFirstTime || secondPlayersBoard.monsterPlacesSize() > 0)
                            throw new Exception("you can’t attack the opponent directly");
                        else {
                            whileAttackTrapsActivation(firstPlayersBoard, secondPlayersBoard, currentMenu);
                            int lp = secondPlayersBoard.getPlayer().getLP();
                            int attackPoint = monsterCard.getAttackPoint();
                            secondPlayersBoard.getPlayer().setLP(lp - attackPoint);
                            monsterCard.setReadyToAttack(false);
                            firstPlayersBoard.deselectAll();
                            secondPlayersBoard.deselectAll();
                            return attackPoint;
                        }
                    }
                }
            }
        }
    }

    public void resetMonsters(GameBoard firstPlayersBoard) {
        for (Map.Entry<Integer, MonsterCard> entry : firstPlayersBoard.getMonstersPlace().entrySet()) {
            if (entry.getValue() != null)
                entry.getValue().setReadyToAttack(true);
        }
    }

    public void changePositionReset(GameBoard playerBoard) {
        for (Map.Entry<Integer, MonsterCard> entry : playerBoard.getMonstersPlace().entrySet()) {
            if (entry.getValue() != null)
                entry.getValue().setPositionChangedInThisTurn(false);
        }
    }

    public String showGraveyard(GameBoard firstPlayersBoard) {
        if (firstPlayersBoard.getGraveYard().size() == 0)
            return "graveyard empty";
        else {
            StringBuilder result = new StringBuilder();
            for (Card card : firstPlayersBoard.getGraveYard())
                result.append(card.toString()).append("\n");
            return result.toString();
        }
    }

    public String showSelectedCard(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard) throws Exception {
        if (firstPlayersBoard.checkSelections() && secondPlayersBoard.checkSelections())
            throw new Exception("no card is selected yet");
        else {
            Card yourCard = firstPlayersBoard.getSelectedCard();
            Card opponentCard = secondPlayersBoard.getSelectedCard();
            if (yourCard != null) {
                if (MonsterCard.isMonsterCard(yourCard.getName()))
                    return ((MonsterCard) yourCard).exclusiveToString();
                else
                    return ((SpellTrapCard) yourCard).exclusiveToString();
            } else {
                if (opponentCard.isSet())
                    return "card is not visible";
                else {
                    if (MonsterCard.isMonsterCard(opponentCard.getName()))
                        return ((MonsterCard) opponentCard).exclusiveToString();
                    else
                        return ((SpellTrapCard) opponentCard).exclusiveToString();
                }
            }
        }
    }

    public void changePosition(GameBoard firstPlayerBoard,
                               GameBoard secondPlayerBoard, String selectedPosition,
                               GamePhases currentPhase) throws Exception {
        if (firstPlayerBoard.checkSelections() && secondPlayerBoard.checkSelections())
            throw new Exception("no card is selected yet");
        else {
            if (firstPlayerBoard.getMonsterSelectedCard() == null)
                throw new Exception("you can’t change this card position");
            else {
                MonsterCard selectedMonsterCard = firstPlayerBoard.getMonsterSelectedCard();
                if (!currentPhase.equals(GamePhases.FIRST_MAIN) &&
                        !currentPhase.equals(GamePhases.SECOND_MAIN))
                    throw new Exception("you can’t do this action in this phase");
                else {
                    if ((selectedPosition.equals("attack") && selectedMonsterCard.isSummoned() && !selectedMonsterCard.isDefensive()) ||
                            (selectedPosition.equals("defense") && selectedMonsterCard.isDefensive()))
                        throw new Exception("this card is already in the wanted position");
                    else {
                        if (selectedMonsterCard.isPositionChangedInThisTurn())
                            throw new Exception("you already changed this card position in this turn");
                        else {
                            selectedMonsterCard.setDefensive(!selectedPosition.equals("attack"));
                            selectedMonsterCard.setPositionChangedInThisTurn(true);
                            firstPlayerBoard.deselectAll();
                            secondPlayerBoard.deselectAll();
                        }
                    }
                }
            }
        }
    }

    public void changePhase(GameBoard firstPlayersBoard, GameBoard secondPlayersBoard,
                            GamePhases currentPhase, GamePlay currentMenu) {
        if (currentPhase.equals(GamePhases.DRAW)) GamePlay.setPhase(GamePhases.STANDBY);
        else if (currentPhase.equals(GamePhases.STANDBY)) GamePlay.setPhase(GamePhases.FIRST_MAIN);
        else if (currentPhase.equals(GamePhases.FIRST_MAIN)) {
            if (secondPlayersBoard.doesSpellTrapZoneContainsCard("Time Seal") &&
                    !firstPlayersBoard.doesMonsterZoneContainsSummonedMirageDragon()) {
                if (currentMenu.wantToActiveEffect()) {
                    String activatedCardName = currentMenu.activeEffect("Time Seal");
                    if (activatedCardName.equalsIgnoreCase("Time Seal"))
                        CardController.getInstance().timeSealEffect(firstPlayersBoard);
                }
            }
            GamePlay.setPhase(GamePhases.BATTLE);
        } else if (currentPhase.equals(GamePhases.BATTLE))
            GamePlay.setPhase(GamePhases.SECOND_MAIN);
        else if (currentPhase.equals(GamePhases.SECOND_MAIN)) {
            resetMonsters(firstPlayersBoard);
            GamePlay.setPhase(GamePhases.END);
        }
    }

    public String addOneCardToHand(GameBoard playersBoard) {
        Card toAddedCard = playersBoard.getMainDeckCards().get(0);
        playersBoard.addCardToHand(toAddedCard);
        playersBoard.getMainDeckCards().remove(0);
        return toAddedCard.getName();
    }

    public void setWinner(GameBoard firstPlayerBoard, GameBoard secondPlayerBoard,
                          int rounds, Menu mainMenu, GamePlay currentMenu, boolean isSurrender) {
        User winner = firstPlayerBoard.getPlayer();
        User loser = secondPlayerBoard.getPlayer();
        if (rounds == 1) {
            winner.setScore(winner.getScore() + 1000);
            winner.setWins(winner.getWins() + 1);
            loser.setLoses(loser.getLoses() + 1);
            winner.setBalance(winner.getLP() + 1000 + winner.getBalance());
            loser.setBalance(loser.getBalance() + 100);
            System.out.println(winner.getUsername() + " won the whole match with score: " +
                    winner.getScore() + "-" + loser.getScore());
            mainMenu.run();
        } else {
            if (firstPlayerBoard.getWinsCount() == 0) {
                firstPlayerBoard.setWinsCount(firstPlayerBoard.getWinsCount() + 1);
                if (!isSurrender)
                    firstPlayerBoard.setMaxLP(winner.getLP());
                winner.setLP(8000);
                loser.setLP(8000);
                System.out.println(winner.getUsername() + " won the game and the score is: " +
                        winner.getScore() + "-" + loser.getScore());

                if (currentMenu.exchangeCardsCheck(winner.getNickname()))
                    currentMenu.getCardsName(winner);
                if (currentMenu.exchangeCardsCheck(loser.getNickname()))
                    currentMenu.getCardsName(loser);


                gamePreparation(mainMenu, loser, winner, rounds, secondPlayerBoard, firstPlayerBoard);
            } else {
                winner.setScore(winner.getScore() + 3000);
                winner.setWins(winner.getWins() + 1);
                loser.setLoses(loser.getLoses() + 1);
                winner.setBalance(winner.getBalance() + 3000 + 3 * firstPlayerBoard.getMaxLP());
                loser.setBalance(loser.getBalance() + 300);
                System.out.println(winner.getUsername() + " won the whole match with score: " +
                        winner.getScore() + "-" + loser.getScore());
                mainMenu.run();
            }
        }
    }

    public void exchangeCard(User player, String cardsName) throws Exception {
        Deck activeDeck = Deck.getDeckByName(player.getActiveDeck());
        String[] cardsNameSplit = cardsName.split(",");
        String mainDecksCard = cardsNameSplit[0];
        String sideDecksCard = cardsNameSplit[1];
        if (activeDeck.doesMainDeckHasThisCard(mainDecksCard) &&
                activeDeck.doesSideDeckHasThisCard(sideDecksCard)) {
            activeDeck.removeCardFromSideDeck(sideDecksCard);
            activeDeck.addCardToMainDeck(mainDecksCard);
        } else
            throw new Exception("your entered card does not exists in Main/Side Deck");
    }

    public Card extractCard(GameBoard firstPlayerBoard, GameBoard secondPlayerBoard) {
        Card card;
        card = firstPlayerBoard.getSelectedCard();
        if (card != null)
            return card;
        card = secondPlayerBoard.getSelectedCard();
        return card;
    }


    public static DuelController getInstance() {
        if (instance == null)
            instance = new DuelController();
        return instance;
    }
}
