package orderTest;

import API.API;
import data.UserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.*;

public class CreateOrderTest {
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
    @DisplayName("Создание заказа")
    public void createOrder() {
        String[] ingredients = Api.getIngredients();
        Response createOrderResponse = Api.createOrder(ingredients, userAccessToken);
        createOrderResponse.then().statusCode(HTTP_OK);
        assertTrue(createOrderResponse.jsonPath().getBoolean("success"));
        assertNotNull(createOrderResponse.jsonPath().getString("order.number"));
    }

    @Test
    @DisplayName("Cоздание заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        String[] ingredients = new String[0];
        Response createOrderResponse = Api.createOrder( ingredients, userAccessToken);
        createOrderResponse.then().statusCode(HTTP_BAD_REQUEST);
        String errorMessage = createOrderResponse.jsonPath().getString("message");
        assertEquals("Ingredient ids must be provided", errorMessage);
    }

    @Test
    @DisplayName("Cоздание заказа c несуществующих ингредиентов")
    public void createOrderWithInvalidIngredients() {
        String[] ingredients = Api.getIngredients();
        String[] invalidIngredients = ingredients.clone();
        invalidIngredients[0] = invalidIngredients[0].replace("a", "abcdef123");
        Response createOrderResponse = Api.createOrder(invalidIngredients, userAccessToken);
        createOrderResponse.then().statusCode(HTTP_INTERNAL_ERROR);
        String responseBody = createOrderResponse.getBody().asString();
        assertTrue("Internal Server Error", responseBody.contains("Internal Server Error"));
    }
}
