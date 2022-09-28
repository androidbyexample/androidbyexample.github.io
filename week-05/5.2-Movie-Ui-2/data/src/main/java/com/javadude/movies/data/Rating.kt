package com.javadude.movies.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

@Entity
data class Rating(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var name: String,
    var description: String,
)

// ONE-TO-MANY relationship (Rating -> Movies)
// NOTE: THIS IS NOT AN ENTITY!
data class RatingWithMovies(
    @Embedded
    val rating: Rating,

    @Relation(
        parentColumn = "id",
        entityColumn = "ratingId",
    )
    val movies: List<Movie>
)