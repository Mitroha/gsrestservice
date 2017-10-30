package ru.alexmitrokhin.grestservice;


import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
public class Controller {

    private UserRepository repository;

    @RequestMapping(value="/register", method=RequestMethod.POST)
    public String registerUser(@RequestParam String login, @RequestParam String pass, @RequestParam String email, @RequestParam String role){
        //add login-check
        //add hashing
        User user = new User();
        user.setUserID(UUID.randomUUID());
        user.setLogin(login);
        user.setPassword(pass);
        user.setEmail(email);
        user.setRole(role);
        repository.save(user);
        return "New user added";
    }

    @RequestMapping(value="/auth", method=RequestMethod.POST)
    public String authUser (@RequestParam String login, @RequestParam String pass){
        String sessionID=new String();
        return sessionID;
    }

}
