package de.skillix.keycloak.spi.userprovider;

import lombok.Getter;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.adapter.AbstractUserAdapter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
class SkillixUserAdapter extends AbstractUserAdapter.Streams {

  private final SkillixUser skillixUser;

  private SkillixUserAdapter(
      KeycloakSession session,
      RealmModel realm,
      ComponentModel model,
      SkillixUser skillixUser) {
    super(session, realm, model);
    this.skillixUser = skillixUser;
  }

  @Override
  public String getUsername() {
    return skillixUser.getUuid();
  }

  public String getEmail() {
    return skillixUser.getEmail();
  }

  public String getFirstName() {
    return skillixUser.getFirstName();
  }

  public String getLastName() {
    return skillixUser.getLastName();
  }

  public boolean isEmailVerified() {
    return skillixUser.isEmailVerified();
  }

  public boolean isEnabled() {
    return skillixUser.isEnabled();
  }

  public List<String> getRoles() {
    return skillixUser.getRoles();
  }

  public String getCompany() {
    return skillixUser.getCompany();
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
    attributes.add("company", getCompany());
    return attributes;
  }

  @Override
  protected Set<GroupModel> getGroupsInternal() {
/*    if (skillixUser.getGroups() != null) {
      return skillixUser.getGroups().stream().map(SkillixGroupModel::new).collect(Collectors.toSet());
    }*/
    return Set.of();
  }

  @Override
  protected Set<RoleModel> getRoleMappingsInternal() {
    if (skillixUser.getRoles() != null) {
      return skillixUser.getRoles()
              .stream()
              .map(roleName -> new SkillixRoleModel(roleName, realm))
              .collect(Collectors.toSet());
    }
    return Set.of();
  }

  static class Builder {
    private final KeycloakSession session;
    private final RealmModel realm;
    private final ComponentModel model;
    private final SkillixUser skillixUser;

    Builder(
        KeycloakSession session,
        RealmModel realm,
        ComponentModel model,
        String username) {
      this.session = session;
      this.realm = realm;
      this.model = model;
      skillixUser = new SkillixUser(username);
    }

    SkillixUserAdapter.Builder email(String email) {
      this.skillixUser.setEmail(email);
      return this;
    }

    SkillixUserAdapter.Builder firstName(String firstName) {
      this.skillixUser.setFirstName(firstName);
      return this;
    }

    SkillixUserAdapter.Builder lastName(String lastName) {
      this.skillixUser.setLastName(lastName);
      return this;
    }

    SkillixUserAdapter.Builder company(String company) {
      this.skillixUser.setCompany(company);
      return this;
    }

    SkillixUserAdapter.Builder roles(List<String> roles) {
      this.skillixUser.setRoles(roles);
      return this;
    }

    SkillixUserAdapter.Builder isEmailVerified(boolean emailVerified) {
      this.skillixUser.setEmailVerified(emailVerified);
      return this;
    }

    SkillixUserAdapter.Builder enabled(boolean enabled) {
      this.skillixUser.setEnabled(enabled);
      return this;
    }

    SkillixUserAdapter build() {
      return new SkillixUserAdapter(session, realm, model, skillixUser);
    }

  }
}
