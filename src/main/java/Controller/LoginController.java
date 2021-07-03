package Controller;

import Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LoginController {
    private static final String FILE_PATH = "src\\main\\java\\Database\\Users";
    private static LoginController instance = null;
    private static FileWriter FILE_WRITER;
    private static FileReader FILE_READER;

    static {
        RegisterController.parseUsers();
        CardController.parseCards();
        DeckController.parseDecks();
    }

    private LoginController() {
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

    public boolean canSignUp(String username, String nickname, String password) {
        if (User.getUserByUsername(username) != null)
            return false;
        else {
            if (User.getUserByNickname(nickname) != null)
                return false;
            else {
                createNewUser(username ,nickname ,password);
                return true;
            }
        }
    }

    public void createNewUser(String username, String nickname, String password) {
        User user = new User(username, password, nickname);
        rewriteData();
    }

    public boolean canLogin(String username, String password) {
        if (User.getUserByUsername(username) == null)
            return false;
        else {
            User user = User.getUserByUsername(username);
            if (!user.getPassword().equals(password))
                return false;
            else {
                return true;
            }
        }
    }

    public static void parseUsers() {
        try {
            User user;
            Gson gson = new Gson();
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

    public static LoginController getInstance() {
        if (instance == null)
            instance = new LoginController();
        return instance;
    }
}