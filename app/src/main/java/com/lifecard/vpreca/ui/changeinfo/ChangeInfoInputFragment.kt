package com.lifecard.vpreca.ui.changeinfo

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.ChangeInfoMemberData
import com.lifecard.vpreca.data.model.PhoneData
import com.lifecard.vpreca.databinding.FragmentChangeInfoInputBinding
import com.lifecard.vpreca.utils.Converter
import com.lifecard.vpreca.utils.closeKeyBoard
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import java.util.*

class ChangeInfoInputFragment : Fragment() {

    companion object {
        fun newInstance() = ChangeInfoInputFragment()
    }

    private lateinit var viewModel: ChangeInfoInputViewModel
    private var _binding: FragmentChangeInfoInputBinding? = null
    private val binding get() = _binding!!
    private val args: ChangeInfoInputFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ChangeInfoInputViewModel::class.java)
        _binding = FragmentChangeInfoInputBinding.inflate(inflater, container, false)

        binding.constraintChangeInfo.setOnClickListener(View.OnClickListener { closeKeyBoard() })

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.nav_change_info_data)
                }
            })


        val spinnerCity = binding.spinnerCity
        val spinnerSecret = binding.spinnerSecret
        val btnCancel = binding.appbarSignup.cancelBtn
        val idLayout = binding.idInputLayout
        val usernameLayout = binding.usernameInputLayout
        val idEdt = binding.idInput
        val nicknameEdt = binding.idNickname
        val btnSubmit = binding.btnSubmitPolicy
        val answerLayout = binding.secretAnswerInputLayout
        val answerEdt = binding.idSecret
        val btnBack = binding.appbarSignup.btnBack
        val email1Layout = binding.changeInfoEmail1Layout
        val email1Edt = binding.email1Input
        val email1ConfirmLayout = binding.changeInfoEmail1ConfirmLayout
        val email1ConfirmEdt = binding.email1ConfirmInput
        val email2Layout = binding.changeInfoEmail2Layout
        val email2Edt = binding.email2Input
        val email2ConfirmLayout = binding.changeInfoEmail2ConfirmLayout
        val email2ConfirmEdt = binding.email2ConfirmInput
        val kanaFirstName = binding.kanaFirstName
        val kanaLastName = binding.kanaLastName
        val hiraFirstName = binding.hiraFirstName
        val hiraLastName = binding.hiraLastName
        val toggle1 = binding.toggleSettingFirst
        val toggle2 = binding.toggleSettingSecond
        val toggle3 = binding.toggleMagazineFirst
        val toggle4 = binding.toggleMagazineSecond
        var city = ""
        var question = ""
        val res = resources
        val listCity = res.getStringArray(R.array.cities)
        val listQuestion = res.getStringArray(R.array.secret_question)
        idEdt.setText(args.phoneData?.loginId)
        nicknameEdt.setText(args.phoneData?.memberRoman)
        kanaFirstName.setText(args.phoneData?.memberKana?.split(" ")?.toTypedArray()?.get(0))
        kanaLastName.setText(args.phoneData?.memberKana?.split(" ")?.toTypedArray()?.get(1))
        hiraFirstName.setText(args.phoneData?.memberName?.split(" ")?.toTypedArray()?.get(0)  )
        hiraLastName.setText(args.phoneData?.memberName?.split(" ")?.toTypedArray()?.get(1) )
        spinnerCity.selectItemByIndex(listCity.indexOf(args.phoneData?.addressCity))
        email1Edt.setText(args.phoneData?.mailAddress1)
        email2Edt.setText(args.phoneData?.mailAddress2)
        spinnerSecret.selectItemByIndex(listQuestion.indexOf(args.phoneData?.secretQuestion))
        answerEdt.setText(args.phoneData?.secretQuestionAnswer)
        toggle1.isChecked = Converter.convertStringToBoolean(args.phoneData?.mail1AdMailRecieveFlg!!)
        toggle2.isChecked = Converter.convertStringToBoolean(args.phoneData?.mail2AdMailRecieveFlg!!)
        toggle3.isChecked = Converter.convertStringToBoolean(args.phoneData?.mail1RecievFlg!!)
        toggle4.isChecked = Converter.convertStringToBoolean(args.phoneData?.mail2RecievFlg!!)
        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_change_info_data) })

        btnCancel.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { dialog, which ->
                    // do something on positive button click
//                    val intent = Intent(requireContext(), LoginActivity::class.java).apply {
//                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    }
//                    startActivity(intent)
                    findNavController().navigate(R.id.nav_home)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか")
            }.create().show()
        })

        spinnerSecret.lifecycleOwner = viewLifecycleOwner
        spinnerCity.lifecycleOwner = viewLifecycleOwner

        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { isValid ->
                btnSubmit.isEnabled = isValid
            })

        viewModel.nicknameError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                usernameLayout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })
        viewModel.idError.observe(viewLifecycleOwner, androidx.lifecycle.Observer { error: Int? ->
            idLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })
        viewModel.email1Error.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                email1Layout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })
        viewModel.email2Error.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                email2Layout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })
        viewModel.email1ConfirmError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                email1ConfirmLayout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })
        viewModel.email2ConfirmError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                email2ConfirmLayout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })
        viewModel.answerError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                answerLayout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })

        viewModel.hiraFullNameError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                binding.hiraNameInputLayout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })
        viewModel.kanaFullNameError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                binding.nameInputLayout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })

        viewModel.formResultState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.success?.let {
                val data = ChangeInfoMemberData(
                    args.phoneData?.memberNumber!!,
                    idEdt.text.toString(),
                    nicknameEdt.text.toString(),
                    kanaFirstName.text.toString() + " " + kanaLastName.text.toString(),
                    hiraFirstName.text.toString() + " " + hiraLastName.text.toString(),
                    city,
                    email1Edt.text.toString(),
                    email2Edt.text.toString(),
                    question,
                    answerEdt.text.toString(),
                    Converter.convertBooleanToString(toggle1.isChecked),
                    Converter.convertBooleanToString(toggle2.isChecked),
                    Converter.convertBooleanToString(toggle3.isChecked),
                    Converter.convertBooleanToString(toggle4.isChecked),
                    args.phoneData?.telephoneNumber1!!,
                )
                val action = ChangeInfoInputFragmentDirections.actionChangeInfoInputToConfirm(data)
                findNavController().navigate(action)
            }
        })
        viewModel.formState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { viewModel.checkValidForm() })

        spinnerSecret.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            newItem?.let { viewModel.questionDataChanged(text = it)
            question = it}
        })
        spinnerCity.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            newItem?.let { viewModel.cityDataChanged(text = it)
            city = it}
        })

        idEdt.doAfterTextChanged { text -> viewModel.loginIdDataChanged(text = text.toString()) }
        nicknameEdt.doAfterTextChanged { text -> viewModel.nicknameDataChanged(text = text.toString()) }
        answerEdt.doAfterTextChanged { text -> viewModel.answerDataChanged(text = text.toString()) }
        email1Edt.doAfterTextChanged { text -> viewModel.email1DataChanged(text = text.toString()) }
        email2Edt.doAfterTextChanged { text -> viewModel.email2DataChanged(text = text.toString()) }

        kanaFirstName.doAfterTextChanged { text -> viewModel.kanaFirstNameDataChanged(text = text.toString()) }
        kanaLastName.doAfterTextChanged { text -> viewModel.kanaLastNameDataChanged(text = text.toString()) }
        hiraFirstName.doAfterTextChanged { text -> viewModel.hiraFirstNameDataChanged(text = text.toString()) }
        hiraLastName.doAfterTextChanged { text -> viewModel.hiraLastNameDataChanged(text = text.toString()) }

        email1ConfirmEdt.doAfterTextChanged { text -> viewModel.email1ConfirmDataChanged(text = text.toString()) }
        email2ConfirmEdt.doAfterTextChanged { text -> viewModel.email2ConfirmDataChanged(text = text.toString()) }

        btnSubmit.setOnClickListener(View.OnClickListener {
            viewModel.submit()
        })

        return binding.root
    }
}