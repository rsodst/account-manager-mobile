package com.modulbank.users

data class SignUpModel(
    override val Email: String,
    override val Password: String
) : ICredentialModel
