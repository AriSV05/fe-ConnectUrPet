package com.example.connecturpet.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.connecturpet.R
import com.example.connecturpet.databinding.NotiCardBinding
import com.example.connecturpet.model.Noti

class NotisAdapter() :

    RecyclerView.Adapter<NotisAdapter.ViewHolder>() {
    private var notisList: List<Noti> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotisAdapter.ViewHolder {
        val binding: NotiCardBinding = NotiCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notisList[position]
        holder.binding.notifName.text = item.message

        holder.binding.shadowAlabaster.alpha = if (item.view) 0f else 1f

    }

    override fun getItemCount(): Int {
        return notisList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(notis: List<Noti>) {
        this.notisList = notis
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: NotiCardBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.notifConfirm.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = notisList[position]

                    item.view = !item.view

                    notifyItemChanged(position)
                }
            }
        }
    }
}