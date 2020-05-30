package com.modulbank.accountmanager.models.users

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM usermodel LIMIT 1")
    fun Get() : UserModel

    @Insert
    fun Insert(model : UserModel)

    @Update
    fun Update(model : UserModel)

    @Query("DELETE FROM usermodel")
    fun Delete()
}