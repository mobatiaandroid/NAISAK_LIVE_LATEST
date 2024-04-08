package com.nas.naisak.activity.trips.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nas.naisak.R
import com.nas.naisak.activity.trips.TripListingActivity
import com.nas.naisak.activity.trips.model.TripCategoriesResponseModel
import com.nas.naisak.constants.CommonMethods


class TripsCategoryAdapter(mContext: Context, mListViewArray: ArrayList<TripCategoriesResponseModel.TripCategory>)
    : RecyclerView.Adapter<TripsCategoryAdapter.MyViewHolder>() {
    private val context: Context = mContext
    private val tripCategoryArrayList: java.util.ArrayList<TripCategoriesResponseModel.TripCategory> = mListViewArray


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var categoryItemImageView: ImageView
        var categoryNameTextView: TextView
        init {
            categoryItemImageView = view.findViewById<View>(R.id.imageView) as ImageView
            categoryNameTextView = view.findViewById<View>(R.id.textView) as TextView

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_trip_category_item, parent, false)

        return MyViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.categoryNameTextView.setText(tripCategoryArrayList.get(position).tripCategoryEn)
        if (!tripCategoryArrayList.get(position).image.equals("")) {
            Glide.with(context).load(CommonMethods.replace(tripCategoryArrayList.get(position).image))
                .fitCenter().placeholder(R.drawable.default_banner)
                .into(holder.categoryItemImageView)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, TripListingActivity::class.java)
            intent.putExtra("trip_category_id", tripCategoryArrayList[position].id)
            intent.putExtra("trip_category_name", tripCategoryArrayList[position].tripCategoryEn)
            context.startActivity(intent)
            //                showIntentionPopUp();
        }
    }

    override fun getItemCount(): Int {
        return tripCategoryArrayList.size
    }

}