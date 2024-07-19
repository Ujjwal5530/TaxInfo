package com.example.taxinfo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taxinfo.R
import com.example.taxinfo.adapters.HomeAdapter
import com.example.taxinfo.adapters.OnDelete
import com.example.taxinfo.databinding.FragmentHomeBinding
import com.example.taxinfo.modelClass.TaxDetails
import com.example.taxinfo.modelClass.UserData
import com.example.taxinfo.viewmodel.TaxViewModel
import com.example.taxinfo.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth

    private val taxViewModel : TaxViewModel by viewModels()
    private val userViewModel : UserViewModel by viewModels()

    private var user : UserData? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.plusButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_accountFragment)
        }

        val listener = object : OnDelete {
            override fun onDeleteListener(taxDetails: TaxDetails) {
                taxViewModel.deleteTaxDetails(taxDetails)
            }
        }


        // getting list from viewmodel/livedata

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser?.uid.toString()

        userViewModel.getUserData(currentUser)
        userViewModel.user.observe(viewLifecycleOwner) {
            user = it
            Log.d("UserData1", user.toString())

            taxViewModel.getTaxDetails(user)
            taxViewModel.taxDetails.observe(viewLifecycleOwner){tax ->
                if (tax.isNotEmpty()) {
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.recyclerView.adapter = HomeAdapter(requireContext(), tax, listener)
                }
            }

        }



        return binding.root
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}