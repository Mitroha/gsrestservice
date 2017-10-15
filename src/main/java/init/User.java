package init;

public class User {

    private int userID;
    private String login;
    private String email;
    private String password;
    private Integer sessionID;
    private String role;

    public int getUserID() {
        return userID;
    }

    public Integer getSessionID() {
        return sessionID;
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

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSessionID(Integer sessionID) {
        this.sessionID = sessionID;
    }
}

