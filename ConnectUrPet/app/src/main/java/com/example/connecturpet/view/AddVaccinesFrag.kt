package com.example.connecturpet

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.connecturpet.adapter.VaccinesAdapter
import com.example.connecturpet.databinding.FragmentAddVaccinesBinding
import com.example.connecturpet.model.AddPetVaccineRequest
import com.example.connecturpet.model.PetVaccinesRequest
import com.example.connecturpet.repository.PetRepository
import com.example.connecturpet.service.PetService
import com.example.connecturpet.viewModel.PetViewModel
import com.example.connecturpet.viewModel.PetViewModelFactory


class AddVaccinesFrag : Fragment() {
    private var _binding: FragmentAddVaccinesBinding? = null
    private val binding get() = _binding!!

    private var isPetEditProfile = false
    private lateinit var petViewModel: PetViewModel
    private val args: AddVaccinesFragArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddVaccinesBinding.inflate(inflater, container, false)
        val root = binding.root


        binding.goBackArrow.setOnClickListener {
            if (!findNavController().navigateUp()) {
                // Si no es posible retroceder en la pila de navegación, realiza alguna otra acción
                Log.d("Goback", "No se puede retroceder más en la pila de navegación")
            }
        }

        val recyclerView = binding.vaccinesRv
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val petService = PetService.getInstance()
        val petRepository = PetRepository(petService)

        petViewModel =
            ViewModelProvider(this, PetViewModelFactory(petRepository))[PetViewModel::class.java]
        val request = PetVaccinesRequest(petID = args.petID)
        petViewModel.vaccines(request)

        val adapter = VaccinesAdapter()

        recyclerView.adapter = adapter


        petViewModel.vaccines.observe(viewLifecycleOwner) { vaccines ->
            vaccines?.let { response ->
                adapter.submitList(response.petVaccines, response.allVaccines)
            }
        }


        binding.saveBtn.setOnClickListener {
            val addPetVaccineRequest = AddPetVaccineRequest(
                petID = args.petID,
                vaccinesID = this.updatePetVaccines(adapter)
            )
            petViewModel.addPetVaccine(addPetVaccineRequest)

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

        /* binding.goBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_petVaccinesFrag_to_petEditProfileFrag)


            binding.goBackArrow.setOnClickListener {
                findNavController().navigate(R.id.action_petVaccinesFrag_to_addPetProfileFrag)
            }*/

        return root
    }

    private fun updatePetVaccines(adapter: VaccinesAdapter): List<String> {
        return adapter.selectedVaccines.filter { it.added }
            .map { it.id }
    }
}