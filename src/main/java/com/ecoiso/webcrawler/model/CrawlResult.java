package com.ecoiso.webcrawler.model;

import java.util.Set;

public class CrawlResult {
    private final Set<String> visitedUrls;
    private final long executionTime;
    private final int totalPages;
    private final double pagesPerSecond;

    public CrawlResult(Set<String> visitedUrls, long executionTime) {
        this.visitedUrls = visitedUrls;
        this.executionTime = executionTime;
        this.totalPages = visitedUrls.size();
        this.pagesPerSecond = (double) totalPages / (executionTime / 1000.0);
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

    public double getPagesPerSecond() {
        return pagesPerSecond;
    }
}
