package ru.alexmitrokhin.grestservice;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SessionRepository extends CrudRepository<Session,Long> {
    Session findBySessionID(String sessionID);
    List<Session> findByUser(User user);
}
