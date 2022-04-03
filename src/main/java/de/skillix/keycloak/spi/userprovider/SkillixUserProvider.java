package de.skillix.keycloak.spi.userprovider;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static de.skillix.keycloak.spi.userprovider.QueryParamUtils.parseQueryParams;

@Slf4j
public class SkillixUserProvider
    implements UserStorageProvider,
        // CredentialInputValidator,
        // UserRegistrationProvider,
        UserLookupProvider.Streams,
        UserQueryProvider.Streams {

  private final KeycloakSession session;
  private final ComponentModel model;
  private final SkillixApiClient apiClient;

  public SkillixUserProvider(KeycloakSession session, ComponentModel model) {
    this.session = session;
    this.model = model;
    this.apiClient = new SkillixApiClientImpl(session, model);
  }

  @Override
  public void close() {}

  @Override
  public UserModel getUserById(RealmModel realm, String id) {
    StorageId sid = new StorageId(id);
    String externalId = sid.getExternalId();
    return getUserByUsername(externalId, realm);
  }

  @Override
  @SneakyThrows
  public UserModel getUserByUsername(RealmModel realm, String username) {
    return getSkillixUserModelByIdentity(realm, username);
  }

  @Override
  public UserModel getUserByEmail(RealmModel realm, String email) {
    return getSkillixUserModelByIdentity(realm, email);
  }

  @Override
  public Stream<UserModel> getUsersStream(
      RealmModel realm, Integer firstResult, Integer maxResults) {
    String queryParams = parseQueryParams(null, firstResult, maxResults);
    return searchSkillixUserModelStream(realm, queryParams);
  }

  @Override
  public Stream<UserModel> searchForUserStream(
      RealmModel realm, String search, Integer firstResult, Integer maxResults) {
    String queryParams = parseQueryParams(search, firstResult, maxResults);
    return searchSkillixUserModelStream(realm, queryParams);
  }

  @Override
  public Stream<UserModel> searchForUserStream(
      RealmModel realm, Map<String, String> params, Integer firstResult, Integer maxResults) {
    String search = parseQueryParams(params);
    String queryParams = parseQueryParams(search, firstResult, maxResults);
    return searchSkillixUserModelStream(realm, queryParams);
  }

  @Override
  public Stream<UserModel> searchForUserByUserAttributeStream(
      RealmModel realm, String attrName, String attrValue) {
    String search = parseQueryParams(attrName, attrValue);
    String queryParams = parseQueryParams(search, null, null);
    return searchSkillixUserModelStream(realm, queryParams);
  }

  @Override
  public Stream<UserModel> getGroupMembersStream(
      RealmModel realm, GroupModel group, Integer firstResult, Integer maxResults) {
    return Stream.empty();
  }

  private UserModel mapToUserModel(RealmModel realm, SkillixUser apiResponse) {
    return new SkillixUserAdapter.Builder(session, realm, model, apiResponse.getUuid())
        .email(apiResponse.getEmail())
        .firstName(apiResponse.getFirstName())
        .lastName(apiResponse.getLastName())
        .roles(apiResponse.getRoles())
        .isEmailVerified(apiResponse.isEmailVerified())
        .enabled(apiResponse.isEnabled())
        .build();
  }

  @SneakyThrows
  private UserModel getSkillixUserModelByIdentity(RealmModel realm, String identity) {
    SkillixUser profile = apiClient.getSkillixProfileByIdentity(identity);
    if (profile == null) return null;
    return mapToUserModel(realm, profile);
  }

  @SneakyThrows
  private Stream<UserModel> searchSkillixUserModelStream(RealmModel realm, String queryParams) {
    List<SkillixUser> profiles = apiClient.searchSkillixProfiles(queryParams);
    if (profiles == null || profiles.isEmpty()) return null;
    return profiles.stream().map(profile -> mapToUserModel(realm, profile));
  }
}
