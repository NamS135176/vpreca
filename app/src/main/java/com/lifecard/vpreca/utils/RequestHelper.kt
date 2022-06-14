package com.lifecard.vpreca.utils

import com.lifecard.vpreca.data.model.*

class RequestHelper {
    companion object {
        fun createLoginRequest(loginId: String, loginPassword: String): Request {

            return Request(
                request = LoginRequestContent(
                    loginInfo = LoginRequestContentInfo(
                        loginId = loginId,
                        loginPassword = loginPassword
                    )
                ),
                head = BaseHead(
                    messageType = MessageType.LoginReq.value
                )
            )
        }

        fun createMemberRequest(loginId: String, memberNumber: String): Request {
            return Request(
                request = MemberRequestContent(
                    memberInfo = MemberRequestContentInfo(
                        loginId = loginId,
                        memberNumber = memberNumber
                    )
                ),
                head = BaseHead(
                    messageType = MessageType.MemberSelReq.value
                )
            )
        }

        fun createFeeSelReqRequest(
            cardSchemeId: String,
            feeType: String,
            targetAmount: String
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
                    messageType = MessageType.FeeSelReq.value
                )
            )
        }

        fun createGiftNumberAuthReqRequest(memberNumber: String, giftNumber: String): Request {
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
                    messageType = MessageType.GiftNumberAuthReq.value
                )
            )
        }

        fun createCardInfoRequest(
            memberNumber: String,
            cardSchemeId: String,
            precaNumber: String,
            vcn: String
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
                    messageType = MessageType.CardSelReq.value
                )
            )
        }

        fun createCardInfoWithouMemberRequest(
            cardSchemeId: String,
            precaNumber: String,
            vcn: String
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
                    messageType = MessageType.CardSelReq.value
                )
            )
        }

        fun createIssueSumRequest(
            memberNumber: String,
            cardInfo: CardInfoWithDesignIdContentInfo,
            sumUpInfo: SumUpInfoContentInfo,
            sumUpSrcCardInfo: ArrayList<CardInfoRequestContentInfo>
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
                    messageType = MessageType.IssueSumReq.value
                )
            )
        }

        fun createIssueGiftRequestWithoutCard(
            memberNumber: String,
            cardInfo: CardInfoOnlyDesignId,
            giftNumber: String
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
                    messageType = MessageType.IssueGiftReq.value
                )
            )
        }

        fun createIssueGiftRequestWithCard(
            memberNumber: String,
            cardInfo: CardInfoWithCard,
            giftNumber: String
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
                    messageType = MessageType.IssueGiftReq.value
                )
            )
        }

        fun createListDesignRequest(
            cardSchemeId: String
        ): Request {
            return Request(
                request = ListDesignRequest(
                    cardSchemeId = cardSchemeId
                ),
                head = BaseHead(
                    messageType = MessageType.CardDesignSelReq.value
                )
            )
        }

        fun createRepublishCardRequest(
            memberNumber: String,
            cardSchemeId: String,
            precaNumber: String,
            vcn: String,
            cooperatorNumber: String?
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
                    messageType = MessageType.LoginReq.value
                )
            )
        }

        fun createUpdateCardRequest(
            memberNumber: String,
            creditCard: CreditCard
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
                    messageType = MessageType.CardUpdReq.value
                )
            )
        }

        fun createCardListRequest(
            memberNumber: String,
            invalidCardResFlg: String = "0"
        ): Request {
            return Request(
                request = CardListRequestContent(
                    memberInfo = CardListRequestMemberInfo(
                        memberNumber = memberNumber
                    ),
                    invalidCardResFlg = invalidCardResFlg
                ),
                head = BaseHead(
                    messageType = MessageType.MemberSelReq.value
                )
            )
        }

        fun createSuspendDealListRequest(
            memberNumber: String
        ): Request {
            return Request(
                request = SuspendDealRequest(
                    memberInfo = MemberInfoContent(
                        memberNumber = memberNumber
                    )
                ),
                head = BaseHead(
                    messageType = MessageType.SuspendDealSelReq.value
                )
            )
        }

        fun createCardUsageHistory(
            memberNumber: String,
            creditCard: CreditCard
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
                    messageType = MessageType.CardDealHisReq.value
                )
            )
        }

        fun createCardUsageHistoryWithouMember(
            creditCard: CreditCard
        ): Request {
            return Request(
                request = CardUsageHistoryWithouMemberRequestContent(
                    cardInfo = CardUsageCardInfoRequest(
                        cardSchemeId = creditCard.cardSchemeId,
                        precaNumber = creditCard.precaNumber,
                    ),
                ),
                head = BaseHead(
                    messageType = MessageType.CardDealHisReq.value
                )
            )
        }

        fun createChangeInfoMember(
            memberInfo: ChangeInfoMemberData
        ): Request {
            return Request(
                request = ChangeInfoMemberRequest(
                    memberInfo = memberInfo
                ),
                head = BaseHead(
                    messageType = MessageType.MemberUpdReq.value
                )
            )
        }

        fun createChangePassRequest(
            memberInfo: PasswordUpdateMemberInfoContent
        ): Request {
            return Request(
                request = PasswordUpdateRequest(
                    memberInfo = memberInfo
                ),
                head = BaseHead(
                    messageType = MessageType.PasswordUpdReq.value
                )
            )
        }

        fun createResetPassRequest(
            memberInfo: PasswordResetMemberInfoContent
        ): Request {
            return Request(
                request = PasswordResetRequest(
                    memberInfo = memberInfo
                ),
                head = BaseHead(
                    messageType = MessageType.PasswordResetReq.value
                )
            )
        }

        fun createGiftCertifiRequest(
            cardInfo: GiftCertifiCardInfoRequestContentInfo
        ): Request {
            return Request(
                request = GiftCertifiRequest(
                    cardInfo = cardInfo
                ),
                head = BaseHead(
                    messageType = MessageType.CardCertifiNoMemReq.value
                )
            )
        }

        fun createCardRelationRequest(
            memberNumber: String,
            cardInfo: CardRelationRegRequestContentInfo
        ): Request {
            return Request(
                request = CardRelationRegReqRequest(
                    memberInfo = MemberInfoContent(
                        memberNumber = memberNumber
                    ),
                    cardInfo = cardInfo
                ),
                head = BaseHead(
                    messageType = MessageType.CardRelationRegReq.value
                )
            )
        }
    }
}