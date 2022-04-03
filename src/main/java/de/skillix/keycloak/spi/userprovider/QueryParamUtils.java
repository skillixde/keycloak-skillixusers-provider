package de.skillix.keycloak.spi.userprovider;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class QueryParamUtils {

  @SneakyThrows
  public static String parseQueryParams(Map<String, String> params) {
    if (params == null) return "";
    return params.entrySet().stream()
        .map(
            entry ->
                URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8)
                    + "="
                    + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
        .reduce((e1, e2) -> e1 + "&" + e2)
        .orElse("");
  }

  public static String parseQueryParams(String attrName, String attrValue) {
    if (StringUtils.isBlank(attrName) || StringUtils.isBlank(attrValue)) return "";
    return attrName + "=" + attrValue;
  }

  public static String parseQueryParams(String search, Integer firstResult, Integer maxResults) {

    StringBuilder stringBuilder = new StringBuilder();
    boolean appendQuote = false;

    if (StringUtils.isNotBlank(search)) {
      stringBuilder.append(search);
      appendQuote = true;
    }
    if (firstResult != null) {
      if(appendQuote){
        stringBuilder.append("&");
      }
      stringBuilder.append("offset=");
      stringBuilder.append(firstResult);
      appendQuote = true;
    }
    if (maxResults != null) {
      if(appendQuote){
        stringBuilder.append("&");
      }
      stringBuilder.append("size=");
      stringBuilder.append(maxResults);
    }
    return stringBuilder.toString();
  }
}
