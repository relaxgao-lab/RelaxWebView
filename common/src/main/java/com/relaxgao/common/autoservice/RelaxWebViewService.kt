package com.relaxgao.common.autoservice

import android.content.Context
import androidx.fragment.app.Fragment

interface RelaxWebViewService {
    fun startWebViewActivity(
        context: Context,
        url: String,
        title: String = "WebView页面",
        isShowActionBar: Boolean = true,
        canNativeRefresh: Boolean = true
    )

    fun getWebViewFragment(
        url: String,
        canNativeRefresh: Boolean = true
    ): Fragment
}