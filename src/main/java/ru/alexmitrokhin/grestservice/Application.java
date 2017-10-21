package ru.alexmitrokhin.grestservice;



import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@EnableJpaRepositories
public class Application {

    public static void main(String[] args) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:postgresql://localhost/auth_svc", "mitroha", "qwerty");
    }

}
