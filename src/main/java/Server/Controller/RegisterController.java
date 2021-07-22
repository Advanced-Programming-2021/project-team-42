package Server.Controller;

import Server.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class RegisterController {
    private static final String FILE_PATH = "src\\main\\java\\Database\\Users";
    private static RegisterController instance = null;
    private static FileWriter FILE_WRITER;
    private static FileReader FILE_READER;

    private RegisterController() {
    }

    public static void rewriteData() {
        Gson gson = new GsonBuilder().create();
        for (User user : User.getAllUsers()) {
            try {
                FILE_WRITER = new FileWriter(FILE_PATH + "\\" + user.getUsername() + ".json");
                gson.toJson(user, FILE_WRITER);
                FILE_WRITER.close();
            } catch (IOException e) {
                System.out.println("An error occurred while writing user data!");
            }
        }
    }

    public synchronized void createNewUser(String username, String nickname, String password) throws Exception {
        if (User.getUserByUsername(username) != null)
            throw new Exception("User with username " + username + " already exists");
        else {
            if (User.getUserByNickname(nickname) != null)
                throw new Exception("User with nickname " + nickname + " already exists");
            else {
                User user = new User(username, password, nickname);
                rewriteData();
            }
        }
    }

    public synchronized String loginUser(String username, String password) throws Exception {
        if (User.getUserByUsername(username) == null)
            throw new Exception("Username and Password did not match!");
        else {
            User user = User.getUserByUsername(username);
            if (!user.getPassword().equals(password))
                throw new Exception("Username and Password did not match!");
            else{
                String token = UUID.randomUUID().toString();
                UserController.addUser(token, user);
                return token;
            }
        }
    }

    public static void parseUsers() {
        try {
            User user;
            Gson gson = new GsonBuilder().create();
            File directory = new File(FILE_PATH);
            File[] usersArray = directory.listFiles();
            if (usersArray != null) {
                for (File file : usersArray) {
                    FILE_READER = new FileReader(FILE_PATH + "\\" + file.getName());
                    user = gson.fromJson(FILE_READER, User.class);
                    FILE_READER.close();
                    User.addUserToList(user);
                }
            }
        } catch (Exception e) {
            System.out.println("Can not load Users from file!");
        }
    }

    public static RegisterController getInstance() {
        if (instance == null)
            instance = new RegisterController();
        return instance;
    }
}