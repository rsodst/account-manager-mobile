package com.modulbank.accountmanager.models.accounts

import java.util.*

data class AccountModel(
    val id: String? = null,
    val number : String? = null,
    val isDeleted : Boolean? = null,
    val balance : Float? = null,
    val creationDate : String? = null,
    val accountDetail : AccountDetail
)