package ru.alexmitrokhin.grestservice;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class Controller {

    private UserRepository repository;
    private SessionRepository sessionRepository;

    @Autowired
    public Controller(UserRepository repository, SessionRepository sessionRepository) {
        this.repository = repository;
        this.sessionRepository = sessionRepository;
    }

    @RequestMapping(value="/register", method=RequestMethod.POST)
    public String registerUser(@RequestParam String login, @RequestParam String pass, @RequestParam String email, @RequestParam String role){
        if(repository.findByLogin(login).isEmpty()) {
            Gson gson = new Gson();
            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(pass);
            User user = new User();
            user.setUserID(UUID.randomUUID().toString());
            user.setLogin(login);
            user.setPassword(sha256hex);
            user.setEmail(email);
            user.setRole(role);
            repository.save(user);
            return gson.toJson(user);
        }
        else return "User with this name already exists";
    }

    @RequestMapping(value="/auth", method=RequestMethod.POST)
    public String authUser (@RequestParam String login, @RequestParam String pass){
        Date date = new Date();
        Session session=new Session();
        User user=new User();
        String sessionID;
        List<User> userList;
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(pass);
        userList= repository.findByLogin(login);
        if(userList.isEmpty()) return "BAD_CREDENTIALS";
        else if (userList.get(0).getPassword().equals(sha256hex)) {
            user.setUserID(userList.get(0).getUserID());
            user.setLogin(login);
            user.setPassword(sha256hex);
            user.setEmail(userList.get(0).getEmail());
            user.setRole(userList.get(0).getRole());
            session.setUser(user);
            sessionID=UUID.randomUUID().toString();
            session.setSessionID(sessionID);
            session.setLastActionTime(date); // дата + время
            sessionRepository.save(session);
            return sessionID;
        }
        else return "BAD_CREDENTIALS";
    }
}
