package com.mm.montymobile.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.mm.montymobile.helper.pojo.AllContactsResponse;
import com.mm.montymobile.helper.pojo.MobileVerificationCodeResponse;
import com.mm.montymobile.helper.pojo.RegisterUserResponse;
import com.mm.montymobile.helper.pojo.SaveContactsResponse;
import com.mm.montymobile.repository.AppRepository;
import okhttp3.RequestBody;

public class AppViewModel extends AndroidViewModel {

    public AppViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<MobileVerificationCodeResponse> getVerificationCode(RequestBody body) {
        return AppRepository.getInstance().getVerificationCode(body);
    }


    public LiveData<RegisterUserResponse> registerUser(RequestBody body) {
        return AppRepository.getInstance().verifyCode(body);
    }

    public LiveData<SaveContactsResponse> saveContacts(RequestBody body) {
        return AppRepository.getInstance().addContacts(body);
    }


    public LiveData<AllContactsResponse> searchContacts(String number) {
        return AppRepository.getInstance().searchContacts(number);
    }
}
