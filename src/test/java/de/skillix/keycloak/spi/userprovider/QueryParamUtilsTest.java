package de.skillix.keycloak.spi.userprovider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_GET_PROFILE_API_FORMAT;
import static de.skillix.keycloak.spi.userprovider.Constants.SKILLIX_SEARCH_PROFILES_API_FORMAT;

class QueryParamUtilsTest {

  @Test
  void testParseQueryParamsFromMapToString() {
    Map<String, String> params = new HashMap<>();
    params.put("offset", "100");
    params.put("size", "50");
    params.put("order", "asc");
    String query = QueryParamUtils.parseQueryParams(params);
    Assertions.assertEquals("offset=100&size=50&order=asc", query);
  }

  @Test
  void testParseQueryParamsFromKeyValueToString() {
    String query = QueryParamUtils.parseQueryParams("offset", "100");
    Assertions.assertEquals("offset=100", query);
  }

  @Test
  void testParseQueryParamsFromSearchFirstResultAndMaxResults() {
    String query = QueryParamUtils.parseQueryParams("uuid=abc&first=name", 100, 50);
    Assertions.assertEquals("uuid=abc&first=name&offset=100&size=50", query);
  }

  @Test
  void testParseQueryParamsFromEmptySearchFirstResultAndMaxResults() {
    String query = QueryParamUtils.parseQueryParams("", 100, 50);
    Assertions.assertEquals("offset=100&size=50", query);
  }

  @Test
  void testParseQueryParamsFromEmptySearchAndFirstResult() {
    String query = QueryParamUtils.parseQueryParams("", 100, null);
    Assertions.assertEquals("offset=100", query);
  }

  @Test
  void testParseQueryParamsFromEmptySearchAndMaxResults() {
    String query = QueryParamUtils.parseQueryParams("", null, 50);
    Assertions.assertEquals("size=50", query);
  }

  @Test
  void testParseQueryParamsFromMaxResults() {
    String query = QueryParamUtils.parseQueryParams(null, null, 50);
    Assertions.assertEquals("size=50", query);
  }

  @Test
  void testParseQueryParamsFromNothing() {
    String query = QueryParamUtils.parseQueryParams(null, null, null);
    Assertions.assertEquals("", query);
  }

  @ParameterizedTest
  @CsvSource({
    "https://api.skillix.dev,0,a231-1902-0913f",
    "https://api.skillix.de,1,info@mail.com"
  })
  void testSkillixGetProfileApiFormat(String baseUrl, String apiVersion, String identifier) {
    String actual = String.format(SKILLIX_GET_PROFILE_API_FORMAT, baseUrl, apiVersion, identifier);
    Assertions.assertEquals(baseUrl + "/v" + apiVersion + "/profiles/" + identifier, actual);
  }

  @ParameterizedTest
  @CsvSource({
    "https://api.skillix.dev,0,offset=100&size=50",
    "https://api.skillix.de,1,offset=100&size=50"
  })
  void testSkillixSearchProfilesApiFormat(String baseUrl, String apiVersion, String query) {
    String actual = String.format(SKILLIX_SEARCH_PROFILES_API_FORMAT, baseUrl, apiVersion, query);
    Assertions.assertEquals(baseUrl + "/v" + apiVersion + "/profiles?" + query, actual);
  }
}
