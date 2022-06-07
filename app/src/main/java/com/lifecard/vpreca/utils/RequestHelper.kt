package com.lifecard.vpreca.utils

import com.lifecard.vpreca.data.model.*

class RequestHelper {
    companion object {
        fun createLoginRequest(loginId: String, loginPassword: String): BrandRequest {

            return BrandRequest(
                brandPrecaApi = Request(
                    request = LoginRequestContent(
                        loginInfo = LoginRequestContentInfo(
                            loginId = loginId,
                            loginPassword = loginPassword
                        )
                    ),
                    head = BaseHead(
                        messageType = MessageType.LoginReq.value
                    )
                ),
            )
        }

        fun createMemberRequest(loginId: String, memberNumber: String): BrandRequest {
            return BrandRequest(
                brandPrecaApi = Request(
                    request = MemberRequestContent(
                        memberInfo = MemberRequestContentInfo(
                            loginId = loginId,
                            memberNumber = memberNumber
                        )
                    ),
                    head = BaseHead(
                        messageType = MessageType.MemberSelReq.value
                    )
                ),
            )
        }

        fun createFeeSelReqRequest(cardSchemeId: String,feeType: String, targetAmount: String): BrandRequest {
            return BrandRequest(
                brandPrecaApi = Request(
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
                ),
            )
        }

        fun createGiftNumberAuthReqRequest(memberNumber: String, giftNumber:String): BrandRequest {
            return BrandRequest(
                return BrandRequest(
                    brandPrecaApi = Request(
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
                    ),
                )
            )
        }

        fun createCardInfoRequest(
            memberNumber: String,
            cardSchemeId: String,
            precaNumber: String,
            vcn: String
        ): BrandRequest {
            return BrandRequest(
                brandPrecaApi = Request(
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
                ),
            )
        }

        fun createIssueSumRequest(
            memberNumber: String,
            cardInfo: CardInfoWithDesignIdContentInfo,
            sumUpInfo: SumUpInfoContentInfo,
            sumUpSrcCardInfo: ArrayList<CardInfoRequestContentInfo>
        ): BrandRequest {
            return BrandRequest(
                brandPrecaApi = Request(
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
                ),
            )
        }

        fun createIssueGiftRequestWithoutCard(
            memberNumber: String,
            cardInfo: CardInfoOnlyDesignId,
            giftNumber: String
        ): BrandRequest {
            return BrandRequest(
                brandPrecaApi = Request(
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
                ),
            )
        }

        fun createIssueGiftRequestWithCard(
            memberNumber: String,
            cardInfo: CardInfoWithCard,
            giftNumber: String
        ): BrandRequest {
            return BrandRequest(
                brandPrecaApi = Request(
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
                ),
            )
        }

        fun createListDesignRequest(
           cardSchemeId: String
        ): BrandRequest {
            return BrandRequest(
                brandPrecaApi = Request(
                    request = ListDesignRequest(
                        cardSchemeId = cardSchemeId
                    ),
                    head = BaseHead(
                        messageType = MessageType.CardDesignSelReq.value
                    )
                ),
            )
        }

        fun createRepublishCardRequest(
            memberNumber: String,
            cardSchemeId: String,
            precaNumber: String,
            vcn: String,
            cooperatorNumber:String?
        ): BrandRequest {
            return BrandRequest(
                brandPrecaApi = Request(
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
                ),
            )
        }

        fun createUpdateCardRequest(
            memberNumber: String,
            creditCard: CreditCard
        ): BrandRequest {
            return BrandRequest(
                brandPrecaApi = Request(
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
                ),
            )
        }

        fun createCardListRequest(
            memberNumber: String,
            invalidCardResFlg: String = "0"
        ): BrandRequest {
            return BrandRequest(
                brandPrecaApi = Request(
                    request = CardListRequestContent(
                        memberInfo = CardListRequestMemberInfo(
                            memberNumber = memberNumber
                        ),
                        invalidCardResFlg = invalidCardResFlg
                    ),
                    head = BaseHead(
                        messageType = MessageType.MemberSelReq.value
                    )
                ),
            )
        }

        fun createSuspendDealListRequest(
            memberNumber: String
        ): BrandRequest {
            return BrandRequest(
                brandPrecaApi = Request(
                    request = SuspendDealRequest(
                        memberInfo = MemberInfoContent(
                            memberNumber = memberNumber
                        )
                    ),
                    head = BaseHead(
                        messageType = MessageType.SuspendDealSelReq.value
                    )
                ),
            )
        }

        fun createCardUsageHistory(
            memberNumber: String,
            creditCard: CreditCard
        ): BrandRequest {
            return BrandRequest(
                brandPrecaApi = Request(
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
                        messageType = MessageType.MemberSelReq.value
                    )
                ),
            )
        }

        fun createChangeInfoMember(
            memberInfo:ChangeInfoMemberData
        ): BrandRequest {
            return BrandRequest(
                brandPrecaApi = Request(
                    request = ChangeInfoMemberRequest(
                        memberInfo = memberInfo
                    ),
                    head = BaseHead(
                        messageType = MessageType.MemberUpdReq.value
                    )
                ),
            )
        }
    }


}