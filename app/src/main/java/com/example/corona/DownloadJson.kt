package com.example.corona

import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.fragment.app.FragmentActivity
import com.example.corona.Retrofit.Regional
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

class DownloadJson(
    val dataFetchListener: DataFetchListener,
   val activity: FragmentActivity?
): AsyncTask<String, Int, String>() {


    interface DataFetchListener {
        fun onDataFetchCompleted(data: Any?)
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    override fun doInBackground(vararg url: String?): String {
        var json: String = ""
            val url = URL(url[0])
            var connection: HttpsURLConnection? = null
            try {
                connection = url.openConnection() as HttpsURLConnection
                connection.connect()
                json =
                    connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            } catch (e: Exception) {}

        return json
    }


    override fun onPostExecute(result: String?) {
        val MyModelListR = ArrayList<Regional>()

        val jsonObj: JSONObject = JSONObject(result)
        val dataO: JSONObject = jsonObj.getJSONObject("data")

        var modelR: Regional? = null
        MyModelListR.clear()
        val regionalA: JSONArray = dataO.getJSONArray("regional")

        for (j in 0 until regionalA.length()) {

            var regionalO: JSONObject = regionalA.getJSONObject(j)
            modelR = Regional(
                (regionalO.get("loc") as String),
                (regionalO.get("confirmedCasesIndian") as Int),
                (regionalO.get("confirmedCasesForeign") as Int),
                (regionalO.get("deaths") as Int),
                (regionalO.get("confirmedCasesForeign") as Int)
            )
            MyModelListR.add(modelR)
            regionalO.toString()

        }

        dataFetchListener.onDataFetchCompleted(MyModelListR)

    }

}

