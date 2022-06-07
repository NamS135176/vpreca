package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class IssueSelectSourceData(
    @SerializedName("listCard")
    val listCard:@RawValue List<CreditCard>,
    @SerializedName("listSelectCard")
    val listSelectCard:@RawValue List<SelectedData>,
) : Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}