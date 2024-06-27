package com.example.taxinfo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taxinfo.R
import com.example.taxinfo.adapters.HomeAdapter
import com.example.taxinfo.databinding.FragmentHomeBinding
import com.example.taxinfo.modelClass.TaxDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var reference: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.plusButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_accountFragment)
        }

        // recyclerview

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        list() //calling list adapter

        return binding.root
    }

    private fun list() {
        reference = FirebaseDatabase.getInstance().getReference("Tax Details")
        val list = arrayListOf<TaxDetails>()

        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(snap in snapshot.children){
                        val taxData = snap.getValue(TaxDetails::class.java)
                        list.add(taxData!!)
                    }
                    try {
                        binding.recyclerView.adapter = HomeAdapter(requireContext(), list)
                    }catch (e : NullPointerException){
                        e.localizedMessage?.let { Log.d("NullPoint", it) }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.toException().localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}