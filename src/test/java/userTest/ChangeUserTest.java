package userTest;

import API.API;
import data.UserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChangeUserTest {
    private static String userAccessToken;

    @Before
    public void setUp() {
        // Создаем пользователя перед тестами
        UserData user = new UserData(UserData.userEmail,UserData.userPassword, UserData.userName);
        // Получаем accessToken для последующего удаления пользователя
        ValidatableResponse response = API.createUser(user);
        API.loginUser(user);
        userAccessToken = API.checkCreateUser(response);
    }
    @After
    public void tearDown() {
        ValidatableResponse deleteResponse = API.deleteUser(userAccessToken);
        API.checkDeleteUser(deleteResponse);
    }

    @Test
    @DisplayName("Успешное обновление данных с авторизацией")
    public void updateUserDataWithAuthorization() {
        UserData user = new UserData(UserData.changedEmail,UserData.changedUserPassword, UserData.changedUserName);
        Response response = API.updateUserWithAuthorization(userAccessToken, user);
        String email = response.jsonPath().getString("user.email");
        String name = response.jsonPath().getString("user.name");
        System.out.println("Response body: " + response.body().asString());
        assertEquals("Email не совпадает", UserData.changedEmail, email);
        assertEquals("Name не совпадает", UserData.changedUserName, name);
    }

    @Test
        @DisplayName("Обновление данных без авторизации")
    public void updateUserDataWithoutAuthorization() {
        UserData user = new UserData(UserData.userName, UserData.userEmail,UserData.userPassword);
        Response response = API.updateUserWithoutAuthorization(user);
        String errorMessage = response.jsonPath().getString("message");
        assertEquals("You should be authorised", errorMessage);
    }







}
