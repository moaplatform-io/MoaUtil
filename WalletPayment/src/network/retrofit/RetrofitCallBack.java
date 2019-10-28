package com.moaplanet.gosing.network.retrofit;

import com.moaplanet.gosing.network.model.CommonModel;
import com.orhanobut.logger.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCallBack<T> implements Callback<T> {

    private RetrofitListener retrofitListener;

    public RetrofitCallBack(RetrofitListener<T> retrofitListener) {
        this.retrofitListener = retrofitListener;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        CommonModel model = (CommonModel) response.body();
        if (model == null) {
            //메시지 처리 필요
            if (retrofitListener != null) {
                retrofitListener.onFail("");
            }
            return;
        }
        if (hasReissuedAccessToken()) {
            Logger.d("onResponse >>> hasReissuedAccessToken");
            requestTokenUpdate();
            return;
        }

        if (retrofitListener != null) {
            //통신은 성공하였으나 서버코드값에 따라 성공/실패 처리 로직 분리 필요
            retrofitListener.onSuccess(model);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (retrofitListener != null) {
            retrofitListener.onNetworkError(t);
        }
    }

    /**
     * 1.기본동작
     * >>>서버에서 토큰 만료 코드를 내려주면 로그인을 통하여 토큰 재발급을 수행한다.
     * <p>
     * 2.현재 앱내 동작
     * >>>서버에서 내려주는 토큰 값, 만료값이 있으면 덮어쓰는 형태로 사용중이다.
     */
    private void requestTokenUpdate() {
        //해당 로직에서 로그인 인터페이스 호출 후 성공시 Callback 처리 수행 필요
        if (retrofitListener != null) {
            retrofitListener.onReissuedAccessToken();
        }
    }

    /**
     * 토큰 만료체크 로직 구현 필요
     */
    private boolean hasReissuedAccessToken(){
        return false;
    }
}
