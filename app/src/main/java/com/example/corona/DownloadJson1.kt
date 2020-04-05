package com.example.corona


import android.os.AsyncTask
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.ArrayList

class DownloadJson1(val dataFetchListener: DataFetchListener): AsyncTask<String, Int, String>() {
     var  DataFinalList : ArrayList<DataFinal> =ArrayList()
    interface DataFetchListener {
        fun onDataFetchCompleted(DataFinalModel: ArrayList<DataFinal>)
    }
    override fun doInBackground(vararg url: String?): String {
        var json: String = ""
        val url = URL(url[0])
        var connection: HttpsURLConnection = url.openConnection() as HttpsURLConnection
        connection.connect()
        try {
            json = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
        } finally {
            connection.disconnect()
        }
        return json
    }

    override fun onPostExecute(result: String?) {
        val jsonObj: JSONObject = JSONObject(result)

            val DataFinalA: JSONArray = jsonObj.getJSONArray("data")
    for (i in 0 until DataFinalA.length()) {
        val DataFinalo: JSONObject = DataFinalA.getJSONObject(i)

        val DataFinalModel = DataFinal(
            (DataFinalo.get("day") as String),
            (DataFinalo.get("totalSamplesTested") as Any),
          (DataFinalo.get("totalIndividualsTested") as Any).toString(),
            (DataFinalo.get("totalPositiveCases") as Any))

        DataFinalList.add(DataFinalModel)
    }

        dataFetchListener.onDataFetchCompleted(DataFinalList)

    }

}

class DataFinal(
    val day: Any,
    val totalSamplesTested: Any, val totalIndividualsTested:Any,
    val totalPositiveCases: Any
)