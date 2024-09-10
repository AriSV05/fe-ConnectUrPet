package com.example.connecturpet
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.connecturpet.adapter.ReviewsAdapter
import com.example.connecturpet.databinding.FragmentReviewsBinding
import com.example.connecturpet.repository.MainRepository
import com.example.connecturpet.service.UsersServices
import com.example.connecturpet.viewModel.ReviewViewModel
import com.example.connecturpet.viewModel.SharedViewModel
import com.example.connecturpet.viewModel.ViewModelFactory

class ReviewsFrag : Fragment() {
    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!

    //private lateinit var adapter: ReviewsAdapter
    private val args: ReviewsFragArgs by navArgs()

    private lateinit var reviewViewModel: ReviewViewModel
    private val shareViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.goBackArrow.setOnClickListener {
            try {
                // Intenta navegar con la primera acción
                findNavController().navigate(R.id.action_reviewsFragGiv_to_givEditProfFrag)
            } catch (e: Exception) {
                // Si ocurre un error, navega con la segunda acción
                findNavController().navigate(R.id.action_reviewsFragAdop_to_giverProfileFrag)
            }
        }

        val adapter = ReviewsAdapter()
        val recyclerView = binding.reviewsRv
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val usersServices = UsersServices.getInstance()
        val mainRepository = MainRepository(usersServices)

        reviewViewModel = ViewModelProvider(this, ViewModelFactory(mainRepository))[ReviewViewModel::class.java]
        reviewViewModel.getGiverReviews(args.giverID)

        reviewViewModel.reviewList.observe(viewLifecycleOwner){
            response ->
            if (response != null) {
                adapter.submitList(response)
            }
        }


        return root
    }
}