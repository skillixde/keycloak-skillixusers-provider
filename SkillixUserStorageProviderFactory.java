package de.skillix.auth.provider.user;

import java.sql.Connection;
import java.util.List;

import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.skillix.auth.provider.user.SkillixUserStorageProviderConstants.*;

public class SkillixUserStorageProviderFactory implements UserStorageProviderFactory<SkillixUserStorageProvider> {
    private static final Logger log = LoggerFactory.getLogger(SkillixUserStorageProviderFactory.class);
    protected final List<ProviderConfigProperty> configMetadata;
    
    public SkillixUserStorageProviderFactory() {
        log.info("[I24] CustomUserStorageProviderFactory created");

        //HTTp client connection request (Webclient from SpringBoot) -Spring-Webflux-Starter
        
        // Create config metadata
        configMetadata = ProviderConfigurationBuilder.create()
          .property()
            .name(SKILLIX_HOST_NAME)
            .label("Skillix Host")
            .type(ProviderConfigProperty.STRING_TYPE)
            .defaultValue("https://api.skillix.dev")
            .helpText("Host name of the Skillix application")
            .add()
          .property()
            .name(SKILLIX_USER_API)
            .label("REST API resource of user")
            .type(ProviderConfigProperty.STRING_TYPE)
            .defaultValue("/vo/users/{userId}")
            .helpText("Retrieve the users info based on uuid")
            .add()
          .property()
            .name(CONFIG_KEY_DB_USERNAME)
            .label("Database User")
            .type(ProviderConfigProperty.STRING_TYPE)
            .helpText("Username used to connect to the database")
            .add()
          .property()
            .name(CONFIG_KEY_DB_PASSWORD)
            .label("Database Password")
            .type(ProviderConfigProperty.STRING_TYPE)
            .helpText("Password used to connect to the database")
            .secret(true)
            .add()
          .property()
            .name(CONFIG_KEY_VALIDATION_QUERY)
            .label("SQL Validation Query")
            .type(ProviderConfigProperty.STRING_TYPE)
            .helpText("SQL query used to validate a connection")
            .defaultValue("select 1")
            .add()
          .build();

    }

    @Override
    public SkillixUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        log.info("[I63] creating new CustomUserStorageProvider");
        return new SkillixUserStorageProvider(session,model);
    }

    @Override
    public String getId() {
        log.info("[I69] getId()");
        return PROVIDER_ID;
    }

    
    // Configuration support methods
    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configMetadata;
    }

    @Override
    public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) throws ComponentValidationException {

       try (Connection c = DbUtil.getConnection(config)) {
           log.info("[I84] Testing connection..." );
           c.createStatement().execute(config.get(CONFIG_KEY_VALIDATION_QUERY));
           log.info("[I92] Connection OK !" );
       }
       catch(Exception ex) {
           log.warn("[W94] Unable to validate connection: ex={}", ex.getMessage());
           throw new ComponentValidationException("Unable to validate database connection",ex);
       }
    }

    @Override
    public void onUpdate(KeycloakSession session, RealmModel realm, ComponentModel oldModel, ComponentModel newModel) {
        log.info("[I94] onUpdate()" );
    }

    @Override
    public void onCreate(KeycloakSession session, RealmModel realm, ComponentModel model) {
        log.info("[I99] onCreate()" );
    }
}
