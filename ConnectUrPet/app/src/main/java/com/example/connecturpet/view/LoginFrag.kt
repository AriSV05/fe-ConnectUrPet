package com.example.connecturpet.view

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.connecturpet.MainActivity
import com.example.connecturpet.R
import com.example.connecturpet.databinding.FragmentLoginBinding
import com.example.connecturpet.model.LoggedInUserView
import com.example.connecturpet.model.LoginRequest
import com.example.connecturpet.service.LoginService
import com.example.connecturpet.service.PetService
import com.example.connecturpet.service.ServiceBuilder
import com.example.connecturpet.service.UsersServices
import com.example.connecturpet.viewmodel.LoginViewModel
import com.example.connecturpet.viewmodel.LoginViewModelFactory

class LoginFrag : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root = binding.root

        var selected = "heroku"

        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        binding.emailField.text = Editable.Factory.getInstance().newEditable("auser@example.com")
        binding.passwordField.text = Editable.Factory.getInstance().newEditable("12345")

        loginViewModel.loginFormState.observe(requireActivity(), Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            binding.signInBtn.isEnabled = loginState.isDataValid

            if (loginState.emailError != null) {
                binding.emailField.error = getString(loginState.emailError)
            }
            if (loginState.passwordError != null) {
                binding.passwordField.error = getString(loginState.passwordError)
            }
        })


        loginViewModel.loginResponse.observe(requireActivity(), Observer {
            val loginResult = it ?: return@Observer

            //binding.loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            requireActivity().setResult(Activity.RESULT_OK)
        })

        binding.emailField.afterTextChanged {
            loginViewModel.loginDataChanged(
                LoginRequest(
                    username = binding.emailField.text.toString(),
                    password = binding.passwordField.text.toString()
                )
            )
        }

        binding.passwordField.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    LoginRequest(
                        username = binding.emailField.text.toString(),
                        password = binding.passwordField.text.toString()
                    )
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            LoginRequest(
                                username = binding.emailField.text.toString(),
                                password = binding.passwordField.text.toString()
                            )
                        )
                }
                false
            }

            binding.heroku.setOnClickListener {
                selected = "heroku"
            }

            binding.firebase.setOnClickListener {
                selected = "firebase"
            }

            binding.signInBtn.setOnClickListener {
                ServiceBuilder.updateBaseUrl(selected)

                LoginService.reloadInstance()
                PetService.reloadInstance()
                UsersServices.reloadInstance()

                //binding.loading.visibility = View.VISIBLE
                loginViewModel.login(
                    LoginRequest(
                        username = binding.emailField.text.toString(),
                        password = binding.passwordField.text.toString()
                    )
                )
                //Log.d("INICIO: LoginInfo", "Email: ${binding.emailField.text.toString()}, Password: ${binding.passwordField.text.toString()}")
            }
        }

        loginViewModel.loginDataChanged(
            LoginRequest(
                username = binding.emailField.text.toString(),
                password = binding.passwordField.text.toString()
            )
        )

        binding.newBtn.setOnClickListener{
            findNavController().navigate(R.id.action_loginFrag_to_joinAs)}

        return root
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
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

    private fun updateUiWithUser(model: LoggedInUserView) {
        val username = model.username
        val id = model.id
        val userType = model.userType

        //Complete and destroy login activity once successful
        requireActivity().finish()

        // Initiate successful logged in experience
        val intent = Intent(requireActivity(), MainActivity::class.java)

        Log.d( "Usuario", "$username Id: $id tipo: $userType" )
        intent.putExtra("id",id )
        intent.putExtra("userType", userType)
        startActivity(intent)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}