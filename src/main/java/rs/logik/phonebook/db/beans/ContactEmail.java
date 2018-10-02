package rs.logik.phonebook.db.beans;

import org.joda.time.DateTime;

public class ContactEmail extends ContactDetail{

    private int contactEmailId;

    private String email;

    private DateTime dateChanged;

    private boolean isActive;

    private int contactId;

    public ContactEmail(String email, DateTime dateChanged, boolean isActive, int contactId) {
        this.email = email;
        this.dateChanged = dateChanged;
        this.isActive = isActive;
        this.contactId = contactId;
    }

    public ContactEmail(int contactemailid, String email, DateTime dateChanged, boolean isActive, int contactid) {
        this.contactEmailId = contactemailid;
        this.email = email;
        this.dateChanged = dateChanged;
        this.isActive = isActive;
        this.contactId = contactid;
    }

    public int getContactEmailId() {
        return contactEmailId;
    }

    public void setContactEmailId(int contactemailid) {
        this.contactEmailId = contactemailid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DateTime getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(DateTime dateChanged) {
        this.dateChanged = dateChanged;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactid) {
        this.contactId = contactid;
    }

    @Override
    public boolean equals(Object that) {
        if (that != null && that instanceof ContactEmail) {
            ContactEmail thatz = (ContactEmail) that;
            return this.email.equals(thatz.email);
        }
        return false;
    }

    @Override
    public String toString() {
        return this.email;
    }

}
