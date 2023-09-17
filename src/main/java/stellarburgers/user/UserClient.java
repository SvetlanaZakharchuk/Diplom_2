package stellarburgers.user;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import stellarburgers.models.User;
import stellarburgers.models.UserCreds;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site/";
    //эндпоит для создания пользователя
    private static final String CREATE_USER = "api/auth/register";
    //эндпоинт для авторизации
    private static final String AUTH_USER = "api/auth/login";
    //экнжпоинт для получения информации о пользователе
    private static final String INFO_USER = "api/auth/user";

    public UserClient() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Создание пользователя")
    public Response create(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(CREATE_USER);
    }

    @Step("Авторизация пользователя")
    public Response login(UserCreds creds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(creds)
                .when()
                .post(AUTH_USER);
    }

    @Step("Авторизаиця с неверным логином или паролем")
    public Response login(String email, String password) {
        UserCreds userCreds = new UserCreds(email, password);
        return login(userCreds);
    }

    @Step("Получение данных пользователя")
    public Response getUserInfo(User user, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body(user)
                .when()
                .get(INFO_USER);
    }

    @Step("Изменение данных авторизованного пользователя")
    public Response updateUserInfo(User user, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body(user)
                .when()
                .patch(INFO_USER);
    }

    @Step("Измененеи данных неавторизованного пользователя")
    public Response updateUserInfo(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .patch(INFO_USER);
    }

    @Step("Удаление пользователя")
    public Response delete(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .delete(INFO_USER);
    }
}
