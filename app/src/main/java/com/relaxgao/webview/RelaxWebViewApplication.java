package com.relaxgao.webview;

import com.kingja.loadsir.core.LoadSir;
import com.relaxgao.base.BaseApplication;
import com.relaxgao.base.loadsir.CustomCallback;
import com.relaxgao.base.loadsir.EmptyCallback;
import com.relaxgao.base.loadsir.ErrorCallback;
import com.relaxgao.base.loadsir.LoadingCallback;
import com.relaxgao.base.loadsir.TimeoutCallback;

public class RelaxWebViewApplication extends BaseApplication {

    @Override
    public void onCreate(){
        super.onCreate();
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();
    }
}
