package ru.alexmitrokhin.grestservice;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface UserRepository extends CrudRepository<User,Long> {
    List<User> findByLogin(String login);
    User findByUserID(String userID);
}
