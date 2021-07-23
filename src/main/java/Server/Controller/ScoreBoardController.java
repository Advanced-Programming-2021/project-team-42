package Server.Controller;

import Server.Model.User;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Comparator;

public class ScoreBoardController {
    private static ScoreBoardController instance = null;

    private ScoreBoardController() {
    }

    public synchronized String showScoreboard() {
        int realRank = 1;
        int shownRank = 1;
        StringBuilder result = new StringBuilder();
        ArrayList<User> allUsers = User.getAllUsers();
        Comparator<User> comparator = Comparator.comparing(User::getScore, Comparator.reverseOrder()).
                thenComparing(User::getNickname);
        allUsers.sort(comparator);
        int maxScore = allUsers.get(0).getScore();
        for (User user : allUsers) {
            if (user.getScore() != maxScore) {
                maxScore = user.getScore();
                shownRank = realRank;
            }

            result.append(shownRank).append("- ").append(user.getNickname()).append(": ").append(user.getScore()).append(UserController.getLoggedInUsers().containsValue(user) ? "(Online)" : "").append("\n");
            realRank++;
        }
        return result.toString();
    }


    public static ScoreBoardController getInstance() {
        if (instance == null)
            instance = new ScoreBoardController();
        return instance;
    }
}
