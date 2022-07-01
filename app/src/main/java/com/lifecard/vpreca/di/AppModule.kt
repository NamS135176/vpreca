package com.lifecard.vpreca.di

import android.content.Context
import com.lifecard.vpreca.data.*
import com.lifecard.vpreca.data.api.AWSTextractService
import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.api.ApiServiceFactory
import com.lifecard.vpreca.data.api.GoogleVisionService
import com.lifecard.vpreca.data.source.SecureStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    @Singleton
    fun provideApiService(
        @ApplicationContext appContext: Context,
        userManager: UserManager
    ): ApiService {
        return ApiServiceFactory.createService(appContext, userManager)
    }

    @Provides
    @Singleton
    fun provideCreditCardRepository(
        apiService: ApiService,
        userManager: UserManager
    ): CreditCardRepository {
        return CreditCardRepository(apiService, userManager)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        @ApplicationContext appContext: Context,
        apiService: ApiService,
        userManager: UserManager
    ): UserRepository {
        return UserRepository(appContext, apiService, userManager)
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
        userManager: UserManager
    ): RemoteRepository {
        return RemoteRepository(apiService, userManager)
    }

    @Provides
    @Singleton
    fun provideSuspendDealRepository(
        apiService: ApiService,
        userManager: UserManager
    ): SuspendDealRepository {
        return SuspendDealRepository(apiService, userManager)
    }

    @Provides
    @Singleton
    fun provideIssueCardRepository(
        apiService: ApiService,
        userManager: UserManager
    ): IssueCardRepository {
        return IssueCardRepository(apiService, userManager)
    }

    @Provides
    @Singleton
    fun provideUserManager(
        secureStore: SecureStore
    ): UserManager {
        return UserManager(secureStore)
    }

    @Provides
    fun provideGoogleVisionService(): GoogleVisionService {
        return ApiServiceFactory.createGoogleVisionService()
    }

    @Provides
    fun provideAWSTextractService(): AWSTextractService {
        return ApiServiceFactory.createAWSTextractService()
    }
}