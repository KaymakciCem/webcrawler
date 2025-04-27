package com.ecoiso.webcrawler;

import com.ecoiso.webcrawler.util.UrlUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageFetcher {

    private static final String URL_TAG_REGEX = "<a\\s+[^>]*href=[\"']([^\"']*)[\"']";

    private final String domainAddress;

    public PageFetcher(String domainAddress) {
        this.domainAddress = domainAddress;
    }

    public Set<String> fetchLinks(final HttpURLConnection connection, ConcurrentMap<String, Boolean> visited) throws IOException {
        final Set<String> links = new HashSet<>();

        try (InputStream inputStream = connection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = Pattern.compile(URL_TAG_REGEX).matcher(line);
                while (matcher.find()) {
                    String matcherGroupUrl = matcher.group(1);
                    if (UrlUtils.isSameDomain(domainAddress, UrlUtils.getHost(matcherGroupUrl)) && UrlUtils.isHttpUrl(matcherGroupUrl) && !visited.containsKey(matcherGroupUrl)) {
                        links.add(matcherGroupUrl);
                    }
                }
            }
        } catch (Exception e) {
//            System.out.println("Exception: " + e.getMessage());
        }
        return links;
    }

}
