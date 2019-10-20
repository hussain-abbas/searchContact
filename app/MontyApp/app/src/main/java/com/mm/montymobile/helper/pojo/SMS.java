package com.mm.montymobile.helper.pojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class SMS{
  @SerializedName("OriginatingAddress")
  @Expose
  private Long OriginatingAddress;
  @SerializedName("ErrorCode")
  @Expose
  private Integer ErrorCode;
  @SerializedName("Id")
  @Expose
  private String Id;
  @SerializedName("DestinationAddress")
  @Expose
  private Long DestinationAddress;
  public void setOriginatingAddress(Long OriginatingAddress){
   this.OriginatingAddress=OriginatingAddress;
  }
  public Long getOriginatingAddress(){
   return OriginatingAddress;
  }
  public void setErrorCode(Integer ErrorCode){
   this.ErrorCode=ErrorCode;
  }
  public Integer getErrorCode(){
   return ErrorCode;
  }
  public void setId(String Id){
   this.Id=Id;
  }
  public String getId(){
   return Id;
  }
  public void setDestinationAddress(Long DestinationAddress){
   this.DestinationAddress=DestinationAddress;
  }
  public Long getDestinationAddress(){
   return DestinationAddress;
  }
}