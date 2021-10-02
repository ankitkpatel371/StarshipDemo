package com.example.testcode.service

import com.example.testcode.model.StarshipResult
import io.reactivex.Single
import retrofit2.http.GET

interface StarWarsApi {

    @GET("api/starships")
    fun getStarship(): Single<StarshipResult>
}