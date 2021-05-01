package Controller;

import Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RegisterController {
    private static final String FILE_PATH = "C:\\Users\\Vision\\IdeaProjects\\Game First Phase\\src\\main\\java\\Database";
    private static RegisterController instance = null;

    private RegisterController(){}

    public void createNewUser(String username, String nickname, String password){
        try{
            User user = new User(username, password, nickname);
            Gson gson = new GsonBuilder().create();
            FileWriter fileWriter = new FileWriter(FILE_PATH + "\\" + user.getUsername() + ".json");
            gson.toJson(user, fileWriter);
            fileWriter.close();
            System.out.println("User created successfully");

        }
        catch (IOException e){
            System.out.println("Could not create user\n" +
                    "Please try again");
        }
    }


    public static RegisterController getInstance(){
        if(instance == null)
            instance = new RegisterController();
        return instance;
    }
}