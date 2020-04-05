package com.example.corona

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.cardview2.view.*
import kotlinx.android.synthetic.main.fragment_2.view.*

import kotlin.collections.ArrayList


class Fragment2 : Fragment(),DownloadJson1.DataFetchListener {
    lateinit var recycle2: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var today:TextView
    lateinit var individual:TextView
    lateinit var positive :TextView
    lateinit var sample:TextView
    lateinit var individualF:TextView
    lateinit var positiveF :TextView
    lateinit var sampleF:TextView
lateinit var day:String
    lateinit var month:String
    lateinit var year:String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view1: View = inflater.inflate(R.layout.fragment_2, container, false)
        recycle2= view1.recycle2.findViewById(R.id.recycle2)
        today=view1.findViewById(R.id.today)
        positive=view1.findViewById(R.id.positivenum)
        individual=view1.findViewById(R.id.individualsTestednum)
        individualF=view1.findViewById(R.id.IndividualsTested)
        sample=view1.findViewById(R.id.samplesTestednum)
        sampleF=view1.findViewById(R.id.SamplesTested)
        positiveF=view1.findViewById(R.id.positivecases)
progressBar=view1.findViewById(R.id.progressBar2)

        if (isNetworkAvailable()) {
            val view1: View = inflater.inflate(R.layout.fragment_2, container, false)
            DownloadJson1(this).execute("https://api.rootnet.in/covid19-in/stats/testing/history")

        } else {
            Toast.makeText(activity, "You are offline", Toast.LENGTH_LONG).show()
        }
        return view1
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

 fun convertDateFormat( cData: String): StringBuilder {
      year= cData.substring(0,4)
      month= cData.substring(5,7)
      day= cData.substring(8,10)
     when(month){
         "01"  -> month="Jan"     "02"  -> month="Feb"   "03"  -> month="Mar" "04"  -> month="Apr"  "05"-> month="May"  "06"->month="Jun" "07" ->month= "Jul"  "08"->month="Aug"
         "09"->month="Sep"   "10"->month="Oct"    "11"->month="Nov"   "12"->month="Dec"
     }
     val fdate=StringBuilder("$day  $month, $year")
return fdate
 }
    @SuppressLint("ResourceAsColor")
    override fun onDataFetchCompleted(DataFinalModel: ArrayList<DataFinal>) {
        progressBar.progress = 100
        progressBar.visibility=View.GONE
sampleF.text="Sample tested"
        individualF.text="Individuals tested"

        positiveF.text="Positive cases"

        today.text=convertDateFormat(DataFinalModel[DataFinalModel.size-1].day as String)

        positive.text= DataFinalModel[DataFinalModel.size-1].totalPositiveCases.toString()
        var ChangedtotalIndividualsTested1:String=""
        if(DataFinalModel[DataFinalModel.size-1].totalIndividualsTested.equals("null"))
            ChangedtotalIndividualsTested1 = "NONE"

        else {
            ChangedtotalIndividualsTested1 = DataFinalModel[1].totalIndividualsTested as String
        }
            individual.text= ChangedtotalIndividualsTested1.toString()
        sample.text= DataFinalModel[DataFinalModel.size-1].totalSamplesTested.toString()


        recycle2.layoutManager=LinearLayoutManager(context)
        recycle2.adapter= context?.let { Adapter2(it,DataFinalModel) }
        val snackbar=
            view?.let { it1 ->
                Snackbar.make(it1, "${DataFinalModel.size} LOADED", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
            }
        snackbar?.setTextColor(Color.DKGRAY)
        val sbView: View? = snackbar?.getView()
        sbView?.setBackgroundColor(Color.WHITE)
        snackbar?.show()
    }
}

class Adapter2(
    val context: Context,
    val list: ArrayList<DataFinal>
) : RecyclerView.Adapter<Adapter2.MyViewHolder1>() {

    class MyViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun convertDateFormat( cData: String): StringBuilder {
            val year= cData.substring(0,4)
            var month= cData.substring(5,7)
            val day= cData.substring(8,10)
            when(month){
                "01"  -> month="Jan"  "02"  -> month="Feb""03"  -> month="Mar"     "04"  -> month="Apr"                "05"-> month="May"                "06"->month="Jun"
                "07" ->month= "Jul"      "08"->month="Aug"
                "09"->month="Sep"                "10"->month="Oct"          "11"->month="Nov"                "12"->month="Dec"

            }
            val fdate=StringBuilder("$day  $month, $year")
            return fdate
        }
        fun setData(
            listItem: DataFinal
        ) {
            var ChangedtotalIndividualsTested:String=""
if(listItem.totalIndividualsTested.equals("null")  )
   ChangedtotalIndividualsTested="NONE"
            else
             ChangedtotalIndividualsTested= listItem.totalIndividualsTested as String

            itemView.carddate.text = convertDateFormat(listItem.day as String)
         itemView.cardindivdualtestednum.text= ChangedtotalIndividualsTested
itemView.cardpositivenum.text=listItem.totalPositiveCases.toString()
            itemView.cardsamplestestednum.text=listItem.totalSamplesTested.toString()
        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Adapter2.MyViewHolder1 {
        val view = LayoutInflater.from(context).inflate(R.layout.cardview2, parent, false)
        return Adapter2.MyViewHolder1(itemView = view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Adapter2.MyViewHolder1, position: Int) {

        val listItem = list[position]
        holder.setData(listItem)
    }
}






































