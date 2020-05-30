package com.modulbank.accountmanager.activity.accounts

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.modulbank.accountmanager.R
import com.modulbank.accountmanager.models.accounts.AccountModel
import java.util.*

class AccountViewHolder(inflater : LayoutInflater, parent : ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(
    R.layout.account_list_view_layout, parent, false)){

    private var creationDateView : TextView? = null
    private var numberView :  TextView? = null
    private var balanceView :  TextView? = null

    init{
        creationDateView = itemView.findViewById(R.id.card_account_creationdate)
        numberView = itemView.findViewById(R.id.card_account_number)
        balanceView = itemView.findViewById(R.id.card_account_balance)
    }

    fun bind(accountModel : AccountModel){
        numberView?.setText("№ ${accountModel.number}")
        balanceView?.setText("Balance: ${accountModel.balance}")
        creationDateView?.setText("${accountModel.creationDate}")
    }
}