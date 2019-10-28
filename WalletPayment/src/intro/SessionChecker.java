package com.moaplanet.gosing.intro;


import retrofit2.Response;
import retrofit2.Call;

public class SessionChecker {

    private MoaAuthConfig moaAuthConfig;

    private SessionCallback sessionCallback;

    public MoaAuthConfig getMoaAuthConfig() {
        return moaAuthConfig;
    }

    public void setMoaAuthConfig(MoaAuthConfig moaAuthConfig) {
        this.moaAuthConfig = moaAuthConfig;
    }

    public SessionCallback getSessionCallback() {
        return sessionCallback;
    }

    public void setSessionCallback(SessionCallback sessionCallback) {
        this.sessionCallback = sessionCallback;
    }

    public void setRetryCnt(int i){
        /**
         * not implement
         */
    };
    boolean isSucces(Call call, Response respoonse){
        /**
         * not implement
         */
        return true;
    }
    public void retry(Call call){
        /**
         * not implement
         */
    };

    public void sessionCheck(SessionCallback sessionCallback) {
        this.sessionCallback=sessionCallback;
        /**
         * not implement
         */
        sessionCallback.callback(true);
    }


}


