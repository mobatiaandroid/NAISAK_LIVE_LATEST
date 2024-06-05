package com.nas.naisak.activity.trips

import GeneralSubmitResponseModel
import TripChoicePreferenceResponseModel
import TripDetailsResponseModel
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.nas.naisak.BuildConfig
import com.nas.naisak.R
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.activity.login.LoginActivity
import com.nas.naisak.activity.payment.payhere.PaymentPayActivity
import com.nas.naisak.activity.payment.payhere.adapter.PaymentOptionAdapter
import com.nas.naisak.activity.payment.payhere.model.PaymentGatewayCreditInitiateResponseModel
import com.nas.naisak.activity.trips.adapter.ChoicePreferenceAdapter
import com.nas.naisak.activity.trips.adapter.ImagePagerDrawableAdapter
import com.nas.naisak.activity.trips.adapter.TripImageAdapter
import com.nas.naisak.activity.trips.adapter.TripsCategoryAdapter
import com.nas.naisak.activity.trips.model.ChoicePreferenceModel
import com.nas.naisak.activity.trips.model.SubmitDocResponseModel
import com.nas.naisak.activity.trips.model.TripChoicePaymentCountResponseModel
import com.nas.naisak.activity.trips.model.TripConsentResponseModel
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.GridSpacingItemDecoration
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.ProgressBarDialog
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.RecyclerItemListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Timer
import java.util.TimerTask
import kotlin.properties.Delegates

class TripDetailsActivityNew : AppCompatActivity(), ChoicePreferenceAdapter.OnItemSelectedListener{
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 101
    private val PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE = 2
    private val REQUEST_PERMISSION_EXTERNAL_STORAGE = 102
    private val VISA_CAMERA_REQUEST = 3
    private val PICK_IMAGE_FRONT_PASSPORT = 1
    private val PICK_IMAGE_BACK_PASSPORT = 2
    private val PICK_IMAGE_FRONT_VISA = 3
    private val PICK_IMAGE_BACK_VISA = 4
    private val PICK_IMAGE_FRONT_EID = 5
    private val PICK_IMAGE_BACK_EID = 6

    lateinit var studentName: TextView
    lateinit var studImg: ImageView
    lateinit var stud_img: String

    //    lateinit var studentsModelArrayList: ArrayList<StudentDataModel.DataItem>
    lateinit var progressDialogP: ProgressBarDialog
    private lateinit var pictureImagePath: String
    private lateinit var arrayList: ArrayList<String>
    lateinit var context: Context
    lateinit var extras: Bundle
    lateinit var tab_type: String
    lateinit var relativeHeader: RelativeLayout

    //    lateinit var headermanager: HeaderManager
    lateinit var bannerImageView: ImageView
    lateinit var sendEmailImageView: ImageView
    lateinit var recyclerViewLayoutManager: LinearLayoutManager

    //    lateinit var categoriesList: ArrayList<TripCategoriesResponseModel.Data>
    lateinit var tripsCategoryAdapter: TripsCategoryAdapter
    lateinit var back: ImageView
    lateinit var btn_history: ImageView
    lateinit var home: ImageView
    lateinit var stud_id: String
    var selectedChoice: String=""
    lateinit var studClass: String
    lateinit var orderId: String

    var tripStatus by Delegates.notNull<Int>()
    var paymentToken: String = ""
    var permissionSlip: String = ""
    lateinit var studentList: ArrayList<String>
    lateinit var tripMainBanner: ViewPager
    lateinit var tripImageRecycler: RecyclerView
    lateinit var tripNameTextView: TextView
    lateinit var tripAmountTextView: TextView
    lateinit var dateTextView: TextView
    lateinit var coordinatorNameTextView: TextView
    lateinit var mActivity: Activity
    lateinit var coordinatorName: TextView
    lateinit var coordinatorEmail : TextView
    lateinit var coordinatorMailTextView : TextView
    lateinit var coordinatorPhoneTextView: TextView
    lateinit var tripDescriptionTextView: TextView
    lateinit var submitIntentionButton: Button
    lateinit var submitDetailsButton: Button
    lateinit var viewInvoice: Button
    lateinit var paymentButton: Button
    lateinit var tripStatusTextView: TextView
    lateinit var tripID: String
    lateinit var tripName: String
    var currentPage = 0

    lateinit var multipleInstallmentsArray: ArrayList<TripDetailsResponseModel.InstallmentDetail>
    lateinit var singleInstallmentAmount: String
    lateinit var coodName: String
    lateinit var coodPhone: String
    lateinit var coodEmail: String
    lateinit var coodWhatsapp: String
    lateinit var medicalquestion : String
    lateinit var passportFrontURI: Uri
    lateinit var passportBackURI: Uri
    lateinit var visaFrontURI: Uri
    lateinit var visaBackURI: Uri
    lateinit var eIDFrontURI: Uri
    lateinit var eIDBackURI: Uri
    lateinit var passportFrontFile: File
    lateinit var OrderRef: String
    lateinit var PayUrl: String
    lateinit var AuthUrl: String
    lateinit var merchantOrderReference: String
    lateinit var tripChoiceExceed: String
    lateinit var tripPaymentExceed: String

    lateinit var invoiceArrayList: ArrayList<TripDetailsResponseModel.Invoice>
    var currentPosition: Int? = 0
    lateinit var passportURIArray: ArrayList<Uri>
    lateinit var visaURIArray: ArrayList<Uri>
    lateinit var eIDURIArray: ArrayList<Uri>
    lateinit var imagesArray: ArrayList<String>
    var passportStatus = 0
    var visaStatus = 0
    var eIDStatus = 0
    var permissionStatus = 0
    var medicalconsentstatus = 0

    lateinit var tripImageAdapter: TripImageAdapter
    lateinit var tripQuestion: String
    lateinit var tripType: String
    val permissionsRequiredCalendar = arrayOf(
        Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR
    )
    val permissionsRequiredExternalStorage = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val permissionsRequiredLocation = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
    )
    lateinit var choicePreferenceArray: ArrayList<String>

    var choicePreferenceSorted: ArrayList<ChoicePreferenceModel> = ArrayList()
    private var permissionFlag = true
    private var medicalconsentFlag = true
    private var studentDetailsFLag = true
    private var passportDetailsFLag = true
    private var visaDetailFlag = true
    private var eIDDetailFLag = true
    private var externalStorageToSettings = false
    private lateinit var calendarPermissionStatus: SharedPreferences
    private lateinit var externalStoragePermissionStatus: SharedPreferences
    private lateinit var locationPermissionStatus: SharedPreferences
    lateinit var backRelative: RelativeLayout
    var passportRequired by Delegates.notNull<Int>()
    var visaRequired by Delegates.notNull<Int>()
    var eIDRequired by Delegates.notNull<Int>()
    var consentRequired by Delegates.notNull<Int>()
    var medicalconsentRequired by Delegates.notNull<Int>()

    private lateinit var logoClickImgView: ImageView
    private lateinit var btn_left: ImageView
    private lateinit var heading: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)
        context = this
        externalStoragePermissionStatus =
            context.getSharedPreferences("externalStoragePermissionStatus", MODE_PRIVATE)

        initialiseUI()

    }

    private fun initialiseUI() {
        extras = intent.extras!!
        if (extras != null) {
            tripID = extras.getInt("tripID").toString()!!
            tripName = extras.getString("tripName").toString()!!
        }
        val fixedLength = 2
        passportURIArray = java.util.ArrayList(fixedLength)
        for (i in 0 until fixedLength) {
            passportURIArray.add(Uri.EMPTY)
        }
        visaURIArray = java.util.ArrayList(fixedLength)
        for (i in 0 until fixedLength) {
            visaURIArray.add(Uri.EMPTY)
        }
        eIDURIArray = java.util.ArrayList(fixedLength)
        for (i in 0 until fixedLength) {
            eIDURIArray.add(Uri.EMPTY)
        }
        backRelative = findViewById(R.id.backRelative)

        progressDialogP = ProgressBarDialog(context, R.drawable.spinner)
        tripImageRecycler = findViewById<RecyclerView>(R.id.tripImageRecycler)
        tripMainBanner = findViewById<ViewPager>(R.id.tripMainImage)
        tripNameTextView = findViewById(R.id.tripNameTextView)
        tripAmountTextView = findViewById<TextView>(R.id.tripAmountTextView)
        dateTextView = findViewById(R.id.dateTextView)
        tripImageRecycler = findViewById<RecyclerView>(R.id.tripImageRecycler)

        coordinatorName = findViewById<TextView>(R.id.coordinatorDetails)
        coordinatorNameTextView = findViewById<TextView>(R.id.coordinatorNameTextView)
        coordinatorEmail = findViewById<TextView>(R.id.coordinatorEmail)
        coordinatorMailTextView = findViewById<TextView>(R.id.coordinatorMailTextView)
        tripDescriptionTextView = findViewById<TextView>(R.id.tripDescriptionTextView)
        submitIntentionButton = findViewById<Button>(R.id.submitIntentionButton)
        submitDetailsButton = findViewById<Button>(R.id.submitDetailsButton)
        tripStatusTextView = findViewById<TextView>(R.id.tripStatusTextView)
        viewInvoice = findViewById<Button>(R.id.viewInvoice)
//        descriptionTextView = findViewById(R.id.tripDescriptionTextView);
        //        descriptionTextView = findViewById(R.id.tripDescriptionTextView);
        paymentButton = findViewById<Button>(R.id.paymentButton)
        relativeHeader = findViewById(R.id.relativeHeader)
//        headermanager = HeaderManager(this@TripDetailActivity, tripName)
//        headermanager.getHeader(relativeHeader, 6)
//        back = headermanager.getLeftButton()
//        btn_history = headermanager.getRightHistoryImage()
//        btn_history.visibility = View.INVISIBLE
        tripImageRecycler.setHasFixedSize(true)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        val spacing = 5 // 50px
        logoClickImgView.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        })
        backRelative.setOnClickListener {
            finish()
        }
