package com.nas.naisak.activity.absence_and_early_pick_up

import GeneralSubmitResponseModel
import android.app.DatePickerDialog
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
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
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

class RequestAbsenceActivity : AppCompatActivity() {
    lateinit var mContext: Context
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
    lateinit var enterEndDate: TextView
    lateinit var submitBtn: Button
    lateinit var enterMessage: EditText
    var fromDate: String = ""
    var toDate: String = ""
    lateinit var progressBar: ProgressBarDialog
    lateinit var reasonAPI: String
    lateinit var submitLayout: LinearLayout
    lateinit var myCalendar: Calendar
    lateinit var currentDate: Date
    lateinit var sdate: Date
    lateinit var edate: Date
    var elapsedDays: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_absence)
        mContext = this
        initfn()
        if (CommonMethods.isInternetAvailable(mContext)) {
//            progressDialog.visibility= View.VISIBLE
            callStudentListApi()
        } else {
            Toast.makeText(mContext, "No internet available!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initfn() {
        progressBar = ProgressBarDialog(mContext, R.drawable.spinner)
        progressDialogAdd = findViewById(R.id.progressDialogAdd)
        studentSpinner = findViewById(R.id.studentSpinner)
        heading = findViewById(R.id.heading)
//        heading.text= ConstantWords.absence
        backRelative = findViewById(R.id.backRelative)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        studImg = findViewById<ImageView>(R.id.studImg)
        studentNameTxt = findViewById<TextView>(R.id.studentName)
        myCalendar = Calendar.getInstance()
        currentDate = Calendar.getInstance().time
        studentNameTxt = findViewById<TextView>(R.id.studentName)
        enterMessage = findViewById<EditText>(R.id.enterMessage)
        enterStratDate = findViewById<TextView>(R.id.enterStratDate)
        enterEndDate = findViewById<TextView>(R.id.enterEndDate)
        submitLayout = findViewById<LinearLayout>(R.id.submitLayout)
        submitBtn = findViewById<Button>(R.id.submitBtn)

        studentSpinner.setOnClickListener() {
            showStudentList(mContext, studentListArrayList)
        }
        backRelative.setOnClickListener(View.OnClickListener {
            finish()
        })
//        heading.text= ConstantWords.absence
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
                        "Please select First day of absence",
                        "Alert"
                    )
                } else if (enterEndDate.text.equals("")) {
                    CommonMethods.showDialogueWithOk(mContext, "Please select Return day", "Alert")
                } else {
                    if (enterMessage.text.toString().trim().equals("")) {
                        CommonMethods.showDialogueWithOk(
                            mContext,
                            "Please enter reason for your absence",
                            "Alert"
                        )

                    } else {

                        reasonAPI = enterMessage.text.toString().trim()

                        val inputFormat2: DateFormat = SimpleDateFormat("d-m-yyyy")
                        val outputFormat2: DateFormat = SimpleDateFormat("d-m-yyyy")
                        val inputDateStr2 = fromDate
                        val date2: Date = inputFormat2.parse(inputDateStr2)
                        val f_date: String = outputFormat2.format(date2)

                        val inputFormat3: DateFormat = SimpleDateFormat("d-m-yyyy")
                        val outputFormat3: DateFormat = SimpleDateFormat("d-m-yyyy")
                        val inputDateStr3 = toDate
                        val date3: Date = inputFormat3.parse(inputDateStr3)
                        val t_date: String = outputFormat3.format(date3)

                        callAbsenceSubmitApi(f_date, t_date, reasonAPI)

                    }
                }
            }
        })
        enterStratDate.setOnClickListener {
            cal()

        }

        enterEndDate.setOnClickListener {
            if (enterStratDate.text.equals("")) {
                CommonMethods.showDialogueWithOk(
                    mContext,
                    "Please select First day of absence",
                    "Alert"
                )

            } else {
                calToDate()
            }


        }
    }

    fun callStudentListApi() {
        studentListArrayList = ArrayList()
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
                    studentListArrayList = ArrayList()
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

    fun callAbsenceSubmitApi(from: String, toDate: String, reason: String) {

        var new_fromdate: String = ""
        val inputFormat: DateFormat = SimpleDateFormat("d-m-yyyy")
        val outputFormat: DateFormat = SimpleDateFormat("yyyy-mm-dd")
        val inputDateStr = from
        val date: Date = inputFormat.parse(inputDateStr)
        new_fromdate = outputFormat.format(date)
        var new_todate: String = ""
        val inputFormat2: DateFormat = SimpleDateFormat("d-m-yyyy")
        val outputFormat2: DateFormat = SimpleDateFormat("yyyy-mm-dd")
        val inputDateStr2 = toDate
        val date2: Date = inputFormat2.parse(inputDateStr2)
        new_todate = outputFormat2.format(date2)
        val token = PreferenceManager.getUserCode(mContext)
        val paramObject = JsonObject()
        paramObject.addProperty("student_id", PreferenceManager.getStudentID(mContext))
        paramObject.addProperty("from_date", new_fromdate)
        paramObject.addProperty("to_date", new_todate)
        paramObject.addProperty("reason", reason)
        progressBar.show()
        val call: Call<GeneralSubmitResponseModel> =
            ApiClient.getClient.requestAbsence("Bearer " + token, paramObject)
        call.enqueue(object : Callback<GeneralSubmitResponseModel> {
            override fun onFailure(call: Call<GeneralSubmitResponseModel>, t: Throwable) {
                //mProgressRelLayout.visibility=View.INVISIBLE
                progressBar.dismiss()
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
                                "Successfully submitted your absence.",
                                mContext
                            )
                            //Toast.makeText(nContext, "Transaction successfully completed", Toast.LENGTH_SHORT).show()

                        } else if (response.body()!!.status == 136) {
                            CommonMethods.showDialogueWithOk(
                                mContext,
                                "Date already registered!",
                                "Alert"
                            )

                        } else if (response.body()!!.status == 103) {
                            CommonMethods.showDialogueWithOk(
                                mContext,
                                "Some Error Occurred!",
                                "Alert"
                            )

                        } else {
                            CommonMethods.showDialogueWithOk(
                                mContext,
                                "Some error occurred!",
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
        val dpd1 = DatePickerDialog(this, R.style.MyDatePickerStyle,
            object : DatePickerDialog.OnDateSetListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    var firstday: String? = String.format("%d/%d/%d", month + 1, dayOfMonth, year)
                    var date_sel: String? = String.format("%d-%d-%d", dayOfMonth, month + 1, year)
                    var date_temp = date_sel.toString()

                    val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
                    val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
                    val inputDateStr = date_temp
                    val date: Date = inputFormat.parse(inputDateStr)
                    val outputDateStr: String = outputFormat.format(date)
                    fromDate = date_sel.toString()
                    enterStratDate.text = outputDateStr

                }
            }, year, month, day
        )

        dpd1.datePicker.minDate = android.icu.util.Calendar.getInstance().timeInMillis
        dpd1.show()

    }

    private fun calToDate() {
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
                    var firstday: String? = String.format("%d/%d/%d", month + 1, dayOfMonth, year)
                    var date_sel: String? = String.format("%d-%d-%d", dayOfMonth, month + 1, year)
                    var date_temp = date_sel.toString()

                    val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
                    val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
                    val inputDateStr = date_temp
                    val date: Date = inputFormat.parse(inputDateStr)
                    val outputDateStr: String = outputFormat.format(date)
                    toDate = date_sel.toString()
                    enterEndDate.text = outputDateStr

                }
            },
            year,
            month,
            day
        )

        dpd1.datePicker.minDate = android.icu.util.Calendar.getInstance().timeInMillis
        dpd1.show()

    }


}