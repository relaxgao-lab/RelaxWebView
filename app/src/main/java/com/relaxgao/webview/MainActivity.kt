package com.relaxgao.webview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.relaxgao.common.autoservice.RelaxWebViewService
import com.relaxgao.base.serviceloader.RelaxServiceLoader

class MainActivity : AppCompatActivity() {
    private val relaxServiceLoader = RelaxServiceLoader.load(RelaxWebViewService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommandLogin() == CommandLogin();
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.start_demo).setOnClickListener {
            relaxServiceLoader?.startWebViewActivity(this, "https://www.baidu.com", "测试标题")
        }

        findViewById<Button>(R.id.start_local_demo_html).setOnClickListener {
            relaxServiceLoader?.startWebViewActivity(this, "file:///android_asset/demo.html", "测试标题")
        }

//        GlobalScope.launch {
//            val time = measureTimeMillis {
//                val one = async { doSomethingUsefulOne() }
//                val two = async { doSomethingUsefulTwo() }
//                println("Completed The answer is ${one.await() + two.await()}")
//            }
//            println("Completed in $time ms")
//        }
    }

//    fun main() = runBlocking<Unit> {
//        val time = measureTimeMillis {
//            val one = async { doSomethingUsefulOne() }
//            val two = async { doSomethingUsefulTwo() }
//            println("The answer is ${one.await() + two.await()}")
//        }
//        println("Completed in $time ms")
//    }
//    suspend fun doSomethingUsefulOne(): Int {
//        delay(2000L) // pretend we are doing something useful here
//        return 13
//    }
//
//    suspend fun doSomethingUsefulTwo(): Int {
//        delay(2000L) // pretend we are doing something useful here, too
//        return 29
//    }
}