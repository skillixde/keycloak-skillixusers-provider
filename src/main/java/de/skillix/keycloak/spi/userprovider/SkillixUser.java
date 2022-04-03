package de.skillix.keycloak.spi.userprovider;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SkillixUser {
    private String uuid;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;
    private boolean emailVerified;
    private boolean enabled;
}
