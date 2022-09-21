package com.javadude.movies.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

@Entity(
    indices = [
        Index(value = ["ratingId"])
    ],
)
data class Movie(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var title: String,
    var description: String,
    var ratingId: String,
)

data class MovieWithCast(
    @Embedded
    val movie: Movie,

    @Relation(
        entity = Role::class,
        parentColumn = "id",
        entityColumn = "movieId",
    )
    val rolesWithActors: List<RoleWithActor>,
)

data class RoleWithActor(
    @Embedded
    val role: Role,

    @Relation(
        parentColumn = "actorId",
        entityColumn = "id"
    )
    val actor: Actor,
)