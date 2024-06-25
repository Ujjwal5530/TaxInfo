package com.example.taxinfo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.taxinfo.R
import com.example.taxinfo.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccountFragment : Fragment() {

    private var _binding : FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore : FirebaseFirestore

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        // Dropdown Menu
        val items = listOf("80C", "HouseLoan", "401K")

        val autoCompleteTextView = binding.autoCompleteTextView

        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)

        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.onItemClickListener =
            AdapterView
            .OnItemClickListener { adapterView, view, i, l ->
                val itemSelected = adapterView.getItemAtPosition(i)
            }

        // Get User email from firestore

        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser?.email

        firestore.collection("Users")
            .document(currentUser.toString())
            .get().addOnSuccessListener {
                binding.accEmail.text =  it.get("email").toString()
            }



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}