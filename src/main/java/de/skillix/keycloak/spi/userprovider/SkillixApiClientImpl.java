package de.skillix.keycloak.spi.userprovider;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.keycloak.broker.provider.util.SimpleHttp.Response;
import org.keycloak.component.ComponentModel;
import org.keycloak.connections.httpclient.HttpClientProvider;
import org.keycloak.models.KeycloakSession;

import javax.ws.rs.WebApplicationException;
import java.util.List;

import static de.skillix.keycloak.spi.userprovider.Constants.PASSWORD_BASICAUTH_KEY;
import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_BASE_URL_KEY;
import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_API_VERSION_KEY;
import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_GET_PROFILE_API_FORMAT_KEY;
import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_SEARCH_PROFILES_API_FORMAT_KEY;
import static de.skillix.keycloak.spi.userprovider.Constants.USERNAME_BASICAUTH_KEY;
import static org.keycloak.broker.provider.util.SimpleHttp.doGet;

@Slf4j
public class SkillixApiClientImpl implements SkillixApiClient {

    private final CloseableHttpClient httpClient;
    private final String baseUrl;
    private final String apiVersion;
    private final String basicUsername;
    private final String basicPassword;
    private final String skillixGetProfileApiFormat;
    private final String skillixSearchProfilesApiFormat;

    public SkillixApiClientImpl(KeycloakSession session, ComponentModel model){
        this.httpClient = session.getProvider(HttpClientProvider.class).getHttpClient();
        this.baseUrl = model.get(SKILLIX_BASE_URL_KEY);
        this.apiVersion = model.get(SKILLIX_API_VERSION_KEY);
        this.basicUsername = model.get(USERNAME_BASICAUTH_KEY);
        this.basicPassword = model.get(PASSWORD_BASICAUTH_KEY);
        this.skillixGetProfileApiFormat = model.get(SKILLIX_GET_PROFILE_API_FORMAT_KEY);
        this.skillixSearchProfilesApiFormat = model.get(SKILLIX_SEARCH_PROFILES_API_FORMAT_KEY);
    }

    @Override
    @SneakyThrows
    public SkillixUser getSkillixProfileByIdentity(String identity) {
        String requestUrl = String.format(skillixGetProfileApiFormat, baseUrl, apiVersion, identity);
        return executeHttpGetRequest(requestUrl).asJson(SkillixUser.class);
    }

    @Override
    @SneakyThrows
    public List<SkillixUser> searchSkillixProfiles(String queryParams) {
        String requestUrl = String.format(skillixSearchProfilesApiFormat, baseUrl, apiVersion, queryParams);
        return executeHttpGetRequest(requestUrl).asJson(new TypeReference<>() {});
    }

    @SneakyThrows
    private Response executeHttpGetRequest(String url) {
        Response response = doGet(url, httpClient).authBasic(basicUsername, basicPassword).asResponse();
        if (response.getStatus() != 200) {
            throw new WebApplicationException(response.getStatus());
        }
        return response;
    }
}
