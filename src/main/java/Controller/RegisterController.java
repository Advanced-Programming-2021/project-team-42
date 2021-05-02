package Controller;

import Model.User;
import View.MainMenu;
import View.Menu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RegisterController {
    private static final String FILE_PATH = "C:\\Users\\Vision\\IdeaProjects\\Game First Phase\\src\\main\\java\\Database";
    private static RegisterController instance = null;

    private RegisterController() {
    }

    public void createNewUser(String username, String nickname, String password) {
        if (UserController.getInstance().isUserWithThisFieldExists(username, 0))
            System.out.println("User with username " + username + " already exists");
        else {
            if (UserController.getInstance().isUserWithThisFieldExists(nickname, 1))
                System.out.println("User with nickname " + nickname + " already exists");
            else {
                try {
                    User user = new User(username, password, nickname);
                    Gson gson = new GsonBuilder().create();
                    FileWriter fileWriter = new FileWriter(FILE_PATH + "\\" + user.getUsername() + ".json");
                    gson.toJson(user, fileWriter);
                    fileWriter.close();
                    System.out.println("User created successfully");

                } catch (IOException e) {
                    System.out.println("Could not create user\n" +
                            "Please try again");
                }
            }
        }
    }

    public void loginUser(String username, String password, Menu parentMenu) {
        try {
            Gson gson = new Gson();
            if (!UserController.getInstance().isUserWithThisFieldExists(username, 0))
                System.out.println("Username and password did not match!");
            else {
                FileReader fileReader = new FileReader(FILE_PATH + "\\" + username + ".json");
                User user = gson.fromJson(fileReader, User.class);
                if (!user.getPassword().equals(password))
                    System.out.println("Username and password did not match!");
                else {
                    System.out.println("You logged in successfully!");
                    MainMenu mainMenu = new MainMenu(parentMenu);
                    mainMenu.run();
                }
            }
        } catch (Exception e) {
            System.out.println("Could not do login process!\n" +
                    "Please try again");
        }
    }

    public static RegisterController getInstance() {
        if (instance == null)
            instance = new RegisterController();
        return instance;
    }
}