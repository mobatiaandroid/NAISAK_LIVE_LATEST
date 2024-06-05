package com.nas.naisak.fragment.settings

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.BuildConfig
import com.nas.naisak.R
import com.nas.naisak.activity.cca.adapter.CCAfinalReviewAfterSubmissionAdapter
import com.nas.naisak.activity.login.LoginActivity
import com.nas.naisak.activity.tutorial.TutorialActivity
import com.nas.naisak.commonmodels.DeleteAccountresponseModel
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.CommonMethods.Companion.showCustomToastError
import com.nas.naisak.constants.CommonMethods.Companion.showCustomToastSuccess
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.PreferenceManager.Companion.getUserCode
import com.nas.naisak.constants.progressbar
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener
import com.nas.naisak.fragment.home.model.LogoutResponseModel
import com.nas.naisak.fragment.settings.adapter.SettingsRecyclerAdapter
import com.nas.naisak.fragment.settings.model.ChangePasswordApiModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsFragment : Fragment() {
    lateinit var mContext: Context
    lateinit var titleTextView: TextView
    lateinit var versionText: TextView
    lateinit var notificationRecycler: RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var mSettingsArrayListRegistered : ArrayList<String>
    lateinit var mSettingsArrayListGuest: ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseUI()

    }

    private fun initialiseUI() {
        mContext = requireContext()
        mSettingsArrayListRegistered= ArrayList()
        mSettingsArrayListGuest=ArrayList()
        titleTextView = view?.findViewById(R.id.titleTextView) as TextView
        versionText = view?.findViewById(R.id.versionText) as TextView
        notificationRecycler = view?.findViewById(R.id.notificationRecycler) as RecyclerView
        titleTextView.text=resources.getString(R.string.settings)
        var version:String=BuildConfig.VERSION_NAME
        versionText.text = "V " + version
        linearLayoutManager = LinearLayoutManager(mContext)
        notificationRecycler.layoutManager = linearLayoutManager
        if(PreferenceManager.getUserCode(mContext).equals(""))
        {
            mSettingsArrayListGuest.add(resources.getString(R.string.change_app_settings))
            mSettingsArrayListGuest.add(resources.getString(R.string.terms_of_service))
            mSettingsArrayListGuest.add(resources.getString(R.string.email_us))
            mSettingsArrayListGuest.add(resources.getString(R.string.tutorial))
            mSettingsArrayListGuest.add(resources.getString(R.string.logout))
        }
        else{
            mSettingsArrayListRegistered.add(resources.getString(R.string.change_app_settings))
            mSettingsArrayListRegistered.add(resources.getString(R.string.terms_of_service))
            mSettingsArrayListRegistered.add(resources.getString(R.string.email_us))
            mSettingsArrayListRegistered.add(resources.getString(R.string.tutorial))
            mSettingsArrayListRegistered.add(resources.getString(R.string.change_password))
            mSettingsArrayListRegistered.add(resources.getString(R.string.delete_my_account))
            mSettingsArrayListRegistered.add(resources.getString(R.string.logout))
        }

        if (PreferenceManager.getUserCode(mContext).equals(""))
        {
            val settingsAdapter = SettingsRecyclerAdapter(mSettingsArrayListGuest,mContext)
            notificationRecycler.adapter = settingsAdapter
        }
        else{
            val settingsAdapter = SettingsRecyclerAdapter(mSettingsArrayListRegistered,mContext)
            notificationRecycler.adapter = settingsAdapter
        }
        notificationRecycler.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

                if (PreferenceManager.getUserCode(mContext).equals("")) {
                    // change App Settings
                    if (position == 0) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", activity!!.packageName, null)
                        intent.data = uri
                        mContext.startActivity(intent)
                    }
                    // Terms Of Service
                    else if (position == 1) {
                        val intent = Intent(activity, TermsOfServiceActivity::class.java)
                        activity?.startActivity(intent)
                    }
                    // Email Help
                    else if (position == 2)
                    {
                        val to = "infoalkhor@nais.qa"
                        val email = Intent(Intent.ACTION_SEND)
                        email.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
                        email.type = "message/rfc822"
                        startActivity(Intent.createChooser(email, "Choose an Email client :"))
                    }
                    //Tutorial
                    else if (position==3)
                    {
                        val intent =Intent(activity, TutorialActivity::class.java)
                        intent.putExtra("isFrom", "settings")
                        activity?.startActivity(intent)

                    }
                    else if (position==4){
                        showLogoutDialogue(mContext)
                    }

                }
                else{
                    // change App Settings
                    if (position == 0) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", activity!!.packageName, null)
                        intent.data = uri
                        mContext.startActivity(intent)
                    }
                    // Terms Of Service
                    else if (position == 1) {
                        val intent = Intent(activity, TermsOfServiceActivity::class.java)
                        activity?.startActivity(intent)
                    }
                    // Email Help
                    else if (position == 2)
                    {
                        val to = "infoalkhor@nais.qa"
                        val email = Intent(Intent.ACTION_SEND)
                        email.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
                        email.type = "message/rfc822"
                        startActivity(Intent.createChooser(email, "Choose an Email client :"))
                    }
                    //Tutorial
                    else if (position==3)
                    {
                        val intent =Intent(activity, TutorialActivity::class.java)
                        intent.putExtra("isFrom", "settings")
                        activity?.startActivity(intent)
                    }
                    // Change Password
                    else if (position==4)
                    {
                        showChangePasswordDialogue(mContext)
                    }
                    else if (position==5){
                        deleteAccountDialog()
                    }
                    else if (position==6){
                        showLogoutDialogue(mContext)
                    }

                }


            }

        })

    }


    @SuppressLint("WrongViewCast", "ClickableViewAccessibility")
    fun showChangePasswordDialogue(context: Context)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialogue_change_password)
        var text_currentpassword = dialog.findViewById(R.id.text_currentpassword) as EditText
        var text_currentnewpassword = dialog.findViewById(R.id.text_currentnewpassword) as EditText
        var text_confirmpassword = dialog.findViewById(R.id.text_confirmpassword) as EditText
        var btn_changepassword = dialog.findViewById(R.id.btn_changepassword) as Button
        var progressDialog = dialog.findViewById(R.id.progressDialog) as ProgressBar
        var btn_cancel = dialog.findViewById(R.id.btn_cancel) as Button
        btn_cancel.isClickable = true


        btn_changepassword.alpha = 0.5f
        btn_changepassword.isClickable = false
        var isSigned: Boolean = false


        text_currentpassword.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.toString().trim().equals("")) {
                    btn_changepassword.alpha = 0.5f
                    btn_changepassword.isClickable = false
                    isSigned = false
                } else {
                    if (text_currentnewpassword.text.toString().trim().equals("")) {
                        btn_changepassword.alpha = 0.5f
                        btn_changepassword.isClickable = false
                        isSigned = false
                    } else {
                        if (text_confirmpassword.text.toString().trim().equals("")) {
                            btn_changepassword.alpha = 0.5f
                            btn_changepassword.isClickable = false
                            isSigned = false
                        } else {
                            btn_changepassword.alpha = 1.0f
                            btn_changepassword.isClickable = true
                            isSigned = true
                        }
                    }

                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
                if (s.toString().trim().equals("")) {
                    btn_changepassword.alpha = 0.5f
                    btn_changepassword.isClickable = false
                    isSigned = false
                } else {
                    if (text_currentnewpassword.text.toString().trim().equals("")) {
                        btn_changepassword.alpha = 0.5f
                        btn_changepassword.isClickable = false
                        isSigned = false
                    } else {
                        if (text_confirmpassword.text.toString().trim().equals("")) {
                            btn_changepassword.alpha = 0.5f
                            btn_changepassword.isClickable = false
                            isSigned = false
                        } else {
                            btn_changepassword.alpha = 1.0f
                            btn_changepassword.isClickable = true
                            isSigned = true
                        }
                    }

                }
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (s.toString().trim().equals("")) {
                    btn_changepassword.alpha = 0.5f
                    btn_changepassword.isClickable = false
                    isSigned = false
                } else {
                    if (text_currentnewpassword.text.toString().trim().equals("")) {
                        btn_changepassword.alpha = 0.5f
                        btn_changepassword.isClickable = false
                        isSigned = false
                    } else {
                        if (text_confirmpassword.text.toString().trim().equals("")) {
                            btn_changepassword.alpha = 0.5f
                            btn_changepassword.isClickable = false
                            isSigned = false
                        } else {
                            btn_changepassword.alpha = 1.0f
                            btn_changepassword.isClickable = true
                            isSigned = true
                        }
                    }

                }
            }
        })

        text_currentnewpassword.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.toString().trim().equals("")) {
                    btn_changepassword.alpha = 0.5f
                    btn_changepassword.isClickable = false
                    isSigned = false
                } else {
                    if (text_currentpassword.text.toString().trim().equals("")) {
                        btn_changepassword.alpha = 0.5f
                        btn_changepassword.isClickable = false
                        isSigned = false
                    } else {
                        if (text_confirmpassword.text.toString().trim().equals("")) {
                            btn_changepassword.alpha = 0.5f
                            btn_changepassword.isClickable = false
                            isSigned = false
                        } else {
                            btn_changepassword.alpha = 1.0f
                            btn_changepassword.isClickable = true
                            isSigned = true
                        }
                    }

                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
                if (s.toString().trim().equals("")) {
                    btn_changepassword.alpha = 0.5f
                    btn_changepassword.isClickable = false
                    isSigned = false
                } else {
                    if (text_currentpassword.text.toString().trim().equals("")) {
                        btn_changepassword.alpha = 0.5f
                        btn_changepassword.isClickable = false
                        isSigned = false
                    } else {
                        if (text_confirmpassword.text.toString().trim().equals("")) {
                            btn_changepassword.alpha = 0.5f
                            btn_changepassword.isClickable = false
                            isSigned = false
                        } else {
                            btn_changepassword.alpha = 1.0f
                            btn_changepassword.isClickable = true
                            isSigned = true
                        }
                    }

                }
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (s.toString().trim().equals("")) {
                    btn_changepassword.alpha = 0.5f
                    btn_changepassword.isClickable = false
                    isSigned = false
                } else {
                    if (text_currentpassword.text.toString().trim().equals("")) {
                        btn_changepassword.alpha = 0.5f
                        btn_changepassword.isClickable = false
                        isSigned = false
                    } else {
                        if (text_confirmpassword.text.toString().trim().equals("")) {
                            btn_changepassword.alpha = 0.5f
                            btn_changepassword.isClickable = false
                            isSigned = false
                        } else {
                            btn_changepassword.alpha = 1.0f
                            btn_changepassword.isClickable = true
                            isSigned = true
                        }
                    }

                }
            }
        })

        text_confirmpassword.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.toString().trim().equals("")) {
                    btn_changepassword.alpha = 0.5f
                    btn_changepassword.isClickable = false
                    isSigned = false
                } else {
                    if (text_currentpassword.text.toString().trim().equals("")) {
                        btn_changepassword.alpha = 0.5f
                        btn_changepassword.isClickable = false
                        isSigned = false
                    } else {
                        if (text_currentnewpassword.text.toString().trim().equals("")) {
                            btn_changepassword.alpha = 0.5f
                            btn_changepassword.isClickable = false
                            isSigned = false
                        } else {
                            btn_changepassword.alpha = 1.0f
                            btn_changepassword.isClickable = true
                            isSigned = true
                        }
                    }

                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
                if (s.toString().trim().equals("")) {
                    btn_changepassword.alpha = 0.5f
                    btn_changepassword.isClickable = false
                    isSigned = false
                } else {
                    if (text_currentpassword.text.toString().trim().equals("")) {
                        btn_changepassword.alpha = 0.5f
                        btn_changepassword.isClickable = false
                        isSigned = false
                    } else {
                        if (text_currentnewpassword.text.toString().trim().equals("")) {
                            btn_changepassword.alpha = 0.5f
                            btn_changepassword.isClickable = false
                            isSigned = false
                        } else {
                            btn_changepassword.alpha = 1.0f
                            btn_changepassword.isClickable = true
                            isSigned = true
                        }
                    }

                }
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (s.toString().trim().equals("")) {
                    btn_changepassword.alpha = 0.5f
                    btn_changepassword.isClickable = false
                    isSigned = false
                } else {
                    if (text_currentpassword.text.toString().trim().equals("")) {
                        btn_changepassword.alpha = 0.5f
                        btn_changepassword.isClickable = false
                        isSigned = false
                    } else {
                        if (text_currentnewpassword.text.toString().trim().equals("")) {
                            btn_changepassword.alpha = 0.5f
                            btn_changepassword.isClickable = false
                            isSigned = false
                        } else {
                            btn_changepassword.alpha = 1.0f
                            btn_changepassword.isClickable = true
                            isSigned = true
                        }
                    }

                }
            }
        })



        btn_cancel.setOnClickListener()
        {

            dialog.dismiss()
        }

        btn_changepassword.setOnClickListener()
        {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(text_currentpassword.windowToken, 0)
            val imn = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imn.hideSoftInputFromWindow(text_currentnewpassword.windowToken, 0)
            val imo = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imo.hideSoftInputFromWindow(text_confirmpassword.windowToken, 0)
            if (text_currentpassword.text.toString().trim().equals("")) {
                Toast(mContext).showCustomToastError(
                    resources.getString(R.string.please_enter_current_psswrd),
                    requireActivity()
                )

                //  CommonMethods.showDialogueWithOk(mContext, "Please enter Current Password.", "Alert")
            } else {
                if (text_currentnewpassword.text.toString().trim().equals("")) {
                    Toast(mContext).showCustomToastError(
                        resources.getString(R.string.please_enter_new_psswrd),
                        requireActivity()
                    )
                    // CommonMethods.showDialogueWithOk(mContext, "Please enter New Password.", "Alert")
                } else {
                    if (text_confirmpassword.text.toString().trim().equals("")) {
                        Toast(mContext).showCustomToastError(
                            resources.getString(R.string.please_enter_current_psswrd),
                            requireActivity()
                        )
                        //  CommonMethods.showDialogueWithOk(mContext, "Please enter Current Password.", "Alert")
                    } else{
                        callChangePasswordApi(text_currentpassword.text.toString().trim(),text_currentnewpassword.text.toString().trim(),text_confirmpassword.text.toString(),dialog,progressDialog)
                    }
                }
            }

        }


        dialog.show()
    }



    fun callChangePasswordApi(currentPassword:String, newPassword:String, confirmPassword:String, dialog: Dialog, progressBar: ProgressBar) {
        progressBar.visibility= View.VISIBLE
        val token = PreferenceManager.getUserCode(mContext)
        val changePasswordBody = ChangePasswordApiModel(newPassword, confirmPassword, currentPassword)
        val call: Call<LogoutResponseModel> =
            ApiClient.getClient.changePassword(changePasswordBody, "Bearer " + token)
        call.enqueue(object : Callback<LogoutResponseModel> {
            override fun onFailure(call: Call<LogoutResponseModel>, t: Throwable) {
                //Log.e("Failed", t.localizedMessage)
                progressBar.visibility= View.GONE
            }

            override fun onResponse(call: Call<LogoutResponseModel>, response: Response<LogoutResponseModel>) {
                val responsedata = response.body()
                progressBar.visibility= View.GONE
                //Log.e("Response Signup", responsedata.toString())
                if (responsedata != null) {
                    try {
                        if (response.body()!!.status == 100) {
                            //   showChangePasswordSuccessDialogue(mContext,dialog)
                            Toast(mContext).showCustomToastSuccess(
                                resources.getString(R.string.change_password_success),
                                requireActivity()
                            )
                            dialog.dismiss()
                        } else if (response.body()!!.status == 116) {

                            dialog.dismiss()
                            PreferenceManager.setUserCode(mContext, "")
                            PreferenceManager.setUserEmail(mContext, "")
                            val mIntent = Intent(activity, LoginActivity::class.java)
                            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            mContext.startActivity(mIntent)


                        } else {
                            if (response.body()!!.validationErrorArray.size > 0) {
                                val message =
                                    response.body()!!.validationErrorArray.get(0).toString()
                                Toast(mContext).showCustomToastError(message, requireActivity())
                                // CommonMethods.showDialogueWithOk(mContext,message,"Alert")
                            } else {
                                val msg = response.body()!!.message
                                Toast(mContext).showCustomToastError(msg, requireActivity())
                                // CommonMethods.showDialogueWithOk(mContext,msg,"Alert")
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }

    fun showChangePasswordSuccessDialogue(context: Context,dialogNew: Dialog)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialogue_ok_alert)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as? ImageView
        var alertHead = dialog.findViewById(R.id.alertHead) as? TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as? TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        text_dialog?.text = resources.getString(R.string.change_password_success)
        alertHead?.text = resources.getString(R.string.success)
        iconImageView?.setImageResource(R.drawable.tick)
        btn_Ok.setOnClickListener()
        {
            dialog.dismiss()
            dialogNew.dismiss()
        }
        dialog.show()
    }
    fun showLogoutDialogue(context: Context)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialogue_logout)
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        var btn_Cancel = dialog.findViewById(R.id.btn_Cancel) as Button
        var progress = dialog.findViewById(R.id.progressDialog) as ProgressBar
        progress.visibility=View.GONE
        btn_Ok.setOnClickListener()
        {
            if (PreferenceManager.getUserCode(context).equals(""))
            {
                dialog.dismiss()
                PreferenceManager.setUserCode(context,"")
                PreferenceManager.setUserEmail(context,"")
                val mIntent = Intent(activity, LoginActivity::class.java)
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                com.nas.naisak.fragment.home.mContext.startActivity(mIntent)
            }
            else{
                progress.visibility=View.VISIBLE
                callLogoutApi(context,dialog,progress)
            }


        }
        btn_Cancel.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        dialog.show()
    }
    private fun callLogoutApi(context: Context,dialog: Dialog,progessbar:ProgressBar) {
        progessbar.visibility = View.VISIBLE
        val call: Call<LogoutResponseModel> = ApiClient.getClient.logout("Bearer "+ PreferenceManager.getUserCode(context))
        call.enqueue(object : Callback<LogoutResponseModel> {
            override fun onFailure(call: Call<LogoutResponseModel>, t: Throwable) {
                progessbar.visibility = View.GONE

            }

            override fun onResponse(
                call: Call<LogoutResponseModel>,
                response: Response<LogoutResponseModel>
            ) {
                progessbar.visibility = View.GONE

                if (response.body()!!.status == 100) {

                    dialog.dismiss()
                    PreferenceManager.setUserCode(context, "")
                    PreferenceManager.setUserEmail(context, "")
                    val mIntent = Intent(activity, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    com.nas.naisak.fragment.home.mContext.startActivity(mIntent)

                } else if (response.body()!!.status == 116) {
                    dialog.dismiss()
                    PreferenceManager.setUserCode(com.nas.naisak.fragment.home.mContext, "")
                    PreferenceManager.setUserEmail(com.nas.naisak.fragment.home.mContext, "")
                    val mIntent = Intent(activity, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    com.nas.naisak.fragment.home.mContext.startActivity(mIntent)

                } else {
                    if (response.body()!!.status == 101) {
                        CommonMethods.showDialogueWithOk(com.nas.naisak.fragment.home.mContext, resources.getString(R.string.some_error_occurred), resources.getString(R.string.alert))
                    }
                }

            }

        })
    }
    fun deleteAccountDialog()
    {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_delete_account)
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        var btn_Cancel = dialog.findViewById(R.id.btn_Cancel) as Button
        var progressDialogAdd = dialog.findViewById(R.id.progressDialogAdd) as ProgressBar
        btn_Ok.setOnClickListener()
        {
            var internetCheck = CommonMethods.isInternetAvailable(mContext)
            if (internetCheck) {
                progressDialogAdd.visibility= View.VISIBLE
                deleteAccountApi(dialog)

            } else {
                CommonMethods.showSuccessInternetAlert(mContext)
            }

        }
        btn_Cancel.setOnClickListener()
        {
            dialog.dismiss()
        }
        dialog.show()
    }
    fun deleteAccountApi(dialog:Dialog)   {
        progressbar.visibility=View.VISIBLE
        val token = PreferenceManager.getUserCode(mContext)

        val call: Call<DeleteAccountresponseModel> = ApiClient.getClient.deleteAccount("Bearer "+token)
        call.enqueue(object : Callback<DeleteAccountresponseModel> {
            override fun onFailure(call: Call<DeleteAccountresponseModel>, t: Throwable) {
                dialog.dismiss()
               // Log.e("Failed", t.localizedMessage)
                progressbar.visibility=View.GONE
            }
            override fun onResponse(call: Call<DeleteAccountresponseModel>, response: Response<DeleteAccountresponseModel>) {
                val responsedata = response.body()
                progressbar.visibility=View.GONE
                if (responsedata != null) {
                    try {

                        if (response.body()!!.status==100)
                        {
                            dialog.dismiss()
                            PreferenceManager.setUserCode(mContext,"")
                            dialog.dismiss()
                            val mIntent = Intent(context, LoginActivity::class.java)
                            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            mContext.startActivity(mIntent)
                        }
                        else {
                            dialog.dismiss()
                            CommonMethods.showDialogueWithOk(
                                mContext,
                                getString(R.string.common_error),
                                resources.getString(R.string.alert)
                            )
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }else {
                    dialog.dismiss()
                    CommonMethods.showDialogueWithOk(
                        mContext,
                        getString(R.string.common_error),
                        resources.getString(R.string.alert)
                    )
                }

            }

        })
    }
}