package com.example.connecturpet.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.connecturpet.GiverHomeFragDirections
import com.example.connecturpet.R
import com.example.connecturpet.databinding.GiverHomeBinding
import com.example.connecturpet.model.PetData
import com.example.connecturpet.model.PetsOfGiver
import com.example.connecturpet.viewModel.PetViewModel

class GiverHomeAdapter(
    private val navController: NavController,
) : RecyclerView.Adapter<GiverHomeAdapter.ViewPagerViewHolder>() {

    private var petList: List<PetsOfGiver> = emptyList()

    inner class ViewPagerViewHolder(val binding: GiverHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val binding: GiverHomeBinding = GiverHomeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewPagerViewHolder(binding)
    }


    override fun onBindViewHolder(binding: ViewPagerViewHolder, position: Int) {
        val petDetail = petList[position].petData

        binding.binding.petName.text = petDetail.name
        binding.binding.petDescription.text = petDetail.description
        binding.binding.petPersonality.personalityTv.text = petDetail.personality

        val imageResource = when (petDetail.breed.specieName.uppercase()) {
            "DOG" -> R.drawable.ic_dog
            "CAT" -> R.drawable.ic_cat
            "RABBIT" -> R.drawable.ic_rabbit
            else -> R.drawable.ic_pawprint
        }
        binding.binding.imageProfile.setImageResource(imageResource)

        binding.binding.editPetBtn.setOnClickListener {
            val petID = petList[position].petID
            val action = (GiverHomeFragDirections.actionGiverHomeFragToPetEditProfileFrag(petID))
            navController.navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return petList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<PetsOfGiver>) {
        petList = data
        notifyDataSetChanged()
    }
}