package de.skillix.keycloak.spi.userprovider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCounter {
    private int total;
}
