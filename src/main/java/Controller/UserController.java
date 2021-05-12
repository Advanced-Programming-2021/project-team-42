package Controller;

import Model.User;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserController {
    private static UserController instance = null;
    private static final String FILE_PATH = "C:\\Users\\Vision\\IdeaProjects\\Game First Phase\\src\\main\\java\\Database\\Users";

    private UserController() {
    }

    public boolean isUserWithThisFieldExists(String field, int flag) {
        try {
            Gson gson = new Gson();
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

    public void changeNickname(String username, String nickname){
        try {
            if(checkNicknameExistence(nickname))
                System.out.println("user with nickname " + nickname + " already exists");
            else {
                User user = User.getUserByUsername(username);
                user.setNickname(nickname);
                updateUsersFile(username, user);
                System.out.println("Nickname successfully changed!");
            }
        }catch (Exception e){
            System.out.println("Can not Change your Nickname!\n" +
                    "Try again");
            e.printStackTrace();
        }
    }

    private void updateUsersFile(String username, User user) throws IOException {
        Gson gson = new Gson();
        File dir = new File(FILE_PATH);
        File[] allFiles = dir.listFiles();
        if (allFiles != null) {
            for (File file : allFiles) {
                if (file.getName().equals(username + ".json")) {
                    FileWriter fileWriter = new FileWriter(FILE_PATH + "\\" + file.getName());
                    gson.toJson(user, fileWriter);
                    fileWriter.close();
                }
            }
        }
    }

    public void changePassword(String username, String currentPassword, String newPassword){
        try {
            if (!isPasswordCorrect(username, currentPassword))
                System.out.println("Current password is invalid");
            else {
                if (currentPassword.equals(newPassword))
                    System.out.println("Please enter a new password");
                else {
                    User user = User.getUserByUsername(username);
                    user.setPassword(newPassword);
                    updateUsersFile(username, user);
                    System.out.println("Password successfully changed!");
                }
            }
        }catch (Exception e){
            System.out.println("Can not Change your Password!\n" +
                    "Try again");
            e.printStackTrace();
        }
    }

    public boolean isPasswordCorrect(String username, String password){
        User user = User.getUserByUsername(username);
        return user.getPassword().equals(password);
    }


    public boolean checkNicknameExistence(String nickname){
        for(User user : User.getAllUsers()){
            if(user.getNickname().equals(nickname))
                return true;
        }
        return false;
    }

    public static UserController getInstance() {
        if (instance == null)
            instance = new UserController();
        return instance;
    }
}
