package org.sanjida.spring.kafka.messaging.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Created by JsIdentity on 10/11/2017.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventPayload {

    @JsonProperty("event_name")
    String eventName;

    @JsonProperty("time")
    LocalDateTime time;

    @JsonProperty("payload")
    MessagePayload messagePayload;

    @Override
    public String toString() {
        return "EventPayload{" +
                "eventName='" + eventName + '\'' +
                ", messagePayload=" + messagePayload +
                '}';
    }
}
