package stanchfield.scott.restserver

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.UriInfo
import java.util.UUID

private val movies = mutableMapOf<String, Movie>()
private val actors = mutableMapOf<String, Actor>()
private val rolesByMovieId = mutableMapOf<String, MutableMap<String, Role>>()
private val rolesByActorId = mutableMapOf<String, MutableMap<String, Role>>()

private val notFoundMovie = Movie("-", "NOT FOUND", "NOT FOUND")
private val notFoundActor = Actor("-", "NOT FOUND")

private fun <T> response(status: Response.Status, entity: T) =
    Response.status(status).entity(entity).build()

private fun <T> ok(entity: T) =
    response(Response.Status.OK, entity)

private fun <T> notFound(entity: T) =
    response(Response.Status.NOT_FOUND, entity)

private fun <T> created(entity: T) =
    response(Response.Status.CREATED, entity)

@Path("/")
class RestController {
    @GET
    @Path("movie")
    @Produces(MediaType.APPLICATION_JSON)
    fun getMovies(): Response = ok(movies.values.sortedBy { it.title })

    @GET
    @Path("actor")
    @Produces(MediaType.APPLICATION_JSON)
    fun getActors(): Response = ok(actors.values.sortedBy { it.name })

    @GET
    @Path("cast")
    @Produces(MediaType.APPLICATION_JSON)
    fun getCast(): Response = ok(rolesByMovieId)

    @GET
    @Path("filmography")
    @Produces(MediaType.APPLICATION_JSON)
    fun getFilmography(): Response = ok(rolesByActorId)

