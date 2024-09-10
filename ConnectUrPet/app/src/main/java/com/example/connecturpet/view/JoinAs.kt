package com.example.connecturpet.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.connecturpet.R
import com.example.connecturpet.databinding.FragmentJoinAsBinding

class JoinAs : Fragment() {

    private var _binding: FragmentJoinAsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentJoinAsBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.giverLoginBtn.setOnClickListener {
            navigateToCreateAccount("giver")
        }
        binding.adopterLoginBtn.setOnClickListener {
            navigateToCreateAccount("adopter")
        }
        binding.goBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_joinAs_to_loginFrag)
        }

        return root
    }

    //Pasando argumentos por navArgs a CreateAccount
    private fun navigateToCreateAccount(userType: String) {
        val action = JoinAsDirections.actionJoinAsToCreateAccountFrag(userType)
        findNavController().navigate(action)
    }
}