//        val itemDecoration = ItemOffsetDecoration(context, spacing)
        recyclerViewLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

//        tripImageRecycler.addItemDecoration(DividerItemDecoration(context.resources.getDrawable(R.drawable.list_divider)))
//        tripImageRecycler.addItemDecoration(itemDecoration)
        tripImageRecycler.addOnItemTouchListener(
            RecyclerItemListener(
                context,
                tripImageRecycler,
                object : RecyclerItemListener.RecyclerTouchListener {
                    override fun onClickItem(v: View?, position: Int) {
                        val intent = Intent(context, TripImagesViewActivity::class.java)
                        intent.putExtra("photo_array", imagesArray)
                        intent.putExtra("pos", position)
                        startActivity(intent)
                    }

                    override fun onLongClickItem(v: View?, position: Int) {}
                })
        )
        heading = findViewById(R.id.heading)
        btn_left = findViewById(R.id.btn_left)

        heading.text = tripName

        getChoicePreferenceArrayList()
        tripImageRecycler.layoutManager = recyclerViewLayoutManager
//        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back)
//        back.setOnClickListener {
//            AppUtils.hideKeyBoard(context)
//            finish()
//        }

//        home = headermanager.getLogoButton()
//        home.setOnClickListener {
//            val `in` = Intent(context, HomeListAppCompatActivity::class.java)
//            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            startActivity(`in`)
//        }
        submitIntentionButton.setOnClickListener {
            if (tripChoiceExceed.equals("", ignoreCase = true)) {
                showIntentionPopUp()
            } else {
                CommonMethods.showDialogueWithOk(
                    context as Activity,
                    "You cannot submit any more intentions, as you have already reached your limit.",
                    "Alert",
                )
            }
        }

        submitDetailsButton.setOnClickListener {
            checkTripCount(tripID, object : TripCountCheckCallback {
                override fun onTripCountChecked(isTripCountEmpty: Boolean) {
                    if (isTripCountEmpty) {
                        if (Build.VERSION.SDK_INT > 30) {
                            showDocumentSubmissionPopUp()
                        } else {
                            if (ActivityCompat.checkSelfPermission(
                                    context, permissionsRequiredExternalStorage[0]
                                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                                    context, permissionsRequiredExternalStorage[1]
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                if (ActivityCompat.shouldShowRequestPermissionRationale(
                                        (context as Activity), permissionsRequiredExternalStorage[0]
                                    ) || ActivityCompat.shouldShowRequestPermissionRationale(
                                        (context as Activity), permissionsRequiredExternalStorage[1]
                                    )
                                ) {
                                    //Show information about why you need the permission
                                    val builder = AlertDialog.Builder(context)
                                    builder.setTitle("Need Storage Permission")
                                    builder.setMessage("This module needs Storage permissions.")
                                    builder.setPositiveButton(
                                        "Grant"
                                    ) { dialog, which ->
                                        dialog.cancel()
                                        ActivityCompat.requestPermissions(
                                            (context as Activity),
                                            permissionsRequiredExternalStorage,
                                            PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE
                                        )
                                    }
                                    builder.setNegativeButton(
                                        "Cancel"
                                    ) { dialog, which -> dialog.cancel() }
                                    builder.show()
                                } else if (externalStoragePermissionStatus.getBoolean(
                                        permissionsRequiredExternalStorage[0], false
                                    )
                                ) {
                                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                                    // Redirect to Settings after showing information about why you need the permission
                                    val builder = AlertDialog.Builder(context)
                                    builder.setTitle("Need Storage Permission")
                                    builder.setMessage("This module needs Storage permissions.")
                                    builder.setPositiveButton(
                                        "Grant"
                                    ) { dialog, which ->
                                        dialog.cancel()
                                        externalStorageToSettings = true
                                        val intent =
                                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        val uri = Uri.fromParts(
                                            "package", context.packageName, null
                                        )
                                        intent.setData(uri)
                                        startActivityForResult(
                                            intent, REQUEST_PERMISSION_EXTERNAL_STORAGE
                                        )
                                        Toast.makeText(
                                            context,
                                            "Go to settings and grant access to storage",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    builder.setNegativeButton(
                                        "Cancel"
                                    ) { dialog, which ->
                                        dialog.cancel()
                                        externalStorageToSettings = false
                                    }
                                    builder.show()
                                } else if (externalStoragePermissionStatus.getBoolean(
                                        permissionsRequiredExternalStorage[1], false
                                    )
                                ) {
                                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                                    // Redirect to Settings after showing information about why you need the permission
                                    val builder = AlertDialog.Builder(context)
                                    builder.setTitle("Need Storage Permission")
                                    builder.setMessage("This module needs Storage permissions.")
                                    builder.setCancelable(false)
                                    builder.setPositiveButton(
                                        "Grant"
                                    ) { dialog, which ->
                                        dialog.cancel()
                                        externalStorageToSettings = true
                                        val intent =
                                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        val uri = Uri.fromParts(
                                            "package", context.packageName, null
                                        )
                                        intent.setData(uri)
                                        startActivityForResult(
                                            intent, REQUEST_PERMISSION_EXTERNAL_STORAGE
                                        )
                                        Toast.makeText(
                                            context,
                                            "Go to settings and grant access to storage",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    builder.setNegativeButton(
                                        "Cancel"
                                    ) { dialog, which ->
                                        dialog.cancel()
                                        externalStorageToSettings = false
                                    }
                                    builder.show()
                                } else {

                                    //just request the permission
                                    //                        ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
                                    requestPermissions(
                                        permissionsRequiredExternalStorage,
                                        PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE
                                    )
                                }
                                val editor = externalStoragePermissionStatus.edit()
                                editor.putBoolean(permissionsRequiredExternalStorage[0], true)
                                editor.commit()
                            } else {
                                showDocumentSubmissionPopUp()
                            }
                        }
                    } else {
                        CommonMethods.showDialogueWithOk(
                            context,
                            "You can no longer apply for this trip, as all the slots have been filled.",
                            "Alert"
                        )

                    }
                }
            })


        }
        paymentButton.setOnClickListener {
            Log.e("trippaymentexeed",tripPaymentExceed)
            checkTripCount(tripID,object :TripCountCheckCallback{
                override fun onTripCountChecked(isTripCountEmpty: Boolean) {
                   if (isTripCountEmpty)
                   {
                       if (tripPaymentExceed.equals("", ignoreCase = true)) {
                           showPaymentsPopUp(context)
                       } else {
                           if (tripStatus == 6) {
                               showPaymentsPopUp(context)
                           } else CommonMethods.showDialogueWithOk(
                               context,
                               "You cannot submit any more payments, as you have already reached your trip limit.",
                               "Alert"
                           )
                       }
                   }
                    else
                   {
                       if (tripStatus == 6) {
                           showPaymentsPopUp(context)
                       }
                       else
                       {
                           CommonMethods.showDialogueWithOk(
                               context,
                               "You can no longer apply for this trip, as all the slots have been filled.",
                               "Alert"
                           )
                       }

                   }
                }

            })


        }
      //  coordinatorDetails.setOnClickListener { showCoordinatorDetailsPopUp() }
        viewInvoice.setOnClickListener {
            val intent = Intent(context, TripInvoiceListingActivity::class.java)
            intent.putExtra("tripID", tripID)
            intent.putExtra("tripName", tripName)
            startActivity(intent)
        }

        if (CommonMethods.isInternetAvailable(context)) {
            getTripDetails(tripID)
            getTripConsent()
        } else {
            CommonMethods.showDialogueWithOk(
                context,
                "Network error occurred. Please check your internet connection and try again later",
                "Network Error"
            )

        }
    }

    private fun getTripConsent() {
        val paramObject = JsonObject()
      //  Log.e("tripID name", tripID)
        paramObject.addProperty("student_id", PreferenceManager.getStudentID(context))
        paramObject.addProperty("trip_item_id", tripID)
        val call: Call<TripConsentResponseModel> = ApiClient.getClient.tripConsent(
            "Bearer " + PreferenceManager.getUserCode(context), paramObject
        )
        call.enqueue(object : Callback<TripConsentResponseModel> {
            override fun onResponse(
                call: Call<TripConsentResponseModel>, response: Response<TripConsentResponseModel>
            ) {
                if (response.body()!!.status == 100) {

                    permissionSlip = response.body()!!.data.lists.permissionContent

                } else {
                }
            }

            override fun onFailure(call: Call<TripConsentResponseModel>, t: Throwable) {
            }

        })

    }

    private fun showCoordinatorDetailsPopUp() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_coordinator_details)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val coordinatorNameTextView = dialog.findViewById<TextView>(R.id.coordinatorNameTextView)
        val phoneContactEditText = dialog.findViewById<EditText>(R.id.phoneContactEditText)
        val emailContactEditText = dialog.findViewById<EditText>(R.id.emailContactEditText)
        val whatsappContactEditText = dialog.findViewById<EditText>(R.id.whatsappContactEditText)
        coordinatorNameTextView.text = coodName
        phoneContactEditText.setText(coodPhone)
        emailContactEditText.setText(coodEmail)
        whatsappContactEditText.setText(coodWhatsapp)
        emailContactEditText.setOnClickListener {
            /* val intent = Intent(Intent.ACTION_SENDTO)
             intent.setData(Uri.parse("mailto:$coodEmail"))
             if (intent.resolveActivity(packageManager) != null) {
                 startActivity(intent)
             }*/

            val intent = Intent(Intent.ACTION_SEND)
            val recipients = arrayOf(coodEmail)
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            intent.setType("text/html")
            intent.setPackage("com.google.android.gm")
            context.startActivity(Intent.createChooser(intent, "Send mail"))
        }
        phoneContactEditText.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:$coodPhone"))
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
        whatsappContactEditText.setOnClickListener {


            /*String url = "https://wa.me/" + coodWhatsapp;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }*/
            val url = "https://api.whatsapp.com/send?phone=$coodWhatsapp"
            try {
                val pm = context.packageManager
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                val i = Intent(Intent.ACTION_VIEW)
                i.setData(Uri.parse(url))
                startActivity(i)
            } catch (e: PackageManager.NameNotFoundException) {
                Toast.makeText(
                    context,
                    "Whatsapp app not installed in your phone",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
            /* val url = "https://wa.me/$coodWhatsapp"
             val intent = Intent(Intent.ACTION_VIEW)
             intent.setData(Uri.parse(url))
             if (intent.resolveActivity(packageManager) != null) {
                 startActivity(intent)
             }*/
        }
        dialog.show()
    }


    private fun showPaymentsPopUp(activity: Context) {
        val bottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialogTheme)
        val layout: View =
            LayoutInflater.from(activity).inflate(R.layout.dialog_bottom_sheet_payment, null)
        bottomSheetDialog.setContentView(layout)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setCanceledOnTouchOutside(true)
        val payTotalView = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.payTotalView)
        val selectPaymentMethodView =
            bottomSheetDialog.findViewById<ConstraintLayout>(R.id.selectPaymentMethodView)
        val debitCardView = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.debitCardView)
        val creditCardView = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.creditCardView)
        val payInstallmentView =
            bottomSheetDialog.findViewById<ConstraintLayout>(R.id.payInstallmentView)
        val totalAmountTextView = bottomSheetDialog.findViewById<TextView>(R.id.totalAmountTextView)
        totalAmountTextView!!.text = "$singleInstallmentAmount QAR"
        if (multipleInstallmentsArray.size > 1) {
            payInstallmentView!!.visibility = View.VISIBLE
        } else {
            payInstallmentView!!.visibility = View.GONE
        }
        if (tripStatus == 6) {
            payInstallmentView.visibility = View.VISIBLE
            payTotalView!!.visibility = View.GONE
        }
        payInstallmentView.setOnClickListener {
            bottomSheetDialog.dismiss()
//            Toast.makeText(context, tripID, Toast.LENGTH_SHORT).show()
         //   Log.e("Before Activity trip ID", tripID)
            val intent = Intent(context, TripInstallmentActivity::class.java)
            intent.putExtra("tripID", tripID)
            intent.putExtra("tripName", tripName)
            context.startActivity(intent)
        }
        payTotalView!!.setOnClickListener {
//            bottomSheetDialog.dismiss()
            selectPaymentMethodView!!.visibility = View.VISIBLE
//            showOptionPopUp(context)
//            callOptionDialog(mContext)
//            initialisePayment()
        }
        debitCardView!!.setOnClickListener {
            bottomSheetDialog.dismiss()

            callDebitInitApi("2")

        }
        creditCardView!!.setOnClickListener {
            bottomSheetDialog.dismiss()
            callCreditInitApi("1")

        }
        bottomSheetDialog.show()
    }

    private fun showOptionPopUp(activity: Context) {
        val bottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialogTheme)
        val layout: View =
            LayoutInflater.from(activity)
                .inflate(R.layout.dialog_bottom_sheet_payment_credit_or_debit, null)
        bottomSheetDialog.setContentView(layout)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setCanceledOnTouchOutside(true)
        val debitCardView: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.debitCardView)
        val creditCardView: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.creditCardView)
        debitCardView!!.setOnClickListener {
            bottomSheetDialog.dismiss()
            callDebitInitApi("2")
        }
        creditCardView!!.setOnClickListener {
            bottomSheetDialog.dismiss()
            callCreditInitApi("1")
        }

        bottomSheetDialog.findViewById<ConstraintLayout>(R.id.selectPaymentMethodView)

        bottomSheetDialog.show()
    }

    override fun onResume() {
        super.onResume()
        getTripDetails(tripID)
    }


    fun callOptionDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_credit_debit)
        var btn_Ok = dialog.findViewById(R.id.btn_dismiss) as Button
        lateinit var mPaymentOptionArrayList: ArrayList<String>
        var studentListRecycler =
            dialog.findViewById(R.id.recycler_view_social_media) as RecyclerView
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        studentListRecycler.layoutManager = llm
        mPaymentOptionArrayList = ArrayList()
        mPaymentOptionArrayList.add("Credit")
        mPaymentOptionArrayList.add("Debit")
        val settingsAdapter = PaymentOptionAdapter(mPaymentOptionArrayList)
        studentListRecycler.adapter = settingsAdapter

        btn_Ok.setOnClickListener()
        {

            dialog.dismiss()
        }

        studentListRecycler.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                if (CommonMethods.isInternetAvailable(context)) {
                    var method: String = ""
                    if (position == 0) {
                        method = "1"
//                        mProgressRelLayout.visibility= View.VISIBLE
//                        callCreditInitApi(method)
                    } else if (position == 1) {
                        method = "2"
                        callDebitInitApi(method)
                        // CommonMethods.showDialogueWithOkPay(context,"Currently not supported this type of payment","Alert")

                    } else {
                        method = "3"
                        CommonMethods.showDialogueWithOkPay(
                            context,
                            "Currently not supported this type of payment",
                            "Alert"
                        )
                    }
//                    mProgressRelLayout.visibility= View.VISIBLE
//                    callGatewayLink(method)
                } else {
                    CommonMethods.showSuccessInternetAlert(context)
                }
                dialog.dismiss()
            }
        })

        dialog.show()
    }

    fun callCreditInitApi(paymentMethod: String) {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        var device = manufacturer + model
        val versionName: String = BuildConfig.VERSION_NAME
        val token = PreferenceManager.getUserCode(context)
        val paramObject = JsonObject()
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        merchantOrderReference = "TRIPAND$ts"
        paramObject.addProperty("student_id", PreferenceManager.getStudentID(context))
        paramObject.addProperty("trip_item_id", tripID)
        paramObject.addProperty("order_reference", merchantOrderReference)
        paramObject.addProperty("invoice_number", merchantOrderReference)
        paramObject.addProperty("paid_amount", singleInstallmentAmount)
        paramObject.addProperty("payment_type", "full_payment")
        paramObject.addProperty("device_type", "2")
        paramObject.addProperty("device_name", device)
        paramObject.addProperty("app_version", versionName)

        progressDialogP.show()
        val call: Call<PaymentGatewayCreditInitiateResponseModel> =
            ApiClient.getClient.tripCCPaymentInitiate("Bearer " + token, paramObject)

        call.enqueue(object : Callback<PaymentGatewayCreditInitiateResponseModel> {
            override fun onFailure(
                call: Call<PaymentGatewayCreditInitiateResponseModel>,
                t: Throwable
            ) {
               // Log.e("Error", t.localizedMessage)
                progressDialogP.dismiss()
            }

            override fun onResponse(
                call: Call<PaymentGatewayCreditInitiateResponseModel>,
                response: Response<PaymentGatewayCreditInitiateResponseModel>
            ) {
                progressDialogP.dismiss()
                if (response.body()!!.status == 100) {

                    var payment_url = response.body()!!.data.redirect_url
                    val intent = Intent(context, PaymentPayActivity::class.java)
                    intent.putExtra("payment_url", payment_url)
                    startActivity(intent)


                } else if (response.body()!!.status == 116) {
                    PreferenceManager.setUserCode(context, "")
                    PreferenceManager.setUserEmail(context, "")
                    val mIntent = Intent(this@TripDetailsActivityNew, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(mIntent)

                } else {


                }
            }

        })
    }


    fun callDebitInitApi(paymentMethod: String) {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        var device = manufacturer + model
        val versionName: String = BuildConfig.VERSION_NAME
        val token = PreferenceManager.getUserCode(context)
        val paramObject = JsonObject()
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        merchantOrderReference = "TRIPAND$ts"
        paramObject.addProperty("student_id", PreferenceManager.getStudentID(context))
        paramObject.addProperty("trip_item_id", tripID)
        paramObject.addProperty("order_reference", merchantOrderReference)
        paramObject.addProperty("invoice_number", merchantOrderReference)
        paramObject.addProperty("paid_amount", singleInstallmentAmount)
        paramObject.addProperty("payment_type", "full_payment")
        paramObject.addProperty("device_type", "2")
        paramObject.addProperty("device_name", device)
        paramObject.addProperty("app_version", versionName)
        val call: Call<PaymentGatewayCreditInitiateResponseModel> =
            ApiClient.getClient.tripDCPaymentInitiate("Bearer " + token, paramObject)
        call.enqueue(object : Callback<PaymentGatewayCreditInitiateResponseModel> {
            override fun onFailure(
                call: Call<PaymentGatewayCreditInitiateResponseModel>,
                t: Throwable
            ) {
               // ("Error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<PaymentGatewayCreditInitiateResponseModel>,
                response: Response<PaymentGatewayCreditInitiateResponseModel>
            ) {
                if (response.body()!!.status == 100) {

                    var payment_url = response.body()!!.data.redirect_url
                    val intent = Intent(context, PaymentPayActivity::class.java)
                    intent.putExtra("payment_url", payment_url)
                    startActivity(intent)
//                    var url = payment_url.replaceFirst(
//                        "^(http[s]?://www\\\\.|http[s]?://|www\\\\.)",
//                        ""
//                    )
//                    mainLinear.visibility = View.GONE
//                    paymentWeb.visibility = View.VISIBLE
//                    setWebViewSettingsPrint()
//                    Log.e("URL LOAD", url)
//                    paymentWeb.loadUrl(url)


                } else if (response.body()!!.status == 116) {
                    PreferenceManager.setUserCode(context, "")
                    PreferenceManager.setUserEmail(context, "")
                    val mIntent = Intent(this@TripDetailsActivityNew, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(mIntent)

                } else {


                }
            }

        })
    }


    private fun getTripDetails(tripID: String) {
        progressDialogP.show()
        val paramObject = JsonObject()
       // Log.e("tripID name", tripID)
        paramObject.addProperty("student_id", PreferenceManager.getStudentID(context))
        paramObject.addProperty("trip_id", tripID)
        val call: Call<TripDetailsResponseModel> = ApiClient.getClient.tripDetail(
            "Bearer " + PreferenceManager.getUserCode(context), paramObject
        )
        call.enqueue(object : Callback<TripDetailsResponseModel> {
            override fun onResponse(
                call: Call<TripDetailsResponseModel>, response: Response<TripDetailsResponseModel>
            ) {
                progressDialogP.dismiss()
                imagesArray = java.util.ArrayList()
                if (response.body()!!.status == 100) {
                    if (response.body()!!.data.lists.tripImage.size > 0) {
//                            Glide.with(context).load(AppUtils.replace(response.body().getResponse().getData().getTripImage().get(0))).placeholder(R.drawable.default_banner).into(tripMainBanner);
                        for (i in 0 until response.body()!!.data.lists.tripImage.size) {
                            imagesArray.add(
                                response.body()!!.data.lists.tripImage[i]
                            )
                        }
                        tripMainBanner.adapter = ImagePagerDrawableAdapter(imagesArray, context)
                        tripImageAdapter = TripImageAdapter(context, imagesArray)
                        tripImageRecycler.adapter = tripImageAdapter
                    } else {
//                            if (response.body().getResponse().getData().getTripImage().size() > 0) {
//                                Glide.with(cofntext).load(AppUtils.replace(response.body().getResponse().getData().getTripImage().get(0))).placeholder(R.drawable.default_banner).into(tripMainBanner);
//
//                            }
                    }
                    passportRequired = response.body()!!.data.lists.documentsRequired.passportDoc
                    visaRequired = response.body()!!.data.lists.documentsRequired.visaDoc
                    eIDRequired = response.body()!!.data.lists.documentsRequired.emiratesDoc
                    consentRequired = response.body()!!.data.lists.documentsRequired.consentDoc
                    medicalconsentRequired = response.body()!!.data.lists.documentsRequired.medicalconsentDoc
                    tripChoiceExceed = response.body()!!.data.choices_exceed
                    tripPaymentExceed = response.body()!!.data.no_of_trips_exceed
                    if (imagesArray != null) {
                        val handler = android.os.Handler()
                        val Update = Runnable {
                            if (currentPage == imagesArray.size) {
                                currentPage = 0
                                tripMainBanner.setCurrentItem(currentPage, false)
                            } else {
                                tripMainBanner.setCurrentItem(currentPage++, true)
                            }
                        }
                        val swipeTimer = Timer()
                        swipeTimer.schedule(object : TimerTask() {
                            override fun run() {
                                handler.post(Update)
                            }
                        }, 100, 3000)
                    }
                    if (imagesArray.size > 0) {
                        tripMainBanner.adapter = ImagePagerDrawableAdapter(imagesArray, context)
                    } else {
                        //CustomStatusDialog();
//											bannerImagePager.setBackgroundResource(R.drawable.banner);
                        tripMainBanner.setBackgroundResource(R.drawable.default_banner)
                        //											Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show();
                    }
                    //                        tripQuestion = response.body().getResponse().getData().q
                    tripNameTextView.setText(response.body()!!.data.lists.tripNameEn)
                    tripAmountTextView.text =
                        "Trip Amount : " + response.body()!!.data.lists.totalPrice + " QAR"
                    dateTextView.text =
                        "Trip Date : " + CommonMethods.dateParsingyyyyMMddToDdMmmYyyy(response.body()!!.data.lists.tripStartDate) + " To " + CommonMethods.dateParsingyyyyMMddToDdMmmYyyy(
                            response.body()!!.data.lists.tripEndDate
                        )
                    coordinatorNameTextView.setText("Co-ordinator Name       :")
                    coordinatorName.setText(response.body()!!.data.lists.coordinatorName)
                    coordinatorEmail.setText(response.body()!!.data.lists.coordinatorEmail)
                    coordinatorMailTextView.setText("Co-ordinator Email        :")
                    coodName = response.body()!!.data.lists.coordinatorName
                    coodPhone = response.body()!!.data.lists.coordinatorPhone
                    coodEmail = response.body()!!.data.lists.coordinatorEmail
                    coodWhatsapp = response.body()!!.data.lists.coordinatorWp
                    medicalquestion = response.body()!!.data.lists.medicalconsentquestion
                    passportStatus =
                        response.body()!!.data.lists.documentUploadStatus.passportStatus
                    visaStatus = response.body()!!.data.lists.documentUploadStatus.visaStatus
                    eIDStatus = response.body()!!.data.lists.documentUploadStatus.emiratesStatus
                    permissionStatus =
                        response.body()!!.data.lists.documentUploadStatus.consentStatus
                    medicalconsentstatus = response.body()!!.data.lists.documentUploadStatus.medicalconsentStatus
                    //                        coordinatorDetails.setText(response.body().getResponse().getData().getCoordinatorEmail());
//                        coordinatorPhoneTextView.setText(response.body().getResponse().getData().getCoordinatorPhone());
//                        tripDescriptionTextView.setText(response.body().getResponse().getData().getDescription());
                    tripDescriptionTextView.text =
                        Html.fromHtml(response.body()!!.data.lists.description)
//                        headermanager = HeaderManager(
//                            this@TripDetailsActivity,
//                            response.body().getResponse().getData().getTripName()
//                        )
                    if (response.body()!!.data.lists.installmentDetails.size > 0) {
                        multipleInstallmentsArray = response.body()!!.data.lists.installmentDetails
                    }
                    singleInstallmentAmount = response.body()!!.data.lists.totalPrice
                    if (response.body()!!.data.lists.invoices.size > 0) {
                        invoiceArrayList = response.body()!!.data.lists.invoices
                    }
                    tripStatus = response.body()!!.data.lists.tripStatus
                    if (response.body()!!.data.lists.trip_type.equals("international"))
                    {
                        if (tripStatus == 0) {
                            submitIntentionButton.visibility = View.VISIBLE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            tripStatusTextView.visibility = View.GONE
                        } else if (tripStatus == 1) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            tripStatusTextView.visibility = View.VISIBLE
                            tripStatusTextView.text = "Waiting for approval"
                        } else if (tripStatus == 2) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            tripStatusTextView.visibility = View.GONE
                        } else if (tripStatus == 3) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.VISIBLE
                            submitDetailsButton.setText("Pay Now")
                            paymentButton.visibility = View.GONE
                            tripStatusTextView.visibility = View.GONE
                        } else if (tripStatus == 4) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            tripStatusTextView.visibility = View.VISIBLE
                            tripStatusTextView.text = "Trip Canceled"
                        } else if (tripStatus == 5) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.VISIBLE
                            tripStatusTextView.visibility = View.GONE
                        } else if (tripStatus == 6) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.VISIBLE
                            tripStatusTextView.visibility = View.GONE
                        } else if (tripStatus == 7) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            viewInvoice.visibility = View.VISIBLE
                            tripStatusTextView.visibility = View.GONE
                        } else {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            tripStatusTextView.visibility = View.GONE
                        }
                    }else if (response.body()!!.data.lists.trip_type.equals("domestic"))
                    {
                        if (tripStatus == 0) {
                            submitIntentionButton.visibility = View.VISIBLE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            tripStatusTextView.visibility = View.GONE
                        } else if (tripStatus == 1) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            tripStatusTextView.visibility = View.VISIBLE
                            tripStatusTextView.text = "Waiting for approval"
                        } else if (tripStatus == 2) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            tripStatusTextView.visibility = View.GONE
                        } else if (tripStatus == 3) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.VISIBLE
                            submitDetailsButton.setText("Book your Trip")
                            paymentButton.visibility = View.GONE
                            tripStatusTextView.visibility = View.GONE
                        } else if (tripStatus == 4) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            tripStatusTextView.visibility = View.VISIBLE
                            tripStatusTextView.text = "Trip Canceled"
                        } else if (tripStatus == 5) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.VISIBLE
                            tripStatusTextView.visibility = View.GONE
                        } else if (tripStatus == 6) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.VISIBLE
                            tripStatusTextView.visibility = View.GONE
                        } else if (tripStatus == 7) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            viewInvoice.visibility = View.VISIBLE
                            tripStatusTextView.visibility = View.GONE
                        } else {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            tripStatusTextView.visibility = View.GONE
                        }
                    }
                    else
                    {

                    }

                } else {
                }

            }

            override fun onFailure(call: Call<TripDetailsResponseModel>, t: Throwable) {
                progressDialogP.dismiss()
                CommonMethods.showDialogueWithOk(context, getString(R.string.common_error), "")
                CommonMethods.showDialogueWithOk(
                    context as Activity, getString(R.string.common_error), "Alert"
                )
            }
        })

    }


    interface TripCountCheckCallback {
        fun onTripCountChecked(isTripCountEmpty: Boolean)
    }


    private fun showDocumentSubmissionPopUp() {
        val dial = Dialog(context)
        dial.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dial.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dial.setCancelable(false)
        dial.setContentView(R.layout.dialog_details_submit_new)
//        val nxt_btn = dial.findViewById<Button>(R.id.paymentbutton)
        val permissionLinear = dial.findViewById<ConstraintLayout>(R.id.permissionDetailsConstraint)
        val passportLinear = dial.findViewById<LinearLayout>(R.id.passportConstraint)
        val visaDetailsLinear = dial.findViewById<LinearLayout>(R.id.visaConstraint)
        val eIDDetailsLinear = dial.findViewById<LinearLayout>(R.id.emirateIDConstraint)
        val medicalConstraint = dial.findViewById<LinearLayout>(R.id.medicalConstraint)
        val payButtonConstraint = dial.findViewById<ConstraintLayout>(R.id.payNowButtonConstraint)
        val chooseFilePassportFront = dial.findViewById<Button>(R.id.chooseFilePassportFront)
        val passportNumberEditText = dial.findViewById<EditText>(R.id.passportNumberEditText)
        val chooseFilePassportBack = dial.findViewById<Button>(R.id.chooseFilePassportBack)
        val uploadPassportDetailsButton =
            dial.findViewById<Button>(R.id.uploadPassportDetailsButton)
        val visaEditText = dial.findViewById<EditText>(R.id.visaEditText)
        val permissionSlipTextView = dial.findViewById<TextView>(R.id.permissionSlipTextView)
        val chooseFileVisaFront = dial.findViewById<Button>(R.id.chooseFileVisaFront)
        val chooseFileVisaBack = dial.findViewById<Button>(R.id.choosefileVisaBack)
        val uploadVisaDetailsButton = dial.findViewById<Button>(R.id.uploadVisaDetailsButton)
        permissionSlipTextView.text = Html.fromHtml(permissionSlip)
        val eIDEditText = dial.findViewById<EditText>(R.id.eIDEditText)
        val studtitle = dial.findViewById<TextView>(R.id.studtitle)
        val passportTitleTV = dial.findViewById<TextView>(R.id.passportTitleTV)
        val visaTitleTV = dial.findViewById<TextView>(R.id.visaTitleTV)
        val emiratedIDTV = dial.findViewById<TextView>(R.id.emiratedIDTV)
        val medicalIDTV = dial.findViewById<TextView>(R.id.medicalIDTV)
        val permissiontitle = dial.findViewById<TextView>(R.id.permissiontitle)
        val chooseFileEIDFront = dial.findViewById<Button>(R.id.chooseFileEIDFront)
        val choosefileEIDBack = dial.findViewById<Button>(R.id.choosefileEIDBack)
        val uploadEIDDetailsButton = dial.findViewById<Button>(R.id.uploadEIDDetailsButton)
        val submitConsentButton = dial.findViewById<Button>(R.id.submitConsentButton)
        val payNowButtonText = dial.findViewById<TextView>(R.id.payNowButton)
        payNowButtonText.text = "PAY $singleInstallmentAmount QAR"
        val close_btn = dial.findViewById<ImageView>(R.id.close_btn)
        val studentAdd = dial.findViewById<ImageView>(R.id.studentAdd)
        val passportAdd = dial.findViewById<ImageView>(R.id.passportAdd)
        val visaAdd = dial.findViewById<ImageView>(R.id.visaAdd)
        val emiratesAdd = dial.findViewById<ImageView>(R.id.emiratesAdd)
        val permissionAdd = dial.findViewById<ImageView>(R.id.permissionAdd)

        val medicalconsentAdd = dial.findViewById<ImageView>(R.id.medicalconcentImg)
        val medicalconsentQuestion = dial.findViewById<TextView>(R.id.medicalconsentQuestion)
        val medicalconsentEditText = dial.findViewById<EditText>(R.id.medicalconsentEditText)
        val uploadmedicalDetailsButton = dial.findViewById<Button>(R.id.uploadmedicalDetailsButton)

        val yesNoRadioGroup = dial.findViewById<RadioGroup>(R.id.yesNoRadioGroup)
        val yesButton = dial.findViewById<RadioButton>(R.id.yesRadio)
        val noButton = dial.findViewById<RadioButton>(R.id.noRadio)
        val signhere = dial.findViewById<TextView>(R.id.signhere)
        yesNoRadioGroup.orientation = LinearLayout.HORIZONTAL
        medicalconsentQuestion.setText(medicalquestion)
        yesButton.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                medicalconsentEditText.visibility = View.VISIBLE
                //                    submitIntentionButton.setVisibility(View.GONE);
            }
        }
        noButton.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                medicalconsentEditText.visibility = View.GONE
                uploadmedicalDetailsButton.visibility = View.VISIBLE
            }
        }

