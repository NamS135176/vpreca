package com.lifecard.vpreca.utils

import com.lifecard.vpreca.data.model.*

class RequestHelper {
    companion object {
        fun createLoginRequest(loginId: String, loginPassword: String, deviceId: String): Request {
            return Request(
                request = LoginRequestContent(
                    loginInfo = LoginRequestContentInfo(
                        loginId = loginId,
                        loginPassword = loginPassword
                    )
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createMemberRequest(loginId: String, memberNumber: String, deviceId: String): Request {
            return Request(
                request = MemberRequestContent(
                    memberInfo = MemberRequestContentInfo(
                        loginId = loginId,
                        memberNumber = memberNumber
                    )
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createFeeSelReqRequest(
            cardSchemeId: String,
            feeType: String,
            targetAmount: String,
            deviceId: String
        ): Request {
            return Request(
                request = FeeSelReqRequest(
                    feeInfo = FeeSelReqRequestContentInfo(
                        cardSchemeId = cardSchemeId,
                        feeType = feeType,
                        targetAmount = targetAmount
                    )
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createGiftNumberAuthReqRequest(memberNumber: String, giftNumber: String, deviceId: String): Request {
            return Request(
                request = GiftNumberAuthReqRequest(
                    memberInfo = MemberInfoContent(
                        memberNumber = memberNumber
                    ),
                    giftNumberInfo = GiftNumberRequestContentInfo(
                        giftNumber = giftNumber
                    )
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createCardInfoRequest(
            memberNumber: String,
            cardSchemeId: String,
            precaNumber: String,
            vcn: String,
            deviceId: String
        ): Request {
            return Request(
                request = CardInfoRequestContent(
                    memberInfo = MemberInfoContent(
                        memberNumber = memberNumber
                    ),
                    cardInfo = CardInfoRequestContentInfo(
                        cardSchemeId = cardSchemeId,
                        precaNumber = precaNumber,
                        vcn = vcn
                    )
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createCardInfoWithouMemberRequest(
            cardSchemeId: String,
            precaNumber: String,
            vcn: String,
            deviceId: String
        ): Request {
            return Request(
                request = CardInfoWithouMemberRequestContent(
                    cardInfo = CardInfoRequestContentInfo(
                        cardSchemeId = cardSchemeId,
                        precaNumber = precaNumber,
                        vcn = vcn
                    )
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createIssueSumRequest(
            memberNumber: String,
            cardInfo: CardInfoWithDesignIdContentInfo,
            sumUpInfo: SumUpInfoContentInfo,
            sumUpSrcCardInfo: ArrayList<CardInfoRequestContentInfo>,
            deviceId: String
        ): Request {
            return Request(
                request = IssueSumReqRequest(
                    memberInfo = MemberInfoContent(
                        memberNumber = memberNumber
                    ),
                    cardInfo = cardInfo,
                    sumUpInfo = sumUpInfo,
                    sumUpSrcCardInfo = sumUpSrcCardInfo
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createIssueGiftRequestWithoutCard(
            memberNumber: String,
            cardInfo: CardInfoOnlyDesignId,
            giftNumber: String,
            deviceId: String
        ): Request {
            return Request(
                request = IssueGiftReqWithoutCardRequest(
                    memberInfo = MemberInfoContent(
                        memberNumber = memberNumber
                    ),
                    cardInfo = cardInfo,
                    chargeInfo = GiftNumberRequestContentInfo(giftNumber = giftNumber)
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createIssueGiftRequestWithCard(
            memberNumber: String,
            cardInfo: CardInfoWithCard,
            giftNumber: String,
            deviceId: String
        ): Request {
            return Request(
                request = IssueGiftReqWithCardRequest(
                    memberInfo = MemberInfoContent(
                        memberNumber = memberNumber
                    ),
                    cardInfo = cardInfo,
                    chargeInfo = GiftNumberRequestContentInfo(giftNumber = giftNumber)
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createListDesignRequest(
            cardSchemeId: String,
            deviceId: String
        ): Request {
            return Request(
                request = ListDesignRequest(
                    cardSchemeId = cardSchemeId
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createRepublishCardRequest(
            memberNumber: String,
            cardSchemeId: String,
            precaNumber: String,
            vcn: String,
            cooperatorNumber: String?,
            deviceId: String
        ): Request {
            return Request(
                request = CardRepublishRequest(
                    memberInfo = MemberInfoContent(
                        memberNumber = memberNumber
                    ),
                    cardInfo = CardRepublishRequestContentInfo(
                        cardSchemeId = cardSchemeId,
                        precaNumber = precaNumber,
                        vcn = vcn,
                        cooperatorNumber = cooperatorNumber
                    )
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createUpdateCardRequest(
            memberNumber: String,
            creditCard: CreditCard,
            deviceId: String
        ): Request {
            return Request(
                request = UpdateCardRequest(
                    memberInfo = MemberInfoContent(
                        memberNumber = memberNumber
                    ),
                    cardInfo = CardInfoRequestContentInfo(
                        cardSchemeId = creditCard.cardSchemeId,
                        precaNumber = creditCard.precaNumber,
                        vcn = creditCard.vcn
                    ),
                    changeInfo = UpdateCardRequestContentInfo(
                        nickName = NickNameContent(cardNickname = creditCard.cardNickname),
                        autoCharge = AutoChargeContent(
                            autoChargeFlg = creditCard.autoChargeFlg,
                            autoChargeThereshold = creditCard.autoChargeThereshold,
                            autoChargeAmount = creditCard.autoChargeAmount
                        )
                    ),
                    securityLock = SecurityLockContent(
                        vcnSecurityLockFlg = creditCard.vcnSecurityLockFlg
                    )
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createCardListRequest(
            memberNumber: String,
            invalidCardResFlg: String = "0",
            deviceId: String
        ): Request {
            return Request(
                request = CardListRequestContent(
                    memberInfo = CardListRequestMemberInfo(
                        memberNumber = memberNumber
                    ),
                    invalidCardResFlg = invalidCardResFlg
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createSuspendDealListRequest(
            memberNumber: String,
            deviceId: String
        ): Request {
            return Request(
                request = SuspendDealRequest(
                    memberInfo = MemberInfoContent(
                        memberNumber = memberNumber
                    )
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createCardUsageHistory(
            memberNumber: String,
            creditCard: CreditCard,
            deviceId: String
        ): Request {
            return Request(
                request = CardUsageHistoryRequestContent(
                    memberInfo = CardUsageMemberInfoRequest(
                        memberNumber = memberNumber
                    ),
                    cardInfo = CardUsageCardInfoRequest(
                        cardSchemeId = creditCard.cardSchemeId,
                        precaNumber = creditCard.precaNumber,
                    ),
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createCardUsageHistoryWithouMember(
            creditCard: CreditCard,
            deviceId: String
        ): Request {
            return Request(
                request = CardUsageHistoryWithouMemberRequestContent(
                    cardInfo = CardUsageCardInfoRequest(
                        cardSchemeId = creditCard.cardSchemeId,
                        precaNumber = creditCard.precaNumber,
                    ),
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createChangeInfoMember(
            memberInfo: ChangeInfoMemberData,
            deviceId: String
        ): Request {
            return Request(
                request = ChangeInfoMemberRequest(
                    memberInfo = memberInfo
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createChangePassRequest(
            memberInfo: PasswordUpdateMemberInfoContent,
            deviceId: String
        ): Request {
            return Request(
                request = PasswordUpdateRequest(
                    memberInfo = memberInfo
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createResetPassRequest(
            memberInfo: PasswordResetMemberInfoContent,
            deviceId: String
        ): Request {
            return Request(
                request = PasswordResetRequest(
                    memberInfo = memberInfo
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createGiftCertifiRequest(
            cardInfo: GiftCertifiCardInfoRequestContentInfo,
            deviceId: String
        ): Request {
            return Request(
                request = GiftCertifiRequest(
                    cardInfo = cardInfo
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createCardRelationRequest(
            memberNumber: String,
            cardInfo: CardRelationRegRequestContentInfo,
            deviceId: String
        ): Request {
            return Request(
                request = CardRelationRegReqRequest(
                    memberInfo = MemberInfoContent(
                        memberNumber = memberNumber
                    ),
                    cardInfo = cardInfo
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createSMSConfirm(
            memberNumber: String,
            certType: String,
            operationType: String,
            certCode: String,
            extCertDealId: String,
            deviceId: String
        ): Request {
            return Request(
                request = SMSAuthRequest(
                    memberInfo = MemberInfoContent(
                        memberNumber = memberNumber
                    ),
                    certInfo = SMSAuthRequestContentInfo(
                        certType = certType,
                        operationType = operationType,
                        certCode = certCode,
                        extCertDealId = extCertDealId
                    )
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createSMSIvrConfirm(
            certType: String,
            loginId: String,
            certCode: String,
            extCertDealId: String,
            deviceId: String
        ): Request {
            return Request(
                request = SmsIvrAuthRequest(
                    certType = certType,
                    loginId = loginId,
                    certCode = certCode,
                    extCertDealId = extCertDealId
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

        fun createSendSMSRequest(
          loginId: String,
          deviceId: String
        ): Request {
            return Request(
                request = SendSMSRequest(
                  loginId = loginId
                ),
                head = BaseHead(
                    appliTermId = deviceId
                )
            )
        }

    }
}