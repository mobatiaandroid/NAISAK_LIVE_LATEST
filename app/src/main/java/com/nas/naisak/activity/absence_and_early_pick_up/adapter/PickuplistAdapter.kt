package com.nas.naisak.activity.absence_and_early_pick_up.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

internal class PickuplistAdapter(
    private var Context: Context,
    var pickup_list: ArrayList<EarlyPickupListArray>
) :
    RecyclerView.Adapter<PickuplistAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var listDate: TextView = view.findViewById(R.id.listDate)
        var listStatus: TextView = view.findViewById(R.id.listStatus)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_absence_leave_recycelr, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = pickup_list[position]

        val fromDate = list.pickup_date
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val inputDateStr = fromDate
        val date: Date = inputFormat.parse(inputDateStr)
        val outputDateStr: String = outputFormat.format(date)
        holder.listDate.text = outputDateStr

        var status = list.status
        if (status == 1) {
            holder.listStatus.visibility = View.VISIBLE
            holder.listStatus.text = "PENDING"
            //label pending
        } else if (status == 2) {
            holder.listStatus.visibility = View.VISIBLE
            holder.listStatus.text = "APPROVED"
            //label approved
        } else {
            holder.listStatus.visibility = View.VISIBLE
            holder.listStatus.text = "REJECTED"
            //label rejected
        }

    }

    override fun getItemCount(): Int {
        return pickup_list.size
    }
}