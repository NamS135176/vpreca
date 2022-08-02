package com.lifecard.vpreca.di

import android.content.Context
import com.lifecard.vpreca.data.*
import com.lifecard.vpreca.data.api.AWSTextractService
import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.api.ApiServiceFactory
import com.lifecard.vpreca.data.api.GoogleVisionService
import com.lifecard.vpreca.data.source.SecureStore
import com.lifecard.vpreca.utils.DeviceIDHelper
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
        userManager: UserManager,
        deviceID: DeviceID,
    ): CreditCardRepository {
        return CreditCardRepository(apiService, userManager, deviceID)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        @ApplicationContext appContext: Context,
        apiService: ApiService,
        userManager: UserManager,
        deviceID: DeviceID,
    ): UserRepository {
        return UserRepository(appContext, apiService, userManager, deviceID)
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
        userManager: UserManager,
        deviceID: DeviceID
    ): RemoteRepository {
        return RemoteRepository(apiService, userManager, deviceID)
    }

    @Provides
    @Singleton
    fun provideSuspendDealRepository(
        apiService: ApiService,
        userManager: UserManager,
        deviceID: DeviceID
    ): SuspendDealRepository {
        return SuspendDealRepository(apiService, userManager, deviceID)
    }

    @Provides
    @Singleton
    fun provideIssueCardRepository(
        apiService: ApiService,
        userManager: UserManager,
        deviceID: DeviceID
    ): IssueCardRepository {
        return IssueCardRepository(apiService, userManager, deviceID)
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

    @Provides
    fun provideDeviceID(@ApplicationContext appContext: Context): DeviceID {
        return DeviceID(deviceId = DeviceIDHelper.getDeviceId(appContext))
    }
}