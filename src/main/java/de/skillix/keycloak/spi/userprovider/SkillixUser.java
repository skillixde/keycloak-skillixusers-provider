package de.skillix.keycloak.spi.userprovider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SkillixUser {
    private final String uuid;
    private String email;
    private String firstName;
    private String lastName;
    private String company;
    private List<String> roles;
    private boolean emailVerified;
    private boolean enabled;
}
