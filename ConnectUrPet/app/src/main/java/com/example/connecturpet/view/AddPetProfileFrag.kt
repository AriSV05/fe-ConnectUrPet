package com.example.connecturpet

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.connecturpet.databinding.FragmentAddPetProfileBinding
import com.example.connecturpet.model.AddPetRequest
import com.example.connecturpet.model.BreedEdit
import com.example.connecturpet.model.BreedModel
import com.example.connecturpet.model.EditPetRequest
import com.example.connecturpet.model.OnePetRequest
import com.example.connecturpet.model.SpecieModel
import com.example.connecturpet.model.SpeciesAndBreeds
import com.example.connecturpet.model.personalitySpinner
import com.example.connecturpet.model.sexSpinner
import com.example.connecturpet.model.sizeSpinner
import com.example.connecturpet.repository.PetRepository
import com.example.connecturpet.service.PetService
import com.example.connecturpet.viewModel.PetViewModel
import com.example.connecturpet.viewModel.PetViewModelFactory
import java.util.Calendar

class AddPetProfileFrag : Fragment() {

    private var _binding: FragmentAddPetProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var selectedBreedId: String
    private val args: AddPetProfileFragArgs by navArgs()

    private lateinit var petViewModel: PetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPetProfileBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.petEditProfile.calendarBtn.setOnClickListener {
            showDatePicker()
        }

        binding.goBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_addPetProfileFrag_to_giverHomeFrag)
        }

        binding.petEditProfile.vaccinesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addPetProfileFrag_to_petVaccinesFrag)
        }

        val petService = PetService.getInstance()
        val petRepository = PetRepository(petService)

        petViewModel =
            ViewModelProvider(this, PetViewModelFactory(petRepository))[PetViewModel::class.java]
        petViewModel.speciesAndBreeds()

        petViewModel.speciesAndBreeds.observe(viewLifecycleOwner) { petResponse ->
            petResponse?.let { response ->
                loadSpinnerData(response)
            }
        }

        binding.petEditProfile.vaccinesBtn.visibility = View.INVISIBLE
        binding.petEditProfile.addWhen.visibility = View.VISIBLE

        binding.petEditProfile.saveBtn.setOnClickListener {
            val addPetRequest = AddPetRequest(
                idUser = args.idUser,
                name = binding.petNameField.text.toString(),
                personality = binding.petEditProfile.personalityValue.selectedItem.toString(),
                description = binding.petEditProfile.descriptionField.text.toString(),
                bornDate = binding.petEditProfile.bornDateValue.text.toString(),
                size = binding.petEditProfile.sizeValue.selectedItem.toString(),
                breed = BreedEdit(
                    id = selectedBreedId,
                    name = binding.petEditProfile.breedValue.selectedItem.toString()
                ),
                sex = binding.petEditProfile.sexValue.selectedItem.toString()
            )
            petViewModel.giverAddPet(addPetRequest)

            clearInputs()

            val inflater: LayoutInflater = layoutInflater
            val layout: View = inflater.inflate(R.layout.custom_card, null)

            val textView: TextView = layout.findViewById(R.id.custom_text)
            textView.text = "Mascota guardada"

            // Crea y muestra el Toast
            with (Toast(context)) {
                duration = Toast.LENGTH_LONG
                view = layout
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }

        return root
    }

    private fun clearInputs() {
        binding.petNameField.text = Editable.Factory.getInstance().newEditable("")
        binding.petEditProfile.bornDateValue.text = Editable.Factory.getInstance().newEditable("")
        binding.petEditProfile.descriptionField.text = Editable.Factory.getInstance().newEditable("")
    }

    private fun loadSpinnerData(data: SpeciesAndBreeds) {

        val personalitySpinner: List<String> = personalitySpinner.map { it.personality }
        val sizeSpinner: List<String> = sizeSpinner.map { it.size }
        val sexSpinner: List<String> = sexSpinner.map { it.sex }
        val specieModelList: List<SpecieModel> =
            data.species.map { SpecieModel(id = it.id, name = it.name) }
        val breedModelList: List<BreedModel> =
            data.breeds.map {
                BreedModel(
                    id = it.id,
                    breed = it.name,
                    specieID = it.specieID
                )
            }
        val specieSpinner: List<String> = specieModelList.map { it.name }

        val personalitySpinnerAdapter = createSpinnerAdapter(personalitySpinner)
        val specieListSpinnerAdapter = createSpinnerAdapter(specieSpinner)
        val sizeSpinnerAdapter = createSpinnerAdapter(sizeSpinner)
        val sexSpinnerAdapter = createSpinnerAdapter(sexSpinner)

        binding.petEditProfile.personalityValue.adapter = personalitySpinnerAdapter
        binding.petEditProfile.specieValue.adapter = specieListSpinnerAdapter

        binding.petEditProfile.specieValue.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedSpecie = specieModelList[position]

                    // Filtrar las razas basadas en la especie seleccionada
                    val filteredBreeds = breedModelList.filter { it.specieID == selectedSpecie.id }
                    val breedAdapter = createSpinnerAdapter(filteredBreeds.map { it.breed })
                    binding.petEditProfile.breedValue.adapter = breedAdapter

                    selectedBreedId = filteredBreeds.firstOrNull()?.id.toString()

                    val imageResource = when (selectedSpecie.name.uppercase()) {
                        "DOG" -> R.drawable.ic_dog
                        "CAT" -> R.drawable.ic_cat
                        "RABBIT" -> R.drawable.ic_rabbit
                        else -> R.drawable.ic_pawprint
                    }
                    binding.imagePet.setImageResource(imageResource)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Manejar la situación en la que no se ha seleccionado nada
                }
            }

        binding.petEditProfile.sizeValue.adapter = sizeSpinnerAdapter
        binding.petEditProfile.sexValue.adapter = sexSpinnerAdapter

    }

    private fun createSpinnerAdapter(dataList: List<String>): ArrayAdapter<String> {
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, dataList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                // Obtener el año, mes y día seleccionados
                val selectedYearText = "$selectedYear"
                val selectedMonthText =
                    (selectedMonth + 1).toString() // Añadir 1 porque los meses se indexan desde 0
                val selectedDayText = selectedDay.toString()

                // Formatear la fecha seleccionada (por ejemplo, "dd/MM/yyyy")
                val formattedDate = "$selectedDayText/$selectedMonthText/$selectedYearText"

                // Obtener referencia al TextView desde el layout inflado
                val bornDateTextView: TextView = binding.petEditProfile.bornDateValue

                // Establecer la fecha seleccionada en el TextView
                bornDateTextView.text = formattedDate
            },
            year,
            month,
            day
        ).show()
    }


}