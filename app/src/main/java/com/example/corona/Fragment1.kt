package com.example.corona

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.corona.Retrofit.Regional
import com.google.android.material.snackbar.Snackbar


 class Fragment1 : Fragment() ,DownloadJson.DataFetchListener{

lateinit var  listView:ListView
   lateinit var search:SearchView
 var listFinal:ArrayList<Regional> = ArrayList<Regional>()
    var listFinal2:ArrayList<Regional> = ArrayList<Regional>()
var truth :Boolean =true
    lateinit var progressBar:ProgressBar

     override fun onDataFetchCompleted(data: Any?) {

        val adapter=Adapter1(data as ArrayList<Regional>)

         progressBar.progress = 100
         progressBar.visibility=View.GONE
         search.queryHint="Search"
         view?.let {
             val snackbar1= Snackbar.make(it, "${data.size} LOADED", Snackbar.LENGTH_LONG)
                 .setAction("Action", null)
             snackbar1.setTextColor(Color.DKGRAY)
             val sbView: View = snackbar1.getView()
             sbView.setBackgroundColor(Color.WHITE)
             snackbar1.show()

         }

                search.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
    override fun onQueryTextSubmit(query: String?): Boolean {
return false
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onQueryTextChange(newText: String?): Boolean {

truth=false
        val   char : String? =newText
        if (char!=null) {
            listFinal2.clear()
            listFinal.clear()

            data.forEach {
                if (it.loc.toLowerCase().contains(char) || it.loc.contains(char)) {
                        listFinal.add(it)
                    val snackbar=
                        view?.let { it1 ->
                            Snackbar.make(it1, "${listFinal.size} RESULTS MATCHED..", Snackbar.LENGTH_LONG)
                                .setAction("Action", null)
                        }
                    snackbar?.setTextColor(Color.DKGRAY)
                    val sbView: View? = snackbar?.view
                    sbView?.setBackgroundColor(Color.WHITE)
                    snackbar?.show()
                }
                }
            val adapter1 = Adapter1(listFinal)
                listView.adapter = adapter1

           }
            return true}
})
        listView.adapter=adapter
     }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view:View=inflater.inflate(R.layout.fragment_1, container, false)
        listView=view.findViewById(R.id.listView)
        search=view.findViewById(R.id.search1)

      progressBar=view.findViewById(R.id.progressBar)
    if(isNetworkAvailable())

        DownloadJson(this,activity).execute("https://api.rootnet.in/covid19-in/stats/latest")

        return view
    }

     private fun isNetworkAvailable(): Boolean {
         val connectivityManager =activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
         val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
         return activeNetworkInfo != null && activeNetworkInfo.isConnected
     }


}
