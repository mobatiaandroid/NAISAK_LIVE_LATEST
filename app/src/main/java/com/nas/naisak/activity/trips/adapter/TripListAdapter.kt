package com.nas.naisak.activity.trips.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nas.naisak.R
import com.nas.naisak.activity.trips.TripDetailsActivity
import com.nas.naisak.activity.trips.model.TripListResponseModel
import com.nas.naisak.constants.CommonMethods


class TripListAdapter(
    mContext: Context, mListViewArray: ArrayList<TripListResponseModel.Trip>
) : RecyclerView.Adapter<TripListAdapter.MyViewHolder>() {
    private val context: Context = mContext
    private val tripList: ArrayList<TripListResponseModel.Trip> = mListViewArray


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tripImageView: ImageView
        var tripNameTextView: TextView
        var tripPriceTextView: TextView
        var tripDateTextView: TextView
        var tripBookButton: Button

        init {
            tripImageView = view.findViewById<View>(R.id.tripImageView) as ImageView
            tripNameTextView = view.findViewById<View>(R.id.tripNameTextView) as TextView
            tripPriceTextView = view.findViewById<View>(R.id.priceTextView) as TextView
            tripDateTextView = view.findViewById<View>(R.id.estimateDateTextView) as TextView
            tripBookButton = view.findViewById(R.id.bookNowButton)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_trip_list_item, parent, false)

        return MyViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tripNameTextView.setText(tripList[position].tripNameEn)
        if (tripList[position].tripImage.size > 0) {
            if (!tripList[position].tripImage[0].equals("")) {
                Glide.with(context).load(CommonMethods.replace(tripList[position].tripImage.get(0)))
                    .fitCenter().placeholder(R.drawable.default_banner).into(holder.tripImageView)
            }
        }
        if (tripList[position].tripStatus === 0) {
            holder.tripBookButton.text = "Book your Trip"
        } else if (tripList[position].tripStatus === 1) {
            holder.tripBookButton.text = "Pending"
        } else if (tripList[position].tripStatus === 2) {
            holder.tripBookButton.text = "Rejected"
        } else if (tripList[position].tripStatus === 3) {
            holder.tripBookButton.text = "Continue"
        } else if (tripList[position].tripStatus === 4) {
            holder.tripBookButton.text = "Cancelled"
        } else if (tripList[position].tripStatus === 5) {
            holder.tripBookButton.text = "Pay now"
        } else if (tripList[position].tripStatus === 6) {
            holder.tripBookButton.text = "Pay now"
        } else if (tripList[position].tripStatus === 7) {
            holder.tripBookButton.text = "View Invoice"
        } else {
            holder.tripBookButton.text = "Not Available"
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, TripDetailsActivity::class.java)
            intent.putExtra("tripID", tripList[position].id)
            intent.putExtra("tripName", tripList[position].tripNameEn)
            context.startActivity(intent)
//            showPaymentsPopUp(context)

            //                showIntentionPopUp();
        }
        holder.tripBookButton.setOnClickListener {
            val intent = Intent(context, TripDetailsActivity::class.java)
            intent.putExtra("tripID", tripList[position].id)
            intent.putExtra("tripName", tripList[position].tripNameEn)
            context.startActivity(intent)
//            showPaymentsPopUp(context)
            //                showIntentionPopUp();
        }
        holder.tripPriceTextView.setText(tripList[position].totalPrice + " AED")

        holder.tripDateTextView.setText(
            CommonMethods.dateParsingyyyyMMddToDdMmmYyyy(tripList[position].tripStartDate) + " to " + CommonMethods.dateParsingyyyyMMddToDdMmmYyyy(
                tripList[position].tripEndDate
            )
        )
    }

    private fun showPaymentsPopUp(activity: Context) {
        var flag = false
        val bottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialogTheme)
        val layout: View =
            LayoutInflater.from(activity)
                .inflate(R.layout.dialog_bottom_sheet_payment_credit_or_debit, null)
        bottomSheetDialog.setContentView(layout)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setCanceledOnTouchOutside(true)
//        val payTotalView = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.payTotalView)
//        val payInstallmentView =
//            bottomSheetDialog.findViewById<ConstraintLayout>(R.id.payInstallmentView)
//        val selectPaymentMethodView =
        bottomSheetDialog.findViewById<ConstraintLayout>(R.id.selectPaymentMethodView)
//        val totalAmountTextView = bottomSheetDialog.findViewById<TextView>(R.id.totalAmountTextView)
//        totalAmountTextView!!.text = "425 AED"

//        if (multipleInstallmentsArray.size > 1) {
//            payInstallmentView!!.visibility = View.VISIBLE
//        } else {
//            payInstallmentView!!.visibility = View.GONE
//        }
//        if (tripStatus.equals("6", ignoreCase = true)) {
//            payInstallmentView.visibility = View.VISIBLE
//            payTotalView!!.visibility = View.GONE
//        }
//        payInstallmentView.setOnClickListener {
//            bottomSheetDialog.dismiss()
//            val intent = Intent(context, TripInstallmentActivity::class.java)
////            intent.putExtra("tripID", tripID)
////            intent.putExtra("tripName", tripName)
//            context.startActivity(intent)
//        }
//        payTotalView!!.setOnClickListener {
////            bottomSheetDialog.dismiss()
////            initialisePayment()
//            if (!flag){
//                flag = true
//                selectPaymentMethodView!!.visibility = View.VISIBLE
//            }else{
//                flag = false
//                selectPaymentMethodView!!.visibility = View.GONE
//
//            }
//        }
        bottomSheetDialog.show()
    }


    override fun getItemCount(): Int {
        return tripList.size
    }

}