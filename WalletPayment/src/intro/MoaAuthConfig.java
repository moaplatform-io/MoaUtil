package com.moaplanet.gosing.intro;
import retrofit2.Call;
import retrofit2.Response;


public class MoaAuthConfig {

    private int totalRetryCnt;
    private int loginFail;
    private String logTagName;

    public int getLoginFail() {
        return loginFail;
    }

    public String getLogTagName() {
        return logTagName;
    }

    public void setLogTagName(String logTagName) {
        this.logTagName = logTagName;
    }

    public void setLoginFail(int loginFail) {
        this.loginFail = loginFail;
    }

    public static boolean isCallSuccess(Call call, Response respoonse){
        return false;
    }
    public int getTotalRetryCnt() {
        return totalRetryCnt;
    }

    public void setTotalRetryCnt(int totalRetryCnt) {
        this.totalRetryCnt = totalRetryCnt;
    }
}
