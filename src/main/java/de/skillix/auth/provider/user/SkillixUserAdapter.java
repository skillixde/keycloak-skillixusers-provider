package de.skillix.auth.provider.user;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.adapter.AbstractUserAdapter;


class SkillixUserAdapter extends AbstractUserAdapter {

    private final String uuid;
    private final String email;
    private final String fullName;
    private final String role;


    private SkillixUserAdapter(KeycloakSession session, RealmModel realm,
      ComponentModel storageProviderModel,
      String uuid,
      String email,
      String fullName,
      String role,) {
        super(session, realm, storageProviderModel);
        this.uuid = uuid;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public String getEmail() {
        return email;
    }

    
    @Override
    public Map<String, List<String>> getAttributes() {
        MultivaluedHashMap<String, String> attributes = new MultivaluedHashMap<>();
        attributes.add(UserModel.USERNAME, getUuid());
        attributes.add(UserModel.EMAIL,getEmail());
        attributes.add(UserModel.FIRST_NAME,getFirstName());
        attributes.add(UserModel.LAST_NAME,getLastName());
        return attributes;
    }

    static class Builder {
        private final KeycloakSession session;
        private final RealmModel realm;
        private final ComponentModel storageProviderModel;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private Date birthDate;
        
        Builder(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel,String username) {
            this.session = session;
            this.realm = realm;
            this.storageProviderModel = storageProviderModel;
            this.username = username;
        }
        
        SkillixUserAdapter.Builder email(String email) {
            this.email = email;
            return this;
        }

        SkillixUserAdapter.Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        SkillixUserAdapter.Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        SkillixUserAdapter.Builder birthDate(Date birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        SkillixUserAdapter build() {
            return new SkillixUserAdapter(
              session,
              realm,
              storageProviderModel,
              username,
              email,
              firstName,
              lastName,
              birthDate);
            
        }
    }
}