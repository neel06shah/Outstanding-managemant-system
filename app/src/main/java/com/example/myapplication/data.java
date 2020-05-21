package com.example.myapplication;

public class data {
    private String Area, Date, Due_on, Party_Name, Contact,Ref_no;
    private Long Overdue,Pending,id, Opening;
    data() {}

    public data(String area, String date, String due_on, String party_Name, String contact, String ref_no, Long overdue, Long pending, Long id, Long opening) {
        Area = area;
        Date = date;
        Due_on = due_on;
        Party_Name = party_Name;
        Contact = contact;
        Ref_no = ref_no;
        Overdue = overdue;
        Pending = pending;
        this.id = id;
        Opening = opening;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDue_on() {
        return Due_on;
    }

    public void setDue_on(String due_on) {
        Due_on = due_on;
    }

    public String getParty_Name() {
        return Party_Name;
    }

    public void setParty_Name(String party_Name) {
        Party_Name = party_Name;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getRef_no() {
        return Ref_no;
    }

    public void setRef_no(String ref_no) {
        Ref_no = ref_no;
    }

    public Long getOverdue() {
        return Overdue;
    }

    public void setOverdue(Long overdue) {
        Overdue = overdue;
    }

    public Long getPending() {
        return Pending;
    }

    public void setPending(Long pending) {
        Pending = pending;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOpening() {
        return Opening;
    }

    public void setOpening(Long opening) {
        Opening = opening;
    }
}
