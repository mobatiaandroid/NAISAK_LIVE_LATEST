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
import com.nas.naisak.constants.PreferenceManager
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

    lateinit var namelayout : LinearLayout
    lateinit var studClassLinear : LinearLayout
    lateinit var fromlayout : LinearLayout
   // lateinit var tolayout : LinearLayout
   // lateinit var tolayoutArab :LinearLayout
    lateinit var fromlayoutArab : LinearLayout
    lateinit var studClassLinearArab : LinearLayout
    lateinit var namelayoutArab : LinearLayout
    lateinit var pickuplayout : LinearLayout
    lateinit var pickuplayoutArab : LinearLayout
    lateinit var statuslayout : LinearLayout
    lateinit var statusLayoutarab : LinearLayout
    lateinit var stnameValueArab : TextView
    lateinit var studClassValueArab : TextView
    lateinit var leaveDateFromValueArab : TextView
    lateinit var statusArab : TextView
    lateinit var pickupbyNameArab : TextView

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
        namelayout = findViewById<LinearLayout>(R.id.namelayout)
        studClassLinear = findViewById<LinearLayout>(R.id.studClassLinear)
        fromlayout = findViewById<LinearLayout>(R.id.fromlayout)
      //  tolayout = findViewById<LinearLayout>(R.id.tolayout)
        //tolayoutArab = findViewById<LinearLayout>(R.id.tolayoutArab)
        fromlayoutArab = findViewById<LinearLayout>(R.id.fromlayoutArab)
        studClassLinearArab= findViewById<LinearLayout>(R.id.studClassLinearArab)
        namelayoutArab = findViewById<LinearLayout>(R.id.namelayoutArab)

        pickuplayout= findViewById<LinearLayout>(R.id.pickuplayout)
        pickuplayoutArab= findViewById<LinearLayout>(R.id.pickuplayoutArab)
        statuslayout= findViewById<LinearLayout>(R.id.statuslayout)
        statusLayoutarab= findViewById<LinearLayout>(R.id.statusLayoutarab)

        stnameValueArab= findViewById<TextView>(R.id.stnameValueArab)
        studClassValueArab = findViewById<TextView>(R.id.studClassValueArab)
        leaveDateFromValueArab = findViewById<TextView>(R.id.leaveDateFromValueArab)

        statusArab= findViewById<TextView>(R.id.statusArab)
        pickupbyNameArab = findViewById<TextView>(R.id.pickupbyNameArab)

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

        status_txt = findViewById(R.id.status)
        stud_class = findViewById(R.id.studClassValue)
        dateofPickup = findViewById(R.id.leaveDateFromValue)
        reasonRejectionLinear = findViewById(R.id.reasonRejectlayout)
        reasonRejectionScroll = findViewById(R.id.reasonRejectionScroll)
        pickup_name = findViewById(R.id.pickupbyName)
        reasonTxt = findViewById(R.id.reasonValue)
        reasonRejectionTxt = findViewById(R.id.reasonRejectValue)
        if (PreferenceManager().getLanguage(mContext).equals("ar")) {
          //  tolayoutArab.visibility = View.VISIBLE
            fromlayoutArab.visibility = View.VISIBLE
            studClassLinearArab.visibility = View.VISIBLE
            namelayoutArab.visibility = View.VISIBLE
            pickuplayoutArab.visibility = View.VISIBLE
            statusLayoutarab.visibility = View.VISIBLE

            namelayout.visibility = View.GONE
            studClassLinear.visibility = View.GONE
            fromlayout.visibility = View.GONE
            //tolayout.visibility = View.GONE
            pickuplayout.visibility = View.GONE
            statuslayout.visibility = View.GONE

            stnameValueArab.text = studname_pickup
            studClassValueArab.text = studcls_pickup
            leaveDateFromValueArab.text = outputDateStr
            pickupbyNameArab.text = pickby_pickup
            reasonTxt.text = reason_pickup
            if (status_pickup.equals("1")) {
                statusArab.text = resources.getString(R.string.pending)
                reasonRejectionLinear.visibility = View.GONE
                reasonRejectionScroll.visibility = View.GONE
            } else if (status_pickup.equals("2")) {
                statusArab.text = resources.getString(R.string.approved)
                reasonRejectionLinear.visibility = View.GONE
                reasonRejectionScroll.visibility = View.GONE
            } else {
                statusArab.text = resources.getString(R.string.rejected)
                reasonRejectionLinear.visibility = View.VISIBLE
                reasonRejectionScroll.visibility = View.VISIBLE
                reasonRejectionTxt.text = reason_for_rejection

            }

        }
        else{
            namelayout.visibility=View.VISIBLE
            studClassLinear.visibility=View.VISIBLE
            fromlayout.visibility=View.VISIBLE
            //tolayout.visibility=View.VISIBLE
            pickuplayout.visibility = View.VISIBLE
            statuslayout.visibility = View.VISIBLE

            //tolayoutArab.visibility=View.GONE
            fromlayoutArab.visibility=View.GONE
            studClassLinearArab.visibility=View.GONE
            namelayoutArab.visibility=View.GONE
            pickuplayoutArab.visibility = View.GONE
            statusLayoutarab.visibility = View.GONE
            stud_name.text = studname_pickup
            stud_class.text = studcls_pickup
            dateofPickup.text = outputDateStr
            pickup_name.text = pickby_pickup
            reasonTxt.text = reason_pickup

            if (status_pickup.equals("1")) {
                status_txt.text = resources.getString(R.string.pending)
                reasonRejectionLinear.visibility = View.GONE
                reasonRejectionScroll.visibility = View.GONE
            } else if (status_pickup.equals("2")) {
                status_txt.text = resources.getString(R.string.approved)
                reasonRejectionLinear.visibility = View.GONE
                reasonRejectionScroll.visibility = View.GONE
            } else {
                status_txt.text = resources.getString(R.string.rejected)
                reasonRejectionLinear.visibility = View.VISIBLE
                reasonRejectionScroll.visibility = View.VISIBLE
                reasonRejectionTxt.text = reason_for_rejection

            }


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