package com.nas.naisak.activity.trips;

import TripDetailsResponseModel
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.nas.naisak.BuildConfig
import com.nas.naisak.R
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.activity.login.LoginActivity
import com.nas.naisak.activity.payment.payhere.PaymentPayActivity
import com.nas.naisak.activity.payment.payhere.model.PaymentGatewayCreditInitiateResponseModel
import com.nas.naisak.activity.trips.adapter.TripInstallmentsAdapter
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.ProgressBarDialog
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripInstallmentActivity : AppCompatActivity() {
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
    lateinit var backRelative: RelativeLayout
    var merchantOrderReference = ""
    lateinit var installmentDetailArrayList: ArrayList<TripDetailsResponseModel.InstallmentDetail>
    var tripID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_installment)
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
        installmentDetailArrayList = ArrayList()
        back = findViewById(R.id.back)

        linearLayoutManager = LinearLayoutManager(mContext)
        titleTextView = findViewById(R.id.titleTextView)
        back = findViewById(R.id.back)
        logoclick = findViewById(R.id.logoclick)
        primaryRecyclerdetails = findViewById(R.id.primaryRecyclerdetails)
        progress = findViewById(R.id.progress)
        primaryRecyclerdetails.layoutManager = linearLayoutManager
        titleTextView.text = "Installments"
        back.setOnClickListener {
            finish()
        }
        if (CommonMethods.isInternetAvailable(mContext)) {
            installmentDetailArrayList = ArrayList()
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

                if (installmentDetailArrayList[position].paidStatus == 1) {
                    val intent = Intent(mContext, TripInvoiceViewActivity::class.java)
                    intent.putExtra(
                        "invoice_number",
                        installmentDetailArrayList[position].invoiceNo
                    )
//                    intent.putExtra("invoice_number", invoicesArray[position].invoiceNo)
                    startActivity(intent)

                } else {
                    showOptionPopUp(
                        mContext,
                        installmentDetailArrayList[position].amount,
                        installmentDetailArrayList[position].id
                    )
                }

            }

        })
    }

    private fun showOptionPopUp(activity: Context, amount: String, id: Int) {
        val bottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialogTheme)
        val layout: View =
            LayoutInflater.from(activity)
                .inflate(R.layout.dialog_bottom_sheet_payment_credit_or_debit, null)
        bottomSheetDialog.setContentView(layout)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setCanceledOnTouchOutside(true)
        val debitCardView: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.debitCardView)
        val creditCardView: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.creditCardView)
        debitCardView!!.setOnClickListener {
            bottomSheetDialog.dismiss()
            callDebitInitApi("2", amount, id)
        }
        creditCardView!!.setOnClickListener {
            bottomSheetDialog.dismiss()
            callCreditInitApi("1", amount, id)
        }

        bottomSheetDialog.findViewById<ConstraintLayout>(R.id.selectPaymentMethodView)

        bottomSheetDialog.show()
    }

    override fun onResume() {
        super.onResume()
        installmentDetailArrayList = ArrayList()
        getTripDetails(tripID)
    }

    fun callCreditInitApi(paymentMethod: String, amount: String, id: Int) {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        var device = manufacturer + model
        val versionName: String = BuildConfig.VERSION_NAME
        val token = PreferenceManager.getUserCode(mContext)
        val paramObject = JsonObject()
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        merchantOrderReference = "TRIPAND$ts"
        paramObject.addProperty("student_id", PreferenceManager.getStudentID(mContext))
        paramObject.addProperty("trip_item_id", tripID)
        paramObject.addProperty("order_reference", merchantOrderReference)
        paramObject.addProperty("invoice_number", merchantOrderReference)
        paramObject.addProperty("paid_amount", amount)
        paramObject.addProperty("installment_id", id)

        paramObject.addProperty("payment_type", "installment")
        paramObject.addProperty("device_type", "2")
        paramObject.addProperty("device_name", device)
        paramObject.addProperty("app_version", versionName)

        progressDialogP.show()
        val call: Call<PaymentGatewayCreditInitiateResponseModel> =
            ApiClient.getClient.tripCCPaymentInitiate("Bearer " + token, paramObject)

        call.enqueue(object : Callback<PaymentGatewayCreditInitiateResponseModel> {
            override fun onFailure(
                call: Call<PaymentGatewayCreditInitiateResponseModel>,
                t: Throwable
            ) {
               // Log.e("Error", t.localizedMessage)
                progressDialogP.dismiss()
            }

            override fun onResponse(
                call: Call<PaymentGatewayCreditInitiateResponseModel>,
                response: Response<PaymentGatewayCreditInitiateResponseModel>
            ) {
                progressDialogP.dismiss()
                if (response.body()!!.status == 100) {

                    var payment_url = response.body()!!.data.redirect_url
                    val intent = Intent(mContext, PaymentPayActivity::class.java)
                    intent.putExtra("payment_url", payment_url)
                    startActivity(intent)


                } else if (response.body()!!.status == 116) {
                    PreferenceManager.setUserCode(mContext, "")
                    PreferenceManager.setUserEmail(mContext, "")
                    val mIntent = Intent(this@TripInstallmentActivity, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    mContext.startActivity(mIntent)

                } else {


                }
            }

        })
    }


    fun callDebitInitApi(paymentMethod: String, amount: String, id: Int) {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        var device = manufacturer + model
        val versionName: String = BuildConfig.VERSION_NAME
        val token = PreferenceManager.getUserCode(mContext)
        val paramObject = JsonObject()
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        merchantOrderReference = "TRIPAND$ts"
        paramObject.addProperty("student_id", PreferenceManager.getStudentID(mContext))
        paramObject.addProperty("trip_item_id", tripID)
        paramObject.addProperty("order_reference", merchantOrderReference)
        paramObject.addProperty("invoice_number", merchantOrderReference)
        paramObject.addProperty("paid_amount", amount)
        paramObject.addProperty("payment_type", "installment")
        paramObject.addProperty("installment_id", id)
        paramObject.addProperty("device_type", "2")
        paramObject.addProperty("device_name", device)
        paramObject.addProperty("app_version", versionName)
        val call: Call<PaymentGatewayCreditInitiateResponseModel> =
            ApiClient.getClient.tripDCPaymentInitiate("Bearer " + token, paramObject)
        call.enqueue(object : Callback<PaymentGatewayCreditInitiateResponseModel> {
            override fun onFailure(
                call: Call<PaymentGatewayCreditInitiateResponseModel>,
                t: Throwable
            ) {
               // Log.e("Error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<PaymentGatewayCreditInitiateResponseModel>,
                response: Response<PaymentGatewayCreditInitiateResponseModel>
            ) {
                if (response.body()!!.status == 100) {

                    var payment_url = response.body()!!.data.redirect_url
                    val intent = Intent(mContext, PaymentPayActivity::class.java)
                    intent.putExtra("payment_url", payment_url)
                    startActivity(intent)
//                    var url = payment_url.replaceFirst(
//                        "^(http[s]?://www\\\\.|http[s]?://|www\\\\.)",
//                        ""
//                    )
//                    mainLinear.visibility = View.GONE
//                    paymentWeb.visibility = View.VISIBLE
//                    setWebViewSettingsPrint()
//                    Log.e("URL LOAD", url)
//                    paymentWeb.loadUrl(url)


                } else if (response.body()!!.status == 116) {
                    PreferenceManager.setUserCode(mContext, "")
                    PreferenceManager.setUserEmail(mContext, "")
                    val mIntent = Intent(this@TripInstallmentActivity, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    mContext.startActivity(mIntent)

                } else {


                }
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
//                val ib_detailsadapter = TripInstallmentsAdapter(mContext, ArrayList())
//                primaryRecyclerdetails.adapter = ib_detailsadapter
                if (response.body()!!.status == 100) {

                    if (response.body()!!.data.lists.installmentDetails.size > 0) {
                        installmentDetailArrayList = response.body()!!.data.lists.installmentDetails
                        val ib_detailsadapter =
                            TripInstallmentsAdapter(mContext, installmentDetailArrayList)
                        primaryRecyclerdetails.adapter = ib_detailsadapter
                    } else {
//                        val ib_detailsadapter = TripInstallmentsAdapter(mContext, ArrayList())
//                        primaryRecyclerdetails.adapter = ib_detailsadapter
                        Toast.makeText(mContext, "No Installments to be Paid", Toast.LENGTH_SHORT)
                            .show()
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