package Controller;

import Model.User;

import java.util.ArrayList;
import java.util.Comparator;

public class ScoreBoardController {
    private static ScoreBoardController instance = null;

    private ScoreBoardController(){}

    public void showScoreboard(){
        int rank = 1;
        ArrayList<User> allUsers = User.getAllUsers();
        Comparator<User> sortUsers = Comparator.comparing(User::getScore , Comparator.reverseOrder()).
                thenComparing(User::getNickname);
        allUsers.sort(sortUsers);
        for(User user : allUsers) {
            System.out.println(rank + "- " + user.getNickname() + ": " + user.getScore());
            rank++;
        }
        System.out.println("\n");
    }


    public static ScoreBoardController getInstance(){
        if(instance == null)
            instance = new ScoreBoardController();
        return instance;
    }
}
