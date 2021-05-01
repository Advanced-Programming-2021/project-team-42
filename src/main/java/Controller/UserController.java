package Controller;

import java.io.File;

public class UserController {
    private static UserController instance = null;
    private UserController(){}

    public boolean isUserExists(String username) {
        File allUsers = new File("C:\\Users\\Vision\\IdeaProjects\\Game First Phase\\src\\main\\java\\Database");
        File[] allUsersArray = allUsers.listFiles();
        if (allUsersArray != null) {
            for (File file : allUsersArray) {
                if (file.getName().equals(username))
                    return true;
            }
        }
        return false;
    }


    public static UserController getInstance() {
        if(instance == null)
            instance = new UserController();
        return instance;
    }
}
