package org.mousehole.stuff_sellingmadeeasy.view.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.mousehole.stuff_sellingmadeeasy.R
import org.mousehole.stuff_sellingmadeeasy.view.adapter.MarketFragmentAdapter


// 3- This is where market or sell fragment will be shown
// The main page after login

class StuffMainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private lateinit var navigationView: BottomNavigationView
    private lateinit var mainViewPager: ViewPager
    private lateinit var marketfragmentAdapter: MarketFragmentAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stuff_main_page)

        marketfragmentAdapter = MarketFragmentAdapter(supportFragmentManager)

        mainViewPager = findViewById(R.id.main_viewpager)

        mainViewPager.adapter = marketfragmentAdapter

        mainViewPager.addOnPageChangeListener(this)

        navigationView= findViewById(R.id.my_bottom_view)

        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.market_place -> loadFragment(0)
                R.id.sell_item -> loadFragment(1)
                else -> loadFragment(2)
            }
            true
        }
    }

    private fun loadFragment(fragmentId: Int){
        mainViewPager.currentItem = fragmentId

    }


    override fun onPageScrollStateChanged(state: Int) {
        // Nothing here
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        // no need for this
    }

    override fun onPageSelected(position: Int) {

        navigationView.selectedItemId = when (position) {
            0 -> R.id.market_place
            1 -> R.id.sell_item
            else -> R.id.user_profile
        }
    }
}