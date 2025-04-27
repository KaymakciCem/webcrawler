package com.ecoiso.webcrawler.config;

public class CrawlerConfig {

    private final int maxThreads;
    private final int connectionTimeout;
    private final int readTimeout;

    private CrawlerConfig(Builder builder) {
        this.maxThreads = builder.maxThreads;
        this.connectionTimeout = builder.connectionTimeout;
        this.readTimeout = builder.readTimeout;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public static class Builder {
        private int maxThreads = 100;
        private int connectionTimeout = 5000;
        private int readTimeout = 5000;

        public Builder maxThreads(int maxThreads) {
            this.maxThreads = maxThreads;
            return this;
        }

        public Builder connectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder readTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public CrawlerConfig build() {
            return new CrawlerConfig(this);
        }
    }
}
