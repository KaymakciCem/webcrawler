package com.ecoiso.webcrawler.model;

import java.util.Set;

public class CrawlResult {
    private final Set<String> visitedUrls;
    private final long executionTime;
    private final int totalPages;

    public CrawlResult(Set<String> visitedUrls, long executionTime) {
        this.visitedUrls = visitedUrls;
        this.executionTime = executionTime;
        this.totalPages = visitedUrls.size();
    }

    public Set<String> getVisitedUrls() {
        return visitedUrls;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
