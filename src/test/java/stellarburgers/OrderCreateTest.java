//тесты для проверки создания заказов
package stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.models.User;
import stellarburgers.orders.OrderClient;
import stellarburgers.user.UserClient;

import java.io.File;

import static stellarburgers.user.UserGenerator.randomUser;

public class OrderCreateTest {
    private static final String NO_INGREDIENTS_MESSAGE = "Ingredient ids must be provided";
    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken = "";

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
    }

    // проверка создания заказа с авторизацией
    @Test
    @DisplayName("createOrderWithAuthorizationTest")
    public void createOrderWithAuthorizationTest() {
        User user = randomUser();
        Response response = userClient.create(user);
        accessToken = response.path("accessToken");
        //проверяем, что пользователь создан
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        //получаем список ингридиентов для заказа
        File json = new File("src/test/resources/IngredientsInfo.json");
        //создаем заказ и передаем токен для авторизации
        Response order_response = orderClient.createOrder(json, accessToken);
        //проверяем статус код
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_OK, order_response.getStatusCode());
        //проверяем тело ответа
        Assert.assertEquals("Неверное тело ответа", true, order_response.path("success"));
    }

    //проверка создания заказа без авторизации
    @Test
    @DisplayName("createOrderWithUnAuthorizationTest")
    public void createOrderWithUnAuthorizationTest() {
        //получаем список ингридиентов для заказа
        File json = new File("src/test/resources/IngredientsInfo.json");
        //создаем заказ без авторизации, передает пустой токен
        Response order_response = orderClient.createOrder(json, accessToken);
        //проверяем статус код
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_OK, order_response.getStatusCode());
        //проверяем тело ответа
        Assert.assertEquals("Неверное тело сообщения", true, order_response.path("success"));
    }

    //проверка создания заказа с ингридиентами
    @Test
    @DisplayName("createOrderWithIngredients")
    public void createOrderWithIngredients() {
        //получаем список ингридиентов
        File json = new File("src/test/resources/IngredientsInfo.json");
        //создаем заказ
        Response response = orderClient.createOrderWithIngredients(json);
        boolean actualResult = response.path("success");
        boolean expectedResult = true;
        //проверяем статус код
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_OK, response.getStatusCode());
        //проеряем тело ответа
        Assert.assertEquals("Неверное тело ответа", expectedResult, actualResult);
    }

    //проверка создания заказаов без ингридиентов
    @Test
    @DisplayName("createOrderNoIngredients")
    public void createOrderNoIngredients() {
        File json = new File("src/test/resources/NoIngredientsInfo.json");
        Response response = orderClient.createOrderWithIngredients(json);
        String actualResult = response.path("message");

        Assert.assertEquals("Неверный статус код", HttpStatus.SC_BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("Неверное тело ответа", NO_INGREDIENTS_MESSAGE, actualResult);
    }

    //проверка создания заказа с неверным хэшем ингридиентов
    @Test
    @DisplayName("createOrderWithInvalidHash")
    public void createOrderWithInvalidHash() {
        File json = new File("src/test/resources/InvalidHashIngredients.json");
        Response response = orderClient.createOrderWithIngredients(json);
        Assert.assertEquals("Получилось создать заказ", HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @After
    public void tearsDown() {
        userClient.delete(accessToken);
    }
}
