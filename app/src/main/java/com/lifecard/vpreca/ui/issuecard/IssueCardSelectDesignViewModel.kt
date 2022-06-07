package com.lifecard.vpreca.ui.issuecard

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
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.balance_amount.FeeInfoResult
import com.lifecard.vpreca.ui.home.CreditCardResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class IssueCardSelectDesignViewModel @Inject constructor(
    private val creditCardRepository: CreditCardRepository,
    private val issueCardRepository: IssueCardRepository
) : ViewModel() {
    private val _feeInfoResult = MutableLiveData<FeeInfoResult>()
    val feeInfoResult: LiveData<FeeInfoResult> = _feeInfoResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _listDesignResult = MutableLiveData<ListDesignState>()
    val listDesignResult : LiveData<ListDesignState> = _listDesignResult

    init {
        viewModelScope.launch {
            _loading.value = true
            val result = creditCardRepository.getListDesign("")

            if (result is Result.Success) {
                _listDesignResult.value = ListDesignState(success = result.data)
            } else if (result is Result.Error) {
                when (result.exception) {
                    is NoConnectivityException -> _listDesignResult.value =
                        ListDesignState(networkTrouble = true)
                    is ApiException -> _listDesignResult.value = ListDesignState(
                        error = ErrorMessageException(
                            errorMessage = result.exception.errorMessage,
                            exception = result.exception
                        )
                    )
                    is InternalServerException -> _listDesignResult.value =
                            //TODO this internalError should be html from server, it will be implement later
                        ListDesignState(internalError = "")
                    else -> _listDesignResult.value =
                        ListDesignState( error = ErrorMessageException(
                            messageResId = R.string.login_failed,
                            exception = result.exception
                        ))
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
        sumUpSrcCardInfo : ArrayList<CardInfoRequestContentInfo>
    ) {
        viewModelScope.launch {
            _loading.value = true
            val cardInfo =
                CardInfoWithDesignIdContentInfo(cardSchemeId, designId, cardNickName, vcnName)
            val res =
                issueCardRepository.issueSumReq(cardInfo, sumUpSrcCardInfo, cardSchemeId, "1", "1", sumUpSrcCardInfo.size.toString())
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