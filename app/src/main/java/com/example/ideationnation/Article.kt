package com.example.ideationnation

 data class Article(
    val id: Int,
    val title: String,
    val content: String,
    val date: String,
    val userId: Int
) {
    constructor(title: String, content: String, date: String, userId: Int) : this(0, title, content, date, userId)
}
