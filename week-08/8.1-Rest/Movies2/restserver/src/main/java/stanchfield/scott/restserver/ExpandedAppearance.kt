package stanchfield.scott.restserver

import com.fasterxml.jackson.annotation.JsonProperty
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
data class ExpandedAppearance(
    @JsonProperty("movieId") val movieId: String,
    @JsonProperty("movie") val movie: Movie,
    @JsonProperty("character") val character: String,
    @JsonProperty("orderInCredits") val orderInCredits: Int
)