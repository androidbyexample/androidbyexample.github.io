package com.javadude.movies.repository

import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    val ratingsFlow: Flow<List<RatingDto>>
    val moviesFlow: Flow<List<MovieDto>>
    val actorsFlow: Flow<List<ActorDto>>

    suspend fun getMovie(id: String): MovieDto

    suspend fun getRatingWithMovies(id: String): RatingWithMoviesDto
    suspend fun getMovieWithCast(id: String): MovieWithCastDto
    suspend fun getActorWithFilmography(id: String): ActorWithFilmographyDto

    suspend fun insert(movie: MovieDto)
    suspend fun insert(actor: ActorDto)
    suspend fun insert(rating: RatingDto)

    suspend fun update(movie: MovieDto)
    suspend fun update(actor: ActorDto)
    suspend fun update(rating: RatingDto)

    suspend fun delete(movie: MovieDto)
    suspend fun delete(actor: ActorDto)
    suspend fun delete(rating: RatingDto)

    suspend fun deleteMoviesById(ids: Set<String>)
    suspend fun deleteActorsById(ids: Set<String>)
    suspend fun deleteRatingsById(ids: Set<String>)

    suspend fun resetDatabase()
}