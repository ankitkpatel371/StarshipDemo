package com.example.testcode.view


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testcode.R
import com.example.testcode.adapter.OnClickListener
import com.example.testcode.adapter.StarshipAdapter
import com.example.testcode.databinding.BottomSheetStarshipBinding
import com.example.testcode.model.Starship
import com.example.testcode.service.DaggerApiComponent
import com.example.testcode.viewmodel.StarshipViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), OnClickListener {

    @Inject
    lateinit var starshipAdapter: StarshipAdapter

    private val starshipViewModel: StarshipViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        starshipViewModel.setupDatabase(this)
        DaggerApiComponent.create().inject(this)

        rv_starship.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = starshipAdapter
        }

        starshipAdapter.setupClickListener(this)

        observeLiveData()
    }

    private fun observeLiveData() {
        observeInProgress()
        observeIsError()
        observeStarshipList()
    }

    private fun observeStarshipList() {
        starshipViewModel.starshipListLD.observe(this, Observer { allStarships ->
            allStarships.let {
                rv_starship.visibility = View.VISIBLE
                starshipAdapter.setUpStarships(it)
            }
        })
    }

    private fun observeInProgress() {
        starshipViewModel.inProgressLD.observe(this, Observer { isLoading ->
            isLoading.let {
                if (it) {
                    rv_starship.visibility = View.GONE
                    progress_bar.visibility = View.VISIBLE
                } else {
                    progress_bar.visibility = View.GONE
                }
            }
        })
    }

    //getting data from local database as place holder and also if API fails to load data
    private fun observeIsError() {
        starshipViewModel.isErrorLD.observe(this, {
            starshipViewModel.getStarshipFromLocalDatabase()
        })
    }

    // Option Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val menuItem = menu.findItem(R.id.search)
        val searchView: SearchView = menuItem.actionView as SearchView
        searchView.queryHint = "Search Hint"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    starshipViewModel.filterStarship(newText)
                }
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sortatoz -> starshipViewModel.sortStarshipAtoZ()
            R.id.sortztoa -> starshipViewModel.sortStarshipZtoA()
            R.id.sortmanufactureatoz -> starshipViewModel.sortManufactureAtoZ()
            R.id.sortmanufactureztoa -> starshipViewModel.sortManufactureZtoA()
            R.id.getAll -> starshipViewModel.getStarshipFromLocalDatabase()
        }
        return super.onOptionsItemSelected(item)
    }

    //interface function for card click to show details
    override fun onClick(item: Starship, position: Int) {
        showDetails(item, position)
    }

    //interface function to add favriate
    override fun onFavriateClick(item: Starship, position: Int) {
        starshipViewModel.changeFavStatusStarship(item)
        rv_starship.adapter?.notifyItemChanged(position)
    }

    //show details of starship in bottom sheet
    private fun showDetails(starship: Starship, position: Int) {
        val bottomSheet = BottomSheetDialog(this)
        val bindingSheetUtil = DataBindingUtil.inflate<BottomSheetStarshipBinding>(
            layoutInflater,
            R.layout.bottom_sheet_starship,
            null,
            false
        )
        bottomSheet.setContentView(bindingSheetUtil.root)
        bindingSheetUtil.starship = starshipViewModel.getStarshipDetails(starship.name)
        bindingSheetUtil.tvIsMyFav.setOnClickListener {
            starshipViewModel.changeFavStatusStarship(starship)
            rv_starship.adapter?.notifyItemChanged(position)
            bottomSheet.dismiss()
        }
        bottomSheet.show()
    }
}

