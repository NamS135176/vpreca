package com.lifecard.vpreca.di

import android.content.Context
import android.content.Intent
import com.lifecard.vpreca.LoginActivity
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.RemoteRepository
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.api.ApiServiceFactory
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
    fun provideApiService(
        @ApplicationContext appContext: Context,
        secureStore: SecureStore
    ): ApiService {
        return ApiServiceFactory.createService(appContext, secureStore)
    }

    @Provides
    @Singleton
    fun provideCreditCardRepository(
        apiService: ApiService,
        userRepository: UserRepository
    ): CreditCardRepository {
        return CreditCardRepository(apiService, userRepository)
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

    @Provides
    @Singleton
    fun provideRemoteRepository(
        apiService: ApiService,
        userRepository: UserRepository
    ): RemoteRepository {
        return RemoteRepository(apiService, userRepository)
    }

}