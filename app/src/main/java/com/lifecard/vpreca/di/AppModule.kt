package com.lifecard.vpreca.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.data.source.SecureStore
import com.lifecard.vpreca.utils.Constanst
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
    fun provideCreditCardRepository(): CreditCardRepository {
        return CreditCardRepository()
    }

    @Provides
    @Singleton
    fun provideUserRepository(secureStore: SecureStore): UserRepository {
        return UserRepository(secureStore)
    }

    @Provides
    @Singleton
    fun provideSecureStore(@ApplicationContext appContext: Context): SecureStore {
        return SecureStore(appContext)
    }

}