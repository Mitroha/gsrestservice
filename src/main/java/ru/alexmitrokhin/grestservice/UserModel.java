package ru.alexmitrokhin.grestservice;

public class UserModel {

    public String login;
    public String email;

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }
}
