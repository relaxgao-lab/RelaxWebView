package com.relaxgao.webview;

import android.os.RemoteException;
import android.util.Log;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.relaxgao.base.serviceloader.RelaxServiceLoader;
import com.relaxgao.common.ICallbackFromMainprocessToWebViewProcessInterface;
import com.relaxgao.common.autoservice.IUserCenterService;
import com.relaxgao.common.autoservice.eventbus.LoginEvent;
import com.relaxgao.common.autoservice.relaxgaowebview.Command;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

@AutoService({Command.class})
public class CommandLogin implements Command {
    IUserCenterService iUserCenterService = RelaxServiceLoader.INSTANCE.load(IUserCenterService.class);
    ICallbackFromMainprocessToWebViewProcessInterface callback;
    String callbacknameFromNativeJs;
    public CommandLogin(){
        EventBus.getDefault().register(this);
    }

    @Override
    public String name() {
        return "login";
    }

    @Override
    public void execute(final Map parameters, ICallbackFromMainprocessToWebViewProcessInterface callback) {
        Log.d("CommandLogin", new Gson().toJson(parameters));

        if (iUserCenterService != null && !iUserCenterService.isLogined()) {
            iUserCenterService.login();
            this.callback = callback;//app process callback webview process
            this.callbacknameFromNativeJs = parameters.get("callbackname").toString();//callback js
        }
    }

    @Subscribe
    public void onMessageEvent(LoginEvent event) {
        Log.d("CommandLogin", event.userName);
        HashMap map = new HashMap();
        map.put("accountName", event.userName);
        if(this.callback != null) {
            try {
                this.callback.onResult(callbacknameFromNativeJs, new Gson().toJson(map));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}
