package com.example.connecturpet.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.connecturpet.databinding.ViewVaccineCardBinding
import com.example.connecturpet.model.PetVaccine

class AllVaccAdapter() :

    RecyclerView.Adapter<AllVaccAdapter.ViewHolder>() {

    private var petVaccines: List<PetVaccine> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding: ViewVaccineCardBinding = ViewVaccineCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = petVaccines[position]
        holder.binding.vaccineName.text = item.name
    }

    override fun getItemCount(): Int {
        return petVaccines.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(petVaccines: List<PetVaccine>) {
        this.petVaccines = petVaccines
        notifyDataSetChanged()
    }

    //TODO metodo de refresh

    // ViewHolder que contiene las vistas de los elementos del RecyclerView
    inner class ViewHolder(val binding: ViewVaccineCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val textView: TextView = binding.vaccineName

    }
}