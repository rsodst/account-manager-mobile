package com.modulbank.users

import java.util.*

data class UserModel(
    val Email : String,
    val UserId : UUID,
    val AccessToken : String
)
