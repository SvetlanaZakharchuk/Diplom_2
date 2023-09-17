package stellarburgers.user;

import stellarburgers.models.User;

import static stellarburgers.Utils.randomString;

public class UserGenerator {
    //создаем пользователя со всеми полями
    public static User randomUser() {
        return new User()
                .withEmail(randomEmail())
                .withPassword(randomPassword())
                .withName(randomName());
    }

    //создаем пользователя без пароля
    public static User randomUserWithoutPassword() {
        return new User()
                .withEmail(randomEmail())
                .withName(randomName());
    }

    //созадем пользователя без логина
    public static User randomUserWithoutEmail() {
        return new User()
                .withPassword(randomPassword())
                .withName(randomName());
    }

    //создаем пользователя без имени
    public static User randomUserWithoutName() {
        return new User()
                .withEmail(randomEmail())
                .withPassword(randomName());
    }

    //генерируем почту
    public static String randomEmail() {
        return (randomString(8) + "@" + randomString(4) + "." + randomString((2)));
    }

    //генерируем пароль
    public static String randomPassword() {
        return randomString(10);
    }

    //генерируем имя пользователя
    public static String randomName() {
        return randomString(8);
    }

}
