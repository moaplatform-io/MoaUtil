package com.moaplanet.gosing.intro;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

abstract public class MoaAuthCallback<T>  implements Callback<T> {
    private  Call<T> call;
    private int retryCount = 0;
    private MoaAuthConfig authConfig;

    private SessionChecker sessionChecker;

    public MoaAuthCallback(MoaAuthConfig authConfig,SessionChecker sessionChecker) {
        this.authConfig=authConfig;
        this.sessionChecker=sessionChecker;
    }
    public MoaAuthCallback(Call<T> call) {
        this.call=call;
    }
    @Override
    public void onResponse(Call<T> call, final Response<T> response) {
        if(this.call==null){
            this.call=call;
        }
        sessionChecker.sessionCheck(new SessionCallback() {
            @Override
            public void callback(boolean isT) {
                if(isT)
                    onFinalResponse(call,response);
                else{
                    onFinalFailure(call,false,new Exception("Moa session not invalid"));
                }
            }
        });
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if(this.call==null){
            this.call=call;
        }
        Log.e(authConfig.getLogTagName(), t.getMessage());
        if (retryCount++ < authConfig.getTotalRetryCnt()) {
            Log.v(authConfig.getLogTagName(), "Retrying API Call -  (" + retryCount + " / " + authConfig.getTotalRetryCnt() + ")");
            retry();
        } else
            onFinalFailure(call,true, t);
    }

    abstract  public void onFinalResponse(Call<T> call, Response<T> response) ;

    abstract  public void onFinalFailure(Call<T> call,boolean isSession, Throwable t) ;

    private void retry() {
        Log.i(authConfig.getLogTagName(),"retry::"+this.retryCount);
        call.clone().enqueue(this);
    }
}
