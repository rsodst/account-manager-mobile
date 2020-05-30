package com.modulbank.accountmanager.models.accounts

import java.math.BigDecimal

data class AccountEditModel(
    val description : String? = null,
    val limitByOperation : BigDecimal? = null,
    val currency : Int? = null
)