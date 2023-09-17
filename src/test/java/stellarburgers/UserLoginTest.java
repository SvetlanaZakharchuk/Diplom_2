//тесты для проверки авторизации пользователя
package stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.models.User;
import stellarburgers.models.UserCreds;
import stellarburgers.user.UserClient;

import static stellarburgers.user.UserGenerator.randomUser;

public class UserLoginTest {
    private static final String EXPECTED_BODY_MESSAGE = "email or password are incorrect";
    private static final boolean EXPECTED_BODY_ANSWER = true;
    private String accessToken = "";
    private UserClient userClient;

    @Before
    public void SetUp() {
        userClient = new UserClient();
    }

    //проверка авторизация пользователя с сущестующими логином и паролем
    @Test
    @DisplayName("authUserTest")
    public void authUserTest() {
        User user = randomUser();
        Response response = userClient.create(user);
        //получаем токен, чтобы удалить пользователя после выполнения теста
        accessToken = response.path("accessToken");
        //проверяем,что пользователь создан
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_OK, response.getStatusCode());
        //авторизуемся
        Response auth_response = userClient.login(UserCreds.credsFrom(user));
        boolean actualBodyAnswer = auth_response.path("success");

        //проверяем код ответа
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_OK, response.getStatusCode());
        //проверяем тело ответа
        Assert.assertEquals("Неверное тело ответа", EXPECTED_BODY_ANSWER, actualBodyAnswer);
    }

    //проверка авторизации пользователя с неверным логином
    @Test
    @DisplayName("authUserWrongEmailTest")
    public void authUserWrongEmailTest() {
        User user = randomUser();
        Response response = userClient.create(user);
        accessToken = response.path("accessToken");
        //проверка, что пользователь создан
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_OK, response.getStatusCode());
        //авторизация в неверным логином
        Response auth_response = userClient.login("wrongEmail", user.getPassword());
        String actualBodyMessage = auth_response.path("message");
        //проверка кода ответа
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_UNAUTHORIZED, auth_response.getStatusCode());
        //проверка тела ответа
        Assert.assertEquals("Получилось авторизоваться в неверным email-ом", EXPECTED_BODY_MESSAGE, actualBodyMessage);
    }

    //проверка авторизации пользователя с неверным паролем
    @Test
    @DisplayName("authUserWrongPasswordTest")
    public void authUserWrongPasswordTest() {
        User user = randomUser();
        Response response = userClient.create(user);
        accessToken = response.path("accessToken");
        //проверка, что пользователь создан
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_OK, response.getStatusCode());
        //авторизация в неверным паролем
        Response auth_response = userClient.login(user.getEmail(), "wrongPassword");
        String actualBodyMessage = auth_response.path("message");
        //проверка кода ответа
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_UNAUTHORIZED, auth_response.getStatusCode());
        //проверка сообщения
        Assert.assertEquals("Получилось авторизоваться в неверным паролем", EXPECTED_BODY_MESSAGE, actualBodyMessage);

    }

    @After
    public void tearsDown() {
        userClient.delete(accessToken);
    }
}
