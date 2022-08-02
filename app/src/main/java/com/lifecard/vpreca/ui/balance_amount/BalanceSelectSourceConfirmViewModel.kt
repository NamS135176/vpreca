package com.lifecard.vpreca.ui.balance_amount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.IssueCardRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.SuspendDealRepository
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
class BalanceSelectSourceConfirmViewModel @Inject constructor(
    private val issueCardRepository: IssueCardRepository,
    private val suspendDealRepository: SuspendDealRepository
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _feeInfoResult = MutableLiveData<FeeInfoResult>()
    val feeInfoResult: LiveData<FeeInfoResult> = _feeInfoResult

    private val _suspendDealResult = MutableLiveData<SuspendDealResult>()
    val suspendDealResult: LiveData<SuspendDealResult> = _suspendDealResult
    init {
        viewModelScope.launch {
            _loading.value = true
            val result = suspendDealRepository.getListSuspendDeal()

            if (result is Result.Success) {
                _suspendDealResult.value = SuspendDealResult(success = result.data)
            } else if (result is Result.Error) {
                when (result.exception) {
                    is NoConnectivityException -> _suspendDealResult.value =
                        SuspendDealResult(networkTrouble = true)
                    is ApiException -> _suspendDealResult.value = SuspendDealResult(
                        error = ErrorMessageException(
                            errorMessage = result.exception.errorMessage
                        )
                    )
                    is InternalServerException -> _suspendDealResult.value =
                        SuspendDealResult(internalError = "")

                    else -> _suspendDealResult.value =
                        SuspendDealResult(error = ErrorMessageException(R.string.get_list_card_failure))
                }
            }
            _loading.value = false
        }
    }

    fun creditCardSelectDataChanged(
        cardSchemeId: String,
        designId: String,
        cardNickName: String,
        vcnName: String,
        precaNumber: String,
        vcn: String,
        sumUpSrcCardInfo:ArrayList<CardInfoRequestContentInfo>
    ) {
        viewModelScope.launch {
            _loading.value = true
            val cardInfo =
                CardInfoWithDesignIdContentInfo(cardSchemeId, designId, cardNickName, vcnName)
//            val sumUpSrcCardInfo = ArrayList<CardInfoRequestContentInfo>()
//            val data = CardInfoRequestContentInfo(cardSchemeId, precaNumber, vcn)
//            sumUpSrcCardInfo.add(data)
            val res =
                issueCardRepository.issueSumReq(
                    cardInfo,
                    sumUpSrcCardInfo,
                    cardSchemeId,
                    Constant.FEE_TYPE_BALANCE,
                    "1",
                    "1"
                )
            if (res is Result.Success) {
                _feeInfoResult.value = FeeInfoResult(success = res.data)
            } else if (res is Result.Error) {
                when (res.exception) {
                    is NoConnectivityException -> _feeInfoResult.value =
                        FeeInfoResult(networkTrouble = true)
                    is ApiException -> _feeInfoResult.value = FeeInfoResult(
                        error = ErrorMessageException(
                            errorMessage = res.exception.message
                        )
                    )
                    is InternalServerException -> _feeInfoResult.value =
                        FeeInfoResult(internalError = "")
                    else -> _feeInfoResult.value =
                        FeeInfoResult(error = ErrorMessageException(R.string.get_list_card_failure))
                }
            }
            _loading.value = false
        }
    }
}