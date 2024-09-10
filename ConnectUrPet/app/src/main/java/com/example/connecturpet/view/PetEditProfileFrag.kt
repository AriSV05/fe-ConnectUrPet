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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.connecturpet.databinding.FragmentPetEditProfileBinding
import com.example.connecturpet.model.BreedEdit
import com.example.connecturpet.model.BreedModel
import com.example.connecturpet.model.EditPetRequest
import com.example.connecturpet.model.OnePetEditResponse
import com.example.connecturpet.model.OnePetRequest
import com.example.connecturpet.model.SpecieModel
import com.example.connecturpet.model.personalitySpinner
import com.example.connecturpet.model.sexSpinner
import com.example.connecturpet.model.sizeSpinner
import com.example.connecturpet.repository.PetRepository
import com.example.connecturpet.service.PetService
import com.example.connecturpet.viewModel.PetViewModel
import com.example.connecturpet.viewModel.PetViewModelFactory
import java.util.Calendar


class PetEditProfileFrag : Fragment() {

    private var _binding: FragmentPetEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectedBreedId: String

    private lateinit var petViewModel: PetViewModel
    private val args: PetEditProfileFragArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPetEditProfileBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.goBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_petEditProfileFrag_to_giverHomeFrag)
        }

        binding.petEditProfile.vaccinesBtn.setOnClickListener {
            findNavController().navigate(PetEditProfileFragDirections.actionPetEditProfileFragToPetVaccinesFrag(args.petID))
        }

        binding.petEditProfile.calendarBtn.setOnClickListener {
            showDatePicker()
        }

        val petService = PetService.getInstance()
        val petRepository = PetRepository(petService)

        petViewModel =
            ViewModelProvider(this, PetViewModelFactory(petRepository))[PetViewModel::class.java]
        val request = OnePetRequest(petID = args.petID)
        petViewModel.onePetEdit(request)

        petViewModel.onePetEdit.observe(viewLifecycleOwner) { petResponse ->
            petResponse?.let { response ->
                loadSpinnerData(response)
                binding.petEditProfile.descriptionField.text =
                    Editable.Factory.getInstance().newEditable(response.petData.description)
                binding.petEditProfile.bornDateValue.text = response.petData.bornDate
                binding.petNameField.text =
                    Editable.Factory.getInstance().newEditable(response.petData.name)
            }
        }

        binding.petEditProfile.saveBtn.setOnClickListener {
            val savePetRequest = EditPetRequest(
            petID= args.petID,
            name= binding.petNameField.text.toString(),
            personality= binding.petEditProfile.personalityValue.selectedItem.toString(),
            description= binding.petEditProfile.descriptionField.text.toString(),
            bornDate= binding.petEditProfile.bornDateValue.text.toString(),
            size= binding.petEditProfile.sizeValue.selectedItem.toString(),
            breed= BreedEdit(id=selectedBreedId,name=binding.petEditProfile.breedValue.selectedItem.toString()),
            sex= binding.petEditProfile.sexValue.selectedItem.toString(),
            )
            petViewModel.giverEditPet(savePetRequest)

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

        return root
    }

    private fun loadSpinnerData(editResponse: OnePetEditResponse) {

        val personality = editResponse.petData.personality
        val size = editResponse.petData.size
        val sex = editResponse.petData.sex
        val specie = editResponse.petData.breed.specieName

        val personalitySpinner: List<String> = personalitySpinner.map { it.personality }
        val sizeSpinner: List<String> = sizeSpinner.map { it.size }
        val sexSpinner: List<String> = sexSpinner.map { it.sex }
        val specieModelList: List<SpecieModel> =
            editResponse.species.map { SpecieModel(id = it.id, name = it.name) }
        val breedModelList: List<BreedModel> =
            editResponse.breeds.map { BreedModel(id = it.id, breed = it.name, specieID = it.specieID) }
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
                    val filteredBreeds = breedModelList.filter {it.specieID == selectedSpecie.id}
                    val breedAdapter = createSpinnerAdapter(filteredBreeds.map { it.breed })
                    binding.petEditProfile.breedValue.adapter = breedAdapter

                    selectedBreedId = filteredBreeds.firstOrNull()?.id.toString()

                    val imageResource = when (selectedSpecie.name.uppercase()) {
                        "DOG" -> R.drawable.ic_dog
                        "CAT" -> R.drawable.ic_cat
                        "RABBIT" -> R.drawable.ic_rabbit
                        else -> R.drawable.ic_pawprint
                    }
                    binding.imageProfile.setImageResource(imageResource)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Manejar la situación en la que no se ha seleccionado nada
                }
            }

        binding.petEditProfile.sizeValue.adapter = sizeSpinnerAdapter
        binding.petEditProfile.sexValue.adapter = sexSpinnerAdapter

        val personalityIndex = personalitySpinner.indexOf(personality)
        val sizeIndex = sizeSpinner.indexOf(size)
        val sexIndex = sexSpinner.indexOf(sex)
        val specieIndex = specieSpinner.indexOf(specie)

        binding.petEditProfile.personalityValue.setSelection(personalityIndex)
        binding.petEditProfile.sizeValue.setSelection(sizeIndex)
        binding.petEditProfile.sexValue.setSelection(sexIndex)
        binding.petEditProfile.specieValue.setSelection(specieIndex)

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
            DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
                // Obtener el año, mes y día seleccionados
                val selectedYearText = "$selectedYear"
                val selectedMonthText = (selectedMonth + 1).toString() // Añadir 1 porque los meses se indexan desde 0
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