package org.mousehole.stuff_sellingmadeeasy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.mousehole.stuff_sellingmadeeasy.model.Repository.StuffRepository
import org.mousehole.stuff_sellingmadeeasy.model.data.Stuff

class MarketViewModel: ViewModel() {


    fun getStuff(): LiveData<List<Stuff>> = StuffRepository.getStuff()

    fun uploadStuff(stuff: Stuff){
        StuffRepository.postStuff(stuff)
    }

    fun updateStuffItemOne(stuff: Stuff){
        StuffRepository.updateStuffItemOne(stuff)
    }

}