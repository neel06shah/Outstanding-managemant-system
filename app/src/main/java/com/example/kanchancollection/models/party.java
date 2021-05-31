package com.example.kanchancollection.models;

public class party {
    private String contact, party_name,area;
    party() {}
    public party(String contact, String party_name, String area) {
        this.contact = contact;
        this.party_name = party_name;
        this.area = area;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getParty_name() {
        return party_name;
    }

    public void setParty_name(String party_name) {
        this.party_name = party_name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
