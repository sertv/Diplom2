package orderTest;

import API.API;
import data.UserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.junit.Assert.*;

public class GetOrderTest {
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
        if (userAccessToken != null ) {
            ValidatableResponse deleteResponse = Api.deleteUser(userAccessToken);
            Api.checkDeleteUser(deleteResponse);
        }

    }
    @Test
    @DisplayName("Получить заказ авторизованным пользователем")
    public void getUserOrdersWithAuthorization() {
        String[] ingredients = Api.getIngredients();
        Response createOrderResponse = Api.createOrder(ingredients, userAccessToken);
        Response response = Api.getUserOrderWithAuthorization(userAccessToken);
        boolean errorMessage = response.jsonPath().getList("orders").isEmpty();
        assertFalse("You should be authorised", errorMessage);
    }
    @Test
    @DisplayName("Получить заказ неавторизованным пользователем")
    public void getUserOrdersWithoutAuthorization() {
        Response response = Api.getUserOrderWithoutAuthorization();
        response.then().statusCode(HTTP_UNAUTHORIZED);
        String errorMessage = response.jsonPath().getString("message");
        assertEquals("You should be authorised", errorMessage);
    }
}
