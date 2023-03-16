package com.relaxgao.relaxgao

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.relaxgao.base.loadsir.ErrorCallback
import com.relaxgao.base.loadsir.LoadingCallback
import com.relaxgao.relaxgao.databinding.FragmentWebviewBinding
import com.relaxgao.relaxgao.webviewprocess.webviewclient.WebViewCallBack

const val WEBVIEW_FRAGMENT_URL = "url"
const val WEBVIEW_FRAGMENT_CAN_NATIVE_REFRESH = "can_native_refresh"

class RelaxGaoWebViewFragment : Fragment(), OnRefreshListener, WebViewCallBack {
    private var mUrl: String? = null
    private var mCanNativeRefresh = true
    lateinit var mBinding: FragmentWebviewBinding
    lateinit var mLoadService: LoadService<*>

    private var mIsError = false
    private val TAG = "WebViewFragment"

    companion object {
        fun newInstance(url: String?, canNativeRefresh: Boolean): RelaxGaoWebViewFragment {
            val fragment = RelaxGaoWebViewFragment()
            val bundle = Bundle()
            bundle.putString(WEBVIEW_FRAGMENT_URL, url)
            bundle.putBoolean(WEBVIEW_FRAGMENT_CAN_NATIVE_REFRESH, canNativeRefresh)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            mUrl = bundle.getString(WEBVIEW_FRAGMENT_URL)
            mCanNativeRefresh = bundle.getBoolean(WEBVIEW_FRAGMENT_CAN_NATIVE_REFRESH)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_webview, container, false)
        mBinding.webview.registerWebViewCallBack(this)
        mBinding.webview?.loadUrl(mUrl!!)
        mLoadService = LoadSir.getDefault().register(mBinding!!.smartrefreshlayout) {
            mLoadService!!.showCallback(LoadingCallback::class.java)
            mBinding!!.webview.reload()
        }
        mBinding.smartrefreshlayout.setOnRefreshListener(this)
        mBinding.smartrefreshlayout.isEnableRefresh = mCanNativeRefresh
        mBinding.smartrefreshlayout.isEnableLoadMore = false
        return mLoadService.getLoadLayout()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mBinding.webview.reload()
    }

    override fun pageStarted(url: String?) {
        if (mLoadService != null) {
            mLoadService.showCallback(LoadingCallback::class.java)
        }
    }

    override fun pageFinished(url: String?) {
        if (mIsError) {
            mBinding.smartrefreshlayout.isEnableRefresh = true
        } else {
            mBinding.smartrefreshlayout.isEnableRefresh = mCanNativeRefresh
        }
        Log.d(TAG, "pageFinished")
        mBinding.smartrefreshlayout.finishRefresh()
        if (mLoadService != null) {
            if (mIsError) {
                mLoadService.showCallback(ErrorCallback::class.java)
            } else {
                mLoadService.showSuccess()
            }
        }
        mIsError = false
    }

    override fun onError() {
        Log.e(TAG, "onError")
        mIsError = true
        mBinding.smartrefreshlayout.finishRefresh()
    }

    override fun updateTitle(title: String?) {
        if (activity is RelaxGaoWebViewActivity) {
            (activity as RelaxGaoWebViewActivity?)?.updateTitle(title)
        }
    }
}