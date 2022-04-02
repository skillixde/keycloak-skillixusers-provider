package de.skillix.keycloak.spi.userstorage;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

public class SkillixUserStorageProviderFactory implements UserStorageProviderFactory<SkillixUserStorageProvider> {

    @Override
    public SkillixUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        return new SkillixUserStorageProvider(session, model);
    }

    @Override
    public String getId() {
        return "skillix users provider";
    }
}
