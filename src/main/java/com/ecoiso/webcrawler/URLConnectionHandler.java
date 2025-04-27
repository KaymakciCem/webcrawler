package com.ecoiso.webcrawler;

import com.ecoiso.webcrawler.config.CrawlerConfig;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class URLConnectionHandler {

    private final CrawlerConfig crawlerConfig;

    private final ExecutionLogger logger;

    public URLConnectionHandler(ExecutionLogger executionLogger, CrawlerConfig crawlerConfig) {
        this.logger = executionLogger;
        this.crawlerConfig = crawlerConfig;
    }

    public HttpURLConnection openConnection(final String urlText) throws IOException {
        final URL url = URI.create(urlText).toURL();
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(crawlerConfig.getConnectionTimeout());
        connection.setReadTimeout(crawlerConfig.getReadTimeout());
        connection.connect();

        if (checkHttpStatusNOTOK(connection.getResponseCode())) {
            logger.logError("HTTP error: " + connection.getResponseCode());
            throw new IOException("HTTP error: " + connection.getResponseCode());
        }

        if (isInvalidContentType(connection)) {
            logger.logError("Invalid content type: " + connection.getContentType());
            throw new IOException("Invalid content type: " + connection.getContentType());
        }

        return connection;
    }

    private boolean checkHttpStatusNOTOK(int responseCode) {
        return responseCode != 200;
    }

    private boolean isInvalidContentType(final HttpURLConnection connection) {
        final String contentType = connection.getContentType();
        return contentType != null && !contentType.startsWith("text/");
    }
}
