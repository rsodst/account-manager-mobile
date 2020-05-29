package com.modulbank.accountmanager.models.users

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Query("SELECT * FROM usermodel LIMIT 1")
    fun Get() : UserModel

    @Insert
    fun Insert(model : UserModel)

    @Update
    fun Update(model : UserModel)
}