package com.javadude.movies.repository

import android.content.Context
import com.javadude.movies.data.createDao
import kotlinx.coroutines.flow.map

class MovieDatabaseRepository(context: Context): MovieRepository {
    private val dao = createDao(context)

    override val ratingsFlow =
        dao.getRatingsFlow()
            .map { ratings ->
                ratings.map { it.toDto() }
            }
    override val moviesFlow =
        dao.getMoviesFlow()
            .map { movies ->
                movies.map { it.toDto() }
            }
    override val actorsFlow =
        dao.getActorsFlow()
            .map { actors ->
                actors.map { it.toDto() }
            }

    override suspend fun getMovie(id: String): MovieDto =
        dao.getMovie(id).toDto()

    override suspend fun getRatingWithMovies(id: String): RatingWithMoviesDto =
        dao.getRatingWithMovies(id).toDto()

    override suspend fun getMovieWithCast(id: String): MovieWithCastDto =
        dao.getMovieWithCast(id).toDto()

    override suspend fun getActorWithFilmography(id: String): ActorWithFilmographyDto =
        dao.getActorWithFilmography(id).toDto()

    override suspend fun insert(movie: MovieDto) = dao.insert(movie.toEntity())
    override suspend fun insert(actor: ActorDto) = dao.insert(actor.toEntity())
    override suspend fun insert(rating: RatingDto) = dao.insert(rating.toEntity())

    override suspend fun update(movie: MovieDto) = dao.update(movie.toEntity())
    override suspend fun update(actor: ActorDto) = dao.update(actor.toEntity())
    override suspend fun update(rating: RatingDto) = dao.update(rating.toEntity())

    override suspend fun delete(movie: MovieDto) = dao.delete(movie.toEntity())
    override suspend fun delete(actor: ActorDto) = dao.delete(actor.toEntity())
    override suspend fun delete(rating: RatingDto) = dao.delete(rating.toEntity())

    override suspend fun deleteMoviesById(ids: Set<String>) = dao.deleteMoviesById(ids)
    override suspend fun deleteActorsById(ids: Set<String>) = dao.deleteActorsById(ids)
    override suspend fun deleteRatingsById(ids: Set<String>) = dao.deleteRatingsById(ids)

    override suspend fun resetDatabase() = dao.resetDatabase()
}