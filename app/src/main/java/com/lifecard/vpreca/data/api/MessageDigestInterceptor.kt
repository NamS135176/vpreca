package com.lifecard.vpreca.data.api

import com.google.gson.Gson
import com.lifecard.vpreca.utils.bodyToString
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class MessageDigestInterceptor(private val messageDigest: MessageDigest) : Interceptor {
    val gson = Gson()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        request.bodyToString()?.let { jsonBodyString ->
            try {
                val postMessageDigest = messageDigest.sign(jsonBodyString)
                val jsonBody = JSONObject(jsonBodyString)
                val brandPrecaApi = jsonBody.get("brandPrecaApi") as JSONObject
                val head = brandPrecaApi.get("head") as JSONObject
                head.put("messageDigest", postMessageDigest)

                val newRequestBody = RequestBody.create(
                    MediaType
                        .parse("application/json"), brandPrecaApi.toString()
                )
                val newRequest = request.newBuilder().post(
                    newRequestBody
                ).build()
                chain.proceed(newRequest)
            } catch (e: Exception) {
                return chain.proceed(request)
            }
        }
        return chain.proceed(request)
    }
}