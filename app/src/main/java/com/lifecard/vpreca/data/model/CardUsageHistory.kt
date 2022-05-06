package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class CardUsageHistory(
    @SerializedName("afterBalance")
    val afterBalance: String,
    @SerializedName("beforeBalance")
    val beforeBalance: String,
    @SerializedName("cardDealHistorySequenceNumber")
    val cardDealHistorySequenceNumber: String,
    @SerializedName("creditDealHistorySequenceNumber")
    val creditDealHistorySequenceNumber: String,
    @SerializedName("currencyCode")
    val currencyCode: String,
    @SerializedName("dealEndFlg")
    val dealEndFlg: String,
    @SerializedName("dealNumber")
    val dealNumber: String,
    @SerializedName("dealTotalAmount")
    val dealTotalAmount: String,
    @SerializedName("differenceAmount")
    val differenceAmount: String,
    @SerializedName("exchangeRate")
    val exchangeRate: String,
    @SerializedName("feeAmount")
    val feeAmount: String,
    @SerializedName("foreignFlg")
    val foreignFlg: String,
    @SerializedName("free")
    val free: String,
    @SerializedName("historyAddDate")
    val historyAddDate: Date,
    @SerializedName("historyTypeId")
    val historyTypeId: String,
    @SerializedName("holdAdjustAmount")
    val holdAdjustAmount: String,
    @SerializedName("localCurrencyAmount")
    val localCurrencyAmount: String,
    @SerializedName("localCurrencyDifferenceAmount")
    val localCurrencyDifferenceAmount: String,
    @SerializedName("localCurrencyTotalAmount")
    val localCurrencyTotalAmount: String,
    @SerializedName("relationHistorySequenceNumber")
    val relationHistorySequenceNumber: String,
    @SerializedName("saleShopNameKana")
    val saleShopNameKana: String,
    @SerializedName("saleShopNameKanji")
    val saleShopNameKanji: String,
    @SerializedName("updateAmount")
    val updateAmount: String,
    @SerializedName("uSerializedNameAdjustAmount")
    val uSerializedNameAdjustAmount: String
)