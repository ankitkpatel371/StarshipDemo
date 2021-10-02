package com.example.testcode.database

import androidx.room.*
import com.example.testcode.model.Starship

@Dao
interface StarshipDao {
    @Query("SELECT * FROM starshipTable")
    fun getAll(): List<Starship>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(starship: Starship)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(starships: List<Starship>): List<Long>

    @Update
    fun update(starship: Starship)

    @Update
    fun updateAll(starship: List<Starship>)

    @Query("SELECT * FROM starshipTable WHERE name = :name")
    fun getStarshipDetails(name: String): Starship

    @Delete
    fun delete(starships: Starship)

    @Query("DELETE FROM starshipTable")
    fun deleteAll()

    @Query("SELECT * FROM starshipTable WHERE name == :strName")
    fun isUserExist(strName: String): Boolean

    @Transaction
    fun updateOrInsert(starships: List<Starship>) {
        for (i in starships.indices) {
            if (isUserExist(starships[i].name)) {
                update(starships[i])
            } else {
                insert(starships[i])
            }
        }
    }

}