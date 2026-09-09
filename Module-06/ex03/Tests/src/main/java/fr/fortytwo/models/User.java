package fr.fortytwo.models;

import java.util.Objects;

public class User {

    private Long id;
    private String login;
    private String password;
    private boolean authenticated;

    public User() {
    }

    public User(Long id, String login, String password, boolean authenticated) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.authenticated = authenticated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return authenticated == user.authenticated
                && Objects.equals(id, user.id)
                && Objects.equals(login, user.login)
                && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, authenticated);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", login='" + login + "', authenticated=" + authenticated + '}';
    }
}
