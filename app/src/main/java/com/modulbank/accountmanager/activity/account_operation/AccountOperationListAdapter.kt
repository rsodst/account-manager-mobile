package com.modulbank.accountmanager.activity.account_operation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.modulbank.accountmanager.models.accounts.AccountActionModel

class AccountOperationListAdapter(val list : List<AccountActionModel>) : RecyclerView.Adapter<AccountOperationViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountOperationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AccountOperationViewHolder(inflater,parent)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: AccountOperationViewHolder, position: Int) {
        val account : AccountActionModel = list[position]

        holder.bind(account)
    }
}