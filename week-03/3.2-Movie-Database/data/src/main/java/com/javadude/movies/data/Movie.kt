package com.javadude.movies.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
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
