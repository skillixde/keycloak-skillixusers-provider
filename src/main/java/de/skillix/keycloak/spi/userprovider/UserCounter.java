package de.skillix.keycloak.spi.userprovider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCounter {
    private int totalCount;
    private ZonedDateTime timestamp;
}
