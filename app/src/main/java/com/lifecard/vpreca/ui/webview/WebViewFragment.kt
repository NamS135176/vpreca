package com.lifecard.vpreca.ui.webview

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewClientCompat
import androidx.webkit.WebViewFeature
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentWebViewBinding
import com.lifecard.vpreca.utils.fragmentFindNavController
import com.lifecard.vpreca.utils.hideToolbar
import com.lifecard.vpreca.utils.showToolbar

class WebViewFragment : Fragment() {

    companion object {
        fun newInstance(
            webUrl: String,
            method: String? = "get",
            postData: ByteArray? = null
        ): WebViewFragment {
            val f = WebViewFragment()
            f.arguments = createBundle(webUrl, method, postData)

            return f
        }

        fun createBundle(
            webUrl: String,
            method: String? = "get",
            postData: ByteArray? = null
        ): Bundle {
            return bundleOf("web_url" to webUrl, "method" to method, "post_data" to postData)
        }
    }

    private val viewModel: WebViewViewModel by viewModels()
    private var _binding: FragmentWebViewBinding? = null
    private val args: WebViewFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var isHideToolbar = false

    private var myWebViewClient = object : WebViewClientCompat() {
        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse? {
            return super.shouldInterceptRequest(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            viewModel.handlePageFinished(view, url)
            view?.title.let { title -> binding.appbarWebview.setTitle(title) }
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
        val method = arguments?.getString("method")
        val postData = arguments?.getByteArray("post_data")




        webView.webViewClient = myWebViewClient
        webView.webChromeClient = MyWebChromeClient()
        webView.apply {
            settings.apply {
                javaScriptEnabled = false
                useWideViewPort = true
                domStorageEnabled = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    isForceDarkAllowed = false
                    forceDark = WebSettings.FORCE_DARK_OFF
                }
            }
        }

        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            WebSettingsCompat.setForceDark(webView.settings, WebSettingsCompat.FORCE_DARK_OFF)
        }

        webUrl?.let {
            if ("post" == method && postData != null)
                webView.postUrl(webUrl, postData)
            else webView.loadUrl(webUrl)
        }

        buttonCancel.setOnClickListener(View.OnClickListener {
            val navController = fragmentFindNavController()
            if (navController.previousBackStackEntry?.destination?.id == R.id.nav_policy) {

                val action = WebViewFragmentDirections.actionToPolicy(args.checkState)
                navController.navigate(action)
            } else {
                navController.popBackStack()
            }
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}

private class MyWebChromeClient : WebChromeClient() {

}