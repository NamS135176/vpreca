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