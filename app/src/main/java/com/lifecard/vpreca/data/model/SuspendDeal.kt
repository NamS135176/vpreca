package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SuspendDeal(
    @SerializedName("adjustEndFlg")
    val adjustEndFlg: String,
    @SerializedName("authorizationTotalAmount")
    val authorizationTotalAmount: String,
    @SerializedName("brandGwDealNumber")
    val brandGwDealNumber: String,
    @SerializedName("cardSchemeId")
    val cardSchemeId: String,
    @SerializedName("chargebackAmount")
    val chargebackAmount: String,
    @SerializedName("corporationNumber")
    val corporationNumber: String,
    @SerializedName("dccDealFlg")
    val dccDealFlg: String,
    @SerializedName("dealSuspendType")
    val dealSuspendType: String,
    @SerializedName("differenceAmount")
    val differenceAmount: String,
    @SerializedName("foreignFlg")
    val foreignFlg: String?,
    @SerializedName("lastAuthorizationDate")
    val lastAuthorizationDate: String,
    @SerializedName("lastSaleDate")
    val lastSaleDate: String,
    @SerializedName("localCurrencyAuthorizationTotalAmount")
    val localCurrencyAuthorizationTotalAmount: String?,
    @SerializedName("localCurrencyDifferenceAmount")
    val localCurrencyDifferenceAmount: String,
    @SerializedName("localCurrencyTotalAmount")
    val localCurrencyTotalAmount: String,
    @SerializedName("manualAdjustStartDate")
    val manualAdjustStartDate: String?,
    @SerializedName("precaNumber")
    val precaNumber: String,
    @SerializedName("saleShopNameKana")
    val saleShopNameKana: String,
    @SerializedName("saleShopNameKanji")
    val saleShopNameKanji: String,
    @SerializedName("saleTotalAmount")
    val saleTotalAmount: String?,
    @SerializedName("suspendReasonCode")
    val suspendReasonCode: String?,
    @SerializedName("suspendReasonType")
    val suspendReasonType: String?,
    @SerializedName("termNumber")
    val termNumber: String,
    @SerializedName("unadjustDifferenceAmount")
    val unadjustDifferenceAmount: String,
    @SerializedName("vcn")
    val vcn: String,
    @SerializedName("vcnCardId")
    val vcnCardId: String,
    @SerializedName("vcnName")
    val vcnName: String?
) : Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}