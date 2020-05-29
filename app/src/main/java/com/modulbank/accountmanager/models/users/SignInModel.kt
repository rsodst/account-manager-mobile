package com.modulbank.accountmanager.models.users

import com.modulbank.accountmanager.models.users.ISignInModel

class SignInModel : ISignInModel
{
    override var email : String? = null
    override var password : String? = null
}

