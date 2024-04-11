package com.nas.naisak.fragment.absence

import android.app.Dialog
import android.content.Context
import android.content.Intent
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
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.nas.naisak.R
import com.nas.naisak.activity.absence_and_early_pick_up.AbsenceDetailActivity
import com.nas.naisak.activity.absence_and_early_pick_up.EarlyPickUpDetailActivity
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
import kotlin.properties.Delegates


class AbsenceEarlyPickUpFragment : Fragment() {
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
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var progressDialogAdd: ProgressBar
    lateinit var studentSpinner: LinearLayout
    var studentListArrayList = ArrayList<StudentDataListResponse>()
    lateinit var studImg: ImageView
    lateinit var studentName: String
    var studentId by Delegates.notNull<Int>()
    lateinit var studentImg: String
    lateinit var studentClass: String
    lateinit var studentNameTxt: TextView
    lateinit var newRequestAbsence: TextView
    lateinit var newRequestPickup: TextView
    lateinit var mAbsenceListView: RecyclerView
    lateinit var mPickupListView: RecyclerView
    lateinit var pickup_list: ArrayList<EarlyPickupListArray>
    lateinit var pickupListSort: ArrayList<EarlyPickupListArray>
    lateinit var studentAbsenceCopy: ArrayList<AbsenceRequestListModel>
    var studentAbsenceArrayList = ArrayList<AbsenceRequestListModel>()

    lateinit var absence_btn: TextView
    lateinit var pickup_btn: TextView
    lateinit var heading: TextView
    lateinit var titleTextView: TextView

    var select_val: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_absence_early_pick_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mContext = requireContext()

        initializeUI()
        if (CommonMethods.isInternetAvailable(mContext)) {
            callStudentListApi()
        } else {
            Toast.makeText(mContext, "Network Error!", Toast.LENGTH_SHORT).show()
            //DialogFunctions.showInternetAlertDialog(mContext)
        }

