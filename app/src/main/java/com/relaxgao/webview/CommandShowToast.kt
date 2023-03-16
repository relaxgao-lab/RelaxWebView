package com.relaxgao.webview

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.auto.service.AutoService
import com.relaxgao.base.BaseApplication
import com.relaxgao.common.ICallbackFromMainprocessToWebViewProcessInterface
import com.relaxgao.common.autoservice.relaxgaowebview.Command

@AutoService(Command::class)
class CommandShowToast : Command {
    override fun name(): String {
        return "showToast"
    }

    override fun execute(
        parameters: Map<*, *>,
        callback: ICallbackFromMainprocessToWebViewProcessInterface?
    ) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            Toast.makeText(
                BaseApplication.sApplication,
                parameters["message"].toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}