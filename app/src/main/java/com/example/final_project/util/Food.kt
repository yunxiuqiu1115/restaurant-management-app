package com.example.final_project.util

data class Food (
    var id:String,
    var name:String,
    val amount:Int,
    val description:String,
    val available:Boolean,
    val image:String,
    val discount:Int,
    val ordertimes:Int
)