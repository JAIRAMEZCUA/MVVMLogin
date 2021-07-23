package com.example.mvvmlogin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActivityViewModel extends ViewModel {
    private MutableLiveData <String> user =  new MutableLiveData<>();
    private MutableLiveData <String> pass =  new MutableLiveData<>();



    public ActivityViewModel() {
        defaultSettings();
    }

    private void defaultSettings() {
        user.setValue("User");
        pass.setValue("Pass");
    }

    public MutableLiveData<String> getUser() {
        return user;
    }

    public MutableLiveData<String> getPass() {
        return pass;
    }


    public void setUser(String state) {
        user.setValue(state);
    }

    public void setPass(String state) {
        pass.setValue(state);
    }

    public boolean valid(){
        UseCaseVerify verify = new UseCaseVerify(getUser().getValue(),getPass().getValue());
        return verify.setValidaUsu();
    }
}
