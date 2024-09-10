package com.example.connecturpet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.connecturpet.databinding.FragmentRecommendationsBinding
import com.example.connecturpet.model.OnePetRequest
import com.example.connecturpet.model.RecommendationInput
import com.example.connecturpet.repository.PetRepository
import com.example.connecturpet.service.PetService
import com.example.connecturpet.viewModel.PetViewModel
import com.example.connecturpet.viewModel.PetViewModelFactory

class RecommendationsFrag : Fragment() {
    private var _binding: FragmentRecommendationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var petViewModel: PetViewModel
    private val args: RecommendationsFragArgs by navArgs()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecommendationsBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.goBackArrow.setOnClickListener {
            findNavController().navigate(RecommendationsFragDirections.actionRecommendationsFragToPetProfileFrag())
        }

        val inflater: LayoutInflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.wait_card, null)

        val textView: TextView = layout.findViewById(R.id.custom_text)
        textView.text = "Wait, we're loading..."

        with (Toast(context)) {
            duration = Toast.LENGTH_LONG
            view = layout
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }

        val petService = PetService.getInstance()
        val petRepository = PetRepository(petService)

        petViewModel =
            ViewModelProvider(this, PetViewModelFactory(petRepository))[PetViewModel::class.java]


        petViewModel.onePet(petRequest = OnePetRequest(petID = args.petID))

        petViewModel.onePet.observe(viewLifecycleOwner) { onePet ->
            onePet?.petData?.breed?.let {
                val recomm = RecommendationInput(
                    size = onePet.petData.size,
                    breed = it,
                    sex = onePet.petData.sex,
                    name = onePet.petData.name,
                    bornDate = onePet.petData.bornDate
                )
                petViewModel.recommendations(petRequest = recomm)
            }
        }

        petViewModel.petCareInfo.observe(viewLifecycleOwner) { info ->
            if (info != null) {
                binding.nutritionTv.text =
                    "Nutrition\nFood type: ${info.nutrition.foodType}\nPortion size:${info.nutrition.portionSize}\nSupplements:${info.nutrition.supplements}"
                binding.exerciseTv.text =
                    "Exercise\nActivities: ${info.exercise.activities}\nDaily:${info.exercise.daily}"
                binding.groomingTv.text =
                    "Grooming\nBathing: ${info.grooming.bathing}\nBrushing:${info.grooming.brushing}\nEar cleaning:${info.grooming.earCleaning}\nNail trimming:${info.grooming.nailTrimming}"
                binding.healthCareTv.text = "Health care\nVaccination:${info.healthCare.vaccination}"
            }
        }

        return root
    }
}