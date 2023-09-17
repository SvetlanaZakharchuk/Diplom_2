//тесты для проверки получения информации о заказах
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

import static stellarburgers.user.UserGenerator.randomUser;

public class OrderGetTest {
    private String accessToken = "";
    private UserClient userClient;
    private OrderClient orderClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
    }

    //проверка получения списка заказов с авторизованным пользователем
    @Test
    @DisplayName("getOrdersForAuthUserTest")
    public void getOrdersForAuthUserTest() {
        User user = randomUser();
        Response response = userClient.create(user);
        accessToken = response.path("accessToken");
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

        //получаем список заказов
        Response orderResponse = orderClient.getOrders(accessToken);
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_OK, orderResponse.getStatusCode());
        Assert.assertEquals("Неверное тело ответа", true, orderResponse.path("success"));
    }

    //проверка получения списка заказав неавторизованного пользователя
    @Test
    @DisplayName("getOrdersForUnauthUserTest")
    public void getOrdersForUnauthUserTest() {
        //передаем пустой токен
        Response response = orderClient.getOrders(accessToken);

        String actualResult = response.path("message");
        String expectedResult = "You should be authorised";
        //проверяем статус код и тело ответа
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_UNAUTHORIZED, response.getStatusCode());
        Assert.assertEquals("Неверное тело ответа", expectedResult, actualResult);
    }

    @After
    public void tearsDown() {
        userClient.delete(accessToken);
    }
}
