package com.modulbank.accountmanager.models.users

import com.modulbank.accountmanager.models.users.ISignInModel

class SignUpModel : ISignInModel {
    override var email : String? = null
    override var password : String? = null
    var paswordConfirmation : String? = null
}