package com.lifecard.vpreca.ui.balance_amount

import androidx.lifecycle.*
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.IssueCardRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.changeinfo.ChangeInfoInputResultState
import com.lifecard.vpreca.ui.issuecard.IssueCardByCodeInputState
import com.lifecard.vpreca.ui.signup.ConfirmPhoneState
import com.lifecard.vpreca.utils.RegexUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BalanceByCodeInputViewModel @Inject constructor(
    private val issueCardRepository: IssueCardRepository
) : ViewModel() {

    val codeError = MutableLiveData<Int?>()
    val validForm = MutableLiveData<Boolean>()
    val formState = MutableLiveData(ConfirmPhoneState())
    var formResultState = MutableLiveData<ChangeInfoInputResultState?>()

    private fun checkCfPhoneValid(): Boolean {
        return if (!RegexUtils.isGiftNumberValid(formState.value?.confirmCode)) {
            codeError.value = R.string.error_code
            false
        } else {
            codeError.value = null
            true
        }
    }

    fun codeDataChanged(text: String) {
        formState.value = formState.value?.copy(confirmCode = text)
    }

    fun submit() {
        val isCfPhoneValid = checkCfPhoneValid()
        if(isCfPhoneValid){
            formResultState.value = ChangeInfoInputResultState(success = true)
        }
    }

    fun checkFormValid(): Boolean {
        return formState.value?.let { form ->
            val isValid = !arrayOf(form.confirmCode)
                .any { it.isNullOrEmpty() }
            validForm.value = isValid
            return isValid
        } ?: false
    }

    var _giftInfoResult = MutableLiveData<GiftInfoResult?>()
//    val giftInfoResult: LiveData<GiftInfoResult> = _giftInfoResult
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    fun getGiftData(giftNumber: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = issueCardRepository.giftNumberAuthReq(giftNumber)

            if (result is Result.Success) {
                _giftInfoResult.value = GiftInfoResult(success = result.data)
            } else if (result is Result.Error) {
                when (result.exception) {
                    is NoConnectivityException -> _giftInfoResult.value =
                        GiftInfoResult(networkTrouble = true)
                    is ApiException -> _giftInfoResult.value = GiftInfoResult(
                        error = ErrorMessageException(
                            errorMessage = result.exception.errorMessage
                        )
                    )
                    is InternalServerException -> _giftInfoResult.value =
                        GiftInfoResult(internalError = "")
                    else -> _giftInfoResult.value =
                        GiftInfoResult(error = ErrorMessageException(R.string.get_list_card_failure))
                }
            }
            _loading.value = false
        }
    }


}