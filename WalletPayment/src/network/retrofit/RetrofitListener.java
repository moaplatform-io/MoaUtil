package com.moaplanet.gosing.network.retrofit;

/**
 * Controller Listener
 */
public interface RetrofitListener<T> {
    void onSuccess(T responseData);
    void onReissuedAccessToken();
    void onFail(String msg);
    void onNetworkError(Throwable t);
}