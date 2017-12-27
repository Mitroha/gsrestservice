package ru.alexmitrokhin.grestservice;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@EnableScheduling
@RestController
public class Controller {

    private UserRepository repository;
    private SessionRepository sessionRepository;

    @Autowired
    public Controller(UserRepository repository, SessionRepository sessionRepository) {
        this.repository = repository;
        this.sessionRepository = sessionRepository;
    }

    @Scheduled(fixedRate=60000)
    public void clearOldSessions(){
        Integer clearInterval;
        clearInterval=300000; //удаляются сессии, если пользователь не активен более получаса
        Iterable<Session> sessionList;
        Date date=new Date();
        sessionList=sessionRepository.findAll();
        for (Session curSession : sessionList){
            if ((curSession.getLastActionTime()==null)||(date.getTime()-curSession.getLastActionTime().getTime()>clearInterval)){
                sessionRepository.delete(curSession);
            }
        }
    }

    @RequestMapping(value="/register", method=RequestMethod.POST)
    public String registerUser(@RequestParam String login, @RequestParam String pass, @RequestParam String email, @RequestParam String role){
        if(repository.findByLogin(login).isEmpty()) {
            Gson gson = new Gson();
            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(pass);
            User user = new User();
            UserModel userModel=new UserModel();
            user.setUserID(UUID.randomUUID().toString());
            user.setLogin(login);
            user.setPassword(sha256hex);
            user.setEmail(email);
            user.setRole(role);
            userModel.setEmail(email);
            userModel.setLogin(login);
            repository.save(user);
            return gson.toJson(userModel);
        }
        else return "LOGIN_TAKEN";
    }

    @RequestMapping(value="/auth", method=RequestMethod.POST)
    public String authUser (@RequestParam String login, @RequestParam String pass){
        Date date = new Date();
        Session session=new Session();
        User user=new User();
        String sessionID;
        List<User> userList;
        List<Session> activeSessionList;
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(pass);
        userList= repository.findByLogin(login);
        if(userList.isEmpty()) return "BAD_CREDENTIALS";
        else if (userList.get(0).getPassword().equals(sha256hex)) {
            user.setUserID(userList.get(0).getUserID());
            user.setLogin(login);
            user.setPassword(sha256hex);
            user.setEmail(userList.get(0).getEmail());
            user.setRole(userList.get(0).getRole());
            activeSessionList=sessionRepository.findByUser(user);
            if (!(activeSessionList.isEmpty())) {
                sessionRepository.delete(activeSessionList.get(0));
            }
            sessionID=UUID.randomUUID().toString();
            session.setSessionID(sessionID);
            session.setUser(user);
            session.setLastActionTime(date); // дата + время
            sessionRepository.save(session);
            return sessionID;
        }
        else return "BAD_CREDENTIALS";
    }

    @RequestMapping(value="/user", method=RequestMethod.GET)
    public String currentUser(@RequestHeader String sessionID){
        Gson gson = new Gson();
        Session session;
        UserModel userModel=new UserModel();
        Date date=new Date();
        session=sessionRepository.findBySessionID(sessionID);
        if (session==null) return "UNAUTHORIZED_USER";
        else {
            session.setLastActionTime(date);
            sessionRepository.save(session);
            userModel.setLogin(session.getUser().getLogin());
            userModel.setEmail(session.getUser().getEmail());
            return gson.toJson(userModel);
        }
    }

    @RequestMapping(value="/list",method=RequestMethod.GET)
    public String allUsers(@RequestHeader String sessionID){
        Gson gson = new Gson();
        int i=0;
        Iterable<User> fullUserList;
        String result=new String();
        UserModel curUserModel=new UserModel();
        String adminStatus;
        adminStatus=checkAdmin(sessionID);
        if ((adminStatus.equals("UNAUTHORIZED_USER"))||(adminStatus.equals("NO_ACCESS_RIGHTS"))) return adminStatus;
        else{
            fullUserList= repository.findAll();
            for (User user:fullUserList){
                curUserModel.setEmail(user.getEmail());
                curUserModel.setLogin(user.getLogin());
                result+=gson.toJson(curUserModel);
            }
            return result;
        }
    }

