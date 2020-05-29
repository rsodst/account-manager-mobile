package com.modulbank.accountmanager.models

class SignUpModel : ISignInModel {
    override var email : String? = null
    override var password : String? = null
    var paswordConfirmation : String? = null
}