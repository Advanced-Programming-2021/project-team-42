package Test;

import Controller.RegisterController;
import Model.User;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.function.Executable;

public class TestClass {
    @BeforeEach
    public void clearData(){
        User.getAllUsers().clear();
    }

    @Test
    @DisplayName("Single User Signup Without Throwing Exception")
    public void singleUserSignup() {
        Executable executable = () -> RegisterController.getInstance().createNewUser("username", "nickname", "password");

        Assertions.assertDoesNotThrow(executable);
    }

    @Test
    @DisplayName("Creating Two User with Identical Username and Nickname")
    public void manyUserSignup(){
        Executable executable = () -> {
            RegisterController.getInstance().createNewUser("username", "nickname", "password");
            RegisterController.getInstance().createNewUser("anotherUsername", "anotherNickname", "password");
        };

        Assertions.assertDoesNotThrow(executable);
    }

    @Test
    @DisplayName("Throw Exception if There is Another User With Same Username")
    public void sameUsernameException(){
        Executable executable = () -> {
            RegisterController.getInstance().createNewUser("username", "nickname", "password");
            RegisterController.getInstance().createNewUser("username", "anotherNickname", "password");
        };

        Assertions.assertThrows(Exception.class, executable);
    }

    @Test
    @DisplayName("Throw Exception if There is Another User With Same Nickname")
    public void sameNicknameException(){
        Executable executable = () -> {
            RegisterController.getInstance().createNewUser("username", "nickname", "password");
            RegisterController.getInstance().createNewUser("anotherUsername", "nickname", "password");
        };

        Assertions.assertThrows(Exception.class, executable);
    }
}
