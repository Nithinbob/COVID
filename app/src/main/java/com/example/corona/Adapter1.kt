package com.example.corona

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.corona.Retrofit.Regional
import kotlinx.android.synthetic.main.cardview_item.view.*
import kotlin.collections.ArrayList


class Adapter1(

    var list: ArrayList<Regional>
) :BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater=LayoutInflater.from(parent?.context)
        val data =layoutInflater.inflate(R.layout.cardview_item,parent,false)
        data.text2.append("\n\n      "+ list.get(position).confirmedCasesForeign.toString())
       data.text1.append("\n   "+list.get(position).confirmedCasesIndian.toString())
       data.text3.append("\n\n     "+list.get(position).discharged.toString())
      data.text4.append("\n\n       "+list.get(position).deaths.toString())
  data.Location.text=list.get(position).loc
        return data
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
return position.toLong()
    }

    override fun getCount(): Int {
return list.size
    }

}