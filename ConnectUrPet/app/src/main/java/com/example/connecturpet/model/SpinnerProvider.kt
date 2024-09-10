package com.example.connecturpet.model

data class Sex(val sex:String)
data class Personality(val personality:String)
data class Size(val size:String)

val sexSpinner: List<Sex> = listOf(Sex("Male"), Sex("Female"))
val personalitySpinner: List<Personality> = listOf(Personality("Confident"), Personality("Happy"), Personality("Lazy"), Personality("Friendly"))
val sizeSpinner = arrayOf(Size("Very small"),Size("Small"),Size("Medium"),Size("Large"),Size("Very large"))