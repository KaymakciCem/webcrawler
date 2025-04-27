package com.ecoiso.webcrawler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class CrawlerWorker implements Runnable {

    private final String url;
    private final ConcurrentMap<String, Boolean> visited;
    private final ExecutorService executorPool;
    private final URLConnectionHandler urlConnectionHandler;
    private final PageFetcher pageFetcher;
    private final TaskManager taskManager;
    private final ExecutionLogger executionLogger;

    public CrawlerWorker(String url,
                         ConcurrentMap<String, Boolean> visited,
                         ExecutorService executorPool,
                         URLConnectionHandler urlConnectionHandler,
                         PageFetcher pageFetcher,
                         TaskManager taskManager,
                         ExecutionLogger executionLogger) {
        this.visited = visited;
        this.url = url;
        this.executorPool = executorPool;
        this.urlConnectionHandler = urlConnectionHandler;
        this.pageFetcher = pageFetcher;
        this.taskManager = taskManager;
        this.executionLogger = executionLogger;
    }

    @Override
    public void run() {
        if (visited.containsKey(url)) {
            executionLogger.logInfo(String.format("URL '%s' already visited. %s\n", url, Thread.currentThread().getName()));
            return;
        }

        try {
            HttpURLConnection connection = urlConnectionHandler.openConnection(url);
            Set<String> linksOnPage = pageFetcher.fetchLinks(connection, visited);
            visited.putIfAbsent(url, true);
            executionLogger.logInfo(String.format("Visiting URL: %s thread name %s\n", url, Thread.currentThread().getName()));

            List<Future<?>> tasks = new LinkedList<>();
            for (String nextUrl : linksOnPage) {
                if (!visited.containsKey(nextUrl)) {
                    System.out.printf("NextUrl URL: %s thread name %s\n", nextUrl, Thread.currentThread().getName());

                    CrawlerWorker crawlerWorker = new CrawlerWorker(nextUrl, visited, executorPool,
                            urlConnectionHandler, pageFetcher, taskManager, executionLogger);
                    tasks.add(executorPool.submit(crawlerWorker));
                }
            }

            taskManager.waitForTasks(tasks);
        } catch (IOException e) {
            executionLogger.logError(String.format("Error occurred while accessing URL '%s': %s\n", url, e.getMessage()));
        }
    }

}
