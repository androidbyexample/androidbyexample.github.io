package stanchfield.scott.restserver

import com.fasterxml.jackson.annotation.JsonProperty
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class Role(
    @JsonProperty("movieId") var movieId: String,
    @JsonProperty("actorId") var actorId: String,
    @JsonProperty("character") var character: String,
    @JsonProperty("orderInCredits") var orderInCredits: Int
)