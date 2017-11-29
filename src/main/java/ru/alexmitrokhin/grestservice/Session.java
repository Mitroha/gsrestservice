package ru.alexmitrokhin.grestservice;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="session")
public class Session {

    @ManyToOne
    @JoinColumn
    private User user;

    @Id
    private String sessionID;

    @Column
    private Date lastActionTime;

    public Date getLastActionTime() {
        return lastActionTime;
    }

    public void setLastActionTime(Date lastActionTime) {
        this.lastActionTime = lastActionTime;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
    public String getSessionID() {
        return sessionID;
    }



}
