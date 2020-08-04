package com.example.base

fun String.formatPhoneNumber(): String {
    val lastTen = trim()
        .replace(" ", "")
        .takeLast(10)
    return "0098$lastTen"
}

fun String.beautifyPhoneNumber(): String {
    val lastTen = trim()
        .replace(" ", "")
        .takeLast(10)
    return "0$lastTen"
}