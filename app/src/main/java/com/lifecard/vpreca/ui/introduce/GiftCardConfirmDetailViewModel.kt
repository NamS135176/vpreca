package com.lifecard.vpreca.ui.introduce

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.model.CardInfo
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class GiftCardConfirmDetailViewModel  @Inject constructor(
    private val creditCardRepository: CreditCardRepository,
) : ViewModel() {
    private val _giftCardState = MutableLiveData<GiftCardRelationState>()
    val giftCardState: LiveData<GiftCardRelationState> = _giftCardState
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun giftCardRelation(cardInfo:CardInfo) {
        viewModelScope.launch {
            _loading.value = true
            val res = creditCardRepository.cardRelation(cardInfo.vcn, cardInfo.vcnExpirationDate, cardInfo.securityCode!!, cardInfo.cardNickname)
            if (res is Result.Success) {
                _giftCardState.value = GiftCardRelationState(success = res.data)
            } else if (res is Result.Error) {
                when (res.exception) {
                    is NoConnectivityException -> _giftCardState.value =
                        GiftCardRelationState(networkTrouble = true)
                    is ApiException -> _giftCardState.value = GiftCardRelationState(
                        error = ErrorMessageException(
                            errorMessage = res.exception.errorMessage,
                            exception = res.exception
                        )
                    )
                    is InternalServerException -> _giftCardState.value =
                            //TODO this internalError should be html from server, it will be implement later
                        GiftCardRelationState(internalError = "")
                    else -> _giftCardState.value =
                        GiftCardRelationState(
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