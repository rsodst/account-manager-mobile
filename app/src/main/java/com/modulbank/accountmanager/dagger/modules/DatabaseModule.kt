package com.modulbank.accountmanager.dagger.modules

import android.app.Application
import com.modulbank.accountmanager.database.AppDatabase
import com.modulbank.accountmanager.models.users.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(val application : Application) {

    @Singleton
    @Provides
    fun provideDatabase() : AppDatabase {
        return AppDatabase.getDatabase(application)
    }

    @Singleton
    @Provides
    fun provideUserDao() : UserDao {
        return AppDatabase.getDatabase(application).userDao()
    }
}