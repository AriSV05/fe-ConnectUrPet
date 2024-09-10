package com.example.connecturpet.model

data class Reaction(
    var id:String,
    var pet:String,
    var adopter:String,
    var giver:String,
    var match:Boolean,
    var view:Boolean
)

data class ReactionInput(
    var pet:String,
    var adopter:String,
    var giver:String,
    var match:Boolean = false,
    var view:Boolean = false
)
