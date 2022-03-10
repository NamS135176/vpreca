package com.lifecard.vpreca.di

import android.content.Context
import android.content.Intent
import com.lifecard.vpreca.LoginActivity
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.source.SecureStore
import com.lifecard.vpreca.utils.Constanst
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    @Singleton
    fun provideApiService(@ApplicationContext appContext: Context): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(Interceptor { chain ->
                val response = chain.proceed(chain.request())
                if (response.code() == 401) {
                    println("Retrofit Interceptor... handle header 401")
                    val intent = Intent(appContext, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    appContext.startActivity(intent)
                }
                response
            })
            .build()
        val retrofit = Retrofit.Builder().run {
            client(client)
            baseUrl(Constanst.API_BASE_URL)
            addConverterFactory(GsonConverterFactory.create())
            build()
        }
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCreditCardRepository(apiService: ApiService): CreditCardRepository {
        return CreditCardRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideUserRepository(secureStore: SecureStore, apiService: ApiService): UserRepository {
        return UserRepository(secureStore, apiService)
    }

    @Provides
    @Singleton
    fun provideSecureStore(@ApplicationContext appContext: Context): SecureStore {
        return SecureStore(appContext)
    }

}