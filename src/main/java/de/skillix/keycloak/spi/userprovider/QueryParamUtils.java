package de.skillix.keycloak.spi.userprovider;

import lombok.SneakyThrows;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.keycloak.utils.StringUtil.isBlank;
import static org.keycloak.utils.StringUtil.isNotBlank;

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
        if (isBlank(attrName) || isBlank(attrValue)) return "";
        return attrName + "=" + attrValue;
    }

    public static String parseQueryParams(String search, Integer firstResult, Integer maxResults) {

        StringBuilder stringBuilder = new StringBuilder();
        boolean appendQuote = false;

        if (isNotBlank(search)) {
            stringBuilder.append("query=");
            stringBuilder.append(search);
            appendQuote = true;
        }
        if (firstResult != null) {
            if (appendQuote) {
                stringBuilder.append("&");
            }
            stringBuilder.append("offset=");
            stringBuilder.append(firstResult);
            appendQuote = true;
        }
        if (maxResults != null) {
            if (appendQuote) {
                stringBuilder.append("&");
            }
            stringBuilder.append("size=");
            stringBuilder.append(maxResults);
        }
        return stringBuilder.toString();
    }

    public static boolean isNumeric(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String formatQueryParams(String format, String baseUrl, String apiVersion, String param) {
        if (isBlank(param)) {
            String url = String.format(format, baseUrl, apiVersion, "");
            return url.substring(0, url.length() - 1);
        }
        return String.format(format, baseUrl, apiVersion, param);
    }

}
