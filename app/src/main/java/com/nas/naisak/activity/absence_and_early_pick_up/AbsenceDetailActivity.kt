package com.nas.naisak.activity.absence_and_early_pick_up

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nas.naisak.R
import com.nas.naisak.activity.home.HomeActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class AbsenceDetailActivity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var backRelative: RelativeLayout
    lateinit var heading: TextView
    lateinit var logoClickImgView: ImageView
    lateinit var stnameValue: TextView
    lateinit var studClassValue: TextView
    lateinit var leaveDateFromValue: TextView
    lateinit var leaveDateToValue: TextView
    lateinit var reasonValue: TextView
    var reason: String? = ""
    var studentName: String? = ""
    var studentClass: String? = ""
    var fromDate: String? = ""
    var toDate: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absence_detail)
        mContext = this
        initfn()
    }

    private fun initfn() {
        heading = findViewById(R.id.heading)
//        heading.text= ConstantWords.earlypickup
        backRelative = findViewById(R.id.backRelative)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        reason = intent.getStringExtra("reason")
        studentName = intent.getStringExtra("studentName")
        studentClass = intent.getStringExtra("studentClass")
        fromDate = intent.getStringExtra("fromDate")
        toDate = intent.getStringExtra("toDate")
        stnameValue = findViewById<TextView>(R.id.stnameValue)
        studClassValue = findViewById<TextView>(R.id.studClassValue)
        leaveDateFromValue = findViewById<TextView>(R.id.leaveDateFromValue)
        leaveDateToValue = findViewById<TextView>(R.id.leaveDateToValue)
        reasonValue = findViewById<TextView>(R.id.reasonValue)
        stnameValue.text = studentName
        studClassValue.text = studentClass
        val fromdate = fromDate
        val todate = toDate
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
        val inputDateStr = fromdate
        val date: Date = inputFormat.parse(inputDateStr)
        val outputDateStr: String = outputFormat.format(date)




        if (todate != "") {
            val inputFormat1: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat1: DateFormat = SimpleDateFormat("dd MMMM yyyy")
            val inputDateStr1 = todate
            val date1: Date = inputFormat1.parse(inputDateStr1)
            val outputDateStr1: String = outputFormat1.format(date1)
            leaveDateToValue.text = outputDateStr1
            leaveDateFromValue.text = outputDateStr
            reasonValue.text = reason

        } else {
            leaveDateFromValue.text = outputDateStr
            reasonValue.text = reason
            leaveDateToValue.text = "-"

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
    }

}