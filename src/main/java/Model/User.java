package Model;


import java.util.HashMap;

public class User {
    private HashMap<String, Card> usersAllCards;
    private String username;
    private String password;
    private String nickname;
    private int balance;
    private int score;
    private int LP;
    private boolean isLoggedIn;

    public User(String username, String password, String nickname){
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.balance = 50000;
        this.score = 0;
        this.LP = 0;
        usersAllCards = new HashMap<>();
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
