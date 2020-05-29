package com.modulbank.accountmanager.dagger.modules

import com.modulbank.accountmanager.api.IUserApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Singleton
    @Provides
    public fun provideApi() : IUserApi { return IUserApi.create()}
}