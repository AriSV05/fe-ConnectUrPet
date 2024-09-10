package com.example.connecturpet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.connecturpet.adapter.AllVaccAdapter
import com.example.connecturpet.adapter.VaccinesAdapter
import com.example.connecturpet.databinding.FragmentViewAllVaccinesBinding
import com.example.connecturpet.model.Item
import com.example.connecturpet.model.PetVaccinesRequest
import com.example.connecturpet.repository.PetRepository
import com.example.connecturpet.service.PetService
import com.example.connecturpet.viewModel.PetViewModel
import com.example.connecturpet.viewModel.PetViewModelFactory

class ViewAllVaccinesFrag : Fragment() {
    private var _binding: FragmentViewAllVaccinesBinding? = null
    private val binding get() = _binding!!

    private lateinit var petViewModel: PetViewModel
    private val args: ViewAllVaccinesFragArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewAllVaccinesBinding.inflate(inflater, container, false)
        val root = binding.root


        binding.goBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_viewAllVaccinesFrag_to_petProfileFrag)
        }


        val recyclerView = binding.vaccinesRv
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val petService = PetService.getInstance()
        val petRepository = PetRepository(petService)

        petViewModel =
            ViewModelProvider(this, PetViewModelFactory(petRepository))[PetViewModel::class.java]
        val request = PetVaccinesRequest(petID = args.petID)
        petViewModel.petVaccines(request)

        val adapter = AllVaccAdapter()

        recyclerView.adapter = adapter

        petViewModel.petVaccines.observe(viewLifecycleOwner) { vaccines ->
            vaccines?.let { response ->
                adapter.submitList(response.petVaccines)
            }
        }

        return root
    }

}