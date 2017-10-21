package ru.alexmitrokhin.grestservice;

import org.hibernate.annotations.Table;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
@javax.persistence.Table(name="user")
public class User {

    @Id private int userID;
    @Column(name="login") private String login;
    @Column(name="email") private String email;
    @Column(name="password") private String password;
    @Column(name="role") private String role;

    @Id public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
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

}

