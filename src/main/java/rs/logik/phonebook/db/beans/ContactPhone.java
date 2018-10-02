package rs.logik.phonebook.db.beans;

import org.joda.time.DateTime;

public class ContactPhone extends ContactDetail {

    private int contactPhoneId;

    private String phonenum;

    private String phonenumType;

    private DateTime dateChanged;

    private boolean isActive;

    private int contactId;

    public ContactPhone(String phonenum, String phonenumType, DateTime dateChanged, boolean isActive, int contactId) {
        this.phonenum = phonenum;
        this.phonenumType = phonenumType;
        this.dateChanged = dateChanged;
        this.isActive = isActive;
        this.contactId = contactId;
    }

    public ContactPhone(int phonenumId, String phonenum, String phonenumType, DateTime dateChanged, boolean isActive, int contactId) {
        this.contactPhoneId = phonenumId;
        this.phonenum = phonenum;
        this.phonenumType = phonenumType;
        this.dateChanged = dateChanged;
        this.isActive = isActive;
        this.contactId = contactId;
    }

    public int getContactPhoneId() {
        return contactPhoneId;
    }

    public void setContactPhoneId(int contactPhoneId) {
        this.contactPhoneId = contactPhoneId;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getPhonenumType() {
        return phonenumType;
    }

    public void setPhonenumType(String phonenumType) {
        this.phonenumType = phonenumType;
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

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    @Override
    public String toString() {
        return this.phonenum + " | " + this.phonenumType;
    }

    @Override
    public boolean equals(Object that) {
        if (that != null && that instanceof ContactPhone) {
            ContactPhone thatz = (ContactPhone) that;
            return this.getPhonenum().equals(thatz.phonenum);
        }
        return false;
    }

}
