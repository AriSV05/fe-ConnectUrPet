package com.example.connecturpet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.connecturpet.databinding.FragmentPetListAdopterBinding
import com.example.connecturpet.model.Item

class PetListAdopterFrag : Fragment() {
    private var _binding: FragmentPetListAdopterBinding? = null
    private val binding get() = _binding!!
    private val itemList = mutableListOf<Item>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPetListAdopterBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.goBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_petListAdopterFrag_to_adoptEditProfFrag)
        }

        // Configurar RecyclerView
        val recyclerView = binding.listPetRv
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Agregar elementos de ejemplo
        itemList.add(Item("Mascota 1"))
        itemList.add(Item("Masacota 2"))
        itemList.add(Item("Mascota 3"))


        return root
    }
}