package com.relaxgao.relaxgao.webviewprocess.webviewclient;

public interface WebViewCallBack {
    void pageStarted(String url);
    void pageFinished(String url);
    void onError();
    void updateTitle(String title);
}
