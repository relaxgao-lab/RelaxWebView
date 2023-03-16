package com.relaxgao.webview;

import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;

import com.google.auto.service.AutoService;
import com.relaxgao.base.BaseApplication;
import com.relaxgao.common.ICallbackFromMainprocessToWebViewProcessInterface;
import com.relaxgao.common.autoservice.relaxgaowebview.Command;

import java.util.Map;

@AutoService(Command.class)
public class CommandOpenPage implements Command {

    @Override
    public String name() {
        return "openPage";
    }

    @Override
    public void execute(Map parameters, ICallbackFromMainprocessToWebViewProcessInterface callback) {
        String targetClass = String.valueOf(parameters.get("target_class"));
        if (!TextUtils.isEmpty(targetClass)) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(BaseApplication.Companion.getSApplication(), targetClass));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            BaseApplication.Companion.getSApplication().startActivity(intent);
        }
    }
}
