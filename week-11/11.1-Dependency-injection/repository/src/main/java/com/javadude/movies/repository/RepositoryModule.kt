package com.javadude.movies.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    // ##START 030-repo
    @Binds
    abstract fun bindMovieRepository(
        movieDatabaseRepository: MovieDatabaseRepository
    ): MovieRepository
    // ##END
}