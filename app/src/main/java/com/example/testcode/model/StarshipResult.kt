package com.example.testcode.model

data class StarshipResult(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Starship>
)