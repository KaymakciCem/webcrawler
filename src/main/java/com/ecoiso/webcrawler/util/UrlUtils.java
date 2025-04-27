package com.ecoiso.webcrawler.util;

import java.net.URI;
import java.net.URL;

public class UrlUtils {

    private UrlUtils() { }

    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getHost(String url) {
        final URI uri = URI.create(url);
        return uri.getHost();
    }

    public static boolean isSameDomain(final String domainAddress1,  String domainAddress2) {
        return domainAddress1.equals(domainAddress2);
    }

    public static boolean isHttpUrl(String url) {
        return url != null && url.startsWith("http");
    }
}