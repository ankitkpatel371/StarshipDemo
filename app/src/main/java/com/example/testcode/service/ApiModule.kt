package com.example.testcode.service

import com.example.testcode.adapter.StarshipAdapter
import com.example.testcode.model.Starship
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class ApiModule {

    @Provides
    fun provideStarWarsApi(): StarWarsApi {
        return Retrofit.Builder()
            .baseUrl(NetworkService.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(StarWarsApi::class.java)
    }

    @Provides
    fun provideStarshipList(): ArrayList<Starship> {
        return ArrayList()
    }

    @Provides
    fun provideStarshipAdapter(starships: ArrayList<Starship>): StarshipAdapter {
        return StarshipAdapter(starships)
    }

    @Provides
    fun provideNetworkService(): NetworkService {
        return NetworkService()
    }

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }
}