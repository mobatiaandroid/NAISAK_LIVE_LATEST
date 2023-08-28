package com.nas.naisak.constants

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.nas.naisak.R
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.fragment.home.mContext

lateinit var progressbar: ProgressBar
class WebviewLoader : AppCompatActivity() {
    lateinit var back: ImageView

    //lateinit var downloadpdf: ImageView
    lateinit var context: Context
    lateinit var webview: WebView

    var urltoshow: String = ""
    var titleToShow: String = ""

    lateinit var logoclick: ImageView
    lateinit var titleTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_loader)
        context = this
        titleTextView = findViewById(R.id.titleTextView)
        logoclick = findViewById(R.id.logoclick)
        progressbar = findViewById(R.id.progress)
        progressbar.visibility= View.VISIBLE
        urltoshow = intent.getStringExtra("webview_url").toString()
        titleToShow = intent.getStringExtra("title").toString()
        back = findViewById(R.id.back)
        titleTextView.text = titleToShow

        //url=intent.getStringExtra("url")
        //headingValue=intent.getStringExtra("heading")
        initializeUI()
        getWebViewSettings()


    }
    private fun getWebViewSettings() {
       // progressDialogAdd.visibility=View.VISIBLE
        val settings = webview.settings
        settings.domStorageEnabled = true
    }
    private fun initializeUI() {
        // headermanager=HeaderManagerNoColorSpace(SocialMediaDetailActivity.this, "FACEBOOK");
        mContext=this
        webview = findViewById(R.id.webview)
        progressbar = findViewById(R.id.progress)
        progressbar.visibility= View.VISIBLE
        logoclick.setOnClickListener {
            val mIntent = Intent(context, HomeActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(mIntent)
        }
        back.setOnClickListener {
            finish()
        }
        webview.settings.javaScriptEnabled = true
        Log.e("url",urltoshow)
        if (urltoshow.contains("facebook")){
            Log.e("fb","true")
            val userAgent =
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"
            webview.getSettings().setUserAgentString(userAgent)
            webview.webViewClient = MyWebViewClient(this)
            webview.loadUrl(urltoshow!!)
            //progressbar.visibility= View.GONE
        }else{
            Log.e("nofb","true")
            webview.webViewClient = MyWebViewClient(this)
            webview.loadUrl(urltoshow!!)
           // progressbar.visibility= View.GONE
        }

       // progressbar.visibility= View.GONE
    }



    class MyWebViewClient internal constructor(private val activity: Activity) : WebViewClient() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url: String = request?.url.toString()
            view?.loadUrl(url)
            progressbar.visibility= View.GONE
            return true
        }

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            progressbar.visibility= View.GONE
            return true
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            progressbar.visibility= View.GONE
            Log.e("ERROR",error.toString())
            //Toast.makeText(activity, "Got Error! $error", Toast.LENGTH_SHORT).show()
        }
    }
}



        // downloadpdf = findViewById(R.id.downloadpdf)
       /* webview = findViewById(R.id.webview)
        webview.settings.javaScriptEnabled = true
        webview.settings.cacheMode= WebSettings.LOAD_NO_CACHE
        webview.settings.domStorageEnabled=true
        webview.settings.mixedContentMode=WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        webview.settings.javaScriptCanOpenWindowsAutomatically = true
        webview.settings.loadsImagesAutomatically = true

        webview.settings.loadWithOverviewMode=true
        webview.settings.loadWithOverviewMode=true
        webview.settings.builtInZoomControls=true
        webview.settings.displayZoomControls=false
        webview.settings.supportZoom()
        webview.settings.defaultTextEncodingName="utf-8"

        webview.setBackgroundColor(Color.TRANSPARENT)
        webview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)
        progressbar = findViewById(R.id.progress)
        webview.webViewClient = MyWebViewClient(this)
        logoclick.setOnClickListener {
            val mIntent = Intent(context, HomeActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(mIntent)
        }
        back.setOnClickListener {
            finish()
        }

//        if (urltoshow.contains("http")) {
//            urltoshow = urltoshow.replace("http", "https")
//        }
        val userAgent =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"
        webview.getSettings().setUserAgentString(userAgent)
        webview.loadUrl(urltoshow)
        Log.e("LOADINGURL==>",urltoshow)

        webview.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressbar.progress = newProgress
                if (newProgress == 100) {
                    progressbar.visibility = View.GONE
                    back.visibility = View.VISIBLE

                }
            }
        }


    }

    class MyWebViewClient internal constructor(private val activity: Activity) : WebViewClient() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val url: String = request?.url.toString()
            view?.loadUrl(url)
            return true
        }

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)

            return true
        }
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)

        }
        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(mContext)
            var message = "SSL Certificate error."
            when (error.primaryError) {
                SslError.SSL_UNTRUSTED -> message = "The certificate authority is not trusted."
                SslError.SSL_EXPIRED -> message = "The certificate has expired."
                SslError.SSL_IDMISMATCH -> message = "The certificate Hostname mismatch."
                SslError.SSL_NOTYETVALID -> message = "The certificate is not yet valid."
            }
            message += " Do you want to continue anyway?"
            builder.setTitle("SSL Certificate Error")
            builder.setMessage(message)
            builder.setPositiveButton("continue",
                DialogInterface.OnClickListener { dialog, which -> handler.proceed() })
            builder.setNegativeButton("cancel",
                DialogInterface.OnClickListener { dialog, which -> handler.cancel() })
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
        }

    }

}*/