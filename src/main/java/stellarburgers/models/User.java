package stellarburgers.models;

public class User {
    private String email;
    private String password;
    private String name;

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public User withEmail(String email) {
        this.email = email;
        return this;
    }

    public User withPassword(String password) {
        this.password = password;
        return this;
    }

    public User withName(String name) {
        this.name = name;
        return this;
    }
}
