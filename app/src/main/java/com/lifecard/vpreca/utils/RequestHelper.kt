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
    }


}