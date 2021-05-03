package Controller;

import Model.User;
import View.MainMenu;
import View.Menu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
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
                System.out.println("Username and Password did not match!");
            else {
                FileReader fileReader = new FileReader(FILE_PATH + "\\" + username + ".json");
                User user = gson.fromJson(fileReader, User.class);
                if (!user.getPassword().equals(password))
                    System.out.println("Username and Password did not match!");
                else {
                    if(parseUsers()){
                        System.out.println("You logged in successfully!");
                        MainMenu mainMenu = new MainMenu(parentMenu);
                        mainMenu.setUsersName(username);
                        mainMenu.run();
                    }
                    else{
                        System.out.println("An error occurred while Logging you in!\n" +
                                "Please try again later!");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Could not do login process!\n" +
                    "Please try again");
        }
    }

    public boolean parseUsers(){
        try {
            User user;
            FileReader fileReader;
            Gson gson = new Gson();
            File directory = new File(FILE_PATH);
            File[] usersArray = directory.listFiles();
            if (usersArray != null) {
                for (File file : usersArray) {
                    fileReader = new FileReader(FILE_PATH + "\\" + file.getName());
                    user = gson.fromJson(fileReader, User.class);
                    User.addUserToList(user);
                }
            }
            return true;
        }catch (Exception e){
            System.out.println("Can not load Users from file!");
            e.printStackTrace();
            return false;
        }
    }

    public static RegisterController getInstance() {
        if (instance == null)
            instance = new RegisterController();
        return instance;
    }
}