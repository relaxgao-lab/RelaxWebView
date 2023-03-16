package com.relaxgao.relaxgao.webviewprocess;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.relaxgao.relaxgao.bean.JsParam;
import com.relaxgao.relaxgao.webviewprocess.settings.WebViewDefaultSettings;
import com.relaxgao.relaxgao.webviewprocess.webchromeclient.RelaxgaoWebChromeClient;
import com.relaxgao.relaxgao.webviewprocess.webviewclient.WebViewCallBack;
import com.relaxgao.relaxgao.webviewprocess.webviewclient.RelaxGaoWebViewClient;

public class BaseWebView extends WebView {
    public static final String TAG = "WebView";

    public BaseWebView(Context context) {
        super(context);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        WebViewProcessCommandsDispatcher.INSTANCE.initAidlConnection();
        addJavascriptInterface(this, "relaxgaowebview");
        WebViewDefaultSettings.getInstance().setSettings(this);
    }

    public void registerWebViewCallBack(WebViewCallBack webViewCallBack) {
        setWebViewClient(new RelaxGaoWebViewClient(webViewCallBack));
        setWebChromeClient(new RelaxgaoWebChromeClient(webViewCallBack));
    }

    @JavascriptInterface
    public void takeNativeAction(final String jsParam) {
        Log.e(TAG, "this is a call from html javascript." + jsParam);
        if (!TextUtils.isEmpty(jsParam)) {
            final JsParam jsParamObject = new Gson().fromJson(jsParam, JsParam.class);
            if (jsParamObject != null) {
                WebViewProcessCommandsDispatcher.INSTANCE.executeCommand(jsParamObject.name, new Gson().toJson(jsParamObject.param), this);
            }
        }
    }

    public void handleCallback(final String callbackname, final String response){
        if(!TextUtils.isEmpty(callbackname) && !TextUtils.isEmpty(response)){
            post(new Runnable() {
                @Override
                public void run() {
                    String jscode = "javascript:demojs.callback('" + callbackname + "'," + response + ")";
                    evaluateJavascript(jscode, null);
                }
            });
        }
    }
}
