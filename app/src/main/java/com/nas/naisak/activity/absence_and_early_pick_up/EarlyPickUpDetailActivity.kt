package com.nas.naisak.activity.absence_and_early_pick_up

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nas.naisak.R
import com.nas.naisak.activity.home.HomeActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class EarlyPickUpDetailActivity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var backRelative: RelativeLayout
    lateinit var heading: TextView
    lateinit var logoClickImgView: ImageView
    lateinit var stud_name: TextView
    lateinit var stud_class: TextView
    lateinit var dateofPickup: TextView
    lateinit var timeofPickup: TextView
    lateinit var pickup_name: TextView
    lateinit var reasonTxt: TextView
    lateinit var reasonRejectionTxt: TextView
    lateinit var reasonRejectionLinear: LinearLayout
    lateinit var reasonRejectionScroll: ScrollView
    lateinit var status_txt: TextView
    var day_pickup: String = ""
    var time_pickup: String = ""
    var studname_pickup: String = ""
    var studcls_pickup: String = ""
    var pickby_pickup: String = ""
    var reason_pickup: String = ""
    var status_pickup: String = ""
    var reason_for_rejection: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_early_pick_up_detail)
        mContext = this
        initfn()
    }

    private fun initfn() {
        heading = findViewById(R.id.heading)
//        heading.text= ConstantWords.earlypickup
        backRelative = findViewById(R.id.backRelative)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        timeofPickup = findViewById(R.id.leaveDateToValue)
        day_pickup = intent.getStringExtra("date").toString()
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val inputDateStr = day_pickup
        val date: Date = inputFormat.parse(inputDateStr)
        val outputDateStr: String = outputFormat.format(date)

        time_pickup = intent.getStringExtra("time").toString()
        val inFormat: DateFormat = SimpleDateFormat("hh:mm:ss")
        val outFormat: DateFormat = SimpleDateFormat("hh:mm aa")
        val inputTimeStr = time_pickup
        val time: Date = inFormat.parse(inputTimeStr)
        val outputTimeStr: String = outFormat.format(time)
        timeofPickup.text = outputTimeStr


        studname_pickup = intent.getStringExtra("studentName").toString()
        studcls_pickup = intent.getStringExtra("studentClass").toString()
        pickby_pickup = intent.getStringExtra("pickupby").toString()
        reason_pickup = intent.getStringExtra("reason").toString()
        reason_for_rejection = intent.getStringExtra("reason_for_rejection").toString()
        status_pickup = intent.getIntExtra("status", 0).toString()
        stud_name = findViewById(R.id.stnameValue)
        stud_name.text = studname_pickup
        status_txt = findViewById(R.id.status)
        stud_class = findViewById(R.id.studClassValue)
        stud_class.text = studcls_pickup
        dateofPickup = findViewById(R.id.leaveDateFromValue)
        dateofPickup.text = outputDateStr
        reasonRejectionLinear = findViewById(R.id.reasonRejectlayout)
        reasonRejectionScroll = findViewById(R.id.reasonRejectionScroll)
        pickup_name = findViewById(R.id.pickupbyName)
        pickup_name.text = pickby_pickup
        reasonTxt = findViewById(R.id.reasonValue)
        reasonTxt.text = reason_pickup
        reasonRejectionTxt = findViewById(R.id.reasonRejectValue)

        if (status_pickup.equals("1")) {
            status_txt.text = "PENDING"
            reasonRejectionLinear.visibility = View.GONE
            reasonRejectionScroll.visibility = View.GONE
        } else if (status_pickup.equals("2")) {
            status_txt.text = "APPROVED"
            reasonRejectionLinear.visibility = View.GONE
            reasonRejectionScroll.visibility = View.GONE
        } else {
            status_txt.text = "REJECTED"
            reasonRejectionLinear.visibility = View.VISIBLE
            reasonRejectionScroll.visibility = View.VISIBLE
            reasonRejectionTxt.text = reason_for_rejection

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
    }

}