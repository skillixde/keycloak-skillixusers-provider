package de.skillix.keycloak.spi.userprovider;

public final class Constants {
  public static final String PROVIDER_ID = "skillix";
  //Default Skillix APIs values
  public static final String SKILLIX_BASE_URL_DEFAULT = "https://api.skillix.dev";
  public static final String SKILLIX_API_VERSION_DEFAULT = "0";
  public static final String SKILLIX_GET_PROFILE_API_FORMAT_DEFAULT = "%s/v%s/iam-profile/%s";
  public static final String SKILLIX_SEARCH_PROFILES_API_FORMAT_DEFAULT = "%s/v%s/iam-profile/search?%s";
  public static final String SKILLIX_COUNT_PROFILES_API_FORMAT_DEFAULT = "%s/v%s/iam-profile/count?%s";
  //Skillix Keys for Keycloak providers
  public static final String SKILLIX_BASE_URL_KEY = "skillixApiBaseUrl";
  public static final String BEARER_TOKEN_KEY = "skillixApiBearerToken";
  public static final String SKILLIX_API_VERSION_KEY = "skillixApiVersion";
  public static final String SKILLIX_GET_PROFILE_API_FORMAT_KEY = "skillixGetProfileApiFormat";
  public static final String SKILLIX_SEARCH_PROFILES_API_FORMAT_KEY = "skillixSearchProfilesApiFormat";
  public static final String SKILLIX_COUNT_PROFILES_API_FORMAT_KEY = "skillixCountProfilesApiFormat";
}
