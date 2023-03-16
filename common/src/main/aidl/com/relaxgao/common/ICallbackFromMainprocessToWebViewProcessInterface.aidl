// ICallbackFromMainprocessToWebViewProcessInterface.aidl
package com.relaxgao.common;

interface ICallbackFromMainprocessToWebViewProcessInterface {
    void onResult(String kotlinToJavescriptCallBackName, String response);
}