    @RequestMapping(value="/user/{userID}",method=RequestMethod.GET)
    public String myList(@RequestHeader String sessionID, @PathVariable String userID){
        String adminStatus;
        Gson gson = new Gson();
        User user;
        UserModel userModel=new UserModel();
        adminStatus=checkAdmin(sessionID);
        if ((adminStatus.equals("UNAUTHORIZED_USER"))||(adminStatus.equals("NO_ACCESS_RIGHTS"))) return adminStatus;
        else{
            user=repository.findByUserID(userID);
            if (user==null) {
                return "WRONG_USERID";
            }
            else {
                userModel.setLogin(user.getLogin());
                userModel.setEmail(user.getEmail());
                return gson.toJson(userModel);
            }
        }
    }

    @RequestMapping(value="/pass",method=RequestMethod.POST)
    public String changePassword(@RequestHeader String sessionID, @RequestParam String pass){
        Session session;
        User user;
        String curUserID;
        session=sessionRepository.findBySessionID(sessionID);
        if (session==null) return "UNAUTHORIZED_USER";
        else {
            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(pass);
            curUserID=session.getUser().getUserID();
            user= repository.findByUserID(curUserID);
            if (user==null) {
                return "PASSWORD_CHANGE_FAILED";
            } else{
                user.setPassword(sha256hex);
                repository.save(user);
                return "PASSWORD_CHANGED";
            }
        }
    }

    @RequestMapping(value="/profile",method=RequestMethod.POST)
    public String updateProfile (@RequestHeader String sessionID, @RequestParam String login, @RequestParam String email){
        Date date=new Date();
        Session session;
        List<User> userListByLogin;
        User user;
        String curUserID;
        session=sessionRepository.findBySessionID(sessionID);
        if (session==null) return "UNAUTHORIZED_USER";
        else{
            session.setLastActionTime(date);
            sessionRepository.save(session);
            curUserID=session.getUser().getUserID();
            user= repository.findByUserID(curUserID);
            if (user==null) {
                return "DATA_CHANGE_FAILED";
            } else{
                user.setEmail(email);
                userListByLogin=repository.findByLogin(login);
                if (userListByLogin.isEmpty()){
                    user.setLogin(login);
                    repository.save(user);
                    return "DATA_CHANGED";
                }
                else {
                    repository.save(user);
                    return "DATA_CHANGED_NO_LOGIN_CHANGED";
                }
            }
        }
    }


    @RequestMapping(value="/user/{userID}",method=RequestMethod.POST)
    public String updateUser (@RequestHeader String sessionID, @PathVariable String userID, @RequestParam String login, @RequestParam String email, @RequestParam String role){
        String adminStatus;
        User user;
        List<User> userListByLogin;
        adminStatus=checkAdmin(sessionID);
        if ((adminStatus.equals("UNAUTHORIZED_USER"))||(adminStatus.equals("NO_ACCESS_RIGHTS"))) return adminStatus;
        else{
            user=repository.findByUserID(userID);
            if (user==null) {
                return "WRONG_USERID";
            }
            else{
                user.setEmail(email);
                user.setRole(role);
                userListByLogin=repository.findByLogin(login);
                if (userListByLogin.isEmpty()){
                    user.setLogin(login);
                    repository.save(user);
                    return "USER_UPDATED";
                }
                else{
                    repository.save(user);
                    return "USER_UPDATED_NO_LOGIN_CHANGED";
                }
            }
        }
    }

    public String checkAdmin(String sessionID){
        Session session;
        User user;
        session=sessionRepository.findBySessionID(sessionID);
        Date date=new Date();
        String curUserID;
        if (session==null) return "UNAUTHORIZED_USER";
        else{
            curUserID=session.getUser().getUserID();
            session.setLastActionTime(date);
            user=repository.findByUserID(curUserID);
            if (user.getRole().equals("ADMIN")){
                return "ACCESS_GRANTED";
            }
            else{
                return "NO_ACCESS_RIGHTS";
            }
        }
    }

}
