package com.example.connecturpet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.connecturpet.adapter.AdopHomeAdapter
import com.example.connecturpet.databinding.FragmentAdopterHomeBinding
import com.example.connecturpet.model.OnePetRequest
import com.example.connecturpet.model.ReactionInput
import com.example.connecturpet.repository.MainRepository
import com.example.connecturpet.repository.PetRepository
import com.example.connecturpet.service.PetService
import com.example.connecturpet.service.UsersServices
import com.example.connecturpet.viewModel.AdopterViewModel
import com.example.connecturpet.viewModel.PetViewModel
import com.example.connecturpet.viewModel.PetViewModelFactory
import com.example.connecturpet.viewModel.SharedViewModel
import com.example.connecturpet.viewModel.ViewModelFactory

class AdopterHomeFrag : Fragment() {

    private var _binding: FragmentAdopterHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adopterViewModel: AdopterViewModel
    private val shareViewModel: SharedViewModel by activityViewModels()

    private lateinit var petViewModel: PetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdopterHomeBinding.inflate(inflater, container, false)
        val root = binding.root

        val petService = PetService.getInstance()
        val petRepository = PetRepository(petService)

        petViewModel =
            ViewModelProvider(this, PetViewModelFactory(petRepository))[PetViewModel::class.java]
        petViewModel.giversPets()

        val usersServices = UsersServices.getInstance()
        val mainRepository = MainRepository(usersServices)

        adopterViewModel =
            ViewModelProvider(this, ViewModelFactory(mainRepository))[AdopterViewModel::class.java]

        val navController = findNavController()

        val adapter = AdopHomeAdapter(navController)

        val recyclerView = binding.viewPagerPets
        recyclerView.adapter = adapter

        petViewModel.allPets.observe(viewLifecycleOwner) { allPetsResponse ->
            allPetsResponse?.let { response ->
                adapter.submitList(response.petData)
            }
        }

        shareViewModel.userId.observe(viewLifecycleOwner) { userId ->
            adapter.selectedPetID.observe(viewLifecycleOwner) { petID ->
                petViewModel.onePet(petRequest = OnePetRequest(petID))
                petViewModel.onePet.observe(viewLifecycleOwner) {
                    adopterViewModel.addReaction(
                        reactionIn = ReactionInput(
                            pet = petID,
                            adopter = userId.toString(),
                            giver = it?.giverID ?: ""
                        )
                    )
                }

            }

        }

        return root

    }

}