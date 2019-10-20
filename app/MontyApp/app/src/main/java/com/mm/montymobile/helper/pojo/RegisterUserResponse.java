package com.mm.montymobile.helper.pojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class RegisterUserResponse {
  @SerializedName("user")
  @Expose
  private String user;
  private String response;


    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getUser(){
   return user;
  }

  public void setUser(String user){
   this.user=user;
  }


}