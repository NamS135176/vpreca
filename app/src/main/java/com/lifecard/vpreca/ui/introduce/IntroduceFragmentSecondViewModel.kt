package com.lifecard.vpreca.ui.introduce

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.changeinfo.ChangeInfoInputResultState
import com.lifecard.vpreca.utils.RegexUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroduceFragmentSecondViewModel  @Inject constructor(
    private val creditCardRepository: CreditCardRepository,
) : ViewModel() {
    val giftError = MutableLiveData<Int?>()
    val vcnError = MutableLiveData<Int?>()
    val validForm = MutableLiveData<Boolean>()
    val formState = MutableLiveData(IntroduceSecondState())
    val formResultState = MutableLiveData<ChangeInfoInputResultState?>()

    private val _giftCardState = MutableLiveData<GiftCardInfoRequestState>()
    val giftCardState: LiveData<GiftCardInfoRequestState> = _giftCardState
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private fun checkGiftNumberValide(): Boolean {
        return if (!RegexUtils.isGiftNumberValid(formState.value?.giftNumber)) {
            giftError.value = R.string.rgx_error_gift_number
            false
        } else {
            giftError.value = null
            true
        }
    }

    fun giftNumberDataChanged(text: String) {
        formState.value = formState.value?.copy(giftNumber = text)
    }

    private fun checkVcnValid(): Boolean {
        return if (formState.value?.vcnFour?.length != 4) {
            vcnError.value = R.string.rgx_error_vcn
            false
        } else {
            vcnError.value = null
            true
        }
    }

    fun vcnDataChanged(text: String) {
        formState.value = formState.value?.copy(vcnFour = text)
    }

    fun submit() {
        val isCfPhoneValid = checkGiftNumberValide()
        val isVcnValid = checkVcnValid()
        if(isCfPhoneValid && isVcnValid){
            formResultState.value = ChangeInfoInputResultState(success = true)
        }
    }

    fun checkFormValid(): Boolean {
        return formState.value?.let { form ->
            val isValid = !arrayOf(form.giftNumber, form.vcnFour)
                .any { it.isNullOrEmpty() }
            validForm.value = isValid
            return isValid
        } ?: false
    }


    fun getGiftCardInfo(confirmationNumber:String, vcnFourDigits:String) {
        viewModelScope.launch {
            _loading.value = true
            val res = creditCardRepository.giftCardInfo(confirmationNumber ,vcnFourDigits)
            if (res is Result.Success) {
                _giftCardState.value = GiftCardInfoRequestState(success = res.data)
            } else if (res is Result.Error) {
                when (res.exception) {
                    is NoConnectivityException -> _giftCardState.value =
                        GiftCardInfoRequestState(networkTrouble = true)
                    is ApiException -> _giftCardState.value = GiftCardInfoRequestState(
                        error = ErrorMessageException(
                            errorMessage = res.exception.errorMessage,
                            exception = res.exception
                        )
                    )
                    is InternalServerException -> _giftCardState.value =
                            //TODO this internalError should be html from server, it will be implement later
                        GiftCardInfoRequestState(internalError = "")
                    else -> _giftCardState.value =
                        GiftCardInfoRequestState(
                            error = ErrorMessageException(
                                messageResId = R.string.login_failed,
                                exception = res.exception
                            )
                        )
                }
            }
            _loading.value = false
        }
    }
}