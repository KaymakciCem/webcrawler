package com.ecoiso.webcrawler;

import com.ecoiso.webcrawler.config.CrawlerConfig;
import com.ecoiso.webcrawler.model.CrawlResult;
import com.ecoiso.webcrawler.service.CrawlerService;
import com.ecoiso.webcrawler.util.UrlUtils;

import java.net.URI;
import java.util.Scanner;

public class Main {

    public static final int CONNECTION_TIMEOUT = 5000;
    public static final int READ_TIMEOUT = 5000;
    public static final int NUMBER_OF_THREADS = 100;

    public static void main(String[] args) {

        String userInput = getUserInput();

        final CrawlerConfig crawlerConfig = new CrawlerConfig.Builder()
                .maxThreads(NUMBER_OF_THREADS)
                .connectionTimeout(CONNECTION_TIMEOUT)
                .readTimeout(READ_TIMEOUT)
                .build();

        final ExecutionLogger executionLogger = new ExecutionLogger();
        final URLConnectionHandler urlConnectionHandler = new URLConnectionHandler(executionLogger, crawlerConfig);
        final PageFetcher pageFetcher = new PageFetcher(URI.create(userInput).getHost());
        final TaskManager taskManager = new TaskManager();

        CrawlerService crawlerService = new CrawlerService(
                executionLogger,
                urlConnectionHandler,
                pageFetcher,
                taskManager,
                crawlerConfig.getMaxThreads()
        );

        CrawlResult result = crawlerService.crawl(userInput);
        executionLogger.printStatistics(result);
    }

    private static String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        String userInput;

        while (true) {
            System.out.print("Enter a URL to crawl: ");
            userInput = scanner.nextLine();

            try {
                UrlUtils.isValidUrl(userInput);
                System.out.println("Valid URL: " + userInput);
                break;
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid URL. Please try again. Sample url => http://example.com");
            }
        }

        scanner.close();
        return userInput;
    }
}