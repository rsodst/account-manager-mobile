package com.modulbank.users

data class SignInModel(
    override val Email: String,
    override val Password: String
) : ICredentialModel