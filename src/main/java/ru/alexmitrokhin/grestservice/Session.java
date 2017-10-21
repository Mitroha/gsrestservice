package ru.alexmitrokhin.grestservice;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@javax.persistence.Table(name="session")
public class Session {

    @ManyToOne private User user;



}
