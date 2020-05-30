package com.modulbank.accountmanager.activity.accounts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.modulbank.accountmanager.models.accounts.AccountModel

class AccountListAdapter(val list : List<AccountModel>) : RecyclerView.Adapter<AccountViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AccountViewHolder(inflater,parent)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account : AccountModel = list[position]
        holder.bind(account)
    }
}