package com.example.testcode.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testcode.database.StarshipDatabase
import com.example.testcode.model.Starship
import com.example.testcode.service.DaggerApiComponent
import com.example.testcode.service.NetworkService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.stream.Collectors
import javax.inject.Inject


class StarshipViewModel : ViewModel() {

    @Inject
    lateinit var networkService: NetworkService

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    lateinit var databaseObject: StarshipDatabase

    val starshipList by lazy { MutableLiveData<List<Starship>>() }
    val starshipListLD: LiveData<List<Starship>>
        get() = starshipList
    private val inProgress by lazy { MutableLiveData<Boolean>() }
    val inProgressLD: LiveData<Boolean>
        get() = inProgress
    private val isError by lazy { MutableLiveData<Boolean>() }
    val isErrorLD: LiveData<Boolean>
        get() = isError

    init {
        DaggerApiComponent.create().inject(this)
        fetchStarships()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun setupDatabase(context: Context) {
        databaseObject = StarshipDatabase.getInstance(context)!!
    }

    //get data from server
    private fun fetchStarships() {
        compositeDisposable.add(
            networkService.fetchStarship()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.results }
                .subscribeWith(createStarshipObserver())
        )
    }

    private fun createStarshipObserver(): DisposableSingleObserver<List<Starship>> {
        return object : DisposableSingleObserver<List<Starship>>() {

            override fun onSuccess(starships: List<Starship>) {
                inProgress.value = true
                isError.value = false
                databaseObject.starshipDao().updateOrInsert(starships)
                starshipList.value = databaseObject.starshipDao().getAll()
                inProgress.value = false
            }

            override fun onError(e: Throwable) {
                inProgress.value = true
                isError.value = true
                inProgress.value = false
            }
        }
    }

    //search function
    fun filterStarship(searchKeyword: String) {
        val result: List<Starship> = databaseObject.starshipDao().getAll().stream()
            .filter { item -> item.name.contains(searchKeyword, true) }
            ?.collect(Collectors.toList()) as List<Starship>
        starshipList.value = result
    }

    fun sortStarshipAtoZ() {
        starshipList.value = starshipList.value?.sortedWith(compareBy { it.name })
    }

    fun sortStarshipZtoA() {
        starshipList.value = starshipList.value?.sortedWith(compareBy { it.name })?.reversed()
    }

    fun sortManufactureAtoZ() {
        starshipList.value = starshipList.value?.sortedWith(compareBy { it.manufacturer })
    }

    fun sortManufactureZtoA() {
        starshipList.value =
            starshipList.value?.sortedWith(compareBy { it.manufacturer })?.reversed()
    }

    fun getStarshipFromLocalDatabase() {
        starshipList.value = databaseObject.starshipDao().getAll()
    }

    fun changeFavStatusStarship(starship: Starship) {
        starship.ismyfav = !starship.ismyfav
        databaseObject.starshipDao().update(starship)
    }

    fun getStarshipDetails(name: String): Starship {
        return databaseObject.starshipDao().getStarshipDetails(name)
    }

}
