package com.nas.naisak.activity.trips

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.gson.JsonObject
import com.nas.naisak.R
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.activity.login.LoginActivity
import com.nas.naisak.activity.trips.adapter.TripHistoryListAdapter
import com.nas.naisak.activity.trips.model.TripHistoryResponseModel
import com.nas.naisak.commonadapters.StudentListAdapter
import com.nas.naisak.commonmodels.StudentDataListResponse
import com.nas.naisak.commonmodels.StudentListModel
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.ProgressBarDialog
import com.nas.naisak.constants.recyclermanager.ItemOffsetDecoration
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripPaymentsActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var extras: Bundle
    lateinit var tab_type: String
    private lateinit var progressDialogP: ProgressBarDialog
    lateinit var relativeHeader: RelativeLayout
    lateinit var tripListRecycler: RecyclerView
    lateinit var recyclerViewLayoutManager: GridLayoutManager
    lateinit var backRelative: RelativeLayout
    var categoryID = ""
    var categoryName = ""
    lateinit var mStudentSpinner: LinearLayout
    var studentListArrayList = ArrayList<StudentDataListResponse>()
    var studentName: String = ""
    var studentId: Int = 0
    var studentImg: String = ""
    var studentClass: String = ""

    //    lateinit var categoriesList: ArrayList<TripListResponseModel.TripItem>
    lateinit var tripsCategoryAdapter: TripHistoryListAdapter
    var contactEmail = ""
    lateinit var back: ImageView

    //    lateinit var btn_history: ImageView
    lateinit var home: ImageView
    lateinit var studentNameTxt: TextView
    lateinit var studImg: ImageView
    lateinit var stud_id: String
    var studClass = ""
    var orderId = ""
    lateinit var stud_img: String

    //    var studentsModelArrayList = ArrayList<StudentDataModel.DataItem>()
    var studentList = ArrayList<String>()
    private lateinit var logoClickImgView: ImageView
    private lateinit var btn_left: ImageView
    private lateinit var heading: TextView
    var tripList: ArrayList<TripHistoryResponseModel.Trip> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_payments)
        context = this
        initialiseUI()
        if (CommonMethods.isInternetAvailable(context)) {
            callStudentListApi()
        } else {
            CommonMethods.showSuccessInternetAlert(context)
        }
    }

    private fun initialiseUI() {
//        extras = intent.extras!!
//        categoryID = extras.getInt("trip_category_id", 0).toString()!!
//        categoryName = extras.getString("trip_category_name")!!
        progressDialogP = ProgressBarDialog(context, R.drawable.spinner)
        logoClickImgView = findViewById(R.id.logoClickImgView)

        relativeHeader = findViewById(R.id.relativeHeader)
        heading = findViewById(R.id.heading)
        backRelative = findViewById(R.id.backRelative)


        tripListRecycler = findViewById(R.id.tripListRecycler)
        tripListRecycler.setHasFixedSize(true)
        heading.text = getString(R.string.payments)
        val spacing = 5 // 50px

        val itemDecoration = ItemOffsetDecoration(context, spacing)
        recyclerViewLayoutManager = GridLayoutManager(context, 1)


        tripListRecycler.addItemDecoration(itemDecoration)
        tripListRecycler.layoutManager = recyclerViewLayoutManager
        logoClickImgView.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        })
        backRelative.setOnClickListener {
            finish()
        }

        mStudentSpinner = findViewById(R.id.studentSpinner)
        studentNameTxt = findViewById<TextView>(R.id.studentName)
        studImg = findViewById<ImageView>(R.id.imagicon)


        mStudentSpinner.setOnClickListener {
            showStudentList(context, studentListArrayList)
        }
    }

    fun showStudentList(context: Context, mStudentList: ArrayList<StudentDataListResponse>) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialogue_student_list)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var btn_dismiss = dialog.findViewById(R.id.btn_dismiss) as Button
        var studentListRecycler =
            dialog.findViewById(R.id.recycler_view_social_media) as RecyclerView
        iconImageView.setImageResource(R.drawable.boy)
        //if(mSocialMediaArray.get())
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            btn_dismiss.setBackgroundDrawable(
                context.resources.getDrawable(R.drawable.button_new)
            )
        } else {
            btn_dismiss.background = context.resources.getDrawable(R.drawable.button_new)
        }


        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        studentListRecycler.layoutManager = llm
        val studentAdapter = StudentListAdapter(context, mStudentList)
        studentListRecycler.adapter = studentAdapter
        btn_dismiss.setOnClickListener()
        {
            dialog.dismiss()
        }



        studentListRecycler.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                studentName = studentListArrayList[position].studentName
                studentImg = studentListArrayList[position].photo
                studentId = studentListArrayList[position].studentId
                studentClass = studentListArrayList[position].section
                PreferenceManager.setStudentID(context, studentId)
                PreferenceManager.setStudentName(context, studentName)
                PreferenceManager.setStudentPhoto(context, studentImg)
                PreferenceManager.setStudentClass(context, studentClass)
                studentNameTxt.text = studentName
                if (studentImg != "") {
                    Glide.with(context) //1
                        .load(studentImg).fitCenter()

                        .placeholder(R.drawable.boy)
                        .error(R.drawable.boy)
                        .skipMemoryCache(true) //2
                        .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                        .transform(CircleCrop()) //4
                        .into(studImg)
                } else {
                    studImg.setImageResource(R.drawable.boy)
                }
                if (CommonMethods.isInternetAvailable(context)) {
                    callTripList()
                } else {
                    CommonMethods.showSuccessInternetAlert(context)
                }
                dialog.dismiss()
            }
        })
        dialog.show()
    }


    private fun callTripList() {
        progressDialogP.show()
        var paramObject = JsonObject()
       // android.util.Log.e("student name", studentNameTxt.getText().toString())
        paramObject.addProperty("student_id", PreferenceManager.getStudentID(context))
        paramObject.addProperty("language_type",PreferenceManager().getLanguage(context!!)!!)
        val call: Call<TripHistoryResponseModel> =
            ApiClient.getClient.tripHistory(
                "Bearer " + PreferenceManager.getUserCode(context),
                paramObject
            )
        call.enqueue(object : Callback<TripHistoryResponseModel> {
            override fun onResponse(
                call: Call<TripHistoryResponseModel>,
                response: Response<TripHistoryResponseModel>
            ) {
                progressDialogP.dismiss()
                tripList = ArrayList()

                if (response.body()!!.status == 100) {
                    if (response.body()!!.data.trips.size > 0) {
                       // Log.e("asd", response.body()!!.data.trips.size.toString())

                        tripList = response.body()!!.data.trips
                       // Log.e("tripList", tripList.size.toString())
                        if (tripList.size > 0) {
                           // Log.e("Here", "Here")
                            tripsCategoryAdapter = TripHistoryListAdapter(context, tripList)
                            tripListRecycler.adapter = tripsCategoryAdapter
                        } else {
                          //  Log.e("Here", "not")
                            tripList =
                                ArrayList<TripHistoryResponseModel.Trip>()
                            tripsCategoryAdapter =
                                TripHistoryListAdapter(context, ArrayList())
                            tripListRecycler.adapter = tripsCategoryAdapter
                            Toast.makeText(
                                this@TripPaymentsActivity,
                                getString(R.string.no_trip_available),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        tripList = java.util.ArrayList<TripHistoryResponseModel.Trip>()
                        tripsCategoryAdapter =
                            TripHistoryListAdapter(context, ArrayList())
                        tripListRecycler.adapter = tripsCategoryAdapter
                        Toast.makeText(
                            this@TripPaymentsActivity,
                            getString(R.string.no_trip_available),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    tripList = java.util.ArrayList<TripHistoryResponseModel.Trip>()
                    tripsCategoryAdapter =
                        TripHistoryListAdapter(context, ArrayList())
                    tripListRecycler.adapter = tripsCategoryAdapter
                    Toast.makeText(
                        this@TripPaymentsActivity,
                        getString(R.string.no_trip_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onFailure(call: Call<TripHistoryResponseModel>, t: Throwable) {
                progressDialogP.dismiss()
            }
        })
    }

    fun callStudentListApi() {
        studentListArrayList = ArrayList()
        val token = PreferenceManager.getUserCode(context)
        val call: Call<StudentListModel> = ApiClient.getClient.studentList("Bearer " + token)
        call.enqueue(object : Callback<StudentListModel> {
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {
               // Log.e("Error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<StudentListModel>,
                response: Response<StudentListModel>
            ) {
                if (response.body()!!.status == 100) {
                    studentListArrayList = ArrayList()
                    studentListArrayList.addAll(response.body()!!.dataArray.studentListArray)
                   // Log.e("studlistsize", studentListArrayList.size.toString())
                    if (PreferenceManager.getStudentID(context) == 0) {
                        studentName = studentListArrayList.get(0).studentName
                        studentImg = studentListArrayList.get(0).photo
                        studentId = studentListArrayList.get(0).studentId
                        studentClass = studentListArrayList.get(0).section
                        PreferenceManager.setStudentID(context, studentId)
                        PreferenceManager.setStudentName(context, studentName)
                        PreferenceManager.setStudentPhoto(context, studentImg)
                        PreferenceManager.setStudentClass(context, studentClass)
                        studentNameTxt.text = studentName
                        if (!studentImg.equals("")) {
                            Glide.with(context) //1
                                .load(studentImg).fitCenter()

                                .placeholder(R.drawable.boy)
                                .error(R.drawable.boy)
                                .skipMemoryCache(true) //2
                                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                                .transform(CircleCrop()) //4
                                .into(studImg)
                        } else {
                            studImg.setImageResource(R.drawable.boy)

                        }

                    } else {
                        studentName = PreferenceManager.getStudentName(context)!!
                        studentImg = PreferenceManager.getStudentPhoto(context)!!
                        studentId = PreferenceManager.getStudentID(context)
                        studentClass = PreferenceManager.getStudentClass(context)!!
                        studentNameTxt.text = studentName
                        if (studentImg != "") {
                            Glide.with(context) //1
                                .load(studentImg).fitCenter()

                                .placeholder(R.drawable.boy)
                                .error(R.drawable.boy)
                                .skipMemoryCache(true) //2
                                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                                .transform(CircleCrop()) //4
                                .into(studImg)
                        } else {
                            studImg.setImageResource(R.drawable.boy)
                        }
                    }

                    if (CommonMethods.isInternetAvailable(context)) {
                        callTripList()
                    } else {
                        CommonMethods.showSuccessInternetAlert(context)
                    }
                } else if (response.body()!!.status == 116) {
                    PreferenceManager.setUserCode(context, "")
                    PreferenceManager.setUserEmail(context, "")
                    val mIntent = Intent(this@TripPaymentsActivity, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(mIntent)

                } else {

                }
            }
        })
    }
}