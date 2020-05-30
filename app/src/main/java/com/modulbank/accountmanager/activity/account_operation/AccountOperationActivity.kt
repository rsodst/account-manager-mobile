package com.modulbank.accountmanager.activity.account_operation

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.modulbank.accountmanager.R
import com.modulbank.accountmanager.activity.accounts.AccountsActivity
import com.modulbank.accountmanager.api.IAccountApi
import com.modulbank.accountmanager.dagger.components.DaggerAppComponent
import com.modulbank.accountmanager.dagger.modules.DatabaseModule
import com.modulbank.accountmanager.databinding.AccountOperationLayoutBinding
import com.modulbank.accountmanager.models.users.UserDao
import javax.inject.Inject

class AccountOperationActivity : AppCompatActivity() {

    @Inject lateinit var userDao : UserDao
    @Inject lateinit var accountApi : IAccountApi
    private lateinit var binding : AccountOperationLayoutBinding
    val viewModel : AccountOperationViewModel by viewModels<AccountOperationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AccountOperationLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)

        DaggerAppComponent.builder()
            .databaseModule(DatabaseModule(application))
            .build().inject(this)

        binding.accountOperationLoader.visibility = View.GONE

        binding.accountOperationRecycler.apply{
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            adapter = AccountOperationListAdapter(listOf())
        }

        viewModel.actions.observe(this, Observer {
            binding.accountOperationRecycler.apply{
                hasFixedSize()
                layoutManager = LinearLayoutManager(context)
                adapter = AccountOperationListAdapter(it)
            }
        })

        viewModel.state.observe(this, Observer {
            if (it.isLoading){
                binding.accountOperationLoader.visibility = View.VISIBLE
            }else{
                binding.accountOperationLoader.visibility = View.GONE
            }

            if (it.isResponseError != null){
                binding.accountOperationErrors.setText(it.isResponseError)
            }else{
                binding.accountOperationErrors.setText("")
            }
        })

        val bundle = intent.extras
        val accountId = bundle?.getString("accountId")
        val currency = bundle?.getInt("currency")

        binding.accountOperationRefill.setOnClickListener({
            val refillPopupView = layoutInflater.inflate(R.layout.refill_popup_layout, null)
            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true
            val popupWindow = PopupWindow(refillPopupView, width,height,focusable)
            popupWindow.showAtLocation(refillPopupView, Gravity.CENTER, 0,0)

            val amount = refillPopupView.findViewById<EditText>(R.id.refill_amount)

            refillPopupView.findViewById<Button>(R.id.refill_button).setOnClickListener{

                if (amount.text.isNullOrEmpty()){
                    amount.error = "Amount is required"
                }else {
                    viewModel.refill(userDao, accountApi, accountId!!, amount.text.toString().toBigDecimal())
                    popupWindow.dismiss();
                }
            }
        })

        binding.accountOperationTransfer.setOnClickListener({
            val refillPopupView = layoutInflater.inflate(R.layout.transfer_popup_layout, null)
            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true
            val popupWindow = PopupWindow(refillPopupView, width,height,focusable)
            popupWindow.showAtLocation(refillPopupView, Gravity.CENTER, 0,0)

            val amount = refillPopupView.findViewById<EditText>(R.id.transfer_amount)
            val number = refillPopupView.findViewById<EditText>(R.id.transfer_number)

            refillPopupView.findViewById<Button>(R.id.transfer_button).setOnClickListener{

                if (amount.text.isNullOrEmpty()){
                    amount.error = "Amount is required"
                }else {
                    viewModel.transfer(userDao, accountApi, accountId!!, amount.text.toString().toBigDecimal(), number.text.toString().toLong(), currency!!)
                    popupWindow.dismiss();
                }
            }
        })

        viewModel.loadAccountActionsList(userDao, accountApi, accountId!!)
    }

    override fun onBackPressed() {
        val intent = Intent(this, AccountsActivity::class.java)
        this.startActivity(intent)
        finish()
    }
}