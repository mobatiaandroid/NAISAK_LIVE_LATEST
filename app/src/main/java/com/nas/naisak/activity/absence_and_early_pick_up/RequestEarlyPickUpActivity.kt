package com.nas.naisak.activity.absence_and_early_pick_up

import GeneralSubmitResponseModel
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
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
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.gson.JsonObject
import com.nas.naisak.R
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.activity.login.LoginActivity
import com.nas.naisak.commonadapters.StudentListAdapter
import com.nas.naisak.commonmodels.StudentDataListResponse
import com.nas.naisak.commonmodels.StudentListModel
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.ProgressBarDialog
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.properties.Delegates

class RequestEarlyPickUpActivity : AppCompatActivity() {
    lateinit var mContext: Context
    var hour: Int = 0
    var min: String = ""
    var hour_new: String = ""
    var new_time: String = ""
    lateinit var backRelative: RelativeLayout
    lateinit var heading: TextView
    lateinit var logoClickImgView: ImageView
    lateinit var progressDialogAdd: ProgressBar
    var studentListArrayList = ArrayList<StudentDataListResponse>()
    lateinit var studentSpinner: LinearLayout
    lateinit var studImg: ImageView
    lateinit var studentName: String
    var studentId by Delegates.notNull<Int>()
    lateinit var studentImg: String
    lateinit var studentClass: String
    lateinit var studentNameTxt: TextView
    lateinit var enterStratDate: TextView
    lateinit var enterTime: TextView
    lateinit var pickupName: TextView
    lateinit var submitBtn: Button
    lateinit var enterMessage: EditText
    var fromDate: String = ""
    var toDate: String = ""
    var totime: String = ""
    lateinit var submitLayout: LinearLayout
    lateinit var myCalendar: Calendar
    lateinit var currentDate: Date
    lateinit var sdate: Date
    lateinit var progressBar: ProgressBarDialog
    lateinit var edate: Date
    var elapsedDays: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_early_pick_up)
        mContext = this
        initfn()

        if (CommonMethods.isInternetAvailable(mContext)) {
//            progressDialog.visibility= View.VISIBLE
            callStudentListApi()
        } else {
            Toast.makeText(mContext, "No internet available!", Toast.LENGTH_SHORT).show()
        }
    }

    fun callStudentListApi() {
        studentListArrayList = java.util.ArrayList()
        val token = PreferenceManager.getUserCode(mContext)
        val call: Call<StudentListModel> = ApiClient.getClient.studentList("Bearer " + token)
        call.enqueue(object : Callback<StudentListModel> {
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<StudentListModel>,
                response: Response<StudentListModel>
            ) {
                if (response.body()!!.status == 100) {
                    studentListArrayList = java.util.ArrayList()
                    studentListArrayList.addAll(response.body()!!.dataArray.studentListArray)
                    Log.e("studlistsize", studentListArrayList.size.toString())
                    if (PreferenceManager.getStudentID(mContext) == 0) {
                        Log.e("Empty", "Empty")
                        studentName = studentListArrayList[0].studentName
                        studentImg = studentListArrayList.get(0).photo
                        studentId = studentListArrayList.get(0).studentId
                        studentClass = studentListArrayList.get(0).section
                        PreferenceManager.setStudentID(mContext, studentId)
                        PreferenceManager.setStudentName(mContext, studentName)
                        PreferenceManager.setStudentPhoto(mContext, studentImg)
                        PreferenceManager.setStudentClass(mContext, studentClass)
                        studentNameTxt.text = studentName
                        if (!studentImg.equals("")) {
                            Glide.with(mContext) //1
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
                        studentName = PreferenceManager.getStudentName(mContext)!!
                        studentImg = PreferenceManager.getStudentPhoto(mContext)!!
                        studentId = PreferenceManager.getStudentID(mContext)
                        studentClass = PreferenceManager.getStudentClass(mContext)!!
                        studentNameTxt.text = studentName
                        if (studentImg != "") {
                            Glide.with(mContext) //1
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

//                    if (CommonMethods.isInternetAvailable(mContext)) {
//                        callTripList()
//                    } else {
//                        CommonMethods.showSuccessInternetAlert(mContext)
//                    }
                } else if (response.body()!!.status == 116) {
                    PreferenceManager.setUserCode(mContext, "")
                    PreferenceManager.setUserEmail(mContext, "")
                    val mIntent = Intent(mContext, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    mContext!!.startActivity(mIntent)

                } else {

                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initfn() {
        progressBar = ProgressBarDialog(mContext, R.drawable.spinner)

        progressDialogAdd = findViewById(R.id.progressDialogAdd)
        studentSpinner = findViewById(R.id.studentSpinner)
        heading = findViewById(R.id.heading)
//        heading.text= ConstantWords.earlypickup
        backRelative = findViewById(R.id.backRelative)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        myCalendar = Calendar.getInstance()
        currentDate = Calendar.getInstance().time
        studentNameTxt = findViewById<TextView>(R.id.studentName)
        enterStratDate = findViewById<TextView>(R.id.enterStratDate)
        enterTime = findViewById<TextView>(R.id.enterEndDate)
        pickupName = findViewById(R.id.enterPickupname)
        studImg = findViewById<ImageView>(R.id.studImg)
        enterMessage = findViewById<EditText>(R.id.enterMessage)
        submitLayout = findViewById<LinearLayout>(R.id.submitLayout)
        submitBtn = findViewById<Button>(R.id.submitBtn)
        studentSpinner.setOnClickListener() {
            showStudentList(mContext, studentListArrayList)
        }
        backRelative.setOnClickListener(View.OnClickListener {
            finish()
        })
//        heading.text= ConstantWords.earlypickup
        logoClickImgView.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        })

        submitBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                if (enterStratDate.text.equals("")) {
                    CommonMethods.showDialogueWithOk(
                        mContext,
                        "Please select Date of Early Pickup",
                        "Alert"
                    )
                } else {
                    if (enterTime.text.equals("")) {
                        CommonMethods.showDialogueWithOk(
                            mContext,
                            "Please select your Pickup Time",
                            "Alert"
                        )
                    } else {
                        if (pickupName.text.isEmpty()) {
                            CommonMethods.showDialogueWithOk(
                                mContext,
                                "Please enter pickup person name",
                                "Alert"
                            )


                        } else {

                            if (enterMessage.text.isEmpty()) {
                                CommonMethods.showDialogueWithOk(
                                    mContext,
                                    "Please enter reason for early pickup",
                                    "Alert"
                                )


                            } else {
                                var date_entered = enterStratDate.text
                                var date = toDate
                                var time_entered = enterTime.text
                                var time = totime
                                var pickupname_entered = pickupName.text
                                var reason_entered = enterMessage.text

                                /*callPickupSubmitApi(date,time.toString(),pickupname_entered.toString(),
                                    reason_entered.toString()
                                )*/
                                callPickupSubmitApi(
                                    date, new_time, pickupname_entered.toString(),
                                    reason_entered.toString()
                                )
                            }
                        }
                    }


                }


            }
        })
        enterStratDate.setOnClickListener {
            cal()
        }
        enterTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (enterStratDate.text.equals("")) {
                    CommonMethods.showDialogueWithOk(
                        mContext,
                        "Please select Date of Early Pickup",
                        "Alert"
                    )


                } else {
                    val mTimePicker: TimePickerDialog
                    val mcurrentTime = Calendar.getInstance()
                    val hours = mcurrentTime.get(Calendar.HOUR_OF_DAY)
                    val minute = mcurrentTime.get(Calendar.MINUTE)
                    //var am_pm = mcurrentTime.get(Calendar.AM_PM)
                    var am_pm: String = ""

                    mTimePicker =
                        TimePickerDialog(mContext, object : TimePickerDialog.OnTimeSetListener {
                            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                                var AM_PM: String
                                hour = hourOfDay
                                min = minute.toString()
                                var hour_n = hour.toString()

                                if (minute < 10) {
                                    min = "0" + min
                                }
                                if (hourOfDay < 10) {
                                    hour_n = "0" + hour.toString()

                                }

                                new_time = hour_n + ":" + min + ":" + "00"

                                if (hour == 0) {

                                    hour = 12
                                    AM_PM = "AM"
                                } else if (hour < 12) {
                                    hour = hourOfDay
                                    AM_PM = "AM"
                                } else if (hour > 12) {
                                    hour -= 12
                                    AM_PM = "PM"
                                } else if (hour == 12) {

                                    hour = 12
                                    AM_PM = "PM"
                                } else
                                    AM_PM = "AM"
                                enterTime.text = hour.toString() + ":" + min + ":" + "00" + AM_PM
                                totime = hour.toString() + ":" + min + ":" + "00"
                            }
                        }, hour, minute, false)

                    enterTime.setOnClickListener({ v ->
                        mTimePicker.show()
                    })

                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun cal() {
        val mcurrentTime = android.icu.util.Calendar.getInstance()
        val year = mcurrentTime.get(android.icu.util.Calendar.YEAR)
        val month = mcurrentTime.get(android.icu.util.Calendar.MONTH)
        val day = mcurrentTime.get(android.icu.util.Calendar.DAY_OF_MONTH)
        val minDate = android.icu.util.Calendar.getInstance()
        minDate.set(android.icu.util.Calendar.HOUR_OF_DAY, 0)
        minDate.set(android.icu.util.Calendar.MINUTE, 0)
        minDate.set(android.icu.util.Calendar.SECOND, 0)
        val dpd1 = DatePickerDialog(
            this,
            R.style.MyDatePickerStyle,
            object : DatePickerDialog.OnDateSetListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    var firstday: String? = String.format("%d-%d-%d", month + 1, dayOfMonth, year)


                    var date_sel: String? = String.format("%d-%d-%d", dayOfMonth, month + 1, year)
                    var date_temp = date_sel.toString()

                    val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
                    val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
                    val inputDateStr = date_temp
                    val date: Date = inputFormat.parse(inputDateStr)
                    val outputDateStr: String = outputFormat.format(date)
                    toDate = date_sel.toString()
                    enterStratDate.text = outputDateStr

                }
            },
            year,
            month,
            day
        )
        //enterStratDate.setOnClickListener{
        dpd1.datePicker.minDate = android.icu.util.Calendar.getInstance().timeInMillis
        dpd1.show()
//}
    }

    fun callPickupSubmitApi(date: String, time: String, pickupby: String, reason: String) {
        var devicename: String = (Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES::class.java.fields[Build.VERSION.SDK_INT]
            .name)
        var new_date: String = ""
        val inputFormat: DateFormat = SimpleDateFormat("d-m-yyyy")
        val outputFormat: DateFormat = SimpleDateFormat("yyyy-mm-dd")
        val inputDateStr = date
        val date: Date = inputFormat.parse(inputDateStr)
        new_date = outputFormat.format(date)
        val token = PreferenceManager.getUserCode(mContext)
        val paramObject = JsonObject()
        paramObject.addProperty("student_id", PreferenceManager.getStudentID(mContext))
        paramObject.addProperty("pickup_date", new_date)
        paramObject.addProperty("pickup_time", time)
        paramObject.addProperty("pickup_reason", reason)
        paramObject.addProperty("pickup_by_whom", pickupby)
        paramObject.addProperty("device_type", "2")
        paramObject.addProperty("device_name", devicename)
        paramObject.addProperty("app_version", "1.0")
        progressBar.show()
        val call: Call<GeneralSubmitResponseModel> =
            ApiClient.getClient.requestEarlyPickUp("Bearer " + token, paramObject)
        call.enqueue(object : Callback<GeneralSubmitResponseModel> {
            override fun onFailure(call: Call<GeneralSubmitResponseModel>, t: Throwable) {
                progressBar.dismiss()
                //mProgressRelLayout.visibility=View.INVISIBLE
            }

            override fun onResponse(
                call: Call<GeneralSubmitResponseModel>,
                response: Response<GeneralSubmitResponseModel>
            ) {
                val responsedata = response.body()
                progressBar.dismiss()
                //progressDialog.visibility = View.GONE

                if (responsedata != null) {
                    try {

                        if (response.body()!!.status == 100) {

                            commonSuccessAlertDialog(
                                "Success",
                                "Successfully submitted your early pickup request. Please wait for approval.",
                                mContext
                            )
                            //Toast.makeText(nContext, "Transaction successfully completed", Toast.LENGTH_SHORT).show()

                        } else if (response.body()!!.status == 136) {
                            CommonMethods.showDialogueWithOk(
                                mContext,
                                "Date already Registered!",
                                "Alert"
                            )
                        } else {
                            CommonMethods.showDialogueWithOk(
                                mContext,
                                "Some Error Occurred",
                                "Alert"
                            )
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })

    }

    fun commonSuccessAlertDialog(heading: String, Message: String, context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_success_alert)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var messageTxt = dialog.findViewById(R.id.messageTxt) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        messageTxt.text = Message
        alertHead.text = heading
        btn_Ok.setOnClickListener()
        {
            dialog.dismiss()
            finish()
        }
        dialog.show()

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

                dialog.dismiss()
            }
        })
        dialog.show()
    }
}