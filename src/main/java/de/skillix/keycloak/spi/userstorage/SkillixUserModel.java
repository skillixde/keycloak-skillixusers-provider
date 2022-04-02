package de.skillix.keycloak.spi.userstorage;

import lombok.Getter;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.adapter.AbstractUserAdapter;

import java.util.List;
import java.util.Map;

@Getter
class SkillixUserModel extends AbstractUserAdapter {

  private final String uuid;
  private final String email;
  private final String firstName;
  private final String lastName;
  private final List<String> roles;
  private final boolean emailVerified;
  private final boolean enabled;

  public SkillixUserModel(
      KeycloakSession session,
      RealmModel realm,
      ComponentModel storageProviderModel,
      String uuid,
      String email,
      String firstName,
      String lastName,
      List<String> roles,
      boolean emailVerified,
      boolean enabled) {
    super(session, realm, storageProviderModel);
    this.uuid = uuid;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.roles = roles;
    this.emailVerified = emailVerified;
    this.enabled = enabled;
  }

  @Override
  public String getUsername() {
    return uuid;
  }

  @Override
  public Map<String, List<String>> getAttributes() {
    MultivaluedHashMap<String, String> attributes = new MultivaluedHashMap<>();
    attributes.add(UserModel.USERNAME, getUsername());
    attributes.add(UserModel.EMAIL, getEmail());
    attributes.add(UserModel.FIRST_NAME, getFirstName());
    attributes.add(UserModel.LAST_NAME, getLastName());
    attributes.add(UserModel.EMAIL_VERIFIED, String.valueOf(isEmailVerified()));
    attributes.add(UserModel.ENABLED, String.valueOf(isEnabled()));
    attributes.add("roles", String.join(",", getRoles()));
    return attributes;
  }

  static class Builder {
    private final KeycloakSession session;
    private final RealmModel realm;
    private final ComponentModel storageProviderModel;
    private final String uuid;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;
    private boolean emailVerified;
    private boolean enabled;

    Builder(
        KeycloakSession session,
        RealmModel realm,
        ComponentModel storageProviderModel,
        String uuid) {
      this.session = session;
      this.realm = realm;
      this.storageProviderModel = storageProviderModel;
      this.uuid = uuid;
    }

    SkillixUserModel.Builder email(String email) {
      this.email = email;
      return this;
    }

    SkillixUserModel.Builder firstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    SkillixUserModel.Builder lastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    SkillixUserModel.Builder roles(List<String> roles) {
      this.roles = roles;
      return this;
    }

    SkillixUserModel.Builder isEmailVerified(boolean emailVerified) {
      this.emailVerified = emailVerified;
      return this;
    }

    SkillixUserModel.Builder enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    SkillixUserModel build() {
      return new SkillixUserModel(
          session, realm, storageProviderModel, uuid, email, firstName, lastName, roles, emailVerified, enabled);
    }
  }
}
