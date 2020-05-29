package com.modulbank.accountmanager.activity.accounts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.modulbank.accountmanager.R
import com.modulbank.accountmanager.activity.profile.ProfileEditorActivity
import com.modulbank.accountmanager.dagger.components.DaggerAppComponent
import com.modulbank.accountmanager.dagger.modules.DatabaseModule
import com.modulbank.accountmanager.models.users.UserDao
import javax.inject.Inject

class AccountsActivity : AppCompatActivity() {

    @Inject lateinit var userDao : UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.private_layout)

        DaggerAppComponent.builder()
            .databaseModule(DatabaseModule(application))
            .build().inject(this)

        val btn = findViewById<Button>(R.id.button)

        btn.setOnClickListener({
            val intent = Intent(this, ProfileEditorActivity::class.java)
            this.startActivity(intent)
        })
    }

    override fun onBackPressed() {

    }
}