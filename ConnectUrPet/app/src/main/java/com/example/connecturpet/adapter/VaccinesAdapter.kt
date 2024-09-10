package com.example.connecturpet.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.connecturpet.databinding.VaccineCardBinding
import com.example.connecturpet.model.PetVaccine
import com.example.connecturpet.model.Vaccine
import com.example.connecturpet.model.VaccinesModel

class VaccinesAdapter(
) :

    RecyclerView.Adapter<VaccinesAdapter.ViewHolder>() {

    var selectedVaccines: MutableList<VaccinesModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: VaccineCardBinding = VaccineCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = selectedVaccines[position]
        holder.binding.vaccineName.text = item.name

        holder.binding.shadowAlabaster.alpha = if (item.added) 0f else 1f

    }

    override fun getItemCount(): Int {
        return selectedVaccines.size
    }

    private fun vaccineListRefactor(
        vaccinesList: List<PetVaccine>,
        allVaccinesList: List<Vaccine>
    ): MutableList<VaccinesModel> {
        data class VaccineInfo(val name: String, val id: String)

        val petVaccines = vaccinesList.map { VaccineInfo(it.name, it.id) }
        val allVaccines = allVaccinesList.map { VaccineInfo(it.name, it.id) }

        val vaccines: MutableList<VaccinesModel> = mutableListOf()

        for (vaccine in allVaccines) {
            if (petVaccines.any { it.id == vaccine.id }) {
                val selectedVaccine =
                    VaccinesModel(id = vaccine.id, name = vaccine.name, added = true)
                vaccines.add(selectedVaccine)
            } else {
                val selectedVaccine =
                    VaccinesModel(id = vaccine.id, name = vaccine.name, added = false)
                vaccines.add(selectedVaccine)
            }
        }
        return vaccines
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(vaccines: List<PetVaccine>, allVaccines: List<Vaccine>) {
        selectedVaccines = vaccineListRefactor(vaccines, allVaccines)
        notifyDataSetChanged()
    }

    // ViewHolder que contiene las vistas de los elementos del RecyclerView
    inner class ViewHolder(val binding: VaccineCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val textView: TextView = binding.vaccineName

        init {
            binding.addVaccine.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = selectedVaccines[position]
                    // Modificar el estado del elemento
                    item.added = true
                    // Notificar al adaptador sobre el cambio en el elemento
                    notifyItemChanged(position)
                }
            }
            binding.deleteVaccine.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = selectedVaccines[position]
                    item.added = false
                    // Notificar al adaptador sobre el cambio en el elemento
                    notifyItemChanged(position)
                }
            }
        }
    }
}
