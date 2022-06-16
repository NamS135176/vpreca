package com.lifecard.vpreca.ui.balance_amount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.IssueCardRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.model.CardInfoRequestContentInfo
import com.lifecard.vpreca.data.model.CardInfoWithDesignIdContentInfo
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class BalanceByCodeConfirmViewModel @Inject constructor(
    private val creditCardRepository: CreditCardRepository,
    private val issueCardRepository: IssueCardRepository
) : ViewModel() {
    // TODO: Implement the ViewModel
    private val _issueGiftReqResult = MutableLiveData<IssueGiftResult>()
    val issueGiftReqResult: LiveData<IssueGiftResult> = _issueGiftReqResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun issueGiftCardWithoutCard(
        designId:String,
        giftNumber:String
    ) {
        viewModelScope.launch {
            _loading.value = true

            val res =
                issueCardRepository.issueGiftReqWithCard(designId, giftNumber, Constant.CARD_SCHEME_ID, "", Constant.CARD_NAME)
            if (res is Result.Success) {
                _issueGiftReqResult.value = IssueGiftResult(success = res.data)
            } else if (res is Result.Error) {
                when (res.exception) {
                    is NoConnectivityException -> _issueGiftReqResult.value =
                        IssueGiftResult(networkTrouble = true)
                    is ApiException -> _issueGiftReqResult.value = IssueGiftResult(
                        error = ErrorMessageException(
                            errorMessage = res.exception.message
                        )
                    )
                    is InternalServerException -> _issueGiftReqResult.value =
                            //TODO this internalError should be html from server, it will be implement later
                        IssueGiftResult(internalError = "")
                    else -> _issueGiftReqResult.value =
                        IssueGiftResult(error = ErrorMessageException(R.string.get_list_card_failure))
                }
            }
            _loading.value = false
        }
    }
}