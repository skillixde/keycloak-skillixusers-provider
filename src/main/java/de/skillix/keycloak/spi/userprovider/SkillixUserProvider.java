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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.skillix.keycloak.spi.userprovider.QueryParamUtils.parseQueryParams;

@Slf4j
public class SkillixUserProvider implements UserStorageProvider, UserLookupProvider.Streams, UserQueryProvider.Streams {

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
    log.info("getUserById(realm, id:{}) called", id);
    StorageId sid = new StorageId(id);
    String externalId = sid.getExternalId();
    return getUserByUsername(externalId, realm);
  }

  @Override
  @SneakyThrows
  public UserModel getUserByUsername(RealmModel realm, String username) {
    log.info("getUserByUsername(realm, username:{}) called", username);
    return getSkillixUserModelByIdentity(realm, username);
  }

  @Override
  public UserModel getUserByEmail(RealmModel realm, String email) {
    log.info("getUserByEmail(realm, email:{}) called", email);
    return getSkillixUserModelByIdentity(realm, email);
  }

  @Override
  public Stream<UserModel> getUsersStream(
      RealmModel realm, Integer firstResult, Integer maxResults) {
    log.info("getUsersStream(realm, firstResult:{}, maxResults:{}) method called", firstResult, maxResults);
    String queryParams = parseQueryParams(null, firstResult, maxResults);
    return searchSkillixUserModelStream(realm, queryParams);
  }

  @Override
  public Stream<UserModel> searchForUserStream(
      RealmModel realm, String search, Integer firstResult, Integer maxResults) {
    log.info("searchForUserStream(realm, search:{}, firstResult:{}, maxResults:{}) called", search, firstResult, maxResults);
    String queryParams = parseQueryParams(search, firstResult, maxResults);
    return searchSkillixUserModelStream(realm, queryParams);
  }

  @Override
  public Stream<UserModel> searchForUserStream(
      RealmModel realm, Map<String, String> params, Integer firstResult, Integer maxResults) {
    log.info("searchForUserStream(realm, params, firstResult:{}, maxResults:{}) called", firstResult, maxResults);
    String search = parseQueryParams(params);
    String queryParams = parseQueryParams(search, firstResult, maxResults);
    return searchSkillixUserModelStream(realm, queryParams);
  }

  @Override
  public Stream<UserModel> searchForUserByUserAttributeStream(
      RealmModel realm, String attrName, String attrValue) {
    log.info("searchForUserByUserAttributeStream(realm, attrName:{}, attrValue:{}) called", attrName, attrValue);
    String search = parseQueryParams(attrName, attrValue);
    String queryParams = parseQueryParams(search, null, null);
    return searchSkillixUserModelStream(realm, queryParams);
  }

  @Override
  public Stream<UserModel> getGroupMembersStream(
      RealmModel realm, GroupModel group, Integer firstResult, Integer maxResults) {
    log.info("getGroupMembersStream(realm, group, firstResult:{}, maxResults:{}) called", firstResult, firstResult);
    return Stream.empty();
  }

  @Override
  public int getUsersCount(RealmModel realm, Map<String, String> params) {
    log.info("getUsersCount(realm, params) called");
    String queryParams = parseQueryParams(params);
    UserCounter counter = apiClient.countUsers(queryParams);
    return counter.getTotalCount();
  }

  @Override
  public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
    log.info("getUsers(realm, firstResult:{}, maxResults:{}) called", firstResult, maxResults);
    String queryParams = parseQueryParams(null, firstResult, maxResults);
    return apiClient.searchSkillixProfiles(queryParams)
            .stream()
            .map(skillixUser -> mapToUserModel(realm, skillixUser))
            .collect(Collectors.toList());
  }

  @Override
  public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults) {
    log.info("searchForUser(search:{}, realm, firstResult:{}, maxResults:{}) called", search, firstResult, maxResults);
    String queryParams = parseQueryParams(search, firstResult, maxResults);
    return apiClient.searchSkillixProfiles(queryParams)
            .stream()
            .map(skillixUser -> mapToUserModel(realm, skillixUser))
            .collect(Collectors.toList());
  }

  @Override
  public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
    log.info("searchForUser(params, realm");
    String queryParams = parseQueryParams(params);
    return apiClient.searchSkillixProfiles(queryParams)
            .stream()
            .map(skillixUser -> mapToUserModel(realm, skillixUser))
            .collect(Collectors.toList());
  }

  @Override
  public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult, int maxResults) {
    log.info("searchForUser(params, realm, firstResult:{}, maxResults:{}) called", firstResult, maxResults);
    String queryParams = parseQueryParams(params);
    return apiClient.searchSkillixProfiles(queryParams)
            .stream()
            .map(skillixUser -> mapToUserModel(realm, skillixUser))
            .collect(Collectors.toList());
  }

  @Override
  public List<UserModel> searchForUser(String search, RealmModel realm) {
    log.info("searchForUser(search:{}, realm) called", search);
    return apiClient.searchSkillixProfiles(search)
            .stream()
            .map(skillixUser -> mapToUserModel(realm, skillixUser))
            .collect(Collectors.toList());
  }

  @Override
  public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm) {
    log.info("searchForUserByUserAttribute(attrName:{}, attrValue:{}, realm) called", attrName, attrValue);
    String search = parseQueryParams(attrName, attrValue);
    return apiClient.searchSkillixProfiles(search)
            .stream()
            .map(skillixUser -> mapToUserModel(realm, skillixUser))
            .collect(Collectors.toList());
  }

  @Override
  public List<UserModel> getUsers(RealmModel realm) {
    log.info("getUsers(realm) called");
    return apiClient.searchSkillixProfiles(null)
            .stream()
            .map(skillixUser -> mapToUserModel(realm, skillixUser))
            .collect(Collectors.toList());
  }

  @Override
  public Stream<UserModel> getUsersStream(RealmModel realm) {
    log.info("getUsersStream(realm) called");
    return apiClient.searchSkillixProfiles(null)
            .stream()
            .map(skillixUser -> mapToUserModel(realm, skillixUser));
  }

  @Override
  public Stream<UserModel> searchForUserStream(RealmModel realm, String search) {
    log.info("searchForUserStream(realm, search:{}) called", search);
    return apiClient.searchSkillixProfiles(search)
            .stream()
            .map(skillixUser -> mapToUserModel(realm, skillixUser));
  }

  @Override
  public Stream<UserModel> searchForUserStream(RealmModel realm, Map<String, String> params) {
    log.info("searchForUserStream(realm, params) called");
    String search = parseQueryParams(params);
    return apiClient.searchSkillixProfiles(search)
            .stream()
            .map(skillixUser -> mapToUserModel(realm, skillixUser));
  }

  private UserModel mapToUserModel(RealmModel realm, SkillixUser apiResponse) {
    return new SkillixUserAdapter.Builder(session, realm, model, apiResponse.getUuid())
        .email(apiResponse.getEmail())
        .firstName(apiResponse.getFirstName())
        .lastName(apiResponse.getLastName())
        .company(apiResponse.getCompany())
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
