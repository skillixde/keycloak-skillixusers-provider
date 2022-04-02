package de.skillix.keycloak.spi.userstorage;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class SkillixApiClient {

    private static final String SKILLIX_GET_PROFILE_API = "https://api.skillix.dev/v0/profiles/%s";
    private static final String SKILLIX_SEARCH_PROFILES_API = "https://api.skillix.dev/v0/profiles?%s";

    public static HttpResponse<byte[]> getSkillixProfileByIdentity(String identity) {
        String getSkillixProfileApiUrl = String.format(SKILLIX_GET_PROFILE_API, identity);
        return executeHttpGetRequest(URI.create(getSkillixProfileApiUrl));
    }

    public static HttpResponse<byte[]> searchSkillixProfiles(String queryParams) {
        String searchProfilesApiUrl = String.format(SKILLIX_SEARCH_PROFILES_API, queryParams);
        return executeHttpGetRequest(URI.create(searchProfilesApiUrl));
    }

    @SneakyThrows
    private static HttpResponse<byte[]> executeHttpGetRequest(URI uri) {
        log.info("executing http request GET {}", uri);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpClient client = HttpClient.newHttpClient();
        return client.send(request, HttpResponse.BodyHandlers.ofByteArray());
    }
}
