package de.skillix.keycloak.spi.userprovider;

import org.apache.commons.lang3.StringUtils;
import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;

import java.util.Collections;
import java.util.List;

import static de.skillix.keycloak.spi.userprovider.SkillixConstants.BASE_URL;
import static de.skillix.keycloak.spi.userprovider.SkillixConstants.BASE_URL_KEY;
import static de.skillix.keycloak.spi.userprovider.SkillixConstants.PASSWORD_BASICAUTH_KEY;
import static de.skillix.keycloak.spi.userprovider.SkillixConstants.PROVIDER_ID;
import static de.skillix.keycloak.spi.userprovider.SkillixConstants.SKILLIX_API_VERSION;
import static de.skillix.keycloak.spi.userprovider.SkillixConstants.SKILLIX_API_VERSION_KEY;
import static de.skillix.keycloak.spi.userprovider.SkillixConstants.USERNAME_BASICAUTH_KEY;

public class SkillixUserProviderFactory implements UserStorageProviderFactory<SkillixUserProvider> {

  @Override
  public SkillixUserProvider create(KeycloakSession session, ComponentModel model) {
    return new SkillixUserProvider(session, model);
  }

  @Override
  public String getId() {
    return PROVIDER_ID;
  }

  @Override
  public String getHelpText() {
    return "Skillix User Provider";
  }

  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    return ProviderConfigurationBuilder.create()
        .property(
            BASE_URL_KEY,
            "Base URL",
            "Base URL of the Skillix API",
            ProviderConfigProperty.STRING_TYPE,
            BASE_URL,
            Collections.emptyList())
        .property(
            USERNAME_BASICAUTH_KEY,
            "Username BasicAuth",
            "Username for BasicAuth at the  Skillix API",
            ProviderConfigProperty.STRING_TYPE,
            "",
            Collections.emptyList())
        .property(
            PASSWORD_BASICAUTH_KEY,
            "Password BasicAuth",
            "Password for BasicAuth at the  Skillix API",
            ProviderConfigProperty.PASSWORD,
            "",
            Collections.emptyList())
        .property(
            SKILLIX_API_VERSION_KEY,
            "API Version",
            "Version of the  Skillix API",
            ProviderConfigProperty.STRING_TYPE,
            SKILLIX_API_VERSION,
            Collections.emptyList())
        .build();
  }

  @Override
  public void validateConfiguration(
      KeycloakSession session, RealmModel realm, ComponentModel config)
      throws ComponentValidationException {
    if (StringUtils.isBlank(config.get(BASE_URL_KEY))) {
      throw new ComponentValidationException("Base URL for Skillix API must be provided");
    }
    if (StringUtils.isBlank(config.get(USERNAME_BASICAUTH_KEY))) {
      throw new ComponentValidationException("Username for BasicAuth must be provided");
    }
    if (StringUtils.isBlank(config.get(PASSWORD_BASICAUTH_KEY))) {
      throw new ComponentValidationException("Password for BasicAuth must be provided");
    }
    if (StringUtils.isBlank(config.get(SKILLIX_API_VERSION_KEY))) {
      throw new ComponentValidationException("Version of Skillix API must be provided");
    }
    if (!StringUtils.isNumeric(config.get(SKILLIX_API_VERSION_KEY))) {
      throw new ComponentValidationException("Version of Skillix API must be an integer");
    }
  }
}
