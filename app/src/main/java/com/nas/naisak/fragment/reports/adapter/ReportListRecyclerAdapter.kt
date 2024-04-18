package com.nas.naisak.fragment.reports.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R


class ReportListRecyclerAdapter(
    private var mContext: Context,
    private var reportslist: ArrayList<ReportDetailModel>
) :
    RecyclerView.Adapter<ReportListRecyclerAdapter.MyViewHolder>() {

    lateinit var clickedurl: String
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var detailArray: ArrayList<ReportListDetailModel>

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.accYr)
        var reportRecycler: RecyclerView = view.findViewById(R.id.recycler_view_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_report_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return reportslist.size

    }

    override fun onBindViewHolder(holder: ReportListRecyclerAdapter.MyViewHolder, position: Int) {
        holder.title.text = reportslist[position].academic_year
        linearLayoutManager = LinearLayoutManager(mContext)
        holder.reportRecycler.layoutManager = linearLayoutManager
        holder.reportRecycler.itemAnimator = DefaultItemAnimator()
        detailArray = ArrayList()
        if (reportslist[position].data.size > 0) {
            detailArray = reportslist[position].data
            val reportCycleAdapter = ReportDetailAdapter(mContext, detailArray)
            holder.reportRecycler.adapter = reportCycleAdapter
        }

    }
}