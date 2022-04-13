package de.skillix.keycloak.spi.userprovider;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.keycloak.broker.provider.util.SimpleHttp.Response;
import org.keycloak.component.ComponentModel;
import org.keycloak.connections.httpclient.HttpClientProvider;
import org.keycloak.models.KeycloakSession;

import javax.ws.rs.WebApplicationException;
import java.util.List;

import static de.skillix.keycloak.spi.userprovider.Constants.BEARER_TOKEN_KEY;
import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_BASE_URL_KEY;
import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_API_VERSION_KEY;
import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_GET_PROFILE_API_FORMAT_KEY;
import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_SEARCH_PROFILES_API_FORMAT_KEY;
import static org.keycloak.broker.provider.util.SimpleHttp.doGet;

@Slf4j
@RequiredArgsConstructor
public class SkillixApiClientImpl implements SkillixApiClient {

    private final CloseableHttpClient httpClient;
    private final String baseUrl;
    private final String apiVersion;
    private final String bearerToken;
    private final String skillixGetProfileApiFormat;
    private final String skillixSearchProfilesApiFormat;

    public SkillixApiClientImpl(KeycloakSession session, ComponentModel model){
        this.httpClient = session.getProvider(HttpClientProvider.class).getHttpClient();
        this.baseUrl = model.get(SKILLIX_BASE_URL_KEY);
        this.apiVersion = model.get(SKILLIX_API_VERSION_KEY);
        this.bearerToken = model.get(BEARER_TOKEN_KEY);
        this.skillixGetProfileApiFormat = model.get(SKILLIX_GET_PROFILE_API_FORMAT_KEY);
        this.skillixSearchProfilesApiFormat = model.get(SKILLIX_SEARCH_PROFILES_API_FORMAT_KEY);
    }

    @Override
    @SneakyThrows
    public SkillixUser getSkillixProfileByIdentity(String identity) {
        log.info("GET Skillix profile by identity: {}", identity);
        String requestUrl = String.format(skillixGetProfileApiFormat, baseUrl, apiVersion, identity);
        return executeHttpGetRequest(requestUrl).asJson(SkillixUser.class);
    }

    @Override
    @SneakyThrows
    public List<SkillixUser> searchSkillixProfiles(String queryParams) {
        log.info("GET Skillix profiles by query: {}", queryParams);
        String requestUrl = String.format(skillixSearchProfilesApiFormat, baseUrl, apiVersion, queryParams);
        return executeHttpGetRequest(requestUrl).asJson(new TypeReference<>() {});
    }

    @SneakyThrows
    private Response executeHttpGetRequest(String url) {
        log.info("executing HTTP GET request: {}", url);
        //Response response = doGet(url, httpClient).auth(bearerToken).asResponse();
        Response response = doGet(url, httpClient).asResponse();
        if (response.getStatus() != 200) {
            RuntimeException cause = new RuntimeException("HTTP GET request was not successful: " + url);
            throw new WebApplicationException(cause, response.getStatus());
        }
        return response;
    }
}
