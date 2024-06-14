package com.nas.naisak.activity.payment.payhere.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import com.nas.naisak.activity.common_model.DetailListitems
import com.nas.naisak.constants.PreferenceManager

internal class PaymentInformationListAdapter(
    private var primarydetailslist: List<DetailListitems>,
    var mContext: Context
) :
    RecyclerView.Adapter<PaymentInformationListAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mTitleTxt: TextView = view.findViewById(R.id.listTxtTitle)
        var arrowImgarab : ImageView = view.findViewById(R.id.arrowImgarab)
        var arrowImg : ImageView = view.findViewById(R.id.arrowImg)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_information_list, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = primarydetailslist[position]
        if (PreferenceManager().getLanguage(mContext).equals("ar"))
        {

            holder.arrowImg.visibility= View.GONE
            holder.arrowImgarab.visibility= View.VISIBLE



        }
        holder.mTitleTxt.text = list.title

    }
    override fun getItemCount(): Int {
        return primarydetailslist.size
    }
}