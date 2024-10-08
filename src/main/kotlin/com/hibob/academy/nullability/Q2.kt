package com.hibob.academy.nullability
/**
 * Modify the main function to calculate and print the sum of all non-null integers in the list numbers.
 * Use safe calls and/or the Elvis operator where appropriate.
 **/
fun main3() {
    val numbers: List<Int?> = listOf(1, null, 3, null, 5)
    // Task: Calculate the sum of all non-null numbers
    println("The sum of all non-null numbers is: ${calculateListSum(numbers)}")
}

fun calculateListSum(numbers: List<Int?> ): Int{
    return numbers.sumOf { it ?: 0 }
}