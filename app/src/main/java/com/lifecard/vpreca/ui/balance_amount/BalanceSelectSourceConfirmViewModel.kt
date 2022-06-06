package com.lifecard.vpreca.ui.balance_amount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.IssueCardRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.SuspendDealRepository
import com.lifecard.vpreca.data.model.*
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.home.CreditCardResult
import com.lifecard.vpreca.ui.listvpreca.CardInfoResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BalanceSelectSourceConfirmViewModel @Inject constructor(
    private val creditCardRepository: CreditCardRepository,
    private val issueCardRepository: IssueCardRepository
) : ViewModel() {
    private val _feeInfoResult = MutableLiveData<FeeInfoResult>()
    val feeInfoResult: LiveData<FeeInfoResult> = _feeInfoResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun creditCardSelectDataChanged(
        cardSchemeId: String,
        designId: String,
        cardNickName: String,
        vcnName: String,
        precaNumber: String,
        vcn: String
    ) {
        viewModelScope.launch {
            _loading.value = true
            val cardInfo =
                CardInfoWithDesignIdContentInfo(cardSchemeId, designId, cardNickName, vcnName)
            val sumUpSrcCardInfo = ArrayList<CardInfoRequestContentInfo>()
            val data = CardInfoRequestContentInfo(cardSchemeId, precaNumber, vcn)
            sumUpSrcCardInfo.add(data)
            val res =
                issueCardRepository.issueSumReq(cardInfo, sumUpSrcCardInfo, cardSchemeId, "1", "1","1")
            if (res is Result.Success) {
                _feeInfoResult.value = FeeInfoResult(success = res.data)
            } else if (res is Result.Error) {
                when (res.exception) {
                    is NoConnectivityException -> _feeInfoResult.value =
                        FeeInfoResult(networkTrouble = true)
                    is ApiException -> FeeInfoResult(
                        error = ErrorMessageException(
                            errorMessage = res.exception.message
                        )
                    )
                    is InternalServerException -> _feeInfoResult.value =
                            //TODO this internalError should be html from server, it will be implement later
                        FeeInfoResult(internalError = "")
                    else -> _feeInfoResult.value =
                        FeeInfoResult(error = ErrorMessageException(R.string.get_list_card_failure))
                }
            }
            _loading.value = false
        }
    }
}