package Model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private static ArrayList<User> allUsers;
    private HashMap<String, Integer> usersAllCards;
    private ArrayList<String> userDecks;
    private String username;
    private String password;
    private String nickname;
    private String activeDeck;
    private int balance;
    private int score;
    private int LP;

    static {
        allUsers = new ArrayList<>();
    }

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.activeDeck = null;
        this.balance = 100000;
        this.score = 0;
        this.LP = 0;
        usersAllCards = new HashMap<>();
        userDecks = new ArrayList<>();
        allUsers.add(this);
    }

    public static User getUserByUsername(String username) {
        for (User user : allUsers) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public void addDeckToList(String deckName){
        userDecks.add(deckName);
    }

    public void removeDeckFromList(String deckName){
        userDecks.remove(deckName);
    }

    public static void addUserToList(User user) {
        allUsers.add(user);
    }

    public void setActiveDeck(String activeDeck) {
        this.activeDeck = activeDeck;
    }

    public String getActiveDeck() {
        return activeDeck;
    }

    public boolean doesUserHasThisCard(String cardName){
        for(Map.Entry<String, Integer> entry : usersAllCards.entrySet()){
            if(entry.getKey().equals(cardName))
                return true;
        }
        return false;
    }

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public ArrayList<String> getUserDecks() {
        return this.userDecks;
    }

    public HashMap<String, Integer> getUserAllCards() {
        return this.usersAllCards;
    }

    public void increaseCard(String cardName) {
        if(usersAllCards.containsKey(cardName))
            usersAllCards.put(cardName, usersAllCards.get(cardName) + 1);
        else
            usersAllCards.put(cardName, 1);
    }

    public void decreaseCard(String cardName){
        usersAllCards.put(cardName, usersAllCards.get(cardName) - 1);
        if(usersAllCards.get(cardName) == 0)
            usersAllCards.remove(cardName);
    }

    public int getLP() {
        return LP;
    }

    public void setLP(int LP) {
        this.LP = LP;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
