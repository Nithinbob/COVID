package com.example.corona

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.corona.Contacts.Contacts
import kotlinx.android.synthetic.main.cardview4.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class Fragment4 : Fragment() {
    lateinit var recycler4: RecyclerView
    val ContactsList: ArrayList<Contacts> = ArrayList()
    lateinit var itemView1: View
    lateinit var progressBar: ProgressBar
    var count = 1
    fun pp() {
        val per1 = activity?.let {
            ContextCompat.checkSelfPermission(
                it,
                android.Manifest.permission.CALL_PHONE
            )
        }
        if (per1 != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.CALL_PHONE), 1)
            count = 2
        } else
            count = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_4, container, false)
        recycler4 = view.findViewById<RecyclerView>(R.id.recycle4)
        val www = view.findViewById<ImageView>(R.id.www)
        progressBar = view.findViewById(R.id.progressBar4)
        val face = view.findViewById<ImageView>(R.id.facebook)
        val twitter = view.findViewById<ImageView>(R.id.twitter)
        val number1 = view.findViewById<TextView>(R.id.text4)
        number1.setPaintFlags(number1.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        val number2 = view.findViewById<TextView>(R.id.text5)
        number2.setPaintFlags(number2.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        val email = view.findViewById<TextView>(R.id.text6)
        email.setPaintFlags(email.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        number1.movementMethod = LinkMovementMethod.getInstance()
        number2.movementMethod = LinkMovementMethod.getInstance()
        email.movementMethod = LinkMovementMethod.getInstance()
        number1.setOnClickListener {
            pp()
            val intent11 = Intent(Intent.ACTION_CALL)
            intent11.setData(Uri.parse("tel:" + number1.text))
            if (count == 1)
                startActivity(intent11)

        }

        number2.setOnClickListener {
            pp()
            val intent22 = Intent(Intent.ACTION_CALL)
            intent22.setData(Uri.parse("tel:" + number2.text))
            if (count == 1)
                startActivity(intent22)

        }

        email.setOnClickListener {
            val emailIntent0: Intent = Intent(Intent.ACTION_SEND)
            emailIntent0.putExtra(Intent.EXTRA_EMAIL, "ncov2019@gov.in")
            emailIntent0.setType("message/rfc822")
            startActivity(emailIntent0)

        }

        www.setOnClickListener {
            val browserIntent: Intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://pib.gov.in/newsite/pmreleases.aspx?mincode=31")
            )
            startActivity(browserIntent)
        }
        face.setOnClickListener {
            val browserIntent: Intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/MoHFWIndia"))
            startActivity(browserIntent)
        }
        twitter.setOnClickListener {
            val browserIntent: Intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/MoHFW_INDIA"))
            startActivity(browserIntent)
        }

        recycler4.layoutManager = LinearLayoutManager(context)

        if (isNetworkAvailable()) {
            AsynDownload().execute("https://api.rootnet.in/covid19-in/contacts")
            progressBar.progress = 100
            progressBar.visibility = View.GONE
        } else
            Toast.makeText(context, "You are offline", Toast.LENGTH_LONG).show()

        return view
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    inner class AsynDownload : AsyncTask<String, Int, String>() {

        override fun doInBackground(vararg url: String?): String {
            var json: String = ""
            val url = URL(url[0])
            var connection: HttpsURLConnection = url.openConnection() as HttpsURLConnection
            connection.connect()
            try {
                json =
                    connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            } finally {
                connection.disconnect()
            }
            return json
        }

        override fun onPostExecute(result: String?) {

            val jsonObj: JSONObject = JSONObject(result)
            val dataO: JSONObject = jsonObj.getJSONObject("data")
            val dataC: JSONObject = dataO.getJSONObject("contacts")
            val ContactsA: JSONArray = dataC.getJSONArray("regional")
            for (j in 0 until ContactsA.length()) {
                var ContactO: JSONObject = ContactsA.getJSONObject(j)


                val ContactModel = Contacts(
                    (ContactO.get("loc") as String),
                    (ContactO.get("number") as String)
                )

                ContactsList.add(ContactModel)


                recycler4.adapter = context?.let { MyAdapter2(it, ContactsList) }
            }
        }
    }

    class MyAdapter2(
        val context: Context,
        val list: ArrayList<Contacts>
    ) : RecyclerView.Adapter<Fragment4.MyAdapter2.MyViewHolder1>() {

        class MyViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun setData(
                listItem: Contacts,
                position: Int
            ) {


                itemView.StateText.text = listItem.loc
                itemView.CallId.text = listItem.number


            }

        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): Fragment4.MyAdapter2.MyViewHolder1 {
            val view = LayoutInflater.from(context).inflate(R.layout.cardview4, parent, false)
            return MyAdapter2.MyViewHolder1(itemView = view)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: MyAdapter2.MyViewHolder1, position: Int) {
            val listItem = list[position]
            holder.setData(listItem, position)
        }
    }

}