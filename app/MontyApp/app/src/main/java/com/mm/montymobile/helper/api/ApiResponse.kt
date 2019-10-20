package com.mm.montymobile.helper.api

import com.google.gson.JsonArray
import com.mm.montymobile.helper.pojo.AllContactsResponse
import com.mm.montymobile.helper.pojo.MobileVerificationCodeResponse
import com.mm.montymobile.helper.pojo.RegisterUserResponse
import com.mm.montymobile.helper.pojo.SaveContactsResponse

import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface ApiResponse {

    @Headers(
        "Content-Type:application/json"
    )

    @POST("messages")
    fun getVerificationCode(
        @Body jsonObject: RequestBody
    ): Call<MobileVerificationCodeResponse>


    /*@Headers(
        "Content-Type:application/json"
     )

    @POST("messages")
    fun getVerificationCode(
         @Body jsonObject: JSONObject
    ): Call<MobileVerificationCodeResponse>*/


    @Headers(
        "Content-Type:application/json"
    )

  //  @Multipart
    @POST("user/add")
    fun registerUser(
        @Body jsonObject: RequestBody
    ): Call<RegisterUserResponse>

    @GET("contact/")
    fun getAllContacts(): Call<List<AllContactsResponse>>

    @POST("contact/adds")
    fun saveContacts(
        @Body jsonObject: RequestBody
    ): Call<SaveContactsResponse>


    @GET("contact/{number}")
    fun searchContacts(@Path("number") number: String): Call<List<AllContactsResponse>>



}