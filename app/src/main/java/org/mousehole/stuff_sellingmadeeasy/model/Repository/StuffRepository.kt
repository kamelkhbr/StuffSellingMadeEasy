package org.mousehole.stuff_sellingmadeeasy.model.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.mousehole.stuff_sellingmadeeasy.model.data.Stuff

object StuffRepository {


    private val stuffLiveData: MutableLiveData<List<Stuff>> = MutableLiveData()


    val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()


    init {
        firebaseDatabase.setPersistenceEnabled(true)
    }

    fun getStuff(): LiveData<List<Stuff>> {

        firebaseDatabase.reference.child("STUFF")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG_X", "Error ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    val stuffList = mutableListOf<Stuff>()
                    snapshot.children.forEach {
                        it.getValue(Stuff::class.java)?.let { stuff ->
                            stuffList.add(stuff)
                        }
                    }
                    stuffLiveData.value = stuffList
                }

            })

        return stuffLiveData
    }

    fun postStuff(stuff: Stuff){

        firebaseDatabase.reference.child("STUFF").child(stuff.idKey).setValue(stuff)
        Log.d("TAG_X", "SHARE POSTED!")
    }

    fun updateStuffItemOne(stuff: Stuff){

        firebaseDatabase.reference.child("STUFF").child(stuff.idKey).setValue(stuff)
    }

}