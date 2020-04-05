package com.example.corona


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = MyViewPagerAdapter(supportFragmentManager)

    adapter.addFragment(Fragment1(), "Updates")
    adapter.addFragment(Fragment2(), "History")
    adapter.addFragment(Fragment3(), "Patients")
    adapter.addFragment(Fragment4(), "Help")
    viewPager.adapter = adapter
    Tab.setupWithViewPager(viewPager)
    Tab.getTabAt(0)?.setIcon(R.drawable.people_add)
    Tab.getTabAt(1)?.setIcon(R.drawable.bug)
    Tab.getTabAt(2)?.setIcon(R.drawable.hospital1)
    Tab.getTabAt(3)?.setIcon(R.drawable.info)
}

}



