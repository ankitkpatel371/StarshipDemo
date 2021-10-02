package com.example.testcode.service

import com.example.testcode.view.MainActivity
import com.example.testcode.viewmodel.StarshipViewModel

import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(networkService: NetworkService)

    fun inject(starshipViewModel: StarshipViewModel)

    fun inject(mainActivity: MainActivity)
}