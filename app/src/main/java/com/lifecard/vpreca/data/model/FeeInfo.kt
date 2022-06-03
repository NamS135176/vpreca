package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class FeeInfo(
    @SerializedName("cardSchemeId")
    val cardSchemeId: String,
    @SerializedName("feeAmount")
    val feeAmount: String,
    @SerializedName("feeApplyMaxAmount")
    val feeApplyMaxAmount: String,
    @SerializedName("feeApplyMinAmount")
    val feeApplyMinAmount: String,
    @SerializedName("feeCalculateType")
    val feeCalculateType: String,
    @SerializedName("feeGetResultType")
    val feeGetResultType: String,
    @SerializedName("feeInclusiveFlg")
    val feeInclusiveFlg: String,
    @SerializedName("feeRate")
    val feeRate: String,
    @SerializedName("feeSequenceNumber")
    val feeSequenceNumber: String,
    @SerializedName("feeStartDate")
    val feeStartDate: String?,
    @SerializedName("feeType")
    val feeType: String,
) : Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}