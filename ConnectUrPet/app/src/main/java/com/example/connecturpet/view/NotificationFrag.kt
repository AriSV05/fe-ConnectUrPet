package com.example.connecturpet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.connecturpet.adapter.NotisAdapter
import com.example.connecturpet.databinding.FragmentNotificationBinding
import com.example.connecturpet.model.Item
import com.example.connecturpet.model.NotisRequest
import com.example.connecturpet.repository.MainRepository
import com.example.connecturpet.service.UsersServices
import com.example.connecturpet.viewModel.GiverViewModel
import com.example.connecturpet.viewModel.SharedViewModel
import com.example.connecturpet.viewModel.ViewModelFactory

class NotificationFrag : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NotisAdapter

    private lateinit var giverViewModel: GiverViewModel
    private val shareViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)

        // Configurar RecyclerView
        val recyclerView = binding.notisRv
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = NotisAdapter()
        recyclerView.adapter = adapter

        val usersServices = UsersServices.getInstance()
        val mainRepository = MainRepository(usersServices)

        giverViewModel =
            ViewModelProvider(this, ViewModelFactory(mainRepository))[GiverViewModel::class.java]

        shareViewModel.userId.observe(viewLifecycleOwner) { userId ->
            giverViewModel.readNotis(notisRequest = NotisRequest(userId))
        }

        giverViewModel.notis.observe(viewLifecycleOwner){
            if (it != null) {
                adapter.submitList(it.notisList)
            }
        }

        return binding.root
    }


}