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
import com.example.connecturpet.databinding.FragmentGiverProfileBinding
import com.example.connecturpet.repository.MainRepository
import com.example.connecturpet.service.UsersServices
import com.example.connecturpet.viewModel.GiverViewModel
import com.example.connecturpet.viewModel.SharedViewModel
import com.example.connecturpet.viewModel.ViewModelFactory

class GiverProfileFrag : Fragment() {
    private var _binding: FragmentGiverProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var giverViewModel: GiverViewModel
    private val shareViewModel: SharedViewModel by activityViewModels()

    private val args: GiverProfileFragArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGiverProfileBinding.inflate(inflater, container, false)
        val root = binding.root

        val usersServices = UsersServices.getInstance()
        val mainRepository = MainRepository(usersServices)

        giverViewModel =
            ViewModelProvider(this, ViewModelFactory(mainRepository))[GiverViewModel::class.java]

        giverViewModel.getGiverByID(args.giverID)
        giverViewModel.giver.observe(viewLifecycleOwner){ giver ->
            giver?.let {
                binding.giverName.text = giver.name
                binding.giverDescription.text = giver.description
                binding.giverEmail.text = giver.email
                binding.giverLocation.text = giver.location
            }
        }

        binding.goBackArrow.setOnClickListener {
            findNavController().navigate(GiverProfileFragDirections.actionGiverProfileFragToPetProfileFrag())
        }

        binding.seeReviewsBtn.setOnClickListener {
            findNavController().navigate(GiverProfileFragDirections.actionGiverProfileFragToReviewsFragA(args.giverID))
        }

        binding.leaveReviewBtn.setOnClickListener {
            findNavController().navigate(GiverProfileFragDirections.actionGiverProfileFragToLeaveReviewsFrag(args.giverID))
        }

        return root
    }
}