package com.example.mvvmlogin;

public class UseCaseVerify {

    private String strEmailAddress;
    private String strPassword;

    private String UsuarioCorrecto="avillanueva@na-at.com.mx",PassCorrecto="c775e7b757ede630cd0aa1113bd102661ab38829ca52a6422ab782862f268646";

    public UseCaseVerify(String EmailAddress, String Password) {
        strEmailAddress = EmailAddress;
        strPassword = Password;
    }

    public Boolean setValidaUsu() {
        if((strEmailAddress.equals(UsuarioCorrecto) && strPassword.equals(PassCorrecto))){
            return true;
        }else{
            return false;
        }
    }

}
