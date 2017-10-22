package ru.alexmitrokhin.grestservice;

import javax.persistence.*;

@Entity
@Table(name="session")
public class Session {

    @ManyToOne
    @JoinColumn
    private User user;

    @Id
    private Integer sessionID;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSessionID(Integer sessionID) {
        this.sessionID = sessionID;
    }

    public Integer getSessionID() {
        return sessionID;
    }
}
