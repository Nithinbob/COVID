package com.example.corona

import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.corona.Patient.RawPatientData
import kotlinx.android.synthetic.main.cardview_item1.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.ArrayList


class Fragment3 : Fragment() {
lateinit var recycler:RecyclerView
    val RawPatientDataList = ArrayList<RawPatientData>()
    val RawPatientDataList2= ArrayList<RawPatientData>()
    val NoteList=ArrayList<String>()
lateinit var progressBar:ProgressBar
    lateinit var searchBar:SearchView
    var truth:Boolean=true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      val view= inflater.inflate(R.layout.fragment_3, container, false)
        progressBar=view.findViewById(R.id.progressBar3)
        searchBar=view.findViewById(R.id.searchFinal)
        recycler =view.findViewById<RecyclerView>(R.id.recycle)
        recycler.layoutManager=LinearLayoutManager(context)
        if(isNetworkAvailable()) {
            AsynDownload().execute("https://api.rootnet.in/covid19-in/unofficial/covid19india.org")
            progressBar.progress = 100
            progressBar.visibility=View.GONE
        }
else
            Toast.makeText(context,"You are offline",Toast.LENGTH_LONG).show()

        return  view
    }

    override fun onStart() {
        super.onStart()
            searchBar.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                truth = false
                val char: String? = newText
                if (char != null) {
                    RawPatientDataList2.clear()
                    RawPatientDataList.forEach {
                        if (it.city.toLowerCase().contains(char) || it.state.toLowerCase().contains(char) || it.district.toLowerCase().contains(char)) {
                            RawPatientDataList2.add(it)

                        }
                    }
                    recycler.adapter=context?.let { MyAdapter1(it,RawPatientDataList2,NoteList) }

                }
                recycler.adapter=context?.let { MyAdapter1(it,RawPatientDataList2,NoteList) }

                return true
            }
        })
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    inner  class AsynDownload:AsyncTask<String, Int, String>() {
    override fun doInBackground(vararg url: String?): String {
        var json: String = ""
        val url = URL(url[0])
        var connection: HttpsURLConnection = url.openConnection() as HttpsURLConnection
        connection.connect()
        try {
            json = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            Log.v("Downloaded json ", "$json")
        } finally {
            connection.disconnect()
        }
        return json
    }

    override fun onPostExecute(result: String?) {

        val jsonObj: JSONObject = JSONObject(result)
        val dataO: JSONObject = jsonObj.getJSONObject("data")
        val RawPatientDataA: JSONArray = dataO.getJSONArray("rawPatientData")
        for (j in 0 until RawPatientDataA.length()) {
            var RawPatientDataO: JSONObject = RawPatientDataA.getJSONObject(j)

try{
   if (RawPatientDataO.get("notes") !=null)
       NoteList.add(RawPatientDataO.get("notes") as String)
   }catch (e:Exception){}

    val RawPatModel = RawPatientData(
        (RawPatientDataO.get("gender") as String),
        (RawPatientDataO.get("ageEstimate") as String),
        (RawPatientDataO.get("reportedOn") as String),
        (RawPatientDataO.get("patientId") as Int),
        (RawPatientDataO.get("city") as String),
        (RawPatientDataO.get("district") as String),
        (RawPatientDataO.get("state") as String),
        (RawPatientDataO.get("status") as String)

    )

if(RawPatModel.city.equals("Bengaluru"))
    RawPatModel.city="Banaglore"

            RawPatientDataList.add(RawPatModel)
            recycler.adapter=context?.let { MyAdapter1(it,RawPatientDataList,NoteList) }
        }
    }
}
class MyAdapter1(
    val context: Context,
    val list: ArrayList<RawPatientData>,
    val noteList: ArrayList<String>
) :RecyclerView.Adapter<MyAdapter1.MyViewHolder1>(){
    class MyViewHolder1(itemView:View):RecyclerView.ViewHolder(itemView){
        fun setData(
            listItem: RawPatientData,
            position: Int,
            note: String
        ) {
            itemView.Pid.text="  Patient ID  : ${listItem.patientId.toString()}"
            if(listItem.ageEstimate.equals(""))
                itemView.text1_1.text= "--"
            else
itemView.text1_1.text=listItem.ageEstimate
            itemView.text2_2.text=listItem.gender
            itemView.text3_3.text=listItem.district
            itemView.text3_3.append(" - "+listItem.city+" , "+listItem.state)

            itemView.text4_4.text=note
            itemView.text5_5.text=listItem.reportedOn


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter1.MyViewHolder1 {
val view=LayoutInflater.from(context).inflate(R.layout.cardview_item1,parent,false)
        return MyViewHolder1(itemView = view)
    }

    override fun getItemCount(): Int {
return  list.size
    }

    override fun onBindViewHolder(holder: MyAdapter1.MyViewHolder1, position: Int) {
val listItem=list[position]
        val note=noteList[position]
        holder.setData(listItem,position,note)
    }

}
}