package com.lifecard.vpreca.ui.webview

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.lifecard.vpreca.base.BackPressFragment
import com.lifecard.vpreca.databinding.FragmentWebViewBinding
import com.lifecard.vpreca.utils.fragmentFindNavController
import com.lifecard.vpreca.utils.hideToolbar
import com.lifecard.vpreca.utils.showToolbar

class WebViewFragment : BackPressFragment() {

    companion object {
        fun newInstance(webUrl: String): WebViewFragment {
            val f = WebViewFragment()
            val args = Bundle()
            args.putString("web_url", webUrl)
            f.arguments = args

            return f
        }

        fun createBundle(webUrl: String): Bundle {
            return bundleOf("web_url" to webUrl)
        }
    }

    private val viewModel: WebViewViewModel by viewModels()
    private var _binding: FragmentWebViewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var isHideToolbar = false

    private var myWebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) return true//only support from 24 and above
            request?.url?.let { url ->
                view?.loadUrl(url.toString())
            }
            return true
        }

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) return false//only support from 23 and bellow
            url?.let { view?.loadUrl(url) }
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            viewModel.handlePageFinished(view, url)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            viewModel.handlePageStarted(view, url, favicon)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        val webView = binding.webview
        val buttonCancel = binding.appbarWebview.buttonCancel

        val webUrl = arguments?.getString("web_url")
        webView.webViewClient = myWebViewClient
        webView.webChromeClient = MyWebChromeClient()
        webUrl?.let { webView.loadUrl(webUrl) }

        buttonCancel.setOnClickListener(View.OnClickListener {
            val navController = fragmentFindNavController()
            navController.popBackStack()
        })

        binding.buttonPrev.setOnClickListener(View.OnClickListener { webView.goBack() })
        binding.buttonForward.setOnClickListener(View.OnClickListener { webView.goForward() })

        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            updateButtonNavigator(loading)
        })
        return binding.root
    }

    private fun updateButtonNavigator(forceDisable: Boolean = false) {
        val webView = binding.webview
        val canGoBack = webView.canGoBack()
        val canGoForward = webView.canGoForward()

        binding.buttonPrev.isEnabled = !forceDisable && webView.canGoBack()
        binding.buttonForward.isEnabled = !forceDisable && webView.canGoForward()
        println("updateButtonNavigator... $forceDisable - canGoBack=$canGoBack - canGoForward=$canGoForward ")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isHideToolbar =
            (requireActivity() as? AppCompatActivity)?.supportActionBar?.isShowing == false
        hideToolbar()
    }

    override fun onDetach() {
        super.onDetach()
        if (!isHideToolbar) showToolbar()//only show toolbar if current is showing
    }
}

private class MyWebChromeClient : WebChromeClient() {

}