//        permissiontitle.text = permissionSlip
        //        if (tripType.equalsIgnoreCase("1")) {
//            studentAdd.setVisibility(View.GONE);
//            studtitle.setVisibility(View.GONE);
//            passportAdd.setVisibility(View.GONE);
//            passportTitleTV.setVisibility(View.GONE);
//            passportLinear.setVisibility(View.GONE);
//            visaDetailsLinear.setVisibility(View.GONE);
//            eIDDetailsLinear.setVisibility(View.GONE);
//            visaTitleTV.setVisibility(View.GONE);
//            visaAdd.setVisibility(View.GONE);
//            emiratedIDTV.setVisibility(View.GONE);
//            emiratesAdd.setVisibility(View.GONE);
//            permissiontitle.setVisibility(View.GONE);
//        }
        if (passportRequired == 0) {
            passportTitleTV.visibility = View.GONE
            passportAdd.visibility = View.GONE
            passportLinear.visibility = View.GONE
        } else {
            passportTitleTV.visibility = View.VISIBLE
            passportAdd.visibility = View.VISIBLE
            passportLinear.visibility = View.VISIBLE
        }
        if (visaRequired == 0) {
            visaTitleTV.visibility = View.GONE
            visaAdd.visibility = View.GONE
            visaDetailsLinear.visibility = View.GONE
        } else {
            visaTitleTV.visibility = View.VISIBLE
            visaAdd.visibility = View.VISIBLE
            visaDetailsLinear.visibility = View.VISIBLE
        }
        if (eIDRequired == 0) {
            emiratedIDTV.visibility = View.GONE
            emiratesAdd.visibility = View.GONE
            eIDDetailsLinear.visibility = View.GONE
        } else {
            emiratedIDTV.visibility = View.VISIBLE
            emiratesAdd.visibility = View.VISIBLE
            eIDDetailsLinear.visibility = View.VISIBLE
        }
        if (consentRequired == 0) {
            permissiontitle.visibility = View.GONE
            permissionAdd.visibility = View.GONE
            permissionLinear.visibility = View.GONE
        } else {
            permissiontitle.visibility = View.VISIBLE
            permissionAdd.visibility = View.VISIBLE
            permissionLinear.visibility = View.VISIBLE
        }

        if (medicalconsentRequired == 0) {
            medicalIDTV.visibility = View.GONE
            medicalconsentAdd.visibility = View.GONE
            medicalConstraint.visibility = View.GONE
        } else {
            medicalIDTV.visibility = View.VISIBLE
            medicalconsentAdd.visibility = View.VISIBLE
            medicalConstraint.visibility = View.VISIBLE
        }
        val rememeberMeImg = dial.findViewById<CheckBox>(R.id.rememeberMeImg)
        val signature_pad: SignaturePad = dial.findViewById(R.id.signature_pad)
        if (passportStatus == 1) {
            if (passportRequired == 1) {
                passportLinear.visibility = View.GONE
                passportAdd.setImageResource(R.drawable.participatingsmallicon_new)
            } else {
                passportLinear.visibility = View.GONE
                passportAdd.visibility = View.GONE
                passportTitleTV.visibility = View.GONE
            }
        }
        if (visaStatus == 1) {
            if (visaRequired == 1) {
                visaDetailsLinear.visibility = View.GONE
                visaAdd.setImageResource(R.drawable.participatingsmallicon_new)
            } else {
                visaDetailsLinear.visibility = View.GONE
                visaAdd.visibility = View.GONE
                visaTitleTV.visibility = View.GONE
            }
        }
        if (eIDStatus == 1) {
            if (eIDRequired == 1) {
                eIDDetailsLinear.visibility = View.GONE
                emiratesAdd.setImageResource(R.drawable.participatingsmallicon_new)
            } else {
                eIDDetailsLinear.visibility = View.GONE
                emiratesAdd.visibility = View.GONE
                emiratedIDTV.visibility = View.GONE
            }
        }
        if (permissionStatus == 1) {
            if (consentRequired == 1) {
                permissionSlipTextView.text = permissionSlip
                permissionLinear.visibility = View.GONE
                permissionAdd.setImageResource(R.drawable.participatingsmallicon_new)
            } else {
                permissionLinear.visibility = View.GONE
                permissionAdd.visibility = View.GONE
                permissiontitle.visibility = View.GONE
            }
        }

        if (medicalconsentstatus == 1) {
            if (medicalconsentRequired == 1) {
                medicalConstraint.visibility = View.GONE
                medicalconsentAdd.setImageResource(R.drawable.participatingsmallicon_new)
            } else {
                medicalConstraint.visibility = View.GONE
                medicalconsentAdd.visibility = View.GONE
                medicalIDTV.visibility = View.GONE
            }
        }
        payButtonConstraint.setOnClickListener {
            if (passportStatus == 1 && visaStatus == 1 && eIDStatus == 1 && permissionStatus == 1) {
                showPaymentsPopUp(context)
            } else Toast.makeText(context, "Please upload all documents.", Toast.LENGTH_SHORT)
                .show()
        }
        studentAdd.setOnClickListener {
            if (studentDetailsFLag) {
                passportLinear.visibility = View.GONE
                visaDetailsLinear.visibility = View.GONE
                eIDDetailsLinear.visibility = View.GONE
            } else {
                if (passportStatus != 1) {
                    passportLinear.visibility = View.VISIBLE
                }
                if (visaStatus != 1) {
                    visaDetailsLinear.visibility = View.VISIBLE
                }
                if (eIDStatus != 1) {
                    eIDDetailsLinear.visibility = View.VISIBLE
                }
            }
            studentDetailsFLag = !studentDetailsFLag
        }
        passportAdd.setOnClickListener {
            if (passportDetailsFLag) {
                passportLinear.visibility = View.GONE
            } else {
                passportLinear.visibility = View.VISIBLE
            }
            passportDetailsFLag = !passportDetailsFLag
        }
        visaAdd.setOnClickListener {
            if (visaDetailFlag) {
                visaDetailsLinear.visibility = View.GONE
            } else {
                visaDetailsLinear.visibility = View.VISIBLE
            }
            visaDetailFlag = !visaDetailFlag
        }
        emiratesAdd.setOnClickListener {
            if (eIDDetailFLag) {
                eIDDetailsLinear.visibility = View.GONE
            } else {
                eIDDetailsLinear.visibility = View.VISIBLE
            }
            eIDDetailFLag = !eIDDetailFLag
        }
        permissionAdd.setOnClickListener {
            if (permissionFlag) {
                permissionLinear.visibility = View.GONE
            } else {
                permissionLinear.visibility = View.VISIBLE
            }
            permissionFlag = !permissionFlag
        }
        medicalconsentAdd.setOnClickListener {
            if (medicalconsentFlag) {
                medicalConstraint.visibility = View.GONE
            } else {
                medicalConstraint.visibility = View.VISIBLE
            }
            medicalconsentFlag = !medicalconsentFlag
        }
        submitConsentButton.setOnClickListener {
            if (signature_pad.isEmpty()) {
                // Prompt the user to enter a signature
                Toast.makeText(context, "Please enter your signature", Toast.LENGTH_SHORT).show()
            } else if (!rememeberMeImg.isChecked) {
                Toast.makeText(
                    context, "Please agree to terms and conditions", Toast.LENGTH_SHORT
                ).show()
            } else {
                val signatureBitmap: Bitmap = signature_pad.getSignatureBitmap()
                val signatureFile: File = bitmapToFile(signatureBitmap)
                uploadConsentAPICall(dial, signatureFile)
            }
        }

        uploadmedicalDetailsButton.setOnClickListener {

            if (yesButton.isChecked && (!medicalconsentEditText.text.toString().equals(""))) {

                uploadmedicalConsentApi(
                    dial,"1", medicalconsentEditText.text.toString())
            } else if (noButton.isChecked)  {

                uploadmedicalConsentApi(
                    dial, "2" ,"")
            } else Toast.makeText(context, "Please provide your Medical Consent!", Toast.LENGTH_SHORT)
                .show()

        }
        close_btn.setOnClickListener { dial.dismiss() }
        chooseFilePassportFront.setOnClickListener {
            currentPosition = 0
            openGallery(PICK_IMAGE_FRONT_PASSPORT)
        }
        rememeberMeImg.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                signature_pad.setVisibility(View.VISIBLE)
                signhere.visibility=View.VISIBLE
            } else
            {
                signature_pad.setVisibility(View.GONE)
                signhere.visibility=View.GONE
            }
        }
        chooseFilePassportBack.setOnClickListener {
            currentPosition = 1
            openGallery(PICK_IMAGE_BACK_PASSPORT)
        }
        uploadPassportDetailsButton.setOnClickListener {
           // Log.e("herer", passportURIArray[0].path + passportURIArray[1].path)
            if (!passportURIArray[0].path.equals(
                    "", ignoreCase = true
                ) && !passportURIArray[1].path.equals(
                    "", ignoreCase = true
                ) && !passportNumberEditText.text.toString().equals("", ignoreCase = true)
            ) {
                uploadDocumentsAPICall(
                    dial, passportURIArray, passportNumberEditText.text.toString(), "passport"
                )
            } else if (passportNumberEditText.text.toString().equals("", ignoreCase = true)) {
                Toast.makeText(context, "Please enter passport number.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please upload both images.", Toast.LENGTH_SHORT).show()
            }
        }
        chooseFileVisaFront.setOnClickListener {
            currentPosition = 0
            openGallery(PICK_IMAGE_FRONT_VISA)
        }
        chooseFileVisaBack.setOnClickListener {
            currentPosition = 1
            openGallery(PICK_IMAGE_BACK_VISA)
        }
        uploadVisaDetailsButton.setOnClickListener {
           // Log.e("herer", visaURIArray[0].path!!)
            if (!visaURIArray[0].path.equals(
                    "", ignoreCase = true
                ) && !visaEditText.text.toString().equals("", ignoreCase = true)
            ) {
                uploadSingleDocumentsAPICall(
                    dial, visaURIArray[0], visaEditText.text.toString(), "visa"
                )
            } else if (visaEditText.text.toString().equals("", ignoreCase = true)) {
                Toast.makeText(context, "Please enter Visa number.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please upload both images.", Toast.LENGTH_SHORT).show()
            }
        }
        chooseFileEIDFront.setOnClickListener {
            currentPosition = 0
            openGallery(PICK_IMAGE_FRONT_EID)
        }
        choosefileEIDBack.setOnClickListener {
            currentPosition = 1
            openGallery(PICK_IMAGE_BACK_EID)
        }
        uploadEIDDetailsButton.setOnClickListener {
            //Log.e("herer", eIDURIArray[0].path!!)
           // Log.e("front", eIDURIArray[0].path!!)
          //  Log.e("back", eIDURIArray[1].path!!)
            if (!eIDURIArray[0].path.equals(
                    "", ignoreCase = true
                ) && !eIDURIArray[1].path.equals(
                    "", ignoreCase = true
                ) && !eIDEditText.text.toString().equals("", ignoreCase = true)
            ) {
                uploadDocumentsAPICall(
                    dial, eIDURIArray, eIDEditText.text.toString(), "emirates"
                )
            } else if (eIDEditText.text.toString().equals("", ignoreCase = true)) {
                Toast.makeText(context, "Please enter QID number.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, "Please upload both images.", Toast.LENGTH_SHORT).show()
            }
        }
        dial.show()
    }

    private fun openGallery(requestCode: Int) {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       // Log.e("current", currentPosition.toString())
        if (resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? =
                contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
            cursor?.use {
                it.moveToFirst()
                val columnIndex = it.getColumnIndex(filePathColumn[0])
                val imagePath = it.getString(columnIndex)
                when (requestCode) {
                    PICK_IMAGE_FRONT_PASSPORT -> {
                        passportURIArray[currentPosition!!] = Uri.parse(imagePath)
                      //  Log.e("uri", passportURIArray[currentPosition!!].path!!)
                    }

                    PICK_IMAGE_BACK_PASSPORT -> {
                        passportURIArray[currentPosition!!] = Uri.parse(imagePath)
                        //Log.e("uri", passportURIArray[currentPosition!!].path!!)
                    }

                    PICK_IMAGE_FRONT_VISA -> {
                        visaURIArray[currentPosition!!] = Uri.parse(imagePath)
                        //Log.e("uri", visaURIArray[currentPosition!!].path!!)
                    }

                    PICK_IMAGE_FRONT_EID -> {
                        eIDURIArray[currentPosition!!] = Uri.parse(imagePath)
                      //  Log.e("uri", eIDURIArray[currentPosition!!].path!!)
                      //  Log.e("ursai", Uri.parse(imagePath).toString())
                    }

                    PICK_IMAGE_BACK_EID -> {
                        eIDURIArray[currentPosition!!] = Uri.parse(imagePath)
                      //  Log.e("uri", eIDURIArray[currentPosition!!].path.toString())
                      //  Log.e("ursai", Uri.parse(imagePath).toString())
                    }

                    else -> Toast.makeText(context, "Transaction failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun getChoicePreferenceArrayList() {
        val call: Call<TripChoicePreferenceResponseModel> =
            ApiClient.getClient.tripChoicePreference(
                "Bearer " + PreferenceManager.getUserCode(context)
            )
        call.enqueue(object : Callback<TripChoicePreferenceResponseModel> {

            override fun onResponse(
                call: Call<TripChoicePreferenceResponseModel>,
                response: Response<TripChoicePreferenceResponseModel>
            ) {
                choicePreferenceArray = response.body()!!.data.choices
                for (choice in choicePreferenceArray) {
                    val model = ChoicePreferenceModel()
                    model.choiceName = choice
                    model.selected = false
                    choicePreferenceSorted.add(model)
                }
            }

            override fun onFailure(call: Call<TripChoicePreferenceResponseModel>, t: Throwable) {
                Toast.makeText(this@TripDetailsActivityNew, "Some error occurred.", Toast.LENGTH_SHORT)
                    .show()
            }

        })

    }


    fun checkTripCount(
        tripID: String?, callback: TripCountCheckCallback
    ) {
        val paramObject = JsonObject()
       // Log.e("tripID name", tripID!!)
        paramObject.addProperty("trip_id", tripID)
        val call: Call<TripChoicePaymentCountResponseModel> = ApiClient.getClient.tripCountCheck(
            "Bearer " + PreferenceManager.getUserCode(context), paramObject
        )
        call.enqueue(object : Callback<TripChoicePaymentCountResponseModel> {

            override fun onResponse(
                call: Call<TripChoicePaymentCountResponseModel>,
                response: Response<TripChoicePaymentCountResponseModel>
            ) {
                val isTripCountEmpty =
                    response.isSuccessful && response.body() != null && response.body()!!.data.tripMaxStudents.isEmpty()
                callback.onTripCountChecked(isTripCountEmpty)
            }

            override fun onFailure(call: Call<TripChoicePaymentCountResponseModel>, t: Throwable) {
                callback.onTripCountChecked(false)
            }

        })

    }


    private fun showIntentionPopUp() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_intention_pop_up)
        val yesNoRadioGroup = dialog.findViewById<RadioGroup>(R.id.yesNoRadioGroup)
        val yesButton = dialog.findViewById<RadioButton>(R.id.yesRadio)
        val tripIntentionQuestion = dialog.findViewById<TextView>(R.id.tripIntentionQuestion)
        val studentNameTextView = dialog.findViewById<TextView>(R.id.studentNameTextView)
        studentNameTextView.text = PreferenceManager.getStudentName(context)
        val noButton = dialog.findViewById<RadioButton>(R.id.noRadio)
        val preferenceLayout = dialog.findViewById<ConstraintLayout>(R.id.preferenceLayout)
        val submitIntentionButton = dialog.findViewById<Button>(R.id.submitIntentionButton)
        val closeImageView = dialog.findViewById<ImageView>(R.id.close_img)
        val choicePreferenceQuestion = dialog.findViewById<TextView>(R.id.choicePreferenceQuestion)
        val choicePreferenceListView =
            dialog.findViewById<RecyclerView>(R.id.choicePreferenceRecycler)
        preferenceLayout.visibility = View.GONE // Initially hidden
        yesNoRadioGroup.orientation = LinearLayout.HORIZONTAL
        val adapter = ChoicePreferenceAdapter(context, choicePreferenceSorted, this)

        choicePreferenceListView.adapter = adapter
        val layoutManager = GridLayoutManager(context, 3)
        choicePreferenceListView.layoutManager = layoutManager
        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        val itemDecoration = GridSpacingItemDecoration(3, spacing, true)
        choicePreferenceListView.addItemDecoration(itemDecoration)
        choicePreferenceListView.addOnItemTouchListener(
            RecyclerItemListener(
                context,
                choicePreferenceListView,
                object : RecyclerItemListener.RecyclerTouchListener {
                    override fun onClickItem(v: View?, position: Int) {
                        for(i in 0..choicePreferenceSorted.size-1)
                        {
                            choicePreferenceSorted.get(i).selected=false
                        }
                        choicePreferenceSorted.get(position).selected=true
                        selectedChoice=choicePreferenceSorted.get(position).choiceName
                        adapter.notifyDataSetChanged()
//                submitIntent("1",dialog,choicePreferenceArray.get(position));
                    }

                    override fun onLongClickItem(v: View?, position: Int) {
//                submitIntent("1",dialog,choicePreferenceArray.get(position));
                    }
                })
        )
        submitIntentionButton.setOnClickListener {

             if (yesButton.isChecked ) {

                 submitIntent("1", dialog)
             } else if (noButton.isChecked) {

                 submitIntent("2", dialog)
             } else Toast.makeText(context, "Please provide your intention!", Toast.LENGTH_SHORT)
                 .show()

           /* if (yesButton.isChecked && selectedChoice == "") {
                Toast.makeText(context, "Please select your choice!", Toast.LENGTH_SHORT).show()
            } else if (yesButton.isChecked && selectedChoice != "") {
                submitIntent("1", dialog, selectedChoice)
            } else if (noButton.isChecked) {
                submitIntent("2", dialog, "")
            } else Toast.makeText(context, "Please provide your intention!", Toast.LENGTH_SHORT)
                .show()*/
        }
        yesButton.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                preferenceLayout.visibility = View.GONE
                //                    submitIntentionButton.setVisibility(View.GONE);
            }
        }
        noButton.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                preferenceLayout.visibility = View.GONE
                submitIntentionButton.visibility = View.VISIBLE
            }
        }
        closeImageView.setOnClickListener { dialog.dismiss() }

        closeImageView.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun submitIntent(intent: String, dialog: Dialog) {
        progressDialogP.show()
        val paramObject = JsonObject()

        paramObject.addProperty("student_id", PreferenceManager.getStudentID(context))
        paramObject.addProperty("trip_item_id", tripID)
        paramObject.addProperty("status", intent)

        val call: Call<GeneralSubmitResponseModel> = ApiClient.getClient.tripIntentSubmit(
            "Bearer " + PreferenceManager.getUserCode(context), paramObject
        )
        call.enqueue(object : Callback<GeneralSubmitResponseModel> {

            override fun onResponse(
                call: Call<GeneralSubmitResponseModel>,
                response: Response<GeneralSubmitResponseModel>
            ) {
                dialog.dismiss()
                if (response.body()!!.status == 100) {
                    Toast.makeText(
                        this@TripDetailsActivityNew,
                        "Intention successfully submitted.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (response.body()!!.status == (313)) {
                    Toast.makeText(
                        this@TripDetailsActivityNew, "Intention already submitted.", Toast.LENGTH_SHORT
                    ).show()
                } else if (response.body()!!.status == 314) {
                    Toast.makeText(
                        this@TripDetailsActivityNew,
                        "Intention already submitted for this choice. Select any other choice.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@TripDetailsActivityNew, "Intention Submission Failed.", Toast.LENGTH_SHORT
                    ).show()
                }

                getTripDetails(tripID)
            }

            override fun onFailure(call: Call<GeneralSubmitResponseModel>, t: Throwable) {
                progressDialogP.dismiss()

                CommonMethods.showDialogueWithOk(
                    context as Activity, getString(R.string.common_error), "Alert"

                )
            }
        })
    }
private fun uploadmedicalConsentApi(dial: Dialog, preference: String, reason: String)
{

    var paramObject = JsonObject()
    // android.util.Log.e("student name", studentNameTxt.getText().toString())
    paramObject.addProperty("action", "medical_consent")
    paramObject.addProperty("trip_item_id", tripID)
    paramObject.addProperty("student_id", PreferenceManager.getStudentID(context))
    paramObject.addProperty("medical_concern",preference)
    paramObject.addProperty("medical_concern_reason", reason)


    progressDialogP.show()
    val call: Call<SubmitDocResponseModel> = ApiClient.getClient.uploadmedicalDocuments(
        "Bearer " + PreferenceManager.getUserCode(context),
        paramObject
    )
    call.enqueue(object : Callback<SubmitDocResponseModel> {
        override fun onResponse(
            call: Call<SubmitDocResponseModel>,
            response: Response<SubmitDocResponseModel>
        ) {
            progressDialogP.dismiss()
            if (response.body()!!.status == 100) {
                dial.dismiss()
                Toast.makeText(
                    this@TripDetailsActivityNew,
                    "Document successfully submitted.",
                    Toast.LENGTH_SHORT
                ).show()
                passportStatus = response.body()!!.data.documentStatus.passportStatus
                visaStatus = response.body()!!.data.documentStatus.visaStatus
                eIDStatus = response.body()!!.data.documentStatus.emiratesStatus
                permissionStatus = response.body()!!.data.documentStatus.consentStatus
                medicalconsentstatus = response.body()!!.data.documentStatus.medicalconsentStatus
                dial.dismiss()
                if (response.body()!!.data.documentStatus.documentCompletionStatus == 1
                ) {
                    getTripDetails(tripID)
                } else {
                    showDocumentSubmissionPopUp()
                }
            } else {
                Toast.makeText(
                    this@TripDetailsActivityNew,
                    "Document submit failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onFailure(call: Call<SubmitDocResponseModel>, t: Throwable) {
            progressDialogP.dismiss()

        }
    })

}
    private fun uploadConsentAPICall(dial: Dialog, signatureFile: File) {
        val requestFile1: RequestBody
        var requestFile2: RequestBody
        var attachment1: MultipartBody.Part? = null


        if (signatureFile.length() > 0) {
            val requestFile1 =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), signatureFile)
            attachment1 =
                MultipartBody.Part.createFormData("attachment1", signatureFile.name, requestFile1)
        }


        val frontImagePart: MultipartBody.Part? = attachment1
        val action = RequestBody.create("text/plain".toMediaTypeOrNull(), "consent")
        val student_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            PreferenceManager.getStudentID(context).toString()
        )
        val trip_item_id = RequestBody.create("text/plain".toMediaTypeOrNull(), tripID)
        val card_number = RequestBody.create("text/plain".toMediaTypeOrNull(), "number")
        progressDialogP.show()
        val call: Call<SubmitDocResponseModel> = ApiClient.getClient.uploadSingleDocument(
            "Bearer " + PreferenceManager.getUserCode(context),
            action,
            trip_item_id,
            student_id,
            card_number,
            frontImagePart
        )
        call.enqueue(object : Callback<SubmitDocResponseModel> {
            override fun onResponse(
                call: Call<SubmitDocResponseModel>,
                response: Response<SubmitDocResponseModel>
            ) {
                progressDialogP.dismiss()
                if (response.body()!!.status == 100) {
                    dial.dismiss()
                    Toast.makeText(
                        this@TripDetailsActivityNew,
                        "Permission slip successfully submitted.",
                        Toast.LENGTH_SHORT
                    ).show()
                    passportStatus = response.body()!!.data.documentStatus.passportStatus
                    visaStatus = response.body()!!.data.documentStatus.visaStatus
                    eIDStatus = response.body()!!.data.documentStatus.emiratesStatus
                    permissionStatus = response.body()!!.data.documentStatus.consentStatus
                    dial.dismiss()
                    if (response.body()!!.data.documentStatus.documentCompletionStatus == 1
                    ) {
                        getTripDetails(tripID)
                    } else {
                        showDocumentSubmissionPopUp()
                    }
                } else {
                    Toast.makeText(
                        this@TripDetailsActivityNew,
                        "Permission slip submit failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<SubmitDocResponseModel>, t: Throwable) {
                progressDialogP.dismiss()

            }
        })

//


    }

    private fun bitmapToFile(bitmap: Bitmap): File {
        val signatureFile = File(context.externalCacheDir, "signature.png")
        try {
            // Write the bitmap to the file
            val fos = FileOutputStream(signatureFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return signatureFile
    }

    private fun prepareImagePart(uri: Uri, partName: String): MultipartBody.Part? {
        return try {
            val file = File(uri.path)
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
            val byteArray = stream.toByteArray()
            val currentTimeMillis = System.currentTimeMillis().toString()
            val compressedFile = File(
                context.cacheDir, "compressed_image$currentTimeMillis.jpg"
            )
            val fos = FileOutputStream(compressedFile)
            fos.write(byteArray)
            fos.flush()
            fos.close()
            val requestFile =
                compressedFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(partName, compressedFile.name, requestFile)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun uploadSingleDocumentsAPICall(
        dial: Dialog, uriArray: Uri, number: String, documentType: String
    ) {
        val file1 = File(uriArray.path)
        val frontImagePart = prepareImagePart(uriArray, "attachment1")
       // Log.e("path", uriArray.path.toString())

        val action = RequestBody.create("text/plain".toMediaTypeOrNull(), documentType)
        val student_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            PreferenceManager.getStudentID(context).toString()
        )
        val trip_item_id = RequestBody.create("text/plain".toMediaTypeOrNull(), tripID)
        val card_number = RequestBody.create("text/plain".toMediaTypeOrNull(), number)
        progressDialogP.show()
        val call: Call<SubmitDocResponseModel> = ApiClient.getClient.uploadSingleDocument(
            "Bearer " + PreferenceManager.getUserCode(context),
            action,
            trip_item_id,
            student_id,
            card_number,
            frontImagePart
        )
        call.enqueue(object : Callback<SubmitDocResponseModel> {
            override fun onResponse(
                call: Call<SubmitDocResponseModel>,
                response: Response<SubmitDocResponseModel>
            ) {
                progressDialogP.dismiss()
                if (response.body()!!.status == 100) {
                    dial.dismiss()
                    Toast.makeText(
                        this@TripDetailsActivityNew,
                        "Document successfully submitted.",
                        Toast.LENGTH_SHORT
                    ).show()
                    passportStatus = response.body()!!.data.documentStatus.passportStatus
                    visaStatus = response.body()!!.data.documentStatus.visaStatus
                    eIDStatus = response.body()!!.data.documentStatus.emiratesStatus
                    permissionStatus = response.body()!!.data.documentStatus.consentStatus
                    dial.dismiss()
                    if (response.body()!!.data.documentStatus.documentCompletionStatus == 1
                    ) {
                        getTripDetails(tripID)
                    } else {
                        showDocumentSubmissionPopUp()
                    }
                } else {
                    Toast.makeText(
                        this@TripDetailsActivityNew,
                        "Document submit failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<SubmitDocResponseModel>, t: Throwable) {
                progressDialogP.dismiss()

            }

        })


    }


    private fun uploadDocumentsAPICall(
        dial: Dialog, uriArray: java.util.ArrayList<Uri>, number: String, documentType: String
    ) {

        val file1 = File(uriArray[0].path)
        val file2 = File(uriArray[1].path)
        progressDialogP.show()
        val frontImagePart = prepareImagePart(uriArray[0], "attachment1")
        val backImagePart = prepareImagePart(uriArray[1], "attachment2")


        val action = RequestBody.create("text/plain".toMediaTypeOrNull(), documentType)
        Log.e("action", action.toString())
        val student_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            PreferenceManager.getStudentID(context).toString()
        )
        val trip_item_id = RequestBody.create("text/plain".toMediaTypeOrNull(), tripID)
        val card_number = RequestBody.create("text/plain".toMediaTypeOrNull(), number)
        val call: Call<SubmitDocResponseModel> = ApiClient.getClient.uploadDocuments(
            "Bearer " + PreferenceManager.getUserCode(context),
            action,
            trip_item_id,
            student_id,
            card_number,
            frontImagePart,
            backImagePart
        )
        call.enqueue(object : Callback<SubmitDocResponseModel> {
            override fun onResponse(
                call: Call<SubmitDocResponseModel>,
                response: Response<SubmitDocResponseModel>
            ) {
                progressDialogP.dismiss()
                if (response.body()!!.status == 100) {
                    dial.dismiss()
                    Toast.makeText(
                        this@TripDetailsActivityNew,
                        "Document successfully submitted.",
                        Toast.LENGTH_SHORT
                    ).show()
                    passportStatus = response.body()!!.data.documentStatus.passportStatus
                    visaStatus = response.body()!!.data.documentStatus.visaStatus
                    eIDStatus = response.body()!!.data.documentStatus.emiratesStatus
                    permissionStatus = response.body()!!.data.documentStatus.consentStatus
                    dial.dismiss()
                    if (response.body()!!.data.documentStatus.documentCompletionStatus == 1
                    ) {
                        getTripDetails(tripID)
                    } else {
                        showDocumentSubmissionPopUp()
                    }
                } else {
                    Toast.makeText(
                        this@TripDetailsActivityNew,
                        "Document submit failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<SubmitDocResponseModel>, t: Throwable) {
                progressDialogP.dismiss()

            }

        })

    }

    override fun onItemSelected(choice: String?) {
        selectedChoice = choice!!

    }

}