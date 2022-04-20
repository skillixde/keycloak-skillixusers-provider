package de.skillix.keycloak.spi.userprovider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillixUser {
    private String uuid;
    private String email;
    private String firstName;
    private String lastName;
    private String company;
    private List<String> roles = new ArrayList<>();
    private boolean emailVerified;
    private boolean enabled;

    SkillixUser(String uuid){
        this.uuid = uuid;
    }
}
