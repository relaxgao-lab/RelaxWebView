package com.relaxgao.relaxgao

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.google.auto.service.AutoService
import com.relaxgao.common.autoservice.RelaxWebViewService
import com.relaxgao.relaxgao.RelaxGaoWebViewFragment.Companion.newInstance

@AutoService(RelaxWebViewService::class)
class RelaxGaoWebViewServiceImpl : RelaxWebViewService {
    override fun startWebViewActivity(
        context: Context,
        url: String,
        title: String,
        isShowActionBar: Boolean,
        canNativeRefresh: Boolean
    ) {
        val intent = Intent(context, RelaxGaoWebViewActivity::class.java)
        intent.putExtra(WEBVIEW_ACTIVITY_URL, url)
        intent.putExtra(WEBVIEW_ACTIVITY_TITLE, title)
        intent.putExtra(WEBVIEW_ACTIVITY_IS_SHOW_ACTIONBAR, isShowActionBar)
        intent.putExtra(WEBVIEW_FRAGMENT_CAN_NATIVE_REFRESH, canNativeRefresh)
        context.startActivity(intent)
    }

    override fun getWebViewFragment(
        url: String,
        canNativeRefresh: Boolean
    ): Fragment {
        return newInstance(url, canNativeRefresh)
    }
}