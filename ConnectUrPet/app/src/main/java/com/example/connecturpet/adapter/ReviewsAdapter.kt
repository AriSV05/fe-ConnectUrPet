package com.example.connecturpet.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.connecturpet.databinding.ReviewCardBinding
import com.example.connecturpet.model.Review

class ReviewsAdapter () :

    RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {

        private var reviewList: List<Review> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding: ReviewCardBinding = ReviewCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = reviewList[position]
        holder.binding.reviewTv.text = item.text

        when (item.puntuation) {
            1 -> {
                holder.binding.twoPoint.alpha = 0f
                holder.binding.threePoint.alpha = 0f
                holder.binding.fourPoint.alpha = 0f
                holder.binding.fivePoint.alpha = 0f
            }
            2 -> {
                holder.binding.threePoint.alpha = 0f
                holder.binding.fourPoint.alpha = 0f
                holder.binding.fivePoint.alpha = 0f
            }
            3 -> {
                holder.binding.fourPoint.alpha = 0f
                holder.binding.fivePoint.alpha = 0f
            }
            4 -> {
                holder.binding.fivePoint.alpha = 0f
            }
            // Si la puntuación es 5, no se oculta
        }
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(reviews: List<Review>) {
        reviewList = reviews
        notifyDataSetChanged()
    }
    // ViewHolder que contiene las vistas de los elementos del RecyclerView
    inner class ViewHolder(val binding: ReviewCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val textView: TextView = binding.reviewTv
    }
}