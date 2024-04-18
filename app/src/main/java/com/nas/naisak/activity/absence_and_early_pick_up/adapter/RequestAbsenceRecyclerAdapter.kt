package com.nas.naisak.activity.absence_and_early_pick_up.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

internal class RequestAbsenceRecyclerAdapter(private var studentInfoList: List<AbsenceRequestListModel>) :
    RecyclerView.Adapter<RequestAbsenceRecyclerAdapter.MyViewHolder>() {
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
        holder.listStatus.visibility = View.GONE
        val movie = studentInfoList[position]

        val fromDate = movie.from_date
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val inputDateStr = fromDate
        val date: Date = inputFormat.parse(inputDateStr)
        val outputDateStr: String = outputFormat.format(date)

        if (movie.to_date != "") {
            val toDate = movie.to_date
            val inputFormat1: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat1: DateFormat = SimpleDateFormat("dd MMM yyyy")
            val inputDateStr1 = toDate
            val date1: Date = inputFormat1.parse(inputDateStr1)
            val outputDateStr1: String = outputFormat1.format(date1)
            holder.listDate.text = outputDateStr + " - " + outputDateStr1
        } else {
            holder.listDate.text = outputDateStr
        }


    }

    override fun getItemCount(): Int {
        return studentInfoList.size
    }
}