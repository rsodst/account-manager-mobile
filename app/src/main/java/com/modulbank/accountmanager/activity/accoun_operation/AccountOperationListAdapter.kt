package com.modulbank.accountmanager.activity.accoun_operation

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.modulbank.accountmanager.R
import com.modulbank.accountmanager.activity.account_editor.AccountEditorActivity
import com.modulbank.accountmanager.models.accounts.AccountActionModel
import com.modulbank.accountmanager.models.accounts.AccountModel

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