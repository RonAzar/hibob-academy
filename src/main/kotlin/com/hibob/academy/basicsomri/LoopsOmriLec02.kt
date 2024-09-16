package com.hibob.academy.basicsomri

//Omri Solution
fun isValidIdentifier(s: String): Boolean {

    fun isValidCharacterOption1(ch: Char) = ch == '_' || ch.isLetterOrDigit()

    fun isValidCharacterOption2(ch: Char) = ch == '_' || ch in '0'..'9' || ch in 'a'..'z' || ch in 'A'..'Z'

    if (s.isEmpty() || s[0].isDigit()) return false
    for (ch in s) {
        if (!isValidCharacterOption2(ch)) return false
    }
    return true
}

//My solution-
// If starts with a number return false,
// If contain $ return false
// else its OK!
//fun isValidIdentifier(s: String): Boolean {
//        return when{
//            s.isNullOrEmpty() -> false
//            s[0] in '0'..'9' -> false
//            '$' !in s -> true
//            else -> false
//        }
//}


fun main11(args:Array<String>){
    println(isValidIdentifier("name"))//true
    println(isValidIdentifier("_name"))//true
    println(isValidIdentifier("_12"))//true
    println(isValidIdentifier(""))//false
    println(isValidIdentifier("012"))//false
    println(isValidIdentifier("no$"))//false
}