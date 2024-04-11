package com.nas.naisak.fragment.trips

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.nas.naisak.R
import com.nas.naisak.activity.trips.TripCategoriesActivity
import com.nas.naisak.activity.trips.TripInfoActivity
import com.nas.naisak.activity.trips.TripPaymentsActivity
import com.nas.naisak.constants.ProgressBarDialog

class TripsFragment : Fragment() {

    private lateinit var mContext: Context
    private lateinit var progressDialogP: ProgressBarDialog
    private lateinit var mRootView: View
    private lateinit var sendEmail: ImageView
    private lateinit var descriptionTitle: TextView
    private lateinit var mtitle: RelativeLayout
    private lateinit var bannerImagePager: ImageView
    private lateinit var paymentLinear: LinearLayout
    private lateinit var registerTripLinear: LinearLayout
    private lateinit var informationLinear: LinearLayout
    private lateinit var studeLinear: ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_trips, container,
            false
        )
        mContext = requireActivity()
        val mTitleTextView = mRootView.findViewById<View>(R.id.titleTextView) as TextView
        mTitleTextView.text = "Trips"

        progressDialogP =
            ProgressBarDialog(mContext, R.drawable.spinner)

        registerTripLinear =
            mRootView.findViewById<View>(R.id.registerSchoolTripLinear) as LinearLayout
        informationLinear = mRootView.findViewById<View>(R.id.informationLinear) as LinearLayout
        paymentLinear = mRootView.findViewById<View>(R.id.paymentLinear) as LinearLayout
        studeLinear = mRootView.findViewById<View>(R.id.studeLinear) as ConstraintLayout
        mtitle = mRootView.findViewById<View>(R.id.title) as RelativeLayout
        bannerImagePager = mRootView.findViewById<View>(R.id.bannerImagePager) as ImageView

        descriptionTitle = mRootView.findViewById<View>(R.id.descriptionTitle) as TextView
        sendEmail = mRootView.findViewById<View>(R.id.sendEmail) as ImageView


        registerTripLinear.setOnClickListener {
            val intent = Intent(activity, TripCategoriesActivity::class.java)
            activity?.startActivity(intent)
        }

        informationLinear.setOnClickListener {
            val intent = Intent(activity, TripInfoActivity::class.java)
            activity?.startActivity(intent)
        }
        paymentLinear.setOnClickListener {
            val intent = Intent(activity, TripPaymentsActivity::class.java)
            activity?.startActivity(intent)
        }
        return mRootView

    }

}