        selectCategory()
    }

    private fun selectCategory() {
        absence_btn.setOnClickListener {
            progressDialogAdd.visibility = View.VISIBLE
            select_val = 0
            callStudentLeaveInfo()
            absence_btn.setBackgroundResource(R.drawable.event_spinnerfill)
            absence_btn.setTextColor(Color.BLACK)
            pickup_btn.setBackgroundResource(R.drawable.event_greyfill)
            pickup_btn.setTextColor(Color.BLACK)
            heading.text = "App Registered Absences"
            mAbsenceListView.visibility = View.VISIBLE
            newRequestAbsence.visibility = View.VISIBLE
            mPickupListView.visibility = View.GONE
            newRequestPickup.visibility = View.GONE

        }
        pickup_btn.setOnClickListener {
            progressDialogAdd.visibility = View.VISIBLE
            select_val = 1
            if (CommonMethods.isInternetAvailable(mContext)) {
                callpickuplist_api()
            } else {
//                DialogFunctions.showInternetAlertDialog(mContext)
                Toast.makeText(context, "Network Error!", Toast.LENGTH_SHORT).show()
            }

            absence_btn.setBackgroundResource(R.drawable.event_greyfill)
            absence_btn.setTextColor(Color.BLACK)
            pickup_btn.setBackgroundResource(R.drawable.event_spinnerfill)
            pickup_btn.setTextColor(Color.BLACK)
            heading.text = "App Registered Early Pickup"
            mAbsenceListView.visibility = View.GONE
            newRequestAbsence.visibility = View.GONE
            mPickupListView.visibility = View.VISIBLE
            newRequestPickup.visibility = View.VISIBLE
            mPickupListView.layoutManager = LinearLayoutManager(mContext)
            var pickuplistAdapter = PickuplistAdapter(mContext, pickup_list)
            mPickupListView.adapter = pickuplistAdapter
        }
        newRequestPickup.setOnClickListener {
            val intent = Intent(activity, RequestearlypickupActivity::class.java)
            activity?.startActivity(intent)
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
                    context!!.startActivity(mIntent)

                } else {

                }
            }
        })
    }

    private fun callStudentLeaveInfo() {
        studentAbsenceCopy = ArrayList<AbsenceRequestListModel>()
        studentAbsenceArrayList.clear()
        mAbsenceListView.visibility = View.GONE
        progressDialogAdd.visibility = View.VISIBLE
        val studentInfoAdapter = RequestAbsenceRecyclerAdapter(studentAbsenceArrayList)
        mAbsenceListView.adapter = studentInfoAdapter
        val token = PreferenceManager.getaccesstoken(mContext)
        val pickupSuccessBody =
            ListAbsenceApiModel(PreferenceManager.getStudentID(mContext).toString(), 0, 20)
        val call: Call<AbsenceListModel> =
            ApiClient.getClient.absencelist(pickupSuccessBody, "Bearer " + token)
        call.enqueue(object : Callback<AbsenceListModel> {
            override fun onFailure(call: Call<AbsenceListModel>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
                progressDialogAdd.visibility = View.GONE
                //mProgressRelLayout.visibility=View.INVISIBLE
            }

            override fun onResponse(
                call: Call<AbsenceListModel>,
                response: Response<AbsenceListModel>
            ) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                Log.e("Response Signup", responsedata.toString())
                progressDialogAdd.visibility = View.GONE
                if (responsedata != null) {
                    try {

                        if (response.body()!!.status == 100) {
                            studentAbsenceCopy.addAll(response.body()!!.responseArray.request)
                            studentAbsenceArrayList = studentAbsenceCopy

                            if (studentAbsenceArrayList.size > 0) {
                                mAbsenceListView.visibility = View.VISIBLE
                                val studentInfoAdapter =
                                    RequestAbsenceRecyclerAdapter(studentAbsenceArrayList)
                                mAbsenceListView.adapter = studentInfoAdapter
                            } else {
                                Toast.makeText(
                                    mContext,
                                    "No Registered Absence Found",
                                    Toast.LENGTH_SHORT
                                ).show()
                                mAbsenceListView.visibility = View.GONE
                            }


                        } else if (response.body()!!.status == 103) {
                            callStudentLeaveInfo()
                        } else {

                            DialogFunctions.commonErrorAlertDialog(
                                mContext.resources.getString(R.string.alert),
                                ConstantFunctions.commonErrorString(response.body()!!.status),
                                mContext
                            )
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })


    }

    private fun callpickuplist_api() {
        pickupListSort = ArrayList()
        pickup_list = ArrayList()
        progressDialogAdd.visibility = View.VISIBLE
        val token = PreferenceManager.getaccesstoken(mContext)
        val pickupSuccessBody =
            ListPickupApiModel(PreferenceManager.getStudentID(mContext).toString(), "0", "20")
        val call: Call<PickupListModel> =
            ApiClient.getClient.pickUplist(pickupSuccessBody, "Bearer " + token)
        call.enqueue(object : Callback<PickupListModel> {
            override fun onFailure(call: Call<PickupListModel>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
                progressDialogAdd.visibility = View.GONE
                //mProgressRelLayout.visibility=View.INVISIBLE
            }

            override fun onResponse(
                call: Call<PickupListModel>,
                response: Response<PickupListModel>
            ) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                Log.e("Response Signup", responsedata.toString())

                if (responsedata != null) {
                    try {

                        if (response.body()!!.status == 100) {

                            pickup_list.addAll(response.body()!!.pickupListArray)
                            progressDialogAdd.visibility = View.GONE
                            mPickupListView.visibility = View.VISIBLE
                            var list_size = pickup_list.size - 1
                            pickupListSort = ArrayList()
                            if (pickup_list.size > 0) {
                                mPickupListView.layoutManager = LinearLayoutManager(mContext)
                                var pickuplistAdapter = PickuplistAdapter(mContext, pickup_list)
                                mPickupListView.adapter = pickuplistAdapter
                            } else {
                                mPickupListView.layoutManager = LinearLayoutManager(mContext)
                                var pickuplistAdapter = PickuplistAdapter(mContext, pickup_list)
                                mPickupListView.adapter = pickuplistAdapter
                                Toast.makeText(
                                    mContext,
                                    "No Registered Early Pickup Found",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {

                            DialogFunctions.commonErrorAlertDialog(
                                mContext.resources.getString(R.string.alert),
                                ConstantFunctions.commonErrorString(response.body()!!.status),
                                mContext
                            )
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })

    }


    private fun initializeUI() {
        progressDialogAdd = requireView().findViewById(R.id.progressDialogAdd)
        progressDialogAdd.visibility = View.VISIBLE
        studentSpinner = requireView().findViewById<LinearLayout>(R.id.studentSpinner)
        studImg = requireView().findViewById<ImageView>(R.id.imagicon)
        studentNameTxt = requireView().findViewById<TextView>(R.id.studentName)
        titleTextView = requireView().findViewById(R.id.titleTextView)
        titleTextView.text = "Absence & Early Pick-Up"
        newRequestAbsence = requireView().findViewById(R.id.newRequestAbsence)
        newRequestPickup = requireView().findViewById(R.id.newRequestEarly)
        mAbsenceListView = requireView().findViewById(R.id.mAbsenceListView) as RecyclerView
        mPickupListView = requireView().findViewById(R.id.mPickupListView)
        pickup_list = ArrayList()
        pickupListSort = ArrayList()
        heading = requireView().findViewById(R.id.appregisteredHint)
        absence_btn = requireView().findViewById(R.id.absenc_btn)
        absence_btn.setBackgroundResource(R.color.colorAccent)
        pickup_btn = requireView().findViewById(R.id.earlypickup_btn)
        linearLayoutManager = LinearLayoutManager(mContext)
        mAbsenceListView.layoutManager = linearLayoutManager
        mAbsenceListView.itemAnimator = DefaultItemAnimator()
        absence_btn.setBackgroundResource(R.drawable.event_spinnerfill)
        studentSpinner.setOnClickListener {
            showStudentList(mContext, studentListArrayList)
        }
        mAbsenceListView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                val intent = Intent(activity, AbsenceDetailActivity::class.java)
                intent.putExtra("studentName", PreferenceManager.getStudentName(mContext))
                intent.putExtra("studentClass", PreferenceManager.getStudentClass(mContext))
                intent.putExtra("fromDate", studentAbsenceArrayList.get(position).from_date)
                intent.putExtra("toDate", studentAbsenceArrayList.get(position).to_date)
                intent.putExtra("reason", studentAbsenceArrayList.get(position).reason)
                activity?.startActivity(intent)
            }
        })
        mPickupListView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                val intent = Intent(activity, EarlyPickUpDetailActivity::class.java)
                intent.putExtra("studentName", PreferenceManager.getStudentName(mContext))
                intent.putExtra("studentClass", PreferenceManager.getStudentClass(mContext))
                intent.putExtra("date", pickup_list.get(position).pickup_date)
                intent.putExtra("time", pickup_list.get(position).pickup_time)
                intent.putExtra("pickupby", pickup_list.get(position).pickup_by_whom)
                intent.putExtra("reason", pickup_list.get(position).reason)
                intent.putExtra("status", pickup_list.get(position).status)
                intent.putExtra(
                    "reason_for_rejection",
                    pickup_list.get(position).reason_for_rejection
                )
                activity?.startActivity(intent)
            }
        })
        newRequestAbsence.setOnClickListener(View.OnClickListener {
            showSuccessmailAlert(
                mContext,
                "For planned absences please email your head of school", "Alert"
            )


        })
    }

    fun showSuccessmailAlert(context: Context, message: String, msgHead: String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_ok_cancel)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        var btn_Cancel = dialog.findViewById(R.id.btn_Cancel) as Button
        text_dialog.text = message
        alertHead.text = msgHead
        iconImageView.setImageResource(R.drawable.exclamationicon)
        btn_Ok.setOnClickListener()
        {
            dialog.dismiss()
            val intent = Intent(activity, RequestabsenceActivity::class.java)
            activity?.startActivity(intent)
        }
        btn_Cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onResume() {
        super.onResume()

        mPickupListView.visibility = View.GONE
        mAbsenceListView.visibility = View.GONE
        studentNameTxt.text = PreferenceManager.getStudentName(mContext)
        studentId = PreferenceManager.getStudentID(mContext)
        studentImg = PreferenceManager.getStudentPhoto(mContext)!!
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
        if (select_val == 0) {
            progressDialogAdd.visibility = View.VISIBLE
            callStudentLeaveInfo()
        } else if (select_val == 1) {
            progressDialogAdd.visibility = View.VISIBLE
            if (CommonMethods.isInternetAvailable(mContext)) {
                callpickuplist_api()
            } else {
                //DialogFunctions.showInternetAlertDialog(mContext)
            }

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
//                if (CommonMethods.isInternetAvailable(context)) {
//                    callTripList()
//                }
//                else
//                {
//                    CommonMethods.showSuccessInternetAlert(context)
//                }
                dialog.dismiss()
            }
        })
        dialog.show()
    }


}