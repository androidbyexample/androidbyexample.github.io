package com.javadude.movies.repository

import com.javadude.movies.data.Actor

data class ActorDto(
    val id: String,
    val name: String,
)

internal fun Actor.toDto() =
    ActorDto(id = id, name = name)
internal fun ActorDto.toEntity() =
    Actor(id = id, name = name)
