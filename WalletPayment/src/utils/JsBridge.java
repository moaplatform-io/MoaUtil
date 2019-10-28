package com.moaplanet.gosing.utils;

import android.webkit.JavascriptInterface;

import com.moaplanet.gosing.common.interfaces.JsReceiver;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class JsBridge implements JsReceiver {

    private JsReceiver jsReceiver;

    public JsBridge(JsReceiver jsReceiver) {
        this.jsReceiver = jsReceiver;
    }

    @JavascriptInterface
    public void showHTML(String html) {
        if (html.indexOf('{') == -1) {
            onJsReceiverFail();
            return;
        }
        String resultJson = html.substring(html.indexOf('{'), html.indexOf('}') + 1);
        try {
            new JSONObject(resultJson);
        } catch (JSONException e) {
            Logger.d("Json Error\n" + e);
            return;
        }
        if (resultJson.length() == 0) {
            onJsReceiverFail();
            return;
        }
        onJsReceiverSuccess(resultJson);
    }

    @Override
    public void onJsReceiverSuccess(String resultMsg) {
        jsReceiver.onJsReceiverSuccess(resultMsg);
    }

    @Override
    public void onJsReceiverFail() {

    }
}
