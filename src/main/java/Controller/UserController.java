package Controller;

import Model.User;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class UserController {
    private static UserController instance = null;
    private static final String FILE_PATH = "C:\\Users\\Vision\\IdeaProjects\\Game First Phase\\src\\main\\java\\Database";

    private UserController() {
    }

//    public boolean isUserExists(String username) {
//        Gson gson = new Gson();
//        try {
//            File allUsers = new File(FILE_PATH);
//            File[] allUsersArray = allUsers.listFiles();
//            if (allUsersArray != null) {
//                for (File file : allUsersArray) {
//                    FileReader fileReader = new FileReader(FILE_PATH + "\\" + file.getName());
//                    User user = gson.fromJson(fileReader, User.class);
//                    if(user.getNickname().equals(username))
//                        return true;
//                }
//            }
//        }catch(IOException e){
//            System.out.println("Could not read information from file");
//        }
//        return false;
//    }

    public boolean isUserWithThisFieldExists(String field, int flag) {
        Gson gson = new Gson();
        try {
            File allUsers = new File(FILE_PATH);
            File[] allUsersArray = allUsers.listFiles();
            if (allUsersArray != null) {
                for (File file : allUsersArray) {
                    FileReader fileReader = new FileReader(FILE_PATH + "\\" + file.getName());
                    User user = gson.fromJson(fileReader, User.class);
                    if(flag == 1) {
                        if (user.getNickname().equals(field))
                            return true;
                    }
                    else{
                        if (user.getUsername().equals(field))
                            return true;
                    }
                }
            }
        }catch(IOException e){
            System.out.println("Could not read information from file");
        }
        return false;
    }


    public static UserController getInstance() {
        if (instance == null)
            instance = new UserController();
        return instance;
    }
}
