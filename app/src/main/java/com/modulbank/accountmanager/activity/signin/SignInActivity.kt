package com.modulbank.accountmanager.activity.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.modulbank.accountmanager.R
import com.modulbank.accountmanager.activity.accounts.AccountsActivity
import com.modulbank.accountmanager.activity.extensions.afterTextChanged
import com.modulbank.accountmanager.activity.profile.ProfileEditorActivity
import com.modulbank.accountmanager.api.IUserApi
import com.modulbank.accountmanager.dagger.components.DaggerAppComponent
import com.modulbank.accountmanager.dagger.modules.AppModule
import com.modulbank.accountmanager.dagger.modules.DatabaseModule
import com.modulbank.accountmanager.databinding.SigninActivityBinding
import com.modulbank.accountmanager.models.users.UserDao
import javax.inject.Inject


class SignInActivity : AppCompatActivity()
{

    private val model: SignInViewModel by viewModels()
    private lateinit var binding : SigninActivityBinding
    @Inject lateinit var userApi : IUserApi
    @Inject lateinit var userDao : UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerAppComponent.builder()
            .databaseModule(DatabaseModule(application))
            .build().inject(this)

        binding = SigninActivityBinding.inflate(layoutInflater)

        supportActionBar?.hide()

        setContentView(binding.root)

        binding.signinLoad.visibility = View.GONE

        model.openAccountActivity.observe(this, Observer {
            val intent = Intent(this, AccountsActivity::class.java)
            this.startActivity(intent)
            finish()
        })

        model.state.observe(this, Observer {
            binding.signinEmail.isEnabled = !it.isLoading
            binding.signinPassword.isEnabled = !it.isLoading
            binding.signin.isEnabled = !it.isLoading

            if (it.isLoading){
                binding.signinLoad.visibility = View.VISIBLE
            }else{
                binding.signinLoad.visibility = View.GONE
            }

            if (it.isResponseError != null){
                binding.signinErrors.setText(it.isResponseError)
            }else{
                binding.signinErrors.setText("")
            }
        })

        model.passwordValidationError.observe(this, Observer {
            binding.signinPassword.error = it
        })

        model.emailValidationError.observe(this, Observer {
            binding.signinEmail.error = it
        })

        binding.signinEmail.afterTextChanged {
            model.changeEmail(binding.signinEmail.text.toString())
        }

        binding.signinPassword.afterTextChanged {
            model.changePassword(binding.signinPassword.text.toString())
        }

        binding.signin.setOnClickListener({
            model.signIn(userApi, userDao)
        })


    }
}