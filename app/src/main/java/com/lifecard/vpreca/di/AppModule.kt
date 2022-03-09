package com.lifecard.vpreca.di

import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    @Singleton
    fun provideCreditCardRepository(): CreditCardRepository{
        return CreditCardRepository()
    }

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return UserRepository()
    }

}