package com.lifecard.vpreca.ui.termofuse

import android.content.Context
import android.content.SharedPreferences
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
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
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

        viewModel.phoneDataChanged(sharedPref?.getBoolean("checked",false)!!)

        cbTermOfUse.setOnClickListener(View.OnClickListener {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putBoolean("checked", cbTermOfUse.isChecked )
            editor.apply()
            editor.commit()
            viewModel.phoneDataChanged(cbTermOfUse.isChecked)
        })

        viewModel.validForm.observe(viewLifecycleOwner, Observer {
            btnSubmit.isEnabled = it
            cbTermOfUse.isChecked = it
        })

        btnSubmit.setOnClickListener(View.OnClickListener {
            PreferenceHelper.setAcceptTermOfUseFirstTime(
                appContext = requireContext(),
                value = true
            )
            findNavController().navigate(R.id.nav_login)
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
