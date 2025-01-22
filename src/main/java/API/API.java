package API;

import data.OrderData;
import data.UserData;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import com.google.gson.Gson;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_ACCEPTED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.equalTo;

public class API {
    private static final String REGISTER = "/api/auth/register";
    private static final String LOGIN = "/api/auth/login";
    private static final String USER = "/api/auth/user";
    private static final String ORDER = "/api/orders";
    private static final String INGREDIENTS = "/api/ingredients";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String MAIN_PAGE_URL = "https://stellarburgers.nomoreparties.site";
    static Gson gson = new Gson();

    @Step ("Регистрация пользователя")
    public static ValidatableResponse createUser(UserData userData) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(MAIN_PAGE_URL)
                .body(gson.toJson(userData))
                .when()
                .post(REGISTER)
                .then().log().all();
    }

    @Step ("Удаление пользователя")
    public static ValidatableResponse deleteUser(String userAccessToken) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .header(AUTHORIZATION_HEADER, userAccessToken)
                .baseUri(MAIN_PAGE_URL)
                .when()
                .delete(USER)
                .then().log().all();

    }
    @Step ("Авторизация")
    public static ValidatableResponse loginUser(UserData userData) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(MAIN_PAGE_URL)
                .body(gson.toJson(userData))
                .when()
                .post(LOGIN)
                .then().log().all();
    }

    @Step ("Создание заказа")
    public static Response createOrder(String[] ingredients, String accessToken) {
        return
                given()
                        .header(AUTHORIZATION_HEADER, accessToken)
                        .contentType(ContentType.JSON)
                        .baseUri(MAIN_PAGE_URL)
                        .body(gson.toJson(new OrderData(ingredients)))
                        .post(ORDER);

    }
    @Step("Получение списка ингредиентов")
    public static String[] getIngredients() {
        Response response =
                given().log().all()
                .baseUri(MAIN_PAGE_URL)
                .get(INGREDIENTS);
        response.then().statusCode(HTTP_OK);
        return response.jsonPath().getList("data._id", String.class).toArray(new String[0]);
    }

    @Step ("Получение заказов с авторизацией")
    public static Response getUserOrderWithAuthorization(String accessToken) {
        Response response =
                given().log().all()
                .header(AUTHORIZATION_HEADER, accessToken)
                .baseUri(MAIN_PAGE_URL)
                .get(ORDER)
                .then()
                .statusCode(200)
                .extract()
                .response();
        return response;
    }

    @Step ("Получение заказов без авторизации")
    public static Response getUserOrderWithoutAuthorization() {
        Response response =
                given().log().all()
                .baseUri(MAIN_PAGE_URL)
                .get(ORDER);
        return response;
    }

    @Step("Обновление данных пользователя с авторизацией")
    public static Response updateUserWithAuthorization(String accessToken, UserData user) {
        Response response =
                given()
                        .header(AUTHORIZATION_HEADER, accessToken)
                        .contentType(ContentType.JSON)
                        .baseUri(MAIN_PAGE_URL)
                        .body(gson.toJson(user))
                        .patch(USER)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();
        return response;
    }

    @Step ("Обновление данных пользователя без авторизации")
    public static Response updateUserWithoutAuthorization(UserData user) {
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .baseUri(MAIN_PAGE_URL)
                        .body(gson.toJson(user))
                        .patch(USER)
                        .then()
                        .statusCode(401)
                        .extract()
                        .response();
        return response;
    }

    @Step ("Проверка и возвращение токена")
    public static String checkCreateUser(ValidatableResponse response) {
        String accessToken = response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("accessToken");
        Assert.assertNotNull(accessToken);
        return accessToken;
    }
    @Step ("Проверка удаления пользователя")
    public static void checkDeleteUser(ValidatableResponse response) {
        boolean removed = response
                .assertThat()
                .statusCode(HTTP_ACCEPTED)
                .extract()
                .path("success");
        Assert.assertTrue(removed);
    }

    @Step ("Проверка авторизации пользователя")
    public static String checkLoginUser(ValidatableResponse response) {
        String accessToken = response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("accessToken");
        Assert.assertNotNull(accessToken);
        return accessToken;
    }
    @Step ("Авторизация пользователя с некорректными данными и проверка сообщения об ошибке")
    public static ValidatableResponse loginUserWithIncorrectData(UserData userData) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(MAIN_PAGE_URL)
                .body(gson.toJson(userData))
                .when()
                .post(LOGIN)
                .then().log().all()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }
    @Step ("Регистрация уже зарегестрированного пользователя и проверка сообщения об ошибке")
    public static ValidatableResponse registrationUserWithIncorrectData(UserData userData) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(MAIN_PAGE_URL)
                .body(gson.toJson(userData))
                .when()
                .post(REGISTER)
                .then().log().all()
                .assertThat()
                .statusCode(403)
                .body("message", equalTo("User already exists"));
    }
    @Step ("Регистрация уже зарегестрированного пользователя и проверка сообщения об ошибке")
    public static ValidatableResponse registrationUserWithoutName(UserData userData) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(MAIN_PAGE_URL)
                .body(gson.toJson(userData))
                .when()
                .post(REGISTER)
                .then().log().all()
                .assertThat()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }

}
