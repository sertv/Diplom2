package userTest;

import API.API;
import data.UserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginTest {
    private static String userAccessToken;
    API Api = new API();
    @Before
    public void setUp() {
        // Создаем пользователя перед тестами
        UserData user = new UserData(UserData.userEmail,UserData.userPassword, UserData.userName);
        // Получаем accessToken для последующего удаления пользователя
        ValidatableResponse response = Api.createUser(user);
        userAccessToken = Api.checkCreateUser(response);
    }
    @After
    public void tearDown() {
        ValidatableResponse deleteResponse = Api.deleteUser(userAccessToken);
        Api.checkDeleteUser(deleteResponse);
    }
    @Test
    @DisplayName("Проверка авторизации пользователя")
    public void successfulUserLogin() {
        UserData user = new UserData(UserData.userEmail, UserData.userPassword);
        ValidatableResponse response = Api.loginUser(user);
        Api.checkLoginUser(response);
    }
    @Test
    @DisplayName("Авторизация с некорректным паролем")
    public void userLoginWithIncorrectPassword() {
        UserData userIncorrectPassword = new UserData(UserData.userEmail, UserData.userIncorrectPassword);
        ValidatableResponse response = Api.loginUserWithIncorrectData(userIncorrectPassword);
    }
    @Test
    @DisplayName("Авторизация с неправильным email")
    public void  userLoginWithIncorrectEmail() {
        UserData userIncorrectEmail = new UserData(UserData.userIncorrectEmail, UserData.userPassword);
        ValidatableResponse response = Api.loginUserWithIncorrectData(userIncorrectEmail);
    }

}
