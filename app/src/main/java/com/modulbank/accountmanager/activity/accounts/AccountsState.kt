package com.modulbank.accountmanager.activity.accounts

data class AccountsState(
    var isLoading : Boolean = false,
    var isResponseError : String? = null
)