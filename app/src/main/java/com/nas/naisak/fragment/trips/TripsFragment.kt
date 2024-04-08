package com.nas.naisak.fragment.trips

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.nas.naisak.R
import com.nas.naisak.activity.payment.information.PaymentInformationActivity
import com.nas.naisak.activity.trips.TripCategoriesActivity
import com.nas.naisak.activity.trips.TripInfoActivity
import com.nas.naisak.activity.trips.TripPaymentsActivity
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.ProgressBarDialog

class TripsFragment : Fragment() {
    private lateinit var mTitle: String
    private lateinit var mTabId: String
    private lateinit var mContext: Context
    private lateinit var progressDialogP: ProgressBarDialog
    private lateinit var progressDialog: ProgressBar
    private lateinit var mRootView: View
    private lateinit var text_content: TextView
    private lateinit var text_dialog: TextView
    private var description: String = ""
    private var contactEmail: String = ""
    private lateinit var signRelative: RelativeLayout
    private lateinit var sendEmail: ImageView
    private var tab_type: String = ""
    private lateinit var signUpModule: TextView
    private lateinit var descriptionTitle: TextView
    private lateinit var bannerUrlImageArray: ArrayList<String>
    private lateinit var mtitle: RelativeLayout
    private lateinit var bannerImagePager: ImageView
//    private lateinit var parentAssociationEventsModelsArrayList: ArrayList<ParentAssociationEventsModel>
    private lateinit var paymentLinear: LinearLayout
    private lateinit var registerTripLinear: LinearLayout
    private lateinit var informationLinear: LinearLayout
//    private lateinit var mAnswerList: ArrayList<AnswerSubmitModel>
    private var isemoji1Selected: Boolean = false
    private var isemoji2Selected: Boolean = false
    private var isemoji3Selected: Boolean = false
    private var survey_satisfation_status: Int = 0
    private var currentPage: Int = 0
    private var currentPageSurvey: Int = 0
    private var surveySize: Int = 0
    private var isShown: Boolean = false
    private var pos: Int = -1
//    private lateinit var surveyArrayList: ArrayList<SurveyModel>
//    private lateinit var surveyQuestionArrayList: ArrayList<SurveyQuestionsModel>
//    private lateinit var surveyAnswersArrayList: ArrayList<SurveyAnswersModel>
    private lateinit var studeLinear: ConstraintLayout
    private val EMAIL_PATTERN: String = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$"
    private val pattern: String = "^([a-zA-Z ]*)$"

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
        return mRootView

    }

}