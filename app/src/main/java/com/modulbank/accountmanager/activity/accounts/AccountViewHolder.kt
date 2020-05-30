package com.modulbank.accountmanager.activity.accounts

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.modulbank.accountmanager.R
import com.modulbank.accountmanager.models.accounts.AccountModel


class AccountViewHolder(inflater : LayoutInflater, parent : ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(
    R.layout.account_list_view_layout, parent, false)){

    private var creationDateView : TextView? = null
    private var numberView :  TextView? = null
    private var balanceView :  TextView? = null
    var editButtonView : Button? = null

    public var id : String? = null

    init{
        creationDateView = itemView.findViewById(R.id.card_account_creationdate)
        numberView = itemView.findViewById(R.id.card_account_number)
        balanceView = itemView.findViewById(R.id.card_account_balance)
        editButtonView = itemView.findViewById(R.id.account_editor)
    }

    fun bind(accountModel : AccountModel){
        id = accountModel.id
        numberView?.setText("№ ${accountModel.number}")
        balanceView?.setText("Balance: ${accountModel.balance}")

        val date = accountModel.creationDate?.split("T")?.get(0)

        creationDateView?.setText("${date}")
    }
}