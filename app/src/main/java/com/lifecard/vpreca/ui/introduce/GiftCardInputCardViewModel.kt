package com.lifecard.vpreca.ui.introduce

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.R
import com.lifecard.vpreca.ui.issuecard.IssueCardByCodeInputState

class GiftCardInputCardViewModel : ViewModel() {
    val giftCodeError = MutableLiveData<Int?>()

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