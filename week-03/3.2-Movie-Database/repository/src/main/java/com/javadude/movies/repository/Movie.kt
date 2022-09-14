package com.javadude.movies.repository

import com.javadude.movies.data.Movie

data class MovieDto(
    val id: String,
    val title: String,
    val description: String,
    val ratingId: String,
)

internal fun Movie.toDto() =
    MovieDto(id = id, title = title, description = description, ratingId = ratingId)
internal fun MovieDto.toEntity() =
    Movie(id = id, title = title, description = description, ratingId = ratingId)
