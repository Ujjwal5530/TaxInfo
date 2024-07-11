package com.example.taxinfo.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taxinfo.modelClass.TaxDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TaxViewModel : ViewModel() {

    private lateinit var reference: DatabaseReference

    private val _taxDetails = MutableLiveData<ArrayList<TaxDetails>>()
    val taxDetails : LiveData<ArrayList<TaxDetails>> get() = _taxDetails
    fun getTaxDetails(context : Context) {


        val list = arrayListOf<TaxDetails>()
        reference = FirebaseDatabase.getInstance().getReference().child("Tax Details")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(snap in snapshot.children){
                        val taxData = snap.getValue(TaxDetails::class.java)
                        if (taxData != null) list.add(taxData)
                    }
                    _taxDetails.value = list
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toException().localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

//    fun saveTaxDetails(id : String, amount : String, itemSelected : String, context : Context, view : View){
//        repo.saveTaxDetails(id, amount, itemSelected, context, view )
//    }

}