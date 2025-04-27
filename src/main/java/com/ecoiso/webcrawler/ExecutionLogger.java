package com.ecoiso.webcrawler;

import com.ecoiso.webcrawler.model.CrawlResult;

public class ExecutionLogger {

    public void printStatistics(final CrawlResult result) {

        System.out.println("\nNumber of pages crawled: " + result.getVisitedUrls().size());
        System.out.println("Execution time: " + result.getExecutionTime() + "ms");

        double pagesPerSecond = (double) result.getVisitedUrls().size() / (result.getTotalPages() / 1000.0);
        System.out.println("Throughput: " + pagesPerSecond + " pages/sec");

        System.out.println("==============================================================================================");
        System.out.println("Visited pages \n" + result.getVisitedUrls() + "\n");
    }

    public void logInfo(final String message) {
        System.out.println("Log: " + message);
    }

    public void logError(final String message) {
        System.out.println("Error: " + message);
    }

}
