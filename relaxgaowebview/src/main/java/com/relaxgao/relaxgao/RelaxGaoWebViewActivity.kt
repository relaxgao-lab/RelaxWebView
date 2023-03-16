package com.relaxgao.relaxgao

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.relaxgao.relaxgao.databinding.ActivityRelaxgaoWebviewBinding


const val WEBVIEW_ACTIVITY_URL = "url"
const val WEBVIEW_ACTIVITY_TITLE = "title"
const val WEBVIEW_ACTIVITY_IS_SHOW_ACTIONBAR = "is_show_actionbar"

class RelaxGaoWebViewActivity : AppCompatActivity() {
    private val webViewTag = "WebViewActivity"
    lateinit var mBinding: ActivityRelaxgaoWebviewBinding
    lateinit var mUrl: String
    lateinit var mTitle: String
    var mCanNativeRefresh: Boolean = true
    var mIsShowActionBar: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_relaxgao_webview)
        mUrl = intent?.extras?.getString(WEBVIEW_ACTIVITY_URL) ?: "https://www.baidu.com"
        mTitle = intent?.extras?.getString(WEBVIEW_ACTIVITY_TITLE) ?: "WebView"
        mIsShowActionBar = intent?.extras?.getBoolean(WEBVIEW_ACTIVITY_IS_SHOW_ACTIONBAR) ?: true
        mCanNativeRefresh = intent?.extras?.getBoolean(WEBVIEW_FRAGMENT_CAN_NATIVE_REFRESH) ?: true

        mBinding.title.text = mTitle
        if (!mIsShowActionBar) {
            mBinding.actionBar.visibility = View.GONE
        }
        mBinding.back.setOnClickListener { this@RelaxGaoWebViewActivity.finish() }
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val fragment: Fragment =
            RelaxGaoWebViewFragment.newInstance(intent.getStringExtra(WEBVIEW_FRAGMENT_URL), canNativeRefresh = mCanNativeRefresh)
        transaction.replace(R.id.web_view_fragment, fragment).commit()
    }

    fun updateTitle(title: String?) {
        mBinding.title.text = title
    }
}