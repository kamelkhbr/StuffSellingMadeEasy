package org.mousehole.stuff_sellingmadeeasy.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.mousehole.stuff_sellingmadeeasy.view.ui.fragment.MarketFragment
import org.mousehole.stuff_sellingmadeeasy.view.ui.fragment.SellFragment
import org.mousehole.stuff_sellingmadeeasy.view.ui.fragment.UserProfileFragment

class MarketFragmentAdapter (fragmentManager: FragmentManager):
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private val marketFragment= MarketFragment()
    private val sellFragment = SellFragment()
    private val userProfileFragment= UserProfileFragment() // not yet implemented



    override fun getItem(position: Int): Fragment {

        return when(position){
            0 -> marketFragment
            1-> sellFragment
            else -> userProfileFragment
        }
    }
    override fun getCount(): Int = 3
    }