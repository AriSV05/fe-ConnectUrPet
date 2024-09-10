package com.example.connecturpet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.connecturpet.databinding.FragmentAdoptEditProfBinding
import com.example.connecturpet.model.UserProfile
import com.example.connecturpet.repository.MainRepository
import com.example.connecturpet.service.UsersServices
import com.example.connecturpet.viewModel.AdopterViewModel
import com.example.connecturpet.viewModel.SharedViewModel
import com.example.connecturpet.viewModel.ViewModelFactory
import com.example.connecturpet.viewmodel.LoginViewModel
import com.example.connecturpet.viewmodel.LoginViewModelFactory


class AdoptEditProfFrag : Fragment() {
    private var _binding: FragmentAdoptEditProfBinding? = null
    private val binding get() = _binding!!

    private lateinit var adopterViewModel: AdopterViewModel
    private val shareViewModel: SharedViewModel by activityViewModels()

    private lateinit var loginViewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdoptEditProfBinding.inflate(inflater, container, false)
        val root = binding.root

        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        val usersServices = UsersServices.getInstance()
        val mainRepository = MainRepository(usersServices)

        adopterViewModel =
            ViewModelProvider(this, ViewModelFactory(mainRepository))[AdopterViewModel::class.java]

        shareViewModel.userId.observe(viewLifecycleOwner) { userId ->
            Log.d("Adopt", "UserID: $userId")
            adopterViewModel.getAdopterByID(userId)
        }

        adopterViewModel.adopter.observe(viewLifecycleOwner) { adopter ->
            adopter?.let {
                binding.editUserProfile.nameField.setText(adopter.name)
                binding.editUserProfile.descriptionField.setText(adopter.description)
                binding.editUserProfile.emailField.setText(adopter.email)
                binding.editUserProfile.locationField.setText(adopter.location)
            }
        }

        binding.editUserProfile.saveBtn.setOnClickListener {
            val userId = shareViewModel.userId.value ?: return@setOnClickListener

            val name = binding.editUserProfile.nameField.text.toString()
            val description = binding.editUserProfile.descriptionField.text.toString()
            val email = binding.editUserProfile.emailField.text.toString()
            val location = binding.editUserProfile.locationField.text.toString()

            val adopterEditRequest = UserProfile(
                id = userId,
                name = name,
                description = description,
                email = email,
                location = location
            )

            adopterViewModel.editAdopterByID(userId, adopterEditRequest)

            val inflater: LayoutInflater = layoutInflater
            val layout: View = inflater.inflate(R.layout.custom_card, null)

            val textView: TextView = layout.findViewById(R.id.custom_text)
            textView.text = "Cambio guardado"

            // Crea y muestra el Toast
            with (Toast(context)) {
                duration = Toast.LENGTH_LONG
                view = layout
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }

        binding.editUserProfile.logoutBtn.setOnClickListener {
            loginViewModel.logout()
            requireActivity().finish()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

        return root
    }
}