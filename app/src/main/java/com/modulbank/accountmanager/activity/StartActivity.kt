package com.modulbank.accountmanager.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.modulbank.accountmanager.R
import com.modulbank.accountmanager.activity.signin.SignInActivity
import com.modulbank.accountmanager.activity.signup.SignUpActivity

class StartActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.start_layout);

        findViewById<Button>(R.id.signin_button).setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            this.startActivity(intent)
        }

        findViewById<Button>(R.id.signup_button).setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            this.startActivity(intent)
        }
    }
}