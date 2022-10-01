package stanchfield.scott.restserver

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
data class Actor(
    @JsonProperty("id") var id: String = UUID.randomUUID().toString(),
    @JsonProperty("name") var name: String
)