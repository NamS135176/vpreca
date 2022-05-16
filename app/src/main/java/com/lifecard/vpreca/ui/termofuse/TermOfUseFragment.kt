package com.lifecard.vpreca.ui.termofuse

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.TermOfUseFragmentBinding
import com.lifecard.vpreca.ui.webview.WebViewFragment
import com.lifecard.vpreca.utils.PreferenceHelper

class TermOfUseFragment : Fragment() {

    companion object {
        fun newInstance() = TermOfUseFragment()
    }

    private var _binding: TermOfUseFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TermOfUseViewModel
//    private lateinit var buttonToolbarBack: AppCompatButton

    private var webViewClient = object : WebViewClient() {

        private fun handleOpenUrl(view: WebView?, url: String) {
            if (url.contains("privacy_policy.html") && url.contains("file://")) {
                view?.loadUrl("file:///android_asset/privacy_policy.html")
//                buttonToolbarBack.visibility = View.VISIBLE
            } else if (url.startsWith("http://") || url.startsWith("https://")) {
                //open webview fragment
                findNavController().navigate(TermOfUseFragmentDirections.actionTermOfUseToWeb(url))
//                findNavController().navigate(R.id.nav_webview, WebViewFragment.createBundle(url))
            }
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) return true//only support from 24 and above
            request?.url?.let {
                handleOpenUrl(view, it.toString())
            }
            return true
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) return false//only support from 23 and bellow
            url?.let { handleOpenUrl(view, it) }
            return true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TermOfUseFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(TermOfUseViewModel::class.java)

        val btnSubmit = binding.btnSubmitTermOfUse
        val cbTermOfUse = binding.cbTermOfUse
        val webView = binding.webview
//        buttonToolbarBack = binding.appbar.buttonBack

//        buttonToolbarBack.visibility = View.INVISIBLE
//        buttonToolbarBack.setOnClickListener(View.OnClickListener {
//            buttonToolbarBack.visibility = View.INVISIBLE
//            webView.loadUrl("file:///android_asset/term_of_use.html")
//        })

        cbTermOfUse.setOnClickListener(View.OnClickListener {
            context?.let { it1 ->
                PreferenceHelper.setAcceptTermOfUseFirstTime(
                    appContext = it1,
                    value = true
                )
            }
            btnSubmit.isEnabled = cbTermOfUse.isChecked
        })

        btnSubmit.setOnClickListener(View.OnClickListener {
            context?.let { it1 ->
                PreferenceHelper.setAcceptTermOfUseFirstTime(
                    appContext = it1,
                    value = true
                )
            }
            findNavController().navigate(R.id.nav_login)
//            val intent = Intent(activity, MainActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            }
//            startActivity(intent)
        })
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.builtInZoomControls = false
        webView.settings.javaScriptEnabled = false
        webView.webViewClient = webViewClient
        webView.loadUrl("file:///android_asset/term_of_use.html")
        return binding.root
    }

}
