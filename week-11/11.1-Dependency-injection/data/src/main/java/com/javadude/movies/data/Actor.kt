package com.javadude.movies.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

@Entity
data class Actor(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var name: String,
)

data class ActorWithFilmography(
    @Embedded
    val actor: Actor,

    @Relation(
        entity = Role::class,
        parentColumn = "id",
        entityColumn = "actorId",
    )
    val rolesWithMovies: List<RoleWithMovie>,
)

data class RoleWithMovie(
    @Embedded
    val role: Role,

    @Relation(
        parentColumn = "movieId",
        entityColumn = "id"
    )
    val movie: Movie,
)