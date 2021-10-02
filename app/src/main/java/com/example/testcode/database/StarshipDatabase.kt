package com.example.testcode.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testcode.model.Starship

@Database(entities = [Starship::class], version = 1)
abstract class StarshipDatabase : RoomDatabase() {
    abstract fun starshipDao(): StarshipDao

    companion object {
        private var INSTANCE: StarshipDatabase? = null

        fun getInstance(context: Context): StarshipDatabase? {
            if (INSTANCE == null) {
                synchronized(StarshipDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        StarshipDatabase::class.java, "starship.db"
                    ).allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}