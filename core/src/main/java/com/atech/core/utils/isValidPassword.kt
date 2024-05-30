package com.atech.core.utils

import java.util.regex.Pattern

fun isValidPassword(password: String): Boolean {
    // Regular expression to check the conditions
    val pattern = "^(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*\\d).+$"
    val passwordPattern = Pattern.compile(pattern)

    // Check if the password matches the pattern
    val matcher = passwordPattern.matcher(password)
    return matcher.matches()
}
/*
    println(isValidPassword("Password123!"))  // True
    println(isValidPassword("password123!"))  // False (no capital letter)
    println(isValidPassword("PASSWORD123"))   // False (no special character)
    println(isValidPassword("Password!"))     // False (no numerical value)
 */