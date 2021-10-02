package com.example.testcode.service

import com.example.testcode.model.StarshipResult
import io.reactivex.Single
import javax.inject.Inject

class NetworkService {

    @Inject
    lateinit var starWarsApi: StarWarsApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun fetchStarship(): Single<StarshipResult> {
        return starWarsApi.getStarship()
    }

    companion object {
        val BASE_URL = "https://swapi.dev/"
    }
}