package com.modulbank.accountmanager.dagger.modules

import androidx.room.Room
import com.modulbank.accountmanager.api.IProfileApi
import com.modulbank.accountmanager.api.IUserApi
import com.modulbank.accountmanager.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApiModule {

    @Singleton
    @Provides
    public fun provideUserApi() : IUserApi { return IUserApi.create()}

    @Singleton
    @Provides
    public fun provideProfileApi() : IProfileApi { return IProfileApi.create()}
}