package com.nas.naisak.fragment.reports.adapter


import GeneralSubmitResponseModel
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.nas.naisak.R
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.PdfReaderActivity
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.WebviewLoader
import com.nas.naisak.fragment.reports.model.ReportsResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ReportDetailAdapter(
    private var mContext: Context,
    private var repoetDetailArray: ArrayList<ReportsResponseModel.ReportItem>
) :
    RecyclerView.Adapter<ReportDetailAdapter.MyViewHolder>() {
    lateinit var clickedurl: String

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var report_cycle: TextView = view.findViewById(R.id.termname)
        var termnamearab:TextView = view.findViewById(R.id.termnamearab)
        var relativeclick: LinearLayout = view.findViewById(R.id.clickLinear)
        var relativeclickarab:LinearLayout=view.findViewById(R.id.clickLineararab)
        var statusLayout: RelativeLayout = view.findViewById(R.id.statusLayout)
        var statusLayoutarab: RelativeLayout = view.findViewById(R.id.statusLayoutarab)

        var status: TextView = view.findViewById(R.id.status)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_report_cycle, parent, false)
        return MyViewHolder(itemView)
    }


    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val summary = repoetDetailArray[position]
        if (PreferenceManager().getLanguage(mContext).equals("ar"))
        {
            holder.relativeclick.visibility=View.GONE
            holder.relativeclickarab.visibility=View.VISIBLE
            holder.termnamearab.text = repoetDetailArray[position].reportingCycle
            if (repoetDetailArray.get(position).status.equals("0")) {
                Log.e("0","0")
                holder.statusLayoutarab.setVisibility(View.VISIBLE)
                holder.status.setBackgroundResource(R.drawable.rectangle_red)
                holder.status.setText(mContext.getString(R.string.new_keyword))
            } else if (repoetDetailArray.get(position).status.equals("2")) {
                Log.e("1","1")

                holder.statusLayoutarab.setVisibility(View.VISIBLE)
                holder.status.setBackgroundResource(R.drawable.rectangle_orange)
                holder.status.setText(mContext.getString(R.string.updated))
            } else if (repoetDetailArray.get(position).status.equals("")) {
                Log.e("2","2")

                holder.statusLayoutarab.setVisibility(View.GONE)
            } else if (repoetDetailArray.get(position).status.equals("1")) {
                Log.e("3","3")

                holder.statusLayoutarab.setVisibility(View.GONE)
            }

        }
        else
        {
            holder.relativeclick.visibility=View.VISIBLE
            holder.relativeclickarab.visibility=View.GONE
            holder.report_cycle.text = repoetDetailArray[position].reportingCycle
            if (repoetDetailArray.get(position).status.equals("0")) {
                Log.e("0","0")
                holder.statusLayout.setVisibility(View.VISIBLE)
                holder.status.setBackgroundResource(R.drawable.rectangle_red)
                holder.status.setText(mContext.getString(R.string.new_keyword))
            } else if (repoetDetailArray.get(position).status.equals("2")) {
                Log.e("1","1")
                holder.statusLayout.setVisibility(View.VISIBLE)
                holder.status.setBackgroundResource(R.drawable.rectangle_orange)
                holder.status.setText(mContext.getString(R.string.updated))
            } else if (repoetDetailArray.get(position).status.equals("")) {
                Log.e("2","2")
                holder.statusLayout.setVisibility(View.GONE)
            } else if (repoetDetailArray.get(position).status.equals("1")) {
                Log.e("3","3")
                holder.statusLayout.setVisibility(View.GONE)
            }
        }

        holder.relativeclick.setOnClickListener {

            clickedurl = repoetDetailArray[position].fileUrl

            if (repoetDetailArray.get(position).status.equals("0") || repoetDetailArray.get(
                    position
                ).status.equals("2")
            ) {
                callStatusChangeApi(
                    repoetDetailArray.get(position).id,
                    position,
                    repoetDetailArray.get(position).status
                )
            } else {
                if (repoetDetailArray.get(position).fileUrl.endsWith(".pdf")) {
                    val intent = Intent(mContext, PdfReaderActivity::class.java)
                    intent.putExtra("pdf_url", repoetDetailArray.get(position).fileUrl)
                    intent.putExtra("pdf_title", repoetDetailArray.get(position).reportingCycle)

                    mContext.startActivity(intent)
                } else {
                    val intent = Intent(mContext, WebviewLoader::class.java)
                    intent.putExtra("url", repoetDetailArray.get(position).fileUrl)
                    mContext.startActivity(intent)
                }
            }
            // mContext.startActivity(Intent(mContext, WebviewLoad::class.java).putExtra("Url",repoetDetailArray[position].file))


        }
    }

    private fun callStatusChangeApi(id: Int, position: Int, status: String) {

        var token = "Bearer " + PreferenceManager.getUserCode(mContext)
        val paramObject = JsonObject()
        paramObject.addProperty("id", id)
        paramObject.addProperty("type", "report")
        val call: Call<GeneralSubmitResponseModel> =
            ApiClient.getClient.statusChangeAPI("Bearer " + token, paramObject)
        call.enqueue(object : Callback<GeneralSubmitResponseModel> {
            override fun onFailure(call: Call<GeneralSubmitResponseModel>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<GeneralSubmitResponseModel>,
                response: Response<GeneralSubmitResponseModel>
            ) {
                val responsedata = response.body()

                if (responsedata != null) {
                    try {


                        if (status.equals("0", ignoreCase = true) || status.equals(
                                "2",
                                ignoreCase = true
                            )
                        ) {
                            repoetDetailArray.get(position).status = "1"
                            notifyDataSetChanged()
                            if (repoetDetailArray.get(position).fileUrl.endsWith(".pdf")) {
                                val intent = Intent(mContext, PdfReaderActivity::class.java)
                                Log.e("urlasdjha", repoetDetailArray.get(position).fileUrl)
                                intent.putExtra("pdf_url", repoetDetailArray.get(position).fileUrl)
                                intent.putExtra(
                                    "pdf_title",
                                    repoetDetailArray.get(position).reportingCycle
                                )

                                mContext.startActivity(intent)
                            } else {
                                val intent = Intent(mContext, WebviewLoader::class.java)
                                intent.putExtra("url", repoetDetailArray.get(position).fileUrl)
                                mContext.startActivity(intent)
                            }
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }

    override fun getItemCount(): Int {

        return repoetDetailArray.size

    }
}