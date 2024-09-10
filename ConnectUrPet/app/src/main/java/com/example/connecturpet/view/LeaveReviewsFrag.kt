package com.example.connecturpet.view

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.connecturpet.R
import com.example.connecturpet.databinding.FragmentLeaveReviewsBinding
import com.example.connecturpet.model.ReviewInput
import com.example.connecturpet.repository.MainRepository
import com.example.connecturpet.service.UsersServices
import com.example.connecturpet.viewModel.ReviewViewModel
import com.example.connecturpet.viewModel.SharedViewModel
import com.example.connecturpet.viewModel.ViewModelFactory


class LeaveReviewsFrag : Fragment() {
    private var _binding: FragmentLeaveReviewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var reviewViewModel: ReviewViewModel
    private val shareViewModel: SharedViewModel by activityViewModels()
    private val args: LeaveReviewsFragArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLeaveReviewsBinding.inflate(inflater, container, false)
        val root = binding.root

        //------------------------------------------------------------------------------------------

        val datosSpinner: Array<String> = arrayOf("1","2","3","4","5")

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            datosSpinner
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.puntuation.adapter = adapter

        //------------------------------------------------------------------------------------------

        val usersServices = UsersServices.getInstance()
        val mainRepository = MainRepository(usersServices)

        reviewViewModel = ViewModelProvider(this, ViewModelFactory(mainRepository))[ReviewViewModel::class.java]


        binding.leaveReview.setOnClickListener {
            val adopterID: String = shareViewModel.userId.value ?: ""
            val giverID: String = args.giverID
            val text = binding.reviewDesc.text.toString()
            val puntuation = Integer.parseInt(binding.puntuation.selectedItem.toString())

            val reviewAddInput = ReviewInput(
                text = text,
                puntuation = puntuation,
                adopterId = adopterID,
                giverId = giverID,
            )
            reviewViewModel.addReviewToGiver(reviewAddInput)

            val inflater: LayoutInflater = layoutInflater
            val layout: View = inflater.inflate(R.layout.custom_card, null)

            val textView: TextView = layout.findViewById(R.id.custom_text)
            textView.text = "Review guardada"

            // Crea y muestra el Toast
            with (Toast(context)) {
                duration = Toast.LENGTH_LONG
                view = layout
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }

        binding.goBackArrow.setOnClickListener {
            try {
                // Intenta navegar con la primera acción
                findNavController().navigate(R.id.action_leaveReviewsFrag_to_giverProfileFrag)
            } catch (e: Exception) {
                // Si ocurre un error, navega con la segunda acción
                findNavController().navigate(R.id.action_leaveReviewsFrag_to_giverProfileFrag)
            }
        }

        // Inflate the layout for this fragment
        return root
    }
}