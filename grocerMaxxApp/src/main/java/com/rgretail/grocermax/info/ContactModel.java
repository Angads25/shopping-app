package com.rgretail.grocermax.info;

import java.util.ArrayList;

/**
 * Created by anchit-pc on 10-Jun-16.
 */
public class ContactModel {

    String contact_id;
    String contact_name;
    ArrayList<String> contact_no;
    ArrayList<String> contact_email;

    public ArrayList<String> getContact_no() {
        return contact_no;
    }

    public void setContact_no(ArrayList<String> contact_no) {
        this.contact_no = contact_no;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public ArrayList<String> getContact_email() {
        return contact_email;
    }

    public void setContact_email(ArrayList<String> contact_email) {
        this.contact_email = contact_email;
    }
}
