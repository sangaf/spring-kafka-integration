package org.sanjida.spring.kafka.messaging.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessagePayload {

    @JsonProperty("id")
    private String id;

	@JsonProperty("title")
    private String title;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;
    
    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;
    
    @JsonProperty("nectar_card")
    private String nectarCard;

    @JsonProperty("loyalty_card")
    private String loyaltyCard;

}
