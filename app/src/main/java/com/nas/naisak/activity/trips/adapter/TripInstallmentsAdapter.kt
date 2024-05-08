package com.nas.naisak.activity.trips.adapter

import TripDetailsResponseModel
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import com.nas.naisak.constants.CommonMethods


class TripInstallmentsAdapter(
    private var mContext: Context,
    private var repoetDetailArray: ArrayList<TripDetailsResponseModel.InstallmentDetail>
) :
    RecyclerView.Adapter<TripInstallmentsAdapter.MyViewHolder>() {
    lateinit var clickedurl: String

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var termname: TextView = view.findViewById(R.id.listTxtTitle)
        var status: TextView = view.findViewById(R.id.status)
        var statusLayout: RelativeLayout = view.findViewById(R.id.statusLayout)
        var tripsDateTxt: TextView = view.findViewById(R.id.tripsDateTxt)

//        var clickLinear: LinearLayout = view.findViewById(R.id.clickLinear)

    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_payment_recycler, parent, false)
        return MyViewHolder(itemView)
    }


    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val summary = repoetDetailArray[position]
        holder.termname.text =
            "Installment No." + (position + 1) + " - " + repoetDetailArray[position].amount + " QAR"
        holder.tripsDateTxt.text= CommonMethods.dateConversionddmmyyyytoddMMYYYY(repoetDetailArray[position].dueDate)
        if (repoetDetailArray[position].paidStatus == 1) {
            holder.statusLayout.visibility = View.VISIBLE
            holder.status.text = "Paid"
            holder.statusLayout.setBackgroundResource(R.drawable.rect_green)
        } else {
            holder.statusLayout.visibility = View.VISIBLE
            holder.status.text = "Pay"
            holder.statusLayout.setBackgroundResource(R.drawable.rectangle_blue_update)
        }


    }

    override fun getItemCount(): Int {
        Log.e("Size", repoetDetailArray.size.toString())
        return repoetDetailArray.size

    }
}