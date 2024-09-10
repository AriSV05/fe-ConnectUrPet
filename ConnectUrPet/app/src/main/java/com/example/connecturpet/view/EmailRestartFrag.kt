package com.example.connecturpet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.connecturpet.databinding.FragmentEmailRestartBinding

class EmailRestartFrag : Fragment() {

    private var _binding: FragmentEmailRestartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmailRestartBinding.inflate(inflater, container, false)
        val root = binding.root


        binding.confirmEmailBtn.setOnClickListener {
            findNavController().navigate(R.id.action_emailRestartFrag_to_repeatPasswordFrag)
        }

        binding.goBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_emailRestartFrag_to_loginFrag)
        }

        return root
    }
}