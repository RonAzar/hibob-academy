package com.hibob.bootcamp


/**
 * Modify the main function to print each user's email safely.
 * Use the Elvis operator to provide the "Email not provided" default string if the email is null.
 * Ensure your solution handles both user1 and user2 correctly.
 */
data class User(val name: String?, val email: String?)

fun main() {
    val user1: User = User("Alice", null)
    val user2: User = User(null, "alice@example.com")

    // Task: Print user email or "Email not provided" if null
    println(getUserEmail(user1.email))
    println(getUserEmail(user2.email))
}

fun getUserEmail(email: String?):String {
    return  email ?: "Email not provided"
}
