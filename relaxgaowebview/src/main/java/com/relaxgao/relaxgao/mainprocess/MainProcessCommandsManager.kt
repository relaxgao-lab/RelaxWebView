package com.relaxgao.relaxgao.mainprocess

import android.util.Log
import com.google.gson.Gson
import com.relaxgao.common.ICallbackFromMainprocessToWebViewProcessInterface
import com.relaxgao.common.IWebviewProcessToMainProcessInterface
import com.relaxgao.common.autoservice.relaxgaowebview.Command
import java.util.*
 /**
   * @description主进程服务端的binder
   * @author gaojiangfeng
   * @email  relaxgao@gmail.com
   * @time 2022/3/16 13:59
   */
object MainProcessCommandsManager : IWebviewProcessToMainProcessInterface.Stub() {
    private val mCommands: HashMap<String, Command> = HashMap<String, Command>()

    init {
        //该类初始化的时候 会将commad所有实现类的加载进内存
        val serviceLoader = ServiceLoader.load(
            Command::class.java
        )
        for (command in serviceLoader) {
            if (!mCommands.containsKey(command.name())) {
                mCommands[command.name()] = command
            }
        }
    }

    const val TAG = "CommandsManager"
      /**
        * @description 当webview端的js界面交互后会通过aidl回调该接口，通过命令名称查到对应的命令实现类进行命令的执行
        * @param
        * @return
        * @author gaojiangfeng
        * @email  relaxgao@gmail.com
        */
    override fun handleWebCommand(
        commandName: String?,
        jsonParams: String?,
        callback: ICallbackFromMainprocessToWebViewProcessInterface?
    ) {
        Log.i(TAG, "Main process commands manager handle web command")
        mCommands[commandName]?.execute(Gson().fromJson(jsonParams, MutableMap::class.java), callback)
    }
}