package com.nas.naisak.activity.trips

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nas.naisak.R
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.activity.login.LoginActivity
import com.nas.naisak.activity.trips.adapter.TripsCategoryAdapter
import com.nas.naisak.activity.trips.model.TripCategoriesResponseModel
import com.nas.naisak.commonmodels.StudentDataListResponse
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.ProgressBarDialog
import com.nas.naisak.constants.recyclermanager.ItemOffsetDecoration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripCategoriesActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var extras: Bundle
    lateinit var tab_type: String
    private val EMAIL_PATTERN =
        "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$"
    private val pattern = "^([a-zA-Z ]*)$"
    lateinit var text_content: TextView
    lateinit var text_dialog: TextView
    lateinit var progressDialogP: ProgressBarDialog

    //    lateinit var progressDialog: ProgressBar
    lateinit var relativeHeader: RelativeLayout

    //    lateinit var headermanager: HeaderManager
    lateinit var bannerImageView: ImageView
    lateinit var descriptionTextView: TextView
    lateinit var sendEmailImageView: ImageView
    lateinit var categoryListRecyclerView: RecyclerView
    lateinit var recyclerViewLayoutManager: GridLayoutManager
    lateinit var categoriesList: ArrayList<TripCategoriesResponseModel.TripCategory>
    lateinit var tripsCategoryAdapter: TripsCategoryAdapter
    lateinit var contactEmail: String
    lateinit var back: ImageView
    private lateinit var backRelative: RelativeLayout

    //    lateinit var btn_history: ImageView
    lateinit var home: ImageView
    lateinit var studentName: TextView
    lateinit var studImg: ImageView
    lateinit var stud_id: String
    lateinit var studClass: String
    lateinit var orderId: String
    lateinit var stud_img: String
    lateinit var studentsModelArrayList: ArrayList<StudentDataListResponse>
    lateinit var studentList: ArrayList<String>
    private lateinit var logoClickImgView: ImageView
    private lateinit var btn_left: ImageView
    private lateinit var heading: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_categories)
        context = this
        initialiseUI()
        if (CommonMethods.isInternetAvailable(context)) {
            callTripCategories()
        } else {
            CommonMethods.showSuccessInternetAlert(context)
        }
    }

    private fun callTripCategories() {
        progressDialogP.show()
        val call: Call<TripCategoriesResponseModel> =
            ApiClient.getClient.tripCategories("Bearer " + PreferenceManager.getUserCode(context))
        call.enqueue(object : Callback<TripCategoriesResponseModel> {
            override fun onFailure(call: Call<TripCategoriesResponseModel>, t: Throwable) {
                progressDialogP.dismiss()
            }

            override fun onResponse(
                call: Call<TripCategoriesResponseModel>,
                response: Response<TripCategoriesResponseModel>
            ) {
                progressDialogP.dismiss()

                if (response.body()!!.status == 100) {
                    val bannerImageResponse: String = response.body()!!.data.bannerImage
                    if (bannerImageResponse != "") {
                        Glide.with(context).load(CommonMethods.replace(bannerImageResponse))
                            .centerCrop().placeholder(R.drawable.default_banner)
                            .into(bannerImageView)
                    } else {
                        bannerImageView.setBackgroundResource(R.drawable.default_banner)
                    }
                    if (!response.body()!!.data.bannerDescription.equals("")) {
                        descriptionTextView.text = response.body()!!.data.bannerDescription
                    } else descriptionTextView.visibility = View.GONE
                    if (!response.body()!!.data.bannerContactEmail.equals("")) {
                        contactEmail = response.body()!!.data.bannerContactEmail
                    } else sendEmailImageView.visibility = View.GONE
                    categoriesList = response.body()!!.data.tripCategories
                    if (categoriesList.size > 0) {
                       // Log.e("Here", "Here")
                        tripsCategoryAdapter = TripsCategoryAdapter(context, categoriesList)
                        categoryListRecyclerView.adapter = tripsCategoryAdapter
                    } else {
                     //   Log.e("Here", "not")
                        Toast.makeText(
                            this@TripCategoriesActivity,
                            "No categories available.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                } else if (response.body()!!.status == 116) {


                    PreferenceManager.setUserCode(context, "")
                    PreferenceManager.setUserEmail(context, "")
                    val mIntent = Intent(context, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(mIntent)

                } else {
                    if (response.body()!!.status == 101) {
                        CommonMethods.showDialogueWithOk(context, "Some error occurred", "Alert")
                    }
                }

            }

        })
    }

    private fun initialiseUI() {
//        extras = intent.extras!!
//        if (extras != null) {
//            tab_type = extras.getString("tab_type")!!
//        }
        heading = findViewById(R.id.heading)
        btn_left = findViewById(R.id.btn_left)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        heading.text = "Trip Categories"
        progressDialogP = ProgressBarDialog(context, R.drawable.spinner)
        bannerImageView = findViewById<ImageView>(R.id.bannerImage)
        descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)
        sendEmailImageView = findViewById<ImageView>(R.id.sendEmailImageView)
        relativeHeader = findViewById(R.id.relativeHeader)
//        headermanager = HeaderManager(this@TripCategoriesActivity, tab_type)
//        headermanager.getHeader(relativeHeader, 6)
//        back = headermanager.getLeftButton()
//        btn_history = headermanager.getRightHistoryImage()
//        btn_history.visibility = View.INVISIBLE
        backRelative = findViewById(R.id.backRelative)

        categoryListRecyclerView = findViewById<RecyclerView>(R.id.categoryListRecycler)
        categoryListRecyclerView.setHasFixedSize(true)
        val spacing = 5 // 50px
        backRelative.setOnClickListener(View.OnClickListener {
            finish()
        })
        logoClickImgView.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        })
        val itemDecoration = ItemOffsetDecoration(context, spacing)
        recyclerViewLayoutManager = GridLayoutManager(context, 2)
//        categoryListRecyclerView.addItemDecoration(
//                new DividerItemDecoration(context.getResources().getDrawable(R.drawable.list_divider)));
        //        categoryListRecyclerView.addItemDecoration(
//                new DividerItemDecoration(context.getResources().getDrawable(R.drawable.list_divider)));
        categoryListRecyclerView.addItemDecoration(itemDecoration)
        categoryListRecyclerView.layoutManager = recyclerViewLayoutManager
//        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back)
//        back.setOnClickListener {
//            AppUtils.hideKeyBoard(context)
//            finish()
//        }
//
//        home = headermanager.getLogoButton()
//        home.setOnClickListener {
//            val `in` = Intent(context, HomeListAppCompatActivity::class.java)
//            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            startActivity(`in`)
//        }
//        studentName = findViewById<TextView>(R.id.studentName)
//        studentName = findViewById<TextView>(R.id.studentName)
//        studImg = findViewById<ImageView>(R.id.imagicon)
        sendEmailImageView.setOnClickListener {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.alert_send_email_dialog)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dialogCancelButton = dialog.findViewById<View>(R.id.cancelButton) as Button
            val submitButton = dialog.findViewById<View>(R.id.submitButton) as Button
            text_dialog = dialog.findViewById<View>(R.id.text_dialog) as EditText
            text_content = dialog.findViewById<View>(R.id.text_content) as EditText
//            progressDialog = dialog.findViewById<View>(R.id.progressdialogg) as ProgressBar
            dialogCancelButton.setOnClickListener { //   AppUtils.hideKeyBoard(mContext);
                val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(text_dialog.windowToken, 0)
                imm.hideSoftInputFromWindow(text_content.windowToken, 0)
                dialog.dismiss()
            }
            submitButton.setOnClickListener {
                if (text_dialog.text.toString().trim { it <= ' ' } == "") {
                    val toast = Toast.makeText(
                        context, "Enter Subject", Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else {
                    if (text_content.text.toString().trim { it <= ' ' } == "") {
                        val toast = Toast.makeText(
                            context, "Enter Content", Toast.LENGTH_SHORT
                        )
                        toast.show()
                    } else if (contactEmail.matches(EMAIL_PATTERN.toRegex())) {
                        if (text_dialog.text.toString().trim { it <= ' ' }
                                .matches(pattern.toRegex())) {
                            if (text_content.text.toString().trim { it <= ' ' }
                                    .matches(pattern.toRegex())) {
                                if (CommonMethods.isInternetAvailable(context)) {
                                    // TODO sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF, dialog)
                                } else {
//                                    AppUtils.showDialogAlertDismiss(
//                                        context as Activity,
//                                        "Network Error",
//                                        getString(R.string.no_internet),
//                                        R.drawable.nonetworkicon,
//                                        R.drawable.roundred
//                                    )
                                }
                            } else {
                                val toast = Toast.makeText(
                                    context, "Enter valid content", Toast.LENGTH_SHORT
                                )
                                toast.show()
                            }
                        } else {
                            val toast = Toast.makeText(
                                context, "Enter valid subject", Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }
                    } else {
                        val toast = Toast.makeText(
                            context, "Email Invalid", Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }
            }
            dialog.show()
        }
    }
}