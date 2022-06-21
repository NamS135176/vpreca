package com.lifecard.vpreca.utils

@Suppress("kotlin:S1192") // String literals should not be duplicated
class ApiError {
    companion object {
        const val otherErrorCode = "9999999"
        val otherErrorMessage = "システムエラーです。"

        private val commonErrors = mapOf(
            //common: 外部インタフェース定義書(IFBPAP0101_共通部).pdf
            "9999001" to "メッセージダイジェスト検証エラーです。",
            "9999002" to "センタ識別子不存在エラーです。",
            "9999003" to "未加入ブランドスキームエラーです。",
            "9999004" to "対象電文権限不足エラーです。",
            "9999005" to "該当電文処理中エラーです。",
            "9999901" to "ロック取得エラーです。",
            "9999902" to "DB障害エラーです。",
            "9999903" to "マスタ定義エラーです。",
            "9999904" to "DB論理エラーです。",
            "9999101" to "外部センタ通信エラー(プリカセンタ決済) です。",
            "9999102" to "外部センタ通信エラー(プリカセンタAPI) です。",
            "9999103" to "外部センタ通信エラー(ブランドプリカセンタAPI) です。",
            "9999111" to "外部センタ通信エラー(BlueGateワンクリ) です。",
            "9999112" to "外部センタ通信エラー(ブランドGW) です。",
            "9999113" to "外部センタ通信エラー(HTTP変換GW) です。",
            "9999121" to "外部センタ通信エラー(VeriTrans) です。",
            "9999122" to "外部センタ通信エラー(NTTコム) です。",
            "9999999" to "システムエラーです。",
            //end common
        )

        private val AP1101_loginErrors = mapOf(
            //login: 外部インタフェース定義書(IFBPAP1101_ログイン).pdf
            "1101101" to "そのログインIDの会員情報が存在しません。",
            "1101102" to "アカウントロック中です。",
            "1101103" to "ログインパスワードが正しくありません。",
            //end login
        )
        private val AP1102_memberQueryError = mapOf(
            //member query: 外部インタフェース定義書(IFBPAP1102_会員情報照会).pdf
            "1101101" to "そのログインIDの会員情報が存在しません。",
            "1101102" to "アカウントロック中です。",
            "1101103" to "ログインパスワードが正しくありません。",
            //end member query
        )
        private val AP1104_cardListQueryError = mapOf(

            //card list information inquiry: 外部インタフェース定義書(IFBPAP1104_カード一覧情報照会).pdf
            "1104101" to "会員情報が存在しません。",
            "1104102" to "会員情報が有効ではありません。",
            "1104103" to "登録プリカ情報が存在しません。",
            "9999904" to "DB論理エラーです。",
            //end card list information inquiry
        )

        private val AP1105_cardDetailQueryError = mapOf(
            //card detailed information inquiry: 外部インタフェース定義書(IFBPAP1105_カード詳細情報照会) .pdf
            "1105101" to "会員情報が存在しません。",
            "1105102" to "会員情報が有効ではありません。",
            "1105103" to "カードの情報が取得できません。",
            "1105104" to "会員番号とカードの情報が紐づいていません。",
            //end card detailed information inquiry

        )

        private val AP1106_pendingTransactionInquiryQueryError = mapOf(
            //Pending Transaction Inquiry: 外部インタフェース定義書(IFBPAP1106_保留中取引照会).pdf
            "1106101" to "会員情報が存在しません。",
            "1106102" to "会員情報が有効ではありません。",
            //end Pending Transaction Inquiry
        )

        private val AP1107_reciptNumberAuthenticationError = mapOf(
            //receipt number authentication: 外部インタフェース定義書(IFBPAP1107_受取番号認証).pdf
            "1107101" to "会員情報が存在しません。",
            "1107102" to "会員情報が有効ではありません。",
            "1107103" to "受付番号登録ロック中です。",
            "1107104" to "受付番号が存在しません。",
            "1107105" to "受付番号が未利用ではありません。",
            "1107106" to "受付番号が利用可能期間外です。",
            //end receipt number authentication
        )

        private val AP1108_memberListError = mapOf(
            //member list error: 外部インタフェース定義書(IFBPAP1107_受取番号認証).pdf
            "1108101" to "要求電文で連携された会員が存在しない場合",
            "1108102" to "要求電文で連携された会員が会員情報取得上限件数（プロパティ）を超えた場合",
            //end member list error
        )


        private val AP1202_cardUsageHistoryQueryError = mapOf(
            //card usage history inquiry: 外部インタフェース定義書(IFBPAP1202_カード利用履歴照会).pdf
            "1202101" to "会員情報が存在しません。",
            "1202102" to "会員情報が有効ではありません。",
            "1202103" to "カード情報が存在しません。",
            "1202104" to "カード情報取得フラグが取得済ではありません。",
            //end receipt number authentication
        )
        private val AP1501_cardDesignError = mapOf(
            //card design inquiry: 外部インタフェース定義書(IFBPAP1501_カードデザイン照会).pdf
            "1501101" to "カードデザインが存在しません。",
            //end receipt number authentication
        )

        private val AP2101_membershipRegistrationError = mapOf(
            //Membership registration: AP2101_会員登録
            "2101101" to "ブランドスキームID内に同一ログインIDが既に登録されています。",
            "9999901" to "会員番号の払い出しに失敗しました。",
            "9999904" to "会員番号の連番が払い出し可能な最大の番号に到達しています。",
            //end Membership registration
        )

        private val AP2102_memberInformationChangeError = mapOf(
            //Member Information change: 外部インタフェース定義書(IFBPAP2102_会員情報変更).pdf
            "2102101" to "会員情報が存在しません。",
            "2102102" to "会員情報が有効ではありません。",
            "2102103" to "ブランドスキームID内に同一ログインIDが既に登録されています。",
            "2102104" to "ACS認証がログインIDを使用する場合、変更されたログインID+パスワードの桁数が3Dパスワードの規定値の桁数を超えています。",
            //end Member Information change
        )

        private val AP2104_passwordResetError = mapOf(
            //Password reset: 外部インタフェース定義書(IFBPAP2104_パスワード再設定依頼).pdf
            "2104101" to "会員情報が存在しません、または有効ではありません。",
            "2104102" to "会員のメールアドレス1が登録されていません。",
            //end Password reset
        )

        private val AP2105_passwordChangeError = mapOf(
            //Password reset: 外部インタフェース定義書(IFBPAP2105_パスワード変更).pdf
            "2105101" to "会員情報が存在しません。",
            "2105102" to "会員情報が有効ではありません。",
            "2105103" to "パスワード再設定用コードの有効期限が切れています。",
            "2105104" to "現在のログインパスワード、会員情報テーブルのログインパスワードが不整合です。",
            "2105105" to "ACS認証がログインIDを使用する場合、変更されたログインID+パスワードの桁数が3Dパスワードの規定値の桁数を超えています。",
            "2105106" to "ブランドスキーム情報が取得できません。",
            //end Password reset
        )

        private val AP2113_MobileConfirmSendRequestError = mapOf(
            //Mobile confirm send request: 外部インタフェース定義書(IFBPAP2113_携帯番号確認依頼).xls
            "2113101" to "要求電文で連携された携帯番号の確認依頼回数が上限回数を超えている場合",
            "2113102" to "ブランドスキーム外部認証機関設定情報が存在しない場合",
            "2113103" to "オープン化メール定義情報が存在しない場合",
            "2113104" to "SMS認証依頼にてエラーが発生した場合",
            "9999903" to "外部認証設定情報が存在しない場合",
            //end Mobile confirm send request
        )
        private val AP2114_MobileConfirmRequestError = mapOf(
            //Mobile Confirm Req: 外部インタフェース定義書(IFBPAP2114_携帯番号確認).xls
            "2113101" to "要求電文で連携された携帯番号の確認依頼回数が上限回数を超えている場合",
            "2113102" to "ブランドスキーム外部認証機関設定情報が存在しない場合",
            "2113103" to "オープン化メール定義情報が存在しない場合",
            "2113104" to "SMS認証依頼にてエラーが発生した場合",
            "9999903" to "外部認証設定情報が存在しない場合",
            //end Mobile Confirm Req
        )

        private val AP2115_MailConfirmSendReqError = mapOf(
            //Mail Confirm Send Req: 外部インタフェース定義書(IFBPAP2115_メールアドレス確認依頼).xls
            "2115101" to "要求電文で連携された会員が存在しない場合",
            "2115102" to "要求電文で連携された会員が有効でない場合",
            "2115103" to "要求電文で連携された会員のメールアドレスが存在しない場合",
            "2115104" to "要求電文で連携されたメールアドレス又は会員番号に紐づくメールアドレスの確認依頼回数が上限回数を超えている場合",
            "9999903" to "外部認証設定情報が存在しない場合",
            //end Mail Confirm Send Req
        )

        private val AP2116_MailConfirmReqError = mapOf(
            //Mail Confirm Req: 外部インタフェース定義書(IFBPAP2116_メールアドレス確認).xls
            "2116101" to "連絡先確認履歴が存在しない場合",
            "2116102" to "要求電文で連携された確認コードが既に、認証済みの場合",
            "2116103" to "要求電文で連携された確認コードがロックされている場合",
            "2116104" to "要求電文で連携された確認コードがコード有効時間を超過している場合",
            "2116105" to "要求電文で連携された確認コードが不一致の場合",
            "9999903" to "外部認証設定情報の閾値情報が取得できない場合",
            //end Mail Confirm Req
        )

        private val AP2201_cardLinkRegistrationError = mapOf(
            //card link registration: 外部インタフェース定義書(IFBPAP2201_カード紐付け登録).pdf
            "2201101" to "会員情報が存在しません。",
            "2201102" to "会員情報が有効ではありません。",
            "2201103" to "プリカ登録ロック中です。",
            "2201104" to "カードの情報が取得できません。",
            "2201105" to "プリカ番号が既に登録されています。",
            "2201106" to "カードステータスが無効です。",
            "2201107" to "プリカ有効期限が切れています。",
            "2201108" to "ブランドプリカGWに照会した結果、VCNステータスが無効です。",
            "2201109" to "VCNの登録誤り回数が、規定値を超えています。",
            "2201110" to "VCN有効期限またはVCNセキュリティコードが間違っています。",
            "2201111" to "ブランドスキームの会員カード保持上限枚数を超えています。",
            "2201112" to "クレジットブランドの会員カード保持上限枚数を超えています。",
            "2201201" to "カードスキームがアクティベート対象で、かつアクティベートカードステータスが未アクティベートの場合のエラーです。",
            "2201202" to "カード利用停止フラグが停止中です。",
            //end card link registration
        )

        private val AP2202_cardInformationChangeError = mapOf(
            //card information change: 外部インタフェース定義書(IFBPAP2202_カード情報変更).pdf
            "2202101" to "会員情報が存在しません。",
            "2202102" to "会員情報が有効ではありません。",
            "2202103" to "カードの情報が取得できません。",
            "2202104" to "会員番号とカードの情報が紐づいていません。",
            "2202105" to "プリカが無効状態です。",
            "2202106" to "プリカがVCNの有効期限切れです。",
            "2202107" to "ブランドプリカGWにおける、VCNステータスが無効です。",
            "2202108" to "クレジット情報が登録されていない会員へのオートチャージ設定変更依頼エラーです。",
            "2202109" to "オートチャージ設定変更時に、クレジットカード有効期限が当日までの場合のエラーです。",
            "2202110" to "オートチャージ設定変更時に、オートチャージ金額が1回ごとのチャージ限度額を超えている場合のエラーです。",
            "2202111" to "オートチャージ設定変更時に、オートチャージ金額+オートチャージ閾値がカードの残高上限額を超えている場合のエラーです。",
            "2202112" to "オートチャージ設定変更時に、要求電文で連携されたプリカが再入金実施不可の場合のエラーです。",
            "2202113" to "オートチャージ設定変更時に、プリカ有効期限が当日までの場合のエラーです。",
            "2202201" to "カードの利用停止フラグが停止中です。",
            //end card information change
        )
        private val AP2206_cardReissueError = mapOf(
            //card reissue: AP2206_カード再発行
            "2206101" to "会員情報が存在しません。",
            "2206102" to "会員情報が有効ではありません。",
            "2206103" to "カードの情報が取得できません。",
            "2206104" to "会員番号とカードの情報が紐づいていません。",
            "2206105" to "カードステータスが無効です。",
            "2206106" to "プリカ有効期限が切れています。",
            "2206107" to "VCN有効期限が切れています。",
            "2206108" to "ブランドプリカGWに照会した結果、VCNステータスが無効です。",
            "2206109" to "要求電文で連携されたカードのカード利用停止フラグが停止中です。",
            "2206110" to "要求電文で連携されたカードの再発行回数が上限回数を超えています。",
            "2206191" to "要求電文で連携されたカード情報がリアルカードのためエラーです。",
            //end card reissue
        )


        private val AP2309_cardIssuanceGiftError = mapOf(
            //Card issuance gift: AP2309_カード発行ギフト
            "2309101" to "会員情報が存在しません。",
            "2309102" to "会員情報が有効ではありません。",
            "2309103" to "受付番号登録ロック中です。",
            "2309104" to "カードスキームが存在しません。",
            "2309105" to "カードデザインが存在しません。",
            "2309106" to "カードデザインが使用中ではありません。",
            "2309107" to "カードデザインが利用可能期間外です。",
            "2309108" to "カードデザインが当該のカードスキームIDで利用不可です。",
            "2309109" to "保有上限枚数が上限を超えています。",
            "2309110" to "クレジットブランドの保有上限枚数が上限を超えています。",
            "2309111" to "受付番号が存在しません。",
            "2309112" to "受付番号が未利用ではありません。",
            "2309113" to "受付番号が利用可能期間外です。",
            "2309114" to "受取番号のギフト額が累積のチャージ可能額上限を超えています。",
            "2309115" to "受取番号のギフト額が月間のチャージ可能額上限を超えています。",
            "2309116" to "受取番号のギフト額が日間のチャージ可能額上限を超えています。",
            "2309117" to "受取番号のギフト額が1回あたりチャージ上限を超えています。",
            "2309118" to "受取番号のギフト額がチャージ残高上限を超えています。",
            //end card issuance gift
        )

        private val AP2311_cardIssuanceBalanceTotalError = mapOf(
            //Card issuance balance total: AP2311_カード発行残高合算
            "2311101" to "会員情報が存在しません。",
            "2311102" to "会員情報が有効ではありません。",
            "2311103" to "カードスキームが存在しません。",
            "2311104" to "カードデザインが存在しません。",
            "2311105" to "カードデザインが使用中ではありません。",
            "2311106" to "カードデザインが利用可能期間外です。",
            "2311107" to "カードデザインが当該のカードスキームIDで利用不可です。",
            "2311108" to "残高合算元カードが存在しません。",
            "2311109" to "会員と残高合算元カードの紐付けが無効です。",
            "2311110" to "残高合算元カードのカードステータスが無効です。",
            "2311111" to "残高合算元カードがプリカ有効期限切れです。",
            "2311112" to "残高合算元カードに同一のカードが存在します。",
            "2311113" to "残高合算元カードが外部決済予約状態です。",
            "2311114" to "保有上限枚数が上限を超えています。",
            "2311115" to "クレジットブランドの保有上限枚数が上限を超えています。",
            "2311116" to "手数料が有効ではありません。",
            "2311117" to "合算手数料額が合算後のチャージ残高を超えています。",
            "2311118" to "合算金額がチャージ可能残り金額を超えています。",
            "2311201" to "残高合算元カードの利用停止フラグが停止中です。",
            //end Card issuance balance total
        )

        private val AP2321_memberTransactionSettlement = mapOf(
            //Member transaction settlement: 外部インタフェース定義書(IFBPAP2321_会員取引精算).pdf
            "2321101" to "会員情報が存在しません。",
            "2321102" to "会員情報が有効ではありません。",
            "2321103" to "カード情報が存在しません。",
            "2321104" to "会員番号とカードの情報が紐づいていません。",
            "2321105" to "プリカ番号のカードステータスが無効です。",
            "2321106" to "プリカ番号が有効期限切れです。",
            "2321107" to "カードの利用停止フラグが停止中です。",
            "2321108" to "取引精算に失敗しました。",
            //end Member transaction settlement
        )

        private val AP2322_cardIssuanceGiftBalanceTotal = mapOf(
            //card issuance gift(with balance total): 外部インタフェース定義書(IFBPAP2322_カード発行ギフト（残高合算あり）).pdf
            "2322101" to "会員情報が存在しません。",
            "2322102" to "会員情報が有効ではありません。",
            "2322103" to "受付番号登録ロック中です。",
            "2322104" to "カードスキームが存在しません。",
            "2322105" to "カードデザインが存在しません。",
            "2322106" to "カードデザインが使用中ではありません。",
            "2322107" to "カードデザインが利用可能期間外です。",
            "2322108" to "カードデザインが当該のカードスキームIDで利用不可です。",
            "2322109" to "受付番号が存在しません。",
            "2322110" to "受付番号が未利用ではありません。",
            "2322111" to "受付番号が利用可能期間外です。",
            "2322112" to "残高合算元カードが存在しません。",
            "2322113" to "会員と残高合算元カードの紐付けが無効です。",
            "2322114" to "残高合算元カードのカードステータスが無効です。",
            "2322115" to "残高合算元カードがプリカ有効期限切れです。",
            "2322116" to "残高合算元カードの利用停止フラグが停止中です。",
            "2322117" to "残高合算元カードに同一のカードが存在します。",
            "2322118" to "残高合算元カードが外部決済予約状態です。",
            "2322119" to "保有上限枚数が上限を超えています。",
            "2322120" to "クレジットブランドの保有上限枚数が上限を超えています。",
            "2322121" to "手数料が有効ではありません。",
            "2322122" to "受取番号のギフト額が累積のチャージ可能額上限を超えています。",
            "2322123" to "受取番号のギフト額が月間のチャージ可能額上限を超えています。",
            "2322124" to "受取番号のギフト額が日間のチャージ可能額上限を超えています。",
            "2322125" to "受取番号のギフト額が1回あたりチャージ上限を超えています。",
            "2322126" to "受取番号のギフト額+合算額がチャージ残高上限を超えています。",
            "2322301" to "合算手数料額が残高合算元カードの合算金額の合計を超えています。",
            //end Member transaction settlement
        )

        private val mappingMessageTypeWithError = mapOf(
            "LoginReq" to AP1101_loginErrors,
//            "Logout" to ,//don't check this one
            "MemberSelReq" to AP1102_memberQueryError,
            "CardListSelReq" to AP1104_cardListQueryError,
            "CardSelReq" to AP1105_cardDetailQueryError,
            "SuspendDealSelReq" to AP1106_pendingTransactionInquiryQueryError,
            "GiftNumberAuthReq" to AP1107_reciptNumberAuthenticationError,
            "MemberListSelReq" to AP1108_memberListError,
            "CardDealHisReq" to AP1202_cardUsageHistoryQueryError,
//            "FeeSelReq" to AP1401,//don't have error
            "CardDesignSelReq" to AP1501_cardDesignError,
            "MemberRegReq" to AP2101_membershipRegistrationError,
            "MemberUpdReq" to AP2102_memberInformationChangeError,
            "PasswordResetReq" to AP2104_passwordResetError,
            "PasswordUpdReq" to AP2105_passwordChangeError,
            "MobileConfirmSendReq" to AP2113_MobileConfirmSendRequestError, //check,
            "MobileConfirmReq" to AP2114_MobileConfirmRequestError,//check,
            "MailConfirmSendReq" to AP2115_MailConfirmSendReqError,//check,
            "MailConfirmReq" to AP2116_MailConfirmReqError,//check,
            "CardRelationRegReq" to AP2201_cardLinkRegistrationError,
            "CardUpdReq" to AP2202_cardInformationChangeError,
            "CardRepublishReq" to AP2206_cardReissueError,
            "IssueGiftReq" to AP2309_cardIssuanceGiftError,
            "IssueSumReq" to AP2311_cardIssuanceBalanceTotalError,
//            "IssueBankReq" to AP2317,//don't have error
//            "IssueSumBankReq" to AP2318,//don't have error
            "DealAdjustReq" to AP2321_memberTransactionSettlement,
            "IssueGiftSumReq" to AP2322_cardIssuanceGiftBalanceTotal,
//"SｍsIvrAuthCodeSendReq" to //later
//"SｍsIvrAuthReq" to  //later
//"CardCertifiNoMemReq" to  //later
//"NoticeSelReq" to  //later
        )

        fun getErrorMessage(messageType: String, resultCode: String): String {
            //check on error mappping first
            var message = mappingMessageTypeWithError[messageType]?.get(resultCode)
            //check on common if not found
            if (message.isNullOrEmpty()) {
                message = commonErrors[resultCode]
            }
            if (message.isNullOrEmpty()) {
                message = otherErrorMessage//other - 9999999
            }
            return message
        }

        fun isResultCodeError(resultCode: String): Boolean {
            return resultCode != "0000000"
        }
    }
}