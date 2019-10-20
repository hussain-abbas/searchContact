package com.mm.montymobile.helper.pojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class AllContactsResponse{
  @SerializedName("number")
  @Expose
  private String number;
  @SerializedName("address")
  @Expose
  private String address;
  @SerializedName("notes")
  @Expose
  private String notes;
  @SerializedName("interest")
  @Expose
  private String interest;
  @SerializedName("__v")
  @Expose
  private Integer __v;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("photo")
  @Expose
  private Long photo;
  @SerializedName("_id")
  @Expose
  private String _id;
  private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setNumber(String number){
   this.number=number;
  }
  public String getNumber(){
   return number;
  }
  public void setAddress(String address){
   this.address=address;
  }
  public String getAddress(){
   return address;
  }
  public void setNotes(String notes){
   this.notes=notes;
  }
  public String getNotes(){
   return notes;
  }
  public void setInterest(String interest){
   this.interest=interest;
  }
  public String getInterest(){
   return interest;
  }
  public void set__v(Integer __v){
   this.__v=__v;
  }
  public Integer get__v(){
   return __v;
  }
  public void setName(String name){
   this.name=name;
  }
  public String getName(){
   return name;
  }
  public void setPhoto(Long photo){
   this.photo=photo;
  }
  public Long getPhoto(){
   return photo;
  }
  public void set_id(String _id){
   this._id=_id;
  }
  public String get_id(){
   return _id;
  }
}