package com.modulbank.accountmanager.models.accounts

import java.math.BigDecimal

data class TransferModel(
    val destinationAccountNumber : Long?=null,
    val amount : BigDecimal?=null,
    val currency : Int?=null
)