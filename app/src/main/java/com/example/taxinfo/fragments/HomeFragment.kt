package com.example.taxinfo.fragments

import android.os.Bundle
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
import com.example.taxinfo.databinding.FragmentHomeBinding
import com.example.taxinfo.modelClass.TaxDetails
import com.example.taxinfo.viewmodel.TaxViewModel
import com.google.firebase.database.DatabaseReference

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private lateinit var reference: DatabaseReference

    private val taxViewModel : TaxViewModel by viewModels()
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
        recyclerView = binding.recyclerView

        // getting list from viewmodel/livedata

        taxViewModel.taxDetails.observe(viewLifecycleOwner){
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            if (it.isNotEmpty()) recyclerView.adapter = HomeAdapter(requireContext(), it)
        }

        taxViewModel.getTaxDetails(requireContext())

        return binding.root
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}