package com.hibob.KotlinBasics
//Q1
fun List<Int>.sum():Int{
    var result = 0
    for (i in this){
        result+=i
    }
    return result
}

fun main12(args:Array<String>){
    val sum = listOf(1,2,3).sum()
    println(sum) //6
    println(3 toPowerOf -2)
}


//Q2
infix fun Number.toPowerOf(exponent: Number):Double{
    //Q2 solution using Math.pow extension
    return Math.pow(this.toDouble(), exponent.toDouble())
}