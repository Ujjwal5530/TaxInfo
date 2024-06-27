package com.example.taxinfo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.taxinfo.R
import com.example.taxinfo.databinding.FragmentAccountBinding
import com.example.taxinfo.modelClass.TaxDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class AccountFragment : Fragment() {

    private var _binding : FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore : FirebaseFirestore

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var firebaseDatabase: DatabaseReference

    private var itemSelected : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        // Dropdown Menu
        val items = listOf("80C", "HouseLoan", "401K","Education")

        val autoCompleteTextView = binding.autoCompleteTextView

        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)

        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.onItemClickListener =
            AdapterView
            .OnItemClickListener { adapterView, view, i, l ->
                itemSelected = adapterView.getItemAtPosition(i).toString()
            }

        // Get User email from firestore

        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser?.email.toString()

        firestore.collection("Users")
            .document(currentUser)
            .get().addOnSuccessListener {
                binding.accEmail.text =  it.get("email").toString()
            }

        //Store Tax detail values in realtime database

        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Tax Details")
        val id = firebaseDatabase.push().key!!

        binding.save.setOnClickListener {
            val amount = binding.amount.text.toString()

            if (itemSelected != null && amount.isNotEmpty()){
                firebaseDatabase.child(id)
                    .setValue(TaxDetails(amount, itemSelected)).addOnSuccessListener {child ->
                        Navigation.findNavController(it).navigate(R.id.action_accountFragment_to_homeFragment)
                        Toast.makeText(requireContext(), "Tax Details Added", Toast.LENGTH_SHORT).show()
                    }
            } else Toast.makeText(requireContext(), "All Fields Required!!", Toast.LENGTH_SHORT).show()


        }

        binding.cancel.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_accountFragment_to_homeFragment)
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}