package com.example.connecturpet.view

import android.app.Activity
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.connecturpet.R
import com.example.connecturpet.databinding.FragmentCreateAccountBinding
import com.example.connecturpet.model.RegisterRequest
import com.example.connecturpet.service.LoginService
import com.example.connecturpet.service.PetService
import com.example.connecturpet.service.ServiceBuilder
import com.example.connecturpet.service.UsersServices
import com.example.connecturpet.viewmodel.CreateAccViewModel
import com.example.connecturpet.viewmodel.CreateAccViewModelFactory

class CreateAccountFrag : Fragment() {

    private var _binding: FragmentCreateAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var createViewModel: CreateAccViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        val root = binding.root
        val userType = arguments?.getString("userType")// Asumiendo "adopter" como valor por defecto

        var selected = "heroku"

        createViewModel =
            ViewModelProvider(this, CreateAccViewModelFactory())[CreateAccViewModel::class.java]


        createViewModel.registerFormState.observe(requireActivity(), Observer {
            val registerState = it ?: return@Observer

            binding.createBtn.isEnabled = registerState.isDataValid
            if (registerState.emailError != null) {
                binding.emailField.error = getString(registerState.emailError)
            }
            if (registerState.nameError != null) {
                binding.nameField.error = getString(registerState.nameError)
            }
            if (registerState.passwordError != null) {
                binding.passwordField.error = getString(registerState.passwordError)
            }

        })

        createViewModel.registerResponse.observe(requireActivity(), Observer {
            val registerResult = it ?: return@Observer

            //binding.loading.visibility = View.GONE
            if (registerResult.error != null) {
                showRegisterFailed(registerResult.error)
            }
            if (registerResult.success != null) {
                findNavController().navigate(R.id.action_createAccountFrag_to_loginFrag)
                requireActivity().setResult(Activity.RESULT_OK)
            }
            requireActivity().setResult(Activity.RESULT_OK)
        })

        fun performRegister() {
            val registerRequest = RegisterRequest(
                email = binding.emailField.text.toString(),
                password = binding.passwordField.text.toString(),
                name = binding.nameField.text.toString()
            )

            if (userType == "adopter") {
                createViewModel.registerAdopter(registerRequest)
            } else {
                createViewModel.registerGiver(registerRequest)
            }

        }

        binding.emailField.afterTextChanged {
            createViewModel.registerDataChanged(
                RegisterRequest(
                    email = binding.emailField.text.toString(),
                    password = binding.passwordField.text.toString(),
                    name = binding.nameField.text.toString()
                )
            )
        }

        binding.nameField.afterTextChanged {
            createViewModel.registerDataChanged(
                RegisterRequest(
                    email = binding.emailField.text.toString(),
                    password = binding.passwordField.text.toString(),
                    name = binding.nameField.text.toString()
                )
            )
        }

        binding.passwordField.apply {
            afterTextChanged {
                createViewModel.registerDataChanged(
                    RegisterRequest(
                        email = binding.emailField.text.toString(),
                        password = binding.passwordField.text.toString(),
                        name = binding.nameField.text.toString()
                    )
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    performRegister()
                    true
                } else {
                    false
                }
            }
        }

        binding.heroku.setOnClickListener {
            selected = "heroku"
        }

        binding.firebase.setOnClickListener {
            selected = "firebase"
        }

        binding.createBtn.setOnClickListener {
            ServiceBuilder.updateBaseUrl(selected)

            LoginService.reloadInstance()
            PetService.reloadInstance()
            UsersServices.reloadInstance()

            performRegister()
        }

        binding.goBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_createAccountFrag_to_joinAs)
        }

        return root
    }

    private fun showRegisterFailed(@StringRes errorString: Int) {
        val inflater: LayoutInflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.custom_card, null)

        val textView: TextView = layout.findViewById(R.id.custom_text)
        textView.text = "Error al hacer login"

        val paw: ImageView = layout.findViewById(R.id.card_paw)
        val color = ContextCompat.getColor(binding.root.context, R.color.Blush)
        paw.setColorFilter(color, PorterDuff.Mode.SRC_IN)

        with (Toast(context)) {
            duration = Toast.LENGTH_LONG
            view = layout
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}