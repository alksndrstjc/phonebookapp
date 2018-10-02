package rs.logik.phonebook.db.beans;

import org.joda.time.DateTime;

public class User {
    
    private int userid;
    
    private String username;
    
    private String password;
    
    private String email;
    
    private boolean isActive;
    
    private DateTime dateTime;
    
    public User() {
        
    }
    
    public User(String username, String password, String email, boolean isActive, DateTime dateTime) {
        this.userid = 0;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isActive = isActive;
        this.dateTime = dateTime;
    }
    
    public User(int userid, String username, String password, String email, boolean isActive, DateTime dateTime) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isActive = isActive;
        this.dateTime = dateTime;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
    
    
}
