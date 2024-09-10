package com.example.connecturpet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.connecturpet.adapter.GiverHomeAdapter
import com.example.connecturpet.databinding.FragmentGiverHomeBinding
import com.example.connecturpet.model.PetsOfGiverRequest
import com.example.connecturpet.repository.PetRepository
import com.example.connecturpet.service.PetService
import com.example.connecturpet.viewModel.PetViewModel
import com.example.connecturpet.viewModel.PetViewModelFactory
import com.example.connecturpet.viewModel.SharedViewModel


class GiverHomeFrag : Fragment() {

    private var _binding: FragmentGiverHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var petViewModel: PetViewModel
    private val shareViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGiverHomeBinding.inflate(inflater, container, false)
        val root = binding.root

        val petService = PetService.getInstance()
        val petRepository = PetRepository(petService)
        petViewModel =
            ViewModelProvider(this, PetViewModelFactory(petRepository))[PetViewModel::class.java]

        shareViewModel.userId.observe(viewLifecycleOwner) { userId ->
            val request =  PetsOfGiverRequest(giverID = userId.toString())
            petViewModel.petsOfGiver(request)

            binding.newPetBtn.setOnClickListener {
                findNavController().navigate(GiverHomeFragDirections.actionGiverHomeFragToAddPetProfileFrag(userId.toString()))
            }
        }

        val navController = findNavController()

        val adapter = GiverHomeAdapter(navController)

        val recyclerView = binding.viewPagerPets
        recyclerView.adapter = adapter

        petViewModel.petsGiver.observe(viewLifecycleOwner) { giverPetsResponse ->
            giverPetsResponse?.let { response ->
                adapter.submitList(response.petsData)
            }
        }

        return root
    }


}