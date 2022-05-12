package com.lifecard.vpreca.data.source

import android.content.Context
import android.os.Build
import android.security.keystore.KeyProperties
import java.io.IOException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import kotlin.Error

object EncryptionUtils {
    fun encrypt(context: Context, token: String?): String? {
        return getSecurityKey(context)?.encrypt(token)
    }

    fun decrypt(context: Context, token: String?): String? {
        return getSecurityKey(context)?.decrypt(token)
    }

    private fun getSecurityKey(context: Context): SecurityKey? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyStore?.let { EncryptionKeyGenerator.generateSecretKey(it) }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            keyStore?.let { EncryptionKeyGenerator.generateKeyPairPreM(context, it) }
        } else {
            EncryptionKeyGenerator.generateSecretKeyPre18(context)
        }
    }

    private val keyStore: KeyStore?
        get() {
            var keyStore: KeyStore? = null
            try {
                keyStore = KeyStore.getInstance(EncryptionKeyGenerator.ANDROID_KEY_STORE)
                keyStore.load(null)
            } catch (e: KeyStoreException) {
                print("EncryptionUtils keyStore has error ${e}")
            } catch (e: CertificateException) {
                print("EncryptionUtils keyStore has error ${e}")
            } catch (e: NoSuchAlgorithmException) {
                print("EncryptionUtils keyStore has error ${e}")
            } catch (e: IOException) {
                print("EncryptionUtils keyStore has error ${e}")
            }
            return keyStore
        }

    /**
     * Should be call when user logout
     */
    fun clear() {
        val keyStore = keyStore
        try {
            if (keyStore!!.containsAlias(EncryptionKeyGenerator.KEY_ALIAS)) {
                keyStore.deleteEntry(EncryptionKeyGenerator.KEY_ALIAS)
            }
        } catch (e: KeyStoreException) {
            print("SecurityKey... clear has exception ${e}")
        }
    }
}