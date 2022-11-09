package com.javadude.movies.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// ##START 030-data-module
@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
// ##END
    // ##START 030-dao
    @Provides
    @Singleton
    fun provideMovieDao(movieDatabase: MovieDatabase): MovieDAO {
        return movieDatabase.dao
    }
    // ##END

    // ##START 030-database
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "MOVIES"
        ).build()
    }
    // ##END
}