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

    @Before
    public void setUp() {
        // Создаем пользователя перед тестами
        UserData user = new UserData(UserData.userEmail,UserData.userPassword, UserData.userName);
        // Получаем accessToken для последующего удаления пользователя
        ValidatableResponse response = API.createUser(user);
        userAccessToken = API.checkCreateUser(response);
    }
    @After
    public void tearDown() {
        if (userAccessToken != null ) {
            ValidatableResponse deleteResponse = API.deleteUser(userAccessToken);
            API.checkDeleteUser(deleteResponse);
        }

    }
    @Test
    @DisplayName("Получить заказ авторизованным пользователем")
    public void getUserOrdersWithAuthorization() {
        String[] ingredients = API.getIngredients();
        Response createOrderResponse = API.createOrder(ingredients, userAccessToken);
        Response response = API.getUserOrderWithAuthorization(userAccessToken);
        boolean errorMessage = response.jsonPath().getList("orders").isEmpty();
        assertFalse("You should be authorised", errorMessage);
    }
    @Test
    @DisplayName("Получить заказ неавторизованным пользователем")
    public void getUserOrdersWithoutAuthorization() {
        Response response = API.getUserOrderWithoutAuthorization();
        response.then().statusCode(HTTP_UNAUTHORIZED);
        String errorMessage = response.jsonPath().getString("message");
        assertEquals("You should be authorised", errorMessage);
    }
}
