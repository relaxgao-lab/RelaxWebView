package com.relaxgao.common.autoservice.relaxgaowebview;

import com.relaxgao.common.ICallbackFromMainprocessToWebViewProcessInterface;

import java.util.Map;

public interface Command {
    String name();
    void execute(Map parameters, ICallbackFromMainprocessToWebViewProcessInterface callback);
}
