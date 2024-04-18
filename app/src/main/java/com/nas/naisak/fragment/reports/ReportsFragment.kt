package com.nas.naisak.fragment.reports

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.nas.naisak.R
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReportsFragment : Fragment() {
    lateinit var progressBar: ProgressBar
    lateinit var studentName: String
    lateinit var studentId: Int
    lateinit var titleTextView: TextView
    lateinit var studentImg: String
    lateinit var studentClass: String
    lateinit var studentSpinner: LinearLayout
    lateinit var studImg: ImageView
    lateinit var studentNameTxt: TextView
    lateinit var reportsRecycler: RecyclerView
    lateinit var sharedprefs: PreferenceManager
    lateinit var mContext: Context
    private lateinit var linearLayoutManager: LinearLayoutManager
    var studentListArrayList = ArrayList<StudentList>()
    var reportArrayList = ArrayList<ReportListDetailModel>()
    var apiCall: Int = 0
    var apiCallDetail: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reports, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = requireContext()
        sharedprefs = PreferenceManager()
        initializeUI()
        if (CommonMethods.isInternetAvailable(mContext)) {
            callStudentListApi()
        } else {
            DialogFunctions.showInternetAlertDialog(mContext)
        }

    }

    fun callStudentListApi() {
        // progressDialogAdd.visibility=View.VISIBLE
        progressBar.visibility = View.VISIBLE

        studentListArrayList = java.util.ArrayList()
        val call: Call<StudentListModel> =
            ApiClient.getClient.studentList("Bearer " + PreferenceManager.getaccesstoken(mContext))
        call.enqueue(object : Callback<StudentListModel> {
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
                // progressDialogAdd.visibility=View.GONE
                progressBar.visibility = View.GONE

            }

            override fun onResponse(
                call: Call<StudentListModel>,
                response: Response<StudentListModel>
            ) {
                val responsedata = response.body()
                // progressDialogAdd.visibility=View.GONE
                progressBar.visibility = View.VISIBLE

                if (responsedata != null) {
                    try {

                        if (response.body()!!.status == 100) {

                            studentListArrayList = ArrayList()
                            studentListArrayList.addAll(response.body()!!.responseArray.studentList)
                            if (PreferenceManager.getStudentID(mContext).equals("")) {
                                Log.e("Empty Img", "Empty")
                                studentName = studentListArrayList.get(0).name
                                studentImg = studentListArrayList.get(0).photo
                                studentId = studentListArrayList.get(0).id
                                studentClass = studentListArrayList.get(0).section
                                PreferenceManager.setStudentID(mContext, studentId)
                                PreferenceManager.setStudentName(mContext, studentName)
                                PreferenceManager.setStudentPhoto(mContext, studentImg)
                                PreferenceManager.setStudentClass(mContext, studentClass)
                                studentNameTxt.text = studentName
                                Log.e(
                                    "studid(0)",
                                    PreferenceManager.getStudentID(mContext).toString()
                                )
                                if (!studentImg.equals("")) {
                                    Glide.with(mContext) //1
                                        .load(studentImg)
                                        .placeholder(R.drawable.student)
                                        .error(R.drawable.student)
                                        .skipMemoryCache(true) //2
                                        .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                                        .transform(CircleCrop()) //4
                                        .into(studImg)
                                } else {
                                    studImg.setImageResource(R.drawable.student)
                                }

                            } else {
                                studentName = PreferenceManager.getStudentName(mContext)!!
                                studentImg = PreferenceManager.getStudentPhoto(mContext)!!
                                studentId = PreferenceManager.getStudentID(mContext)!!
                                studentClass = PreferenceManager.getStudentClass(mContext)!!
                                studentNameTxt.text = studentName
                                if (!studentImg.equals("")) {
                                    Glide.with(mContext) //1
                                        .load(studentImg)
                                        .placeholder(R.drawable.student)
                                        .error(R.drawable.student)
                                        .skipMemoryCache(true) //2
                                        .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                                        .transform(CircleCrop()) //4
                                        .into(studImg)
                                } else {
                                    studImg.setImageResource(R.drawable.student)
                                }
                            }
                            if (CommonMethods.isInternetAvailable(mContext)) {
                                getreportsdetails()
                            } else {
                                DialogFunctions.showInternetAlertDialog(mContext)
                            }

                        }
                        if (response.body()!!.status == 116) {
                            if (apiCall != 4) {
                                apiCall = apiCall + 1
                                callStudentListApi()
                            } else {
                                progressBar.visibility = View.GONE

                                showSuccessAlert(
                                    mContext,
                                    "Something went wrong.Please try again later",
                                    "Alert"
                                )

                            }
                        } else {
                            //DialogFunctions.commonErrorAlertDialog(mContext.resources.getString(R.string.alert), ConstantFunctions.commonErrorString(response.body()!!.status), mContext)

                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }

    private fun initializeUI() {
        // progressDialog = requireView().findViewById(R.id.progressDialog) as RelativeLayout
        progressBar = requireView().findViewById(R.id.progress)

        studentSpinner = requireView().findViewById(R.id.studentSpinner) as LinearLayout
        studImg = requireView().findViewById(R.id.imagicon) as ImageView
        studentNameTxt = requireView().findViewById(R.id.studentName) as TextView
        titleTextView = requireView().findViewById(R.id.titleTextView) as TextView
        reportsRecycler = requireView().findViewById(R.id.recycler_view_list) as RecyclerView


        titleTextView.text = "Reports"



        linearLayoutManager = LinearLayoutManager(mContext)
        reportsRecycler.layoutManager = linearLayoutManager
        reportsRecycler.itemAnimator = DefaultItemAnimator()

        reportsRecycler.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

            }

        })
        studentSpinner.setOnClickListener {

            showStudentList(mContext, studentListArrayList)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun showStudentList(context: Context, mStudentList: ArrayList<StudentList>) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialogue_student_list)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var btn_dismiss = dialog.findViewById(R.id.btn_dismiss) as Button
        var studentListRecycler = dialog.findViewById(R.id.studentListRecycler) as RecyclerView
        iconImageView.setImageResource(R.drawable.boy)
        //if(mSocialMediaArray.get())
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            btn_dismiss.setBackgroundDrawable(
                mContext.resources.getDrawable(R.drawable.button_new)
            )
        } else {
            btn_dismiss.background = mContext.resources.getDrawable(R.drawable.button_new)
        }

        studentListRecycler.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        studentListRecycler.layoutManager = llm
        val studentAdapter = StudentListAdapter(mContext, mStudentList)
        studentListRecycler.adapter = studentAdapter
        btn_dismiss.setOnClickListener()
        {
            dialog.dismiss()
        }
        studentListRecycler.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                studentName = studentListArrayList[position].name
                studentImg = studentListArrayList[position].photo
                studentId = studentListArrayList[position].id
                studentClass = studentListArrayList[position].section
                PreferenceManager.setStudentID(mContext, studentId)
                PreferenceManager.setStudentName(mContext, studentName)
                PreferenceManager.setStudentPhoto(mContext, studentImg)
                PreferenceManager.setStudentClass(mContext, studentClass)
                studentNameTxt.text = studentName
                if (studentImg != "") {
                    Glide.with(mContext) //1
                        .load(studentImg)
                        .placeholder(R.drawable.student)
                        .error(R.drawable.student)
                        .skipMemoryCache(true) //2
                        .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                        .transform(CircleCrop()) //4
                        .into(studImg)
                } else {
                    studImg.setImageResource(R.drawable.student)
                }
                progressBar.visibility = View.VISIBLE
                getreportsdetails()
                dialog.dismiss()
            }
        })
        dialog.show()
    }

    private fun getreportsdetails() {
        progressBar.visibility = View.VISIBLE
        val token = PreferenceManager.getaccesstoken(mContext)
        val studentid = ReportApiModel(PreferenceManager.getStudentID(mContext)!!)
        val call: Call<ReportListModel> =
            ApiClient.getClient.reportList(studentid, "Bearer " + token)
        call.enqueue(object : Callback<ReportListModel> {
            override fun onFailure(call: Call<ReportListModel>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("Error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<ReportListModel>,
                response: Response<ReportListModel>
            ) {
                progressBar.visibility = View.GONE
                when (response.body()!!.status) {
                    100 -> {


//                        val reportlist: List<ReportResponseArray> = response.body()!!.data
                        val datas: ArrayList<ReportDetailModel> =
                            response.body()!!.responseArray.data as ArrayList<ReportDetailModel>
                        if (datas.size > 0) {
                            reportsRecycler.visibility = View.VISIBLE
                            val rAdapter: ReportListRecyclerAdapter =
                                ReportListRecyclerAdapter(mContext, datas)
                            reportsRecycler.adapter = rAdapter
                        } else {
                            reportsRecycler.visibility = View.GONE


                            showSuccessAlert(mContext, "No reports available.", "Alert")
                        }

                    }

                    132 -> {
                        showSuccessAlert(mContext, "No reports available.", "Alert")
                    }

                    116 -> {
                        if (apiCallDetail != 4) {
                            apiCallDetail = apiCallDetail + 1
                            // AccessTokenClass.getAccessToken(mContext)
                            getreportsdetails()
                        } else {
                            progressBar.visibility = View.GONE
                            showSuccessAlert(
                                mContext,
                                "Something went wrong.Please try again later",
                                "Alert"
                            )
                        }

                    }

                    else -> {
                        DialogFunctions.commonErrorAlertDialog(
                            mContext.resources.getString(R.string.alert),
                            ConstantFunctions.commonErrorString(response.body()!!.status),
                            mContext
                        )
                    }
                }
            }

        })
    }

    fun showSuccessAlert(context: Context, message: String, msgHead: String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_common_error_alert)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as? ImageView
        var alertHead = dialog.findViewById(R.id.alertHead) as? TextView
        var text_dialog = dialog.findViewById(R.id.messageTxt) as? TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        text_dialog?.text = message
        alertHead?.text = msgHead
        btn_Ok.setOnClickListener()
        {
            dialog.dismiss()
        }
        dialog.show()
    }
}