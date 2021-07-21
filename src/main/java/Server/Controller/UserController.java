package Server.Controller;

import Server.Model.GameBoard;
import Server.Model.User;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.HashMap;

public class UserController {
    private static UserController instance = null;
    private final static File[] allProfilePicsPath = new File("src/main/resources/Assets/ProfileImages").listFiles();
    private static int counter = 0;
    private static String shownImageName;
    private static HashMap<String, User> loggedInUsers = new HashMap<>();
    private User opponentUser;
    private GameBoard firstPlayersBoard;
    private GameBoard secondPlayersBoard;

    private UserController() {
    }

    public void changeNickname(String username, String nickname) throws Exception{
        if (User.getUserByNickname(nickname) != null)
            throw new Exception("user with nickname " + nickname + " already exists");
        else
            User.getUserByUsername(username).setNickname(nickname);
    }

    public void changePassword(String username, String currentPassword, String newPassword) throws Exception{
        if (!isPasswordCorrect(username, currentPassword))
            throw new Exception("Current password is invalid");
        else {
            if (currentPassword.equals(newPassword))
                throw new Exception("Please enter a new password");
            else
                User.getUserByUsername(username).setPassword(newPassword);
        }
    }

    public static HashMap<String, User> getLoggedInUsers() {
        return loggedInUsers;
    }

    //    public void load (Label username, Label nickname, ImageView profilePic){
//        username.setText("Username: " + loggedInUser.getUsername());
//        nickname.setText("Nickname: " + loggedInUser.getNickname());
//        profilePic.setImage(new Image(getClass().getResource(loggedInUser.getPathToProfilePhoto()).toExternalForm()));
//    }

    public void increaseMoney(User user, int amount) {
        user.setBalance(user.getBalance() + amount);
    }

    public void increaseLP(User user, int LP) {
        user.setLP(user.getLP() + LP);
    }

    public boolean isPasswordCorrect(String username, String password) {
        User user = User.getUserByUsername(username);
        return user.getPassword().equals(password);
    }

    public static UserController getInstance() {
        if (instance == null)
            instance = new UserController();
        return instance;
    }

    public static User getUserByToken (String token) {
        return loggedInUsers.get(token);
    }

    public static void addUser (String token, User user){
        loggedInUsers.put(token, user);
    }

    public static void removeUser (String token) {
        loggedInUsers.remove(token);
    }

    public User getOpponentUser() {
        return opponentUser;
    }

    public void setOpponentUser(User opponentUser) {
        this.opponentUser = opponentUser;
    }

    public GameBoard getFirstPlayersBoard() {
        return firstPlayersBoard;
    }

    public void setFirstPlayersBoard(GameBoard firstPlayersBoard) {
        this.firstPlayersBoard = firstPlayersBoard;
    }

    public GameBoard getSecondPlayersBoard() {
        return secondPlayersBoard;
    }

    public void setSecondPlayersBoard(GameBoard secondPlayersBoard) {
        this.secondPlayersBoard = secondPlayersBoard;
    }

    public void loadImage(ImageView shownProfilePic, boolean isNext) {
        if(isNext){
            if (counter == allProfilePicsPath.length - 1)
                counter = 0;
            else
                counter++;
        } else {
            if(counter == 0)
                counter = allProfilePicsPath.length - 1;
            else
                counter--;
        }
        File imageFile = allProfilePicsPath[counter];
        shownProfilePic.setImage(new Image(getClass().getResource("/Assets/ProfileImages/" + imageFile.getName()).toExternalForm()));
    }

//    public void setProfilePic(ImageView profilePic) {
//        shownImageName = allProfilePicsPath[counter].getName();
//        loggedInUser.setPathToProfilePhoto("/Assets/ProfileImages/" + shownImageName);
//        shownImageName = null;
//        counter = 0;
//        profilePic.setImage(new Image(getClass().getResource(loggedInUser.getPathToProfilePhoto()).toExternalForm()));
//    }
}
