package de.skillix.keycloak.spi.userstorage;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static de.skillix.keycloak.spi.userstorage.QueryParamUtils.parseQueryParams;
import static de.skillix.keycloak.spi.userstorage.SkillixApiClient.getSkillixProfileByIdentity;
import static de.skillix.keycloak.spi.userstorage.SkillixApiClient.searchSkillixProfiles;

@Slf4j
@AllArgsConstructor
public class SkillixUserStorageProvider
    implements UserStorageProvider, UserLookupProvider.Streams, UserQueryProvider.Streams {

  private final KeycloakSession session;
  private final ComponentModel model;

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

  private UserModel mapToUserModel(RealmModel realm, SkillixProfileApiResponse apiResponse) {
    return new SkillixUserModel.Builder(session, realm, model, apiResponse.getUuid())
        .email(apiResponse.getEmail())
        .firstName(apiResponse.getFirstName())
        .lastName(apiResponse.getLastName())
        .roles(apiResponse.getRoles())
        .isEmailVerified(apiResponse.isEmailVerified())
        .build();
  }

  @SneakyThrows
  private UserModel getSkillixUserModelByIdentity(RealmModel realm, String identity) {
    HttpResponse<byte[]> httpResponse = getSkillixProfileByIdentity(identity);
    if(httpResponse.statusCode() != 200) return null;
    SkillixProfileApiResponse profile = SerializationUtils.deserialize(httpResponse.body());
    return mapToUserModel(realm, profile);
  }

  @SneakyThrows
  private Stream<UserModel> searchSkillixUserModelStream(RealmModel realm, String queryParams) {
    HttpResponse<byte[]> httpResponse = searchSkillixProfiles(queryParams);
    if(httpResponse.statusCode() != 200) return null;
    List<SkillixProfileApiResponse> profiles = SerializationUtils.deserialize(httpResponse.body());
    return profiles.stream().map(profile -> mapToUserModel(realm, profile));
  }
}
