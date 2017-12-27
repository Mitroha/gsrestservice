package ru.alexmitrokhin.grestservice;

import javax.persistence.*;


@Entity
@Table(name="user_table")
public class User {

    @Id
    private String userID;

    @Column
    private String login;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String role;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    protected User() {}

    /*
    public User(String login, String password, String email, String role) { //сук
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
    }
    */
}

