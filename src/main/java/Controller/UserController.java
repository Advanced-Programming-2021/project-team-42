package Controller;

import Model.User;

public class UserController {
    private static UserController instance = null;
    private User loggedInUser;

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

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public static UserController getInstance() {
        if (instance == null)
            instance = new UserController();
        return instance;
    }
}
