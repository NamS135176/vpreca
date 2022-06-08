package com.lifecard.vpreca.ui.issuecard

import androidx.lifecycle.*
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.IssueCardRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.balance_amount.GiftInfoResult
import com.lifecard.vpreca.ui.signup.SignUpFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class IssueCardByCodeInputCodeViewModel @Inject constructor(
    private val creditCardRepository: CreditCardRepository,
    private val issueCardRepository: IssueCardRepository
)  : ViewModel() {
    val giftCodeError = MutableLiveData<Int?>()

    private val _giftInfoResult = MutableLiveData<GiftInfoResult>()
    val giftInfoResult: LiveData<GiftInfoResult> = _giftInfoResult
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    fun getGiftData(giftNumber:String){
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
                            //TODO this internalError should be html from server, it will be implement later
                        GiftInfoResult(internalError = "")
                    else -> _giftInfoResult.value =
                        GiftInfoResult(error = ErrorMessageException(R.string.get_list_card_failure))
                }
            }
            _loading.value = false
        }
    }


    val validForm = MediatorLiveData<IssueCardByCodeInputState>().apply {
        value = IssueCardByCodeInputState()
        addSource(giftCodeError) { value ->
            val previous = this.value
            this.value = previous?.copy(giftCodeError = value)
        }
    }

    fun giftCodeDataChanged(text: String) {
        if (!isGiftCodeValid(text)) {
            giftCodeError.value = R.string.empty
        }
        else{
            giftCodeError.value = null
        }
    }

    private fun isGiftCodeValid(id: String): Boolean {
        return id.length == 15
    }

}