package com.modulbank.accountmanager.activity.signin

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.modulbank.accountmanager.R
import com.modulbank.accountmanager.activity.extensions.afterTextChanged
import com.modulbank.accountmanager.api.IUserApi
import com.modulbank.accountmanager.dagger.components.DaggerApiComponent
import com.modulbank.accountmanager.databinding.SigninActivityBinding
import javax.inject.Inject


class SignInActivity : AppCompatActivity()
{

    private val model: SignInViewModel by viewModels()
    private lateinit var binding : SigninActivityBinding
    @Inject lateinit var userApi : IUserApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerApiComponent.create().inject(this)
        binding = SigninActivityBinding.inflate(layoutInflater)

        supportActionBar?.hide()

        setContentView(binding.root)

        binding.signinLoad.visibility = View.GONE

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
            model.login(userApi)
        })


    }
}