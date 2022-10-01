package stanchfield.scott.restserver

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
data class Movie(
    @JsonProperty("id") val id: String = UUID.randomUUID().toString(),
    @JsonProperty("title") val title: String,
    @JsonProperty("description") val description: String
)