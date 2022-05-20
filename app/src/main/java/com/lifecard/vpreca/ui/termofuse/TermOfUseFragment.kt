package com.lifecard.vpreca.ui.termofuse

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.databinding.TermOfUseFragmentBinding
import com.lifecard.vpreca.utils.PreferenceHelper

class TermOfUseFragment : Fragment() {

    companion object {
        fun newInstance() = TermOfUseFragment()
    }

    private var _binding: TermOfUseFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TermOfUseViewModel
    private val loading = MutableLiveData<Boolean>(false)

    private var webViewClient = object : WebViewClient() {

        private fun handleOpenUrl(url: String) {
            if (url.startsWith("http://") || url.startsWith("https://")) {
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
                handleOpenUrl(it.toString())
            }
            return true
        }

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) return false//only support from 23 and bellow
            url?.let { handleOpenUrl(it) }
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            loading.value = true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            loading.value = true
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
        val loadingProgressBar = binding.loading

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
            findNavController().navigate(TermOfUseFragmentDirections.actionTermOfUseToLogin())
        })
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.builtInZoomControls = false
        webView.settings.javaScriptEnabled = false
        webView.webViewClient = webViewClient
        webView.loadUrl("file:///android_asset/term_of_use.html")

        loading.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> loadingProgressBar.visibility = View.VISIBLE
                false -> loadingProgressBar.visibility = View.GONE
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }
}
