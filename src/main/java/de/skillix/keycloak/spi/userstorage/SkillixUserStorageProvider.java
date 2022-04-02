package de.skillix.keycloak.spi.userstorage;

import lombok.AllArgsConstructor;
import lombok.Data;
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

import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
public class SkillixUserStorageProvider
    implements UserStorageProvider,
        // CredentialInputValidator,
        // UserRegistrationProvider,
        UserLookupProvider.Streams,
        UserQueryProvider.Streams {

  public static final String SKILLIX_GET_PROFILE_API_BY_IDENTITY = "https://api.skillix.dev/v0/profiles/%s";
  public static final String SKILLIX_SEARCH_PROFILES_API = "https://api.skillix.dev/v0/profiles?%s";

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
    return searchSkillixUserModelStream(realm, null, firstResult, maxResults);
  }

  @Override
  public Stream<UserModel> searchForUserStream(
      RealmModel realm, String search, Integer firstResult, Integer maxResults) {
    return searchSkillixUserModelStream(realm, search, firstResult, maxResults);
  }

  /**
   * Searches for user by parameter. If possible, implementations should treat the parameter values as partial match patterns
   * (i.e. in RDMBS terms use LIKE).
   * <p/>
   * Valid parameters are:
   * <ul>
   *     <li>{@link UserModel#FIRST_NAME} - first name (case insensitive string)</li>
   *     <li>{@link UserModel#LAST_NAME} - last name (case insensitive string)</li>
   *     <li>{@link UserModel#EMAIL} - email (case insensitive string)</li>
   *     <li>{@link UserModel#USERNAME} - username (case insensitive string)</li>
   *     <li>{@link UserModel#EMAIL_VERIFIED} - search only for users with verified/non-verified email (true/false)</li>
   *     <li>{@link UserModel#ENABLED} - search only for enabled/disabled users (true/false)</li>
   *     <li>{@link UserModel#IDP_ALIAS} - search only for users that have a federated identity
   *     from idp with the given alias configured (case sensitive string)</li>
   *     <li>{@link UserModel#IDP_USER_ID} - search for users with federated identity with
   *     the given userId (case sensitive string)</li>
   * </ul>
   *
   * Any other parameters will be treated as custom user attributes.
   *
   * This method is used by the REST API when querying users.
   *
   * @param realm a reference to the realm.
   * @param params a map containing the search parameters.
   * @param firstResult first result to return. Ignored if negative, zero, or {@code null}.
   * @param maxResults maximum number of results to return. Ignored if negative or {@code null}.
   * @return a non-null {@link Stream} of users that match the search criteria.
   */
  @Override
  public Stream<UserModel> searchForUserStream(
      RealmModel realm, Map<String, String> params, Integer firstResult, Integer maxResults) {
    String search = fromQueryParams(params);
    return searchSkillixUserModelStream(realm, search, firstResult, maxResults);
  }

  @Override
  public Stream<UserModel> searchForUserByUserAttributeStream(
      RealmModel realm, String attrName, String attrValue) {
    String search = attrName + "=" + attrValue;
    return searchSkillixUserModelStream(realm, search, null, null);
  }

  @Override
  public Stream<UserModel> getGroupMembersStream(
          RealmModel realm, GroupModel group, Integer firstResult, Integer maxResults) {
    return Stream.empty();
  }

  private UserModel buildUserModel(
      RealmModel realm, SkillixUserApiResponse apiResponse) {
    return new SkillixUserModel.Builder(session, realm, model, apiResponse.getUuid())
        .email(apiResponse.getEmail())
        .firstName(apiResponse.getFirstName())
        .lastName(apiResponse.getLastName())
        .roles(apiResponse.getRoles())
            .isEmailVerified(apiResponse.isEmailVerified())
        .build();
  }

  @SneakyThrows
  private static HttpResponse<byte[]> executeHttpGetRequest(URI uri) {
    log.info("executing http request GET {}", uri);
    HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
    HttpClient client = HttpClient.newHttpClient();
    return client.send(request, HttpResponse.BodyHandlers.ofByteArray());
  }

  @SneakyThrows
  private UserModel getSkillixUserModelByIdentity(RealmModel realm, String identity) {
    String getSkillixProfileApiUrl = String.format(SKILLIX_GET_PROFILE_API_BY_IDENTITY, identity);
    HttpResponse<byte[]> response = executeHttpGetRequest(URI.create(getSkillixProfileApiUrl));
    SkillixUserApiResponse apiResponse = SerializationUtils.deserialize(response.body());
    return buildUserModel(realm, apiResponse);
  }

  @SneakyThrows
  private Stream<UserModel> searchSkillixUserModelStream(RealmModel realm, String search, Integer firstResult, Integer maxResults) {
    String queryParams = search + "&offset=" + firstResult + "&limit=" + maxResults;
    String searchProfilesApiUrl = String.format(SKILLIX_SEARCH_PROFILES_API, queryParams);
    HttpResponse<byte[]> apiResponse = executeHttpGetRequest(URI.create(searchProfilesApiUrl));
    List<SkillixUserApiResponse> skillixUserModels = SerializationUtils.deserialize(apiResponse.body());
    return skillixUserModels.stream().map(response -> buildUserModel(realm, response));
  }

  private String fromQueryParams(Map<String, String> params) {
    // TODO
    return "";
  }

  @Data
  public static class SkillixUserApiResponse implements Serializable {
    private String uuid;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;
    private boolean emailVerified;
    private boolean enabled;
  }

}
