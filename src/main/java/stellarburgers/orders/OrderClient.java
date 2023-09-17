package stellarburgers.orders;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.File;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site/";
    //эндпоинт для создания и получения заказов
    private static final String ORDERS_INFO = "api/orders";
    //получение данных об ингридиентах
    private static final String GET_INGREDIENTS = "api/ingredients";
    public OrderClient() {
        RestAssured.baseURI = BASE_URI;
    }

    //создание заказа с авторизацией
    @Step("Создание заказа с авторизацией")
    public Response createOrder(File json, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body(json)
                .when()
                .post(ORDERS_INFO);
    }

    //созадние заказа c ингридиентами без авторизации
    @Step("Создание заказа с ингридиентами")
    public Response createOrderWithIngredients(File json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post(ORDERS_INFO);
    }

    //получение заказа
    @Step("Получение заказов авторизованного пользователя")
    public Response getOrders(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .get(ORDERS_INFO);
    }
}
