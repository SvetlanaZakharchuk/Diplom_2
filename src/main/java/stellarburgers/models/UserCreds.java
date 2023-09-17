package stellarburgers.models;

public class UserCreds {
    private String email;
    private String password;

    public UserCreds(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserCreds(String email) {
        this.email = email;
    }

    //получаем логин и пароль созданного пользователя
    public static UserCreds credsFrom(User user) {
        return new UserCreds(user.getEmail(), user.getPassword());
    }

    //получаем только логин созданного пользователя
    public static UserCreds emailCredsFrom(User user) {
        return new UserCreds(user.getEmail());
    }

    //получаем только пароль созданного пользователя
    public static UserCreds passwordCredsFrom(User user) {
        return new UserCreds(user.getPassword());
    }
}
