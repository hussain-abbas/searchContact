package com.mm.montymobile.helper.pojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class MobileVerificationCodeResponse{
  @SerializedName("ErrorDescription")
  @Expose
  private String ErrorDescription;
  @SerializedName("SMS")
  @Expose
  private List<SMS> SMS;
  @SerializedName("ErrorCode")
  @Expose
  private Integer ErrorCode;
  @SerializedName("MessageCount")
  @Expose
  private Integer MessageCount;
  @SerializedName("MessageParts")
  @Expose
  private Integer MessageParts;
  public void setErrorDescription(String ErrorDescription){
   this.ErrorDescription=ErrorDescription;
  }
  public String getErrorDescription(){
   return ErrorDescription;
  }
  public void setSMS(List<SMS> SMS){
   this.SMS=SMS;
  }
  public List<SMS> getSMS(){
   return SMS;
  }
  public void setErrorCode(Integer ErrorCode){
   this.ErrorCode=ErrorCode;
  }
  public Integer getErrorCode(){
   return ErrorCode;
  }
  public void setMessageCount(Integer MessageCount){
   this.MessageCount=MessageCount;
  }
  public Integer getMessageCount(){
   return MessageCount;
  }
  public void setMessageParts(Integer MessageParts){
   this.MessageParts=MessageParts;
  }
  public Integer getMessageParts(){
   return MessageParts;
  }
}