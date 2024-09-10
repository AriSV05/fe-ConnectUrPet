package com.example.connecturpet.model

data class Review(
    val text: String,
    val puntuation: Int,
)

data class ReviewInput(
    val text: String,
    val puntuation: Int,
    val giverId: String,
    val adopterId: String,
)
