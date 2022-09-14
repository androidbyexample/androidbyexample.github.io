package com.javadude.movies.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Actor(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var name: String,
)
