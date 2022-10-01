package stanchfield.scott.restserver

import com.fasterxml.jackson.annotation.JsonProperty
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
data class ExpandedRole(
    @JsonProperty("actorId") val actorId: String,
    @JsonProperty("actor") val actor: Actor,
    @JsonProperty("character") val character: String,
    @JsonProperty("orderInCredits") val orderInCredits: Int
)