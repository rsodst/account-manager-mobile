package com.modulbank.accountmanager.dagger.components

import com.modulbank.accountmanager.activity.accounts.AccountsActivity
import com.modulbank.accountmanager.activity.profile.ProfileEditorActivity
import com.modulbank.accountmanager.activity.settings.SettingsActivity
import com.modulbank.accountmanager.activity.signin.SignInActivity
import com.modulbank.accountmanager.activity.signup.SignUpActivity
import com.modulbank.accountmanager.dagger.modules.ApiModule
import com.modulbank.accountmanager.dagger.modules.AppModule
import com.modulbank.accountmanager.dagger.modules.DatabaseModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, AppModule::class, DatabaseModule::class])
interface AppComponent {

    fun inject(signinActivity : SignInActivity)
    fun inject(signupActivity : SignUpActivity)
    fun inject(accountsActivity: AccountsActivity)
    fun inject(profileEditorActivity: ProfileEditorActivity)
    fun inject(settingsActivity: SettingsActivity)
}