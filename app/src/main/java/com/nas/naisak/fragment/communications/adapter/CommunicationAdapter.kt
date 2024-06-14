package com.nas.naisak.fragment.communications.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.view.marginRight
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.fragment.communications.model.CommunicationListModel
import com.nas.naisak.fragment.communications.model.CommunicationListUseModel
import com.nas.naisak.fragment.parentsessentials.Model.ParentsEssentialListModel
import com.nas.naisak.fragment.parentsessentials.adapter.ParentsEssentialListAdapter

internal class CommunicationAdapter (private var mContext: Context, private var parentsEssentialArrayList: List<CommunicationListUseModel>) :
    RecyclerView.Adapter<CommunicationAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.listTxtTitle)
        var arrowImgarab : ImageView = view.findViewById(R.id.arrowImgarab)
        var arrowImg : ImageView = view.findViewById(R.id.arrowImg)
        var relativeheader : RelativeLayout = view.findViewById(R.id.relativeheader)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_parents_essential, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = parentsEssentialArrayList[position]
        if (PreferenceManager().getLanguage(mContext).equals("ar"))
        {

            holder.arrowImg.visibility= View.GONE
            holder.arrowImgarab.visibility= View.VISIBLE
            val layoutParams = holder.relativeheader.layoutParams as ViewGroup.MarginLayoutParams
            val marginrightInPixels = 16 // Adjust this value according to your requirement
            layoutParams.rightMargin = marginrightInPixels
            holder. relativeheader.layoutParams = layoutParams
            val layoutParamsleft = holder.relativeheader.layoutParams as ViewGroup.MarginLayoutParams
            val marginLeftInPixels = 0 // Adjust this value according to your requirement
            layoutParamsleft.leftMargin = marginLeftInPixels
            holder. relativeheader.layoutParams = layoutParamsleft



        }
        holder.title.text = movie.title

    }
    override fun getItemCount(): Int {

        return parentsEssentialArrayList.size

    }
}