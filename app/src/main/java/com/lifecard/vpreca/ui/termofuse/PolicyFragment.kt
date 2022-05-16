package com.lifecard.vpreca.ui.termofuse

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentPolicyWebBinding
import com.lifecard.vpreca.databinding.TermOfUseFragmentBinding
import com.lifecard.vpreca.ui.webview.WebViewFragment
import com.lifecard.vpreca.utils.PreferenceHelper

class PolicyFragment : Fragment() {

    companion object {
        fun newInstance() = PolicyFragment()
    }

    private var _binding: FragmentPolicyWebBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TermOfUseViewModel

    private var webViewClient = object : WebViewClient() {

        private fun handleOpenUrl(view: WebView?, url: String) {
            if (url.contains("privacy_policy.html") && url.contains("file://")) {
                view?.loadUrl("file:///android_asset/privacy_policy.html")
            } else if (url.startsWith("http://") || url.startsWith("https://")) {
                //open webview fragment
                findNavController().navigate(TermOfUseFragmentDirections.actionTermOfUseToWeb(url))
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
        _binding = FragmentPolicyWebBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(TermOfUseViewModel::class.java)

        val btnSubmit = binding.btnSubmitTermOfUse
        val cbTermOfUse = binding.cbTermOfUse
        val webView = binding.webview
        val buttonToolbarBack = binding.appbar.btnBack

        buttonToolbarBack.setOnClickListener(View.OnClickListener {
            findNavController().popBackStack()
        })

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
            findNavController().navigate(PolicyFragmentDirections.actionPolicyWebToLogin())
        })
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.builtInZoomControls = false
        webView.settings.javaScriptEnabled = false
        webView.webViewClient = webViewClient
        webView.loadUrl("file:///android_asset/privacy_policy.html")
        return binding.root
    }

}
