package com.modulbank.accountmanager.models.accounts

enum class ActionType() {
    Create,
    Close,
    Edit,
    Refil,
    Transfer;

    companion object {
        fun getByValue(value: Int) = ActionType.values().first {it.ordinal == value}
    }
}