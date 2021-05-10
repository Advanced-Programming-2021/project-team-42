package Model;


import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private static ArrayList<User> allUsers;
    private HashMap<String, Integer> usersAllCards;
    private String username;
    private String password;
    private String nickname;
    private int balance;
    private int score;
    private int LP;
    private boolean isLoggedIn;

    static {
        allUsers = new ArrayList<>();
    }

    public User(String username, String password, String nickname){
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.balance = 100000;
        this.score = 0;
        this.LP = 0;
        usersAllCards = new HashMap<>();
    }

    public static User getUserByUsername(String username){
        for(User user : allUsers){
            if(user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public static void addUserToList(User user){
        allUsers.add(user);
    }

    public static ArrayList<User> getAllUsers(){
        return allUsers;
    }

    public void increaseCard(String cardName){
        usersAllCards.put(cardName, usersAllCards.get(cardName) + 1);
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
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
