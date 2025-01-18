package userTest;

import API.API;
import data.UserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

public class RegistrationTest {
    private static String userAccessToken;
    API Api = new API();

    @After
    public void tearDown() {
        if (userAccessToken != null) {
            ValidatableResponse deleteResponse = Api.deleteUser(userAccessToken);
            Api.checkDeleteUser(deleteResponse);
        }
    }
    @Test
    @DisplayName("Регистрация нового пользователя")
    public void registrationUser() {
        UserData user = new UserData(UserData.userEmail,UserData.userPassword, UserData.userName);
        ValidatableResponse response = Api.createUser(user);
        userAccessToken = Api.checkCreateUser(response);
    }
    @Test
    @DisplayName("Регистрация нового пользователя и попытка регистрации на те же данные")
    public void registrationRegisteredUser() {
        UserData user = new UserData(UserData.userEmail,UserData.userPassword, UserData.userName);
        ValidatableResponse response = Api.createUser(user);
        userAccessToken = Api.checkCreateUser(response);
        ValidatableResponse newResponse = Api.registrationUserWithIncorrectData(user);
    }
    @Test
    @DisplayName("Регистрация пользователя без поля \"Имя\"")
    public void registrationUserWithoutName() {
        UserData user = new UserData(UserData.userEmail,UserData.userPassword);
        ValidatableResponse response = Api.registrationUserWithoutName(user);
    }

}
