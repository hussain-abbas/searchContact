package com.mm.montymobile.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.gson.Gson;
import com.mm.montymobile.helper.api.ApiClient;
import com.mm.montymobile.helper.api.ApiClientVerification;
import com.mm.montymobile.helper.api.ApiResponse;
import com.mm.montymobile.helper.pojo.AllContactsResponse;
import com.mm.montymobile.helper.pojo.MobileVerificationCodeResponse;
import com.mm.montymobile.helper.pojo.RegisterUserResponse;
import com.mm.montymobile.helper.pojo.SaveContactsResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class AppRepository {

    private static AppRepository repository;

    public static synchronized AppRepository getInstance() {
        if (repository == null) {
            repository = new AppRepository();
            return repository;
        }

        return repository;
    }


    public LiveData<MobileVerificationCodeResponse> getVerificationCode(RequestBody body)
    {
        final MutableLiveData<MobileVerificationCodeResponse> mobileVerificationLiveData = new MutableLiveData<>();
        ApiResponse client = ApiClientVerification.getClient().create(ApiResponse.class);
        Call<MobileVerificationCodeResponse> call = client.getVerificationCode(body);
        call.enqueue(new Callback<MobileVerificationCodeResponse>() {
            @Override
            public void onResponse(@NonNull Call<MobileVerificationCodeResponse> call, @NonNull Response<MobileVerificationCodeResponse> response) {
                Gson gson = new Gson();
                String data = gson.toJson(response.body());
                Log.d("RESPONSE", "onResponse: "+data);
                Log.d("RESPONSE", "onResponse: " + response.raw());
                if (response.isSuccessful())
                {
                    MobileVerificationCodeResponse res  = gson.fromJson(data , MobileVerificationCodeResponse.class);
                    mobileVerificationLiveData.setValue(res);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MobileVerificationCodeResponse> call, @NonNull Throwable t) {
                Log.e("RESPONSE", "onFailure createOrganizationResponse: ", t);
                MobileVerificationCodeResponse res = new MobileVerificationCodeResponse();
                res.setMessageCount(0);
                mobileVerificationLiveData.setValue(res);
            }
        });

        return mobileVerificationLiveData;
    }



    public LiveData<RegisterUserResponse> verifyCode(RequestBody body)
    {
        final MutableLiveData<RegisterUserResponse> verifyLiveData = new MutableLiveData<>();
        ApiResponse client = ApiClient.getClient().create(ApiResponse.class);
        Call<RegisterUserResponse> call = client.registerUser(body);
        call.enqueue(new Callback<RegisterUserResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterUserResponse> call, @NonNull Response<RegisterUserResponse> response) {
                Gson gson = new Gson();
                String data = gson.toJson(response.body());
                Log.d("RESPONSE", "onResponse: "+data);
                Log.d("RESPONSE", "onResponse: " + response.raw());
                if (response.isSuccessful())
                {
                    RegisterUserResponse res  = gson.fromJson(data , RegisterUserResponse.class);
                    res.setResponse("200");
                    verifyLiveData.setValue(res);
                }
                else if (response.code() == 403)
                {
                    RegisterUserResponse res = new RegisterUserResponse();
                    res.setResponse("403");
                    verifyLiveData.setValue(res);
                }
                else
                {
                    RegisterUserResponse res = new RegisterUserResponse();
                    res.setResponse("404");
                    verifyLiveData.setValue(res);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterUserResponse> call, @NonNull Throwable t) {
                Log.e("RESPONSE", "onFailure createOrganizationResponse: ", t);
                RegisterUserResponse res = new RegisterUserResponse();
                res.setResponse("404");
                verifyLiveData.setValue(res);
            }
        });

        return verifyLiveData;
    }



    public LiveData<SaveContactsResponse> addContacts(RequestBody body)
    {
        final MutableLiveData<SaveContactsResponse> saveContactsLD = new MutableLiveData<>();
        ApiResponse client = ApiClient.getClient().create(ApiResponse.class);
        Call<SaveContactsResponse> call = client.saveContacts(body);
        call.enqueue(new Callback<SaveContactsResponse>() {
            @Override
            public void onResponse(@NonNull Call<SaveContactsResponse> call, @NonNull Response<SaveContactsResponse> response) {
                Gson gson = new Gson();
                String data = gson.toJson(response.body());
                Log.d("RESPONSE", "onResponse: "+data);
                Log.d("RESPONSE", "onResponse: " + response.raw());
                if (response.isSuccessful())
                {
                    SaveContactsResponse res  = gson.fromJson(data , SaveContactsResponse.class);
                    res.setResponse("200");
                    saveContactsLD.setValue(res);
                }
                else
                {
                    SaveContactsResponse res = new SaveContactsResponse();
                    res.setResponse("404");
                    saveContactsLD.setValue(res);
                }

            }

            @Override
            public void onFailure(@NonNull Call<SaveContactsResponse> call, @NonNull Throwable t) {
                Log.e("RESPONSE", "onFailure createOrganizationResponse: ", t);
                SaveContactsResponse res = new SaveContactsResponse();
                res.setResponse("404");
                saveContactsLD.setValue(res);
            }
        });

        return saveContactsLD;
    }


    public LiveData<AllContactsResponse> searchContacts(String number)
    {
        final MutableLiveData<AllContactsResponse> contactLD = new MutableLiveData<>();
        ApiResponse client = ApiClient.getClient().create(ApiResponse.class);
        Call<List<AllContactsResponse>> call = client.searchContacts(number);
        call.enqueue(new Callback<List<AllContactsResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<AllContactsResponse>> call, @NonNull Response<List<AllContactsResponse>> response) {
                Gson gson = new Gson();
                String data = gson.toJson(response.body());
                Log.d("RESPONSE", "onResponse: "+data);
                Log.d("RESPONSE", "onResponse: " + response.raw());
                if (response.isSuccessful())
                {
                    AllContactsResponse[] res  = gson.fromJson(data , AllContactsResponse[].class);

                    if (res.length > 0) {
                        for (int i = 0; i < res.length; i++) {
                            contactLD.setValue(res[i]);
                        }
                    }
                    else
                    {
                        AllContactsResponse res2 = new AllContactsResponse();
                        res2.setResponse("404");
                        contactLD.setValue(res2);
                    }

                }

                else
                {
                    AllContactsResponse res = new AllContactsResponse();
                    res.setResponse("404");
                    contactLD.setValue(res);
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<AllContactsResponse>> call, @NonNull Throwable t) {
                Log.e("RESPONSE", "onFailure createOrganizationResponse: ", t);
                AllContactsResponse res = new AllContactsResponse();
                res.setResponse("404");
                contactLD.setValue(res);
            }
        });

        return contactLD;
    }
}
