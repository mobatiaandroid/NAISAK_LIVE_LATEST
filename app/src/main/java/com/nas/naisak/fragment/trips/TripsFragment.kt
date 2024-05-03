package com.nas.naisak.fragment.trips

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.nas.naisak.R
import com.nas.naisak.activity.login.LoginActivity
import com.nas.naisak.activity.trips.TripCategoriesActivity
import com.nas.naisak.activity.trips.TripInfoActivity
import com.nas.naisak.activity.trips.TripPaymentsActivity
import com.nas.naisak.activity.trips.model.TripBannerResponseModel
import com.nas.naisak.commonmodels.SendStaffMailApiModel
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.ProgressBarDialog
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    private var contactEmail:String=""


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

        initFn()
        val internetCheck = CommonMethods.isInternetAvailable(mContext)
        if (internetCheck) {
           callTripBannerApi()

        } else {
            CommonMethods.showSuccessInternetAlert(mContext)
        }

        return mRootView

    }

    private fun callTripBannerApi() {

        var token = "Bearer " + PreferenceManager.getUserCode(mContext)

        progressDialogP.show()
        val call: Call<TripBannerResponseModel> = ApiClient.getClient.trip_banner(token)
        call.enqueue(object : Callback<TripBannerResponseModel> {
            override fun onFailure(call: Call<TripBannerResponseModel>, t: Throwable) {
                progressDialogP.hide()
            }

            override fun onResponse(call: Call<TripBannerResponseModel>, response: Response<TripBannerResponseModel>) {
                progressDialogP.hide()
                val responsedata = response.body()

                if (response.body()!!.status == 100) {
                    val bannerImageResponse: String = response.body()!!.data.bannerImage
                    if (bannerImageResponse != "") {
                        Glide.with(mContext).load(CommonMethods.replace(bannerImageResponse))
                            .centerCrop().placeholder(R.drawable.default_banner)
                            .into(bannerImagePager)
                    } else {
                        bannerImagePager.setBackgroundResource(R.drawable.default_banner)
                    }
                    if (!response.body()!!.data.bannerDescription.equals("")) {
                        descriptionTitle.visibility = View.VISIBLE
                        descriptionTitle.text = response.body()!!.data.bannerDescription
                    }
                    else descriptionTitle.visibility = View.GONE
                    if (!response.body()!!.data.bannerContactEmail.equals("")) {
                        sendEmail.visibility = View.VISIBLE
                        contactEmail = response.body()!!.data.bannerContactEmail
                    } else sendEmail.visibility = View.GONE



                }

            }

        })
    }

    private fun initFn() {
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

        sendEmail.setOnClickListener(View.OnClickListener {

            val dialog = Dialog(mContext)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.alert_send_email_dialog)
            var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
            var cancelButton = dialog.findViewById(R.id.cancelButton) as Button
            var submitButton = dialog.findViewById(R.id.submitButton) as Button
            var text_dialog = dialog.findViewById(R.id.text_dialog) as EditText
            var text_content = dialog.findViewById(R.id.text_content) as EditText
            iconImageView.setImageResource(R.drawable.roundemail)
            text_dialog.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    text_dialog.hint = ""
                    text_dialog.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                    text_dialog.setPadding(5, 5, 0, 0)
                } else {
                    text_dialog.hint = "Enter your subject here..."
                    text_dialog.gravity = Gravity.CENTER
                }
            }
            text_content.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    text_content.gravity = Gravity.LEFT
                } else {
                    text_content.gravity = Gravity.CENTER
                }
            }

            cancelButton.setOnClickListener()
            {
                dialog.dismiss()
            }
            submitButton.setOnClickListener()
            {
                if (text_dialog.text.toString().trim().equals("")) {
                    CommonMethods.showDialogueWithOk(
                        mContext,
                        "Please enter your subject",
                        "Alert"
                    )

                } else {
                    if (text_content.text.toString().trim().equals("")) {
                        CommonMethods.showDialogueWithOk(
                            mContext,
                            "Please enter your content",
                            "Alert"
                        )

                    } else {
                        progressDialogP.show()

                        var internetCheck = CommonMethods.isInternetAvailable(mContext)
                        if (internetCheck) {
                            callSendEmailToStaffApi(
                                text_dialog.text.toString().trim(),
                                text_content.text.toString().trim(),
                                contactEmail,
                                dialog,
                                progressDialogP
                            )

                        } else {
                            CommonMethods.showSuccessInternetAlert(mContext)
                        }
                    }
                }
            }
            dialog.show()
        })
    }

    fun callSendEmailToStaffApi(
        title: String,
        message: String,
        staffEmail: String,
        dialog: Dialog,
        progressDialog: ProgressBarDialog
    ) {
        val token = PreferenceManager.getUserCode(mContext)
        val sendMailBody = SendStaffMailApiModel(staffEmail, title, message)
        val call: Call<ResponseBody> =
            ApiClient.getClient.sendStaffMail(sendMailBody, "Bearer " + token)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
                progressDialog.show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responsedata = response.body()
                progressDialog.hide()
                Log.e("Response Signup", responsedata.toString())
                if (responsedata != null) {
                    try {

                        val jsonObject = JSONObject(responsedata.string())
                        if (jsonObject.has("status")) {
                            val status: Int = jsonObject.optInt("status")
                            Log.e("STATUS LOGIN", status.toString())
                            if (status == 100) {
                                dialog.dismiss()
                                CommonMethods.showDialogueWithOk(
                                    mContext,
                                    "Successfully send the email.",
                                    "Success"
                                )
                                //dialog.dismiss()

                            } else if (status == 116) {
                                PreferenceManager.setUserCode(mContext, "")
                                PreferenceManager.setUserEmail(mContext, "")
                                val mIntent = Intent(activity, LoginActivity::class.java)
                                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                mContext.startActivity(mIntent)
                            }

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        if (response.code() == 116) {
                            PreferenceManager.setUserCode(mContext, "")
                            PreferenceManager.setUserEmail(mContext, "")
                            val mIntent = Intent(activity, LoginActivity::class.java)
                            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            mContext.startActivity(mIntent)
                        }
                    }
                }
            }

        })
    }

}