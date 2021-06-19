package Controller;

import Model.User;

import java.util.ArrayList;
import java.util.Comparator;

public class ScoreBoardController {
    private static ScoreBoardController instance = null;

    private ScoreBoardController() {
    }

    public String showScoreboard() {
        StringBuilder result = new StringBuilder();
        int realRank = 1;
        int shownRank = 1;
        ArrayList<User> allUsers = User.getAllUsers();
        Comparator<User> comparator = Comparator.comparing(User::getScore, Comparator.reverseOrder()).
                thenComparing(User::getUsername);
        allUsers.sort(comparator);
        int maxScore = allUsers.get(0).getScore();
        for (User user : allUsers) {
            if (user.getScore() != maxScore) {
                maxScore = user.getScore();
                shownRank = realRank;
            }
            result.append(shownRank).append("- ").append(user.getNickname()).
                    append(": ").append(user.getScore()).append("\n");
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
