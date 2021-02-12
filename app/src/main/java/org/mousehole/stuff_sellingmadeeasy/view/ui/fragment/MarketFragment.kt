package org.mousehole.stuff_sellingmadeeasy.view.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import org.mousehole.stuff_sellingmadeeasy.R
import org.mousehole.stuff_sellingmadeeasy.view.adapter.MarketAdapter
import org.mousehole.stuff_sellingmadeeasy.viewmodel.MarketViewModel


class MarketFragment : Fragment() {


    private lateinit var marketRecyclerView: RecyclerView

    private val marketViewModel : MarketViewModel by viewModels()

    private lateinit var marketAdapter: MarketAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View=  inflater.inflate(R.layout.fragment_market, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getStarted(view)
    }


    private fun getStarted(view: View) {

        marketRecyclerView = view.findViewById(R.id.market_recyclerView)

        marketAdapter = MarketAdapter(listOf(), marketViewModel)

        marketRecyclerView.adapter = marketAdapter

        marketViewModel.getStuff().observe(viewLifecycleOwner, Observer {itemList->
            marketAdapter.updateMarket(itemList)
        })
    }

}