    @GET
    @Path("movie/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getMovie(@PathParam("id") id: String): Response =
        movies[id]?.let { ok(it) } ?: notFound(notFoundMovie)

    @GET
    @Path("actor/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getActor(@PathParam("id") id: String): Response =
        actors[id]?.let { ok(it) } ?: notFound(notFoundActor)

    @GET
    @Path("movie/{id}/cast")
    @Produces(MediaType.APPLICATION_JSON)
    fun getCast(@PathParam("id") id: String): Response =
        ok(
            (rolesByMovieId[id] ?: emptyMap()).values.map {
                ExpandedRole(it.actorId, actors[it.actorId] ?: notFoundActor, it.character, it.orderInCredits)
            }.sortedBy { it.orderInCredits }
        )

    @GET
    @Path("actor/{id}/filmography")
    @Produces(MediaType.APPLICATION_JSON)
    fun getFilmography(@PathParam("id") id: String): Response =
        ok(
            (rolesByActorId[id] ?: emptyMap()).values.map {
                ExpandedAppearance(it.actorId, movies[it.movieId] ?: notFoundMovie, it.character, it.orderInCredits)
            }.sortedBy { it.movie.title }
        )

    @GET
    @Path("actor/{id}/roles")
    @Produces(MediaType.APPLICATION_JSON)
    fun getRolesByActorId(@PathParam("id") id: String): Response =
        ok(rolesByActorId[id]?.values?.toList() ?: emptyList())

    @GET
    @Path("movie/{id}/roles")
    @Produces(MediaType.APPLICATION_JSON)
    fun getRolesByMovieId(@PathParam("id") id: String): Response =
        ok(rolesByMovieId[id]?.values?.toList() ?: emptyList())

    @PUT
    @Path("movie/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    fun updateMovie(@PathParam("id") id: String, movie: Movie): Response {
        movies[id] = movie
        return ok(1)
    }

    @PUT
    @Path("actor/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    fun updateActor(@PathParam("id") id: String, actor: Actor): Response {
        actors[id] = actor
        return ok(1)
    }

    @PUT
    @Path("role/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    fun updateRole(@PathParam("id") id: String, role: Role): Response {
        rolesByActorId[role.actorId, role.movieId] = role
        rolesByMovieId[role.movieId, role.actorId] = role
        return ok(1)
    }
    
    private operator fun MutableMap<String, MutableMap<String, Role>>.set(id1: String, id2: String, role: Role) {
        val roles = this[id1] ?: mutableMapOf<String, Role>().apply { this@set[id1] = this }
        roles[id2] = role
    }

    private operator fun MutableMap<String, MutableMap<String, Role>>.get(id1: String, id2: String) =
        this[id1]?.get(id2)

    @POST
    @Path("movie/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    fun createMovie(@Context uriInfo: UriInfo, movie: Movie): Response {
        val id = UUID.randomUUID().toString()
        val newMovie = movie.copy(id = id)
        movies[id] = newMovie
        return created(newMovie)
    }
    
    @POST
    @Path("actor/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    fun createActor(@Context uriInfo: UriInfo, actor: Actor): Response {
        val id = UUID.randomUUID().toString()
        val newActor = actor.copy(id = id)
        actors[id] = newActor
        return created(newActor)
    }

    @POST
    @Path("role/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    fun createRole(@Context uriInfo: UriInfo, role: Role): Response {
        rolesByMovieId[role.movieId, role.actorId] = role
        rolesByActorId[role.actorId, role.movieId] = role
        return created(role)
    }

    @DELETE
    @Path("movie/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    fun deleteMovie(@PathParam("id") id: String): Response =
        if (movies[id] == null) {
            notFound(0)
        } else {
            movies.remove(id)
            rolesByMovieId.remove(id) // remove all roles
            rolesByActorId.values.forEach { roles ->
                roles.remove(id)
            }
            ok(1)
        }

    @DELETE
    @Path("actor/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    fun deleteActor(@PathParam("id") id: String): Response =
        if (actors[id] == null) {
            notFound(0)
        } else {
            actors.remove(id)
            rolesByActorId.remove(id) // remove all roles
            rolesByMovieId.values.forEach { roles -> // remove filmography for those movies
                roles.remove(id)
            }
            ok(1)
        }

    @DELETE
    @Path("role/{actorId}/{movieId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    fun deleteRole(@PathParam("actorId") actorId: String, @PathParam("movieId") movieId: String) {
        rolesByActorId[actorId]?.remove(movieId)
        rolesByMovieId[movieId]?.remove(actorId)
    }

    @GET
    @Path("reset")
    @Produces(MediaType.TEXT_PLAIN)
    fun resetDatabase(): Response {
        movies.clear()
        actors.clear()
        rolesByActorId.clear()
        rolesByMovieId.clear()

        fun insertMovies(vararg newMovies: Movie) {
            newMovies.forEach {
                movies[it.id] = it
            }
        }

        fun insertActors(vararg newActors: Actor) {
            newActors.forEach {
                actors[it.id] = it
            }
        }

        fun insertRoles(vararg newRoles: Role) {
            newRoles.forEach {
                rolesByActorId[it.actorId, it.movieId] = it
                rolesByMovieId[it.movieId, it.actorId] = it
            }
        }


        insertMovies(
            Movie("m1", "The Transporter", "Jason Statham kicks a guy in the face"),
            Movie("m2", "Transporter 2", "Jason Statham kicks a bunch of guys in the face"),
            Movie("m3", "Hobbs and Shaw", "Cars, Explosions and Stuff"),
            Movie("m4", "Jumanji", "The Rock smolders"),
        )

        insertActors(
            Actor("a1", "Jason Statham"),
            Actor("a2", "The Rock"),
            Actor("a3", "Shu Qi"),
            Actor("a4", "Amber Valletta"),
            Actor("a5", "Kevin Hart"),
        )
        insertRoles(
            Role("m1", "a1", "Frank Martin", 1),
            Role("m1", "a3", "Lai", 2),
            Role("m2", "a1", "Frank Martin", 1),
            Role("m2", "a4", "Audrey Billings", 2),
            Role("m3", "a2", "Hobbs", 1),
            Role("m3", "a1", "Shaw", 2),
            Role("m4", "a2", "Spencer", 1),
            Role("m4", "a5", "Fridge", 2),
        )
        return ok(1)
    }
}