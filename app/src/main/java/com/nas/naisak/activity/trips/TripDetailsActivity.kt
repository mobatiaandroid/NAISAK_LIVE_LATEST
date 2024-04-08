package com.nas.naisak.activity.trips

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
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
import androidx.appcompat.app.AlertDialog
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
import com.nas.naisak.R
import com.nas.naisak.activity.trips.adapter.ImagePagerDrawableAdapter
import com.nas.naisak.activity.trips.adapter.TripImageAdapter
import com.nas.naisak.activity.trips.adapter.TripsCategoryAdapter
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.GridSpacingItemDecoration
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.ProgressBarDialog
import com.nas.naisak.constants.recyclermanager.RecyclerItemListener
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Part
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Timer
import java.util.TimerTask

class TripDetailsActivity : AppCompatActivity() {
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

    lateinit var categoriesList: ArrayList<TripCategoriesResponseModel.Data>
    lateinit var tripsCategoryAdapter: TripsCategoryAdapter
    lateinit var back: ImageView
    lateinit var btn_history: ImageView
    lateinit var home: ImageView
    lateinit var stud_id: String
    lateinit var selectedChoice: String
    lateinit var studClass: String
    lateinit var orderId: String
    lateinit var tripStatus: String
    lateinit var paymentToken: String
    lateinit var permissionSlip: String
    lateinit var studentList: ArrayList<String>
    lateinit var tripMainBanner: ViewPager
    lateinit var tripImageRecycler: RecyclerView
    lateinit var tripNameTextView: TextView
    lateinit var tripAmountTextView: TextView
    lateinit var dateTextView: TextView
    lateinit var coordinatorNameTextView: TextView
    lateinit var mActivity: Activity
    lateinit var coordinatorDetails: TextView
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

    //        lateinit var multipleInstallmentsArray: ArrayList<TripDetailsResponseModel.InstallmentDetail>
    lateinit var singleInstallmentAmount: String
    lateinit var coodName: String
    lateinit var coodPhone: String
    lateinit var coodEmail: String
    lateinit var coodWhatsapp: String
    lateinit var passportFrontURI: Uri
    lateinit var passportBackURI: Uri
    lateinit var visaFrontURI: Uri
    lateinit var visaBackURI: Uri
    lateinit var eIDFrontURI: Uri
    lateinit var eIDBackURI: Uri
    lateinit var passportFrontFile: File
    lateinit var PaymentToken: String
    lateinit var OrderRef: String
    lateinit var PayUrl: String
    lateinit var AuthUrl: String
    lateinit var merchantOrderReference: String
    lateinit var tripChoiceExceed: String
    lateinit var tripPaymentExceed: String

    //    lateinit var invoiceArrayList: ArrayList<TripDetailsResponseModel.TripData.Invoice>
    var currentPosition: Int? = 0
    lateinit var passportURIArray: ArrayList<Uri>
    lateinit var visaURIArray: ArrayList<Uri>
    lateinit var eIDURIArray: ArrayList<Uri>
    lateinit var imagesArray: ArrayList<String>
    var passportStatus = 0
    var visaStatus = 0
    var eIDStatus = 0
    var permissionStatus = 0

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

    //    lateinit var choicePreferenceSorted: ArrayList<ChoicePreferenceModel>
    private var permissionFlag = true
    private var studentDetailsFLag = true
    private var passportDetailsFLag = true
    private var visaDetailFlag = true
    private var eIDDetailFLag = true
    private var externalStorageToSettings = false
    private lateinit var calendarPermissionStatus: SharedPreferences
    private lateinit var externalStoragePermissionStatus: SharedPreferences
    private lateinit var locationPermissionStatus: SharedPreferences

