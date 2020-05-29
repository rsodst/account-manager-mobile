package com.modulbank.accountmanager.dagger.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
public class AppModule(val application: Application)
{
    @Provides
    @Singleton
    fun providesApplication() : Application
    {
        return application;
    }
}