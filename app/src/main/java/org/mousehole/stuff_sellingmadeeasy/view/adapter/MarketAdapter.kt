package org.mousehole.stuff_sellingmadeeasy.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.mousehole.stuff_sellingmadeeasy.R
import org.mousehole.stuff_sellingmadeeasy.model.data.Stuff
import org.mousehole.stuff_sellingmadeeasy.viewmodel.MarketViewModel


class MarketAdapter (private var stuffList: List<Stuff>, val markerViewModel: MarketViewModel) :
    RecyclerView.Adapter<MarketAdapter.MarketViewHolder>(){



    inner class MarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val marketItemImage: ImageView = itemView.findViewById(R.id.market_item_image)
        val marketItemName: TextView = itemView.findViewById(R.id.item_name)
        val marketItemPrice : TextView = itemView.findViewById(R.id.market_item_price)
        val marketItemDescription: TextView = itemView.findViewById(R.id.market_item_description)
        val marketItemLocation: TextView = itemView.findViewById(R.id.market_item_location)
        val marketBidders: TextView = itemView.findViewById(R.id.show_bid)
        val marketBidBtn : Button = itemView.findViewById(R.id.bid_market_btn)

        //val stuff= Stuff().itemBidders


    }


    override fun onCreateViewHolder(mparent: ViewGroup, viewType: Int ): MarketAdapter.MarketViewHolder {

        val itemView =
            LayoutInflater.from(mparent.context).inflate(R.layout.market_item_layout, mparent, false)

        return MarketViewHolder(itemView)
    }

    override fun getItemCount(): Int = stuffList.size

    override fun onBindViewHolder(holder: MarketAdapter.MarketViewHolder, position: Int) {

        val stuff = stuffList[position]
        holder.apply {

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(RequestOptions().centerCrop())
                .load(stuff.imageUrl)
                .into(marketItemImage)


            marketItemName.text = stuff.itemName
            marketItemPrice.text = "Only ${stuff.itemPrice} $"
            marketItemDescription.text = stuff.itemDescription
            marketItemLocation.text = stuff.itemLocation

            marketBidders.text= "Number of Bidders: ${stuff.itemBidders}"

            marketBidBtn.setOnClickListener {
                stuff.itemBidders++
                Log.d("TAG_X", "${stuff.itemBidders}")
                markerViewModel.updateStuffItemOne(stuff)
            }
        }
    }


    fun updateMarket(marketList: List<Stuff>){
        this.stuffList = marketList
        notifyDataSetChanged()
    }

}




