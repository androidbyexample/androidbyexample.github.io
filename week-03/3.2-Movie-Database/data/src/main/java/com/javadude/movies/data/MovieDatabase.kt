package com.javadude.movies.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [
        Movie::class,
        Actor::class,
        Role::class,
        Rating::class,
    ],
    exportSchema = false
)
abstract class MovieDatabase: RoomDatabase() {
    abstract val dao: MovieDAO
}