package com.modulbank.accountmanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.modulbank.accountmanager.models.profile.ProfileModel
import com.modulbank.accountmanager.models.users.UserDao
import com.modulbank.accountmanager.models.users.UserModel

@Database(entities = [UserModel::class], version = 1)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun userDao() : UserDao

    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context : Context)  : AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.inMemoryDatabaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java)
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}