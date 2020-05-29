package com.modulbank.accountmanager.activity.signup

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.modulbank.accountmanager.R
import com.modulbank.accountmanager.activity.extensions.afterTextChanged
import com.modulbank.accountmanager.activity.signin.SignUpViewModel
import com.modulbank.accountmanager.api.IUserApi
import com.modulbank.accountmanager.dagger.components.DaggerApiComponent
import com.modulbank.accountmanager.databinding.SigninActivityBinding
import com.modulbank.accountmanager.databinding.SignupActivityBinding
import javax.inject.Inject


class SignUpActivity : AppCompatActivity()
{

    private val model: SignUpViewModel by viewModels()
    private lateinit var binding : SignupActivityBinding
    @Inject lateinit var userApi : IUserApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerApiComponent.create().inject(this)
        binding = SignupActivityBinding.inflate(layoutInflater)

        supportActionBar?.hide()

        setContentView(binding.root)

        binding.signupLoad.visibility = View.GONE

        model.state.observe(this, Observer {
            binding.signupEmail.isEnabled = !it.isLoading
            binding.signupPassword.isEnabled = !it.isLoading
            binding.signup.isEnabled = !it.isLoading

            if (it.isLoading){
                binding.signupLoad.visibility = View.VISIBLE
            }else{
                binding.signupLoad.visibility = View.GONE
            }

            if (it.isResponseError != null){
                binding.signupErrors.setText(it.isResponseError)
            }else{
                binding.signupErrors.setText("")
            }
        })

        model.passwordValidationError.observe(this, Observer {
            binding.signupPassword.error = it
        })

        model.passwordConfirmationValidationError.observe(this, Observer {
            binding.signupPasswordConfirmation.error = it
        })

        model.emailValidationError.observe(this, Observer {
            binding.signupEmail.error = it
        })

        binding.signupEmail.afterTextChanged {
            model.changeEmail(binding.signupEmail.text.toString())
        }

        binding.signupPassword.afterTextChanged {
            model.changePassword(binding.signupPassword.text.toString())
        }

        binding.signupPasswordConfirmation.afterTextChanged {
            model.changePasswordConfirmation(binding.signupPasswordConfirmation.text.toString())
        }

        binding.signup.setOnClickListener({
            model.signUp(userApi)
        })


    }
}