package com.nas.naisak.fragment.reports

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
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.gson.JsonObject
import com.nas.naisak.R
import com.nas.naisak.activity.cca.adapter.StrudentSpinnerAdapter
import com.nas.naisak.commonadapters.StudentListAdapter
import com.nas.naisak.commonmodels.StudentListReponseModel
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.ProgressBarDialog
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener
import com.nas.naisak.fragment.reports.adapter.ReportListRecyclerAdapter
import com.nas.naisak.fragment.reports.model.ReportsResponseModel
import kotlinx.android.synthetic.main.activity_cca.textViewYear
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReportsFragment : Fragment() {
    lateinit var progressBar: ProgressBar
    lateinit var studentName: String
    var studentId: Int = 0
    lateinit var titleTextView: TextView
    lateinit var studentImg: String
    lateinit var studentClass: String
    lateinit var studentSpinner: LinearLayout
    lateinit var studImg: ImageView
    lateinit var studentNameTxt: TextView
    lateinit var reportsRecycler: RecyclerView
    lateinit var sharedprefs: PreferenceManager
    lateinit var progressBarDialog: ProgressBarDialog
    lateinit var mContext: Context
    private lateinit var linearLayoutManager: LinearLayoutManager
    var stud_id = ""
    var stud_img = ""
    var stud_class = ""
    var stud_name = ""

    //    var studentListArrayList = ArrayList<StudentList>()
    var studentsModelArrayList: ArrayList<StudentListReponseModel.Data.Lists> = ArrayList()

    //    var reportArrayList = ArrayList<ReportListDetailModel>()
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
            getStudentList()
        } else {
            Toast.makeText(mContext, "No internet available!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getStudentList() {
        studentsModelArrayList = ArrayList()
        val token = PreferenceManager.getUserCode(mContext)
        val call: Call<StudentListReponseModel> =
            ApiClient.getClient.getStudent("Bearer $token")
        progressBarDialog.show()
        call.enqueue(object : Callback<StudentListReponseModel> {
            override fun onResponse(
                call: Call<StudentListReponseModel>,
                response: Response<StudentListReponseModel>
            ) {
                progressBarDialog.dismiss()
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        if (response.body()!!.status.toString() == "100") {
                            if (response.body()!!.data!!.lists!!.size > 0) {

                                for (i in response.body()!!.data!!.lists!!.indices) {
                                    studentsModelArrayList!!.add(response.body()!!.data!!.lists!![i]!!)
                                }
                                if (PreferenceManager.getStudIdForCCA(mContext)
                                        .equals("")
                                ) {
                                    studentNameTxt!!.text = studentsModelArrayList!![0].name
                                    stud_id = studentsModelArrayList!![0].id.toString()
                                    stud_name = studentsModelArrayList!![0].name.toString()
                                    stud_class =
                                        studentsModelArrayList!![0].student_class.toString()
                                    textViewYear!!.text =
                                        "Class : " + studentsModelArrayList!![0].student_class
                                    stud_img = studentsModelArrayList!![0].photo.toString()
                                    if (stud_img != "") {
                                        Glide.with(mContext).load(CommonMethods.replace(stud_img))
                                            .fitCenter()
                                            .placeholder(R.drawable.boy).into(studImg!!)
                                    } else {
                                        studImg!!.setImageResource(R.drawable.boy)
                                    }
                                    PreferenceManager.setCCAStudentIdPosition(mContext, "0")
                                } else {
                                    val studentSelectPosition = Integer.valueOf(
                                        PreferenceManager.getCCAStudentIdPosition(mContext)
                                    )
                                    studentNameTxt!!.text =
                                        studentsModelArrayList!![studentSelectPosition].name
                                    stud_id =
                                        studentsModelArrayList!![studentSelectPosition].id.toString()
                                    stud_name =
                                        studentsModelArrayList!![studentSelectPosition].name.toString()
                                    stud_class =
                                        studentsModelArrayList!![studentSelectPosition].student_class.toString()
                                    textViewYear!!.text =
                                        "Class : " + studentsModelArrayList!![studentSelectPosition].student_class.toString()
                                    stud_img =
                                        studentsModelArrayList!![studentSelectPosition].photo.toString()
                                    if (stud_img != "") {
                                        Glide.with(mContext).load(CommonMethods.replace(stud_img))
                                            .fitCenter()

                                            .placeholder(R.drawable.boy).into(studImg!!)
                                    } else {
                                        studImg!!.setImageResource(R.drawable.boy)
                                    }
                                }
                                getreportsdetails(stud_id)
                            } else {

                                //CustomStatusDialog();
                                Toast.makeText(mContext, "No Student Found.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            CommonMethods.showDialogueWithOk(
                                mContext,
                                getString(R.string.common_error),
                                "Alert"
                            )
                        }
                    } else {
                        CommonMethods.showDialogueWithOk(
                            mContext,
                            getString(R.string.common_error),
                            "Alert"
                        )
                    }
                } else {
                    CommonMethods.showDialogueWithOk(
                        mContext,
                        getString(R.string.common_error),
                        "Alert"
                    )
                }
            }

            override fun onFailure(call: Call<StudentListReponseModel>, t: Throwable) {
                progressBarDialog.dismiss()
                CommonMethods.showDialogueWithOk(
                    mContext,
                    getString(R.string.common_error),
                    "Alert"
                )
            }

        })
    }

    private fun initializeUI() {
        // progressDialog = requireView().findViewById(R.id.progressDialog) as RelativeLayout
        progressBar = requireView().findViewById(R.id.progress)
        progressBarDialog = ProgressBarDialog(mContext, R.drawable.spinner)
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

            showStudentList(mContext, studentsModelArrayList)
        }
    }

    fun showStudentList(
        context: Context,
        mStudentList: ArrayList<StudentListReponseModel.Data.Lists>
    ) {
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
                mContext.resources.getDrawable(R.drawable.button_new)
            )
        } else {
            btn_dismiss.background = mContext.resources.getDrawable(R.drawable.button_new)
        }

        studentListRecycler.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        studentListRecycler.layoutManager = llm
        val studentAdapter = StrudentSpinnerAdapter(mContext, mStudentList)
        studentListRecycler.adapter = studentAdapter
        btn_dismiss.setOnClickListener()
        {
            dialog.dismiss()
        }
        studentListRecycler.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                studentName = studentsModelArrayList!![position]!!.name!!
                studentImg = studentsModelArrayList!![position]!!.photo!!
                studentId = studentsModelArrayList!![position]!!.id!!
                studentClass = studentsModelArrayList!![position]!!.section!!
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
                getreportsdetails(stud_id)
                dialog.dismiss()
            }
        })
        dialog.show()
    }

    private fun getreportsdetails(stud_id: String) {
        progressBar.visibility = View.VISIBLE
        val token = PreferenceManager.getUserCode(mContext)
        val paramObject = JsonObject()
        paramObject.addProperty("student_id", PreferenceManager.getStudentID(mContext))
        val call: Call<ReportsResponseModel> =
            ApiClient.getClient.progressReports("Bearer " + token, paramObject)
        call.enqueue(object : Callback<ReportsResponseModel> {
            override fun onFailure(call: Call<ReportsResponseModel>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("Error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<ReportsResponseModel>,
                response: Response<ReportsResponseModel>
            ) {
                progressBar.visibility = View.GONE
                when (response.body()!!.status) {
                    100 -> {


//                        val reportlist: List<ReportResponseArray> = response.body()!!.data
                        val datas: ArrayList<ReportsResponseModel.ProgressReport> =
                            response.body()!!.data.progressReport
                        if (datas.size > 0) {
                            reportsRecycler.visibility = View.VISIBLE
                            val rAdapter =
                                ReportListRecyclerAdapter(mContext, datas)
                            reportsRecycler.adapter = rAdapter
                        } else {
                            reportsRecycler.visibility = View.GONE

                            CommonMethods.showDialogueWithOk(
                                mContext,
                                "No reports available",
                                "Alert"
                            )
                        }

                    }

                    132 -> {
                        CommonMethods.showDialogueWithOk(mContext, "No reports available", "Alert")
                    }

                    116 -> {
                        if (apiCallDetail != 4) {
                            apiCallDetail = apiCallDetail + 1
                            // AccessTokenClass.getAccessToken(mContext)
                            getreportsdetails(this@ReportsFragment.stud_id)
                        } else {
                            progressBar.visibility = View.GONE

                            CommonMethods.showDialogueWithOk(
                                mContext,
                                "Something went wrong.Please try again later",
                                "Alert"
                            )

                        }

                    }

                    else -> {
//                        DialogFunctions.commonErrorAlertDialog(
//                            mContext.resources.getString(R.string.alert),
//                            ConstantFunctions.commonErrorString(response.body()!!.status),
//                            mContext
//                        )
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
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
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