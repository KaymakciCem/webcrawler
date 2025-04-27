package com.ecoiso.webcrawler.service;

import com.ecoiso.webcrawler.*;
import com.ecoiso.webcrawler.model.CrawlResult;

import java.util.List;
import java.util.concurrent.*;

public class CrawlerService {

    private final ExecutionLogger executionLogger;
    private final URLConnectionHandler urlConnectionHandler;
    private final PageFetcher pageFetcher;
    private final TaskManager taskManager;
    private final int maxThreads;

    public CrawlerService(ExecutionLogger executionLogger,
                          URLConnectionHandler urlConnectionHandler,
                          PageFetcher pageFetcher,
                          TaskManager taskManager,
                          int maxThreads) {
        this.executionLogger = executionLogger;
        this.urlConnectionHandler = urlConnectionHandler;
        this.pageFetcher = pageFetcher;
        this.taskManager = taskManager;
        this.maxThreads = maxThreads;
    }

    public CrawlResult crawl(String userInput) {

        long startTime = System.currentTimeMillis();
        final ConcurrentMap<String, Boolean> visited = new ConcurrentHashMap<>();

        try (ExecutorService executor = Executors.newFixedThreadPool(maxThreads)) {
            
            Future<?> task = executor.submit(new CrawlerWorker(userInput, visited, executor,
                    urlConnectionHandler, pageFetcher, taskManager, executionLogger));

            taskManager.waitForTasks(List.of(task));

            executor.shutdown();
            executor.awaitTermination(2, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executionLogger.logError("Main thread interrupted!");
        }

        long endTime = System.currentTimeMillis();
        return new CrawlResult(visited.keySet(), endTime - startTime);
    }

}
