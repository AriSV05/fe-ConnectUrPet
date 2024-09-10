package com.example.connecturpet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.connecturpet.databinding.FragmentPetProfileBinding
import com.example.connecturpet.viewModel.PetViewModel
import com.example.connecturpet.databinding.PersonalityViewBinding
import com.example.connecturpet.model.OnePetRequest
import com.example.connecturpet.model.OnePetResponse
import com.example.connecturpet.model.PetVaccinesRequest
import com.example.connecturpet.repository.PetRepository
import com.example.connecturpet.service.PetService
import com.example.connecturpet.viewModel.PetViewModelFactory

class PetProfileFrag : Fragment() {

    private var _binding: FragmentPetProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var petViewModel: PetViewModel
    private val args: PetProfileFragArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPetProfileBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.recommendationsBtn.setOnClickListener {
            findNavController().navigate(PetProfileFragDirections.actionPetProfileFragToRecommendationsFrag(args.idPet))
        }
        binding.goBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_petProfileFrag_to_adopterHomeFrag)
        }
        binding.vaccinesBtn.setOnClickListener {
            findNavController().navigate(PetProfileFragDirections.actionPetProfileFragToViewAllVaccinesFrag(args.idPet))
        }

        val petService = PetService.getInstance()
        val petRepository = PetRepository(petService)

        petViewModel =
            ViewModelProvider(this, PetViewModelFactory(petRepository))[PetViewModel::class.java]

        val request = OnePetRequest(petID = args.idPet)
        petViewModel.onePet(request)

        petViewModel.onePet.observe(viewLifecycleOwner) { petResponse ->
            petResponse?.let { response ->
                binding.petName.text = response.petData.name
                binding.petPersonality.personalityTv.text = response.petData.personality
                binding.petDescription.text = response.petData.description
                binding.bornDateValue.text = response.petData.bornDate
                binding.sizeValue.text = response.petData.size
                binding.breedValue.text = response.petData.breed.name
                binding.specieValue.text = response.petData.breed.specieName
                binding.sexValue.text = response.petData.sex

                val imageResource = when (response.petData.breed.specieName.uppercase()) {
                    "DOG" -> R.drawable.ic_dog
                    "CAT" -> R.drawable.ic_cat
                    "RABBIT" -> R.drawable.ic_rabbit
                    else -> R.drawable.ic_pawprint
                }
                binding.imageProfile.setImageResource(imageResource)

                binding.giverProfile.setOnClickListener {
                    findNavController().navigate(PetProfileFragDirections.actionPetProfileFragToGiverProfileFrag(response.giverID, args.idPet))
                }

            }
        }

        return root
    }
}