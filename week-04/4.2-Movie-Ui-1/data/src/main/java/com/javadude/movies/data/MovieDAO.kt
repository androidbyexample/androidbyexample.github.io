package com.javadude.movies.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

private val NotRated = Rating(id = "r0", name = "Not Rated", description = "Not yet rated")
private val G = Rating(id = "r1", name = "G", description = "General Audiences")
private val PG = Rating(id = "r2", name = "PG", description = "Parental Guidance Suggested")
private val PG13 = Rating(id = "r3", name = "PG-13", description = "Unsuitable for those under 13")
private val R = Rating(id = "r4", name = "R", description = "Restricted - 17 and older")

@Dao
abstract class MovieDAO {
    @Query("SELECT * FROM Rating")
    abstract fun getRatingsFlow(): Flow<List<Rating>>

    @Query("SELECT * FROM Movie")
    abstract fun getMoviesFlow(): Flow<List<Movie>>

    @Query("SELECT * FROM Actor")
    abstract fun getActorsFlow(): Flow<List<Actor>>

    @Transaction
    @Query("SELECT * FROM Rating WHERE id = :id")
    abstract suspend fun getRatingWithMovies(id: String): RatingWithMovies

    @Transaction
    @Query("SELECT * FROM Actor WHERE id = :id")
    abstract suspend fun getActorWithFilmography(id: String): ActorWithFilmography

    @Transaction
    @Query("SELECT * FROM Movie WHERE id = :id")
    abstract suspend fun getMovieWithCast(id: String): MovieWithCast

    @Insert
    abstract suspend fun insert(vararg ratings: Rating)
    @Insert
    abstract suspend fun insert(vararg movies: Movie)
    @Insert
    abstract suspend fun insert(vararg actors: Actor)
    @Insert
    abstract suspend fun insert(vararg roles: Role)

    @Update
    abstract suspend fun update(vararg ratings: Rating)
    @Update
    abstract suspend fun update(vararg movies: Movie)
    @Update
    abstract suspend fun update(vararg actors: Actor)
    @Update
    abstract suspend fun update(vararg roles: Role)

    @Delete
    abstract suspend fun delete(vararg ratings: Rating)
    @Delete
    abstract suspend fun delete(vararg movies: Movie)
    @Delete
    abstract suspend fun delete(vararg actors: Actor)
    @Delete
    abstract suspend fun delete(vararg roles: Role)

    @Query("DELETE FROM Movie")
    abstract suspend fun clearMovies()
    @Query("DELETE FROM Actor")
    abstract suspend fun clearActors()
    @Query("DELETE FROM Rating")
    abstract suspend fun clearRatings()
    @Query("DELETE FROM Role")
    abstract suspend fun clearRoles()

    @Transaction
    open suspend fun resetDatabase() {
        clearMovies()
        clearActors()
        clearRoles()
        clearRatings()

        insert(NotRated, G, PG, PG13, R)

        insert(
            Movie("m1", "The Transporter", "Jason Statham kicks a guy in the face", "r3"),
            Movie("m2", "Transporter 2", "Jason Statham kicks a bunch of guys in the face", "r4"),
            Movie("m3", "Hobbs and Shaw", "Cars, Explosions and Stuff", "r3"),
            Movie("m4", "Jumanji - Welcome to the Jungle", "The Rock smolders", "r3"),
        )
        insert(
            Actor("a1", "Jason Statham"),
            Actor("a2", "The Rock"),
            Actor("a3", "Shu Qi"),
            Actor("a4", "Amber Valletta"),
            Actor("a5", "Kevin Hart"),
        )
        insert(
            Role("m1", "a1", "Frank Martin", 1),
            Role("m1", "a3", "Lai", 2),
            Role("m2", "a1", "Frank Martin", 1),
            Role("m2", "a4", "Audrey Billings", 2),
            Role("m3", "a2", "Hobbs", 1),
            Role("m3", "a1", "Shaw", 2),
            Role("m4", "a2", "Spencer", 1),
            Role("m4", "a5", "Fridge", 2),
        )
    }
}