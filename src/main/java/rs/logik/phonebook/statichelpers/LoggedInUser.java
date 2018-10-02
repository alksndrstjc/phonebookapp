
package rs.logik.phonebook.statichelpers;

import rs.logik.phonebook.db.beans.User;


public class LoggedInUser {
    
    private static User u = null;

    public static User getUser() {
        return u;
    }

    public static void setUser(User u) {
        LoggedInUser.u = u;
    }
    
    
    
}
