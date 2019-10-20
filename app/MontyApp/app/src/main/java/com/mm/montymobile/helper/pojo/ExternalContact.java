package com.mm.montymobile.helper.pojo;



public class ExternalContact {


    private String name;
    private String number;
    private String pic;
    private String address;
    private String interest;
    private String notes;

    public ExternalContact(String name, String number, String pic, String address, String interest, String notes) {
        this.name = name;
        this.number = number;
        this.pic = pic;
        this.address = address;
        this.interest = interest;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
