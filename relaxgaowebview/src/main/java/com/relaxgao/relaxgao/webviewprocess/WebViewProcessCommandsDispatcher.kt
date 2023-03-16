package com.relaxgao.relaxgao.webviewprocess

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.relaxgao.base.BaseApplication
import com.relaxgao.common.ICallbackFromMainprocessToWebViewProcessInterface
import com.relaxgao.common.IWebviewProcessToMainProcessInterface
import com.relaxgao.relaxgao.mainprocess.MainProcessCommandService

object WebViewProcessCommandsDispatcher : ServiceConnection {
    private var iWebviewProcessToMainProcessInterface: IWebviewProcessToMainProcessInterface? = null

    fun initAidlConnection() {
        val intent = Intent(BaseApplication.sApplication, MainProcessCommandService::class.java)
        BaseApplication.sApplication?.bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    override fun onServiceConnected(componentName: ComponentName?, iBinder: IBinder?) {
        iWebviewProcessToMainProcessInterface =
            IWebviewProcessToMainProcessInterface.Stub.asInterface(iBinder)
    }

    override fun onServiceDisconnected(componentName: ComponentName?) {
        iWebviewProcessToMainProcessInterface = null
        initAidlConnection()
    }

    fun executeCommand(commandName: String, parameters: String, baseWebView: BaseWebView) {
        iWebviewProcessToMainProcessInterface?.handleWebCommand(
            commandName,
            parameters,
            object: ICallbackFromMainprocessToWebViewProcessInterface.Stub() {
                override fun onResult(callbackname: String?, response: String?) {
                    baseWebView.handleCallback(callbackname, response)
                }
            })
    }
}