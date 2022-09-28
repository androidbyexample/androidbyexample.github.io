package com.javadude.movies.repository

import com.javadude.movies.data.Movie
import com.javadude.movies.data.MovieWithCast
import com.javadude.movies.data.RoleWithActor

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

data class MovieWithCastDto(
    val movie: MovieDto,
    val cast: List<RoleWithActorDto>,
)

data class RoleWithActorDto(
    val actor: ActorDto,
    val character: String,
    val orderInCredits: Int,
)

internal fun RoleWithActor.toDto() =
    RoleWithActorDto(
        actor = actor.toDto(),
        character = role.character,
        orderInCredits = role.orderInCredits,
    )

internal fun MovieWithCast.toDto() =
    MovieWithCastDto(
        movie = movie.toDto(),
        cast =
            rolesWithActors.map {
                it.toDto()
            }
    )
