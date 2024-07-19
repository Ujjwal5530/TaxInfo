package com.example.taxinfo.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.taxinfo.R
import com.example.taxinfo.activities.MainActivity
import com.example.taxinfo.activities.RegisterActivity
import com.example.taxinfo.databinding.FragmentAccountBinding
import com.example.taxinfo.modelClass.TaxDetails
import com.example.taxinfo.viewmodel.TaxViewModel
import com.example.taxinfo.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class AccountFragment : Fragment() {

    private var _binding : FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth

    private var itemSelected : String? = null

    private val viewModel : TaxViewModel by viewModels()
    private val userViewModel : UserViewModel by viewModels()
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

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser?.uid.toString()

        userViewModel.getUserData(currentUser)
        userViewModel.user.observe(viewLifecycleOwner){
            binding.accEmail.text = it.email
        }

        //logout button

        binding.logout.setOnClickListener {
            userViewModel.logout()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }


        //Store Tax detail values in realtime database

        binding.save.setOnClickListener {
            val amount = binding.amount.text.toString()

            if (itemSelected != null && amount.isNotEmpty()){
                viewModel.addTaxDetails(TaxDetails(amount, itemSelected!!, "", currentUser))
                Navigation.findNavController(it).navigate(R.id.action_accountFragment_to_homeFragment)
                Toast.makeText(context, "Tax Details Added", Toast.LENGTH_SHORT).show()
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