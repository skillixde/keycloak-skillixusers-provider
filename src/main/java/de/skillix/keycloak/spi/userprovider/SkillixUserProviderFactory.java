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

import static de.skillix.keycloak.spi.userprovider.Constants.PASSWORD_BASICAUTH_KEY;
import static de.skillix.keycloak.spi.userprovider.Constants.PROVIDER_ID;
import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_API_VERSION;
import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_API_VERSION_KEY;
import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_BASE_URL;
import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_BASE_URL_KEY;
import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_GET_PROFILE_API_FORMAT;
import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_GET_PROFILE_API_FORMAT_KEY;
import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_SEARCH_PROFILES_API_FORMAT;
import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_SEARCH_PROFILES_API_FORMAT_KEY;
import static de.skillix.keycloak.spi.userprovider.Constants.USERNAME_BASICAUTH_KEY;

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
            SKILLIX_BASE_URL_KEY,
            "Base URL",
            "Base URL of the Skillix API",
            ProviderConfigProperty.STRING_TYPE,
            SKILLIX_BASE_URL,
            Collections.emptyList())
        .property(
            USERNAME_BASICAUTH_KEY,
            "Username BasicAuth",
            "Username for BasicAuth at the Skillix API",
            ProviderConfigProperty.STRING_TYPE,
            "",
            Collections.emptyList())
        .property(
            PASSWORD_BASICAUTH_KEY,
            "Password BasicAuth",
            "Password for BasicAuth at the Skillix API",
            ProviderConfigProperty.PASSWORD,
            "",
            Collections.emptyList())
        .property(
            SKILLIX_API_VERSION_KEY,
            "API Version",
            "Version of the Skillix API",
            ProviderConfigProperty.STRING_TYPE,
            SKILLIX_API_VERSION,
            Collections.emptyList())
        .property(
            SKILLIX_GET_PROFILE_API_FORMAT_KEY,
            "GET Profile URL Format",
            "The format of GET profile URL. "
                + "Three placeholders (symbolized by %s) are expected: "
                + "base url, version and user identifier",
            ProviderConfigProperty.STRING_TYPE,
            SKILLIX_GET_PROFILE_API_FORMAT,
            Collections.emptyList())
        .property(
            SKILLIX_SEARCH_PROFILES_API_FORMAT_KEY,
            "Search Profiles URL Format",
            "The format of search profiles URL. "
                + "Three placeholders (symbolized by %s) are expected: "
                + "base url, version and query parameters",
            ProviderConfigProperty.STRING_TYPE,
            SKILLIX_SEARCH_PROFILES_API_FORMAT,
            Collections.emptyList())
        .build();
  }

  @Override
  public void validateConfiguration(
      KeycloakSession session, RealmModel realm, ComponentModel config)
      throws ComponentValidationException {
    if (StringUtils.isBlank(config.get(SKILLIX_BASE_URL_KEY))) {
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
    if (StringUtils.isBlank(config.get(SKILLIX_GET_PROFILE_API_FORMAT_KEY))) {
      throw new ComponentValidationException("GET profile URL format of Skillix API must be provided");
    }
    if (StringUtils.isBlank(config.get(SKILLIX_SEARCH_PROFILES_API_FORMAT_KEY))) {
      throw new ComponentValidationException("Search profile URL format of Skillix API must be provided");
    }
  }
}
