package com.example.taxinfo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taxinfo.adapters.HomeAdapter
import com.example.taxinfo.modelClass.TaxDetails
import com.example.taxinfo.modelClass.UserData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.log

class TaxViewModel : ViewModel() {

    private lateinit var database : FirebaseDatabase

    private val _taxDetails = MutableLiveData<ArrayList<TaxDetails>>()
    val taxDetails : LiveData<ArrayList<TaxDetails>> get() = _taxDetails
    fun getTaxDetails(user: UserData?) {

        database = FirebaseDatabase.getInstance()

        val list = arrayListOf<TaxDetails>()
        val reference = database.reference.child("Tax Details")
           .orderByChild("userID").equalTo(user?.id)
        Log.d("ViewmodelData", reference.toString())

        reference.get().addOnSuccessListener {
            for (snap in it.children){
                val taxData = snap.getValue(TaxDetails::class.java)
                if (taxData != null) list.add(taxData)
            }
            Log.d("ViewmodelData2", list.toString())
            _taxDetails.value = list
        }

//        reference.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()){
//                    for(snap in snapshot.children){
//                        val taxData = snap.getValue(TaxDetails::class.java)
//                        if (taxData != null) list.add(taxData)
//                    }
//                    _taxDetails.value = list
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.d("databaseError", error.message)
//            }
//        })

    }

    fun deleteTaxDetails(taxDetails: TaxDetails){
        database = FirebaseDatabase.getInstance()
        database.reference.child("Tax Details").child(taxDetails.id).removeValue()
    }

    fun addTaxDetails(taxDetails: TaxDetails){
        database = FirebaseDatabase.getInstance()
        val reference = database.reference.child("Tax Details").push()
        val uniqueID = reference.key
        if (uniqueID != null) {
            taxDetails.id = uniqueID
        }
        reference.setValue(taxDetails)
    }

}