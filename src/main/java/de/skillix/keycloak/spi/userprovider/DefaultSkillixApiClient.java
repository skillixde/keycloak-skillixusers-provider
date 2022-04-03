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

import static de.skillix.keycloak.spi.userprovider.SkillixConstants.PASSWORD_BASICAUTH_KEY;
import static de.skillix.keycloak.spi.userprovider.SkillixConstants.BASE_URL_KEY;
import static de.skillix.keycloak.spi.userprovider.SkillixConstants.SKILLIX_API_VERSION_KEY;
import static de.skillix.keycloak.spi.userprovider.SkillixConstants.USERNAME_BASICAUTH_KEY;
import static org.keycloak.broker.provider.util.SimpleHttp.*;

@Slf4j
public class DefaultSkillixApiClient implements SkillixApiClient {

    private static final String SKILLIX_GET_PROFILE_API_PATTERN = "%s/v%s/profiles/%s";
    private static final String SKILLIX_SEARCH_PROFILES_API_PATTERN = "%s/v%s/profiles?%s";

    private final CloseableHttpClient httpClient;
    private final String baseUrl;
    private final String apiVersion;
    private final String basicUsername;
    private final String basicPassword;

    public DefaultSkillixApiClient(KeycloakSession session, ComponentModel model){
        this.httpClient = session.getProvider(HttpClientProvider.class).getHttpClient();
        this.baseUrl = model.get(BASE_URL_KEY);
        this.apiVersion = model.get(SKILLIX_API_VERSION_KEY);
        this.basicUsername = model.get(USERNAME_BASICAUTH_KEY);
        this.basicPassword = model.get(PASSWORD_BASICAUTH_KEY);
    }

    @Override
    @SneakyThrows
    public SkillixUserApiResponse getSkillixProfileByIdentity(String identity) {
        String requestUrl = String.format(SKILLIX_GET_PROFILE_API_PATTERN, baseUrl, apiVersion, identity);
        return executeHttpGetRequest(requestUrl).asJson(SkillixUserApiResponse.class);
    }

    @Override
    @SneakyThrows
    public List<SkillixUserApiResponse> searchSkillixProfiles(String queryParams) {
        String requestUrl = String.format(SKILLIX_SEARCH_PROFILES_API_PATTERN, baseUrl, apiVersion, queryParams);
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
