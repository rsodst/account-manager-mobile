package com.modulbank.accountmanager.models.users

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserModel(
    @PrimaryKey
    val userId : String,
    @ColumnInfo(name="email")
    val email : String,
    @ColumnInfo(name="value")
    val value : String
)