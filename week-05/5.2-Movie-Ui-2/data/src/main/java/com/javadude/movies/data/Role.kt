package com.javadude.movies.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["actorId", "movieId"],
    indices = [
        Index(value = ["movieId"]),
        Index(value = ["actorId"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = Actor::class,
            parentColumns = ["id"],
            childColumns = ["actorId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
    ]
)
data class Role(
    var movieId: String,
    var actorId: String,
    var character: String,
    var orderInCredits: Int,
)
