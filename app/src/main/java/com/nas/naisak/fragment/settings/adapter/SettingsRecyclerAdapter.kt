package com.nas.naisak.fragment.settings.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import com.nas.naisak.constants.PreferenceManager

internal class SettingsRecyclerAdapter  (private var notificationList: List<String>,private var context: Context) :
    RecyclerView.Adapter<SettingsRecyclerAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var listTxtTitle: TextView = view.findViewById(R.id.listTxtTitle)
        var txtUser: TextView = view.findViewById(R.id.txtUser)

    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_settings, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (PreferenceManager.getUserLogged(context).equals("1"))
        {

            if (position==6)
            {

                holder.listTxtTitle.setText(notificationList.get(position).toString())
                holder.txtUser.visibility=View.VISIBLE
                holder.txtUser.text="( "+PreferenceManager.getUserEmail(context)+" )"
            }
            else{
                holder.listTxtTitle.text = notificationList.get(position).toString()
                holder.txtUser.visibility=View.GONE
            }
        }
        else{
            if (position==4)
            {
                holder.listTxtTitle.text = notificationList.get(position).toString()
                holder.txtUser.visibility=View.VISIBLE
                holder.txtUser.text="( Guest )"
            }
            else
            {
                holder.listTxtTitle.text = notificationList.get(position).toString()
                holder.txtUser.visibility=View.GONE
            }
        }

    }
    override fun getItemCount(): Int {
        return notificationList.size
    }
}