    lateinit var passportRequired: String
    lateinit var visaRequired: String
    lateinit var eIDRequired: String
    lateinit var consentRequired: String
    private lateinit var logoClickImgView: ImageView
    private lateinit var btn_left: ImageView
    private lateinit var heading: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)
        externalStoragePermissionStatus =
            context.getSharedPreferences("externalStoragePermissionStatus", MODE_PRIVATE)

        initialiseUI()

    }

    private fun initialiseUI() {
        extras = intent.extras!!
        if (extras != null) {
            tripID = extras.getString("tripID")!!
            tripName = extras.getString("tripName")!!
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
        progressDialogP = ProgressBarDialog(context, R.drawable.spinner)
        tripImageRecycler = findViewById<RecyclerView>(R.id.tripImageRecycler)
        tripMainBanner = findViewById<ViewPager>(R.id.tripMainImage)
        tripNameTextView = findViewById(R.id.tripNameTextView)
        tripAmountTextView = findViewById<TextView>(R.id.tripAmountTextView)
        dateTextView = findViewById(R.id.dateTextView)
        tripImageRecycler = findViewById<RecyclerView>(R.id.tripImageRecycler)

        coordinatorNameTextView = findViewById<TextView>(R.id.coordinatorNameTextView)
        coordinatorDetails = findViewById<TextView>(R.id.coordinatorDetails)
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
        val spacing = 5 // 50px

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
        logoClickImgView = findViewById(R.id.logoClickImgView)
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
                    "Alert",
                    "You cannot submit any more intentions, as you have already reached your limit.",
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
                                        (context as Activity),
                                        permissionsRequiredExternalStorage[0]
                                    ) || ActivityCompat.shouldShowRequestPermissionRationale(
                                        (context as Activity),
                                        permissionsRequiredExternalStorage[1]
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
                                            intent,
                                            REQUEST_PERMISSION_EXTERNAL_STORAGE
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
                                            intent,
                                            REQUEST_PERMISSION_EXTERNAL_STORAGE
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
            if (tripPaymentExceed.equals("", ignoreCase = true)) {
                showPaymentsPopUp(context)
            } else {
                if (tripStatus.equals("6", ignoreCase = true)) {
                    showPaymentsPopUp(context)
                } else CommonMethods.showDialogueWithOk(
                    context,
                    "You cannot submit any more payments, as you have already reached your trip limit.",
                    "Alert"
                )
            }
        }
        coordinatorDetails.setOnClickListener { showCoordinatorDetailsPopUp() }
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

    private fun showPaymentsPopUp(activity: Context) {
        val bottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialogTheme)
        val layout: View =
            LayoutInflater.from(activity).inflate(R.layout.dialog_bottom_sheet_payment, null)
        bottomSheetDialog.setContentView(layout)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setCanceledOnTouchOutside(true)
        val payTotalView = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.payTotalView)
        val payInstallmentView =
            bottomSheetDialog.findViewById<ConstraintLayout>(R.id.payInstallmentView)
        val totalAmountTextView = bottomSheetDialog.findViewById<TextView>(R.id.totalAmountTextView)
        totalAmountTextView!!.text = "$singleInstallmentAmount AED"
        if (multipleInstallmentsArray.size > 1) {
            payInstallmentView!!.visibility = View.VISIBLE
        } else {
            payInstallmentView!!.visibility = View.GONE
        }
        if (tripStatus.equals("6", ignoreCase = true)) {
            payInstallmentView.visibility = View.VISIBLE
            payTotalView!!.visibility = View.GONE
        }
        payInstallmentView.setOnClickListener {
            bottomSheetDialog.dismiss()
            val intent = Intent(context, TripInstallmentActivity::class.java)
            intent.putExtra("tripID", tripID)
            intent.putExtra("tripName", tripName)
            context.startActivity(intent)
        }
        payTotalView!!.setOnClickListener {
            bottomSheetDialog.dismiss()
            initialisePayment()
        }
        bottomSheetDialog.show()
    }


    private fun getTripDetails(tripID: String) {
        progressDialogP.show()
        val paramObject = JsonObject()
        Log.e("tripID name", tripID)
        paramObject.addProperty("student_id", PreferenceManager.getTripStudentId(context))
        paramObject.addProperty("trip_id", tripID)
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val call: Call<TripDetailsResponseModel> =
            service.tripDetail("Bearer " + PreferenceManager.getAccessToken(context), paramObject)
        call.enqueue(object : Callback<TripDetailsResponseModel> {
            override fun onResponse(
                call: Call<TripDetailsResponseModel>,
                response: Response<TripDetailsResponseModel>
            ) {
                progressDialogP.dismiss()
                if (response.body().getResponseCode().equalsIgnoreCase("200")) {
                    imagesArray = java.util.ArrayList()
                    if (response.body().getResponse().getStatusCode().equalsIgnoreCase("303")) {
                        if (response.body().getResponse().getData().getTripImage().size() > 1) {
//                            Glide.with(context).load(AppUtils.replace(response.body().getResponse().getData().getTripImage().get(0))).placeholder(R.drawable.default_banner).into(tripMainBanner);
                            for (i in 1 until response.body().getResponse().getData().getTripImage()
                                .size()) {
                                imagesArray.add(
                                    response.body().getResponse().getData().getTripImage().get(i)
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
                        passportRequired =
                            response.body().getResponse().getData().getDocuments_required()
                                .getPassport_doc()
                        visaRequired =
                            response.body().getResponse().getData().getDocuments_required()
                                .getVisa_doc()
                        eIDRequired =
                            response.body().getResponse().getData().getDocuments_required()
                                .getEmirates_doc()
                        consentRequired =
                            response.body().getResponse().getData().getDocuments_required()
                                .getConsent_doc()
                        tripType = response.body().getResponse().getData().getTrip_type()
                        tripChoiceExceed = response.body().getResponse().getTrip_exceed()
                        tripPaymentExceed = response.body().getResponse().getNo_of_trips_exceed()
                        if (imagesArray != null) {
                            val handler = Handler()
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
                        tripNameTextView.setText(
                            response.body().getResponse().getData().getTripName()
                        )
                        tripAmountTextView.text =
                            "Trip Amount : " + response.body().getResponse().getData()
                                .getTotalPrice() + " AED"
                        dateTextView.text =
                            "Trip Date : " + CommonMethods.dateParsingyyyyMMddToDdMmmYyyy(
                                response.body().getResponse().getData().getTripDate()
                            ) + " To " + CommonMethods.dateParsingyyyyMMddToDdMmmYyyy(
                                response.body().getResponse().getData().getTripEndDate()
                            )
                        coordinatorNameTextView.setText(
                            response.body().getResponse().getData().getCoordinatorName()
                        )
                        coodName = response.body().getResponse().getData().getCoordinatorName()
                        coodPhone = response.body().getResponse().getData().getCoordinatorPhone()
                        coodEmail = response.body().getResponse().getData().getCoordinatorEmail()
                        coodWhatsapp =
                            response.body().getResponse().getData().getCoordinatorWhatsApp()
                        passportStatus =
                            response.body().getResponse().getData().getDocumentUploadStatus()
                                .getPassportStatus()
                        visaStatus =
                            response.body().getResponse().getData().getDocumentUploadStatus()
                                .getVisaStatus()
                        eIDStatus =
                            response.body().getResponse().getData().getDocumentUploadStatus()
                                .getEmiratesStatus()
                        permissionStatus =
                            response.body().getResponse().getData().getDocumentUploadStatus()
                                .getConsentStatus()
                        //                        coordinatorDetails.setText(response.body().getResponse().getData().getCoordinatorEmail());
//                        coordinatorPhoneTextView.setText(response.body().getResponse().getData().getCoordinatorPhone());
//                        tripDescriptionTextView.setText(response.body().getResponse().getData().getDescription());
                        tripDescriptionTextView.text =
                            Html.fromHtml(response.body().getResponse().getData().getDescription())
//                        headermanager = HeaderManager(
//                            this@TripDetailsActivity,
//                            response.body().getResponse().getData().getTripName()
//                        )
                        if (response.body().getResponse().getData().getInstallmentDetails()
                                .size() > 0
                        ) {
                            multipleInstallmentsArray =
                                response.body().getResponse().getData().getInstallmentDetails()
                        }
                        singleInstallmentAmount =
                            response.body().getResponse().getData().getTotalPrice()
                        if (response.body().getResponse().getData().getInvoices().size() > 0) {
                            invoiceArrayList = response.body().getResponse().getData().getInvoices()
                        }
                        tripStatus = response.body().getResponse().getData().getTripStatus()
                        if (tripStatus.equals("0", ignoreCase = true)) {
                            submitIntentionButton.visibility = View.VISIBLE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            tripStatusTextView.visibility = View.GONE
                        } else if (tripStatus.equals("1", ignoreCase = true)) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            tripStatusTextView.visibility = View.VISIBLE
                            tripStatusTextView.text = "Waiting for approval"
                        } else if (tripStatus.equals("2", ignoreCase = true)) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            tripStatusTextView.visibility = View.GONE
                        } else if (tripStatus.equals("3", ignoreCase = true)) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.VISIBLE
                            paymentButton.visibility = View.GONE
                            tripStatusTextView.visibility = View.GONE
                        } else if (tripStatus.equals("4", ignoreCase = true)) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            tripStatusTextView.visibility = View.VISIBLE
                            tripStatusTextView.text = "Trip Canceled"
                        } else if (tripStatus.equals("5", ignoreCase = true)) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.VISIBLE
                            tripStatusTextView.visibility = View.GONE
                        } else if (tripStatus.equals("6", ignoreCase = true)) {
                            submitIntentionButton.visibility = View.GONE
                            submitDetailsButton.visibility = View.GONE
                            paymentButton.visibility = View.VISIBLE
                            tripStatusTextView.visibility = View.GONE
                        } else if (tripStatus.equals("7", ignoreCase = true)) {
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
                    } else {
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<TripDetailsResponseModel>, t: Throwable) {
                progressDialogP.dismiss()
                Log.e("error", t.localizedMessage)
                CommonMethods.showDialogueWithOk(context, getString(R.string.common_error), "")
                AppUtils.showDialogAlertDismiss(
                    context as Activity,
                    "Alert",
                    getString(R.string.common_error),
                    R.drawable.exclamationicon,
                    R.drawable.round
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
        val nxt_btn = dial.findViewById<Button>(R.id.paymentbutton)
        val permissionLinear = dial.findViewById<ConstraintLayout>(R.id.permissionDetailsConstraint)
        val passportLinear = dial.findViewById<LinearLayout>(R.id.passportConstraint)
        val visaDetailsLinear = dial.findViewById<LinearLayout>(R.id.visaConstraint)
        val eIDDetailsLinear = dial.findViewById<LinearLayout>(R.id.emirateIDConstraint)
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
        val permissiontitle = dial.findViewById<TextView>(R.id.permissiontitle)
        val chooseFileEIDFront = dial.findViewById<Button>(R.id.chooseFileEIDFront)
        val choosefileEIDBack = dial.findViewById<Button>(R.id.choosefileEIDBack)
        val uploadEIDDetailsButton = dial.findViewById<Button>(R.id.uploadEIDDetailsButton)
        val submitConsentButton = dial.findViewById<Button>(R.id.submitConsentButton)
        val payNowButtonText = dial.findViewById<TextView>(R.id.payNowButton)
        payNowButtonText.text = "PAY $singleInstallmentAmount AED"
        val close_btn = dial.findViewById<ImageView>(R.id.close_btn)
        val studentAdd = dial.findViewById<ImageView>(R.id.studentAdd)
        val passportAdd = dial.findViewById<ImageView>(R.id.passportAdd)
        val visaAdd = dial.findViewById<ImageView>(R.id.visaAdd)
        val emiratesAdd = dial.findViewById<ImageView>(R.id.emiratesAdd)
        val permissionAdd = dial.findViewById<ImageView>(R.id.permissionAdd)
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
        if (passportRequired.equals("0", ignoreCase = true)) {
            passportTitleTV.visibility = View.GONE
            passportAdd.visibility = View.GONE
            passportLinear.visibility = View.GONE
        } else {
            passportTitleTV.visibility = View.VISIBLE
            passportAdd.visibility = View.VISIBLE
            passportLinear.visibility = View.VISIBLE
        }
        if (visaRequired.equals("0", ignoreCase = true)) {
            visaTitleTV.visibility = View.GONE
            visaAdd.visibility = View.GONE
            visaDetailsLinear.visibility = View.GONE
        } else {
            visaTitleTV.visibility = View.VISIBLE
            visaAdd.visibility = View.VISIBLE
            visaDetailsLinear.visibility = View.VISIBLE
        }
        if (eIDRequired.equals("0", ignoreCase = true)) {
            emiratedIDTV.visibility = View.GONE
            emiratesAdd.visibility = View.GONE
            eIDDetailsLinear.visibility = View.GONE
        } else {
            emiratedIDTV.visibility = View.VISIBLE
            emiratesAdd.visibility = View.VISIBLE
            eIDDetailsLinear.visibility = View.VISIBLE
        }
        if (consentRequired.equals("0", ignoreCase = true)) {
            permissiontitle.visibility = View.GONE
            permissionAdd.visibility = View.GONE
            permissionLinear.visibility = View.GONE
        } else {
            permissiontitle.visibility = View.VISIBLE
            permissionAdd.visibility = View.VISIBLE
            permissionLinear.visibility = View.VISIBLE
        }
        val rememeberMeImg = dial.findViewById<CheckBox>(R.id.rememeberMeImg)
        val signature_pad: SignaturePad = dial.findViewById(R.id.signature_pad)
        if (passportStatus == 1) {
            if (passportRequired === "1") {
                passportLinear.visibility = View.GONE
                passportAdd.setImageResource(R.drawable.participatingsmallicon_new)
            } else {
                passportLinear.visibility = View.GONE
                passportAdd.visibility = View.GONE
                passportTitleTV.visibility = View.GONE
            }
        }
        if (visaStatus == 1) {
            if (visaRequired === "1") {
                visaDetailsLinear.visibility = View.GONE
                visaAdd.setImageResource(R.drawable.participatingsmallicon_new)
            } else {
                visaDetailsLinear.visibility = View.GONE
                visaAdd.visibility = View.GONE
                visaTitleTV.visibility = View.GONE
            }
        }
        if (eIDStatus == 1) {
            if (eIDRequired === "1") {
                eIDDetailsLinear.visibility = View.GONE
                emiratesAdd.setImageResource(R.drawable.participatingsmallicon_new)
            } else {
                eIDDetailsLinear.visibility = View.GONE
                emiratesAdd.visibility = View.GONE
                emiratedIDTV.visibility = View.GONE
            }
        }
        if (permissionStatus == 1) {
            if (consentRequired === "1") {
                permissionLinear.visibility = View.GONE
                permissionAdd.setImageResource(R.drawable.participatingsmallicon_new)
            } else {
                permissionLinear.visibility = View.GONE
                permissionAdd.visibility = View.GONE
                permissiontitle.visibility = View.GONE
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
        submitConsentButton.setOnClickListener {
            if (signature_pad.isEmpty()) {
                // Prompt the user to enter a signature
                Toast.makeText(context, "Please enter your signature", Toast.LENGTH_SHORT)
                    .show()
            } else if (!rememeberMeImg.isChecked) {
                Toast.makeText(
                    context,
                    "Please agree to terms and conditions",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val signatureBitmap: Bitmap =
                    signature_pad.getSignatureBitmap()
                val signatureFile: File = bitmapToFile(signatureBitmap)
                uploadConsentAPICall(dial, signatureFile)
            }
        }
        close_btn.setOnClickListener { dial.dismiss() }
        chooseFilePassportFront.setOnClickListener {
            currentPosition = 0
            openGallery(TripDetailActivity.PICK_IMAGE_FRONT_PASSPORT)
        }
        rememeberMeImg.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                signature_pad.setVisibility(View.VISIBLE)
            } else signature_pad.setVisibility(View.GONE)
        }
        chooseFilePassportBack.setOnClickListener {
            currentPosition = 1
            openGallery(TripDetailActivity.PICK_IMAGE_BACK_PASSPORT)
        }
        uploadPassportDetailsButton.setOnClickListener {
            Log.e("herer", "gesg")
            if (!passportURIArray[0].path.equals(
                    "",
                    ignoreCase = true
                ) && !passportURIArray[1].path.equals(
                    "",
                    ignoreCase = true
                ) && !passportNumberEditText.text.toString().equals("", ignoreCase = true)
            ) {
                uploadDocumentsAPICall(
                    dial,
                    passportURIArray,
                    passportNumberEditText.text.toString(),
                    "passport"
                )
            } else if (passportNumberEditText.text.toString().equals("", ignoreCase = true)) {
                Toast.makeText(context, "Please enter passport number.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, "Please upload both images.", Toast.LENGTH_SHORT).show()
            }
        }
        chooseFileVisaFront.setOnClickListener {
            currentPosition = 0
            openGallery(TripDetailActivity.PICK_IMAGE_FRONT_VISA)
        }
        chooseFileVisaBack.setOnClickListener {
            currentPosition = 1
            openGallery(TripDetailActivity.PICK_IMAGE_BACK_VISA)
        }
        uploadVisaDetailsButton.setOnClickListener {
            Log.e("herer", visaURIArray[1].path!!)
            if (!visaURIArray[0].path.equals(
                    "",
                    ignoreCase = true
                ) && !visaEditText.text.toString().equals("", ignoreCase = true)
            ) {
                uploadSingleDocumentsAPICall(
                    dial,
                    visaURIArray[0],
                    visaEditText.text.toString(),
                    "visa"
                )
            } else if (visaEditText.text.toString().equals("", ignoreCase = true)) {
                Toast.makeText(context, "Please enter Visa number.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please upload both images.", Toast.LENGTH_SHORT).show()
            }
        }
        chooseFileEIDFront.setOnClickListener {
            currentPosition = 0
            openGallery(TripDetailActivity.PICK_IMAGE_FRONT_EID)
        }
        choosefileEIDBack.setOnClickListener {
            currentPosition = 1
            openGallery(TripDetailActivity.PICK_IMAGE_BACK_EID)
        }
        uploadEIDDetailsButton.setOnClickListener {
            Log.e("herer", eIDURIArray[0].path!!)
            Log.e("front", eIDURIArray[0].path!!)
            Log.e("back", eIDURIArray[1].path!!)
            if (!eIDURIArray[0].path.equals(
                    "",
                    ignoreCase = true
                ) && !eIDURIArray[1].path.equals(
                    "",
                    ignoreCase = true
                ) && !eIDEditText.text.toString().equals("", ignoreCase = true)
            ) {
                uploadDocumentsAPICall(
                    dial,
                    eIDURIArray,
                    eIDEditText.text.toString(),
                    "emirates"
                )
            } else if (eIDEditText.text.toString().equals("", ignoreCase = true)) {
                Toast.makeText(context, "Please enter Emirates ID number.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, "Please upload both images.", Toast.LENGTH_SHORT).show()
            }
        }
        dial.show()
    }


    private fun getChoicePreferenceArrayList() {
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val call: Call<TripChoicePreferenceResponseModel> =
            service.tripChoicePreference("Bearer " + PreferenceManager.getAccessToken(context))
        call.enqueue(object : Callback<TripChoicePreferenceResponseModel> {
            override fun onResponse(
                call: Call<TripChoicePreferenceResponseModel>,
                response: Response<TripChoicePreferenceResponseModel>
            ) {
                choicePreferenceArray = response.body().getResponse().getChoices()
                for (choice in choicePreferenceArray) {
                    val model = ChoicePreferenceModel()
                    model.setChoiceName(choice)
                    model.setSelected(false)
                    choicePreferenceSorted.add(model)
                }
            }

            override fun onFailure(call: Call<TripChoicePreferenceResponseModel>, t: Throwable) {
                Toast.makeText(this@TripDetailActivity, "Some error occurred.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


    fun checkTripCount(
        tripID: String?,
        callback: TripCountCheckCallback
    ) {
        val paramObject = JsonObject()
        Log.e("tripID name", tripID!!)
        paramObject.addProperty("trip_id", tripID)
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val call: Call<TripCountResponseModel> = service.tripCountCheck(
            "Bearer " + PreferenceManager.getAccessToken(context),
            paramObject
        )
        call.enqueue(object : Callback<TripCountResponseModel?> {
            override fun onResponse(
                call: Call<TripCountResponseModel?>,
                response: Response<TripCountResponseModel?>
            ) {
                val isTripCountEmpty =
                    response.isSuccessful() && response.body() != null && response.body()
                        .getResponse().getTrip_max_students().isEmpty()
                callback.onTripCountChecked(isTripCountEmpty)
            }

            override fun onFailure(call: Call<TripCountResponseModel?>, t: Throwable) {
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
        studentNameTextView.setText(PreferenceManager.getStudentName(context))
        val noButton = dialog.findViewById<RadioButton>(R.id.noRadio)
        val preferenceLayout = dialog.findViewById<ConstraintLayout>(R.id.preferenceLayout)
        val submitIntentionButton = dialog.findViewById<Button>(R.id.submitIntentionButton)
        val closeImageView = dialog.findViewById<ImageView>(R.id.close_img)
        val choicePreferenceQuestion = dialog.findViewById<TextView>(R.id.choicePreferenceQuestion)
        val choicePreferenceListView =
            dialog.findViewById<RecyclerView>(R.id.choicePreferenceRecycler)
        preferenceLayout.visibility = View.GONE // Initially hidden
        yesNoRadioGroup.orientation = LinearLayout.HORIZONTAL
        val adapter = ChoicePreferenceAdapter(
            context, choicePreferenceSorted, this as ChoicePreferenceAdapter.OnItemSelectedListener
        ) // Replace yourDataList with your list of data
        choicePreferenceListView.adapter = adapter
        val layoutManager = GridLayoutManager(context, 3)
        choicePreferenceListView.layoutManager = layoutManager
        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        val itemDecoration = GridSpacingItemDecoration(3, spacing, true)
        choicePreferenceListView.addItemDecoration(itemDecoration)
        choicePreferenceListView.addOnItemTouchListener(
            RecyclerItemListener(context,
                choicePreferenceListView,
                object : RecyclerItemListener.RecyclerTouchListener {
                    override fun onClickItem(v: View?, position: Int) {
//                submitIntent("1",dialog,choicePreferenceArray.get(position));
                    }

                    override fun onLongClickItem(v: View?, position: Int) {
//                submitIntent("1",dialog,choicePreferenceArray.get(position));
                    }
                })
        )
        submitIntentionButton.setOnClickListener {
            if (yesButton.isChecked && selectedChoice == "") {
                Toast.makeText(context, "Please select your choice!", Toast.LENGTH_SHORT).show()
            } else if (yesButton.isChecked && selectedChoice != "") {
                submitIntent("1", dialog, selectedChoice)
            } else if (noButton.isChecked) {
                submitIntent("2", dialog, "")
            } else Toast.makeText(context, "Please provide your intention!", Toast.LENGTH_SHORT)
                .show()
        }
        yesButton.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                preferenceLayout.visibility = View.VISIBLE
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

    private fun submitIntent(intent: String, dialog: Dialog, preference: String) {
        progressDialogP.show()
        val paramObject = JsonObject()
        Log.e("tripID name", tripID)
        paramObject.addProperty("student_id", PreferenceManager.getTripStudentId(context))
        paramObject.addProperty("trip_item_id", tripID)
        paramObject.addProperty("status", intent)
        paramObject.addProperty("preference", preference)
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val call: Call<GeneralSubmitResponseModel> = service.tripIntentSubmit(
            "Bearer " + PreferenceManager.getAccessToken(context),
            paramObject
        )
        call.enqueue(object : Callback<GeneralSubmitResponseModel> {
            override fun onResponse(
                call: Call<GeneralSubmitResponseModel>,
                response: Response<GeneralSubmitResponseModel>
            ) {
                dialog.dismiss()
                if (response.body().getResponseCode().equalsIgnoreCase("200")) {
                    if (response.body().getResponse().getStatusCode().equalsIgnoreCase("303")) {
                        Toast.makeText(
                            this@TripDetailsActivity,
                            "Intention successfully submitted.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (response.body().getResponse().getStatusCode()
                            .equalsIgnoreCase("313")
                    ) {
                        Toast.makeText(
                            this@TripDetailsActivity,
                            "Intention already submitted.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (response.body().getResponse().getStatusCode()
                            .equalsIgnoreCase("314")
                    ) {
                        Toast.makeText(
                            this@TripDetailsActivity,
                            "Intention already submitted for this choice. Select any other choice.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@TripDetailsActivity,
                            "Intention Submission Failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                }
                getTripDetails(tripID)
            }

            override fun onFailure(call: Call<GeneralSubmitResponseModel>, t: Throwable) {
                progressDialogP.dismiss()
                Log.e("error", t.localizedMessage)
                CommonMethods.showDialogueWithOk(
                    context as Activity,
                    getString(R.string.common_error), "Alert"

                )
            }
        })
    }

    private fun uploadConsentAPICall(dial: Dialog, signatureFile: File) {
        val requestFile1: RequestBody
        var requestFile2: RequestBody
        var attachment1: Part? = null
        val attachment2: Part? = null
        if (signatureFile.length() > 0) {
            requestFile1 = RequestBody.create(parse.parse("multipart/form-data"), signatureFile)
            attachment1 =
                createFormData.createFormData("attachment1", signatureFile.name, requestFile1)
        }
        val action = RequestBody.create(parse.parse("text/plain"), "consent")
        val student_id: RequestBody =
            create(parse.parse("text/plain"), PreferenceManager.getTripStudentId(context))
        val trip_item_id = RequestBody.create(parse.parse("text/plain"), tripID)
        val card_number = RequestBody.create(parse.parse("text/plain"), "")
        val frontImagePart: Part? = attachment1
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        progressDialogP.show()
        val call: Call<TripDocumentSubmitResponseModel> = service.uploadPermissionSlip(
            "Bearer " + PreferenceManager.getAccessToken(context),
            action,
            trip_item_id,
            student_id,
            card_number,
            frontImagePart
        )
        call.enqueue(object : Callback<TripDocumentSubmitResponseModel> {
            override fun onResponse(
                call: Call<TripDocumentSubmitResponseModel>,
                response: Response<TripDocumentSubmitResponseModel>
            ) {
                progressDialogP.dismiss()
                if (response.body().getResponseCode().equalsIgnoreCase("200")) {
                    if (response.body().getResponseData().getStatusCode().equalsIgnoreCase("303")) {
                        dial.dismiss()
                        Toast.makeText(
                            this@TripDetailActivity,
                            "Permission slip successfully submitted.",
                            Toast.LENGTH_SHORT
                        ).show()
                        passportStatus = response.body().getResponseData().getDocumentStatus()
                            .getPassportStatus()
                        visaStatus =
                            response.body().getResponseData().getDocumentStatus().getVisaStatus()
                        eIDStatus = response.body().getResponseData().getDocumentStatus()
                            .getEmiratesStatus()
                        permissionStatus =
                            response.body().getResponseData().getDocumentStatus().getConsentStatus()
                        dial.dismiss()
                        if (response.body().getResponseData().getDocumentStatus()
                                .getDocumentCompletionStatus() === 1
                        ) {
                            getTripDetails(tripID)
                        } else {
                            showDocumentSubmissionPopUp()
                        }
                    } else {
                        Toast.makeText(
                            this@TripDetailsActivity,
                            "Permission slip submit failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else Toast.makeText(
                    this@TripDetailsActivity,
                    "Permission slip submit failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<TripDocumentSubmitResponseModel>, t: Throwable) {
                progressDialogP.dismiss()
                Log.e("Response", t.localizedMessage)
            }
        })
    }

    private fun bitmapToFile(bitmap: Bitmap): File {
        val signatureFile = File(externalCacheDir, "signature.png")
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

    private fun prepareImagePart(uri: Uri, partName: String): Part? {
        return try {
            val file = File(uri.path)
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream) // Adjust quality as needed
            val byteArray = stream.toByteArray()
            val currentTimeMillis = System.currentTimeMillis().toString()
            val compressedFile = File(
                context.cacheDir,
                "compressed_image$currentTimeMillis.jpg"
            )
            val fos = FileOutputStream(compressedFile)
            fos.write(byteArray)
            fos.flush()
            fos.close()
            val requestFile = RequestBody.create(parse.parse("multipart/form-data"), compressedFile)
            createFormData.createFormData(partName, compressedFile.name, requestFile)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun uploadSingleDocumentsAPICall(
        dial: Dialog,
        uriArray: Uri,
        number: String,
        documentType: String
    ) {
        var requestFile1: RequestBody
        var requestFile2: RequestBody
        val attachment1: Part? = null
        val attachment2: Part? = null
        val file1 = File(uriArray.path)
        val frontImagePart: Part? = prepareImagePart(uriArray, "attachment1")
        Log.e("path", uriArray.path!!)
        val action = RequestBody.create(parse.parse("text/plain"), documentType)
        val student_id: RequestBody =
            create(parse.parse("text/plain"), PreferenceManager.getTripStudentId(context))
        val trip_item_id = RequestBody.create(parse.parse("text/plain"), tripID)
        val card_number = RequestBody.create(parse.parse("text/plain"), number)
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        progressDialogP.show()
        val call: Call<TripDocumentSubmitResponseModel> = service.uploadSingleDocument(
            "Bearer " + PreferenceManager.getAccessToken(context),
            action,
            trip_item_id,
            student_id,
            card_number,
            frontImagePart
        )
        call.enqueue(object : Callback<TripDocumentSubmitResponseModel> {
            override fun onResponse(
                call: Call<TripDocumentSubmitResponseModel>,
                response: Response<TripDocumentSubmitResponseModel>
            ) {
                progressDialogP.dismiss()
                if (response.body().getResponseCode().equalsIgnoreCase("200")) {
                    if (response.body().getResponseData().getStatusCode().equalsIgnoreCase("303")) {
                        Toast.makeText(
                            this@TripDetailsActivity,
                            "Documents successfully submitted.",
                            Toast.LENGTH_SHORT
                        ).show()
                        dial.dismiss()
                        passportStatus = response.body().getResponseData().getDocumentStatus()
                            .getPassportStatus()
                        visaStatus =
                            response.body().getResponseData().getDocumentStatus().getVisaStatus()
                        eIDStatus = response.body().getResponseData().getDocumentStatus()
                            .getEmiratesStatus()
                        permissionStatus =
                            response.body().getResponseData().getDocumentStatus().getConsentStatus()
                        if (response.body().getResponseData().getDocumentStatus()
                                .getDocumentCompletionStatus() === 1
                        ) {
                            getTripDetails(tripID)
                        } else {
                            showDocumentSubmissionPopUp()
                        }
                    } else {
                        Toast.makeText(
                            this@TripDetailsActivity,
                            "Documents submit failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else Toast.makeText(
                    this@TripDetailsActivity,
                    "Documents submit failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<TripDocumentSubmitResponseModel>, t: Throwable) {
                progressDialogP.dismiss()
                Log.e("Response", t.localizedMessage)
            }
        })
    }


    private fun uploadDocumentsAPICall(
        dial: Dialog,
        uriArray: java.util.ArrayList<Uri>,
        number: String,
        documentType: String
    ) {
        var requestFile1: RequestBody
        var requestFile2: RequestBody
        val attachment1: Part? = null
        val attachment2: Part? = null
        val file1 = File(uriArray[0].path)
        val file2 = File(uriArray[1].path)
        val frontImagePart: Part? = prepareImagePart(uriArray[0], "attachment1")
        val backImagePart: Part? = prepareImagePart(uriArray[1], "attachment2")
        Log.e("path", uriArray[0].path!!)
        Log.e("path", uriArray[1].path!!)
        val action = RequestBody.create(parse.parse("text/plain"), documentType)
        val student_id: RequestBody =
            create(parse.parse("text/plain"), PreferenceManager.getTripStudentId(context))
        val trip_item_id = RequestBody.create(parse.parse("text/plain"), tripID)
        val card_number = RequestBody.create(parse.parse("text/plain"), number)
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        progressDialogP.show()
        val call: Call<TripDocumentSubmitResponseModel> = service.uploadDocuments(
            "Bearer " + PreferenceManager.getAccessToken(context),
            action,
            trip_item_id,
            student_id,
            card_number,
            frontImagePart,
            backImagePart
        )
        call.enqueue(object : Callback<TripDocumentSubmitResponseModel> {
            override fun onResponse(
                call: Call<TripDocumentSubmitResponseModel>,
                response: Response<TripDocumentSubmitResponseModel>
            ) {
                progressDialogP.dismiss()
                if (response.body().getResponseCode().equalsIgnoreCase("200")) {
                    if (response.body().getResponseData().getStatusCode().equalsIgnoreCase("303")) {
                        dial.dismiss()
                        Toast.makeText(
                            this@TripDetailsActivity,
                            "Documents successfully submitted.",
                            Toast.LENGTH_SHORT
                        ).show()
                        passportStatus = response.body().getResponseData().getDocumentStatus()
                            .getPassportStatus()
                        visaStatus =
                            response.body().getResponseData().getDocumentStatus().getVisaStatus()
                        eIDStatus = response.body().getResponseData().getDocumentStatus()
                            .getEmiratesStatus()
                        permissionStatus =
                            response.body().getResponseData().getDocumentStatus().getConsentStatus()
                        if (response.body().getResponseData().getDocumentStatus()
                                .getDocumentCompletionStatus() === 1
                        ) {
                            getTripDetails(tripID)
                        } else {
                            showDocumentSubmissionPopUp()
                        }
                    } else {
                        Toast.makeText(
                            this@TripDetailsActivity,
                            "Documents submit failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else Toast.makeText(
                    this@TripDetailsActivity,
                    "Documents submit failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<TripDocumentSubmitResponseModel>, t: Throwable) {
                progressDialogP.dismiss()
                Log.e("Response", t.localizedMessage)
            }
        })
    }

}