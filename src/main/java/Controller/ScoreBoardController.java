package Controller;

import Model.User;
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

    public String showScoreboard() {
        StringBuilder result = new StringBuilder();
        int realRank = 1;
        int shownRank = 1;
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
            result.append(shownRank).append("- ").append(user.getNickname()).
                    append(": ").append(user.getScore()).append("\n");
            realRank++;
        }
        return result.toString();
    }

    public void showScoreboard(VBox vBox) {
        int realRank = 1;
        int shownRank = 1;
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

            Text text = new Text(shownRank + "- " + user.getNickname() + ": " + user.getScore() + "\n");
            if (user.getUsername().equals(UserController.getInstance().getLoggedInUser().getUsername()))
                text.setFill(Color.BLACK);
            else
                text.setFill(Color.WHITE);
            Font font = new Font("Book Antiqua", 24);
            text.setFont(font);
            vBox.getChildren().add(text);
            realRank++;

        }
    }


    public static ScoreBoardController getInstance() {
        if (instance == null)
            instance = new ScoreBoardController();
        return instance;
    }
}
