package com.javadude.movies.repository

import com.javadude.movies.data.Rating
import com.javadude.movies.data.RatingWithMovies

data class RatingDto(
    val id: String,
    val name: String,
    val description: String,
)

internal fun Rating.toDto() =
    RatingDto(id = id, name = name, description = description)
internal fun RatingDto.toEntity() =
    Rating(id = id, name = name, description = description)

data class RatingWithMoviesDto(
    val rating: RatingDto,
    val movies: List<MovieDto>,
)

// only need the toDto(); we don't use this to do database updates
internal fun RatingWithMovies.toDto() =
    RatingWithMoviesDto(
        rating = rating.toDto(),
        movies = movies.map { it.toDto() },
    )
