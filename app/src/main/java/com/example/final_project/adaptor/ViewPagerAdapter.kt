package com.example.final_project.adaptor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.final_project.R
import com.example.final_project.fragments.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class ViewPagerAdapter2(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> OrderFragment()
            2 -> ServiceFragment()
            3 -> AccessoryFragment()

            else -> HomeFragment()
        }
    }

    override fun getCount(): Int {
        return 4
    }
}