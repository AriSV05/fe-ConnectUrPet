package com.example.connecturpet.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.connecturpet.AdopterHomeFragDirections
import com.example.connecturpet.R
import com.example.connecturpet.databinding.AdopterHomeBinding
import com.example.connecturpet.model.PetData


class AdopHomeAdapter(
    private val navController: NavController,

    ) : RecyclerView.Adapter<AdopHomeAdapter.ViewPagerViewHolder>() {

    private val _selectedPetID = MutableLiveData<String>()
    val selectedPetID: LiveData<String> get() = _selectedPetID

    private var petList: List<PetData> = emptyList()

    inner class ViewPagerViewHolder(val binding: AdopterHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val binding: AdopterHomeBinding = AdopterHomeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewPagerViewHolder(binding)
    }


    override fun onBindViewHolder(binding: ViewPagerViewHolder, position: Int) {
        val petDetail = petList[position].details
        var isListenerSet = false

        val mSpannableString = SpannableString(petDetail.name)
        val colorSpan = ForegroundColorSpan(Color.parseColor("#3531E0"))
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        mSpannableString.setSpan(
            colorSpan,
            0,
            mSpannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.binding.petName.text = mSpannableString

        binding.binding.petDescription.text = petDetail.description
        binding.binding.petPersonality.personalityTv.text = petDetail.personality

        val imageResource = when (petDetail.breed.specieName.uppercase()) {
            "DOG" -> R.drawable.ic_dog
            "CAT" -> R.drawable.ic_cat
            "RABBIT" -> R.drawable.ic_rabbit
            else -> R.drawable.ic_pawprint
        }
        binding.binding.imageHomeAdopPetProfile.setImageResource(imageResource)

        binding.binding.imageHomeAdopPetProfile.setOnClickListener {
            val petID = petList[position].id
            val action = (AdopterHomeFragDirections.actionAdopterHomeFragToPetProfileFrag(petID))
            navController.navigate(action)
        }

        binding.binding.reactionBtn.setOnClickListener {
            val clickedPetID = petList[position].id
            _selectedPetID.value = clickedPetID
            binding.binding.reactionBtn.isVisible = false
        }

    }

    override fun getItemCount(): Int {
        return petList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<PetData>) {
        petList = data
        notifyDataSetChanged()
    }
}