package Controller;

import Model.User;

public class UserController {
    private static UserController instance = null;
    public String userName;

    private UserController() {
    }

    public String changeNickname(String username, String nickname) {
        if (User.getUserByNickname(nickname) != null)
            return ("user with nickname " + nickname + " already exists");
        else {
            User.getUserByUsername(username).setNickname(nickname);
        }
        return "nickname changed successfully";
    }

    public String changePassword(String username, String currentPassword, String newPassword) {
        if (!isPasswordCorrect(username, currentPassword))
            return ("Current password is invalid");
        else {
            if (currentPassword.equals(newPassword))
                return ("Please enter a new password");
            else
                User.getUserByUsername(username).setPassword(newPassword);
            return "password changed successfully";
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


    public static UserController getInstance() {
        if (instance == null)
            instance = new UserController();
        return instance;
    }
}
