//тесты для проверки обновления информации о пользователе
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

import static stellarburgers.user.UserGenerator.*;

public class UserUpdateInfoTest {
    private final boolean AUTHORIZED_SUCCESS = true;
    private final String UNAUTHORIZATION_MESSAGE = "You should be authorised";
    private UserClient userClient;
    private String accessToken = "";

    @Before
    public void SetUp() {
        userClient = new UserClient();
    }

    //Проверка изменения данных авторизованного пользователя
    //изменение email
    @Test
    @DisplayName("updateUserEmailTest")
    public void updateUserEmailTest() {
        User user = randomUser();
        Response response = userClient.create(user);
        accessToken = response.path("accessToken");
        //Проверяем, что пользователь создан
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        //авторизуемся
        Response authResponse = userClient.login(UserCreds.credsFrom(user));
        //проверяем, что авторизация прошла успешно
        Assert.assertEquals(HttpStatus.SC_OK, authResponse.getStatusCode());
        //обновляем почту
        user.withEmail(randomEmail());
        Response updateEmailResponse = userClient.updateUserInfo(user, accessToken);
        //Проверяем код
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_OK, updateEmailResponse.getStatusCode());
        //Проверяем тело сообщения
        Assert.assertEquals(AUTHORIZED_SUCCESS, updateEmailResponse.path("success"));
    }

    //измененеие пароля
    @Test
    @DisplayName("updateUserPasswordTest")
    public void updateUserPasswordTest() {
        User user = randomUser();
        Response response = userClient.create(user);
        accessToken = response.path("accessToken");
        //Проверяем, что пользователь создан
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        //авторизуемся
        Response authResponse = userClient.login(UserCreds.credsFrom(user));
        //проверяем, что авторизация прошла успешно
        Assert.assertEquals(HttpStatus.SC_OK, authResponse.getStatusCode());
        //обновляем пароль
        user.withPassword(randomPassword());
        Response updatePasswordResponse = userClient.updateUserInfo(user, accessToken);
        //Проверяем код
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_OK, updatePasswordResponse.getStatusCode());
        //проверяем тело сообщения
        Assert.assertEquals("Неверное тело сообщения", AUTHORIZED_SUCCESS, updatePasswordResponse.path("success"));
    }

    //изменение имени
    @Test
    @DisplayName("updateUserNameTest")
    public void updateUserNameTest() {
        User user = randomUser();
        Response response = userClient.create(user);
        accessToken = response.path("accessToken");
        //Проверяем, что пользователь создан
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        //авторизуемся
        Response authResponse = userClient.login(UserCreds.credsFrom(user));
        //проверяем, что авторизация прошла успешно
        Assert.assertEquals(HttpStatus.SC_OK, authResponse.getStatusCode());
        //обновляем имя
        user.withName(randomName());
        Response updateNameResponse = userClient.updateUserInfo(user, accessToken);
        //Проверяем код
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_OK, updateNameResponse.getStatusCode());
        //проверяем тело ответа
        Assert.assertEquals("Неверное тело ответа", AUTHORIZED_SUCCESS, updateNameResponse.path("success"));
    }

    //Проверка изменения данных неавторизованного пользователя
    //Проверка изменения логина
    @Test
    @DisplayName("updateUnauthorizedUserEmailTest")
    public void updateUnauthorizedUserEmailTest() {
        User user = randomUser();
        Response response = userClient.create(user);
        accessToken = response.path("accessToken");
        //Проверяем, что пользователь создан
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        //обновляем почту
        user.withEmail(randomEmail());
        Response updateEmailResponse = userClient.updateUserInfo(user);
        //Проверяем код ответа
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_UNAUTHORIZED, updateEmailResponse.getStatusCode());
        //Проверяем тело ответа
        Assert.assertEquals("Неверное тело ответа", UNAUTHORIZATION_MESSAGE, updateEmailResponse.path("message"));
    }

    //проверка измененеия пароля
    @Test
    @DisplayName("updateUnauthorizedUserPasswordTest")
    public void updateUnauthorizedUserPasswordTest() {
        User user = randomUser();
        Response response = userClient.create(user);
        accessToken = response.path("accessToken");
        //Проверяем, что пользователь создан
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        //обновляем пароль
        user.withPassword(randomPassword());
        Response updatePasswordResponse = userClient.updateUserInfo(user);
        //Проверяем код и тело сообщения
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_UNAUTHORIZED, updatePasswordResponse.getStatusCode());
        //проверяем тело ответа
        Assert.assertEquals("Неверное тело ответа", UNAUTHORIZATION_MESSAGE, updatePasswordResponse.path("message"));
    }

    //проверка изменения имени
    @Test
    @DisplayName("updateUnauthorizedUserNameTest")
    public void updateUnauthorizedUserNameTest() {
        User user = randomUser();
        Response response = userClient.create(user);
        accessToken = response.path("accessToken");
        //Проверяем, что пользователь создан
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        //обновляем почту
        user.withName(randomName());
        Response updateNameResponse = userClient.updateUserInfo(user);
        //Проверяем код
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_UNAUTHORIZED, updateNameResponse.getStatusCode());
        //проверяем тело ответа
        Assert.assertEquals("Неверное тело ответа", UNAUTHORIZATION_MESSAGE, updateNameResponse.path("message"));
    }

    @After
    public void tearsDown() {
        userClient.delete(accessToken);
    }
}
