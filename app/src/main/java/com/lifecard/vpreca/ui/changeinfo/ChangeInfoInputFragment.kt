package com.lifecard.vpreca.ui.changeinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.data.model.*
import com.lifecard.vpreca.databinding.FragmentChangeInfoInputBinding
import com.lifecard.vpreca.utils.Converter
import com.lifecard.vpreca.utils.closeKeyBoard
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangeInfoInputFragment : Fragment() {

    companion object {
        fun newInstance() = ChangeInfoInputFragment()
    }

    @Inject
    lateinit var userManager: UserManager

    private val viewModel: ChangeInfoInputViewModel by viewModels()
    private var _binding: FragmentChangeInfoInputBinding? = null
    private val binding get() = _binding!!
    private val args: ChangeInfoInputFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeInfoInputBinding.inflate(inflater, container, false)

        binding.constraintChangeInfo.setOnClickListener { closeKeyBoard() }

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
        val toggleSettingFirst = binding.toggleSettingFirst
        val toggleSettingSecond = binding.toggleSettingSecond
        val toggleMagazineFirst = binding.toggleMagazineFirst
        val toggleMagazineSecond = binding.toggleMagazineSecond
        var city = args.userData?.addressPrefecture
        var question = args.userData?.secretQuestion
        val res = resources

        viewModel.originUserData = userManager.memberInfo!!

        val listCity = res.getStringArray(R.array.cities)
        val listQuestion = res.getStringArray(R.array.secret_question)
        idEdt.setText(args.userData?.loginId)
        nicknameEdt.setText(args.userData?.memberRoman)

        kanaFirstName.setText(args.userData?.getFirstKanaName())
        kanaLastName.setText(args.userData?.getLastKanaName())
        hiraFirstName.setText(args.userData?.getFirstMemberName())
        hiraLastName.setText(args.userData?.getLastMemberName())

        spinnerCity.selectItemByIndex(listCity.indexOf(args.userData?.addressPrefecture))
        email1Edt.setText(args.userData?.mailAddress1)
        email1ConfirmEdt.setText(args.userData?.mailAddress1)
        email2Edt.setText(args.userData?.mailAddress2)
        email2ConfirmEdt.setText(args.userData?.mailAddress2)
        spinnerSecret.selectItemByIndex(listQuestion.indexOf(args.userData?.secretQuestion))
        answerEdt.setText(args.userData?.secretQuestionAnswer)
        toggleSettingFirst.isChecked =
            Converter.convertStringToBoolean(args.userData?.mail1AdMailRecieveFlg!!)
        toggleSettingSecond.isChecked =
            Converter.convertStringToBoolean(args.userData?.mail2AdMailRecieveFlg!!)
        toggleMagazineFirst.isChecked =
            Converter.convertStringToBoolean(args.userData?.mail1RecievFlg!!)
        toggleMagazineSecond.isChecked =
            Converter.convertStringToBoolean(args.userData?.mail2RecievFlg!!)

        viewModel.loginIdDataChanged(args.userData?.loginId!!)
        viewModel.nicknameDataChanged(args.userData?.memberRoman!!)
        viewModel.kanaFirstNameDataChanged(args.userData?.getFirstKanaName())
        viewModel.kanaLastNameDataChanged(args.userData?.getLastKanaName())
        viewModel.hiraFirstNameDataChanged(args.userData?.getFirstMemberName())
        viewModel.hiraLastNameDataChanged(args.userData?.getLastMemberName())
        viewModel.cityDataChanged(args.userData?.addressPrefecture!!)
        viewModel.email1DataChanged(args.userData?.mailAddress1!!)
        viewModel.email1ConfirmDataChanged(args.userData?.mailAddress1!!)
        viewModel.email2DataChanged(args.userData?.mailAddress2!!)
        viewModel.email2ConfirmDataChanged(args.userData?.mailAddress2!!)
        viewModel.questionDataChanged(args.userData?.secretQuestion!!)
        viewModel.answerDataChanged(args.userData?.secretQuestionAnswer!!)

        btnBack.setOnClickListener { findNavController().popBackStack() }
        btnCancel.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    findNavController().popBackStack(R.id.nav_home, inclusive = false)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか")
            }.create().show()
        }

        spinnerSecret.lifecycleOwner = viewLifecycleOwner
        spinnerCity.lifecycleOwner = viewLifecycleOwner

        viewModel.validForm.observe(
            viewLifecycleOwner
        ) { isValid ->
            btnSubmit.isEnabled = isValid
        }

        viewModel.nicknameError.observe(
            viewLifecycleOwner
        ) { error: Int? ->
            usernameLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }
        viewModel.idError.observe(viewLifecycleOwner) { error: Int? ->
            idLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }
        viewModel.email1Error.observe(
            viewLifecycleOwner
        ) { error: Int? ->
            email1Layout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }
        viewModel.email2Error.observe(
            viewLifecycleOwner
        ) { error: Int? ->
            email2Layout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }
        viewModel.email1ConfirmError.observe(
            viewLifecycleOwner
        ) { error: Int? ->
            email1ConfirmLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }
        viewModel.email2ConfirmError.observe(
            viewLifecycleOwner
        ) { error: Int? ->
            email2ConfirmLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }
        viewModel.answerError.observe(
            viewLifecycleOwner
        ) { error: Int? ->
            answerLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }

        viewModel.nameError.observe(
            viewLifecycleOwner
        ) { errors: Array<Number?>? ->
            binding.hiraNameInputLayout.error = try {
                val errorInt = errors?.filterNotNull()
                if (!errorInt.isNullOrEmpty()) {
                    println("errorInt: $errorInt - text ${errorInt.map { getString(it as Int) }}")
                    errorInt.joinToString(separator = "\n") { getString(it as Int) }
                } else {
                    null
                }
            } catch (e: Error) {
                null
            }
        }
        viewModel.furiganaNameError.observe(viewLifecycleOwner) { error ->
            when (error) {
                true -> {
                    binding.hiraFirstName.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.input_signup_selected
                    )
                    binding.hiraLastName.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.input_signup_selected
                    )
                }
                else -> {
                    binding.hiraFirstName.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.input_signup_style)
                    binding.hiraLastName.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.input_signup_style)
                }
            }
        }
        viewModel.kanaNameError.observe(viewLifecycleOwner) { error ->
            when (error) {
                true -> {
                    binding.kanaFirstName.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.input_signup_selected
                    )
                    binding.kanaLastName.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.input_signup_selected
                    )
                }
                else -> {
                    binding.kanaFirstName.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.input_signup_style)
                    binding.kanaLastName.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.input_signup_style)
                }
            }
        }

        viewModel.formResultState.observe(viewLifecycleOwner) {
            it?.success?.let {
                val data = ChangeInfoMemberData(
                    memberNumber = args.userData?.memberNumber!!,
                    loginId = idEdt.text.toString(),
                    memberRoman = nicknameEdt.text.toString(),
                    memberKana = kanaFirstName.text.toString() + "　" + kanaLastName.text.toString(),
                    memberName = hiraFirstName.text.toString() + "　" + hiraLastName.text.toString(),
                    addressPrefecture = city,
                    mailAddress1 = email1Edt.text.toString(),
                    mailAddress2 = email2Edt.text.toString(),
                    secretQuestion = question,
                    secretQuestionAnswer = answerEdt.text.toString(),
                    mail1AdMailRecieveFlg = Converter.convertBooleanToString(toggleMagazineFirst.isChecked),
                    mail2AdMailRecieveFlg = Converter.convertBooleanToString(toggleMagazineSecond.isChecked),
                    mail1RecievFlg = Converter.convertBooleanToString(toggleSettingFirst.isChecked),
                    mail2RecievFlg = Converter.convertBooleanToString(toggleSettingSecond.isChecked),
                    telephoneNumber1 = args.userData?.telephoneNumber1!!,
                )
                val action = ChangeInfoInputFragmentDirections.actionChangeInfoInputToConfirm(data)
                findNavController().navigate(action)
                viewModel.formResultState.value = null//for case back from stack
            }
        }
        viewModel.formState.observe(
            viewLifecycleOwner
        ) { viewModel.checkValidForm() }

        spinnerSecret.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { _, _, _, newItem ->
            newItem?.let {
                viewModel.questionDataChanged(text = it)
                question = it
            }
        })
        spinnerCity.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { _, _, _, newItem ->
            newItem?.let {
                viewModel.cityDataChanged(text = it)
                city = it
            }
        })

        spinnerSecret.setOnSpinnerOutsideTouchListener { _, _ -> spinnerSecret.dismiss() }
        spinnerCity.setOnSpinnerOutsideTouchListener { _, _ -> spinnerCity.dismiss() }

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

        btnSubmit.setOnClickListener {
            viewModel.submit()
        }

        return binding.root
    }
}