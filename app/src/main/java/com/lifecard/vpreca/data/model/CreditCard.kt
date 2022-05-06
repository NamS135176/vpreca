package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreditCard(
    @SerializedName("activateStatus")
    val activateStatus: String,
    @SerializedName("activateDate")
    val activateDate: String,
    @SerializedName("autoChargeAmount")
    val autoChargeAmount: String,
    @SerializedName("autoChargeErrFlg")
    val autoChargeErrFlg: String,
    @SerializedName("autoChargeFlg")
    val autoChargeFlg: String,
    @SerializedName("autoChargeThereshold")
    val autoChargeThereshold: String,
    @SerializedName("cardAccessDate")
    val cardAccessDate: String,
    @SerializedName("cardAccessFlg")
    val cardAccessFlg: String,
    @SerializedName("cardNickname")
    val cardNickname: String,
    @SerializedName("cardImageFile")
    val cardImageFile: String,
    @SerializedName("cardPublishDate")
    val cardPublishDate: String,
    @SerializedName("cardPublishFlg")
    val cardPublishFlg: String,
    @SerializedName("cardRegistDate")
    val cardRegistDate: String,
    @SerializedName("cardSchemeId")
    val cardSchemeId: String,
    @SerializedName("cardStatus")
    val cardStatus: String,
    @SerializedName("cardUnusableDate")
    val cardUnusableDate: String,
    @SerializedName("cardUseStopFlg")
    val cardUseStopFlg: String,
    @SerializedName("chargeBalance")
    val chargeBalance: String,
    @SerializedName("chargeLimit")
    val chargeLimit: String,
    @SerializedName("confirmationNumber")
    val confirmationNumber: String,
    @SerializedName("cooperatorNumber")
    val cooperatorNumber: String,
    @SerializedName("cooperatorNumberId")
    val cooperatorNumberId: String,
    @SerializedName("creditBrandId")
    val creditBrandId: String,
    @SerializedName("designId")
    val designId: String,
    @SerializedName("designName")
    val designName: String,
    @SerializedName("initialPurchaseAmount")
    val initialPurchaseAmount: String,
    @SerializedName("lastUseDate")
    val lastUseDate: String,
    @SerializedName("memberRepublishCnt")
    val memberRepublishCnt: String,
    @SerializedName("memberRepublishDate")
    val memberRepublishDate: String,
    @SerializedName("onlinePinRegFlg")
    val onlinePinRegFlg: String,
    @SerializedName("precaExpirationDate")
    val precaExpirationDate: String,
    @SerializedName("precaNumber")
    val precaNumber: String,
    @SerializedName("publishAddFree")
    val publishAddFree: String,
    @SerializedName("publishAmount")
    val publishAmount: String,
    @SerializedName("publishFee")
    val publishFee: String,
    @SerializedName("publishType")
    val publishType: String,
    @SerializedName("rechargedActFlg")
    val rechargedActFlg: String,
    @SerializedName("thumbnailCardImageFile")
    val thumbnailCardImageFile: String,
    @SerializedName("useLimit")
    val useLimit: String,
    @SerializedName("vcn")
    val vcn: String,
    @SerializedName("vcnCardId")
    val vcnCardId: String,
    @SerializedName("vcnExpirationDate")
    val vcnExpirationDate: String,
    @SerializedName("vcnSecurityLockFlg")
    val vcnSecurityLockFlg: String,
) : Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}