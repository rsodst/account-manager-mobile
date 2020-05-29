package com.modulbank.accountmanager.dagger.components

import com.modulbank.accountmanager.activity.signin.SignInActivity
import com.modulbank.accountmanager.activity.signup.SignUpActivity
import com.modulbank.accountmanager.dagger.modules.ApplicationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApiComponent {
    fun inject(signinActivity : SignInActivity)
    fun inject(signupActivity : SignUpActivity)
}