package com.example.residentapplication;

public class Contacts {
    String addContact,contactName;

    public Contacts() {
    }

    public Contacts(String contactName, String addContact) {
        this.contactName = contactName;
        this.addContact = addContact;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getAddContact() {
        return addContact;
    }

    public void setAddContact(String addContact) {
        this.addContact = addContact;
    }
}
