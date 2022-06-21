package com.lifecard.vpreca.ui.web_direct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.databinding.FragmentWebDirectBinding
import com.lifecard.vpreca.ui.webview.WebViewFragment
import com.lifecard.vpreca.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder
import javax.inject.Inject

@AndroidEntryPoint
class WebDirectFragment : Fragment() {

    companion object {
        fun newInstance() = WebDirectFragment()
        fun createBundle(screenId: String): Bundle {
            return bundleOf("screen_id" to screenId)
        }
    }

    @Inject
    lateinit var userManager: UserManager
    private val viewModel: WebDirectViewModel by viewModels()
    private var _binding: FragmentWebDirectBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebDirectBinding.inflate(inflater)
        val screenId = arguments?.getString("screen_id")

        viewModel.otp.observe(viewLifecycleOwner, Observer { otp ->
            otp?.let { otpString ->
                //child fragment

                val memberNumber = userManager.memberNumber
                val webUrl = String.format(Constant.WEB_DIRECT_BASE_URL, screenId)
                val childFragMan = childFragmentManager
                val childFragTrans = childFragMan.beginTransaction()
                //create postdata
                val postData = "screenId=${URLEncoder.encode(screenId, "UTF-8")}" +
                        "&memberNumber=${URLEncoder.encode(memberNumber, "UTF-8")}" +
                        "&onetmPw=$otpString"
                val webFragment = WebViewFragment.newInstance(
                    webUrl = webUrl,
                    method = "post",
                    postData = postData.toByteArray()
                )
                childFragTrans.add(R.id.webview, webFragment)
                childFragTrans.commit()
            }
        })

        screenId?.let { viewModel.getOpt() } ?: kotlin.run {
            //show alert
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton(R.string.button_ok, null)
                setMessage("Screen id not valid. Please check!")//case always not happend
            }.create().show()
        }

        return binding.root
    }

}