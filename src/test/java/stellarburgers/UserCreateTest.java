//тесты для проверки создания пользователя
package stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.models.User;
import stellarburgers.user.UserClient;

import static stellarburgers.user.UserGenerator.*;

public class UserCreateTest {
    private static final boolean EXPECTED_BODY_ANSWER_FOR_CREATE_USER = true;
    private static final String EXPECTED_BODY_MESSAGE_FOR_WRONG_CREATE = "User already exists";
    private static final String EXPECTED_BODY_MESSAGE_FOR_NO_FIELD = "Email, password and name are required fields";
    private UserClient userClient;
    private String accessToken = "";

    @Before
    public void SetUp() {
        userClient = new UserClient();
    }

    //проверка создания уникального пользователя
    @Test
    @DisplayName("createUserTest")
    public void createUserTest() {
        User user = randomUser();
        Response response = userClient.create(user);

        boolean actualBodyAnswer = response.path("success");
        //получаем accessToken, чтобы удалить пользователя
        accessToken = response.path("accessToken");
        //проверяем код ответа
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_OK, response.getStatusCode());
        //проверяем тело ответа
        Assert.assertEquals("Неверное тело ответа", EXPECTED_BODY_ANSWER_FOR_CREATE_USER, actualBodyAnswer);
    }

    //проверка создания пользователя, который уже зарегистрирован
    @Test
    @DisplayName("createTheSameUserTest")
    public void createTheSameUserTest() {
        User user = randomUser();
        Response response = userClient.create(user);
        //получаем accessToken
        accessToken = response.path("accessToken");
        //проверяем, что пользователь создан
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_OK, response.getStatusCode());
        //создаем еще одного пользователя с теми же данными
        Response anotherUserResponse = userClient.create(user);
        String actualMessage = anotherUserResponse.path("message");
        //проверяем код ответа
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_FORBIDDEN, anotherUserResponse.getStatusCode());
        //проверяем тело ответа
        Assert.assertEquals("Получилось создать еще одного пользователя", EXPECTED_BODY_MESSAGE_FOR_WRONG_CREATE, actualMessage);
    }

    //проверка создания пользователя, если не заполнен логин
    @Test
    @DisplayName("createUserWithoutEmailTest")
    public void createUserWithoutEmailTest() {
        User user = randomUserWithoutEmail();
        Response response = userClient.create(user);
        String responseMessage = response.path("message");
        //проверяем код ответа
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_FORBIDDEN, response.getStatusCode());
        //проверяем тело ответа
        Assert.assertEquals("Удалось создать пользователя без логина", EXPECTED_BODY_MESSAGE_FOR_NO_FIELD, responseMessage);
    }

    //проверка создания пользователя, если не заполнен пароль
    @Test
    @DisplayName("createUserWithoutPasswordTest")
    public void createUserWithoutPasswordTest() {
        User user = randomUserWithoutPassword();
        Response response = userClient.create(user);
        String responseMessage = response.path("message");
        //проверяем код ответа
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_FORBIDDEN, response.getStatusCode());
        //проверяем тело ответа
        Assert.assertEquals("Удалось создать пользователя без логина", EXPECTED_BODY_MESSAGE_FOR_NO_FIELD, responseMessage);
    }

    //проверка создания пользователя, если не заполнено имя
    @Test
    @DisplayName("createUserWithoutNameTest")
    public void createUserWithoutNameTest() {
        User user = randomUserWithoutName();
        Response response = userClient.create(user);
        String responseMessage = response.path("message");
        //проверяем код ответа
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_FORBIDDEN, response.getStatusCode());
        //проверяем тело ответа
        Assert.assertEquals("Удалось создать пользователя без логина", EXPECTED_BODY_MESSAGE_FOR_NO_FIELD, responseMessage);
    }

    @After
    public void tearsDown() {
        userClient.delete(accessToken);
    }
}
