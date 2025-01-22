package userTest;

import API.API;
import data.UserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

public class RegistrationTest {
    private static String userAccessToken;


    @After
    public void tearDown() {
        if (userAccessToken != null) {
            ValidatableResponse deleteResponse = API.deleteUser(userAccessToken);
            API.checkDeleteUser(deleteResponse);
        }
    }
    @Test
    @DisplayName("Регистрация нового пользователя")
    public void registrationUser() {
        UserData user = new UserData(UserData.userEmail,UserData.userPassword, UserData.userName);
        ValidatableResponse response = API.createUser(user);
        userAccessToken = API.checkCreateUser(response);
    }
    @Test
    @DisplayName("Регистрация нового пользователя и попытка регистрации на те же данные")
    public void registrationRegisteredUser() {
        UserData user = new UserData(UserData.userEmail,UserData.userPassword, UserData.userName);
        ValidatableResponse response = API.createUser(user);
        userAccessToken = API.checkCreateUser(response);
        ValidatableResponse newResponse = API.registrationUserWithIncorrectData(user);
    }
    @Test
    @DisplayName("Регистрация пользователя без поля \"Имя\"")
    public void registrationUserWithoutName() {
        UserData user = new UserData(UserData.userEmail,UserData.userPassword);
        ValidatableResponse response = API.registrationUserWithoutName(user);
    }

}
