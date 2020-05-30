package com.modulbank.accountmanager.activity.accoun_operation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.modulbank.accountmanager.R
import com.modulbank.accountmanager.models.accounts.AccountActionModel
import com.modulbank.accountmanager.models.accounts.AccountModel
import com.modulbank.accountmanager.models.accounts.ActionType


class AccountOperationViewHolder(inflater : LayoutInflater, parent : ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(
    R.layout.account_operation_list_view_layout, parent, false)){

    private var creationDate : TextView? = null
    private var actionType :  TextView? = null

    init{
        creationDate = itemView.findViewById(R.id.account_operation_action_creationdate)
        actionType = itemView.findViewById(R.id.account_operation_action_type)
    }

    fun bind(accountAction : AccountActionModel){
        actionType?.setText("${ActionType.getByValue(accountAction.type!!)}")

        val date = accountAction.creationDate?.split("T")?.get(0)

        creationDate?.setText("${date}")
    }
}