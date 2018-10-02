package rs.logik.phonebook.db.beans;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public class Contact {

    private int contactId;

    private String firstname;

    private String lastname;

    private String contactType;

    private String description;

    private boolean isActive;

    private DateTime dateChanged;

    private List<ContactEmail> emails;

    private List<ContactPhone> phones;

    private int userId;

    public Contact(String firstname, String lastname, String contactType, String description, boolean isActive, DateTime dateChanged, int userid) {
        this.contactId = 0;
        this.firstname = firstname;
        this.lastname = lastname;
        this.contactType = contactType;
        this.description = description;
        this.isActive = isActive;
        this.dateChanged = dateChanged;
        this.userId = userid;
        this.emails = new ArrayList<>();
        this.phones = new ArrayList<>();
    }

    public Contact(int contactid, String firstname, String lastname, String contactType, String description, boolean isActive, DateTime dateChanged, int userid) {
        this.contactId = contactid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.contactType = contactType;
        this.description = description;
        this.isActive = isActive;
        this.dateChanged = dateChanged;
        this.userId = userid;
        this.phones = new ArrayList<>();
        this.emails = new ArrayList<>();
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactid) {
        this.contactId = contactid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public DateTime getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(DateTime dateChanged) {
        this.dateChanged = dateChanged;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userid) {
        this.userId = userid;
    }

    public void addContactEmail(ContactEmail ce) {
        if (ce != null) {
            this.emails.add(ce);
        }
    }

    public void removeContactEmail(ContactEmail ce) {
        if (ce != null) {
            this.emails.remove(ce);
        }
    }

    public void addContactPhone(ContactPhone cp) {
        if (cp != null) {
            this.phones.add(cp);
        }
    }

    public void removeContactPhone(ContactPhone cp) {
        if (cp != null) {
            this.phones.remove(cp);
        }
    }

    public void replaceContactEmail(ContactEmail updatedContactEmail) {

    }

    public List<ContactEmail> getEmails() {
        return emails;
    }

    public List<ContactPhone> getPhones() {
        return phones;
    }

    @Override
    public boolean equals(Object that) {
        if (that != null && that instanceof Contact) {
            Contact thatz = (Contact) that;
            return this.contactId == thatz.contactId;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append(firstname).append(" ").append(lastname).toString();
    }

}
