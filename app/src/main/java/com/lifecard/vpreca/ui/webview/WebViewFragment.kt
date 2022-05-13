package com.lifecard.vpreca.ui.webview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.lifecard.vpreca.databinding.FragmentWebViewBinding
import com.lifecard.vpreca.utils.fragmentFindNavController
import com.lifecard.vpreca.utils.hideToolbar
import com.lifecard.vpreca.utils.showToolbar

class WebViewFragment : Fragment() {

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

    private lateinit var viewModel: WebViewViewModel
    private var _binding: FragmentWebViewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        val webView = binding.webview
        val buttonCancel = binding.appbarWebview.buttonCancel

        val webUrl = arguments?.getString("web_url")
        webView.webViewClient = MyWebViewClient()
        webView.webChromeClient = MyWebChromeClient()
        webUrl?.let { webView.loadUrl(webUrl) }

        buttonCancel.setOnClickListener(View.OnClickListener {
            val navController = fragmentFindNavController()
            navController.popBackStack()
        })

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hideToolbar()
    }

    override fun onDetach() {
        super.onDetach()
        showToolbar()
    }
}

private class MyWebViewClient : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        url?.let { view?.loadUrl(url) }
        return true
    }
}

private class MyWebChromeClient : WebChromeClient() {

}