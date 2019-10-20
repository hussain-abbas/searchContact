package com.mm.montymobile.helper.pojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class SaveContactsResponse{
  @SerializedName("contacts")
  @Expose
  private String contacts;
  private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setContacts(String contacts){
   this.contacts=contacts;
  }
  public String getContacts(){
   return contacts;
  }
}