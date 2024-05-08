package com.nas.naisak.activity.trips;

import TripDetailsResponseModel
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.nas.naisak.R
import com.nas.naisak.activity.common_model.DetailListitems
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.activity.trips.adapter.TripInvoiceListAdapter
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.ProgressBarDialog
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripInvoiceListingActivity : AppCompatActivity() {
    lateinit var mContext: Context
    var id: String = ""
    var title: String = ""
    lateinit var progressDialogP: ProgressBarDialog
    lateinit var primaryRecyclerdetails: RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var progress: ProgressBar
    lateinit var back: ImageView
    lateinit var logoclick: ImageView
    lateinit var titleTextView: TextView
    lateinit var extras: Bundle
    lateinit var invoicesArray: ArrayList<TripDetailsResponseModel.Invoice>
    var tripID = ""

    var ibdetaillist = ArrayList<DetailListitems>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_invoice_listing)
        InitUI()
        back.setOnClickListener {
            finish()
        }

    }

    private fun InitUI() {
        mContext = this
        extras = intent.extras!!
        if (extras != null) {
            tripID = extras.getString("tripID").toString()!!
//            tripName = extras.getString("tripName").toString()!!
        }
        progressDialogP = ProgressBarDialog(mContext, R.drawable.spinner)
        invoicesArray = ArrayList()
        linearLayoutManager = LinearLayoutManager(mContext)
        titleTextView = findViewById(R.id.titleTextView)
        back = findViewById(R.id.back)
        logoclick = findViewById(R.id.logoclick)
        primaryRecyclerdetails = findViewById(R.id.primaryRecyclerdetails)
        progress = findViewById(R.id.progress)
        primaryRecyclerdetails.layoutManager = linearLayoutManager
        titleTextView.text = "Invoices"

        if (CommonMethods.isInternetAvailable(mContext)) {
            getTripDetails(tripID)
        } else {
            CommonMethods.showSuccessInternetAlert(mContext)
        }
        logoclick.setOnClickListener {
            val mIntent = Intent(mContext, HomeActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(mIntent)
        }

        primaryRecyclerdetails.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {


                val intent = Intent(mContext, TripInvoiceViewActivity::class.java)
                intent.putExtra("invoice_number", invoicesArray[position].invoiceNo)
//                    intent.putExtra("invoice_number", invoicesArray[position].invoiceNo)
                startActivity(intent)


            }

        })
    }

    private fun getTripDetails(tripID: String) {
        progressDialogP.show()
        val paramObject = JsonObject()
       // Log.e("tripID name", tripID)
        paramObject.addProperty("student_id", PreferenceManager.getStudentID(mContext))
        paramObject.addProperty("trip_id", tripID)
        val call: Call<TripDetailsResponseModel> = ApiClient.getClient.tripDetail(
            "Bearer " + PreferenceManager.getUserCode(mContext), paramObject
        )
        call.enqueue(object : Callback<TripDetailsResponseModel> {
            override fun onResponse(
                call: Call<TripDetailsResponseModel>, response: Response<TripDetailsResponseModel>
            ) {
                progressDialogP.dismiss()
                if (response.body()!!.status == 100) {

                    if (response.body()!!.data.lists.invoices.size > 0) {
                        invoicesArray.addAll(response.body()!!.data.lists.invoices)
                        val ib_detailsadapter = TripInvoiceListAdapter(mContext, invoicesArray)
                        primaryRecyclerdetails.adapter = ib_detailsadapter
                    }

                } else {
                }

            }

            override fun onFailure(call: Call<TripDetailsResponseModel>, t: Throwable) {
                progressDialogP.dismiss()
                CommonMethods.showDialogueWithOk(
                    mContext as Activity, getString(R.string.common_error), "Alert"
                )
            }
        })

    }


}